package util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ConfigUsageTracker {
    public static Set<String> getEnvTestCases = new HashSet<String>();
    public static Set<String> getExpectedResponseTestCases = new HashSet<String>();

    public static void trackGetEnv(String testCaseId){
        if(getEnvTestCases.contains(testCaseId)){
            log.warn("Test case Id used for multiple get env calls:"+ testCaseId);
        }
        getEnvTestCases.add(testCaseId);
    }
}
