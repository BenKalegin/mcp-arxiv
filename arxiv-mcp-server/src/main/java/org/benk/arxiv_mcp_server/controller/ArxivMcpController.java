package org.benk.arxiv_mcp_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.service.ArxivService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for arXiv API.
 */
@Slf4j
@RestController
@RequestMapping("/api/arxiv")
@RequiredArgsConstructor
public class ArxivMcpController {

    private final ArxivService arxivService;

    /**
     * Search for papers on arXiv based on a topic.
     *
     * @param topic      The topic to search for
     * @param maxResults Maximum number of results to retrieve (default: 5)
     * @return List of paper IDs found in the search
     */
    @PostMapping("/search")
    public ResponseEntity<List<String>> searchPapers(
            @RequestParam String topic,
            @RequestParam(required = false, defaultValue = "5") Integer maxResults) {
        log.info("Searching papers: topic={}, maxResults={}", topic, maxResults);
        return ok(arxivService.searchPapers(topic, maxResults));
    }

    /**
     * Extract information about a specific paper.
     *
     * @param paperId The ID of the paper to look for
     * @return JSON string with paper information if found, error message if not found
     */
    @GetMapping("/paper/{paperId}")
    public ResponseEntity<PaperInfo> extractInfo(@PathVariable String paperId) {
        log.info("Extracting info for paper: {}", paperId);
        PaperInfo paperInfo = arxivService.extractPaperInfo(paperId);
        if (paperInfo != null) {
            return ok(paperInfo);
        }
        return notFound().build();
    }

    /**
     * List all available topic folders in the papers directory.
     *
     * @return List of available folders
     */
    @GetMapping("/folders")
    public ResponseEntity<List<String>> getAvailableFolders() {
        log.info("Getting available folders");
        return ok(arxivService.getAvailableFolders());
    }

    /**
     * Get detailed information about papers on a specific topic.
     *
     * @param topic The research topic to retrieve papers for
     * @return Map of paper IDs to paper information
     */
    @GetMapping("/topic/{topic}")
    public ResponseEntity<Map<String, PaperInfo>> getTopicPapers(@PathVariable String topic) {
        log.info("Getting papers for topic: {}", topic);
        Map<String, PaperInfo> papersData = arxivService.getTopicPapers(topic);

        if (papersData.isEmpty()) {
            return notFound().build();
        }

        return ok(papersData);
    }

    /**
     * Get detailed information about papers on a specific topic in Markdown format.
     *
     * @param topic The research topic to retrieve papers for
     * @return Markdown content with paper details
     */
    @GetMapping("/topic/{topic}/markdown")
    public ResponseEntity<String> getTopicPapersMarkdown(@PathVariable String topic) {
        log.info("Getting papers for topic in markdown: {}", topic);
        Map<String, PaperInfo> papersData = arxivService.getTopicPapers(topic);

        if (papersData.isEmpty()) {
            return ok("# No papers found for topic: " + topic + "\n\nTry searching for papers on this topic first.");
        }

        StringBuilder content = new StringBuilder("# Papers on " + topic.replace("_", " ").toUpperCase() + "\n\n");
        content.append("Total papers: ").append(papersData.size()).append("\n\n");

        for (Map.Entry<String, PaperInfo> entry : papersData.entrySet()) {
            String paperId = entry.getKey();
            PaperInfo paperInfo = entry.getValue();

            content.append("## ").append(paperInfo.getTitle()).append("\n");
            content.append("- **Paper ID**: ").append(paperId).append("\n");
            content.append("- **Authors**: ").append(String.join(", ", paperInfo.getAuthors())).append("\n");
            content.append("- **Published**: ").append(paperInfo.getPublished()).append("\n");
            content.append("- **PDF URL**: [").append(paperInfo.getPdfUrl()).append("](")
                    .append(paperInfo.getPdfUrl()).append(")\n\n");
            //noinspection StringOperationCanBeSimplified
            content.append("### Summary\n").append(paperInfo.getSummary().substring(0, Math.min(500, paperInfo.getSummary().length())))
                    .append("...\n\n");
            content.append("---\n\n");
        }

        return ok(content.toString());
    }

    /**
     * Generate a prompt for Claude to find and discuss academic papers on a specific topic.
     *
     * @param topic     The topic to search for papers on
     * @param numPapers Number of papers to search for (default: 5)
     * @return Prompt for Claude
     */
    @GetMapping("/prompt")
    public ResponseEntity<String> generateSearchPrompt(
            @RequestParam String topic,
            @RequestParam(required = false, defaultValue = "5") Integer numPapers) {
        log.info("Generating search prompt: topic={}, numPapers={}", topic, numPapers);
        int papers = numPapers != null ? numPapers : 5;

        String prompt = String.format("""
                Search for %d academic papers about '%s' using the searchPapers tool.

                Follow these instructions:
                1. First, search for papers using searchPapers(topic='%s', maxResults=%d)
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

                Please present both detailed information about each paper and a high-level synthesis of the research landscape in %s.
                """, papers, topic, topic, papers, topic, topic, topic);

        return ok(prompt);
    }
}
