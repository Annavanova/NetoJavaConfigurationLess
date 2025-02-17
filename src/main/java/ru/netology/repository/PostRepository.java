package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  private int postCount;

  public PostRepository() {
    posts.addAll(List.of(
            new Post(1, "First"),
            new Post(2, "Second"),
            new Post(3, "Third"),
            new Post(4, "Fourth"),
            new Post(5, "Five")
    ));
    postCount = 4;
  }

  public List<Post> all() {
    return posts;
  }

  public synchronized Optional<Post> getById(long id) {
    return posts.stream().filter(post -> post.getId() == id).findFirst();
  }

  public synchronized Post save(Post post) {
    Optional<Post> lookForPost = posts.stream().filter(p -> p.getId() == post.getId()).findFirst();
    if (post.getId() == 0 || lookForPost.isEmpty()) {
      post.setId(++postCount);
      posts.add(post);
      postCount = posts.size();
    }
    return post;
  }

  public synchronized void removeById(long id) {
    for (Post p : posts) {
      if (p.getId() == id) {
        posts.remove(p);
        postCount = posts.size();
        break;
      }
    }
  }
/*  private final Map<Long, Post> posts = new ConcurrentHashMap<>(){};
  private final AtomicLong counter = new AtomicLong(0L);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() != 0 && !posts.containsKey(post.getId())) {
      throw new NotFoundException();
    }

    if (post.getId() == 0) {
      var newId = counter.incrementAndGet();
      post.setId(newId);
    }

    posts.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    if (!posts.containsKey(id))
      throw new NotFoundException();
    posts.remove(id);
  }*/
}
