package com.example.sweater;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import com.example.sweater.controller.MessageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = AFTER_TEST_METHOD)
@WithUserDetails("admin")
public class MainControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MessageController mainController;

  @Test
  public void mainPageTest() throws Exception {
    this.mockMvc.perform(get("/main"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//div[@class='navbar-text mr-3']").string("admin"));
  }

  @Test
  public void messageListTest() throws Exception {
    this.mockMvc.perform(get("/main"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//div[@class='card-columns']").nodeCount(1));
  }

  @Test
  public void filterMessageTest() throws Exception {
    this.mockMvc.perform(get("/main").param("filter", "second-tag"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//div[@class='card-columns']").nodeCount(1))
        .andExpect(xpath("//div[@class='card-columns']/div[@data-id=2]").exists());
  }

  @Test
  public void addMessageToListTest() throws Exception {
    MockHttpServletRequestBuilder multipart = multipart("/main")
        .file("file", "123".getBytes())
        .param("text", "fifth")
        .param("tag", "new one")
        .with(csrf());

    this.mockMvc.perform(multipart)
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//div[@class='card-columns']").nodeCount(1))
        .andExpect(xpath("//div[@class='card-columns']/div[@data-id=10]").exists())
        .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/span").string("fifth"))
        .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/i").string("#new one"));
  }
}
