package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class Environment {
    private AuthZEnvironment AuthZ;
    private TppAuthenticationEnvironment tppAuthN;
    private CustomerEnvironment customer;
    private AispEnvironment aisp;
    private CustomerAuthNEnvironment customerAuthN;
    private PispEnvironment pisp;
    private ExecutionEnvironment executionEnvironment;


    public Environment mergeIn(final Environment toMerge){
        if(toMerge.AuthZ != null){
            this.AuthZ = (this.AuthZ == null) ? toMerge.AuthZ : this.AuthZ.mergeIn(toMerge.AuthZ);
        }
        if(toMerge.tppAuthN != null){
            this.tppAuthN = (this.tppAuthN == null) ? toMerge.tppAuthN : this.tppAuthN.mergeIn(toMerge.tppAuthN);
        }
        if(toMerge.pisp != null){
            this.pisp = (this.pisp == null) ? toMerge.pisp : this.pisp.mergeIn(toMerge.pisp);
        }
        if(toMerge.aisp != null){
            this.aisp = (this.aisp == null) ? toMerge.aisp : this.aisp.mergeIn(toMerge.aisp);
        }
        if(toMerge.tppAuthN != null){
            this.tppAuthN = (this.tppAuthN == null) ? toMerge.tppAuthN : this.tppAuthN.mergeIn(toMerge.tppAuthN);
        }
        if(toMerge.executionEnvironment != null){
            this.executionEnvironment = (this.executionEnvironment == null) ? toMerge.executionEnvironment : this.executionEnvironment.mergeIn(toMerge.executionEnvironment);
        }
        if(toMerge.customer != null){
            this.customer = (this.customer == null) ? toMerge.customer : this.customer.mergeIn(toMerge.customer);
        }
        if(toMerge.customerAuthN != null){
            this.customerAuthN = (this.customerAuthN == null) ? toMerge.customerAuthN : this.customerAuthN.mergeIn(toMerge.customerAuthN);
        }

        return this;
    }
}
