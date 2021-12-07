package com.closememo.autotag.infra.messaging.handler;

import com.closememo.autotag.infra.messaging.payload.document.DocumentCreatedEvent;
import com.closememo.autotag.infra.messaging.payload.document.DocumentUpdatedEvent;
import com.closememo.autotag.service.AutoTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentDomainEventHandler {

  private final AutoTagService autoTagService;

  public DocumentDomainEventHandler(AutoTagService autoTagService) {
    this.autoTagService = autoTagService;
  }

  @ServiceActivator(inputChannel = "DocumentCreatedEvent")
  public void handle(DocumentCreatedEvent payload) {
    log.info("DocumentId=" + payload.getDocumentId());
  }

  @ServiceActivator(inputChannel = "DocumentUpdatedEvent")
  public void handle(DocumentUpdatedEvent payload) {
    log.info("DocumentId=" + payload.getDocumentId());
  }
}
