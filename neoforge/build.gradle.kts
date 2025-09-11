import mod.gradle.Properties
import mod.gradle.Versions
import org.apache.tools.ant.filters.LineContains

plugins {
	id("conventions.loader")
	id("net.neoforged.moddev")
	id("me.modmuss50.mod-publish-plugin")
}

repositories {
	maven("https://maven.shedaniel.me/")	// Cloth config
}

dependencies {
	api("me.shedaniel.cloth", "cloth-config-neoforge", Versions.CLOTH_CONFIG)
}

val common = project(":common")

sourceSets {
	getByName("main") {
		compileClasspath += common.sourceSets["main"].output
		runtimeClasspath += common.sourceSets["main"].output
	}
}

neoForge {
	version = Versions.NEOFORGE

	parchment {
		minecraftVersion = Versions.PARCHMENT_MINECRAFT
		mappingsVersion = Versions.PARCHMENT
	}

	val at = common.file("src/main/resources/META-INF/accesstransformer.cfg")
	if (at.exists()) setAccessTransformers(at)
	validateAccessTransformers = true

	runs {
		configureEach {
			systemProperty("forge.logging.markers", "REGISTRIES")
			systemProperty("forge.logging.console.level", "debug")
		}

		create("client") {
			client()
			ideName = "NeoForge Client (:${project.name})"
			gameDirectory.set(file("run/client"))
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}

		create("server") {
			server()
			ideName = "NeoForge Server (:${project.name})"
			gameDirectory.set(file("run/server"))
			programArgument("--nogui")
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}
	}

	mods {
		register(Properties.MOD_ID) {
			sourceSet(sourceSets["main"])
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
	val changelogProvider: Provider<String> by rootProject

	file.set(tasks.named<Jar>("jar").get().archiveFile)
	modLoaders.add("neoforge")
	changelog = changelogProvider
	displayName = "[NeoForge ${Versions.MINECRAFT}] ${Properties.MOD_ID}-${Versions.MOD}"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-neoforge"
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
