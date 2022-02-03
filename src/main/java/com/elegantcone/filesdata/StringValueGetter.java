package com.elegantcone.filesdata;

import com.elegantcone.data.ConsoleArguments;
import com.elegantcone.modules.Logger;

public class StringValueGetter extends AbstractValueGetter<String> {
    public StringValueGetter(Logger logger, ConsoleArguments arguments) {
        super(logger, arguments);
    }

    @Override
    public String parseValue(String line) {
        if (line.contains(" "))
            return null;
        return line;
    }
}
