import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MyBean {

    public MyBean() {
        System.out.println("Step 1: Bean Instantiated.");
    }

    @PostConstruct
    public void init() {
        System.out.println("Step 3: Bean Initialization.");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Step 5: Bean Destruction.");
    }
}

//BeanPostProcessor:

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Step 2: Before Initialization of bean: " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Step 4: After Initialization of bean: " + beanName);
        return bean;
    }
}    
/*
Output:

Step 1: Bean Instantiated.
Step 2: Before Initialization of bean: myBean
Step 3: Bean Initialization.
Step 4: After Initialization of bean: myBean
Step 5: Bean Destruction.
*/