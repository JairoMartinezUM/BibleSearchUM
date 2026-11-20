package mx.um.biblesearchum.ui

import mx.um.biblesearchum.data.FavoriteVerse
import mx.um.biblesearchum.model.BibleBook
import mx.um.biblesearchum.model.BibleVerse

data class BibleUiState(
    val books: List<BibleBook> = emptyList(),
    val availableChapters: List<Int> = emptyList(),
    val availableVerses: List<Int> = emptyList(),
    val selectedBook: BibleBook? = null,
    val selectedChapter: Int? = null,
    val selectedVerse: Int? = null,
    val currentVerseContent: BibleVerse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val favorites: List<FavoriteVerse> = emptyList()
) {
    /** Versículo mostrado actualmente, listo para guardar o compartir. */
    val currentFavorite: FavoriteVerse?
        get() {
            val book = selectedBook ?: return null
            val chapter = selectedChapter ?: return null
            val verse = currentVerseContent ?: return null
            return FavoriteVerse(book.bookId, book.name, chapter, verse.verse, verse.text.trim())
        }

    val isCurrentFavorite: Boolean
        get() = currentFavorite?.let { current -> favorites.any { it.key == current.key } } ?: false
}
