package testbase.helpers;

import testbase.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class FileHelper {

    public static File getFileFromResource(String fullFileName) throws FileNotFoundException {

        ClassLoader classLoader = Configuration.class.getClassLoader();
        URL resource = classLoader.getResource(fullFileName);

        if (resource == null) {
            throw new FileNotFoundException(String.format("File %s not found!", fullFileName));
        } else {
            return new File(resource.getFile());
        }
    }
}
