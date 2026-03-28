package com.wjp.waiagent.demo.invoke;

import com.wjp.waiagent.WAiAgentApplication;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 通过 Spring AI Alibaba（DashScope）调用百炼，配置见 spring.ai.dashscope。
 */
@Component
public class SdkAiInvoke {

    private final ChatClient chatClient;

    public SdkAiInvoke(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * 简单对话示例；模型名在配置 spring.ai.dashscope.chat.options.model（如 qwen-plus）。
     */
    public String callWithMessage() {
        return chatClient.prompt()
                .system("You are a helpful assistant.")
                .user("你是谁？")
                .call()
                .content();
    }

    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx =
                     SpringApplication.run(WAiAgentApplication.class, args)) {
            String text = ctx.getBean(SdkAiInvoke.class).callWithMessage();
            System.out.println(text);
        } catch (Exception e) {
            System.err.println("调用大模型失败: " + e.getMessage());
        }
    }
}
