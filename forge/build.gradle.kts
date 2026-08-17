import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment
import mod.gradle.Mod
import net.minecraftforge.renamer.gradle.RenameJar
import org.apache.tools.ant.filters.LineContains
import org.gradle.jvm.tasks.Jar

// Reference: https://github.com/MinecraftForge/MDKExamples

plugins {
	id("loader")
	alias(libs.plugins.publish)
	alias(libs.plugins.forge.gradle)
	alias(libs.plugins.forge.renamer)
}

repositories {
	minecraft.mavenizer(this)
	maven(fg.forgeMaven)
	maven(fg.minecraftLibsMaven)
}

dependencies {
	implementation(minecraft.dependency(libs.forge.get().toString()))
	annotationProcessor(variantOf(libs.mixin) { classifier("processor") })
	api(renamer.dependency(libs.clothconfig.forge.get().toString()))
}

minecraft {
	mappings(libs.parchment.get().name, "${libs.versions.parchment.get()}-${libs.versions.minecraft.get()}")

	val common = project(":common")
	accessTransformer = common.files("src/main/resources/META-INF/accesstransformer.cfg")

	runs {
		configureEach {
			mods {
				create(Mod.ID) {
					source(sourceSets["main"])
				}
			}

			systemProperty("eventbus.api.strictRuntimeChecks", "true")
			systemProperty("mixin.env.remapRefMap", "true")
			systemProperty("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")
			systemProperty("forge.logging.markers", "REGISTRIES")
			systemProperty("forge.logging.console.level", "debug")
			jvmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
		}

		create("client") {
			workingDir = file("run/client")
		}

		create("server") {
			workingDir = file("run/server")
			args("--nogui")
		}
	}
}

// Only for Minecraft 1.21.11 or lower
renamer {
	// Creates a task named 'renameJar'
	classes(tasks.named<Jar>("jar")) {
		// Just use the final output jar, no need for a separate renamed jar
		archiveClassifier = ""
	}

	// Needs to be after the call of `minecraft.dependency`, also stated by error message
	mappings(minecraft.dependency.toSrg)
}

tasks {
	named<ProcessResources>("processResources").configure {
		filesMatching("*.mixins.json") {
			filter<LineContains>("negate" to true, "contains" to setOf("refmap"))
		}
	}
}

publishMods {
	val changelogProvider = rootProject.extra["changelogProvider"] as Provider<*>
	val minecraftVersion = libs.versions.minecraft.get()

	// Need to use the renameJar jar output
	file.set(tasks.named<RenameJar>("renameJar").get().output)
	modLoaders.add("forge")
	changelog = changelogProvider.get() as String
	displayName = "[Forge $minecraftVersion] ${Mod.VERSION} ${Mod.NAME}"
	version = "${Mod.VERSION}+$minecraftVersion-forge"
	type = STABLE

	curseforge {
		projectId = Mod.CURSEFORGE_PROJECT_ID
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
		minecraftVersions.add(minecraftVersion)

		javaVersions.add(JavaVersion.toVersion(Mod.JAVA))

		client = true
		server = true

		optional("cloth-config")
	}

	modrinth {
		projectId = Mod.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
		minecraftVersions.add(minecraftVersion)

		environment.set(ModrinthEnvironment.CLIENT_AND_SERVER)

		optional("cloth-config")
	}
}
