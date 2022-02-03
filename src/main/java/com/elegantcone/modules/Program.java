package com.elegantcone.modules;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private final ArgumentsValidator argumentsValidator;
    private final Logger logger;
    private final Executor executor;
    private final List<BufferedReader> fileReaders = new ArrayList<>();
    private BufferedWriter fileWriter;


    public Program() {
        logger = new Logger();
        argumentsValidator = new ArgumentsValidator(logger);
        executor = new Executor(logger);
    }

    public void run(String[] args) {
        try {
            argumentsValidator.validateArgs(args);
            executor.setArguments(argumentsValidator.getArguments());
            openFiles();

            executor.execute(fileReaders, fileWriter);
        } catch (Exception ignored) {
        }
        finally {
            closeAllFiles();
        }
    }

    private void openFiles() throws IOException {
        try {
            for (String name : argumentsValidator.getArguments().inputFiles) {
                try {
                    fileReaders.add(new BufferedReader(new FileReader(name)));
                } catch (FileNotFoundException e) {
                    logger.logException(e);
                }
            }
            if (fileReaders.size() < 1)
                throw new FileNotFoundException();
            fileWriter = new BufferedWriter(new FileWriter(argumentsValidator.getArguments().outputFile, false));
        }
        catch (IOException e) {
            logger.logException(e);
            closeAllFiles();
            throw e;
        }
    }

    private void closeAllFiles() {
        try
        {
            for (BufferedReader reader : fileReaders) {
                reader.close();
            }
            fileWriter.close();
            fileReaders.clear();
        } catch (Exception ignored) {
        }
    }
}
