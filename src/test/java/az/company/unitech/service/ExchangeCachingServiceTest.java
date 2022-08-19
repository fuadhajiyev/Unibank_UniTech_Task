package az.company.unitech.service;

import az.company.unitech.model.CurrencyRateRequestModel;
import az.company.unitech.service.cache.CachingService;
import az.company.unitech.service.cache.ExchangeCachingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ExchangeCachingServiceTest {

    @Test
    void get_WhenKeyIsStoredInOneMinute_ReturnValueSuccessfully() {
        CachingService<CurrencyRateRequestModel, Double> service = new ExchangeCachingService();

        CurrencyRateRequestModel key = new CurrencyRateRequestModel("USD", "AZN");
        service.put(key, 1.7);

        CurrencyRateRequestModel key1 = new CurrencyRateRequestModel("USD", "AZN");
        Optional<Double> value = service.get(key1);


        Assertions.assertTrue(value.isPresent());
        assertThat(value.get()).isEqualTo(1.7);
    }


    @Test
    void get_WhenKeyIsStoredInOverOneMinute_ReturnNull() {
        CachingService<CurrencyRateRequestModel, Double> service = new ExchangeCachingService();

        String instantExpected = "2022-01-01T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);

        try (MockedStatic<Instant> mockedStatic = mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            CurrencyRateRequestModel key = new CurrencyRateRequestModel("USD", "AZN");
            service.put(key, 1.7);
        }


        CurrencyRateRequestModel key1 = new CurrencyRateRequestModel("USD", "AZN");
        Optional<Double> value = service.get(key1);

        Assertions.assertTrue(value.isEmpty());
    }
}