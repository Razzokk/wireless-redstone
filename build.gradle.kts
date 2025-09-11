import mod.gradle.Properties
import mod.gradle.Versions
import org.jetbrains.changelog.Changelog
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.changelog") version "2.4.0"
	id("fabric-loom") version "1.10-SNAPSHOT" apply false
	id("net.neoforged.moddev") version "2.0.89" apply false
	id ("net.minecraftforge.gradle") version "[6.0.24,6.2)" apply false
	id("org.parchmentmc.librarian.forgegradle") version "1.+" apply false
	id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
}

val isReleaseBuild = System.getenv("GITHUB_REF")?.startsWith("refs/tags/v") ?: false

val changelogProvider by extra {
	provider {
		val version = if (isReleaseBuild) "${Versions.MOD}+${Versions.MINECRAFT}" else "Unreleased"
		changelog.renderItem(changelog.get(version), Changelog.OutputType.MARKDOWN)
	}
}

allprojects {
	group = Properties.GROUP
	version = "${Versions.MOD}+${Versions.MINECRAFT}"

	if (!isReleaseBuild) {
		val buildNumber: String = Properties.NOW.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
		version = "$version.$buildNumber"
	}
}

changelog {
	version = "${Versions.MOD}+${Versions.MINECRAFT}"
	groups.empty()
	combinePreReleases.set(false)
	repositoryUrl.set(Properties.REPOSITORY_URL)
}
