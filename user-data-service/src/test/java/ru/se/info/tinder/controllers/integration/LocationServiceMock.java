package ru.se.info.tinder.controllers.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

@TestConfiguration
public class LocationServiceMock {
    public static void setupMockLocationResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/locations/list?locationIds=1"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                        .withBody(
                                copyToString(
                                        LocationServiceMock.class.getClassLoader().
                                                getResourceAsStream("payload/locations.txt"),
                                        defaultCharset()))));
    }

    public static void setupMockLocationUnavailableResponse(WireMockServer mockService) throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/locations/list?locationIds=2"))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                        .withBody(
                                copyToString(
                                        LocationServiceMock.class.getClassLoader().
                                                getResourceAsStream("payload/locations.txt"),
                                        defaultCharset()))));
    }
}