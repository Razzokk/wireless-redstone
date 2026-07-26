plugins {
	id("loader")
	alias(libs.plugins.loom)
}

dependencies {
	minecraft(libs.minecraft)

	mappings(loom.layered {
		officialMojangMappings()
		parchment("${libs.parchment.get().module}-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
	})

	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)
	implementation(project(":fabric", "namedElements"))
}

fabricApi {
	configureDataGeneration {
		val common = project(":common")
		val generatedResources = common.file("src/generated/resources")

		outputDirectory = generatedResources
		strictValidation = true
		addToResources = false
	}
}
