package az.company.unitech.service.cache;

import org.springframework.context.annotation.Bean;

import java.util.Optional;

public interface CachingService<K,V> {
    Optional<V> get(K key);
    V put(K key, V value);
}
