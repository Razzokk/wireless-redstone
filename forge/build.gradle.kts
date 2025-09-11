import mod.gradle.Properties
import mod.gradle.Versions
import org.apache.tools.ant.filters.LineContains
import org.gradle.jvm.tasks.Jar

plugins {
	id("conventions.loader")
	id("net.minecraftforge.gradle")
	id("org.parchmentmc.librarian.forgegradle")
	id("me.modmuss50.mod-publish-plugin")
}

repositories {
	maven("https://maven.shedaniel.me/")	// Cloth config
}

dependencies {
	minecraft("net.minecraftforge", "forge", "${Versions.MINECRAFT}-${Versions.FORGE}")
	annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")
	api("me.shedaniel.cloth", "cloth-config-forge", Versions.CLOTH_CONFIG)
}

val common = project(":common")

sourceSets {
	getByName("main") {
		compileClasspath += common.sourceSets["main"].output
//		runtimeClasspath += common.sourceSets["main"].output
	}
}

minecraft {
	mappings("parchment", "${Versions.PARCHMENT}-${Versions.PARCHMENT_MINECRAFT}")
	copyIdeResources = true // Calls processResources when in dev

	val at = common.file("src/main/resources/META-INF/accesstransformer.cfg")
	if (at.exists()) accessTransformer(at)

	runs {
		configureEach {
			mods {
				create(Properties.MOD_ID) {
					source(sourceSets["main"])
				}
			}

			property("mixin.env.remapRefMap", "true")
			property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
			property("forge.logging.markers", "REGISTRIES")
			property("forge.logging.console.level", "debug")
			jvmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
		}

		create("client") {
			ideaModule("${rootProject.name}.${project.name}.main")
			taskName("Client")
			workingDirectory(file("run/client"))
		}

		create("server") {
			ideaModule("${rootProject.name}.${project.name}.main")
			taskName("Server")
			workingDirectory(file("run/server"))
			args("--nogui")
		}
	}
}

tasks {
	named<ProcessResources>("processResources").configure {
		filesMatching("*.mixins.json") {
			filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
		}
	}
}

publishMods {
	file.set(tasks.named<Jar>("jar").get().archiveFile)
	modLoaders.add("forge")
	changelog = rootProject.file("CHANGELOG.md").readText()
	displayName = "[Forge ${Versions.MINECRAFT}] ${Properties.MOD_ID}-${Versions.MOD}"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-forge"
	type = STABLE

	curseforge {
		projectId = Properties.CURSEFORGE_PROJECT_ID
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")

		minecraftVersions.add(Versions.MINECRAFT)
		javaVersions.add(JavaVersion.toVersion(Versions.JAVA))

		clientRequired = true
		serverRequired = true

		optional("cloth-config")
	}

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		minecraftVersions.add(Versions.MINECRAFT)

		optional("cloth-config")
	}
}

sourceSets.forEach {
	val dir = layout.buildDirectory.dir("sourceSets/${it.name}")
	it.output.setResourcesDir(dir)
	it.java.destinationDirectory.set(dir)
}
