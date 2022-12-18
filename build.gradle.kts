import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "mc.tsukimiya.mooney"
version = "1.0-SNAPSHOT"

val mcVersion = "1.19.3"
val latest = "latest.release"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "$mcVersion-R0.1-SNAPSHOT")
    library(kotlin("stdlib"))
    library("org.jetbrains.exposed", "exposed-core", latest)
    library("org.jetbrains.exposed", "exposed-jdbc", latest)
    library("org.jetbrains.exposed", "exposed-dao", latest)
    library("org.xerial", "sqlite-jdbc", latest)
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.9.0")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.9.0")
}

bukkit {
    name = "MooneyCore"
    version = getVersion().toString()
    description = "お金プラグイン"
    author = "deceitya"
    main = "mc.tsukimiya.mooney.core.MooneyCore"
    apiVersion = mcVersion.substring(0, mcVersion.lastIndexOf("."))
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

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/tsukimiyaproject/MooneyCore")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
