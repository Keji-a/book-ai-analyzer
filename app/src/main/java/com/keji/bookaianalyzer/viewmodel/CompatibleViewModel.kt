package com.keji.bookaianalyzer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompatibleViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    
    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> = _fileContent.asStateFlow()
    
    private val _fileName = MutableStateFlow("")
    val fileName: StateFlow<String> = _fileName.asStateFlow()
    
    fun loadSampleContent() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            
            // 模拟加载文件内容
            val sampleContent = """
                《兼容性测试书籍》
                
                第一章：开始
                这是一个测试书籍内容，用于验证应用基础功能。
                
                第二章：功能
                应用包含文件加载、内容显示和基础UI。
                
                第三章：AI分析
                AI分析功能需要网络支持，当前为本地模拟版本。
                
                注意：这是一个兼容性测试版本，确保能在GitHub Actions环境中构建成功。
            """.trimIndent()
            
            _fileContent.value = sampleContent
            _fileName.value = "sample_book.txt"
            _uiState.value = UIState.Success("文件加载成功")
        }
    }
    
    fun analyzeContent() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            
            // 模拟AI分析结果
            val analysisResult = """
                📚 书籍分析结果（模拟）
                
                📖 书籍信息：
                - 书名：兼容性测试书籍
                - 章节数：3章
                - 总字数：约200字
                
                🎯 核心脉络：
                1. 开始阶段 - 介绍测试目的
                2. 功能展示 - 展示应用能力
                3. AI分析 - 说明后续功能
                
                👥 人物分析：
                - 无具体人物（测试内容）
                
                💡 主题分析：
                - 技术兼容性测试
                - 功能验证
                - 后续扩展说明
                
                📝 说明：
                这是一个本地模拟版本，真实AI分析功能将在后续版本中添加。
                当前版本确保能在GitHub Actions环境中成功构建。
            """.trimIndent()
            
            _uiState.value = UIState.AnalysisResult(analysisResult)
        }
    }
}

sealed class UIState {
    data object Idle : UIState()
    data object Loading : UIState()
    data class Success(val message: String) : UIState()
    data class Error(val message: String) : UIState()
    data class AnalysisResult(val result: String) : UIState()
}