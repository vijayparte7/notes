import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Lazy
public class LazyBean {

    public LazyBean() {
        System.out.println("LazyBean instantiated");
    }

    public void doSomething() {
        System.out.println("Doing something with LazyBean");
    }
}

@Component
public class MyService {

    @Autowired
    private LazyBean lazyBean;

    public void useLazyBean() {
        System.out.println("Calling LazyBean for the first time...");
        lazyBean.doSomething();  // This is where the LazyBean gets instantiated
    }
}

//In this example:
//
//The LazyBean is not instantiated when the application starts.
//It is only instantiated the first time useLazyBean() is called.