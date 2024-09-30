package plexsupply.dto;

public class FactorialResult {

  private final int index;
  private final String result;

  public FactorialResult(int index, String result) {
    this.index = index;
    this.result = result;
  }

  public int getIndex() {
    return index;
  }

  public String getResult() {
    return result;
  }

}
