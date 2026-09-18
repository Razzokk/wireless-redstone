import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment
import mod.gradle.Mod

// Reference: https://docs.fabricmc.net/develop/loom

plugins {
	id("loader")
	alias(libs.plugins.publish)
	alias(libs.plugins.loom)
}

repositories {
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
	maven("https://maven.nucleoid.xyz/")	// Needed by ModMenu 9.2.0
}

dependencies {
	minecraft(libs.minecraft)

	mappings(loom.layered {
		officialMojangMappings()
		parchment("${libs.parchment.get().module}-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
	})

	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)

	modApi(libs.clothconfig.fabric) {
		exclude(group = libs.fabric.api.get().group)
	}

	modImplementation(libs.modmenu)
}

loom {
	val aw = file("src/main/resources/${Mod.ID}.accesswidener")
	if (aw.exists()) accessWidenerPath.set(aw)

	runs {
		configureEach {
			generateRunConfig = true
			appendProjectPathToDisplayName = false
			systemProperties.put("mixin.debug.verbose", "true")
			systemProperties.put("mixin.debug.export", "true")
		}

		named("client") {
			client()
			displayName  = "Fabric Client"
			runDirectory = file("run/client")
			programArguments.addAll("--username", "dev")
		}

		named("server") {
			server()
			displayName  = "Fabric Server"
			runDirectory = file("run/server")
		}
	}
}

publishMods {
	val changelogProvider = rootProject.extra["changelogProvider"] as Provider<*>
	val minecraftVersion = libs.versions.minecraft.get()

	file.set(tasks.remapJar.get().archiveFile)
	modLoaders.add("fabric")
	changelog = changelogProvider.get() as String
	displayName = "[Fabric $minecraftVersion] ${Mod.VERSION} ${Mod.NAME}"
	version = "${Mod.VERSION}+$minecraftVersion-fabric"
	type = STABLE

	curseforge {
		projectId = Mod.CURSEFORGE_PROJECT_ID
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
		minecraftVersions.add(minecraftVersion)

		javaVersions.add(JavaVersion.toVersion(Mod.JAVA))

		client = true
		server = true

		requires("fabric-api")
		optional("cloth-config", "modmenu")
	}

	modrinth {
		projectId = Mod.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
		minecraftVersions.add(minecraftVersion)

		environment.set(ModrinthEnvironment.CLIENT_AND_SERVER)

		requires("fabric-api")
		optional("cloth-config", "modmenu")
	}
}
