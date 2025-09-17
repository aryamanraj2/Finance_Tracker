package com.nsutrack.financetracker.ui.screens.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nsutrack.financetracker.data.MessageRole
import com.nsutrack.financetracker.ui.components.ChatInputField
import com.nsutrack.financetracker.ui.components.MessageBubble
import com.nsutrack.financetracker.ui.components.TypingIndicator
import com.nsutrack.financetracker.ui.theme.Yellow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    val context = LocalContext.current
    val chatState by viewModel.chatState.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    var inputText by remember { mutableStateOf("") }
    
    // Initialize chat when screen opens
    LaunchedEffect(Unit) {
        viewModel.initializeChat(context)
    }
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(
                    index = chatState.messages.size - 1,
                    scrollOffset = 0
                )
            }
        }
    }
    
    // Auto-scroll when keyboard appears/disappears
    LaunchedEffect(inputText) {
        if (chatState.messages.isNotEmpty() && inputText.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(
                    index = chatState.messages.size - 1,
                    scrollOffset = 0
                )
            }
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1C1C1E)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main chat content (adjusts to keyboard)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    .imePadding()
            ) {
                // Header
                ChatHeader(
                    onBackClick = onBackClick,
                    isTyping = chatState.isTyping
                )
                
                // Chat Messages
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp // Reduced padding since input is separate
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                items(
                    items = chatState.messages,
                    key = { it.id }
                ) { message ->
                    MessageBubble(
                        message = message,
                        modifier = Modifier
                    )
                }
                
                // Typing indicator
                if (chatState.isTyping) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .widthIn(max = 320.dp)
                                    .padding(end = 48.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Gemini Avatar
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color(0xFF4285F4),
                                                    Color(0xFF34A853),
                                                    Color(0xFFEA4335),
                                                    Color(0xFFFBBC05)
                                                )
                                            ),
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "G",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Card(
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 20.dp,
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2C2C2E)
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    TypingIndicator()
                                }
                            }
                        }
                    }
                }
                
                }
            }
            
            // Input Field (fixed at bottom)
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = Color(0xFF1C1C1E),
                shadowElevation = 8.dp
            ) {
                ChatInputField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    onSendClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText.trim(), context)
                            inputText = ""
                        }
                    },
                    isLoading = chatState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatHeader(
    onBackClick: () -> Unit,
    isTyping: Boolean
) {
    Surface(
        color = Color(0xFF1C1C1E),
        shadowElevation = 4.dp
    ) {
        TopAppBar(
            title = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Yellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Financial Advisor",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    AnimatedVisibility(
                        visible = isTyping,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Text(
                            text = "Analyzing your finances...",
                            color = Yellow,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.statusBarsPadding()
        )
    }
}

// Quick suggestion chips (optional enhancement)
@Composable
private fun QuickSuggestions(
    onSuggestionClick: (String) -> Unit
) {
    val suggestions = listOf(
        "Analyze my spending",
        "Investment tips",
        "How to save money?",
        "Student finance advice"
    )
    
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "💡 Quick questions:",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        items(suggestions) { suggestion ->
            AssistChip(
                onClick = { onSuggestionClick(suggestion) },
                label = {
                    Text(
                        text = suggestion,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFF2C2C2E),
                    labelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun EmptyStateView(
    onSuggestionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Yellow,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "AI Financial Advisor",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Get personalized financial insights and advice tailored for Indian students",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        QuickSuggestions(onSuggestionClick = onSuggestionClick)
    }
}