import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Prototype {

    public Prototype() {
        System.out.println("Prototype bean instantiated");
    }

    public void doSomething() {
        System.out.println("Prototype bean doing something");
    }
}

@Component
public class MyService {

    @Autowired
    private Prototype prototypeBean1;

    @Autowired
    private Prototype prototypeBean2;

    public void testPrototype() {
        System.out.println(prototypeBean1 == prototypeBean2); // Output: false
    }
}
//In this example:
//Each time the bean is injected, a new instance is created, so prototypeBean1 and prototypeBean2 are different objects.
