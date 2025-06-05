package org.benk.arxiv_mcp_server.mcp;

import com.logaritex.mcp.annotation.McpResource;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;
import lombok.RequiredArgsConstructor;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.service.ArxivService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArxivResourceProvider {

    private final ArxivService arxivService;

    /**
     *  List all available topic folders in the papers directory.
     *  This resource provides a simple list of all available topic folders.
     */
    @McpResource(uri = "papers://folders", name = "Papers Folders", description = "Returns a list of folders containing papers")
    public ReadResourceResult listPapersFolders(ReadResourceRequest request) {
        // Get the list of topic folders from the arXiv service
        var folders = arxivService.getAvailableFolders();

        // Create a simple markdown list
        StringBuilder content = new StringBuilder("# Available Topics\n\n");
        if (folders != null && !folders.isEmpty()) {
            for (String folder : folders) {
                content.append("- ").append(folder).append("\n");
            }
            content.append("\nUse @<folder> to access papers in that topic.\n");
        } else {
            content.append("No topics found.\n");
        }
        return new ReadResourceResult(List.of(new TextResourceContents(request.uri(), "text/plain", content.toString())));
    }


    /**
     *     Get detailed information about papers on a specific topic.
     *     Args:
     *         topic: The research topic to retrieve papers for
     */
    @McpResource(uri = "papers://{topic}", name = "Papers by Topic", description = "Returns a list of papers for a specific topic")
    public ReadResourceResult getTopicPapers(String topic) {
        var papers = arxivService.getTopicPapers(topic);

        try {
            // Load papers data from the arXiv service (simulate reading from file)
            StringBuilder content = new StringBuilder("# Papers on ")
                    .append(topic.replace("_", " ").substring(0, 1).toUpperCase())
                    .append(topic.replace("_", " ").substring(1))
                    .append("\n\n");

            content.append("Total papers: ").append(papers.size()).append("\n\n");

            for (Map.Entry<String, PaperInfo> entry : papers.entrySet()) {
                String paperId = entry.getKey();
                PaperInfo paperInfo = entry.getValue();

                content.append("## ").append(paperInfo.getTitle()).append("\n");
                content.append("- **Paper ID**: ").append(paperId).append("\n");
                content.append("- **Authors**: ").append(String.join(", ", paperInfo.getAuthors())).append("\n");
                content.append("- **Published**: ").append(paperInfo.getPublished()).append("\n");
                content.append("- **PDF URL**: [").append(paperInfo.getPdfUrl()).append("](").append(paperInfo.getPdfUrl()).append(")\n\n");
                content.append("### Summary\n")
                        .append(paperInfo.getSummary().length() > 500
                                ? paperInfo.getSummary().substring(0, 500) + "..."
                                : paperInfo.getSummary())
                        .append("\n\n---\n\n");
            }

            return new ReadResourceResult(List.of(
                    new TextResourceContents("papers://" + topic, "text/plain", content.toString())
            ));
        } catch (Exception e) {
            String errorMsg = "# Error reading papers data for " + topic + "\n\nThe papers data is corrupted or unavailable.";
            return new ReadResourceResult(List.of(
                    new TextResourceContents("papers://" + topic, "text/plain", errorMsg)
            ));
        }
    }
}
