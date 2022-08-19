package az.company.unitech.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRateRequestModel {
    private String from;
    private String to;

    @Override
    public boolean equals(Object obj)
    {

        // if both the object references are
        // referring to the same object.
        if(this == obj)
            return true;

        // it checks if the argument is of the
        // type Geek by comparing the classes
        // of the passed argument and this object.
        // if(!(obj instanceof Geek)) return false; ---> avoid.
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        // type casting of the argument.
        CurrencyRateRequestModel ob2 = (CurrencyRateRequestModel) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (ob2.from.equals(this.from)  && ob2.to.equals(this.to));
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (from != null) {
            result = 31 * result + from.hashCode();
        }
        if (to != null) {
            result = 31 * result + to.hashCode();
        };
        return from.hashCode() + to.hashCode();
    }
}
