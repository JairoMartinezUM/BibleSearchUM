package mx.um.biblesearchum.data

import com.google.gson.annotations.SerializedName

/** Versículo guardado por el usuario. Se persiste como JSON en DataStore. */
data class FavoriteVerse(
    @SerializedName("bookId") val bookId: Int,
    @SerializedName("bookName") val bookName: String,
    @SerializedName("chapter") val chapter: Int,
    @SerializedName("verse") val verse: Int,
    @SerializedName("text") val text: String
) {
    /** Cita legible, por ejemplo "Juan 3:16". */
    val reference: String get() = "$bookName $chapter:$verse"

    /** Identificador único del pasaje (libro:capítulo:versículo). */
    val key: String get() = "$bookId:$chapter:$verse"
}
