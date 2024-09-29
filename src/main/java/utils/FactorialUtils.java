package utils;

import java.math.BigInteger;

public final class Factorial {

  public static BigInteger factorial(int inner){
    if(inner == 1){
      return BigInteger.valueOf(1);
    }

    return BigInteger.valueOf(inner).multiply(factorial(inner-1))  ;
  }


 /* public static BigInteger factorial(int inner) {
    if (inner == 1) {
      return BigInteger.valueOf(1);
    }

    BigInteger result = BigInteger.ONE;

    return result.multiply(BigInteger.valueOf(inner -1));
  }*/

}
