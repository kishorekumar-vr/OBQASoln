package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class AccountDetails {
    private String accountIdentifier;
    private String secondaryAccountIdentifier;
    private String agentIdentifier;
    private IdentifierScheme agentScheme;
    private IdentifierScheme accountScheme;
    private String name;

    public AccountDetails mergeIn(final AccountDetails toMerge){
        if(toMerge.accountIdentifier!= null){
            this.accountIdentifier = toMerge.accountIdentifier;
        }
        if(toMerge.secondaryAccountIdentifier!= null){
            this.secondaryAccountIdentifier = toMerge.secondaryAccountIdentifier;
        }
        if(toMerge.agentIdentifier!= null){
            this.agentIdentifier = toMerge.agentIdentifier;
        }
        if(toMerge.accountIdentifier!= null){
            this.accountIdentifier = toMerge.accountIdentifier;
        }
        if(toMerge.agentScheme!= null){
            this.agentScheme = toMerge.agentScheme;
        }
        if(toMerge.accountScheme!= null){
            this.accountScheme = toMerge.accountScheme;
        }
        if(toMerge.name!= null){
            this.name = toMerge.name;
        }
        return this;

    }
}
