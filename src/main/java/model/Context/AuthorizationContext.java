package model.Context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class AuthorizationContext {

    private String accessToken;
    private String consentId;
    private Map<String,String> cookies;
    private LocalDateTime serverTime = LocalDateTime.now();
    private List<String> possibleAccountIds;
}
