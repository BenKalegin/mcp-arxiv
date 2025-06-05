package org.benk.arxiv_mcp_server.config;

import org.benk.arxiv_mcp_server.client.ArxivClient;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivAuthor;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivEntry;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivLink;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Test configuration that provides mock beans for testing.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Mock implementation of the ArxivClient interface for testing.
     */
}