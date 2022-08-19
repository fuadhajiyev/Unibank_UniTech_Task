package az.company.unitech.service;

import az.company.unitech.model.CurrencyRateRequestModel;
import az.company.unitech.repository.AccountRepository;
import az.company.unitech.service.cache.CachingService;
import az.company.unitech.service.thirdparty.ThirdPartyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Test
    void getCurrentExchangeRate_WhenCacheIsAvailable() {
        // TODO:: getCurrentExchangeRate - does not hold much business logic,
        // It's not very necessary to write unit test for this service
        // Cause all will be mocking.

        // The actual caching service implementation test has been implemented
        // in ExchangeCachingServiceTest.class
    }
}