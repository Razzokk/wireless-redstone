plugins {
	id("loader")
	alias(libs.plugins.loom)
}

val common = project(":common")
val fabric = project(":fabric")
evaluationDependsOn(common.path)
evaluationDependsOn(fabric.path)

val generatedResources = common.file("src/generated/resources")

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
