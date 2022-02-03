package com.elegantcone.modules;

import com.elegantcone.exceptions.BadArgumentsException;
import com.elegantcone.exceptions.BadDataException;
import com.elegantcone.exceptions.BadSortedFileException;
import com.elegantcone.exceptions.DuplicateFilenamesException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Logger {
    private static final Map<Class<?>, String> errorNames = new HashMap<>();
    static {
        errorNames.put(BadArgumentsException.class, "errorMessage.BadArguments");
        errorNames.put(FileNotFoundException.class, "errorMessage.FileException");
        errorNames.put(DuplicateFilenamesException.class, "errorMessage.DuplicateFilenamesException");
        errorNames.put(BadDataException.class, "errorMessage.BadDataException");
        errorNames.put(BadSortedFileException.class, "errorMessage.BadSortedFileException");

    }

    private static Properties errorMessages;

    public Logger () {
        try {
            initErrorProperty();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void logException(Exception e) {
        System.err.println(errorMessages.getProperty(errorNames.get(e.getClass()), e.getMessage()));
    }


    private void initErrorProperty() throws IOException {
        String errorsPropertyName = "ErrorMessages.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        errorMessages = new Properties();
        InputStream resourceStream = loader.getResourceAsStream(errorsPropertyName);
        errorMessages.load(resourceStream);
    }
}
