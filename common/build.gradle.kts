import mod.gradle.Mod

// Reference: https://projects.neoforged.net/neoforged/moddevgradle (NeoForm)

plugins {
	id("common")
	alias(libs.plugins.vanilla)
}

dependencies {
	compileOnly(libs.mixin.extras)
	annotationProcessor(libs.mixin.extras)
	compileOnly(libs.mixin.fabric)

	compileOnly(libs.clothconfig.forge)
}

sourceSets {
	create("generated") {
		resources {
			srcDir("src/generated/resources")
		}
	}
}

minecraft {
	version(libs.versions.minecraft.get())

	val aw = file("src/main/resources/${Mod.ID}.accesswidener")
	if (aw.exists()) accessWideners(aw)
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
