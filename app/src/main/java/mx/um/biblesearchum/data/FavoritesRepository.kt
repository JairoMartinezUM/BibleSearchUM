package mx.um.biblesearchum.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore by preferencesDataStore(name = "favorites")

/** Guarda y consulta los versículos favoritos usando Preferences DataStore. */
class FavoritesRepository(private val context: Context) {

    private val favoritesKey = stringPreferencesKey("favorites_json")
    private val gson = Gson()

    /** Lista de favoritos, del más reciente al más antiguo. */
    val favorites: Flow<List<FavoriteVerse>> =
        context.favoritesDataStore.data.map { decode(it[favoritesKey]) }

    /** Agrega el versículo si no estaba guardado; si ya lo estaba, lo quita. */
    suspend fun toggle(verse: FavoriteVerse) {
        context.favoritesDataStore.edit { prefs ->
            val list = decode(prefs[favoritesKey]).toMutableList()
            val index = list.indexOfFirst { it.key == verse.key }
            if (index >= 0) list.removeAt(index) else list.add(0, verse)
            prefs[favoritesKey] = gson.toJson(list)
        }
    }

    suspend fun remove(verse: FavoriteVerse) {
        context.favoritesDataStore.edit { prefs ->
            val list = decode(prefs[favoritesKey]).filterNot { it.key == verse.key }
            prefs[favoritesKey] = gson.toJson(list)
        }
    }

    private fun decode(json: String?): List<FavoriteVerse> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            gson.fromJson(json, Array<FavoriteVerse>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
