package plexsupply.thread;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import plexsupply.dto.FactorialResult;

public class FactorialThread implements FinishedListener {

  private static final int MAX_CALCULATIONS_PER_SECOND = 100;
  private static volatile boolean readerFinished = false;
  private static volatile boolean writerFinished = false;

  private static final String END_LINE = UUID.randomUUID().toString();

  private final String pathInputFile;
  private final String pathOutputFile;
  private final int threadPool;

  public FactorialThread(
      String pathInputFile,
      String pathOutputFile,
      int threadPool
  ) {
    this.pathInputFile = pathInputFile;
    this.pathOutputFile = pathOutputFile;
    this.threadPool = threadPool;
  }

  public void calculateFactorial() {
    BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    BlockingQueue<FactorialResult> outputQueue
        = new PriorityBlockingQueue<>(100, Comparator.comparingInt(FactorialResult::getIndex));

    new Thread(new OutputThread(pathOutputFile, outputQueue, END_LINE, this)).start();
    new Thread(new InputThread(pathInputFile, inputQueue, END_LINE, this)).start();

    int maxCalculationsPerSecondPerThread = MAX_CALCULATIONS_PER_SECOND / threadPool;
    for (int i = 0; i < threadPool; i++) {
      new Thread(
          new ProcessingThread(
              inputQueue,
              outputQueue,
              END_LINE,
              maxCalculationsPerSecondPerThread,
              this
          )).start();
    }
  }

  @Override
  public void onReaderFinished() {
    readerFinished = true;
  }

  @Override
  public boolean isReaderFinished() {
    return readerFinished;
  }

  @Override
  public void onWriteRFinished() {
    writerFinished = true;
  }

  @Override
  public boolean isFinished() {

    return writerFinished;
  }

}
