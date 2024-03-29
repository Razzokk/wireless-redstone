pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		maven("https://maven.minecraftforge.net/") {
			name = "Forge"
		}
		maven("https://maven.neoforged.net/releases") {
			name = "NeoForged"
		}
		maven("https://maven.architectury.dev/") {
			name = "Architectury"
		}
		mavenCentral()
		gradlePluginPortal()
	}
}

val modName: String by extra
rootProject.name = modName

include("common", "datagen", "fabric", "forge", "neoforge")
