package com.closememo.autotag.infra.messaging.handler;

import com.closememo.autotag.infra.http.command.CommandClient;
import com.closememo.autotag.infra.messaging.payload.Identifier;
import com.closememo.autotag.infra.messaging.payload.document.DocumentCreatedEvent;
import com.closememo.autotag.infra.messaging.payload.document.DocumentUpdatedEvent;
import com.closememo.autotag.service.AutoTagService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentDomainEventHandler {

  private final AutoTagService autoTagService;
  private final CommandClient commandClient;

  public DocumentDomainEventHandler(AutoTagService autoTagService,
      CommandClient commandClient) {
    this.autoTagService = autoTagService;
    this.commandClient = commandClient;
  }

  @ServiceActivator(inputChannel = "DocumentCreatedEvent")
  public void handle(DocumentCreatedEvent payload) {
    if (!payload.getOption().isHasAutoTag()) {
      return;
    }

    List<String> autoTags = autoTagService.getAutoTags(payload.getContent());

    if (CollectionUtils.isNotEmpty(autoTags)) {
      commandClient.updateAutoTags(Identifier.convertToString(payload.getDocumentId()), autoTags);
    }
  }

  @ServiceActivator(inputChannel = "DocumentUpdatedEvent")
  public void handle(DocumentUpdatedEvent payload) {
    if (!payload.getOption().isHasAutoTag()) {
      return;
    }

    List<String> autoTags = autoTagService.getAutoTags(payload.getContent());

    if (CollectionUtils.isNotEmpty(autoTags)) {
      commandClient.updateAutoTags(Identifier.convertToString(payload.getDocumentId()), autoTags);
    }
  }
}
