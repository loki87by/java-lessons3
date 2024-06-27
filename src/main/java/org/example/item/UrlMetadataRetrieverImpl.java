package org.example.item;

import lombok.Builder;

import org.example.error.ItemRetrieverException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever{
    @lombok.Value
    @Builder(toBuilder = true)
    static class UrlMetadataImpl implements UrlMetadata {
        String normalUrl;
        String resolvedUrl;
        String mimeType;
        String title;
        boolean hasImage;
        boolean hasVideo;
        Instant dateResolved;
        @Override
        public Instant getDateResolved() {
            return dateResolved;
        }
    }

    private UrlMetadataImpl handleVideo(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetadataImpl handleImage(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }

    private UrlMetadataImpl handleText(URI url) {
        HttpResponse<String> resp = connect(url, "GET", HttpResponse.BodyHandlers.ofString());
        Document doc = Jsoup.parse(resp.body());
        Elements imgElements = doc.getElementsByTag("img");
        Elements videoElements = doc.getElementsByTag("video");

        return UrlMetadataImpl.builder()
                .title(doc.title())
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private <T> HttpResponse<T> connect(URI uri, String method, HttpResponse.BodyHandler<T> responseBodyHandler) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .method(method, HttpRequest.BodyPublishers.noBody())
                    .build();

        try {
            return client.send(request, responseBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new ItemRetrieverException("Failed to connect or send request to URL: " + uri, e);
        }
    }

    @Override
    public UrlMetadata retrieve(String url) {
        final URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new ItemRetrieverException("The URL is malformed: " + url, e);
        }

        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());
        URI finalUrl = resp.uri();

        String contentType = resp.headers()
                .firstValue("Content-Type")
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;

        if(mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(resp.uri());
        } else if(mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(resp.uri());
        } else if(mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleVideo(resp.uri());
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }
        result.toBuilder()
                .dateResolved(Instant.now())
                .mimeType(String.valueOf(mediaType))
                .normalUrl(url)
                .resolvedUrl(String.valueOf(finalUrl))
                .build();
        return result;
    }
}
