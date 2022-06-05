package testbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.log4j.Log4j2;
import testbase.helpers.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;

@Log4j2
public class Configuration {

    private final String configFolder = "config";
    private final String envProperty = System.getProperty("envConfig", "qa");

    public <T> T readEnv(Class<T> tokenClass) throws FileNotFoundException {

        String fileName = "env_" + envProperty + ".yaml";
        String fullFileName = String.format("%s/%s/%s",configFolder,envProperty,fileName);
        log.info("Getting environment file with keys" + fullFileName);

        File file = FileHelper.getFileFromResource(fullFileName);

        T mappedEnv = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mappedEnv = mapper.readValue(file, tokenClass);
        }catch (Exception e) {
            log.error("Cannot read config data:");
            log.error((e.getMessage()));
            System.exit(0);
        }
        return mappedEnv;
    }

    public <T> T readKeys(Class<T> tokenClass) throws FileNotFoundException {

        String fileName = "authkeys_" + envProperty + ".yaml";
        String fullFileName = String.format("%s/%s/%s",configFolder,envProperty,fileName);
        log.info("Getting auth keys file with keys" + fullFileName);

        File file = FileHelper.getFileFromResource(fullFileName);

        T mappedAuthKeys = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mappedAuthKeys = mapper.readValue(file, tokenClass);
        }catch (Exception e) {
            log.error("Cannot read config data:");
            log.error((e.getMessage()));
            System.exit(0);
        }
        return mappedAuthKeys;
    }
}
