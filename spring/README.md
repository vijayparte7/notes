# Spring Bean Lifecycle & Spring Container

### Spring Container

   The Spring Container is responsible for managing the complete lifecycle of beans. It provides IoC (Inversion of Control) and DI (Dependency Injection) mechanisms to handle bean creation, dependency injection, and lifecycle management.

#### Types of Spring containers:

1. **BeanFactory**: Lightweight container for basic DI functionality.
```declarative
BeanFactory factory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
MyBean myBean = (MyBean) factory.getBean("myBean");
```
2. **ApplicationContext**: Extends BeanFactory, providing more enterprise features like event propagation, internationalization, and more.

```declarative
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
MyBean myBean = (MyBean) context.getBean("myBean");
```
##### Key Differences:


| **Feature**                     | **BeanFactory**                               | **ApplicationContext**                        |
|----------------------------------|-----------------------------------------------|-----------------------------------------------|
| **Bean Instantiation**           | Lazy initialization (beans created on demand) | Eager initialization (singleton beans created at startup) |
| **Resource Loading**             | No built-in support                          | Supports resource loading (e.g., files, classpath) |
| **Best for**                     | Simple, resource-constrained environments     | Most common for modern, feature-rich applications |
| **Internationalization (i18n)**  | Not supported                                | Supports i18n via message sources             |
| **AOP Support**                  | Basic support                                | Full AOP integration                          |
| **Autowiring & Post-processing** | Manual registration of `BeanPostProcessor`    | Automatically handles `BeanPostProcessor` and `BeanFactoryPostProcessor` |
| **Configuration Sources**        | Typically XML-based                          | Supports XML, Java-based, and annotations     |
| **Lifecycle Management**         | Limited                                      | Provides full lifecycle management (e.g., `ApplicationListener`) |


### Spring Bean Lifecycle Phases

   The lifecycle of a Spring bean can be broken down into the following phases:

#### Phase 1: Instantiation

The Spring container creates an instance of the bean (object).
The instance is created based on the bean definition (either in XML, annotations, or Java config).
Example:

```
public class MyBean {
    public MyBean() {
        System.out.println("MyBean instance created!");
    }
}
```

#### Phase 2: Populate Properties (Dependency Injection)

Spring injects dependencies into the bean via Constructor Injection, Setter Injection, or Field Injection.
Example: Constructor Injection:
```
@Component
public class MyBean {
private final DependencyService dependencyService;
`
    @Autowired
    public MyBean(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }
}
```

#### Phase 3: Initialization

After the bean is instantiated and dependencies are injected, custom initialization logic can be executed.
Initialization can be done using:
`@PostConstruct` annotation.
Implementing InitializingBean interface.
Specifying a custom init-method in XML or Java-based config.
Example: Using @PostConstruct:
```
@PostConstruct
public void init() {
System.out.println("MyBean is fully initialized!");
}
```

#### Phase 4: Post-Initialization (BeanPostProcessor)

After initialization, Spring provides the BeanPostProcessor interface to allow additional processing.
postProcessBeforeInitialization: Logic before initialization.
postProcessAfterInitialization: Logic after initialization.
Example:

```
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Before Initialization of: " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("After Initialization of: " + beanName);
        return bean;
    }
}
```

#### Phase 5: Destruction

When the Spring container shuts down (e.g., during application termination), destruction logic is executed.
Destruction can be managed using:
`@PreDestroy` annotation.
Implementing DisposableBean interface.
Specifying a custom destroy-method in XML or config.
Example: Using @PreDestroy:
```
@PreDestroy
public void destroy() {
System.out.println("MyBean is being destroyed!");
}
```

### Complete Bean Lifecycle

   Here’s a quick summary of the phases in order:

**Instantiation**: Spring creates the bean.

**Dependency Injection**: Spring injects dependencies.
Initialization: Custom logic runs (@PostConstruct or others).
Post-Initialization: Additional processing with BeanPostProcessor.
Destruction: Cleanup logic runs when the application shuts down (@PreDestroy).


### Common annotations used in the Spring Bean lifecycle:

* **@Component**: Marks a class as a Spring-managed bean.
* **@Autowired**: Injects dependencies into a bean.
* **@PostConstruct**: Method executed after dependency injection and before the bean is fully initialized.
* **@PreDestroy**: Method executed before the bean is destroyed.

### BeanPostProcessor Interface

   The BeanPostProcessor interface allows custom logic to be applied to beans during their lifecycle:

**postProcessBeforeInitialization**: Logic before the initialization of each bean.

**postProcessAfterInitialization**: Logic after the initialization of each bean.
This is useful for adding proxying or custom wrappers to beans.

### Practical Example of Bean Lifecycle

   Here’s a real-world example demonstrating the entire lifecycle:
[JAVA CODE](SpringContainer.java)