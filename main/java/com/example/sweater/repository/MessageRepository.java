package com.example.sweater.repository;

import com.example.sweater.domain.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

  List<Message> findByTag(String tag);
}
