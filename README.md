# Book AI Analyzer

一个Android应用，使用AI技术分析书籍内容，生成大纲、人物关系和主题总结。

## 功能特性

### ✅ 已实现
- 现代Material Design界面（Jetpack Compose）
- 示例TXT文件加载
- DeepSeek API集成框架
- 书籍内容分析功能
- 分析结果展示界面

### 🔧 需要配置
- DeepSeek API Key（在`MainViewModel.kt`中配置）
- 真实的文件选择器集成
- EPUB文件支持

## 快速开始

### 方法1：Android Studio构建
1. 用Android Studio打开本项目
2. 在`MainViewModel.kt`中配置你的DeepSeek API Key：
   ```kotlin
   val apiKey = "sk-your-deepseek-api-key-here" // 替换为真实Key
   ```
3. 连接Android设备或启动模拟器
4. 点击运行按钮（▶️）

### 方法2：GitHub Actions自动构建
1. 将项目推送到GitHub仓库
2. GitHub Actions会自动构建APK
3. 从Releases页面下载APK

### 方法3：手动构建
```bash
# 确保已安装Android SDK
./gradlew assembleDebug    # 构建调试版
./gradlew assembleRelease  # 构建发布版
```

## 项目结构

```
book-ai-analyzer/
├── app/
│   ├── src/main/java/com/keji/bookaianalyzer/
│   │   ├── MainActivity.kt          # 主界面
│   │   ├── viewmodel/MainViewModel.kt # 业务逻辑
│   │   ├── api/DeepSeekService.kt   # API接口
│   │   ├── model/BookAnalysis.kt    # 数据模型
│   │   └── ui/theme/                # 主题和样式
│   ├── src/main/assets/             # 资源文件
│   │   └── sample_book.txt          # 示例书籍
│   └── build.gradle.kts             # 模块配置
├── build.gradle.kts                  # 项目配置
├── settings.gradle.kts               # 项目设置
└── .github/workflows/                # CI/CD配置
    └── android-build.yml             # 自动构建
```

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM + Repository
- **网络**: Retrofit + OkHttp
- **依赖管理**: Gradle Kotlin DSL
- **最低Android版本**: 8.0 (API 26)

## 使用说明

1. **启动应用** - 显示欢迎界面和使用说明
2. **选择文件** - 点击"选择TXT文件"按钮（当前加载示例文件）
3. **预览内容** - 查看文件内容预览
4. **AI分析** - 点击"AI分析"按钮生成分析结果
5. **查看结果** - 查看生成的大纲、人物列表等

## 配置说明

### API Key配置
在`MainViewModel.kt`的第85行修改API Key：
```kotlin
val apiKey = "sk-your-deepseek-api-key-here"
```

### 支持的文件格式
- ✅ TXT文件（已实现）
- ⏳ EPUB文件（计划中）
- ⏳ PDF文件（计划中）

### 权限要求
- 网络权限（API调用）
- 存储权限（文件访问）

## 开发计划

### 版本1.0（当前）
- [x] 基础项目框架
- [x] 示例文件加载
- [x] DeepSeek API集成
- [x] 基础UI界面

### 版本1.1（计划中）
- [ ] 真实文件选择器
- [ ] 自定义API配置界面
- [ ] 数据本地保存
- [ ] EPUB文件支持

### 版本1.2（计划中）
- [ ] 人物关系可视化
- [ ] 阅读进度跟踪
- [ ] 数据导出功能
- [ ] 主题切换

## 注意事项

1. **API限制**：需要有效的DeepSeek API Key
2. **文件大小**：大文件可能需要分段处理
3. **网络要求**：需要稳定的网络连接进行API调用
4. **权限提示**：首次使用需要授予存储权限

## 问题反馈

如果遇到问题，请检查：
1. API Key是否正确配置
2. 网络连接是否正常
3. 存储权限是否已授予
4. Android版本是否兼容（最低8.0）

## 许可证

MIT License# Trigger build
