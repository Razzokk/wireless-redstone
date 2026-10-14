import me.modmuss50.mpp.platforms.modrinth.ModrinthEnvironment
import mod.gradle.Mod

// Reference: https://github.com/MinecraftForge/MDKExamples

plugins {
	id("loader")
	alias(libs.plugins.publish)
	alias(libs.plugins.forge.gradle)
}

repositories {
	minecraft.mavenizer(this)
	maven(fg.forgeMaven)
	maven(fg.minecraftLibsMaven)
}

dependencies {
	implementation(minecraft.dependency(libs.forge.get().toString()))
	annotationProcessor(variantOf(libs.mixin) { classifier("processor") })
	api(libs.clothconfig.forge.get().toString())
}

val mixinConfig = "${Mod.ID}.mixins.json";

minecraft {
	mappings(libs.parchment.get().name, "${libs.versions.parchment.get()}-${libs.versions.minecraft.get()}")

	val common = project(":common")
	accessTransformer = common.files("src/main/resources/META-INF/accesstransformer.cfg")

	runs {
		configureEach {
			systemProperty("eventbus.api.strictRuntimeChecks", "true")
			systemProperty("forge.logging.markers", "REGISTRIES")
			systemProperty("forge.logging.console.level", "debug")
			systemProperty("mixin.debug.verbose", "true")
			systemProperty("mixin.debug.export", "true")

			val file = common.file("src/main/resources/${mixinConfig}")
			if (file.exists()) args("--mixin.config=${mixinConfig}")
		}

		create("client") {
			workingDir = file("run/client")
			args("--username", "dev")
		}

		create("server") {
			workingDir = file("run/server")
			args("--nogui")
		}
	}
}

tasks.jar.configure {
	manifest {
		val file = project(":common").file("src/main/resources/${mixinConfig}")
		if (file.exists()) attributes["MixinConfigs"] = mixinConfig
	}
}

publishMods {
	val changelogProvider = rootProject.extra["changelogProvider"] as Provider<*>
	val minecraftVersion = libs.versions.minecraft.get()

	file.set(tasks.jar.get().archiveFile)
	modLoaders.add("forge")
	changelog = changelogProvider.get() as String
	displayName = "[Forge $minecraftVersion] ${Mod.VERSION} ${Mod.NAME}"
	version = "${Mod.VERSION}+$minecraftVersion-forge"
	type = STABLE

	curseforge {
		projectId = Mod.CURSEFORGE_PROJECT_ID
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
		minecraftVersions.add(minecraftVersion)

		javaVersions.add(JavaVersion.toVersion(Mod.JAVA))

		client = true
		server = true

		optional("cloth-config")
	}

	modrinth {
		projectId = Mod.MODRINTH_PROJECT_ID
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
		minecraftVersions.add(minecraftVersion)

		environment.set(ModrinthEnvironment.CLIENT_AND_SERVER)

		optional("cloth-config")
	}
}
