package plexsupply.utils;

import java.math.BigInteger;

public final class FactorialUtils {

  public static BigInteger factorial(int number){
    if (number <= 0) {
      return BigInteger.ZERO;
    }

    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= number; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }

}
