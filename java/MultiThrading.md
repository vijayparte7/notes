### 1. Thread Class

   **Definition:** In Java, a thread is a lightweight process. Multithreading allows concurrent execution of two or more threads for maximum CPU utilization.
   
**Creating a thread:**
   By extending the Thread class and overriding the run() method.
   Example:
```
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread is running...");
    }
}

public class TestThread {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start(); // Starting the thread
    }
}
```

### 2. Runnable Interface

   **Definition:** Runnable is a functional interface used to define a task for threads. It provides better flexibility as the class implementing Runnable can extend another class.
   
**Creating a thread:**
   Implement the Runnable interface and pass it to the Thread class.
   Example:
```
class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Runnable thread is running...");
    }
}

public class TestRunnable {
    public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable);
        t1.start(); // Starting the thread
    }
}
```

### 3. ThreadPool

   **Definition:** A thread pool manages a set of worker threads, and you can reuse threads to perform tasks, avoiding the overhead of thread creation/destruction.
   
**Benefits:**
   Efficient resource management.
   Reduces thread creation overhead.
   Example using ExecutorService: [JAVA CODE](multithreading/ExecuterService.java)

### 4. Executors Framework

   **Definition:** The Executors class is part of the Java concurrent package that simplifies thread pool creation and management. The ExecutorService interface provides lifecycle methods to manage tasks.

   **Common Executor Methods:**
newFixedThreadPool(int n): Creates a thread pool of fixed size.
newCachedThreadPool(): Creates a thread pool that creates new threads as needed.
newSingleThreadExecutor(): A single-threaded executor.
Example:
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            System.out.println("Task executed by: " + Thread.currentThread().getName());
        });
        executor.shutdown();
    }
}
```

### 5. Callable and Future

   **Definition:** The Callable interface is similar to Runnable but can return a result and throw checked exceptions. The Future interface is used to represent the result of an asynchronous computation.
  
 Example:

```
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class MyCallable implements Callable<Integer> {
    public Integer call() throws Exception {
        // Simulate computation
        Thread.sleep(2000);
        return 10; // Return a result
    }
}

public class CallableFutureExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<Integer> callableTask = new MyCallable();
        Future<Integer> future = executor.submit(callableTask);
        System.out.println("Result from callable: " + future.get());
        executor.shutdown();
    }
}
```

### 6. Synchronization

   **Definition:** Synchronization ensures that only one thread can access a resource at a time to prevent data inconsistency.
  
 **Synchronized Methods:**
   You can synchronize methods to restrict access to a single thread.
   Example:


### 7. ReentrantLock

   **Definition:** An alternative to synchronized blocks for more fine-grained control over locks.

   **Features:**
   Ability to try for a lock, time-out for a lock, and interrupt threads while waiting.
  
 **Example:** [JAVA CODE](multithreading/ReentrantLock.java)


### 8. Deadlock

   **Definition:** Deadlock is a situation where two or more threads are blocked forever, waiting for each other to release the lock.
   
**Avoiding Deadlock:**
   Use proper lock ordering.
   Use timeout in locks.

### 9. Volatile Keyword

   **Definition:** The volatile keyword is used to mark a variable as "stored in main memory," ensuring visibility of changes to variables across threads.