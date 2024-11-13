package printserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
  private boolean isRunning = false;
  private final Map<String, Printer> printers = new ConcurrentHashMap<>();
  private final Map<String, String> config = new ConcurrentHashMap<>();

  // Starts the print server
  public synchronized void start() {
    if (!isRunning) {
      isRunning = true;
      System.out.println("Print server started.");
    } else {
      System.out.println("Print server is already running.");
    }
  }

  // Stops the print server
  public synchronized void stop() {
    if (isRunning) {
      isRunning = false;
      System.out.println("Print server stopped.");
    } else {
      System.out.println("Print server is not running.");
    }
  }

  // Restarts the print server and clears all print queues
  public synchronized void restart() {
    stop();
    clearQueues();
    start();
  }

  // Prints file on the specified printer
  public void print(String filename, String printerName) {
    Printer printer = printers.get(printerName);
    if (printer != null && isRunning) {
      printer.addJob(filename);
      System.out.println("Added job for file " + filename + " to printer " + printerName);
    } else {
      System.out.println("Printer not found or server not running.");
    }
  }

  // Lists the print queue for the specified printer
  public void queue(String printerName) {
    Printer printer = printers.get(printerName);
    if (printer != null) {
      printer.showQueue();
    } else {
      System.out.println("Printer not found.");
    }
  }

  // Moves a job to the top of the print queue for a specified printer
  public void topQueue(String printerName, int jobNumber) {
    Printer printer = printers.get(printerName);
    if (printer != null) {
      printer.moveJobToTop(jobNumber);
    } else {
      System.out.println("Printer not found.");
    }
  }

  // Shows the status of a specified printer
  public void status(String printerName) {
    Printer printer = printers.get(printerName);
    if (printer != null) {
      printer.showStatus();
    } else {
      System.out.println("Printer not found.");
    }
  }

  // Reads the configuration value for a specified parameter
  public void readConfig(String parameter) {
    String value = config.get(parameter);
    if (value != null) {
      System.out.println("Config " + parameter + ": " + value);
    } else {
      System.out.println("Parameter not found.");
    }
  }

  // Sets the configuration value for a specified parameter
  public void setConfig(String parameter, String value) {
    config.put(parameter, value);
    System.out.println("Config " + parameter + " set to " + value);
  }

  // Helper method to clear all print queues
  private void clearQueues() {
    for (Printer printer : printers.values()) {
      printer.clearQueue();
    }
  }

  // Adds a printer to the server
  public void addPrinter(String name) {
    printers.put(name, new Printer(name));
  }
}

class Printer {
  private final String name;
  private final Queue<PrintJob> printQueue = new LinkedBlockingQueue<>();
  private int jobCounter = 1;

  public Printer(String name) {
    this.name = name;
  }

  // Adds a print job to the queue
  public void addJob(String filename) {
    printQueue.add(new PrintJob(jobCounter++, filename));
  }

  // Displays the current print queue
  public void showQueue() {
    if (printQueue.isEmpty()) {
      System.out.println("Print queue for printer " + name + " is empty.");
    } else {
      System.out.println("Queue for printer " + name + ":");
      for (PrintJob job : printQueue) {
        System.out.println(job.getJobNumber() + " " + job.getFilename());
      }
    }
  }

  // Moves a specific job to the top of the queue
  public void moveJobToTop(int jobNumber) {
    List<PrintJob> jobs = new ArrayList<>(printQueue);
    printQueue.clear();
    Optional<PrintJob> jobToMove = jobs.stream()
        .filter(job -> job.getJobNumber() == jobNumber)
        .findFirst();
    if (jobToMove.isPresent()) {
      printQueue.add(jobToMove.get());
      jobs.remove(jobToMove.get());
      printQueue.addAll(jobs);
      System.out.println("Job " + jobNumber + " moved to the top of the queue.");
    } else {
      System.out.println("Job " + jobNumber + " not found.");
    }
  }

  // Displays the printer status
  public void showStatus() {
    System.out.println("Printer " + name + " is " + (printQueue.isEmpty() ? "idle" : "printing"));
  }

  // Clears the print queue
  public void clearQueue() {
    printQueue.clear();
  }
}

class PrintJob {
  private final int jobNumber;
  private final String filename;

  public PrintJob(int jobNumber, String filename) {
    this.jobNumber = jobNumber;
    this.filename = filename;
  }

  public int getJobNumber() {
    return jobNumber;
  }

  public String getFilename() {
    return filename;
  }
}
