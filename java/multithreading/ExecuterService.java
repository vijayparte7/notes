import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerThread implements Runnable {
    private String message;
    public WorkerThread(String s) {
        this.message = s;
    }
    public void run() {
        System.out.println(Thread.currentThread().getName() + " (Start) message = " + message);
// Processing (simulate by sleeping)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " (End)");
    }
}

public class ThreadPoolExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3); // Create a pool of 3 threads
        for (int i = 0; i < 5; i++) {
            Runnable worker = new WorkerThread("" + i);
            executor.execute(worker); // Submit tasks for execution
        }
        executor.shutdown(); // Stop accepting new tasks
    }
}