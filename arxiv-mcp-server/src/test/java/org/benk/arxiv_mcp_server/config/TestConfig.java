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
     * Creates a mock ArxivClient bean for testing.
     *
     * @return A mock ArxivClient
     */
    @Bean
    @Primary
    public ArxivClient arxivClient() {
        return new MockArxivClient();
    }

    /**
     * Mock implementation of the ArxivClient interface for testing.
     */
    private static class MockArxivClient implements ArxivClient {

        @Override
        public ArxivResponse searchPapers(String search, int start, int maxResults) {
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

        @Override
        public ArxivResponse getPaper(String id) {
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
    }
}