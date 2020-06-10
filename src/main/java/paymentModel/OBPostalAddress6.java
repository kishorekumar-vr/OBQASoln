package paymentModel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class OBPostalAddress6 {
    private String addressType = null;
    private String department = null;
    private String subDepartment = null;
    private String streetName = null;
    private String buildingNumber = null;
    private String postCode = null;
    private String townName = null;
    private String countrySubDivision = null;
    private String country = null;
    private List<String> addressLine = new ArrayList<String>();

}
