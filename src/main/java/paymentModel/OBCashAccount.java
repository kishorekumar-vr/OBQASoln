package paymentModel;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class OBCashAccount {

    @JsonProperty("SchemeName")
    @Size(min =1, max = 40)
    @NotNull
    private String schemeName = null;

    @JsonProperty("Identification")
    @Size(min =1, max = 256)
    @NotNull
    private String identification = null;

    @JsonProperty("Name")
    @Size(min =1, max = 70)
    private String name = null;

    @JsonProperty("SecondaryIdentification")
    @Size(min =1, max = 34)
    private String secondaryIdentification = null;
}
