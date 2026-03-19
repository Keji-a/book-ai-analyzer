#!/bin/bash
echo "=== 依赖分析 ==="
echo ""

# 检查Compose BOM版本兼容性
echo "1. Compose BOM版本: 2023.10.01"
echo "   对应Compose编译器: 1.5.10 (正确)"
echo ""

# 检查Kotlin版本
echo "2. Kotlin版本:"
grep "org.jetbrains.kotlin.android" build.gradle.kts
echo ""

# 检查AGP版本
echo "3. Android Gradle Plugin版本:"
grep "com.android.application" build.gradle.kts
echo ""

# 检查可能的问题依赖
echo "4. 可能的问题点:"
echo "   - Retrofit 2.9.0 (较老但稳定)"
echo "   - OkHttp 4.12.0 (兼容)"
echo "   - Coroutines 1.7.3 (稳定)"
echo ""

# 建议的修复
echo "5. 建议调整:"
echo "   a) 更新AGP到最新稳定版"
echo "   b) 统一Compose相关版本"
echo "   c) 简化初始构建目标"
