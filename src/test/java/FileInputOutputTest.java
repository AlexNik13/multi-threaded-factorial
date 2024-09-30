import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileInputOutputTest {

  @Test
  void inputEqualsOutputTest() {
    String pathInputFile = "./date/input.txt";
    String pathOutputFile = "./date/output.txt";

    try (
        BufferedReader inputReader = new BufferedReader(new FileReader(pathInputFile));
        BufferedReader outputReader = new BufferedReader(new FileReader(pathOutputFile))
    ) {
      List<String> input = inputReader.lines().collect(Collectors.toList());

      List<String> output = outputReader.lines()
          .map(line -> line.split(" = ")[0])
          .collect(Collectors.toList());

      Assertions.assertEquals(input, output);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
