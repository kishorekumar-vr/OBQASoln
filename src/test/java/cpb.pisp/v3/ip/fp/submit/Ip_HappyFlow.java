package cpb.pisp.v3.ip.fp.submit;

import model.Environment;
import org.junit.Test;

public class Ip_HappyFlow extends BaseSubmitTests{
    public Ip_HappyFlow() {
        super("/HappyFlow");
    }

    @Test
    public void test_TS_TPP_IP_CPB_Submit_001(){

       final Environment env= this.getEnvironment("TS_TPP_IP_SIP_CPB_Submit_001","pisp");
        env.getPisp().setBaseUrl("http://localhost:8089");
        env.getTppAuthN().setTokenEndpointUrl("http://localhost:8089");
       this.successfulSubmit(env);

    }
}
