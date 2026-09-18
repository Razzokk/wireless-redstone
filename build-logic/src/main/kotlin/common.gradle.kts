import mod.gradle.Mod

plugins {
	idea
	`java-library`
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(Mod.JAVA))
	withSourcesJar()
}

repositories {
	mavenCentral()

	// https://docs.gradle.org/current/userguide/filtering_repository_content.html
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
