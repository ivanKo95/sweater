package com.example.sweater.repository;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

  Page<Message> findAll(Pageable pageable);
  Page<Message> findByTag(String tag, Pageable pageable);
  Page<Message> findAllByAuthor(User user, Pageable pageable);
}
