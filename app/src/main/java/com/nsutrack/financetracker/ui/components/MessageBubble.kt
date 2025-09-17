package com.nsutrack.financetracker.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsutrack.financetracker.data.ChatMessage
import com.nsutrack.financetracker.data.MessageRole
import com.nsutrack.financetracker.ui.theme.Yellow
import kotlinx.coroutines.delay

@Composable
fun MessageBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    var isVisible by remember(message.id) { mutableStateOf(false) }
    
    LaunchedEffect(message.id) {
        if (!isVisible) {
            delay(100)
            isVisible = true
        }
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { if (message.role == MessageRole.USER) it else -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = if (message.role == MessageRole.USER) {
                Arrangement.End
            } else {
                Arrangement.Start
            }
        ) {
            if (message.role == MessageRole.USER) {
                UserMessageBubble(message = message)
            } else {
                AssistantMessageBubble(message = message)
            }
        }
    }
}

@Composable
private fun UserMessageBubble(message: ChatMessage) {
    Card(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .padding(start = 48.dp),
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = 20.dp,
            bottomEnd = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Yellow.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = message.content,
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun AssistantMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .widthIn(max = 320.dp)
            .padding(end = 48.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Gemini Logo/Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4285F4),
                            Color(0xFF34A853),
                            Color(0xFFEA4335),
                            Color(0xFFFBBC05)
                        )
                    )
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
            shape = RoundedCornerShape(
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
            if (message.isTyping) {
                TypingIndicator()
            } else {
                TypewriterText(
                    text = message.content,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val animatedAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(index * 200)
                ),
                label = "dot_alpha_$index"
            )
            
            Canvas(
                modifier = Modifier.size(8.dp)
            ) {
                drawCircle(
                    color = Color.Gray.copy(alpha = animatedAlpha),
                    radius = 4.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
            
            if (index < 2) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    lineHeight: androidx.compose.ui.unit.TextUnit = 22.sp,
    typingSpeed: Long = 30L
) {
    var displayedText by remember(text) { mutableStateOf("") }
    var hasAnimated by remember(text) { mutableStateOf(false) }
    
    LaunchedEffect(text) {
        if (!hasAnimated) {
            displayedText = ""
            text.forEachIndexed { index, _ ->
                displayedText = text.substring(0, index + 1)
                delay(typingSpeed)
            }
            hasAnimated = true
        } else {
            // If already animated, show full text immediately
            displayedText = text
        }
    }
    
    Text(
        text = displayedText,
        color = color,
        fontSize = fontSize,
        lineHeight = lineHeight,
        modifier = modifier
    )
}