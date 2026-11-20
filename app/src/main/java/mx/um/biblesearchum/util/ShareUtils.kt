package mx.um.biblesearchum.util

import android.content.Context
import android.content.Intent

/** Texto que se envía al compartir: la cita textual y su referencia. */
fun verseShareText(reference: String, text: String): String =
    "\"${text.trim()}\"\n— $reference (RV1960)"

/** Abre el selector del sistema para compartir el versículo con Intent.ACTION_SEND. */
fun shareVerse(context: Context, reference: String, text: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, reference)
        putExtra(Intent.EXTRA_TEXT, verseShareText(reference, text))
    }
    context.startActivity(Intent.createChooser(send, "Compartir versículo"))
}
