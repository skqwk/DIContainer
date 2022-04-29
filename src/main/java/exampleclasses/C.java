package exampleclasses;

import annotation.Component;
import annotation.Inject;

@Component
public class C {
    private final InterfaceA a;
    private final InterfaceB b;
    private final D d;

    @Inject
    public C(InterfaceA a,
             InterfaceB b,
             D d) {
        this.a = a;
        this.b = b;
        this.d = d;
    }

    public InterfaceA getA() {
        return a;
    }

    public InterfaceB getB() {
        return b;
    }

    public D getD() {
        return d;
    }
}
