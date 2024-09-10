plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
}

version = "${rootProject.extra["mod_version"]}-${project.extra["minecraft_version"]}"
group = "${rootProject.extra["maven_group"]}.mod.fabric"

base {
    archivesName.set("${project.extra["archives_base_name"]}")
}

loom {
    accessWidenerPath = file("src/main/resources/civchesttracker.accesswidener")

    runConfigs.configureEach {
        this.programArgs
            .addAll("--username LocalModTester".split(" "))
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.extra["minecraft_version"]}")
    loom {
        @Suppress("UnstableApiUsage")
        mappings(layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:${project.extra["parchment_name"]}:${project.extra["parchment_version"]}@zip")
        })
    }

    modImplementation("net.fabricmc:fabric-loader:${project.extra["fabric_loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.extra["fabric_api_version"]}")

    // https://maven.jackf.red/#/releases/red/jackf/chesttracker
    // Ctrl+F for your Minecraft version
    "red.jackf:chesttracker:2.6.0+1.21.1".also {
        include(it)
        modImplementation(it)
    }

    // https://modrinth.com/mod/modmenu/version/11.0.2
    modLocalRuntime("maven.modrinth:modmenu:3ib3Uvvv")
}

repositories {
    maven(url = "https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
    maven(url = "https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
    // For ChestTracker
    maven(url = "https://maven.jackf.red/releases/") {
        name = "JackFredMaven"
        content {
            @Suppress("UnstableApiUsage")
            includeGroupAndSubgroups("red.jackf")
        }
    }
    // For YACL
    maven(url = "https://maven.isxander.dev/releases/") {
        name = "Xander Maven"
        content {
            @Suppress("UnstableApiUsage")
            includeGroupAndSubgroups("dev.isxander")
            @Suppress("UnstableApiUsage")
            includeGroupAndSubgroups("org.quiltmc")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    jar {
        from("LICENCE") {
            rename { "LICENSE_${project.extra["mod_name"]}" } // Use US spelling
        }
    }
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "mod_name" to project.extra["mod_name"],
                "mod_version" to project.version,
                "mod_description" to project.extra["mod_description"],
                "copyright_licence" to project.extra["copyright_licence"],

                "mod_home_url" to project.extra["mod_home_url"],
                "mod_source_url" to project.extra["mod_source_url"],
                "mod_issues_url" to project.extra["mod_issues_url"],

                "minecraft_version" to project.extra["minecraft_version"],
                "fabric_loader_version" to project.extra["fabric_loader_version"],
            )
        }
    }
    register<Delete>("cleanJar") {
        delete(fileTree("./dist") {
            include("*.jar")
        })
    }
    register<Copy>("copyJar") {
        dependsOn(getByName("cleanJar"))
        from(getByName("remapJar"))
        into("./dist")
        rename("(.*?)\\.jar", "\$1-fabric.jar")
    }
    build {
        dependsOn(getByName("copyJar"))
    }
}
