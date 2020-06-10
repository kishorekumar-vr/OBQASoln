package integrationtest;

import lombok.extern.slf4j.Slf4j;
import model.Environment;
import steps.AuthenticationSteps;
import steps.PISPStepsV3;
import util.ConfigTemplateUtils;
import util.TestEnvironmentUtils;

@Slf4j
public abstract class AbstractTestEnvironmentAwareSuper {
    final protected PISPStepsV3 pispStepsV3 = new PISPStepsV3();

    final protected AuthenticationSteps authenticationSteps = new AuthenticationSteps();
    final protected String testConfigBasePath;
    final protected ConfigTemplateUtils templateUtils = new ConfigTemplateUtils();


    protected AbstractTestEnvironmentAwareSuper(final String basePathToConfig) {
        this.testConfigBasePath = basePathToConfig;
    }

    protected Environment getEnvironment(String type) {
        return this.getEnvironment(null,type);
    }

    protected Environment getEnvironment(String testCaseId, String type){
        return this.getEnvironment(testCaseId,"/",type);
    }

    protected Environment getEnvironment(String testCaseId, String startOfPath, String type){
        String releaseVersion = TestEnvironmentUtils.getReleaseVersion(type);

        String envName= "qa";
        String brand = "ubn";
        String brandAndEspEnv = "UBN-U1-UK";
        return getEnvironment(testCaseId, startOfPath,  type,  envName,  brand,  brandAndEspEnv,  releaseVersion );
    }

    protected Environment getEnvironment(String testCaseId, String startOfPath, String type, String envName, String brand, String dataEnvName, String releaseVersion){
        //checkIfShouldAbortAllTests();
        Environment environmentConfig= new EnvironmentConfigLoader(this.testConfigBasePath).loadEnvironmentConfig(testCaseId,startOfPath,type,envName,brand,dataEnvName,releaseVersion);

        try{
            this.templateUtils.parseOutTemplatedValues(testCaseId, environmentConfig);
        }catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
        return environmentConfig;
    }
}
