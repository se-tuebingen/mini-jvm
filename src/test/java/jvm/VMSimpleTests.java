package jvm;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VMSimpleTests extends VMTests {

  @Test
  void testIConst() {
    Instruction[] instructions = { new IConst(42) };
    VM vm = new VM(instructions);
    vm.step();
    assertOperandStackEquals(vm, new int[] { 42 });
  }

  @Test
  void testSimpleStackMachine() {
    Instruction[] instructions = {
      new IConst(1),  // [1]
      new IConst(2),  // [1, 2]
      new IAdd(),           // [3]
      new IConst(4),  // [3, 4]
      new Dup(),            // [3, 4, 4]
      new IMul(),           // [3, 16]
      new ISub()            // [-13]
    };
    VM vm = new VM(instructions);
    vm.step();
    vm.step();
    vm.step();
    assertOperandStackEquals(vm, new int[] { 3 });

    vm.step();
    vm.step();
    vm.step();
    vm.step();
    assertOperandStackEquals(vm, new int[] { -13 });
  }

  @Test
  void testSimpleRegisterMachine() {
    Instruction[] instructions = {
      new IConst(1),  // [1]
      new IConst(2),  // [1, 2]
      new IStore(0),  // [1]
      new IStore(1),  // []
      new ILoad(0),   // [2]
      new ILoad(1),   // [2, 1]
      new ISub()            // [1]
    };
    VM vm = new VM(instructions);
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    assertOperandStackEquals(vm, new int[] {});
    assertRegistersEqual(vm, new int[]{ 2, 1 });

    vm.step();
    vm.step();
    vm.step();
    assertOperandStackEquals(vm, new int[] { 1 });
    assertRegistersEqual(vm, new int[]{ 2, 1 });
  }
}
