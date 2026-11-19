
package com.example.cse226

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cse226.ui.theme.CSE226Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// MODEL
data class CA3Movie(
    val title: String,
    val genre: String
)

// REPOSITORY
class CA3MovieRepository {

    private val _movies =
        MutableStateFlow<List<CA3Movie>>(emptyList())

    val movies = _movies.asStateFlow()

    private val _count = MutableStateFlow(0)
    val count = _count.asStateFlow()

    fun addMovie(movie: CA3Movie) {
        _movies.value = _movies.value + movie
        _count.value = _movies.value.size
    }

    fun getAllMovies() = movies
}

// VIEWMODEL
class CA3MovieViewModel : ViewModel() {

    private val repository = CA3MovieRepository()

    val movies = repository.getAllMovies()
    val count = repository.count

    fun addMovie(title: String, genre: String) {
        repository.addMovie(CA3Movie(title, genre))
    }
}

// MAIN ACTIVITY
class CA3 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CSE226Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->

                    CA3MovieScreen(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

// COMPOSE SCREEN
@Composable
fun CA3MovieScreen(
    modifier: Modifier = Modifier,
    vm: CA3MovieViewModel = viewModel()
) {

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }

    val movies by vm.movies.collectAsState()
    val count by vm.count.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Movie Watchlist",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Movie Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text(text = "Movie Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (title.isNotBlank() && genre.isNotBlank()) {
                    vm.addMovie(title, genre)
                    title = ""
                    genre = ""
                }
            }
        ) {
            Text(text = "Add Movie")
        }

        Text(text = "Total Movies: $count")

        LazyColumn {
            items(movies) { movie ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(text = "Title: ${movie.title}")
                        Text(text = "Genre: ${movie.genre}")
                    }
                }
            }
        }
    }
}
