package plexsupply.thread;

public interface FinishedListener {

  void onReaderFinished();

  boolean isReaderFinished();

  void onWriteRFinished();

  boolean isFinished();

}
