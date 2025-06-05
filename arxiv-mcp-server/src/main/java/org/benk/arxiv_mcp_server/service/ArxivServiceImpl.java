package org.benk.arxiv_mcp_server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benk.arxiv_mcp_server.client.ArxivClient;
import org.benk.arxiv_mcp_server.model.PaperInfo;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivAuthor;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivEntry;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivLink;
import org.benk.arxiv_mcp_server.model.arxiv.ArxivResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the ArxivService interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArxivServiceImpl implements ArxivService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ArxivClient arxivClient;

    private final String papersDirectory = "c:/temp/papers";

    @Override
    public List<String> searchPapers(String topic, int maxResults) {
        log.info("Searching for papers on topic: {} with max results: {}", topic, maxResults);

        // Create a directory for this topic
        String topicDir = formatTopicDirectory(topic);
        Path path = Paths.get(papersDirectory, topicDir);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            log.error("Error creating directory: {}", path, e);
            throw new RuntimeException("Error creating directory", e);
        }

        // Search arXiv API using the Feign client
        ArxivResponse response = arxivClient.searchPapers("all:" + topic, 0, maxResults);

        List<String> paperIds = new ArrayList<>();
        Map<String, PaperInfo> papersInfo = new HashMap<>();

        if (response != null && response.getEntries() != null) {
            for (ArxivEntry entry : response.getEntries()) {
                // Extract paper ID from the entry ID (which is a URL)
                String entryId = entry.getId();
                String paperId = entryId.substring(entryId.lastIndexOf('/') + 1);
                paperIds.add(paperId);

                // Find PDF URL
                String pdfUrl = getPdfUrl(entry, paperId);

                // Extract authors
                List<String> authors = new ArrayList<>();
                if (entry.getAuthors() != null) {
                    authors = entry.getAuthors().stream()
                            .map(ArxivAuthor::getName)
                            .collect(Collectors.toList());
                }

                PaperInfo paperInfo = PaperInfo.builder()
                        .title(entry.getTitle())
                        .authors(authors)
                        .summary(entry.getSummary())
                        .pdfUrl(pdfUrl)
                        .published(entry.getPublished())
                        .build();

                papersInfo.put(paperId, paperInfo);
            }
        }

        // Save papers info to JSON file
        Path filePath = path.resolve("papers_info.json");
        try {
            // Try to load existing papers info
            Map<String, PaperInfo> existingPapersInfo = new HashMap<>();
            if (Files.exists(filePath)) {
                existingPapersInfo = objectMapper.readValue(filePath.toFile(), new TypeReference<>() {});
            }

            // Merge with new papers info
            existingPapersInfo.putAll(papersInfo);

            // Write to file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), existingPapersInfo);
            log.info("Results are saved in: {}", filePath);
        } catch (IOException e) {
            log.error("Error writing papers info to file: {}", filePath, e);
            throw new RuntimeException("Error writing papers info to file", e);
        }

        return paperIds;
    }

    private static String getPdfUrl(ArxivEntry entry, String paperId) {
        String pdfUrl = null;
        if (entry.getLinks() != null) {
            for (ArxivLink link : entry.getLinks()) {
                if ("pdf".equals(link.getType())) {
                    pdfUrl = link.getHref();
                    break;
                }
            }
        }

        // If no PDF URL found, construct one
        if (pdfUrl == null) {
            pdfUrl = "https://arxiv.org/pdf/" + paperId + ".pdf";
        }
        return pdfUrl;
    }

    @Override
    public PaperInfo extractPaperInfo(String paperId) {
        log.info("Extracting info for paper: {}", paperId);

        // First, try to find the paper in the local cache
        File papersDirFile = new File(papersDirectory);
        if (papersDirFile.exists() && papersDirFile.isDirectory()) {
            for (File topicDir : Objects.requireNonNull(papersDirFile.listFiles(File::isDirectory))) {
                File papersInfoFile = new File(topicDir, "papers_info.json");
                if (papersInfoFile.exists() && papersInfoFile.isFile()) {
                    try {
                        Map<String, PaperInfo> papersInfo = objectMapper.readValue(papersInfoFile,
                                new TypeReference<>() {
                                });

                        if (papersInfo.containsKey(paperId)) {
                            log.info("Found paper info in local cache: {}", paperId);
                            return papersInfo.get(paperId);
                        }
                    } catch (IOException e) {
                        log.error("Error reading papers info file: {}", papersInfoFile, e);
                    }
                }
            }
        }

        // If not found in local cache, fetch from arXiv API
        log.info("Paper not found in local cache, fetching from arXiv API: {}", paperId);
        try {
            ArxivResponse response = arxivClient.getPaper(paperId);

            if (response != null && response.getEntries() != null && !response.getEntries().isEmpty()) {
                ArxivEntry entry = response.getEntries().getFirst();

                String pdfUrl = getPdfUrl(entry, paperId);

                // Extract authors
                List<String> authors = new ArrayList<>();
                if (entry.getAuthors() != null) {
                    authors = entry.getAuthors().stream()
                            .map(ArxivAuthor::getName)
                            .collect(Collectors.toList());
                }

                return PaperInfo.builder()
                        .title(entry.getTitle())
                        .authors(authors)
                        .summary(entry.getSummary())
                        .pdfUrl(pdfUrl)
                        .published(entry.getPublished())
                        .build();
            }
        } catch (Exception e) {
            log.error("Error fetching paper info from arXiv API: {}", paperId, e);
        }

        log.warn("No information found for paper: {}", paperId);
        return null;
    }

    @Override
    public List<String> getAvailableFolders() {
        log.info("Getting available folders");

        File papersDirFile = new File(papersDirectory);
        if (!papersDirFile.exists() || !papersDirFile.isDirectory()) {
            log.warn("Papers directory does not exist: {}", papersDirectory);
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(papersDirFile.listFiles(File::isDirectory)))
                .filter(dir -> new File(dir, "papers_info.json").exists())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, PaperInfo> getTopicPapers(String topic) {
        log.info("Getting papers for topic: {}", topic);

        String topicDir = formatTopicDirectory(topic);
        Path papersInfoPath = Paths.get(papersDirectory, topicDir, "papers_info.json");

        if (!Files.exists(papersInfoPath)) {
            log.warn("No papers info file found for topic: {}", topic);
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(papersInfoPath.toFile(),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            log.error("Error reading papers info file: {}", papersInfoPath, e);
            throw new RuntimeException("Error reading papers info file", e);
        }
    }

    /**
     * Format topic string to be used as a directory name.
     * 
     * @param topic The topic string
     * @return Formatted topic string
     */
    private String formatTopicDirectory(String topic) {
        return topic.toLowerCase().replace(" ", "_");
    }
}
