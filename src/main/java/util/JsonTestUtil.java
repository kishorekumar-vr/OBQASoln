package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import java.net.URL;

@Slf4j
@SuppressWarnings("unused")
public class JsonTestUtil {
    //Logger LOG =(Logger) org.slf4j.LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private static ObjectMapper objectMapper = configureMapper();

    @SuppressWarnings("WeakerAccess")
    public static ObjectMapper configureMapper(){
        ObjectMapper mapper= new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS,true);
        return mapper;
    }

    @SuppressWarnings("WeakerAccess")
    public static <T> T fromJson(final URL url, final Class<T> clazz){
        try{
            return objectMapper.readValue(url, clazz);
        }catch(Exception e){
            //log.warn("Failed to deserialise JSON at URL: {}",url,e);
            throw new RuntimeException(e);
        }
    }

}
