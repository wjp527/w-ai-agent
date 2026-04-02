package com.wjp.waiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 根据用户查询需求生成对应的 advisor
 * 检索增强顾问
 */
@Slf4j
public class LoveAppRagCustomAdvisorFactory {

    /**
     * 创建自定义的 RetrievalAugmentationAdvisor
     * @param vectorStore 向量数据库
     * @param status 状态
     * @return
     */
    public static Advisor createLoveAppRagCustomAdvisor(VectorStore vectorStore, String status) {
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status", status)
                .build();

        // 创建 文档检索器
        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore) // 设置向量数据库
                .filterExpression(expression) // 设置过滤条件
                .similarityThreshold(0.5) // 设置相似度阈值
                .topK(3) // 设置返回结果数量
                .build();

        // 创建 最终的检索增强顾问
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // 如果用户问的问题，知识库中找不到，会直接输出 提示模版
                .queryAugmenter(LoveAppContextualQueryAugmenterFactory.createInstance())
                .build();
    }

}
