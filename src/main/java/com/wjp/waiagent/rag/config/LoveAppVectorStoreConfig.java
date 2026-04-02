package com.wjp.waiagent.rag.config;

import com.wjp.waiagent.rag.LoveAppDocumentLoader;
import com.wjp.waiagent.rag.enricher.MyKeywordEnricher;
import com.wjp.waiagent.rag.splitter.MyTokenTextSplitter;
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
     * 引入自定义切词器
     */
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    /**
     * 引入自定义关键词提取器
     */
    @Resource
    private MyKeywordEnricher myKeywordEnricher;

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
        // 加载自定义切词器
//        List<Document> splitterDocuments = myTokenTextSplitter.splitCustomized(documents);
        // 自动补充关键词信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
        // 添加文档
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }


}
