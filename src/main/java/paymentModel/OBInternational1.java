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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class OBInternational1 {

    @JsonProperty("ConsentId")
    @Size(min =1, max = 128)
    @NotNull
    private String consentId = null;

    @JsonProperty("InstructionIdentification")
    @Size(min =1, max = 35)
    @NotNull
    private String instructionIdentification = null;

    @JsonProperty("EndToEndIdentification")
    @Size(min =1, max = 35)
    @NotNull
    private String endToEndIdentification = null;

    @JsonProperty("LocalInstrument")
    @Size(min =1, max = 50)
    private String localInstrument = null;

    @JsonProperty("InstructionPriority")
    @Valid
    private OBPriority2Code instructionPriority = null;

    @JsonProperty("Purpose")
    @Size(min =1, max = 4)
    private String purpose = null;

    @JsonProperty("chargeBearer")
    @Valid
    private String chargeBearer = null;

    @JsonProperty("CurrencyOfTransfer")
    @Pattern(regexp = "^[A-Z]{3,3}$")
    @NotNull
    private String currencyOfTransfer = null;

    @JsonProperty("DestinationCountryCode")
    @Pattern(regexp = "^[A-Z]{2,2}$")
    private String destinationCountryCode = null;

    @JsonProperty("Amount")
    @NotNull
    @Valid
    private OBDomestic1InstructedAmount instructedAmount = null;

    @JsonProperty("DebtorAccount")
    @Valid
    private OBCashAccount debtorAccount = null;

    @JsonProperty("Creditor")
    @Valid
    private OBCreditorParty creditor = null;

    @JsonProperty("CreditorAgent")
    @Valid
    private OBBranchAndFinancialInstitutionIdentification3 creditorAgent = null;

    @JsonProperty("CreditorAccount")
    @NotNull
    @Valid
    private OBCashAccount creditorAccount = null;

    @JsonProperty("RemittanceInformation")
    @Valid
    private OBRemittanceInformation remittanceInformation = null;

    @JsonProperty("SupplementaryData")
    @Valid
    private OBSupplementaryData supplementaryData = null;
}
