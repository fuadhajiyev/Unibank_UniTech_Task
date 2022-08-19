package az.company.unitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeCacheValueModel {
    private Double value;
    private long epochTimeValueStored;
}
