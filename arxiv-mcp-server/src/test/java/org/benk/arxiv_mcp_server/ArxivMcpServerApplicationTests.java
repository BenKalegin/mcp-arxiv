package org.benk.arxiv_mcp_server;

import org.benk.arxiv_mcp_server.config.TestConfig;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.service.ArxivService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
class ArxivMcpServerApplicationTests {

	@Autowired
	private ArxivService arxivService;

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
	}

	@Test
	void testSearchPapers() {
		// Test searching for papers
		List<String> paperIds = arxivService.searchPapers("machine learning", 3);

		// Verify that the search returns the expected number of results
		assertEquals(3, paperIds.size());

		// Verify that the paper IDs are not empty
		for (String paperId : paperIds) {
			assertNotNull(paperId);
			assertFalse(paperId.isEmpty());
		}
	}

	@Test
	void testExtractPaperInfo() {
		// First, search for papers to get a paper ID
		List<String> paperIds = arxivService.searchPapers("artificial intelligence", 1);
		assertFalse(paperIds.isEmpty());

		// Extract information about the paper
		String paperId = paperIds.getFirst();
		PaperInfo paperInfo = arxivService.extractPaperInfo(paperId);

		// Verify that the paper information is not null
		assertNotNull(paperInfo);

		// Verify that the paper information contains the expected fields
		assertNotNull(paperInfo.getTitle());
		assertNotNull(paperInfo.getAuthors());
		assertNotNull(paperInfo.getSummary());
		assertNotNull(paperInfo.getPdfUrl());
		assertNotNull(paperInfo.getPublished());
	}

	@Test
	void testGetTopicPapers() {
		// First, search for papers to create a topic
		arxivService.searchPapers("neural networks", 2);

		// Get papers for the topic
		Map<String, PaperInfo> papers = arxivService.getTopicPapers("neural networks");

		// Verify that the papers are not empty
		assertFalse(papers.isEmpty());

		// Verify that the papers contain the expected information
		for (Map.Entry<String, PaperInfo> entry : papers.entrySet()) {
			assertNotNull(entry.getKey());
			assertNotNull(entry.getValue());
			assertNotNull(entry.getValue().getTitle());
			assertNotNull(entry.getValue().getAuthors());
			assertNotNull(entry.getValue().getSummary());
			assertNotNull(entry.getValue().getPdfUrl());
			assertNotNull(entry.getValue().getPublished());
		}
	}
}
