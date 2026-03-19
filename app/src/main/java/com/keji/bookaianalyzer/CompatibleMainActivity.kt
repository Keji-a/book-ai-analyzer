package com.keji.bookaianalyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keji.bookaianalyzer.ui.theme.BookAIAnalyzerTheme
import com.keji.bookaianalyzer.viewmodel.CompatibleViewModel
import com.keji.bookaianalyzer.viewmodel.UIState

class CompatibleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookAIAnalyzerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompatibleApp()
                }
            }
        }
    }
}

@Composable
fun CompatibleApp() {
    val viewModel: CompatibleViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val fileContent by viewModel.fileContent.collectAsState()
    val fileName by viewModel.fileName.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "📚 Book AI Analyzer",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "兼容性测试版本",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 文件信息
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📄 当前文件: $fileName",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (fileContent.isNotEmpty()) {
                    Text(
                        text = "内容预览: ${fileContent.take(100)}...",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        // 按钮区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { viewModel.loadSampleContent() },
                modifier = Modifier.weight(1f)
            ) {
                Text("📂 加载示例文件")
            }
            
            Button(
                onClick = { viewModel.analyzeContent() },
                modifier = Modifier.weight(1f),
                enabled = fileContent.isNotEmpty()
            ) {
                Text("🤖 模拟AI分析")
            }
        }
        
        // 状态显示
        when (val state = uiState) {
            is UIState.Idle -> {
                Text(
                    text = "👆 请点击上方按钮开始",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }
            
            is UIState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is UIState.Success -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "✅ ${state.message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            is UIState.Error -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "❌ ${state.message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            is UIState.AnalysisResult -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        Text(
                            text = "📊 分析结果:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    items(state.result.split("\n")) { line ->
                        Text(
                            text = line,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
        
        // 底部说明
        Text(
            text = "💡 这是一个兼容性测试版本，确保能在GitHub Actions环境中成功构建。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}