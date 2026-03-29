package com.wjp.waiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 配置向量数据库
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    /**
     * 创建一个向量数据库
     * @param dashscopeEmbeddingModel
     * @return
     */
    @Bean
    VectorStore LoveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 创建一个向量数据库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 加兹文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        // 添加文档
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }


}
