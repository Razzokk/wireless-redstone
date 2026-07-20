import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment
import mod.gradle.Mod
import org.apache.tools.ant.filters.LineContains

// Reference: https://projects.neoforged.net/neoforged/moddevgradle

plugins {
	id("loader")
	alias(libs.plugins.publish)
	alias(libs.plugins.moddev)
}

dependencies {
	api(libs.clothconfig.neoforge)
}

neoForge {
	version = libs.versions.neoforge.loader.get()

	parchment {
		minecraftVersion = libs.versions.minecraft
		mappingsVersion = libs.versions.parchment
	}

	val common = project(":common")
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
			ideName = "NeoForge Client"
			gameDirectory.set(file("run/client"))
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}

		create("server") {
			server()
			ideName = "NeoForge Server"
			gameDirectory.set(file("run/server"))
			programArgument("--nogui")
			jvmArguments.set(setOf("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true"))
		}
	}

	mods {
		register(Mod.ID) {
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
	val changelogProvider = rootProject.extra["changelogProvider"] as Provider<*>
	val minecraftVersion = libs.versions.minecraft.get()

	file.set(tasks.jar.get().archiveFile)
	modLoaders.add("neoforge")
	changelog = changelogProvider.get() as String
	displayName = "[NeoForge $minecraftVersion] ${Mod.VERSION} ${Mod.NAME}"
	version = "${Mod.VERSION}+$minecraftVersion-neoforge"
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
