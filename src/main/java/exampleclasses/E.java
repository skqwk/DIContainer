package exampleclasses;


import annotation.Component;
import annotation.Inject;

@Component
public class E {
    private final D d;
    private final C c;

    @Inject
    public E(D d, C c) {
        this.d = d;
        this.c = c;
    }

    public D getD() {
        return d;
    }

    public C getC() {
        return c;
    }
}
