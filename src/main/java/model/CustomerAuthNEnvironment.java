package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerAuthNEnvironment {
    private String hostHeader;
    private String baseUrl;

    public CustomerAuthNEnvironment mergeIn(CustomerAuthNEnvironment toMerge){
        if(toMerge.hostHeader!=null){
            this.hostHeader = toMerge.hostHeader;
        }

        if(toMerge.baseUrl!=null){
            this.baseUrl = toMerge.baseUrl;
        }
        return this;
    }
}
