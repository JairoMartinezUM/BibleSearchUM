# BibleSearchUM

App Android hecha con **Jetpack Compose** y **Material 3** que consulta pasajes bíblicos
(versión Reina-Valera 1960) mediante la API REST de [Bolls Bible](https://bolls.life/).
Se elige **Libro → Capítulo → Versículo** con menús desplegables dependientes y se muestra el texto.
Incluye **compartir** el versículo y una lista de **favoritos** guardada en el dispositivo.

## Arquitectura (MVVM)

```
app/src/main/java/mx/um/biblesearchum/
├── MainActivity.kt                 # Punto de entrada; monta BibleSearchScreen
├── model/BibleModels.kt            # BibleBook, BibleVerse (Gson)
├── data/                           # FavoriteVerse + FavoritesRepository (Preferences DataStore)
├── util/ShareUtils.kt              # Compartir con Intent.ACTION_SEND
├── network/BibleApiService.kt      # Retrofit: interfaz + RetrofitInstance
└── ui/
    ├── BibleUiState.kt             # Estado inmutable de la pantalla
    ├── BibleViewModel.kt           # StateFlow + corrutinas (carga, éxito, error)
    ├── BibleSearchScreen.kt        # Pantalla en Compose
    ├── FavoritesScreen.kt          # Pantalla de favoritos
    └── components/DropdownSelector.kt  # Selector reutilizable (ExposedDropdownMenuBox)
```

## Tecnologías

Kotlin 2.2 · Jetpack Compose (Material 3) · ViewModel + StateFlow · Retrofit 2 + Gson ·
Kotlin Coroutines · Preferences DataStore · minSdk 26.

## Endpoints usados

- `GET https://bolls.life/get-books/RV1960/`
- `GET https://bolls.life/get-verse/RV1960/{bookId}/{chapter}/{verse}/`

> El código de traducción es **`RV1960`**. La guía del laboratorio indica `RVR1960`, pero la API
> responde 404 con ese código.

## Cómo ejecutarlo

1. Abre la carpeta en **Android Studio** (Jellyfish o superior) y espera el *Gradle sync*.
2. Elige un emulador o dispositivo con Android 8.0 (API 26) o superior, con internet.
3. Pulsa **Run**. También por terminal:

```bash
./gradlew assembleDebug        # genera app/build/outputs/apk/debug/app-debug.apk
```

`local.properties` (ruta del SDK) lo genera Android Studio y no se versiona.

## Uso

1. Selecciona un **Libro**; se habilita el **Capítulo**.
2. Selecciona el capítulo; se habilita el **Versículo**.
3. Selecciona el versículo para ver el texto. El icono de refrescar reinicia la selección.
4. Se muestran indicador de carga y mensajes de error si falla la red.
5. **Compartir:** el icono de compartir de la tarjeta abre el selector del sistema (`Intent.ACTION_SEND`) con la cita y su referencia.
6. **Favoritos:** el corazón de la tarjeta guarda o quita el versículo; el corazón de la barra superior abre la lista de favoritos, donde se pueden compartir o eliminar. Se conservan al cerrar la app.

## Pruebas

```bash
./gradlew testDebugUnitTest
```
