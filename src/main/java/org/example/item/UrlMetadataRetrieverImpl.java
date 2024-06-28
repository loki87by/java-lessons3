package org.example.item;

import lombok.Builder;

import org.example.error.ItemRetrieverException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;

@Service
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
        Timestamp dateResolved;
        @Override
        public Timestamp getDateResolved() {
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
        URI finalUrl = null;
        URI currentUrl = uri;

        while (currentUrl != null) {
            HttpResponse<Void> resp = connect(currentUrl, "HEAD", HttpResponse.BodyHandlers.discarding());
            finalUrl = currentUrl;
            String newUrl = resp.headers().firstValue("Location").orElse(null);
            if (newUrl != null) {
                currentUrl = URI.create(newUrl);
            } else {
                currentUrl = null;
            }
        }
        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());

        String contentType = resp.headers()
                .firstValue("Content-Type")
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;

        if(mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(finalUrl);
        } else if(mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(finalUrl);
        } else if(mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleVideo(finalUrl);
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }
        return result.toBuilder()
                .dateResolved(Timestamp.from(Instant.now()))
                .mimeType(String.valueOf(mediaType))
                .normalUrl(url)
                .resolvedUrl(String.valueOf(finalUrl))
                .build();
    }
}
