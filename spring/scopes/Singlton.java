import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Singleton {

    public Singleton() {
        System.out.println("SingletonBean instantiated");
    }

    public void doSomething() {
        System.out.println("SingletonBean doing something");
    }
}

@Component
public class MyService {

    @Autowired
    private Singleton singletonBean1;

    @Autowired
    private Singleton singletonBean2;

    public void testSingleton() {
        System.out.println(singletonBean1 == singletonBean2); // Output: true
    }
}
//In this example:
//Both singletonBean1 and singletonBean2 are the same instance, since the bean is created only once.