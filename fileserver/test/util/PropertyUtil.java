package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
    public static String getPropertyValue(String name){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("test/resources/properties.property")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(name);
    }
}
