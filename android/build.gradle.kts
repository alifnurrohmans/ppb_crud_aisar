allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory = rootProject.layout.buildDirectory
    .dir("../../build")
    .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)

    afterEvaluate {
        if (project.plugins.hasPlugin("com.android.application") ||
            project.plugins.hasPlugin("com.android.library")) {
            
            // Di Kotlin (kts), kita harus akses lewat extension
            val android = project.extensions.findByName("android") as? com.android.build.gradle.BaseExtension
            android?.apply {
                // Gunakan tanda kurung untuk method call di Kotlin
                compileSdkVersion(36)
                buildToolsVersion("36.0.0")

                // Fix untuk error Isar (Namespace)
                if (namespace == null) {
                    namespace = "com.example.${project.name.replace("-", "_")}"
                }
            }
        }
    }
    
    // Gunakan set() untuk DirectoryProperty di Kotlin terbaru
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}