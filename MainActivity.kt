package com.example.darkai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF101114)
private val Panel = Color(0xFF1B1D22)
private val Accent = Color(0xFF9B87F5)
private val MainText = Color(0xFFF4F4F5)
private val Muted = Color(0xFFAAAAB4)

data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DarkAIApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkAIApp() {
    var input by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("اختيار النموذج") }
    var showModels by remember { mutableStateOf(false) }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("مرحباً! أنا DARK AI. كيف أستطيع مساعدتك اليوم؟", false)
        )
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg, surface = Panel, primary = Accent,
            onBackground = MainText, onSurface = MainText
        )
    ) {
        Scaffold(
            containerColor = Bg,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("DARK AI", fontWeight = FontWeight.Bold, color = MainText)
                            Text("مساعدك الذكي", fontSize = 11.sp, color = Muted)
                        }
                    },
                    actions = {
                        Box {
                            TextButton(onClick = { showModels = true }) {
                                Text(model, color = Accent)
                            }
                            DropdownMenu(
                                expanded = showModels,
                                onDismissRequest = { showModels = false }
                            ) {
                                listOf("اختيار النموذج", "نموذج سريع", "نموذج متقدم").forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = { model = it; showModels = false }
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Bg)
                )
            },
            bottomBar = {
                Column(Modifier.background(Bg).padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = input,
                            onValueChange = { input = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("اكتب رسالتك...", color = Muted) },
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Panel,
                                focusedTextColor = MainText,
                                unfocusedTextColor = MainText
                            )
                        )
                        IconButton(
                            onClick = {
                                if (input.isNotBlank()) {
                                    messages.add(ChatMessage(input.trim(), true))
                                    messages.add(ChatMessage(
                                        "هذه نسخة تجريبية من الواجهة. لربط إجابات الذكاء الاصطناعي، أضف خدمة API آمنة إلى خادم وسيط.",
                                        false
                                    ))
                                    input = ""
                                }
                            },
                            modifier = Modifier.background(Accent, CircleShape)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "إرسال", tint = Color.White)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { }) {
                            Icon(Icons.Default.Add, null, tint = Muted)
                            Spacer(Modifier.width(4.dp))
                            Text("مرفقات", color = Muted)
                        }
                        TextButton(onClick = { }) {
                            Icon(Icons.Default.Image, null, tint = Muted)
                            Spacer(Modifier.width(4.dp))
                            Text("صورة", color = Muted)
                        }
                        TextButton(onClick = { }) {
                            Icon(Icons.Default.Mic, null, tint = Muted)
                            Spacer(Modifier.width(4.dp))
                            Text("صوت", color = Muted)
                        }
                    }
                }
            }
        ) { padding ->
            if (messages.size == 1) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("مرحباً بك في", color = Muted, fontSize = 16.sp)
                    Text("DARK AI", color = MainText, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Text("اسأل، تعلّم، ابتكر، واكتب الأكواد.", color = Muted)
                    Spacer(Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SuggestionChip(onClick = { input = "اشرح لي الذكاء الاصطناعي" }, label = { Text("تعلّم") })
                        SuggestionChip(onClick = { input = "ساعدني في كتابة كود" }, label = { Text("برمجة") })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.fromUser) Arrangement.End else Arrangement.Start
                        ) {
                            Surface(
                                color = if (msg.fromUser) Color(0xFF343044) else Panel,
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.widthIn(max = 310.dp)
                            ) {
                                Text(msg.text, modifier = Modifier.padding(14.dp), color = MainText)
                            }
                        }
                    }
                }
            }
        }
    }
}
