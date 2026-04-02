package com.wjp.waiagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建上下文查询增强器的工厂
 */
public class LoveAppContextualQueryAugmenterFactory {

    /**
     * 创建上下文查询增强器的工厂
     * @return
     */
    public static ContextualQueryAugmenter createInstance() {
        // 兜底策略：如果在知识库中找不到匹配的答案，则返回一个默认的提示
        PromptTemplate promptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                有问题可以联系编程导航客服https：//codefather.cn
                """);
        return ContextualQueryAugmenter
                .builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(promptTemplate)
                .build();
    }

}
