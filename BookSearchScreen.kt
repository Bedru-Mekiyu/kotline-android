package com.example.bookswap.ui.Search



import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookswap.data.AppDatabase
import com.example.bookswap.data.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BookSearchScreen(navController: NavController, context: Context) {
    val db = remember { AppDatabase.getDatabase(context).bookDao() }
    var books by remember { mutableStateOf(listOf<Book>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            books = db.getAllBooks()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                CoroutineScope(Dispatchers.IO).launch {
                    books = db.searchBooks(searchQuery)
                }
            },
            label = { Text("Search by Title or Author") },
            trailingIcon = {
                IconButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        books = db.searchBooks(searchQuery)
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuComponent(
            label = "Select Genre",
            options = listOf("Fiction", "Non-Fiction", "Mystery", "Science Fiction"),
            selectedOption = selectedGenre,
            onOptionSelected = { genre ->
                selectedGenre = genre
                CoroutineScope(Dispatchers.IO).launch {
                    books = db.filterBooksByGenre(genre)
                }
            }
        )

        DropdownMenuComponent(
            label = "Select Condition",
            options = listOf("New", "Good", "Fair", "Old"),
            selectedOption = selectedCondition,
            onOptionSelected = { condition ->
                selectedCondition = condition
                CoroutineScope(Dispatchers.IO).launch {
                    books = db.filterBooksByCondition(condition)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(books) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { navController.navigate("book_details/${book.id}") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(book.title, fontWeight = FontWeight.Bold)
                        Text(book.author)
                    }
                }
            }
        }
    }
}

private fun T.filterBooksByCondition(value: Any): List<Book> {}

@Composable
fun DropdownMenuComponent(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (ERROR) -> ERROR
) {
    TODO("Not yet implemented")
}
