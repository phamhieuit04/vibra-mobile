package com.example.vibramobile.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.example.vibramobile.viewmodels.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    val searchBarState = rememberSearchBarState()
    val focusRequester = remember { FocusRequester() }

    val searchResult by searchViewModel.searchResult.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier.weight(1f),
                    state = searchBarState,
                    inputField = {
                        SearchBarDefaults.InputField(
                            modifier = Modifier.focusRequester(focusRequester),
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = {
                                scope.launch {
                                    searchViewModel.search(query)
                                }
                            },
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = {
                                Text(
                                    "Nội dung...",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, null, tint = Color.Gray)
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = navigateBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xff2b2930)
                    )
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.Gray)
                }
            }
        }
    ) { padding ->
        Crossfade(targetState = searchResult, modifier = Modifier.padding(padding)) {
            when (it) {
                null -> {
                    EmptyResult()
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun EmptyResult(modifier: Modifier = Modifier) {
    Text(text = "Deo co gi", color = Color.White)
}