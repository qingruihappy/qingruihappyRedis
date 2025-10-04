# Filter 和 Interceptor 执行流程示例

本项目演示了 Spring MVC 中 **三个过滤器（Filter）** 和 **三个拦截器（Interceptor）** 的执行流程，包括异常处理的各种场景。

## 项目结构

```
filter-interceptor-demo/
├── src/main/java/com/example/demo/
│   ├── FilterInterceptorDemoApplication.java  # 主应用类
│   ├── config/
│   │   └── WebConfig.java                     # Web配置（注册Filter和Interceptor）
│   ├── filter/
│   │   ├── FirstFilter.java                   # 第一个过滤器
│   │   ├── SecondFilter.java                  # 第二个过滤器（可控制异常）
│   │   └── ThirdFilter.java                   # 第三个过滤器
│   ├── interceptor/
│   │   ├── FirstInterceptor.java              # 第一个拦截器
│   │   ├── SecondInterceptor.java             # 第二个拦截器（可控制异常）
│   │   └── ThirdInterceptor.java              # 第三个拦截器
│   └── controller/
│       └── TestController.java                # 测试控制器
├── src/main/resources/
│   └── application.properties                 # 应用配置
├── pom.xml                                    # Maven配置
└── sequence-diagram.puml                       # 时序图（PlantUML格式）
```

## 核心概念

### Filter（过滤器）
- **作用于**: Servlet容器级别，在请求到达DispatcherServlet之前执行
- **执行顺序**: 按照注册顺序执行（Order: 1, 2, 3）
- **生命周期**: init() → doFilter() → destroy()
- **异常处理**: 必须在Filter内部捕获处理，否则会中断整个过滤器链

### Interceptor（拦截器）
- **作用于**: Spring MVC级别，在DispatcherServlet内部执行
- **执行顺序**: 
  - preHandle(): 按注册顺序执行（1→2→3）
  - postHandle(): 倒序执行（3→2→1）
  - afterCompletion(): 倒序执行（3→2→1）
- **异常处理**: 
  - 如果preHandle()抛异常，只有已执行的拦截器会调用afterCompletion()
  - postHandle()在Controller抛异常时不会执行
  - afterCompletion()总是会执行（对于已执行preHandle的拦截器）

## 执行流程

### 完整执行顺序（正常情况）

```
1. FirstFilter 前置处理
2. SecondFilter 前置处理
3. ThirdFilter 前置处理
4. DispatcherServlet 接收请求
5. FirstInterceptor.preHandle()
6. SecondInterceptor.preHandle()
7. ThirdInterceptor.preHandle()
8. Controller 处理请求
9. ThirdInterceptor.postHandle()      ← 倒序
10. SecondInterceptor.postHandle()    ← 倒序
11. FirstInterceptor.postHandle()     ← 倒序
12. ThirdInterceptor.afterCompletion() ← 倒序
13. SecondInterceptor.afterCompletion()← 倒序
14. FirstInterceptor.afterCompletion() ← 倒序
15. ThirdFilter 后置处理              ← 倒序
16. SecondFilter 后置处理             ← 倒序
17. FirstFilter 后置处理              ← 倒序
```

## 测试场景

### 场景1: 正常流程（无异常）

**测试命令**:
```bash
curl http://localhost:8080/test
```

**执行流程**:
- 所有Filter前置处理 → DispatcherServlet → 所有Interceptor的preHandle → Controller → Interceptor的postHandle（倒序） → Interceptor的afterCompletion（倒序） → Filter后置处理（倒序）

**控制台输出**:
```
========== FirstFilter 前置处理 ==========
========== SecondFilter 前置处理 ==========
========== ThirdFilter 前置处理 ==========
---------- FirstInterceptor preHandle ----------
---------- SecondInterceptor preHandle ----------
---------- ThirdInterceptor preHandle ----------
########## Controller 处理请求 ##########
---------- ThirdInterceptor postHandle ----------
---------- SecondInterceptor postHandle ----------
---------- FirstInterceptor postHandle ----------
---------- ThirdInterceptor afterCompletion (正常) ----------
---------- SecondInterceptor afterCompletion (正常) ----------
---------- FirstInterceptor afterCompletion (正常) ----------
========== ThirdFilter 后置处理 ==========
========== SecondFilter 后置处理 ==========
========== FirstFilter 后置处理 ==========
```

---

### 场景2: SecondFilter 抛出异常

**测试命令**:
```bash
curl "http://localhost:8080/test?throwFilterException=true"
```

**执行流程**:
- FirstFilter前置 → SecondFilter前置（抛异常）→ FirstFilter捕获异常 → 返回错误响应

**特点**:
1. SecondFilter抛异常后，ThirdFilter、DispatcherServlet、所有Interceptor、Controller都不会执行
2. FirstFilter的catch块捕获异常并处理
3. FirstFilter的后置处理不会执行（因为异常被捕获）
4. SecondFilter的后置处理不会执行

**控制台输出**:
```
========== FirstFilter 前置处理 ==========
========== SecondFilter 前置处理 ==========
========== SecondFilter 抛出异常！==========
========== FirstFilter 捕获到异常: SecondFilter 故意抛出的异常 ==========
```

**响应**:
```
Filter捕获异常: SecondFilter 故意抛出的异常
```

---

### 场景3: SecondInterceptor 抛出异常

**测试命令**:
```bash
curl "http://localhost:8080/test?throwInterceptorException=true"
```

**执行流程**:
- 所有Filter前置 → DispatcherServlet → FirstInterceptor.preHandle() → SecondInterceptor.preHandle()（抛异常）→ FirstInterceptor.afterCompletion()（携带异常信息）→ 所有Filter后置处理

**特点**:
1. SecondInterceptor在preHandle中抛异常
2. ThirdInterceptor的preHandle、所有Interceptor的postHandle、Controller都不会执行
3. **只有FirstInterceptor的afterCompletion会执行**（因为只有它的preHandle成功执行了）
4. SecondInterceptor和ThirdInterceptor的afterCompletion不会执行
5. 所有Filter的后置处理仍然会正常执行

**控制台输出**:
```
========== FirstFilter 前置处理 ==========
========== SecondFilter 前置处理 ==========
========== ThirdFilter 前置处理 ==========
---------- FirstInterceptor preHandle ----------
---------- SecondInterceptor preHandle ----------
---------- SecondInterceptor 抛出异常！----------
---------- FirstInterceptor afterCompletion (检测到异常: SecondInterceptor 故意抛出的异常) ----------
========== ThirdFilter 后置处理 ==========
========== SecondFilter 后置处理 ==========
========== FirstFilter 后置处理 ==========
```

---

## 关键区别总结

| 特性 | Filter | Interceptor |
|------|--------|-------------|
| 级别 | Servlet容器级别 | Spring MVC级别 |
| 依赖 | 依赖Servlet API | 依赖Spring框架 |
| 执行位置 | DispatcherServlet之前 | DispatcherServlet内部 |
| 异常处理 | 必须自己catch，否则中断整个链 | Spring会自动处理，触发afterCompletion |
| 后置执行 | 如果前置抛异常（未捕获），后置不执行 | afterCompletion总是执行（对于已执行preHandle的） |
| 访问Spring容器 | 不方便 | 可以直接访问 |
| 适用场景 | 编码、跨域、日志、安全过滤 | 权限校验、日志记录、事务处理 |

## 异常处理机制

### Filter异常处理
```
如果Filter抛异常且未捕获：
├─ 当前Filter链中断
├─ 后续Filter不执行
├─ DispatcherServlet不执行
├─ 所有Interceptor不执行
└─ 返回500错误

如果Filter捕获异常：
├─ 可以自定义错误响应
├─ 可以选择是否继续执行后续Filter
└─ 灵活性更高
```

### Interceptor异常处理
```
如果preHandle()抛异常：
├─ 后续Interceptor的preHandle()不执行
├─ 所有Interceptor的postHandle()不执行
├─ Controller不执行
├─ 已执行preHandle()的Interceptor的afterCompletion()会执行（携带异常）
└─ Filter的后置处理正常执行

如果Controller抛异常：
├─ 所有Interceptor的postHandle()不执行
├─ 所有Interceptor的afterCompletion()会执行（携带异常）
└─ 可以在afterCompletion中处理异常
```

## 运行项目

1. **编译项目**:
```bash
cd /workspace/filter-interceptor-demo
mvn clean package
```

2. **运行项目**:
```bash
mvn spring-boot:run
```

3. **测试各种场景**:
```bash
# 正常流程
curl http://localhost:8080/test

# Filter异常
curl "http://localhost:8080/test?throwFilterException=true"

# Interceptor异常
curl "http://localhost:8080/test?throwInterceptorException=true"
```

## 时序图说明

项目包含详细的PlantUML时序图（`sequence-diagram.puml`），展示了所有场景的完整流程：

1. **场景1**: 正常流程（无异常）
2. **场景2**: SecondFilter抛出异常
3. **场景3**: SecondInterceptor抛出异常

可以使用PlantUML工具查看时序图：
- 在线工具: http://www.plantuml.com/plantuml/
- IDE插件: IntelliJ IDEA、VS Code都有PlantUML插件
- 命令行: `plantuml sequence-diagram.puml`

## 关键要点

1. **Filter的执行顺序**由注册时的Order决定，前置和后置都按照这个顺序的"洋葱模型"
2. **Interceptor的preHandle**按注册顺序执行，**postHandle和afterCompletion**倒序执行
3. **Filter异常**会中断整个请求处理流程（如果不捕获）
4. **Interceptor异常**会中断后续Interceptor，但已执行的Interceptor的afterCompletion仍会执行
5. **afterCompletion**是拦截器中唯一能在异常情况下执行的方法，适合做资源清理和日志记录
6. **DispatcherServlet**是Spring MVC的核心，负责协调Interceptor和Controller的执行

## DispatcherServlet的作用

1. 接收所有HTTP请求（从Filter链传递过来）
2. 查找合适的Handler（Controller方法）
3. 调用所有Interceptor的preHandle()
4. 执行Handler（Controller方法）
5. 调用所有Interceptor的postHandle()
6. 处理视图渲染
7. 调用所有Interceptor的afterCompletion()
8. 返回响应给Filter链

DispatcherServlet是连接Filter和Interceptor的桥梁，它在Filter之后执行，负责管理整个Spring MVC的请求处理流程。
