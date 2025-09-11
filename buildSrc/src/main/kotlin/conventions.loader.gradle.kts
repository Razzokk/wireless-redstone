import mod.gradle.Properties
import mod.gradle.Versions

plugins {
	id("conventions.common")
}

configurations {
	register("commonJava") {
		isCanBeResolved = true
	}
	register("commonResources") {
		isCanBeResolved = true
	}
}

dependencies {
	"commonJava"(project(":common", "commonJava"))
	"commonResources"(project(":common", "commonResources"))
}

tasks {
	named<JavaCompile>("compileJava").configure {
		dependsOn(configurations.getByName("commonJava"))
		source(configurations.getByName("commonJava"))
	}

	named<ProcessResources>("processResources").configure {
		dependsOn(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
	}

	named<Jar>("sourcesJar").configure {
		dependsOn(configurations.getByName("commonJava"))
		from(configurations.getByName("commonJava"))
		dependsOn(configurations.getByName("commonResources"))
		from(configurations.getByName("commonResources"))
	}
}
