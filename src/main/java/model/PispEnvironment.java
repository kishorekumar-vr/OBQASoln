package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import paymentModel.InternationalPaymentDetails;
import paymentModel.OBPostalAddress6;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@Accessors(chain=true)
public class PispEnvironment {
    private String baseUrl;
    private String hostHeader;
    private String fileHash;
    private String fileType;
    private String fileReference;
    private String numberOfTransactions;
    private BigDecimal controlSum;
    private AccountDetails creditorDetails;
    private AccountDetails debtorDetails;
    @JsonProperty(value="Risk")
    private Risk risk;
    private OBPostalAddress6 creditorPostalAddress;
    private HashMap<String, String> headers;
    private BigDecimal amount;
    private String currency;
    private String endToEndId;
    private String instructionId;
    private String paymentDescription;
    private String statementReference;
    private String localInstrument;
    private String requestedExecutionDateTime;
    private String fundsConfirmationConsentExpirationDateTime;
    private InternationalPaymentDetails internationalPaymentDetails;


    public PispEnvironment mergeIn(final PispEnvironment toMerge){
        if(toMerge.controlSum!=null){
            this.controlSum = toMerge.controlSum;
        }
        if(toMerge.creditorDetails != null){
            this.creditorDetails = (this.creditorDetails == null)? toMerge.creditorDetails : this.creditorDetails.mergeIn(toMerge.creditorDetails);
        }
        if(toMerge.internationalPaymentDetails != null){
            this.internationalPaymentDetails = (this.internationalPaymentDetails == null)? toMerge.internationalPaymentDetails : this.internationalPaymentDetails.mergeIn(toMerge.internationalPaymentDetails);
        }
        return this;
    }

}
