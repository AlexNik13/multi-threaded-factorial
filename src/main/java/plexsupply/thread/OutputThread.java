package plexsupply.thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import plexsupply.dto.FactorialResult;


public class OutputThread implements Runnable {

  private final String pathFile;
  private final BlockingQueue<FactorialResult> outputQueue;
  private final String endLine;
  public final FinishedListener listener;

  public OutputThread(
      String pathFile,
      BlockingQueue<FactorialResult> outputQueue,
      String endLine,
      FinishedListener listener
  ) {
    this.pathFile = pathFile;
    this.outputQueue = outputQueue;
    this.endLine = endLine;
    this.listener = listener;
  }

  @Override
  public void run() {
    List<FactorialResult> buffer = new ArrayList<>();
    int expectedIndex = 0;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {
      while (true) {
        try {
          FactorialResult factorialResult = outputQueue.take();

          if (factorialResult.getResult().equals(endLine)) {
            for (FactorialResult r : buffer) {
              writer.write(r.getResult());
              writer.newLine();
            }

            writer.close();
            listener.onWriteRFinished();
            break;
          }

          buffer.add(factorialResult);
          buffer.sort(Comparator.comparingInt(FactorialResult::getIndex));

          while (!buffer.isEmpty() && buffer.get(0).getIndex() == expectedIndex) {
            writer.write(buffer.get(0).getResult());
            writer.newLine();
            buffer.remove(0);
            expectedIndex++;
          }

        } catch (InterruptedException | IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
