package mx.um.biblesearchum.ui

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Favorite

import androidx.compose.material.icons.filled.FavoriteBorder

import androidx.compose.material.icons.filled.Refresh

import androidx.compose.material.icons.filled.Share

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext

import androidx.activity.compose.BackHandler

import mx.um.biblesearchum.util.shareVerse

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.font.FontStyle

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import mx.um.biblesearchum.ui.components.DropdownSelector

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun BibleSearchScreen(viewModel: BibleViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var showFavorites by rememberSaveable { mutableStateOf(false) }

    if (showFavorites) {
        BackHandler { showFavorites = false }
        FavoritesScreen(
            favorites = uiState.favorites,
            onBack = { showFavorites = false },
            onRemove = { viewModel.removeFavorite(it) },
            onShare = { shareVerse(context, it.reference, it.text) }
        )
        return
    }

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = {

                    Text(

                        text = "BibleSearchUM",

                        fontWeight = FontWeight.Bold,

                        color = MaterialTheme.colorScheme.onPrimary

                    )

                },

                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor = MaterialTheme.colorScheme.primary

                ),

                actions = {

                    IconButton(onClick = { showFavorites = true }) {

                        Icon(

                            imageVector = Icons.Default.Favorite,

                            contentDescription = "Ver favoritos",

                            tint = MaterialTheme.colorScheme.onPrimary

                        )

                    }

                    IconButton(onClick = { viewModel.clearSelection() }) {

                        Icon(

                            imageVector = Icons.Default.Refresh,

                            contentDescription = "Reiniciar",

                            tint = MaterialTheme.colorScheme.onPrimary

                        )

                    }

                }

            )

        }

    ) { paddingValues ->

        Column(

            modifier = Modifier

                .fillMaxSize()

                .padding(paddingValues)

                .padding(16.dp)

                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            // Controles de Selección

            DropdownSelector(

                label = "Libro",

                options = uiState.books,

                selectedItem = uiState.selectedBook,

                onItemSelected = { viewModel.onBookSelected(it) },

                itemLabelProvider = { it.name }

            )

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                DropdownSelector(

                    label = "Capítulo",

                    options = uiState.availableChapters,

                    selectedItem = uiState.selectedChapter,

                    onItemSelected = { viewModel.onChapterSelected(it) },

                    itemLabelProvider = { it.toString() },

                    enabled = uiState.selectedBook != null,

                    modifier = Modifier.weight(1f)

                )

                DropdownSelector(

                    label = "Versículo",

                    options = uiState.availableVerses,

                    selectedItem = uiState.selectedVerse,

                    onItemSelected = { viewModel.onVerseSelected(it) },

                    itemLabelProvider = { it.toString() },

                    enabled = uiState.selectedChapter != null,

                    modifier = Modifier.weight(1f)

                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Indicadores de Estado

            if (uiState.isLoading) {

                CircularProgressIndicator(modifier = Modifier.padding(16.dp))

            }

            uiState.errorMessage?.let { error ->

                Card(

                    colors = CardDefaults.cardColors(

                        containerColor = MaterialTheme.colorScheme.errorContainer

                    ),

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Text(

                        text = error,

                        color = MaterialTheme.colorScheme.onErrorContainer,

                        modifier = Modifier.padding(16.dp),

                        textAlign = TextAlign.Center

                    )

                }

            }

            // Visualización del Versículo

            uiState.currentVerseContent?.let { verseData ->

                Card(

                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                    colors = CardDefaults.cardColors(

                        containerColor = MaterialTheme.colorScheme.surfaceVariant

                    ),

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Column(

                        modifier = Modifier.padding(20.dp),

                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Text(

                            text = "${uiState.selectedBook?.name} ${uiState.selectedChapter}:${verseData.verse}",

                            fontSize = 18.sp,

                            fontWeight = FontWeight.Bold,

                            color = MaterialTheme.colorScheme.primary

                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "\"${verseData.text.trim()}\"",
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { viewModel.toggleFavorite() }) {
                                Icon(
                                    imageVector = if (uiState.isCurrentFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (uiState.isCurrentFavorite) "Quitar de favoritos" else "Guardar en favoritos",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = {
                                uiState.currentFavorite?.let { shareVerse(context, it.reference, it.text) }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir versículo",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                    }

                }

            }

        }

    }

}
