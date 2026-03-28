# OpenAI SSE -> gRPC -> Streamable HTTP 最小原型

这个原型用于验证两个问题：

1. 模拟节点能不能把上游 `OpenAI 兼容 SSE` 消费后，转成内部 `gRPC stream`。
2. Spring Boot 网关能不能把客户端的 `Streamable HTTP` 请求转成 `gRPC`，再把结果转回 `Streamable HTTP` 给客户端。

答案是：**可以。**

这个最小原型里：

- 客户端和网关之间统一使用 `Streamable HTTP`
- 具体协议用 `application/x-ndjson` 来模拟
- 模拟节点和上游模型之间使用 `OpenAI 兼容 SSE`

## 目录结构

```text
grpc-sse-prototype
├── pom.xml
├── scripts
├── src/main/proto/chat_bridge.proto
└── src/main/java/com/guava/prototype/bridge
    ├── config
    ├── gateway
    ├── grpc
    └── mock
```

## 原型包含什么

- 一个模拟的 `OpenAI 兼容 SSE` 服务：`/mock-openai/v1/chat/completions`
- 一个内部 `gRPC` 节点服务，监听 `9090`
- 一个 `Spring Boot WebFlux` 网关，监听 `8080`
- 一个 `POST /api/chat/streamable`，验证 `Streamable HTTP -> gRPC -> Streamable HTTP`
- 一个 `POST /api/terminal/render`，专门做终端里的流式文本渲染

完整链路是：

`Client(Streamable HTTP) -> Gateway(Spring Boot) -> gRPC Node -> Mock OpenAI SSE`

返回链路是：

`Mock OpenAI SSE -> gRPC Node -> Gateway(Streamable HTTP) -> Client`

## 快速启动

启动：

```bash
cd /Users/pix/Desktop/guava/grpc-sse-prototype
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH=/opt/homebrew/bin:$JAVA_HOME/bin:$PATH
mvn spring-boot:run
```

## 验证 1：看原始 Streamable HTTP 事件流

请求：

```bash
bash /Users/pix/Desktop/guava/grpc-sse-prototype/scripts/demo-streamable-http.sh
```

你会看到逐行刷出的 `NDJSON` 事件流。

## 验证 2：看终端里的流式输出效果

请求：

```bash
bash /Users/pix/Desktop/guava/grpc-sse-prototype/scripts/demo-terminal-render.sh
```

你会看到 token 文本在终端里连续输出，接近聊天窗口里的流式生成效果。

## 这个原型验证了什么

- Spring Boot 完全可以承担这种协议转换网关
- `WebFlux` 很适合承接 `Streamable HTTP`
- `grpc-java` 适合作为内部微服务流式中转
- 模拟节点把 `OpenAI 兼容 SSE` 转成 `gRPC` 是可行的
- 最终可以在终端里直接看到流式输出效果

## 如果后续要往生产走

下一步建议继续补：

- OpenAI 兼容字段和错误模型的完整映射
- gRPC metadata 与 HTTP header 的映射
- requestId / traceId / tenantId 透传
- 心跳和断线恢复
- 限流、超时、取消传播
- 统一事件模型，不要直接把 proto 暴露给前端
