package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthZEnvironment {
    private String hostHeader;
    private String baseUrl;

    public AuthZEnvironment mergeIn(AuthZEnvironment toMerge){
        if(toMerge.hostHeader!=null){
            this.hostHeader = toMerge.hostHeader;
        }

        if(toMerge.baseUrl!=null){
            this.baseUrl = toMerge.baseUrl;
        }
        return this;
    }
}
