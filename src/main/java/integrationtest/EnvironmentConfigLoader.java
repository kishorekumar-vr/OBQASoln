package integrationtest;

import lombok.extern.slf4j.Slf4j;
import model.Environment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import util.ConfigUsageTracker;
import util.JsonTestUtil;
import util.TestEnvironmentUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Slf4j
public class EnvironmentConfigLoader {
    private static final String JSON = ".json";
    private static final String CONFIG_FILE_NAME = "env.json";
    private final String testConfigBasePath;



    public EnvironmentConfigLoader(final String testConfigBasePath) {
        this.testConfigBasePath = testConfigBasePath;
    }

    public Environment loadEnvironmentConfig(final String testCaseId, final String startOfPath, final String type, final String envName, final String brand, final String dataEnvName, final String releaseVersion){
        if(StringUtils.isEmpty(testCaseId)){
            log.info("No specific test file specified, walking up the tree...");
        }else{
            ConfigUsageTracker.trackGetEnv(testCaseId);
            log.info("Getting config for testcaseId: {} ", testCaseId);
        }

        final String envAndBrand = TestEnvironmentUtils.formatEnvAndBrand(envName,brand);
        final Environment environmentConfig = new Environment();

        final String pathToWalk = startOfPath + TestEnvironmentUtils.getReleaseVersionPath(releaseVersion);

        mergeEnvironementConfigs(environmentConfig,loadEnvironmentConfigsForPath(pathToWalk,envAndBrand+JSON,type,releaseVersion));

        final String fullPathToWalk = (StringUtils.isNoneBlank(testCaseId)) ?
                TestEnvironmentUtils.testCaseEnvPath(environmentConfig, dataEnvName, this.testConfigBasePath, testCaseId, type, releaseVersion):
                TestEnvironmentUtils.baseEnvPath(environmentConfig,dataEnvName,this.testConfigBasePath,type,releaseVersion);

        mergeEnvironementConfigs(environmentConfig, loadEnvironmentConfigsForPath(fullPathToWalk,CONFIG_FILE_NAME,type,releaseVersion));

        return environmentConfig;
    }

    private static List<Environment> loadEnvironmentConfigsForPath(final String fullPath, final String envFileName, final String type, final String releaseVersion){
        final List<Environment> environmentConfigs = new ArrayList<>();

        //pathFlag - The logic was setting it to true initially and then conditionally setting it to false in getRelevantRootPath()
        //instead of static boolean a MutableBoolean object is used.

        walkPathAndAddConfigIfFound(fullPath,environmentConfigs,envFileName,type,releaseVersion, new MutableBoolean(true));
        return environmentConfigs;
    }


    private static void walkPathAndAddConfigIfFound(final String fullPath, final List<Environment> environmentConfigs, final String envFileName, final String type, final String releaseVersion, final MutableBoolean pathFlag){
        final int lastIndexOfDirectorySlash = StringUtils.lastIndexOf(fullPath,"/");

        if(lastIndexOfDirectorySlash>-1){
            final String nextLevelUp = StringUtils.substring(fullPath, 0,lastIndexOfDirectorySlash);
            final String nextRelevantLevelUp = getRelevantRootPath(nextLevelUp, TestEnvironmentUtils.getReleaseVersionPath(releaseVersion),pathFlag);
            final Optional<Environment> loadedConfig = loadConfigFile(nextRelevantLevelUp+"/"+envFileName);

            if(loadedConfig.isPresent()){
                environmentConfigs.add(loadedConfig.get());
            }
            walkPathAndAddConfigIfFound(nextRelevantLevelUp,environmentConfigs,envFileName,type,releaseVersion,pathFlag);
        }
    }

    private static String getRelevantRootPath(final String path, final String releaseVersionPath, final MutableBoolean pathFlag){
        if(StringUtils.isNotEmpty(releaseVersionPath) && path.contains("aisp") && Arrays.asList(path.split("aisp")).size() ==1 && pathFlag.isTrue()){
            pathFlag.setFalse();
            return path.split("aisp")[0] +"aisp"+"/"+releaseVersionPath.substring(0,releaseVersionPath.lastIndexOf("/"));
        } else if(StringUtils.isNotEmpty(releaseVersionPath) && path.contains("pisp") && Arrays.asList(path.split("pisp")).size() ==1 && pathFlag.isTrue()){
            pathFlag.setFalse();
            return path.split("pisp")[0] +"pisp"+"/"+releaseVersionPath.substring(0,releaseVersionPath.lastIndexOf("/"));
        }
        return path;
    }

    private static void mergeEnvironementConfigs(final Environment environmentConfig, final List<Environment> environmentConfigsToMerge){
        for(int x = environmentConfigsToMerge.size()-1; x>=0; x--){
            environmentConfig.mergeIn(environmentConfigsToMerge.get(x));
        }
    }

    public static Optional<Environment> loadConfigFile(final String path){
        final URL resource = EnvironmentConfigLoader.class.getResource(path);
        if(resource==null){
            log.info("not found:" + path);
            return Optional.empty();
        }
        else{
            log.info("found :"+ path);
        }
        return Optional.of(JsonTestUtil.fromJson(resource,Environment.class));

    }



}
