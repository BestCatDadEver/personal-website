package com.carlos.templates

/**
 * Todos los recursos de la pagina son propios, asi que la politica puede ser
 * estrictamente 'self': ningun origen externo, ni inline, ni eval.
 *
 * Antes la enviaba el servidor como cabecera HTTP. GitHub Pages no permite
 * configurar cabeceras, asi que viaja en un <meta http-equiv> en cada pagina. Eso
 * deja fuera `frame-ancestors`, que el navegador solo honra como cabecera; ver el
 * apartado de seguridad del README.
 */
internal val CONTENT_SECURITY_POLICY = listOf(
    "default-src 'self'",
    "script-src 'self'",
    "style-src 'self'",
    "img-src 'self' data:",
    "font-src 'self'",
    "connect-src 'self'",
    "form-action 'none'",
    "base-uri 'none'",
    "object-src 'none'",
).joinToString("; ")
