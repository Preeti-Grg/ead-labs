import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterService {
    private static volatile PrinterService instance;
    private final Lock hardwareLock = new ReentrantLock();
    private boolean isOnline = false;

    private PrinterService() {
        initializeHardware();
    }

    private void initializeHardware() {
        System.out.println("[Printer] Connecting to printer...");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isOnline = true;
        System.out.println("[Printer] Ready");
    }

    public static PrinterService getInstance() {
        PrinterService result = instance;
        if (result == null) {
            synchronized (PrinterService.class) {
                result = instance;
                if (result == null) {
                    instance = result = new PrinterService();
                }
            }
        }
        return result;
    }

    public void printDocument(String document) {
        hardwareLock.lock();
        try {
            if (!isOnline) {
                System.out.println("[Printer] Error: Printer offline");
                return;
            }
            
            System.out.println("[Printer] Processing: " + document);
            // Simulate printing time
            Thread.sleep(500);
            System.out.println("[Printer] Completed: " + document);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            hardwareLock.unlock();
        }
    }

    public void shutdown() {
        hardwareLock.lock();
        try {
            System.out.println("[Printer] Shutting down...");
            isOnline = false;
        } finally {
            hardwareLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrinterService printer = PrinterService.getInstance();
        
        new Thread(() -> printer.printDocument("Resume.pdf")).start();
        new Thread(() -> printer.printDocument("Contract.docx")).start();

        Thread.sleep(2000); // Wait for prints to complete
        printer.shutdown();
        
        // Try printing after shutdown
        printer.printDocument("LateDocument.txt");
    }
}