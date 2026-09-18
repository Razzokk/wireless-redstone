import mod.gradle.Mod
import org.jetbrains.changelog.Changelog
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
	id("common") apply false // Workaround to be able to use the `Mod` object from `build-logic`
	alias(libs.plugins.changelog)
	alias(libs.plugins.publish) apply false
	alias(libs.plugins.vanilla) apply false
	alias(libs.plugins.loom) apply false
}

val versionString = "${Mod.VERSION}+${libs.versions.minecraft.get()}"
val isReleaseBuild = System.getenv("GITHUB_REF")?.startsWith("refs/tags/v") ?: false

extra["changelogProvider"] = provider {
	val version = if (isReleaseBuild) versionString else "Unreleased"
	changelog.renderItem(changelog.get(version), Changelog.OutputType.MARKDOWN)
}

allprojects {
	group = Mod.GROUP
	version = versionString

	if (!isReleaseBuild) {
		val buildNumber: String = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
		version = "$version.$buildNumber"
	}
}

changelog {
	version = versionString
	groups.empty()
	combinePreReleases.set(false)
	repositoryUrl.set(Mod.REPOSITORY_URL)
}
