import mod.gradle.Properties
import mod.gradle.Versions

val common = project(":common")
evaluationDependsOn(common.path)

plugins {
	id("conventions.loader")
	id("fabric-loom")
}

dependencies {
	minecraft("com.mojang", "minecraft", Versions.MINECRAFT)

	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT_MINECRAFT}:${Versions.PARCHMENT}")
	})

	modImplementation("net.fabricmc", "fabric-loader", Versions.FABRIC_LOADER)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", Versions.FABRIC_API)
	modLocalRuntime("net.fabricmc.fabric-api", "fabric-api", Versions.FABRIC_API)
}

sourceSets {
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
		register("datagen") {
			server()
			configName = "Datagen"
			ideConfigGenerated(true)
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${file("../common/src/generated/resources")}")
			vmArg("-Dfabric-api.datagen.modid=${Properties.MOD_ID}")
			runDir("build/datagen")
		}
	}
}

tasks {
	named<ProcessResources>("processResources").configure {
		exclude("accesstransformer.cfg")
	}
}
