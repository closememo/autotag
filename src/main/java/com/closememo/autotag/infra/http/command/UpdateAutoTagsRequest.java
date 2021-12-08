package com.closememo.autotag.infra.http.command;

import java.util.List;
import lombok.Getter;

@Getter
public class UpdateAutoTagsRequest {

  private final String documentId;
  private final List<String> autoTags;

  public UpdateAutoTagsRequest(String documentId, List<String> autoTags) {
    this.documentId = documentId;
    this.autoTags = autoTags;
  }
}
