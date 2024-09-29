package utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class FactorialTest {

  @Test
  void factorial() {
    Map<Integer, BigInteger> factorial = new HashMap<>();

    factorial.put(256, new BigInteger("857817775342842654119082271681232625157781520279485619859655650377269452553147589377440291360451408450375885342336584306157196834693696475322289288497426025679637332563368786442675207626794560187968867971521143307702077526646451464709187326100832876325702818980773671781454170250523018608495319068138257481070252817559459476987034665712738139286205234756808218860701203611083152093501947437109101726968262861606263662435022840944191408424615936000000000000000000000000000000000000000000000000000000000000000"));
    factorial.put(4, new BigInteger("24"));
    factorial.put(10, new BigInteger("3628800"));
    factorial.put(29, new BigInteger("8841761993739701954543616000000"));
    factorial.put(42, new BigInteger("1405006117752879898543142606244511569936384000000000"));
    factorial.put(198, new BigInteger("19815524305648002601818171204326257846611456725808373449616646563928629396065410626138298593265945324225558093942704493222553838950820027765375040827960551033001579328411138624055200727234232302046524227142061137986535960488148111891395081467526982493475408477527124840709196781511817187496273890924527108598562553359394406400000000000000000000000000000000000000000000000"));

    factorial.forEach(
        (input, expected) -> Assertions.assertEquals(expected, FactorialUtils.factorial(input)));
  }

}