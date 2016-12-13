package com.acuo.persist.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

public class Neo4jHealthCheck extends HealthCheck {

    private final String serviceRoot = "/db/data/";
    private String expectedVersion = null;
    private final String urlString;

    private final Client client;

    @Inject
    public Neo4jHealthCheck(Client client, @Named("acuo.healthchecks.neo4j.url") String url) {
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(url);
        urlStringBuilder.append(serviceRoot);

        this.client = client;
        this.urlString = urlStringBuilder.toString();
    }

    @Override
    protected Result check() throws Exception {
        
        RootResponse response = client.target(urlString).request(MediaType.APPLICATION_JSON).get(RootResponse.class);

        if (response == null) {
            return Result.unhealthy("Null response from Neo4j at " + urlString);
        }

        if (expectedVersion != null) {
            if (!expectedVersion.equals(response.neo4jVersion)) {
                return Result.unhealthy("Expected Neo4j version " + expectedVersion + " but found " + response.neo4jVersion);
            }
        }
        return Result.healthy();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RootResponse {
        @JsonProperty("neo4j_version")
        private String neo4jVersion;
    }
}