# ad-chatbot-mvp

这是一个和原有架构探索目录完全隔离的最小原型工程，用来验证下面这条广告问答链路是否顺手：

1. `Orchestrator` 根据用户意图决定走 `RAG Tool Use` 还是 `API Tool Use`
2. `RAG Tool Use` 从 `Qdrant` 向量库取回候选文档，包含摘要和相关性分数，并在节点内进行二次排序
3. `API Tool Use` 调用广告接口，返回最近 7 天 ROI 等指标
4. `Query` 节点做最终守门，判断是否应该展示结果，或提示用户只问广告相关问题
5. `Answer` 节点基于节点结果生成最终回答

## 设计约束

- 使用 `Spring Boot` 提供最小 HTTP 接口
- 使用 `LangGraph4j` 表达节点流关系
- Prompt 与节点类代码分离，放在 `src/main/resources/prompts`
- 使用 Docker 启动 `Qdrant`
- 使用本地 fake 文档自动建库和灌库
- 使用 fake ROI 数据模拟广告接口，优先验证编排可行性

## 目录

- `src/main/java/com/guava/adchatbotmvp/node`：节点实现
- `src/main/java/com/guava/adchatbotmvp/service`：Qdrant RAG、向量化和广告 API 的假实现
- `src/main/java/com/guava/adchatbotmvp/graph`：LangGraph4j 编排装配
- `src/main/resources/prompts`：节点 Prompt 模板
- `src/main/resources/rag/fake-documents.json`：fake 广告规则文档
- `docker-compose.yml`：本地 Qdrant

## 运行

当前原型默认依赖本地 Docker 中的 `Qdrant`。

先启动向量库：

```bash
cd /Users/pix/Desktop/guava/ad-chatbot-mvp
docker compose up -d
```

再启动应用：

```bash
cd /Users/pix/Desktop/guava/ad-chatbot-mvp
mvn spring-boot:run
```

## 示例请求

```bash
curl -X POST http://localhost:8080/api/chat \
  -H 'Content-Type: application/json' \
  -d '{
    "userId": "merchant-1001",
    "question": "什么是 ROI？"
  }'
```
