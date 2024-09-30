package plexsupply.thread;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

import plexsupply.dto.FactorialResult;
import plexsupply.utils.FactorialUtils;

public class ProcessingThread implements Runnable {

  private final BlockingQueue<String> inputQueue;
  private final BlockingQueue<FactorialResult> outputQueue;
  private final String endLine;
  private final FinishedListener listener;
  private final int maxCalculationsPerSecond;

  public ProcessingThread(
      BlockingQueue<String> inputQueue,
      BlockingQueue<FactorialResult> outputQueue,
      String endLine,
      int maxCalculationsPerSecond,
      FinishedListener listener
  ) {
    this.inputQueue = inputQueue;
    this.outputQueue = outputQueue;
    this.endLine = endLine;
    this.maxCalculationsPerSecond = maxCalculationsPerSecond;
    this.listener = listener;
  }

  @Override
  public void run() {
    int cauntThisSecond = 0;
    long lastCheckTime = System.currentTimeMillis();

    while (true) {
      try {
        if (inputQueue.isEmpty() && listener.isReaderFinished()) {
          break;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime >= 1000) {
          cauntThisSecond = 0;
          lastCheckTime = currentTime;
        }

        if (cauntThisSecond >= maxCalculationsPerSecond) {
          Thread.sleep(1000 - (currentTime - lastCheckTime));
          cauntThisSecond = 0;
          lastCheckTime = System.currentTimeMillis();
        }

        String inputLine = inputQueue.take();
        String[] parts = inputLine.split(" ", 2);
        int index = Integer.parseInt(parts[0]);
        String value = parts[1];

        try {
          int number = Integer.parseInt(value);
          BigInteger factorial = FactorialUtils.factorial(number);
          String outputLine = String.format("%d = %s", number, factorial);

          outputQueue.put(new FactorialResult(index, outputLine));
        } catch (NumberFormatException e) {
          if (value.equals(endLine)) {
            outputQueue.put(new FactorialResult(index, endLine));
          } else {
            outputQueue.put(new FactorialResult(index, value));
          }
        }

        cauntThisSecond++;
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

}