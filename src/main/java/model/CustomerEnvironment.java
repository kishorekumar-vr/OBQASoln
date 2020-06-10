package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerEnvironment {
    private String customerId;
    private String dbid;
    private String pin;
    private String password;
    private String userId;
    private String userName;
    private String cin;
    private String bin;
    private String blcin;
    private String customerType;
    private String channelId;

    public CustomerEnvironment mergeIn(CustomerEnvironment toMerge){
        if(toMerge.customerId != null){
            this.customerId = toMerge.customerId;
        }
        if(toMerge.dbid != null){
            this.dbid = toMerge.dbid;
        }
        if(toMerge.pin != null){
            this.pin = toMerge.pin;
        }
        if(toMerge.password != null){
            this.password = toMerge.password;
        }
        if(toMerge.userId != null){
            this.userId = toMerge.userId;
        }
        if(toMerge.userName != null){
            this.userName = toMerge.userName;
        }
        if(toMerge.cin != null){
            this.cin = toMerge.cin;
        }
        if(toMerge.bin != null){
            this.bin = toMerge.bin;
        }
        if(toMerge.blcin != null){
            this.blcin = toMerge.blcin;
        }
        if(toMerge.customerType != null){
            this.customerType = toMerge.customerType;
        }
        if(toMerge.channelId != null){
            this.channelId = toMerge.channelId;
        }
        return this;
    }
}
