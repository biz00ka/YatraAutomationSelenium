package org.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropReaderUtil {

    private Properties properties;

    public PropReaderUtil (String path) throws IOException {
        this.properties= new Properties();
        properties.load(new FileInputStream(path));

    }

    public String getPropValue(String key)
    {
        return properties.getProperty(key);
    }


}
