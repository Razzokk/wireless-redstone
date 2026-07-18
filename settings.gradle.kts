pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		maven("https://maven.neoforged.net/releases") {
			name = "NeoForge"
		}
		maven("https://maven.minecraftforge.net/") {
			name = "Forge"
		}
		maven("https://repo.spongepowered.org/repository/maven-public/") {
			name = "Sponge Snapshots"
		}
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "wireless-redstone"

includeBuild("build-logic")

include("common")
include("datagen")
include("fabric")
include("neoforge")
include("forge")
