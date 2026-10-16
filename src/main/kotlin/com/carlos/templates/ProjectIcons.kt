package com.carlos.templates

import com.carlos.models.ProjectIcon

private const val SVG_HEADER = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" " +
    "stroke=\"#fff\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\">"

private val PROJECT_ICON_BODIES = mapOf(
    ProjectIcon.WEB to """
        <rect x="3" y="4" width="18" height="16" rx="2"/>
        <path d="M3 8H21"/>
        <circle cx="6" cy="6" r="0.6" fill="#fff" stroke="none"/>
        <circle cx="8.4" cy="6" r="0.6" fill="#fff" stroke="none"/>
        <circle cx="10.8" cy="6" r="0.6" fill="#fff" stroke="none"/>
    """.trimIndent(),
    ProjectIcon.API to """
        <rect x="4" y="4" width="16" height="6" rx="1.5"/>
        <rect x="4" y="14" width="16" height="6" rx="1.5"/>
        <circle cx="8" cy="7" r="0.7" fill="#fff" stroke="none"/>
        <circle cx="8" cy="17" r="0.7" fill="#fff" stroke="none"/>
        <path d="M14 10.5V13.5M14 10.5L12 12.5M14 10.5L16 12.5"/>
    """.trimIndent(),
    ProjectIcon.WEATHER to """
        <circle cx="6.5" cy="6.5" r="2.2"/>
        <path d="M6.5 2.7V3.7M6.5 9.3V10.3M2.7 6.5H3.7M9.3 6.5H10.3"/>
        <path d="M7 16a4 4 0 0 1 .5-7.97A5.5 5.5 0 0 1 18 10a3.5 3.5 0 0 1-1 6.9H7Z"/>
    """.trimIndent(),
    ProjectIcon.HABIT to """
        <rect x="4" y="5" width="16" height="15" rx="2"/>
        <path d="M4 9H20"/>
        <path d="M8 3V6M16 3V6"/>
        <path d="M8.5 14L10.5 16L15.5 11"/>
    """.trimIndent(),
    ProjectIcon.SPLIT to """
        <circle cx="7" cy="7" r="2.2"/>
        <circle cx="17" cy="17" r="2.2"/>
        <path d="M17 7L7 17"/>
    """.trimIndent(),
    ProjectIcon.WALLET to """
        <path d="M3 7a2 2 0 0 1 2-2h11a2 2 0 0 1 2 2v1h1a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7Z"/>
        <circle cx="16" cy="13" r="1.2" fill="#fff" stroke="none"/>
    """.trimIndent(),
    ProjectIcon.DEVOPS to """
        <circle cx="12" cy="12" r="3"/>
        <path d="M12 3V5M12 19V21M21 12H19M5 12H3M18.4 5.6L17 7M7 17L5.6 18.4M18.4 18.4L17 17M7 7L5.6 5.6"/>
    """.trimIndent(),
    ProjectIcon.POKEDEX to """
        <circle cx="12" cy="12" r="9"/>
        <path d="M3 12H9"/>
        <path d="M15 12H21"/>
        <circle cx="12" cy="12" r="2.6"/>
        <circle cx="12" cy="12" r="1" fill="#fff" stroke="none"/>
    """.trimIndent(),
)

/**
 * Devuelve markup SVG constante. El parametro es un enum, de modo que el resultado
 * solo puede ser una de las cadenas literales de arriba y nunca contenido externo.
 */
fun projectIconMarkup(icon: ProjectIcon): String =
    "$SVG_HEADER${PROJECT_ICON_BODIES.getValue(icon)}</svg>"

enum class SocialNetwork { LINKEDIN, GITHUB }

private val SOCIAL_ICON_MARKUP = mapOf(
    SocialNetwork.LINKEDIN to """<svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill="currentColor">""" +
        """<path d="M20.45 20.45h-3.56v-5.57c0-1.33-.02-3.03-1.85-3.03-1.85 0-2.14 1.45-2.14 2.94v5.66H9.34V9h3.41v1.56h.05c.48-.9 1.64-1.85 3.38-1.85 3.61 0 4.28 2.38 4.28 5.47v6.27zM5.34 7.43a2.07 2.07 0 1 1 0-4.13 2.07 2.07 0 0 1 0 4.13zM7.12 20.45H3.56V9h3.56v11.45z"/></svg>""",
    SocialNetwork.GITHUB to """<svg viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg" fill="currentColor">""" +
        """<path fill-rule="evenodd" clip-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>""",
)

fun socialIconMarkup(network: SocialNetwork): String = SOCIAL_ICON_MARKUP.getValue(network)
