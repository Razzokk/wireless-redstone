import mod.gradle.Properties
import mod.gradle.Versions
import org.gradle.jvm.tasks.Jar

plugins {
	id("conventions.loader")
	id("fabric-loom")
	id("me.modmuss50.mod-publish-plugin")
}

repositories {
	maven("https://maven.shedaniel.me/")					// Cloth config
	maven("https://maven.terraformersmc.com/releases/")	// Mod Menu
}

dependencies {
	minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT_MINECRAFT}:${Versions.PARCHMENT}")
	})

	modImplementation("net.fabricmc", "fabric-loader", Versions.FABRIC_LOADER)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", Versions.FABRIC_API)
	modLocalRuntime("net.fabricmc.fabric-api","fabric-api", Versions.FABRIC_API)
	modApi("me.shedaniel.cloth", "cloth-config-fabric", Versions.CLOTH_CONFIG)
	modImplementation("com.terraformersmc", "modmenu", Versions.MOD_MENU)
}

sourceSets {
	val common = project(":common")

	getByName("main") {
		compileClasspath += common.sourceSets["main"].output
		runtimeClasspath += common.sourceSets["main"].output
	}
}

loom {
	val aw = file("src/main/resources/${Properties.MOD_ID}.accesswidener")
	if (aw.exists()) accessWidenerPath.set(aw)

	mixin {
		defaultRefmapName.set("${Properties.MOD_ID}.refmap.json")
	}

	mods {
		register(Properties.MOD_ID) {
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

tasks {
	named<ProcessResources>("processResources").configure {
		exclude("accesstransformer.cfg")
	}
}

publishMods {
	val changelogProvider: Provider<String> by rootProject

	file.set(tasks.named<Jar>("remapJar").get().archiveFile)
	modLoaders.add("fabric")
	changelog = changelogProvider
	displayName = "[Fabric ${Versions.MINECRAFT}] ${Versions.MOD} ${Properties.MOD_NAME}"
	version = "${Versions.MOD}+${Versions.MINECRAFT}-fabric"
	type = STABLE

	curseforge {
		projectId = Properties.CURSEFORGE_PROJECT_ID
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")

		minecraftVersions.add(Versions.MINECRAFT)
		javaVersions.add(JavaVersion.toVersion(Versions.JAVA))

		clientRequired = true
		serverRequired = true

		requires("fabric-api")
		optional("cloth-config", "modmenu")
	}

	modrinth {
		projectId = Properties.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		minecraftVersions.add(Versions.MINECRAFT)

		requires("fabric-api")
		optional("cloth-config", "modmenu")
	}
}
