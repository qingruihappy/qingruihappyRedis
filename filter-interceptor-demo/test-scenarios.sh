#!/bin/bash

# Filter和Interceptor测试脚本

echo "======================================"
echo "Filter 和 Interceptor 执行流程测试"
echo "======================================"
echo ""

BASE_URL="http://localhost:8080"

echo "请确保应用已启动（mvn spring-boot:run）"
echo ""
read -p "按Enter键开始测试..."
echo ""

# 场景1: 正常流程
echo "=========================================="
echo "场景1: 正常流程（无异常）"
echo "=========================================="
echo "请求: GET $BASE_URL/test"
echo ""
curl -w "\n\n" "$BASE_URL/test"
echo ""
read -p "按Enter键继续下一个测试..."
echo ""

# 场景2: SecondFilter抛出异常
echo "=========================================="
echo "场景2: SecondFilter 抛出异常"
echo "=========================================="
echo "请求: GET $BASE_URL/test?throwFilterException=true"
echo ""
curl -w "\n\n" "$BASE_URL/test?throwFilterException=true"
echo ""
read -p "按Enter键继续下一个测试..."
echo ""

# 场景3: SecondInterceptor抛出异常
echo "=========================================="
echo "场景3: SecondInterceptor 抛出异常"
echo "=========================================="
echo "请求: GET $BASE_URL/test?throwInterceptorException=true"
echo ""
curl -w "\n\n" "$BASE_URL/test?throwInterceptorException=true"
echo ""

echo "=========================================="
echo "所有测试完成！"
echo "=========================================="
echo ""
echo "提示："
echo "1. 查看控制台输出可以看到详细的执行流程"
echo "2. 对比三个场景的输出可以理解Filter和Interceptor的异常处理机制"
echo "3. 查看 sequence-diagram.puml 可以看到完整的时序图"
