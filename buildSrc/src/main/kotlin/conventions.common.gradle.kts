import mod.gradle.Properties
import mod.gradle.Versions
import java.time.format.DateTimeFormatter

plugins {
	base
	`java-library`
	idea
	`maven-publish`
}

base.archivesName.set("${Properties.MOD_ID}-${project.name}")

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(Versions.JAVA))
	withSourcesJar()
}

repositories {
	mavenCentral()
	// https://docs.gradle.org/current/userguide/declaring_repositories.html#declaring_content_exclusively_found_in_one_repository
	exclusiveContent {
		forRepository {
			maven("https://repo.spongepowered.org/repository/maven-public") {
				name = "Sponge"
			}
		}
		filter { includeGroupAndSubgroups("org.spongepowered") }
	}
	exclusiveContent {
		forRepositories(
			maven("https://maven.parchmentmc.org/") {
				name = "ParchmentMC"
			},
			maven("https://maven.minecraftforge.net/") {
				name = "Forge"
			}
		)
		filter { includeGroup("org.parchmentmc.data") }
	}
	maven("https://maven.fabricmc.net/") {
		name = "Fabric"
	}
}

tasks {
	val license = rootProject.file("LICENSE")

	named<Jar>("sourcesJar").configure {
		from(license)
	}

	named<Jar>("jar").configure {
		from(license)

		manifest {
			attributes(mapOf(
				"Specification-Title" to Properties.MOD_NAME,
				"Specification-Vendor" to Properties.MOD_AUTHOR,
				"Specification-Version" to archiveVersion,
				"Implementation-Title" to "${Properties.MOD_NAME} (${project.name})",
				"Implementation-Version" to archiveVersion,
				"Implementation-Vendor" to Properties.MOD_AUTHOR,
				"Implementation-Timestamp" to Properties.NOW.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
				"Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
				"Built-On-Minecraft" to Versions.MINECRAFT,
			))
		}
	}

	val expandProps = mapOf(
		"group" to project.group,
		"mod_version" to Versions.MOD,
		"mod_id" to Properties.MOD_ID,
		"mod_name" to Properties.MOD_NAME,
		"mod_author" to Properties.MOD_AUTHOR,
		"mod_license" to Properties.LICENSE,
		"mod_description" to Properties.DESCRIPTION,
		"minecraft_version" to Versions.MINECRAFT,
		"fabric_api_version" to Versions.FABRIC_API,
		"fabric_loader_version" to Versions.FABRIC_LOADER,
		"fabric_minecraft_version_range" to Versions.FABRIC_MINECRAFT_RANGE,
		"fabric_loader_range" to Versions.FABRIC_LOADER_RANGE,
		"forge_minecraft_version_range" to Versions.FORGE_MINECRAFT_RANGE,
		"forge_loader_version_range" to Versions.FORGE_LOADER_RANGE,
		"cloth_config_version" to Versions.CLOTH_CONFIG,
		"modmenu_version" to Versions.MOD_MENU,
		"java_version" to Versions.JAVA,
		"curseforge_page" to Properties.CURSEFORGE_PAGE,
		"modrinth_page" to Properties.MODRINTH_PAGE,
		"sources" to Properties.REPOSITORY_URL,
		"discord" to Properties.DISCORD_URL
	)

	val processResourcesTasks = listOf("processResources", "processDatagenResources")

	withType<ProcessResources>().matching { processResourcesTasks.contains(it.name) }.configureEach {
		inputs.properties(expandProps)
		filesMatching(setOf("fabric.mod.json", "META-INF/mods.toml", "*.mixins.json", "pack.mcmeta")) {
			expand(expandProps)
		}
		exclude("\\.cache")
	}
}
