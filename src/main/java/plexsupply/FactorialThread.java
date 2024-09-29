package plexsupply;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import utils.FactorialUtils;

public class FactorialThread {


  private static volatile boolean readerFinished = false;
  private static volatile boolean writerFinished = false;

  private static final String END_LINE = "ChH92PU2KYkZUBR";

  private static final Semaphore rateLimiter = new Semaphore(100);


  private static volatile int calculation = 0;
  private static volatile int counter = 100;
  private static int threadPool = 5;

  public void calculateFactorial() throws IOException {



    String pathInputFile = "./date/input.txt";
    String pathOutputFile = "./date/output.txt";

    File file = new File(pathOutputFile);
    if (file.exists()) {
      file.delete();
    }

    BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();

    BufferedReader reader = new BufferedReader(new FileReader(pathInputFile));
    BufferedWriter writer = new BufferedWriter(new FileWriter(pathOutputFile));

    new Thread(new Input(reader, inputQueue) {
    }).start();
    new Thread(new Output(writer, outputQueue) {
    }).start();

    new Thread(new Processing(inputQueue, outputQueue) {
    }).start();

    /*for (int i = 0; i < threadPool; i++) {
      new Thread(new Processing(inputQueue, outputQueue) {
      }).start();
    }*/

/*    if (readerFinished) {
      reader.close();
      writer.close();
    }*/

    System.out.println("Exited.");
  }

  private class Input implements Runnable {

    private final BufferedReader reader;
    private final BlockingQueue<String> inputQueue;


    public Input(
        BufferedReader reader,
        BlockingQueue<String> inputQueue
    ) {
      this.reader = reader;
      this.inputQueue = inputQueue;
    }

    @Override
    public void run() {

      String inputLine;
      try {
        while ((inputLine = reader.readLine()) != null) {
          inputQueue.put(inputLine);
        }
        inputQueue.put(END_LINE);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      readerFinished = true;
      try {
        reader.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }


  private class Output implements Runnable {

    private final BufferedWriter writer;
    private final BlockingQueue<String> outputQueue;

    public Output(
        BufferedWriter writer,
        BlockingQueue<String> outputQueue
    ) {
      this.writer = writer;
      this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
      String line;
      List<String> outputLines = new ArrayList<>();

      while (true) {
        try {
          line = outputQueue.take();

          if (line.equals(END_LINE)) {

            for (int i = 0; i < outputLines.size(); i++) {
              try {
                if (i > 0) {
                  writer.newLine();
                }
                writer.write(outputLines.get(i));
              } catch (IOException e) {
                e.printStackTrace();
              }
            }

            writerFinished = true;
            writer.close();
            break;
          }

          outputLines.add(line);

        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }

  private class Processing implements Runnable {

    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<String> outputQueue;

    public Processing(BlockingQueue<String> inputQueue, BlockingQueue<String> outputQueue) {
      this.inputQueue = inputQueue;
      this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
      while (true) {

        if (counter / threadPool == calculation) {
          try {
            Thread.sleep(1000);
            System.out.println("sleep");
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }else {
          calculation++;
        }

        try {
          if (inputQueue.isEmpty() && readerFinished) {
            break;
          }

          String inputLine = inputQueue.take();

          try {
            int number = Integer.parseInt(inputLine);

            BigInteger factorial = FactorialUtils.factorial(number);

            String outputLine = String.format("%d = %s", number, factorial);

            outputQueue.put(outputLine);
          } catch (NumberFormatException e) {
            if (inputLine.equals(END_LINE)) {
              outputQueue.put(END_LINE);
            } else {
              outputQueue.put("");
            }
          }

        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }

  }

}
