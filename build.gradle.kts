import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "mc.tsukimiya"
version = "1.0-SNAPSHOT"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.19.2-R0.1-SNAPSHOT")
    library("org.jetbrains.kotlin", "kotlin-stdlib")
    library("org.jetbrains.exposed", "exposed-core", "0.40.1")
    library("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
    library("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    library("org.xerial", "sqlite-jdbc", "3.40.0.0")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.0")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.9.0")
}

bukkit {
    name = "MooneyCore"
    version = "1.2"
    description = "お金プラグイン"
    author = "deceitya"
    main = "mc.tsukimiya.mooney.core.MooneyCore"
    apiVersion = "1.19"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    commands {
        register("money") {
            description = "お金に関するコマンド"
            permission = "tsukimiya.mooney.core.show"
            usage = "/money [show|pay|plus|minus|set|top|help]"
        }
    }

    permissions {
        register("tsukimiya.mooney.core.*") {
            children = listOf(
                "tsukimiya.mooney.core.show",
                "tsukimiya.mooney.core.show.other",
                "tsukimiya.mooney.core.pay",
                "tsukimiya.mooney.core.plus",
                "tsukimiya.mooney.core.minus",
                "tsukimiya.mooney.core.set",
                "tsukimiya.mooney.core.top",
                "tsukimiya.mooney.core.help",
            )
        }
        register("tsukimiya.mooney.core.show") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.show.other") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.pay") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.plus") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.minus") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.set") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.top") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.help") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/tsukimiyaproject/MooneyCore")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
