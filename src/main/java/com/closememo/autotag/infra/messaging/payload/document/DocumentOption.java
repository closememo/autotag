package com.closememo.autotag.infra.messaging.payload.document;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentOption implements Serializable {

  private static final long serialVersionUID = -6562068589493140969L;

  private boolean hasAutoTag;
}
