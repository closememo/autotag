package com.closememo.autotag.infra.messaging;

public abstract class Message {

  public abstract MessageType getMessageType();

  public enum MessageType {
    DOMAIN_EVENT
  }
}
