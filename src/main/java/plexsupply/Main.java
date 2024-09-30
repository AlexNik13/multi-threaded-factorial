package plexsupply;


import java.util.Scanner;

import plexsupply.thread.FactorialThread;

public class Main {

  public static void main(String[] args) {
    String pathInputFile = "./date/input.txt";
    String pathOutputFile = "./date/output.txt";

    Scanner scanner = new Scanner(System.in);
    int threadPool;

    System.out.println(
        "Calculating factorials in multithreaded mode. Please set the number of threads (from 1 to 100). The task will be processed once you've entered the number."
    );

    while (true) {
      System.out.print("Entered the number: ");

      if (scanner.hasNextInt()) {
        threadPool = scanner.nextInt();

        if (threadPool > 0 && threadPool <= 100) {
          scanner.close();
          break;
        } else {
          System.out.println("The number must be greater than the range 1-100.");
        }
      } else {
        System.out.println("Error: Please enter a valid integer.");
        scanner.next();
      }
    }

    FactorialThread thread = new FactorialThread(
        pathInputFile,
        pathOutputFile,
        threadPool
    );

    thread.calculateFactorial();

    while (true) {
      boolean finished = thread.isFinished();
      if (finished) {
        System.out.println("Factorial calculation completed.");
        break;
      }
    }
  }

}
