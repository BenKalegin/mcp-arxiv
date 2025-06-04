package org.benk.arxiv_mcp_server.client;

import org.benk.arxiv_mcp_server.model.arxiv.ArxivResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for the arXiv API.
 */
@FeignClient(name = "arxiv-api", url = "${arxiv.api.url}")
public interface ArxivClient {
    
    /**
     * Search for papers on arXiv based on a query.
     *
     * @param search     The search query
     * @param start      The start index (for pagination)
     * @param maxResults The maximum number of results to return
     * @return The arXiv API response
     */
    @GetMapping
    ArxivResponse searchPapers(
            @RequestParam("search_query") String search,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "max_results", defaultValue = "10") int maxResults
    );
    
    /**
     * Get a specific paper by ID.
     *
     * @param id The ID of the paper
     * @return The arXiv API response
     */
    @GetMapping
    ArxivResponse getPaper(@RequestParam("id_list") String id);
}