package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
  @Autowired
  private UserRepository userRepository;
  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(User user, Map<String, Object> model) {
    User userFromDb = userRepository.findByUsername(user.getUsername());

    if (userFromDb != null) {
      model.put("message", "User exists!");
      return "registration";
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    userRepository.save(user);
    return "redirect:/login";
  }
}
