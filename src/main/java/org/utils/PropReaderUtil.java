package org.utils;

import java.io.*;
import java.util.Properties;

public class PropReaderUtil {

    private Properties properties;

    public PropReaderUtil (String resourceName) throws IOException {
        this.properties = new Properties();

        // Use ClassLoader to get the resource stream reliably
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);

        if (inputStream != null) {
            properties.load(inputStream);
            System.out.println("Loaded properties from " + resourceName);
        } else {
            // Throw a meaningful exception if the resource is not found
            throw new FileNotFoundException("Property file '" + resourceName + "' not found in the classpath.");
        }
    }

    public String getPropValue(String key)
    {
        return properties.getProperty(key);
    }


}
