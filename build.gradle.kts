import mod.gradle.Properties
import mod.gradle.Versions
import org.jetbrains.changelog.Changelog
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.changelog") version "2.5.0"
	id("fabric-loom") version "1.10-SNAPSHOT" apply false
	id("net.neoforged.moddev") version "2.0.141" apply false
	id("net.minecraftforge.gradle") version "[6.0.24,6.2)" apply false
	id("org.parchmentmc.librarian.forgegradle") version "1.+" apply false
	id("me.modmuss50.mod-publish-plugin") version "1.1.0" apply false
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
		val buildNumber: String = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
		version = "$version.$buildNumber"
	}
}

changelog {
	version = "${Versions.MOD}+${Versions.MINECRAFT}"
	groups.empty()
	combinePreReleases.set(false)
	repositoryUrl.set(Properties.REPOSITORY_URL)
}
