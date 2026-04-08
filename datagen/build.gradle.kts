import mod.gradle.Versions

plugins {
	id("conventions.loader")
	id("fabric-loom")
}

val common = project(":common")
val fabric = project(":fabric")
evaluationDependsOn(common.path)
evaluationDependsOn(fabric.path)

val generatedResources = common.file("src/generated/resources")

dependencies {
	minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")

	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${Versions.PARCHMENT_MINECRAFT}:${Versions.PARCHMENT}")
	})

	modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
	modLocalRuntime("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")

	implementation(project(common.path))
	runtimeOnly(project(fabric.path, "namedElements")) { isTransitive = false }
}

//sourceSets {
//	getByName("main") {
//		compileClasspath += common.sourceSets["main"].output
//		runtimeClasspath += common.sourceSets["main"].output
//
//		compileClasspath += fabric.sourceSets["main"].output
//		runtimeClasspath += fabric.sourceSets["main"].output
//	}
//}

fabricApi {
	configureDataGeneration {
		outputDirectory.set(generatedResources)
		addToResources.set(false)
	}
}

tasks {
	named<ProcessResources>("processResources").configure {
		exclude("accesstransformer.cfg")
	}
}
