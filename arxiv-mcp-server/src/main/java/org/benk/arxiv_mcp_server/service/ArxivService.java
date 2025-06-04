package org.benk.arxiv_mcp_server.service;

import org.benk.arxiv_mcp_server.model.PaperInfo;

import java.util.List;
import java.util.Map;

/**
 * Service interface for arXiv operations.
 */
public interface ArxivService {
    
    /**
     * Search for papers on arXiv based on a topic.
     *
     * @param topic       The topic to search for
     * @param maxResults  Maximum number of results to retrieve
     * @return List of paper IDs found in the search
     */
    List<String> searchPapers(String topic, int maxResults);
    
    /**
     * Extract information about a specific paper.
     *
     * @param paperId The ID of the paper to look for
     * @return Paper information if found, null if not found
     */
    PaperInfo extractPaperInfo(String paperId);
    
    /**
     * Get all available topic folders.
     *
     * @return List of available topic folders
     */
    List<String> getAvailableFolders();
    
    /**
     * Get papers for a specific topic.
     *
     * @param topic The research topic to retrieve papers for
     * @return Map of paper IDs to paper information
     */
    Map<String, PaperInfo> getTopicPapers(String topic);
}