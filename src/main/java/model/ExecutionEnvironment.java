package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class ExecutionEnvironment {
    private String brand;
    private String testEnvironment;

    public ExecutionEnvironment mergeIn(ExecutionEnvironment toMerge){
        if(toMerge.brand!=null){
            this.brand  = toMerge.brand;
        }
        if(toMerge.testEnvironment!=null){
            this.testEnvironment  = toMerge.testEnvironment;
        }
        return this;
    }
}
