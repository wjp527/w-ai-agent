package com.wjp.waiagent.tools;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具集中注册
 */
@Configuration
public class ToolRegistration {

    @Bean
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();

        return ToolCallbacks.from(
                fileOperationTool,
                webScrapingTool,
                terminalOperationTool,
                resourceDownloadTool,
                pdfGenerationTool
        );

    }

}
