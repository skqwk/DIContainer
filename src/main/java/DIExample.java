import exampleclasses.A;
import exampleclasses.B;
import exampleclasses.C;
import exampleclasses.D;
import exampleclasses.E;
import injectinator.Injectinator;

public class DIExample {
    public static void main(String[] args) {
        try {
            Injectinator.init("di-container/target/classes");
            Injectinator.getGraph().showDependencies();

            Configurator.configure(Injectinator.getComponentClasses());

            A a = Injectinator.getInstance(A.class);
            B b = Injectinator.getInstance(B.class);
            C c = Injectinator.getInstance(C.class);
            D d = Injectinator.getInstance(D.class);
            E e = Injectinator.getInstance(E.class);

            assert e.getC() == c;
            assert e.getD() == d;

            assert c.getA() == a;
            assert c.getB() == b;

            assert d.getA() == a;
            assert d.getB() == b;

            // Read from application.properties
            assert a.getNum() == 2;
            assert b.getNum() == 3;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
