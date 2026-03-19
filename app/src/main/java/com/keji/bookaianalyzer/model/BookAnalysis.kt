package com.keji.bookaianalyzer.model

data class BookAnalysis(
    val fileName: String = "",
    val content: String = "",
    val summary: String = "",
    val outline: String = "",
    val characters: List<String> = emptyList(),
    val relationships: Map<String, List<String>> = emptyMap(),
    val analysisTime: Long = 0L
)

sealed class AnalysisState {
    object Idle : AnalysisState()
    object Loading : AnalysisState()
    data class Success(val analysis: BookAnalysis) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}