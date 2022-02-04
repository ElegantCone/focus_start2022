package com.elegantcone.filesdata;

import com.elegantcone.data.ConsoleArguments;
import com.elegantcone.exceptions.BadDataException;
import com.elegantcone.exceptions.BadSortedFileException;
import com.elegantcone.modules.Logger;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public abstract class AbstractValueGetter<T extends Comparable<T>> {
    private final Map<Boolean, BiPredicate<T, T>> comparators = new HashMap<>();
    protected T lastValue = null;
    protected T newValue = null;
    protected ConsoleArguments argumentsDataset;
    protected Logger logger;
    protected List<BufferedReader> badDataReaders = new ArrayList<>();

    public AbstractValueGetter(Logger logger, ConsoleArguments arguments) {
        this.logger = logger;
        argumentsDataset = arguments;
        comparators.put(true, (v1, v2) -> v1.compareTo(v2) <= 0);
        comparators.put(false, (v1, v2) -> v1.compareTo(v2) >= 0);
    }

    public BufferedReader getNeededReader(Map<BufferedReader, String> fileReaders) throws BadDataException {
        badDataReaders.clear();
        newValue = null;
        BufferedReader bufferedReader = null;
        for (BufferedReader reader : fileReaders.keySet()) {
            T value = parseValue(fileReaders.get(reader));
            if (value == null) {
                logger.logException(new BadDataException());
                badDataReaders.add(reader);
                continue;
            }

            if (newValue == null) {
                newValue = value;
            }

            if (comparators.get(argumentsDataset.isAscending).test(value, newValue)) {
                if (lastValue != null && !comparators.get(argumentsDataset.isAscending).test(lastValue, value)) {
                    logger.logException(new BadSortedFileException());
                    badDataReaders.add(reader);
                    continue;
                }
                newValue = value;
                bufferedReader = reader;
            }
        }
        if (newValue != null)
            lastValue = newValue;
        return bufferedReader;
    }

    public List<BufferedReader> getBadDataReaders() {
        return badDataReaders;
    }

    public abstract T parseValue(String line);


}
