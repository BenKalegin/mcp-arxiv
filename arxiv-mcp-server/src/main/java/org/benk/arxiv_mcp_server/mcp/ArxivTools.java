package org.benk.arxiv_mcp_server.mcp;

import lombok.RequiredArgsConstructor;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.service.ArxivService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArxivTools {
    private final ArxivService arxivService;

    @Tool(description = "Given a topic, return up to maxResults academic paper titles/URLs related to that topic.")
    public List<String> searchPapers(String topic, int maxResults) {
        if (maxResults <= 0) {
            throw new IllegalArgumentException("maxResults must be greater than 0");
        }
        return arxivService.searchPapers(topic, maxResults);
    }

    @Tool(description = "Given a paper ID, return the title, authors, summary, and PDF URL of the paper.")
    public PaperInfo extractPaperInfo(String paperId) {
        if (paperId == null || paperId.isEmpty()) {
            throw new IllegalArgumentException("paperId cannot be null or empty");
        }
        return arxivService.extractPaperInfo(paperId);
    }

}
