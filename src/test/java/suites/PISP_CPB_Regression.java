package suites;

import cpb.pisp.submit.Submit_HappyFlowTests;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(PISP_CPB_Regression.PispCpbRegessionCategory.class)
@Suite.SuiteClasses({
        Submit_HappyFlowTests.class
})
public class PISP_CPB_Regression {
    public interface PispCpbRegessionCategory{

    }
}
