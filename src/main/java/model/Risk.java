package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Risk {

    @JsonProperty(value="PaymentContextCode")
    private String paymentContextCode;

    @JsonProperty(value="MerchantCategoryCode")
    private String merchantCategoryCode;

    @JsonProperty(value="MerchantCustomerIdentification")
    private String merchantCustomerIdentification;

    @JsonProperty(value="DeliveryAddress")
    private String deliveryAddress;
}
