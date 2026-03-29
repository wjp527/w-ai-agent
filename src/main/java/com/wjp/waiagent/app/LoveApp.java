package com.wjp.waiagent.app;

import com.wjp.waiagent.advisor.MySimpleLoggerAdvisor;
import com.wjp.waiagent.advisor.ReReadingAdvisor;
import com.wjp.waiagent.rag.LoveAppVectorStoreConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    // 系统提示
    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。\n";

    /**
     * 初始化 ChatClient
     *
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 基于内存：使用 MessageWindowChatMemory 控制最大保留消息数
        InMemoryChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 输出日志
                        new MySimpleLoggerAdvisor()
                        // 让模型进行重读，来输出更精准的答案，不过更费钱
                        //  , new ReReadingAdvisor()
                )
                .build();

    }

    /**
     * AI 基础对话
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        // 当前版本不再提供 CHAT_MEMORY_* 常量；conversation id 的 key 固定为 ChatMemory.CONVERSATION_ID
                        .param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();

        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 定义数据类型
     * @param title
     * @param suggestions
     */
    record LoveReport(String title, List<String> suggestions) {

    }

    /**
     * 带报告 （结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱报告，标题为{用户名}的恋爱报告，内容为列表建议")
                .user(message)
                .advisors(spec -> spec
                        // 当前版本不再提供 CHAT_MEMORY_* 常量；conversation id 的 key 固定为 ChatMemory.CONVERSATION_ID
                        .param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    /**
     * 本地向量库 RAG
     */
    @Resource
    private VectorStore loveAppVectorStore;

    /**
     * 云端知识库 RAG：见 {@link com.wjp.waiagent.rag.LoveAppRagCloudAdvisorConfig#questionAnswerAdvisor()}
     */
    @Resource(name = "questionAnswerAdvisor")
    private Advisor cloudQuestionAnswerAdvisor;

    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        // 当前版本不再提供 CHAT_MEMORY_* 常量；conversation id 的 key 固定为 ChatMemory.CONVERSATION_ID
                        .param(ChatMemory.CONVERSATION_ID, chatId)
                )
                // 本地向量库：
//                 .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 云端知识库 阿里
                .advisors(cloudQuestionAnswerAdvisor)
                .call()
                .chatResponse();

        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;

    }















}
