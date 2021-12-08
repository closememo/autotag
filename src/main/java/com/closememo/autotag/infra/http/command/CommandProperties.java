package com.closememo.autotag.infra.http.command;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties("http.command")
@Configuration
public class CommandProperties {

  private String rootUri;
  private String token;
}
