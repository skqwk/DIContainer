package annotation;

import java.util.function.Function;

public enum DataType {
    INTEGER(Integer::parseInt),
    STRING((String val) -> val),
    DOUBLE(Double::parseDouble);

    public final Function<String, Object> parse;



    DataType(Function<String, Object> parse) {
        this.parse = parse;
    }
}
