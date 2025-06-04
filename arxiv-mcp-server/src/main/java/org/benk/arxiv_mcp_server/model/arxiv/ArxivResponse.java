package org.benk.arxiv_mcp_server.model.arxiv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the root element of the arXiv API response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "feed")
public class ArxivResponse {
    
    @JacksonXmlProperty(localName = "totalResults")
    private int totalResults;
    
    @JacksonXmlProperty(localName = "startIndex")
    private int startIndex;
    
    @JacksonXmlProperty(localName = "itemsPerPage")
    private int itemsPerPage;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    private List<ArxivEntry> entries;
}