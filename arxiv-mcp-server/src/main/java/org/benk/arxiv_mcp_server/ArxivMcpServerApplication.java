package org.benk.arxiv_mcp_server;

import com.logaritex.mcp.spring.SpringAiMcpAnnotationProvider;
import io.modelcontextprotocol.server.McpServerFeatures;
import org.benk.arxiv_mcp_server.mcp.ArxivTools;
import org.benk.arxiv_mcp_server.mcp.ArxivPromptProvider;
import org.benk.arxiv_mcp_server.mcp.ArxivResourceProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * Main application class for the arXiv MCP server.
 * This application provides a REST API for searching and extracting information about papers from arXiv.
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"org.benk.arxiv_mcp_server", "org.springframework.ai.mcp"})
public class ArxivMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArxivMcpServerApplication.class, args);
	}

	/**
	 * Provides a ToolCallbackProvider for the arXiv service.
	 * This allows the service to be used as a tool in AI applications communicating via STDIO
	 * @return a ToolCallbackProvider for the arXiv service
	 */
	@Bean
	public ToolCallbackProvider weatherTools(ArxivTools tools) {
		return MethodToolCallbackProvider.builder().toolObjects(tools).build();
	}

	@Bean
	public List<McpServerFeatures.SyncResourceSpecification> resourceSpecs(ArxivResourceProvider arxivResourceProvider) {
		return SpringAiMcpAnnotationProvider.createSyncResourceSpecifications(List.of(arxivResourceProvider));
	}

	@Bean
	public List<McpServerFeatures.SyncPromptSpecification> promptSpecs(ArxivPromptProvider arxivPromptProvider) {
		return SpringAiMcpAnnotationProvider.createSyncPromptSpecifications(List.of(arxivPromptProvider));
	}

	@Bean
	public List<McpServerFeatures.SyncCompletionSpecification> completionSpecs(AutocompleteProvider autocompleteProvider) {
		return SpringAiMcpAnnotationProvider.createSyncCompleteSpecifications(List.of(autocompleteProvider));
	}

}
