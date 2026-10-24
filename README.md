# personal-website

Portfolio personal de Carlos Gelpi. Sitio estatico publicado en GitHub Pages.

## Como funciona

El diseño vive en plantillas de Kotlin (`src/main/kotlin/com/carlos/templates`)
escritas con [kotlinx.html](https://github.com/Kotlin/kotlinx.html), y los
contenidos en repositorios en memoria (`.../repository`). En el build,
`GenerateSite.kt` las renderiza una sola vez y escribe el sitio en `build/site`.

Antes esto era un servidor Ktor que renderizaba la pagina en cada peticion. Se
retiro al pasar a GitHub Pages, que solo sirve ficheros estaticos. Las plantillas
y los repositorios no cambiaron: lo unico que cambio es *cuando* se renderizan,
asi que el diseño sigue teniendo una sola fuente de verdad.

Lo que se genera:

| Fichero             | Origen                                              |
| ------------------- | --------------------------------------------------- |
| `index.html`        | `landingPage` en `templates/LandingPage.kt`         |
| `404.html`          | `notFoundPage`, sustituye al StatusPages del servidor |
| `api/projects.json` | El antiguo endpoint `/api/projects`, ahora estatico |
| `static/`           | Copia de `src/main/resources/static`                |

## Tareas

| Tarea                     | Descripcion                                  |
| ------------------------- | -------------------------------------------- |
| `./gradlew test`          | Corre los tests                              |
| `./gradlew generateSite`  | Genera el sitio en `build/site`              |
| `./gradlew serveSite`     | Genera el sitio y lo sirve en localhost:8080 |
| `./gradlew build`         | Tests + sitio generado                       |

### Ver el sitio en local

```
./gradlew serveSite
```

`DevServer.kt` sirve `build/site` con el servidor HTTP del JDK, sin dependencias
ni despliegue: solo reproduce lo que hace Pages, incluido devolver `404.html`
ante rutas desconocidas. Abrir `index.html` con `file://` no vale, porque no
reproduce rutas, 404 ni tipos MIME.

Las rutas a los assets son relativas, asi que el sitio funciona igual servido
desde la raiz que desde un subdirectorio. La excepcion es `404.html`, que lleva
el base path de produccion incrustado y en local sale sin estilos. Para verlo
como se vera en un dominio propio:

```
./gradlew serveSite -PsitePath=/
```

## Despliegue

`.github/workflows/pages.yml` construye y publica en cada push a `master`. Corre
los tests antes de desplegar, de modo que un fallo detiene la publicacion.

Requiere un ajuste manual una sola vez: **Settings -> Pages -> Source:
"GitHub Actions"**. Sin eso el workflow falla al intentar publicar.

### Base path

Pages sirve un repo de proyecto bajo `/<repo>/`. El workflow toma esa ruta de
`actions/configure-pages` y se la pasa al generador. Solo la necesita `404.html`:
Pages devuelve ese fichero para rutas de cualquier profundidad, asi que una ruta
relativa se resolveria contra la URL pedida y no contra el sitio.

Para generar como si el sitio colgara de la raiz (dominio propio):

```
./gradlew generateSite -PsitePath=/
```

## Seguridad

Las protecciones que dependian del servidor ya no existen, porque GitHub Pages no
permite configurar cabeceras HTTP:

- **Se conserva** la Content-Security-Policy, ahora en un `<meta http-equiv>` en
  cada pagina. El sitio no carga nada de terceros, asi que sigue siendo `'self'`
  estricta, sin `unsafe-inline`.
- **Se pierde** `frame-ancestors` (y su equivalente `X-Frame-Options`): el
  navegador solo los acepta como cabecera, no en un `<meta>`. El sitio es
  embebible en un iframe ajeno. Al ser publico y sin sesion ni formularios, no
  hay nada que un clickjacking pueda aprovechar.
- **Se pierden** HSTS, el rate limit y el saneado de logs. Pages fuerza HTTPS y
  envia su propia HSTS en `github.io`, y el resto no aplica sin servidor.
- **Se conserva** el saneado de URLs (`templates/SafeUrl.kt`), que corre ahora en
  tiempo de generacion en vez de en cada peticion.
