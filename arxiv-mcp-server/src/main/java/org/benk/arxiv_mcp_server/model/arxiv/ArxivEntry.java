package org.benk.arxiv_mcp_server.model.arxiv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents an entry in the arXiv API response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArxivEntry {
    
    @JacksonXmlProperty(localName = "id")
    private String id;
    
    @JacksonXmlProperty(localName = "title")
    private String title;
    
    @JacksonXmlProperty(localName = "summary")
    private String summary;
    
    @JacksonXmlProperty(localName = "published")
    private String published;
    
    @JacksonXmlProperty(localName = "author")
    private List<ArxivAuthor> authors;
    
    @JacksonXmlProperty(localName = "link")
    private List<ArxivLink> links;
}