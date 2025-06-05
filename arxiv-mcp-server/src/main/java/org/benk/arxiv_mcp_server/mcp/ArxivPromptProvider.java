package org.benk.arxiv_mcp_server.mcp;

import com.logaritex.mcp.annotation.McpArg;
import com.logaritex.mcp.annotation.McpPrompt;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArxivPromptProvider {

    /**
     * Generates a prompt for finding and discussing academic papers on a specific topic.
     * @param topic The topic to search for
     * @param numPapers The number of papers to find (default 5)
     * @return A prompt string for Claude
     */
    @McpPrompt(name = "generate-search-prompt", description = "Generate a prompt to find and discuss academic papers on a topic")
    public String generateSearchPrompt(
            @McpArg(name = "topic", description = "The topic to search for", required = true) String topic,
            @McpArg(name = "numPapers", description = "Number of papers to find", required = false) Integer numPapers) {
        int papers = (numPapers != null) ? numPapers : 5;
        return
            """
            Search for %d academic papers about '%s' using the search_papers tool.
            
            Follow these instructions:
            1. First, search for papers using search_papers(topic='%s', max_results=%d)
            2. For each paper found, extract and organize the following information:
               - Paper title
               - Authors
               - Publication date
               - Brief summary of the key findings
               - Main contributions or innovations
               - Methodologies used
               - Relevance to the topic '%s'
            
            3. Provide a comprehensive summary that includes:
               - Overview of the current state of research in '%s'
               - Common themes and trends across the papers
               - Key research gaps or areas for future investigation
               - Most impactful or influential papers in this area
            
            4. Organize your findings in a clear, structured format with headings and bullet points for easy readability.
            
            Please present both detailed information about each paper and a high-level synthesis of the research landscape in %s."""
            .formatted(papers, topic, topic, papers, topic, topic, topic);
    }


    /**
     * A simple greeting prompt that takes a name parameter.
     * @param name The name to greet
     * @return A greeting message
     */
    @McpPrompt(name = "greeting", description = "A simple greeting prompt")
    public GetPromptResult greetingPrompt(
            @McpArg(name = "name", description = "The name to greet", required = true) String name) {
        return new GetPromptResult("Greeting", List.of(new PromptMessage(Role.ASSISTANT,
                new TextContent("Hello, " + name + "! Welcome to the MCP system."))));
    }

/*
     * A more complex prompt that generates a personalized message.
     * @param exchange The server exchange
     * @param name The user's name
     * @param age The user's age
     * @param interests The user's interests
     * @return A personalized message
     *

    @McpPrompt(name = "personalized-message",
            description = "Generates a personalized message based on user information")
    public GetPromptResult personalizedMessage(McpSyncServerExchange exchange,
                                                         @McpArg(name = "name", description = "The user's name", required = true) String name,
                                                         @McpArg(name = "age", description = "The user's age", required = false) Integer age,
                                                         @McpArg(name = "interests", description = "The user's interests", required = false) String interests) {

        exchange.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.INFO)
                .data("personalized-message event").build());

        StringBuilder message = new StringBuilder();
        message.append("Hello, ").append(name).append("!\n\n");

        if (age != null) {
            message.append("At ").append(age).append(" years old, you have ");
            if (age < 30) {
                message.append("so much ahead of you.\n\n");
            }
            else if (age < 60) {
                message.append("gained valuable life experience.\n\n");
            }
            else {
                message.append("accumulated wisdom to share with others.\n\n");
            }
        }

        if (interests != null && !interests.isEmpty()) {
            message.append("Your interest in ")
                    .append(interests)
                    .append(" shows your curiosity and passion for learning.\n\n");
        }

        message
                .append("I'm here to assist you with any questions you might have about the Model Context Protocol.");

        return new GetPromptResult("Personalized Message",
                List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message.toString()))));
    }

    */
/**
     * A prompt that returns a list of messages forming a conversation.
     * @param request The prompt request
     * @return A list of messages
     *//*

    @McpPrompt(name = "conversation-starter", description = "Provides a conversation starter with the system")
    public List<PromptMessage> conversationStarter(McpSchema.GetPromptRequest request) {
        return List.of(
                new PromptMessage(Role.ASSISTANT,
                        new TextContent("Hello! I'm the MCP assistant. How can I help you today?")),
                new PromptMessage(Role.USER,
                        new TextContent("I'd like to learn more about the Model Context Protocol.")),
                new PromptMessage(Role.ASSISTANT, new TextContent(
                        "Great choice! The Model Context Protocol (MCP) is a standardized way for servers "
                        + "to communicate with language models. It provides a structured approach for "
                        + "exchanging information, making requests, and handling responses. "
                        + "What specific aspect would you like to explore first?")));
    }

    */
/**
     * A prompt that accepts arguments as a map.
     * @param arguments The arguments map
     * @return A prompt result
     *//*

    @McpPrompt(name = "map-arguments", description = "Demonstrates using a map for arguments")
    public GetPromptResult mapArguments(Map<String, Object> arguments) {
        StringBuilder message = new StringBuilder("I received the following arguments:\n\n");

        if (arguments != null && !arguments.isEmpty()) {
            for (Map.Entry<String, Object> entry : arguments.entrySet()) {
                message.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        else {
            message.append("No arguments were provided.");
        }

        return new GetPromptResult("Map Arguments Demo",
                List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message.toString()))));
    }

    */
/**
     * A prompt that returns a single PromptMessage.
     * @param name The user's name
     * @return A single PromptMessage
     *//*

    @McpPrompt(name = "single-message", description = "Demonstrates returning a single PromptMessage")
    public PromptMessage singleMessagePrompt(
            @McpArg(name = "name", description = "The user's name", required = true) String name) {
        return new PromptMessage(Role.ASSISTANT,
                new TextContent("Hello, " + name + "! This is a single message response."));
    }

    */
/**
     * A prompt that returns a list of strings.
     * @param topic The topic to provide information about
     * @return A list of strings with information about the topic
     *//*

    @McpPrompt(name = "string-list", description = "Demonstrates returning a list of strings")
    public List<String> stringListPrompt(@McpArg(name = "topic",
            description = "The topic to provide information about", required = true) String topic) {
        if ("MCP".equalsIgnoreCase(topic)) {
            return List.of(
                    "The Model Context Protocol (MCP) is a standardized way for servers to communicate with language models.",
                    "It provides a structured approach for exchanging information, making requests, and handling responses.",
                    "MCP allows servers to expose resources, tools, and prompts to clients in a consistent way.");
        }
        else {
            return List.of("I don't have specific information about " + topic + ".",
                    "Please try a different topic or ask a more specific question.");
        }
    }
*/
}
