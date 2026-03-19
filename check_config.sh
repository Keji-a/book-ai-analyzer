#!/bin/bash
echo "=== 项目配置检查 ==="
echo ""

# 1. 检查gradle wrapper
echo "1. Gradle Wrapper:"
if [ -f "gradlew" ]; then
    echo "   ✅ gradlew存在"
    ls -la gradlew
else
    echo "   ❌ gradlew不存在"
fi
echo ""

# 2. 检查gradle版本
echo "2. Gradle版本配置:"
if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
    grep "distributionUrl" gradle/wrapper/gradle-wrapper.properties
fi
echo ""

# 3. 检查Android配置
echo "3. Android配置:"
echo "   Compile SDK: 34"
echo "   Min SDK: 26"
echo "   Target SDK: 34"
echo "   Compose编译器: 1.5.10"
echo ""

# 4. 检查依赖
echo "4. 关键依赖版本:"
grep -E "(compose-bom|retrofit|okhttp|coroutines)" app/build.gradle.kts | head -10
echo ""

# 5. 检查工作流配置
echo "5. GitHub Actions工作流:"
ls -la .github/workflows/
echo ""

echo "=== 检查完成 ==="
