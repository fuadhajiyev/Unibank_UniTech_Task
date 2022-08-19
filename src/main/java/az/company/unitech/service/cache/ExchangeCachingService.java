package az.company.unitech.service.cache;

import az.company.unitech.model.CurrencyRateRequestModel;
import az.company.unitech.model.ExchangeCacheValueModel;
import az.company.unitech.service.cache.CachingService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeCachingService implements CachingService<CurrencyRateRequestModel, Double> {

    private Map<CurrencyRateRequestModel, ExchangeCacheValueModel> cacheMap;

    public ExchangeCachingService() {
        cacheMap = new HashMap<>();
    }

    /**
     * Check if key exists in map and the difference between time the value stored and now is less than 1 min,
     * return Cached value, otherwise remove the key from cache and return Null.
     *
     * @param key
     * @return
     */
    @Override
    public Optional<Double> get(CurrencyRateRequestModel key) {
        if (!cacheMap.containsKey(key)) {
            return Optional.empty();
        }

        ExchangeCacheValueModel value = cacheMap.get(key);

        long now = Instant.now().toEpochMilli();
        long diffInMillies = Math.abs(value.getEpochTimeValueStored() - now);

        if (TimeUnit.MINUTES.toMillis(1) <= diffInMillies) {
            cacheMap.remove(key);
            return Optional.empty();
        }

        return Optional.of(value.getValue());
    }

    @Override
    public Double put(CurrencyRateRequestModel key, Double value) {
        ExchangeCacheValueModel v = new ExchangeCacheValueModel(value, Instant.now().toEpochMilli());
        cacheMap.put(key, v);
        return value;
    }
}
