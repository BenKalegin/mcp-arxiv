package org.benk.arxiv_mcp_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

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

}
