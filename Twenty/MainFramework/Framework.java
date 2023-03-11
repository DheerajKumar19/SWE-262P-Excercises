package MainFramework;

// https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-collaborators

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Framework {

    static void panic() {
        throw new RuntimeException();
    }

    static <T> T load(String jarPath, String className) {
        try {
            return do_load(jarPath, className);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        panic();
        return null;
    }

    private static <T> T do_load(String jarPath, String className) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        System.out.println("Trying to load " + jarPath);
        URL classUrl = new File(jarPath).toURI().toURL();
        URL[] urls = {classUrl};
        ClassLoader cloader = new URLClassLoader(urls);
        Class countCls = cloader.loadClass(className);
        Object instance = countCls.getDeclaredConstructor().newInstance();
        return (T) instance;
    }

    private static Properties getProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "config.properties";
        FileInputStream inputStream = new FileInputStream(propFileName);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("File not found");
        }
        inputStream.close();
        return prop;
    }

    public static void main(String[] args) {
        try {
            Properties prop = getProperties();

            ExtractWords extract = load(prop.getProperty("pathToJar"), prop.getProperty("nameOfExtract"));
            Counter counter = load(prop.getProperty("pathToJar"), prop.getProperty("nameOfCount"));

            List<String> words = extract.extractWords(args[0]);
            List<Map.Entry<String, Integer>> wordsCount = counter.count(words);
            for (Map.Entry<String, Integer> entry : wordsCount) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}