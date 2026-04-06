import mod.gradle.Versions

plugins {
	id("conventions.common")
	id("net.neoforged.moddev")
}

dependencies {
	compileOnly("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")
	annotationProcessor("io.github.llamalad7:mixinextras-common:${Versions.MIXIN_EXTRAS}")
	compileOnly("net.fabricmc:sponge-mixin:${Versions.FABRIC_MIXIN}")
}

sourceSets {
	create("generated") {
		resources {
			srcDir("src/generated/resources")
		}
	}
}

neoForge {
	neoFormVersion = Versions.NEOFORM

	parchment {
		minecraftVersion = Versions.PARCHMENT_MINECRAFT
		mappingsVersion = Versions.PARCHMENT
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
