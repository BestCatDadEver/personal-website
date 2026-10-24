plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.carlos"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinx.html)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.kotlin.test.junit)
}

// Directorio que se publica en GitHub Pages.
val siteDir: Provider<Directory> = layout.buildDirectory.dir("site")

// Los assets se copian con Sync y no con Copy para que al renombrar o borrar un
// fichero en src/ desaparezca tambien del sitio generado.
val copyStaticAssets by tasks.registering(Sync::class) {
    description = "Copia css/js/imagenes al sitio generado."
    from(layout.projectDirectory.dir("src/main/resources/static"))
    into(siteDir.map { it.dir("static") })
}

// GitHub Pages sirve un repo de proyecto bajo /<repo>/. Con un dominio propio
// servido desde la raiz: ./gradlew generateSite -PsitePath=/
val sitePath: String = (findProperty("sitePath") as String?) ?: "/personal-website/"

val generateSite by tasks.registering(JavaExec::class) {
    group = "build"
    description = "Renderiza el sitio estatico en build/site."
    dependsOn(copyStaticAssets)
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.carlos.GenerateSiteKt"
    argumentProviders.add { listOf(siteDir.get().asFile.absolutePath, sitePath) }
}

// Ver el sitio en local. Sirve build/site igual que lo haria Pages, porque
// abrir index.html con file:// no reproduce rutas, 404 ni tipos MIME.
val serveSite by tasks.registering(JavaExec::class) {
    group = "application"
    description = "Genera el sitio y lo sirve en http://localhost:8080."
    dependsOn(generateSite)
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.carlos.DevServerKt"
    argumentProviders.add { listOf(siteDir.get().asFile.absolutePath, "8080") }
}

// "gradlew build" deja el sitio listo sin tener que acordarse de la tarea.
tasks.named("assemble") {
    dependsOn(generateSite)
}
