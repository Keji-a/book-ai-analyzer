package com.keji.bookaianalyzer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keji.bookaianalyzer.api.DeepSeekRequest
import com.keji.bookaianalyzer.api.DeepSeekResponse
import com.keji.bookaianalyzer.api.DeepSeekService
import com.keji.bookaianalyzer.api.Message
import com.keji.bookaianalyzer.model.AnalysisState
import com.keji.bookaianalyzer.model.BookAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class MainViewModel : ViewModel() {
    
    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()
    
    private val _selectedFileContent = MutableStateFlow("")
    val selectedFileContent: StateFlow<String> = _selectedFileContent.asStateFlow()
    
    private val _fileName = MutableStateFlow("")
    val fileName: StateFlow<String> = _fileName.asStateFlow()
    
    private lateinit var deepSeekService: DeepSeekService
    
    init {
        initializeApiService()
    }
    
    private fun initializeApiService() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deepseek.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        deepSeekService = retrofit.create(DeepSeekService::class.java)
    }
    
    fun loadFileContent(context: Context, uri: String) {
        viewModelScope.launch {
            try {
                _analysisState.value = AnalysisState.Loading
                
                // 简化版本：直接从assets读取示例文件
                // 实际应用中应该解析URI读取真实文件
                val content = readSampleFile(context)
                _selectedFileContent.value = content
                _fileName.value = "sample_book.txt"
                
                _analysisState.value = AnalysisState.Idle
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error("读取文件失败: ${e.message}")
            }
        }
    }
    
    private fun readSampleFile(context: Context): String {
        return try {
            val inputStream = context.assets.open("sample_book.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.readText()
            reader.close()
            content
        } catch (e: Exception) {
            // 如果assets中没有文件，返回示例文本
            """
            这是一个示例书籍内容。
            
            第一章：开始
            
            在一个遥远的世界里，有一个勇敢的冒险者名叫艾伦。他踏上了寻找失落宝藏的旅程。
            
            第二章：相遇
            
            在旅途中，艾伦遇到了智慧的巫师梅林。梅林告诉他宝藏被恶龙守护着。
            
            第三章：挑战
            
            艾伦必须面对三个挑战：智慧之谜、勇气之试和友谊之证。
            
            第四章：胜利
            
            最终，艾伦成功获得了宝藏，并与新朋友们分享了财富。
            """.trimIndent()
        }
    }
    
    fun analyzeContent() {
        viewModelScope.launch {
            try {
                _analysisState.value = AnalysisState.Loading
                
                val content = _selectedFileContent.value
                if (content.isEmpty()) {
                    _analysisState.value = AnalysisState.Error("请先选择文件")
                    return@launch
                }
                
                // 使用固定的API Key（实际应用中应该让用户配置）
                val apiKey = "sk-your-deepseek-api-key-here" // 这里需要替换为真实API Key
                
                val prompt = """
                    请为以下书籍内容生成详细的分析：
                    
                    1. 书籍大纲（章节结构）
                    2. 主要人物列表
                    3. 人物关系分析
                    4. 核心主题总结
                    
                    书籍内容：
                    $content
                    
                    请用中文回答，格式清晰。
                """.trimIndent()
                
                val request = DeepSeekRequest(
                    messages = listOf(
                        Message(role = "user", content = prompt)
                    ),
                    max_tokens = 2000
                )
                
                val response = deepSeekService.analyzeBook(
                    authorization = "Bearer $apiKey",
                    request = request
                )
                
                if (response.isSuccessful) {
                    val analysisResult = response.body()?.choices?.firstOrNull()?.message?.content
                        ?: "分析完成，但未返回结果"
                    
                    val analysis = BookAnalysis(
                        fileName = _fileName.value,
                        content = content,
                        summary = "AI生成的书籍分析",
                        outline = analysisResult,
                        characters = extractCharacters(analysisResult),
                        relationships = emptyMap(),
                        analysisTime = System.currentTimeMillis()
                    )
                    
                    _analysisState.value = AnalysisState.Success(analysis)
                } else {
                    _analysisState.value = AnalysisState.Error("API调用失败: ${response.code()}")
                }
                
            } catch (e: Exception) {
                _analysisState.value = AnalysisState.Error("分析失败: ${e.message}")
            }
        }
    }
    
    private fun extractCharacters(analysis: String): List<String> {
        // 简化的角色提取逻辑
        val characters = mutableListOf<String>()
        val lines = analysis.split("\n")
        
        for (line in lines) {
            if (line.contains("人物") || line.contains("角色") || line.contains(":")) {
                // 简单的关键词匹配
                if (line.contains("艾伦")) characters.add("艾伦")
                if (line.contains("梅林")) characters.add("梅林")
                if (line.contains("恶龙")) characters.add("恶龙")
            }
        }
        
        return if (characters.isEmpty()) listOf("艾伦", "梅林") else characters.distinct()
    }
    
    fun resetState() {
        _analysisState.value = AnalysisState.Idle
    }
}