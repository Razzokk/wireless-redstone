plugins {
	id("loader")
	alias(libs.plugins.loom)
}

val fabric = project(":fabric")
evaluationDependsOn(fabric.path)

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

	runtimeOnly(project(fabric.path, "namedElements")) { isTransitive = false }
}

fabricApi {
	configureDataGeneration {
		val common = project(":common")
		val generatedResources = common.file("src/generated/resources")

		outputDirectory = generatedResources
		addToResources = false
	}
}
