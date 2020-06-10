package model;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain=true)
public class AuthenticationResult {
    private Map<String, String> cookies;
    private String accessToken;
    private CustomerAuthenticationContext context;
    private String accessURL;

    public AuthenticationResult addCookie(String name, String value){
        if(this.cookies == null){
            this.cookies = Maps.newHashMap();
        }
        this.cookies.put(name, value);
        return this;
    }
}
