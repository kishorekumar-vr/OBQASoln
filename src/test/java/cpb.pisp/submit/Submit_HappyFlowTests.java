package cpb.pisp.submit;


import model.Environment;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import suites.PISP_CPB_Regression;

@RunWith(BlockJUnit4ClassRunner.class)
public class Submit_HappyFlowTests extends BaseSubmitTests{
    public Submit_HappyFlowTests(){super("/happyFlow");}

    @Test
    public void test_TS_TPP_FP_SUB_CPB_05(){
        final Environment env = this.getEnvironment("TS_TPP_FP_SUB_CPB_05","pisp");
        this.successfulSubmit(env);
    }

    @Test
    @Category(PISP_CPB_Regression.PispCpbRegessionCategory.class)
    public void test_TS_TPP_FP_SUB_CPB_06(){
        final Environment env = this.getEnvironment("TS_TPP_FP_SUB_CPB_05","pisp");
        this.successfulSubmit(env);
    }

}
