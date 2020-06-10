package util;

import lombok.extern.slf4j.Slf4j;
import model.Environment;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class TestEnvironmentUtils {
    public static final String[] RELEASEVERSIONLIST = new String[]{"v1.1", "v2.0", "v3.1"};

    public static String getReleaseVersion(String type){
        if(type.equalsIgnoreCase("aisp")){
            return  "v3.1";
        }else if(type.equalsIgnoreCase("pisp")){
            return "";
        } else
            return "";
    }
    public static String getReleaseVersionPath(final String releaseVersion){
        return TestEnvironmentUtils.getReleaseVersionPath(null, releaseVersion);
    }

    public static String getReleaseVersionPath(Environment env, String releaseVersion){
        final List<String> releaseList = Arrays.asList(RELEASEVERSIONLIST);
        if(!releaseList.contains(releaseVersion)){
            return "";
        }
        return releaseVersion+"/";
    }

    public static String formatEnvAndBrand(String envName, String brand){
        return envName + "-" + brand;
    }

    public static String removeLeadingAndTrailingSlashes(String path){
        String result = path;
        if(result.startsWith("/")){
            result = result.substring(1);
        }
        if(result.endsWith("/")){
            result = result.substring(0,result.length() -1);
        }
        return result;
    }
    public static String testCaseEnvPath(Environment env, String espDataEnv, String configRoot, String testCaseId, String type, String releaseVersion){
        return String.format("/%s/%s/%s/%s", espDataEnv,removeLeadingAndTrailingSlashes(configRoot),testCaseId,getReleaseVersionPath(env,releaseVersion));
    }

    public static String baseEnvPath(Environment env, String espDataEnv, String configRoot, String type, String releaseVersion){
        return String.format("/%s/%s/%s", espDataEnv,removeLeadingAndTrailingSlashes(configRoot),getReleaseVersionPath(env,releaseVersion));
    }

}
