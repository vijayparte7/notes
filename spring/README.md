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

### Lazy Loading vs Eager Loading in Spring

#### 1. Lazy Loading

   Lazy loading means that a bean is created and initialized only when it is first requested by the application. This can improve application startup time, as only necessary beans are loaded.

Key Points:
Bean is created on demand, i.e., when the getBean() method is called or it’s injected for the first time.
Suitable for beans that are not immediately required at application startup.
Reduces memory usage at startup, as fewer beans are loaded.
Beans are initialized later, which may introduce a slight delay when they are first accessed.

How to Enable Lazy Loading:
Use the `@Lazy` annotation in Spring.

Example: [JAVA CODE](LazyLoading.java)

**Spring Boot Configuration:**
You can globally enable lazy initialization in Spring Boot by adding the property in application.properties:

`spring.main.lazy-initialization=true`

#### 2. Eager Loading

   Eager loading means that a bean is created and initialized as soon as the Spring context is started, regardless of whether the bean is actually used. This is the default behavior in Spring.

Key Points:
Bean is created at the time of container startup.
Suitable for beans that are required immediately or frequently during the application’s execution.
Increases memory usage at startup because all required beans are initialized early.
Ensures all dependencies are resolved during startup, avoiding runtime surprises.

### Scopes
In Spring, scopes define the lifecycle of a bean and determine how and when a bean is created, as well as how it is shared within the Spring application.

#### 1. Singleton Scope (Default)

   In singleton scope, Spring creates a single instance of the bean, and this instance is shared across the entire Spring container. This is the default scope if no scope is specified.

Only one instance of the bean is created per Spring container.
The same instance is returned for every request to the bean.

Example: [JAVA CODE](scopes/Singlton.java)

#### 2. Prototype Scope

   In prototype scope, a new instance of the bean is created every time it is requested. The bean is not shared and is created afresh for each use.

A new instance is created every time the bean is requested.
Useful when different states or behavior are needed for each usage.

**How to Define Prototype Scope**:
Use the annotation with `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`.

Example: [JAVA CODE](scopes/Prototype.java)

#### 3. Request Scope (Web Applications)

   In request scope, a new bean instance is created for each HTTP request and is shared within that request. Once the request is complete, the bean is discarded.


A new instance is created per HTTP request.
Typically used in web applications, ensuring that each HTTP request gets its own instance of the bean.

Use the annotation with  `@Scope(WebApplicationContext.SCOPE_REQUEST)`.

#### 4. Session Scope (Web Applications)

   In session scope, a bean instance is created for each HTTP session. The bean is shared across the session and is discarded when the session expires.

A new instance is created per HTTP session.
Used when you want to store session-specific information in a bean.

Use the annotation with `@Scope(WebApplicationContext.SCOPE_SESSION)`.

#### 5. Application Scope (Web Applications)

   In application scope, a bean is created per ServletContext and is shared across the entire application. This scope is similar to the singleton scope but is specific to web applications.

A single instance is created per ServletContext.
The bean is shared across the entire application for all requests and sessions.

Use the annotation with `@Scope(WebApplicationContext.SCOPE_APPLICATION)`.

#### 6. Global Session Scope (Portlets Only)

   In global session scope, the bean is tied to a global HTTP session. This scope is used in portlet-based web applications where multiple portlets may share the same session.

A new instance is created for each global HTTP session (specific to portlet-based applications).
Typically used when multiple portlets need to share a session.

Use the annotation with `@Scope(WebApplicationContext.SCOPE_GLOBAL_SESSION)`.

