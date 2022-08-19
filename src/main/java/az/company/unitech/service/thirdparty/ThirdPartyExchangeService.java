package az.company.unitech.service.thirdparty;


import org.springframework.stereotype.Service;

@Service
public interface ThirdPartyExchangeService {
    double getExchangeRate(String from, String to);
}
