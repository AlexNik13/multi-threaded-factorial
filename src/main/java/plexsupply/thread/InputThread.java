package plexsupply.thread;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class InputThread implements Runnable {

  private static volatile int lineIndex = 0;

  private final String pathFile;
  private final BlockingQueue<String> inputQueue;
  private final String endLine;
  private final FinishedListener listener;

  public InputThread(
      String pathFile,
      BlockingQueue<String> inputQueue,
      String endLine,
      FinishedListener listener
  ) {
    this.pathFile = pathFile;
    this.inputQueue = inputQueue;
    this.endLine = endLine;
    this.listener = listener;
  }

  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
      String inputLine;
      try {
        while ((inputLine = reader.readLine()) != null) {
          inputQueue.put(lineIndex + " " + inputLine);
          lineIndex++;
        }
        inputQueue.put(lineIndex + " " + endLine);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      listener.onReaderFinished();
      try {
        reader.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
