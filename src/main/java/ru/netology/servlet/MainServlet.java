package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.configuration.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {
  private PostController controller;
  private PostService service;
  private PostRepository repository;

  private String getMethod = "GET";
  private String postMethod = "POST";
  private String deleteMethod = "DELETE";
  private String pathEquals = "/api/posts";
  private String pathEqualsRemove = "/api/posts/\\d+";
  private String pathLastIndexOf = "/";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(getMethod) && path.equals(pathEquals)) {
        controller.all(resp);
        return;
      }
      if (method.equals(getMethod) && path.matches(pathEqualsRemove)) {
         controller.getById(parseId(path), resp);
        return;
      }
      if (method.equals(postMethod) && path.equals(pathEquals)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(deleteMethod) && path.matches(pathEqualsRemove)) {
        controller.removeById(parseId(path), resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e) {
      e.getMessage();
      resp.setStatus(resp.SC_NOT_FOUND);
    } catch (IOException e) {
      e.getMessage();
      resp.setStatus(resp.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long parseId(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf(pathLastIndexOf) + 1));
  }

}

