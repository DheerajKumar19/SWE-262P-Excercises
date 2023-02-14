package Ex5;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class JarClasses {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        /**
         * Inital set up
         * Load the class path and file
         */
        String jarFileName = buildJarFilePath(args);
        URLClassLoader url_class_loader = getUrlClassLoader(jarFileName);


//        String jarFileName = "C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\java\\Ex5\\jsonLatest.jar";

        try (JarFile jarFile = new JarFile(jarFileName)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                /**
                 * check if the entry is not a directory
                 * check if it is a class or not
                 */
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {

                    /**
                     * setting up counter for each class.
                     */
                    int publicMethodCount = 0;
                    int privateMethodCount = 0;
                    int protectedMethodCount = 0;
                    int staticMethodCount = 0;
                    int numFields = 0;

                    /**
                     * now extract the class name from org/json/CDL.class format
                     */
                    String className = entry.getName().replaceAll("/", "\\.");
                    className = className.substring(0, className.length() - ".class".length());

                    /**
                     * Returns the Class object associated with the class or interface with the given string name.
                     */
                    Class<?> classExtracted = Class.forName(className, true, url_class_loader);

                    /**
                     * A Method provides information about, and access to, a single method on a class or interface.
                     * gets list of methods,and Modifiers is represented by int val.
                     */

                    Method[] methods = classExtracted.getDeclaredMethods();

                    for (Method method : methods) {
                        int modifiers = method.getModifiers();

                        /**
                         * depending on the modifiers check for their modifier type
                         */

                        if (Modifier.isPrivate(modifiers)) privateMethodCount++;
                        if (Modifier.isProtected(modifiers)) protectedMethodCount++;
                        if (Modifier.isPublic(modifiers)) publicMethodCount++;
                        if (Modifier.isStatic(modifiers)) staticMethodCount++;
                    }

                    /**
                     * to get the listed fields in the class
                     */
                    Field[] fields = classExtracted.getDeclaredFields();
                    numFields = fields.length;

                    printExtractedFields(publicMethodCount, privateMethodCount, protectedMethodCount, numFields, staticMethodCount, classExtracted);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildJarFilePath(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage Jar Path is not sent => " + args[0].toString());
            return null;
        } else {

            return args[0];
        }
    }

    private static URLClassLoader getUrlClassLoader(String arg) throws MalformedURLException {
        File file = new File(arg);
        URL url = file.toURI().toURL();
        URL[] urlArray = new URL[]{url};
        return new URLClassLoader(urlArray);
    }

    private static void printExtractedFields(int publicMethodCount, int privateMethodCount, int protectedMethodCount, int numFields, int staticMethodCount, Class<?> classExtracted) {
        System.out.println("\n----------" + classExtracted.getName() + "----------");
        System.out.println("Public methods: " + publicMethodCount);
        System.out.println("Private methods: " + privateMethodCount);
        System.out.println("Protected methods: " + protectedMethodCount);
        System.out.println("Static methods: " + staticMethodCount);
        System.out.println("Fields: " + numFields);
    }
}
