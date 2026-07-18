// Reference: https://projects.neoforged.net/neoforged/moddevgradle (NeoForm)

plugins {
	id("common")
	alias(libs.plugins.moddev)
}

dependencies {
	compileOnly(libs.mixin.extras)
	annotationProcessor(libs.mixin.extras)
	compileOnly(libs.mixin.fabric)

	compileOnly(libs.clothconfig.neoforge)
}

sourceSets {
	create("generated") {
		resources {
			srcDir("src/generated/resources")
		}
	}
}

neoForge {
	neoFormVersion = libs.versions.neoform.get()

	parchment {
		minecraftVersion = libs.versions.minecraft
		mappingsVersion = libs.versions.parchment
	}

	accessTransformers.from("src/main/resources/META-INF/accesstransformer-common.cfg")
	validateAccessTransformers = true
}

configurations {
	register("commonJava") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}

	register("commonResources") {
		isCanBeResolved = false
		isCanBeConsumed = true
	}
}

artifacts {
	add("commonJava", sourceSets["main"].java.sourceDirectories.singleFile)
	add("commonResources", sourceSets["main"].resources.sourceDirectories.singleFile)
	add("commonResources", sourceSets["generated"].resources.sourceDirectories.singleFile)
}
