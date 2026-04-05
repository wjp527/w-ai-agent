package com.wjp.waiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.wjp.waiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 文件操作工具类
 * @author wjp
 */
public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + File.separator + "/file";

    /**
     * 读取文件
     * @param fileName 文件名
     * @return 文件内容
     */
    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of the file to read") String fileName) {
        // 要读取文件的路径
        String filePath = FILE_DIR + "/" + fileName;

        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    /**
     * 写入文件
     * @param fileName 文件名
     * @param content 文件内容
     * @return 写入结果
     */
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write")
            String fileName,
            @ToolParam(description = "Content to write to the file")
            String content) {
        // 要写文件的路径
        String filePath = FILE_DIR + File.separator + fileName;

        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            // 写入内容
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}
