package io.klever.techchallenge.infrastructure.adapters.out.redis;

import io.klever.techchallenge.application.ports.out.HandleCacheOutputPort;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandleRedisCacheAdapter implements HandleCacheOutputPort {

  private final RedisTemplate<String, Object> redisTemplate;

  public HandleRedisCacheAdapter(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public <T> Optional<T> get(String key, Class<T> type) {
    Object value = redisTemplate.opsForValue().get(key);
    if (value == null) {
      return Optional.empty();
    }

    if (type.isInstance(value)) {
      return Optional.of(type.cast(value));
    }

    return Optional.empty();
  }

  @Override
  public <T> Optional<List<T>> getList(String key, Class<T> elementType) {
    Object value = redisTemplate.opsForValue().get(key);
    if (value == null) {
      return Optional.empty();
    }

    if (value instanceof List<?>) {
      List<?> list = (List<?>) value;
      List<T> typedList = new ArrayList<>();

      for (Object item : list) {
        if (elementType.isInstance(item)) {
          typedList.add(elementType.cast(item));
        }
      }

      return Optional.of(typedList);
    }

    return Optional.empty();
  }

  @Override
  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(1));
  }
}

