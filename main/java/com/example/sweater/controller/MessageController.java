package com.example.sweater.controller;

import static com.example.sweater.controller.ControllerUtils.getErrorsMap;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repository.MessageRepository;
import com.example.sweater.service.MessageService;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MessageController {
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private MessageService messageService;

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping("/")
  public String greeting(Map<String, Object> model) {
    return "greeting";
  }

  @GetMapping("/main")
  public String main(
      @RequestParam(required = false, defaultValue = "")
      String filter,
      Model model,
      @PageableDefault(sort = {"id"}, direction = DESC)
      Pageable pageable
  ) {
    Page<Message> page = messageService.messageList(pageable, filter);

    model.addAttribute("page", page);
    model.addAttribute("url", "/main");
    model.addAttribute("filter", filter);

    return "main";
  }

  @PostMapping("/main")
  public String add(
      @AuthenticationPrincipal User user,
      @Valid Message message,
      BindingResult bindingResult, // list of arguments and messages about validation errors
      Model model,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    message.setAuthor(user);

    if (bindingResult.hasErrors()) {
      Map<String, String> errorsMap = getErrorsMap(bindingResult);
      model.mergeAttributes(errorsMap);
      model.addAttribute("message", message);
    } else {
      //file.getOriginalFilename() file will be saved only if fileName is present

      saveFile(message, file);
      model.addAttribute("message", null);

      messageRepository.save(message);
    }

    Iterable<Message> messages = messageRepository.findAll();
    model.addAttribute("messages", messages);
    return "main";
  }

  private void saveFile(Message message, MultipartFile file) throws IOException {
    if (!file.isEmpty() && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFileName = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/" + resultFileName));

      message.setFileName(resultFileName);
    }
  }

  @GetMapping("/user-messages/{author}")
  public String userMessages(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User author,
      Model model,
      @RequestParam(required = false) Message message,
      @PageableDefault(sort = {"id"}, direction = DESC)
      Pageable pageable
  ) {
    Page<Message> page = messageService.messageListForUser(pageable, author);

    model.addAttribute("userChannel", author);
    model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
    model.addAttribute("subscribersCount", author.getSubscribers().size());
    model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
    model.addAttribute("page", page);
    model.addAttribute("message", message);
//    model.addAttribute("url", "/user-messages/{user}");
    model.addAttribute("url", "/user-messages/" + author.getId());
    model.addAttribute("isCurrentUser", currentUser.equals(author));

    return "userMessages";
  }

  @PostMapping("/user-messages/{user}")
  public String updateMessage(
      @AuthenticationPrincipal User currentUser,
      @PathVariable Long user,
      @RequestParam("id") Message message,
      @RequestParam("text") String text,
      @RequestParam("tag") String tag,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    if (message.getAuthor().equals(currentUser)) {
      if (!StringUtils.isEmpty(text)) {
        message.setText(text);
      }
      if (!StringUtils.isEmpty(tag)) {
        message.setTag(tag);
      }

      saveFile(message, file);

      messageRepository.save(message);
    }
    return "redirect:/user-messages/" + user;
  }
}
