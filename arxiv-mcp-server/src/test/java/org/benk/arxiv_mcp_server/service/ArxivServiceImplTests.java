package org.benk.arxiv_mcp_server.service;

import org.benk.arxiv_mcp_server.client.ArxivClient;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivAuthor;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivEntry;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivLink;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ArxivServiceImplTests {

    @Mock
    private ArxivClient arxivClient;

    @InjectMocks
    private ArxivServiceImpl arxivService;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(arxivClient.searchPapers(anyString(), anyInt(), anyInt())).thenReturn(searchPapers("test", 0, 3));
        when(arxivClient.getPaper(anyString())).thenReturn(getPaper("2101.01v1"));
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    ArxivResponse searchPapers(String search, int start, int maxResults) {
        List<ArxivEntry> entries = new ArrayList<>();

        for (int i = 1; i <= maxResults; i++) {
            String paperId = "2101.0" + i + "v1";

            List<ArxivAuthor> authors = new ArrayList<>();
            authors.add(new ArxivAuthor("Author " + i));
            authors.add(new ArxivAuthor("Co-Author " + i));

            List<ArxivLink> links = new ArrayList<>();
            links.add(new ArxivLink("https://arxiv.org/pdf/" + paperId + ".pdf", "alternate", "application/pdf", "pdf"));

            ArxivEntry entry = new ArxivEntry(
                    "http://arxiv.org/abs/" + paperId,
                    "Sample Paper " + i + " on " + search,
                    "This is a sample summary for paper " + i + " on the topic of " + search,
                    LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                    authors,
                    links
            );

            entries.add(entry);
        }

        ArxivResponse response = new ArxivResponse();
        response.setEntries(entries);
        response.setTotalResults(maxResults);
        response.setStartIndex(start);
        response.setItemsPerPage(maxResults);

        return response;
    }

    ArxivResponse getPaper(String id) {
        List<ArxivEntry> entries = new ArrayList<>();

        List<ArxivAuthor> authors = new ArrayList<>();
        authors.add(new ArxivAuthor("Author 1"));
        authors.add(new ArxivAuthor("Co-Author 1"));

        List<ArxivLink> links = new ArrayList<>();
        links.add(new ArxivLink("https://arxiv.org/pdf/" + id + ".pdf", "alternate", "application/pdf", "pdf"));

        ArxivEntry entry = new ArxivEntry(
                "http://arxiv.org/abs/" + id,
                "Sample Paper on Requested Topic",
                "This is a sample summary for the requested paper",
                LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                authors,
                links
        );

        entries.add(entry);

        ArxivResponse response = new ArxivResponse();
        response.setEntries(entries);
        response.setTotalResults(1);
        response.setStartIndex(0);
        response.setItemsPerPage(1);

        return response;
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
