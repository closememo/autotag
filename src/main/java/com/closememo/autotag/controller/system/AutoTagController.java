package com.closememo.autotag.controller.system;

import com.closememo.autotag.service.AutoTagService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@SystemAutoTagInterface
public class AutoTagController {

  private final AutoTagService autoTagService;

  public AutoTagController(AutoTagService autoTagService) {
    this.autoTagService = autoTagService;
  }

  @PostMapping("/get-auto-tags")
  public List<String> getAutoTags(@RequestBody String content) {
    return autoTagService.getAutoTags(content);
  }
}
