plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "fxml",
    "media",
    "graphics"
)

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP
val jUnitVersion = "5.8.2"
val javaFxVersion = 11

dependencies {
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }

    implementation("io.github.palexdev:materialfx:11.13.5")
    implementation("net.harawata:appdirs:1.2.1")
    implementation("com.simtechdata:SceneOneFX:1.2.2")
    implementation("io.github.classgraph:classgraph:4.8.147")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("io.github.palexdev:materialfx:11.13.5")
    implementation("io.vacco.jsonbeans:jsonbeans:1.0.0")

    // JUnit API and testing engine
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

application {
    // Define the main class for the application
    mainClass.set("it.unibo.aknightstale.App")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}