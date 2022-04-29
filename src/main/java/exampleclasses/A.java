package exampleclasses;

import annotation.Component;
import annotation.ConfigProperty;
import annotation.DataType;

@Component
public class A implements InterfaceA {
    @ConfigProperty(type = DataType.INTEGER)
    private int num;

    public int getNum() {
        return num;
    }
}
