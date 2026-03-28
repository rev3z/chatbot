# langgraph-skeleton

这部分是基于 `langgraph-interf` 的空实现骨架，用于验证下面的工程形态是否顺手：

- `LangGraph4j` 做图编排
- `Spring Boot` 做 Bean 装配
- 业务节点只依赖接口
- `gRPC` 官方 Java 库只在 adapter 层出现

这里不包含任何真实业务逻辑，只保留最小实现骨架和依赖方向。

## 包结构

- `config`
  - Spring Boot 风格的装配入口骨架
- `node`
  - 示例节点骨架
- `adapter.grpc`
  - `grpc-java` 适配器骨架
- `adapter.memory`
  - 内存版状态存储骨架

## 使用建议

建议把这里看成“未来生产代码的 shape”，而不是最终实现。

如果这套 shape 对团队是顺手的，后面再决定：

1. 是否补成真正的 Maven/Gradle 工程
2. 是否接入 `LangGraph4j` 的 `StateGraph`
3. 是否把 `Tool`/`Skill` 改成注解自动注册
