package paymentModel;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class InternationalPaymentDetails {

    private OBPriority2Code instructionPriority;
    private String purpose;
    private String chargeBearer;
    private String currencyOfTransfer;
    private OBBranchAndFinancialInstitutionIdentification3 creditorAgent;
    private OBCreditorParty creditor;

    public InternationalPaymentDetails mergeIn(InternationalPaymentDetails toMerge){
        if(toMerge.instructionPriority!= null){
            this.instructionPriority  = toMerge.instructionPriority;
        }
        if(toMerge.purpose!= null){
            this.purpose  = toMerge.purpose;
        }
        if(toMerge.chargeBearer!= null){
            this.chargeBearer  = toMerge.chargeBearer;
        }
        if(toMerge.creditorAgent!= null){
            this.creditorAgent  = toMerge.creditorAgent;
        }
        if(toMerge.creditor!= null){
            this.creditor  = toMerge.creditor;
        }
        return this;
    }


}
