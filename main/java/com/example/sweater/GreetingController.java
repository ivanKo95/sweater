package com.example.sweater;

import com.example.sweater.domain.Message;
import com.example.sweater.repository.MessageRepository;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/")
  public String greeting(
      Map<String, Object> model
  ) {
    return "greeting";
  }

  @GetMapping("/main")
  public String main(Map<String, Object> model) {
    Iterable<Message> messages = messageRepository.findAll();
    model.put("messages", messages);
    return "main";
  }

  @PostMapping("/main")
  public String add(
      @RequestParam String text,
      @RequestParam String tag,
      Map<String, Object> model
  ) {
    Message message = new Message(text, tag);

    messageRepository.save(message);
    Iterable<Message> messages = messageRepository.findAll();
    model.put("messages", messages);
    return "main";
  }

  @PostMapping("filter")
  public String filter(
      @RequestParam String filter,
      Map<String, Object> model
  ) {
    Iterable<Message> messages;
    if (filter != null && !filter.isEmpty()) {
      messages = messageRepository.findByTag(filter);
    } else {
      messages = messageRepository.findAll();
    }
    model.put("messages", messages);
    return "main";
  }
}
