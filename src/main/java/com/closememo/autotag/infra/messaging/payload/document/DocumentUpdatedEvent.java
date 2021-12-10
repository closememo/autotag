package com.closememo.autotag.infra.messaging.payload.document;

import com.closememo.autotag.infra.messaging.DomainEvent;
import com.closememo.autotag.infra.messaging.payload.Identifier;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentUpdatedEvent extends DomainEvent {

  private static final long serialVersionUID = 128520692309901884L;

  private Identifier documentId;
  private Identifier ownerId;
  private String title;
  private String previousContent;
  private String content;
  private List<String> tags;
  private ZonedDateTime updatedAt;
  private DocumentOption previousOption;
  private DocumentOption option;
}
