package com.elegantcone.filesdata;

import com.elegantcone.data.ConsoleArguments;
import com.elegantcone.modules.Logger;

public class IntegerValueGetter extends AbstractValueGetter<Integer> {
    public IntegerValueGetter(Logger logger, ConsoleArguments arguments) {
        super(logger, arguments);
    }

    @Override
    public Integer parseValue(String line) {
        try {
            return Integer.parseInt(line);
        }
        catch (Exception e) {
            return null;
        }
    }
}
