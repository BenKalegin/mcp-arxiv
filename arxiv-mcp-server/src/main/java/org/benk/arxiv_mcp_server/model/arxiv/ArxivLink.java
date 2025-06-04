package org.benk.arxiv_mcp_server.model.arxiv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a link in the arXiv API response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArxivLink {
    
    @JacksonXmlProperty(localName = "href", isAttribute = true)
    private String href;
    
    @JacksonXmlProperty(localName = "rel", isAttribute = true)
    private String rel;
    
    @JacksonXmlProperty(localName = "type", isAttribute = true)
    private String type;
    
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
}