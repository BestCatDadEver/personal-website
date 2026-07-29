package com.carlos.templates

private const val SVG_HEADER = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" " +
    "stroke=\"#fff\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\">"

private val PROJECT_ICON_BODIES = mapOf(
    "web" to """
        <rect x="3" y="4" width="18" height="16" rx="2"/>
        <path d="M3 8H21"/>
        <circle cx="6" cy="6" r="0.6" fill="#fff" stroke="none"/>
        <circle cx="8.4" cy="6" r="0.6" fill="#fff" stroke="none"/>
        <circle cx="10.8" cy="6" r="0.6" fill="#fff" stroke="none"/>
    """.trimIndent(),
    "api" to """
        <rect x="4" y="4" width="16" height="6" rx="1.5"/>
        <rect x="4" y="14" width="16" height="6" rx="1.5"/>
        <circle cx="8" cy="7" r="0.7" fill="#fff" stroke="none"/>
        <circle cx="8" cy="17" r="0.7" fill="#fff" stroke="none"/>
        <path d="M14 10.5V13.5M14 10.5L12 12.5M14 10.5L16 12.5"/>
    """.trimIndent(),
    "weather" to """
        <circle cx="6.5" cy="6.5" r="2.2"/>
        <path d="M6.5 2.7V3.7M6.5 9.3V10.3M2.7 6.5H3.7M9.3 6.5H10.3"/>
        <path d="M7 16a4 4 0 0 1 .5-7.97A5.5 5.5 0 0 1 18 10a3.5 3.5 0 0 1-1 6.9H7Z"/>
    """.trimIndent(),
    "habit" to """
        <rect x="4" y="5" width="16" height="15" rx="2"/>
        <path d="M4 9H20"/>
        <path d="M8 3V6M16 3V6"/>
        <path d="M8.5 14L10.5 16L15.5 11"/>
    """.trimIndent(),
    "split" to """
        <circle cx="7" cy="7" r="2.2"/>
        <circle cx="17" cy="17" r="2.2"/>
        <path d="M17 7L7 17"/>
    """.trimIndent(),
    "wallet" to """
        <path d="M3 7a2 2 0 0 1 2-2h11a2 2 0 0 1 2 2v1h1a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z"/>
        <circle cx="16" cy="13" r="1.2" fill="#fff" stroke="none"/>
    """.trimIndent(),
    "devops" to """
        <circle cx="12" cy="12" r="3"/>
        <path d="M12 3V5M12 19V21M21 12H19M5 12H3M18.4 5.6L17 7M7 17L5.6 18.4M18.4 18.4L17 17M7 7L5.6 5.6"/>
    """.trimIndent(),
    "pokedex" to """
        <circle cx="12" cy="12" r="9"/>
        <path d="M3 12H9"/>
        <path d="M15 12H21"/>
        <circle cx="12" cy="12" r="2.6"/>
        <circle cx="12" cy="12" r="1" fill="#fff" stroke="none"/>
    """.trimIndent(),
)

fun projectIconMarkup(icon: String): String {
    val body = PROJECT_ICON_BODIES[icon] ?: PROJECT_ICON_BODIES.getValue("web")
    return "$SVG_HEADER$body</svg>"
}
