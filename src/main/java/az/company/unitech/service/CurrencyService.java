package az.company.unitech.service;

import az.company.unitech.model.CurrencyRateRequestModel;
import az.company.unitech.service.cache.CachingService;
import az.company.unitech.service.thirdparty.ThirdPartyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyService {


    private ThirdPartyExchangeService thirdPartyExchangeService;

    @Autowired
    private CachingService<CurrencyRateRequestModel, Double> cachingService;

    public double getCurrentExchangeRate(CurrencyRateRequestModel requestModel) {
        Optional<Double> value = cachingService.get(requestModel);

        if (value.isPresent()) {
            return value.get();
        }

        // retry logic can be applied here in case of failure
        double exchangeRate = thirdPartyExchangeService.getExchangeRate(requestModel.getFrom(), requestModel.getTo());
        return cachingService.put(requestModel, exchangeRate);
    }
}
