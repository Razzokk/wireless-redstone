plugins {
	id("common")
}

val commonJava = configurations.register("commonJava") {
	isCanBeResolved = true
}

val commonResources = configurations.register("commonResources") {
	isCanBeResolved = true
}

val common = project(":common")
evaluationDependsOn(common.path)

sourceSets {
	getByName("main") {
		compileClasspath += common.sourceSets["main"].output
	}
}

dependencies {
	commonJava(project(":common", commonJava.name))
	commonResources(project(":common", commonResources.name))
}

tasks {
	named<JavaCompile>("compileJava").configure {
		dependsOn(commonJava)
		source(commonJava)
	}

	named<ProcessResources>("processResources").configure {
		dependsOn(commonResources)
		from(commonResources)
	}

	named<Jar>("sourcesJar").configure {
		dependsOn(commonJava)
		from(commonJava)
		dependsOn(commonResources)
		from(commonResources)
	}
}
