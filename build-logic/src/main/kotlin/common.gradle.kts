import mod.gradle.Mod
import org.gradle.accessors.dm.LibrariesForLibs
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
	base
	`java-library`
	idea
	`maven-publish`
}

// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

base.archivesName.set("${Mod.ID}-${project.name}")

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(Mod.JAVA))
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
		filter {
			includeGroupAndSubgroups("org.spongepowered")
		}
	}
	exclusiveContent {
		forRepositories(
			maven("https://maven.parchmentmc.org/") {
				name = "ParchmentMC"
			},
			maven("https://maven.neoforged.net/releases") {
				name = "NeoForge"
			},
			maven("https://maven.minecraftforge.net/") {
				name = "Forge"
			}
		)
		filter {
			includeGroup("org.parchmentmc.data")
		}
	}
	exclusiveContent {
		forRepository {
			maven("https://api.modrinth.com/maven") {
				name = "Modrinth"
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}
	maven("https://maven.fabricmc.net/") {
		name = "Fabric"
	}

	maven("https://maven.shedaniel.me/") // Cloth config
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
				"Specification-Title" to Mod.NAME,
				"Specification-Vendor" to Mod.AUTHOR,
				"Specification-Version" to archiveVersion,
				"Implementation-Title" to "${Mod.NAME} (${project.name})",
				"Implementation-Version" to archiveVersion,
				"Implementation-Vendor" to Mod.AUTHOR,
				"Implementation-Timestamp" to OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
				"Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
				"Built-On-Minecraft" to libs.versions.minecraft,
			))
		}
	}

	val expandProps = mapOf(
		"group" to project.group,
		"mod_version" to Mod.VERSION,
		"mod_id" to Mod.ID,
		"mod_name" to Mod.NAME,
		"mod_author" to Mod.AUTHOR,
		"mod_license" to Mod.LICENSE,
		"mod_description" to Mod.DESCRIPTION,
		"minecraft_version" to libs.versions.minecraft.get(),
		"fabric_loader_version" to libs.versions.fabric.loader.get(),
		"fabric_api_version" to libs.versions.fabric.api.get(),
		"fabric_minecraft_version_range" to libs.versions.fabric.range.minecraft.get(),
		"fabric_loader_range" to libs.versions.fabric.range.loader.get(),
		"forge_version" to libs.versions.forge.loader.get(),
		"forge_minecraft_version_range" to libs.versions.forge.range.minecraft.get(),
		"forge_loader_version_range" to libs.versions.forge.range.loader.get(),
		"cloth_config_version" to libs.versions.clothconfig.get(),
		"modmenu_version" to libs.versions.modmenu.get(),
		"java_version" to Mod.JAVA,
		"curseforge_page" to Mod.CURSEFORGE_PAGE,
		"modrinth_page" to Mod.MODRINTH_PAGE,
		"sources" to Mod.REPOSITORY_URL,
		"discord" to Mod.DISCORD_URL
	)

	val processResourcesTasks = listOf("processResources", "processDatagenResources")

	withType<ProcessResources>().matching { processResourcesTasks.contains(it.name) }.configureEach {
		inputs.properties(expandProps)
		filesMatching(setOf("fabric.mod.json", "META-INF/mods.toml", "*.mixins.json", "pack.mcmeta")) {
			expand(expandProps)
		}
	}
}
