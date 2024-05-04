import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "mc.tsukimiya.mooney"
version = "2.0.0"

val mcVersion = "1.20.4"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven {
        url = uri("https://maven.pkg.github.com/tsukimiyaproject/Lib4B")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${mcVersion}-R0.1-SNAPSHOT")
    compileOnly("mc.tsukimiya:lib4b:1.2.1")
    library(kotlin("stdlib"))
    library("org.jetbrains.exposed:exposed-core:0.41.1")
    library("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    library("org.jetbrains.exposed:exposed-dao:0.41.1")
    library("org.jetbrains.exposed:exposed-java-time:0.41.1")
    library("org.xerial:sqlite-jdbc:3.41.2.2")
    library("com.mysql:mysql-connector-j:8.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

bukkit {
    name = "MooneyCore"
    version = getVersion().toString()
    description = "経済プラグイン"
    author = "deceitya"
    main = "mc.tsukimiya.mooney.core.MooneyCore"
    apiVersion = mcVersion.substring(0, mcVersion.lastIndexOf("."))
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    depend = listOf("Lib4B")

    commands {
        register("mymoney") {
            description = "自分の所持金を表示する。"
            permission = "tsukimiya.mooney.core.mymoney"
            usage = "/mymoney"
        }

        register("paymoney") {
            description = "プレイヤーにお金を支払う。"
            permission = "tsukimiya.mooney.core.paymoney"
            usage = "/paymoney <プレイヤー名> <金額> [理由]"
        }

        register("showmoney") {
            description = "プレイヤーの所持金を表示する。"
            permission = "tsukimiya.mooney.core.showmoney"
            usage = "/showmoney <プレイヤー名>"
        }

        register("setmoney") {
            description = "プレイヤーの所持金を設定する。"
            permission = "tsukimiya.mooney.core.setmoney"
            usage = "/setmoney <プレイヤー名> <金額> [理由]"
        }

        register("givemoney") {
            description = "プレイヤーの所持金を増やす。"
            permission = "tsukimiya.mooney.core.givemoney"
            usage = "/givemoney <プレイヤー名> <金額> [理由]"
        }

        register("takemoney") {
            description = "プレイヤーの所持金を減らす。"
            permission = "tsukimiya.mooney.core.takemoney"
            usage = "/takemoney <プレイヤー名> <金額> [理由]"
        }

        register("topmoney") {
            description = "所持金ランキングを表示する。"
            permission = "tsukimiya.mooney.core.topmoney"
            usage = "/topmoney [ページ]"
        }

        register("logmoney") {
            description = "所持金のログを表示する。"
            permission = "tsukimiya.mooney.core.logmoney"
            usage = "/logmoney [ページ]"
        }
    }

    permissions {
        register("tsukimiya.mooney.core.*") {
            children = listOf(
                "tsukimiya.mooney.core.mymoney",
                "tsukimiya.mooney.core.showmoney",
                "tsukimiya.mooney.core.paymoney",
                "tsukimiya.mooney.core.givemoney",
                "tsukimiya.mooney.core.takemoney",
                "tsukimiya.mooney.core.setmoney",
                "tsukimiya.mooney.core.topmoney",
                "tsukimiya.mooney.core.logmoney"
            )
        }
        register("tsukimiya.mooney.core.mymoney") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.showmoney") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.paymoney") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.givemoney") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.takemoney") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.setmoney") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("tsukimiya.mooney.core.topmoney") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("tsukimiya.mooney.core.logmoney") {
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
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
