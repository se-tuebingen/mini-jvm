package jvm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VMTests {

  // Helper method to assert the operand stack contents
  void assertOperandStackEquals(VM vm, int[] expected) {
    int[] stack = vm.frame.operands;
    int stackSize = vm.frame.nextOperand + 1;
    assertEquals(expected.length, stackSize, "Operand stack size does not match");

    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], stack[i], "Operand stack element at index " + i + " does not match");
    }
  }

  // Helper method to assert the register contents
  void assertRegistersEqual(VM vm, int[] expected) {
    int[] registers = vm.frame.registers;
    assertTrue(registers.length >= expected.length, "Register array size is smaller than expected prefix length");

    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], registers[i], "Register value at index " + i + " does not match");
    }
  }
  void assertRegisterEquals(VM vm, int index, int value) {
    int[] registers = vm.frame.registers;
    assertTrue(registers.length > index, "Register array size is smaller than expected index");
    assertEquals(value, registers[index], "Register value at index " + index + " does not match");
  }
}
