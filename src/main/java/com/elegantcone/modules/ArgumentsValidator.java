package com.elegantcone.modules;

import com.elegantcone.data.ConsoleArguments;
import com.elegantcone.enums.DataTypeEnum;
import com.elegantcone.exceptions.BadArgumentsException;
import com.elegantcone.exceptions.DuplicateFilenamesException;

import java.util.*;

public class ArgumentsValidator {
    private static final Map<String, Boolean> SORT_MODES = Map.of(
            "-a", true,
            "-d", false);
    private static final Map<String, DataTypeEnum> DATA_TYPES = Map.of(
            "-s", DataTypeEnum.String,
            "-i", DataTypeEnum.Integer);

    private final ConsoleArguments consoleArguments;
    private final Logger logger;

    public ArgumentsValidator(Logger logger) {
        consoleArguments = new ConsoleArguments();
        consoleArguments.isAscending = null;
        consoleArguments.dataType = null;
        this.logger = logger;
    }

    public void validateArgs (String[] args) throws BadArgumentsException, DuplicateFilenamesException {
        List<String> files = new ArrayList<>();
        for (String arg : args) {
            if (SORT_MODES.containsKey(arg)) {
                if (consoleArguments.isAscending != null){
                    throwBadArguments();
                }
                consoleArguments.isAscending = SORT_MODES.get(arg);
            }
            else if (DATA_TYPES.containsKey(arg)) {
                if (consoleArguments.dataType != null){
                    throwBadArguments();
                }
                consoleArguments.dataType = DATA_TYPES.get(arg);
            }
            else {
                if (!files.contains(arg))
                    files.add(arg);
                else
                    throwDuplicateFilenames();
            }
        }
        if (args.length < 3 || consoleArguments.dataType == null || files.size() < 2) {
            throwBadArguments();
        }
        consoleArguments.outputFile = files.get(0);
        files.remove(0);
        if (files.contains(consoleArguments.outputFile)) {
            throwDuplicateFilenames();
        }
        consoleArguments.inputFiles = files;
        consoleArguments.isAscending = (consoleArguments.isAscending == null) || consoleArguments.isAscending;
    }

    public ConsoleArguments getArguments() {
        return consoleArguments;
    }

    private void throwBadArguments() throws BadArgumentsException {
        BadArgumentsException exception = new BadArgumentsException();
        logger.logException(exception);
        throw exception;
    }

    private void throwDuplicateFilenames() throws DuplicateFilenamesException {
        DuplicateFilenamesException exception = new DuplicateFilenamesException();
        logger.logException(exception);
        throw exception;
    }
}
