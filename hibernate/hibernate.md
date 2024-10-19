# Hibernate Overview

## What is Hibernate?
Hibernate is an object-relational mapping (ORM) framework for Java. It provides a framework for mapping an object-oriented domain model to a relational database. Hibernate simplifies database interactions by abstracting the complexity of database access and operations.

### Key Features:
- **ORM**: Maps Java objects to database tables.
- **Data Querying**: Provides HQL (Hibernate Query Language) for querying data.
- **Automatic Dirty Checking**: Automatically detects changes in persistent objects and synchronizes them with the database.
- **Caching**: Supports first-level and second-level caching for performance optimization.
- **Lazy Loading**: Loads related data only when accessed, reducing the initial data load.

## Hibernate Configuration Files
Hibernate uses several configuration files to set up its environment and manage sessions. The main configuration files include:

1. **hibernate.cfg.xml**:
   - This is the main configuration file for Hibernate, where you define database connection properties, dialect, and mapping files.
   - Example:
     ```xml
     <hibernate-configuration>
         <session-factory>
             <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
             <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
             <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/mydb</property>
             <property name="hibernate.connection.username">user</property>
             <property name="hibernate.connection.password">password</property>
             <mapping class="com.example.Employee"/>
         </session-factory>
     </hibernate-configuration>
     ```

2. **Mapping Files**:
   - These files define how Java classes are mapped to database tables. They can be XML files or annotations in the entity classes.
   - Example (XML mapping):
     ```xml
     <hibernate-mapping>
         <class name="com.example.Employee" table="Employee">
             <id name="id" column="id">
                 <generator class="identity"/>
             </id>
             <property name="name" column="name"/>
             <property name="department" column="department"/>
         </class>
     </hibernate-mapping>
     ```

3. **Annotation Configuration**:
   - Hibernate also supports annotations to define entity mappings directly in the Java classes.

## Lazy Loading
Lazy loading is a design pattern commonly used in Hibernate to enhance performance by delaying the loading of related entities until they are explicitly accessed. This is particularly useful in reducing the initial load time of data.

### Key Points:
- **Default Behavior**: By default, Hibernate uses lazy loading for collections (like `List`, `Set`) and associations (like `@OneToMany`, `@ManyToOne`).
- **FetchType**: You can specify the fetching strategy using the `@OneToMany` or `@ManyToOne` annotations.
   - Example:
     ```java
     @OneToMany(fetch = FetchType.LAZY)
     private List<Address> addresses;
     ```

### Advantages:
- **Performance**: Reduces the amount of data fetched from the database, leading to lower memory consumption.
- **Efficiency**: Only loads data when it is actually needed, improving application responsiveness.

### Disadvantages:
- **N+1 Problem**: If not handled carefully, lazy loading can lead to the N+1 select problem, where multiple queries are made to fetch related entities.
- **Initialization Issues**: Lazy-loaded collections can throw `LazyInitializationException` if accessed outside the active Hibernate session.

## Key Interfaces in Hibernate
Hibernate provides several important interfaces that are essential for interacting with the ORM framework:

1. **Session**:
   - Represents a single unit of work with the database and provides methods for CRUD operations.
   - Example:
     ```java
     Session session = sessionFactory.openSession();
     session.beginTransaction();
     session.save(emp); // Save operation
     session.getTransaction().commit();
     session.close();
     ```

2. **SessionFactory**:
   - A thread-safe factory for `Session` objects. It is initialized once and is responsible for creating `Session` instances.
   - Example:
     ```java
     SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
     ```

3. **Transaction**:
   - Represents a database transaction and provides methods to begin, commit, and roll back transactions.
   - Example:
     ```java
     Transaction transaction = session.beginTransaction();
     // Perform operations
     transaction.commit();
     ```

4. **Query**:
   - Represents a HQL or SQL query and provides methods for executing it.
   - Example:
     ```java
     Query<Employee> query = session.createQuery("FROM Employee", Employee.class);
     List<Employee> employees = query.list();
     ```

5. **Criteria**:
   - An API for building queries programmatically. Useful for dynamic query construction.
   - Example:
     ```java
     CriteriaBuilder cb = session.getCriteriaBuilder();
     CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
     Root<Employee> root = cq.from(Employee.class);
     cq.select(root).where(cb.equal(root.get("department"), "IT"));
     List<Employee> employees = session.createQuery(cq).getResultList();
     ```


# Hibernate Lifecycle

In Hibernate, entity objects can be in one of four primary states: **Transient**, **Persistent**, **Detached**, or **Removed**. These states define the lifecycle of an entity as it interacts with a Hibernate `Session`.

## 1. Transient State
- **Description**: An object is created but **not yet associated** with a session and **not saved** in the database.
- **Example**:
    ```java
    Employee emp = new Employee(); // Transient state
    emp.setName("John Doe");
    emp.setDepartment("IT");
    ```

## 2. Persistent State
- **Description**: An object is **associated with a session** and **synchronized with the database**.
- **Actions**: The object enters the persistent state through methods like `save()`, `persist()`, or `update()`.
- **Example**:
    ```java
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.save(emp); // emp is now in persistent state
    session.getTransaction().commit();
    session.close(); // Persistent state ends when session is closed
    ```

## 3. Detached State
- **Description**: After the session is closed, the object is in the **detached** state. The object is **no longer managed by Hibernate**, but still exists in memory.
- **Actions**: Changes to the object are not automatically reflected in the database unless re-associated with a session using `update()`.
- **Example**:
    ```java
    emp.setName("Jane Doe"); // Detached state: changes are not synced to DB

    Session newSession = sessionFactory.openSession();
    newSession.beginTransaction();
    newSession.update(emp); // Re-attaching to session (persistent again)
    newSession.getTransaction().commit();
    newSession.close(); // Detached again
    ```

## 4. Removed State
- **Description**: The object is marked for deletion using `delete()`. After the transaction is committed, the object will be **deleted from the database**.
- **Example**:
    ```java
    Session session = sessionFactory.openSession();
    session.beginTransaction();

    session.delete(emp); // emp is now in removed state

    session.getTransaction().commit(); // Object deleted from DB
    session.close();
    ```

## Summary of Hibernate Lifecycle States:

| State       | Description                                                                  | Actions to Transition                           |
|-------------|------------------------------------------------------------------------------|------------------------------------------------|
| **Transient** | Object is created but not associated with the session or database.          | Object creation using `new`                    |
| **Persistent** | Object is associated with a session and synchronized with the database.     | `save()`, `persist()`, `update()`              |
| **Detached** | Object is no longer associated with a session but exists in memory.          | Session is closed, or transaction committed    |
| **Removed**   | Object is marked for deletion and will be removed from the database.         | `delete()`                                     |

## Full Workflow Example:

```java
public class HibernateLifecycleExample {
    public static void main(String[] args) {
        // Step 1: Transient state
        Employee emp = new Employee();
        emp.setName("Alice");
        emp.setDepartment("HR");

        // Step 2: Persistent state
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(emp); // Now in persistent state
        session.getTransaction().commit();
        session.close(); // Detached state

        // Step 3: Detached state
        emp.setDepartment("Finance"); // Changes not reflected in DB

        // Step 4: Re-attaching to session
        Session session2 = sessionFactory.openSession();
        session2.beginTransaction();
        session2.update(emp); // Back to persistent state
        session2.getTransaction().commit();
        session2.close(); // Detached again

        // Step 5: Removed state
        Session session3 = sessionFactory.openSession();
        session3.beginTransaction();
        session3.delete(emp); // Marked for deletion
        session3.getTransaction().commit(); // Deleted from DB
        session3.close();
    }
}
```
