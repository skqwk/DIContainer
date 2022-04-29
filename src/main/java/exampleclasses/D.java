package exampleclasses;

import annotation.Component;
import annotation.Inject;

@Component
public class D {
    private final InterfaceA a;
    private final InterfaceB b;

    @Inject
    public D(InterfaceA a,
             InterfaceB b) {
        this.a = a;
        this.b = b;
    }

    public InterfaceA getA() {
        return a;
    }

    public InterfaceB getB() {
        return b;
    }
}
