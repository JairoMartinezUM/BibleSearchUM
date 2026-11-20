package mx.um.biblesearchum

import mx.um.biblesearchum.data.FavoriteVerse
import mx.um.biblesearchum.model.BibleBook
import mx.um.biblesearchum.model.BibleVerse
import mx.um.biblesearchum.ui.BibleUiState
import mx.um.biblesearchum.util.verseShareText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesAndShareTest {

    private val juan = BibleBook(bookId = 43, name = "Juan", chapters = 21)
    private val juan316 = FavoriteVerse(43, "Juan", 3, 16, "Porque de tal manera amó Dios al mundo")

    @Test
    fun shareText_incluyeCitaYReferencia() {
        assertEquals(
            "\"Porque de tal manera amó Dios al mundo\"\n— Juan 3:16 (RV1960)",
            verseShareText(juan316.reference, " Porque de tal manera amó Dios al mundo ")
        )
    }

    @Test
    fun favorite_referenciaYClave() {
        assertEquals("Juan 3:16", juan316.reference)
        assertEquals("43:3:16", juan316.key)
    }

    @Test
    fun uiState_sinVersiculo_noHayFavoritoActual() {
        assertNull(BibleUiState(selectedBook = juan, selectedChapter = 3).currentFavorite)
        assertFalse(BibleUiState().isCurrentFavorite)
    }

    @Test
    fun uiState_detectaSiElVersiculoActualEsFavorito() {
        val state = BibleUiState(
            selectedBook = juan,
            selectedChapter = 3,
            selectedVerse = 16,
            currentVerseContent = BibleVerse(pk = 1, verse = 16, text = " Porque de tal manera amó Dios al mundo ")
        )
        assertEquals(juan316, state.currentFavorite)
        assertFalse(state.isCurrentFavorite)
        assertTrue(state.copy(favorites = listOf(juan316)).isCurrentFavorite)
    }
}
