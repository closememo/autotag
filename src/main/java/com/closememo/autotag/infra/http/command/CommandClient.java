package com.closememo.autotag.infra.http.command;

import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommandClient {

  private static final String X_SYSTEM_KEY_HEADER = "X-SYSTEM-KEY";

  private final RestTemplate restTemplate;

  public CommandClient(RestTemplateBuilder restTemplateBuilder,
      CommandProperties properties) {

    this.restTemplate = restTemplateBuilder
        .rootUri(properties.getRootUri())
        .defaultHeader(X_SYSTEM_KEY_HEADER, properties.getToken())
        .build();
  }

  public void updateAutoTags(String documentId, List<String> autoTags) {
    RequestEntity<UpdateAutoTagsRequest> requestEntity = RequestEntity
        .post("/command/system/update-auto-tags")
        .body(new UpdateAutoTagsRequest(documentId, autoTags));

    ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class);

    validateResponse(response, "[COMMAND] updateAutoTags failed.");
  }

  private void validateResponse(@NonNull ResponseEntity<?> response, String errorMessage) {
    if (response.getStatusCode().is5xxServerError()) {
      throw new CommandInternalServerException(errorMessage);
    }

    if (response.getStatusCode().isError()) {
      throw new CommandClientException(errorMessage);
    }
  }
}
