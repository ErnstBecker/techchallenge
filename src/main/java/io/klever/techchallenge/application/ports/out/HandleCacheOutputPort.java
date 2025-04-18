package io.klever.techchallenge.application.ports.out;

import java.util.List;
import java.util.Optional;

public interface HandleCacheOutputPort {

  <T> Optional<T> get(String key, Class<T> type);
  <T> Optional<List<T>> getList(String key, Class<T> elementType);
  void set(String key, Object value);
}
