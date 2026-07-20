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

	mods {
		register(Mod.ID) {
			sourceSet(sourceSets["main"])
		}
	}

	runs {
		named("client") {
			client()
			configName = "Fabric Client"
			runDir = "run/client"
			ideConfigGenerated(true)
			vmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
		}

		named("server") {
			server()
			configName = "Fabric Server"
			runDir = "run/server"
			ideConfigGenerated(true)
			vmArgs("-Dmixin.debug.verbose=true", "-Dmixin.debug.export=true")
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
