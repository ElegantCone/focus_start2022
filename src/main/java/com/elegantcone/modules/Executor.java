package com.elegantcone.modules;

import com.elegantcone.data.ConsoleArguments;
import com.elegantcone.exceptions.BadDataException;
import com.elegantcone.filesdata.AbstractValueGetter;
import com.elegantcone.filesdata.IntegerValueGetter;
import com.elegantcone.filesdata.StringValueGetter;

import java.io.*;
import java.util.*;

public class Executor {
    private final Logger logger;
    private final Map<BufferedReader, String> fileReaders;
    private BufferedWriter fileWriter;
    private final List<BufferedReader> readersToRemove;
    private AbstractValueGetter<?> valueGetter;

    public Executor(Logger logger) {
        this.logger = logger;
        fileReaders = new HashMap<>();
        readersToRemove = new ArrayList<>();
    }

    public void setArguments(ConsoleArguments dataset) {
        switch (dataset.dataType) {
            case String:
                valueGetter = new StringValueGetter(logger, dataset);
                break;
            case Integer:
                valueGetter = new IntegerValueGetter(logger, dataset);
                break;
        }
    }

    public void execute(List<BufferedReader> readers, BufferedWriter writer) throws IOException {
        fileWriter = writer;
        readLineFromReaders(readers);
        mergeSort();
    }

    private void mergeSort() throws IOException {
        while (fileReaders.size() > 0) {
            try {
                List<BufferedReader> readersToRead = new ArrayList<>();
                BufferedReader reader = valueGetter.getNeededReader(fileReaders);
                if (reader != null) {
                    writeValue(fileReaders.get(reader));
                    readersToRead.add(reader);
                }
                if (!valueGetter.getBadDataReaders().isEmpty()) {
                    readersToRead.addAll(valueGetter.getBadDataReaders());
                }
                readLineFromReaders(readersToRead);
                if (!readersToRemove.isEmpty()) {
                    removeReaders(readersToRemove);
                }
            } catch (BadDataException e) {
                logger.logException(e);
            }
        }
    }

    private void removeReaders(List<BufferedReader> readersToRemove) {
        for (BufferedReader reader : readersToRemove) {
            fileReaders.remove(reader);
        }
        readersToRemove.clear();
    }

    private void writeValue(String value) throws IOException {
        fileWriter.write(value);
        fileWriter.newLine();
    }

    private void readLineFromReaders(Collection<BufferedReader> readers) {
        for (BufferedReader reader : readers) {
            try {
                String line = reader.readLine();
                if (line == null) {
                    readersToRemove.add(reader);
                }
                fileReaders.put(reader, line);
            } catch (IOException ignored) {
            }
        }
    }

}
