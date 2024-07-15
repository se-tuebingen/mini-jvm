package jvm;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class VMMethodTests extends VMTests {


  Map<Integer, Class> classes = new HashMap<>();

  //  class Test {
  //    static int add(int x, int y) { return x + y; }
  //  }
  {
    var staticMethods = new HashMap<String, Method>();

    staticMethods.put("add",
      new Method("add", 2, 2, 3, new Instruction[] {
        new ILoad(0), // load x
        new ILoad(1), // load y
        new IAdd(),
        new Return()
      }));

    classes.put(0, new Class("Test", new HashMap<>(), staticMethods, new HashMap<>()));
  }

  //    class Adder {
  //       int sum;
  //       int total() { return sum; }
  //       void add(int num) { this.sum = this.sum + num; }
  //    }
  {
    Map<String, Method> instanceMethods = new HashMap<>();
    instanceMethods.put("total", new Method("total", 0, 1, 1, new Instruction[]{
      new ALoad(0), // load 'this'
      new GetField("sum"),
      new Return()
    }));
    instanceMethods.put("add", new Method("add", 1, 2, 2, new Instruction[]{
      new ALoad(0),
      new GetField("sum"),
      new ILoad(1), // load 'num'
      new IAdd(),                  // sum + num
      new ALoad(0), // load 'this'
      new Swap(),
      new PutField("sum"),
      new Return()
    }));

    Map<String, Integer> fields = new HashMap<>();
    fields.put("sum", 0);

    classes.put(1, new Class("Adder", fields,  new HashMap<>(), instanceMethods));
  }

  @Test
  void testSimpleFunctionCall() {
    Instruction[] instructions = {
      new IConst(4),  // [4, 1, 2]
      new IConst(1),  // [4, 1]
      new IConst(2),  // [4, 1, 2]
      new InvokeStatic(0, "add"), // [4, 3]
      new IAdd()            // [7]
    };

    VM vm = new VM(instructions, classes);
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    vm.step();
    vm.step();

    assertOperandStackEquals(vm, new int[] { 7 });
  }

  static void loopExample() {
     var arr = new int[] {13, 14, 15, 16};
     var sum = 0;
     for (int i = 0; ((arr.length - i) != 0); i = i + 1) {
       sum = sum + arr[i];
     }
  }

  @Test
  void testArraySum() {

    // instructions of loopExample above.
    Instruction[] instructions = {
      // L0
      new IConst(4),
      new NewArray(),

      new Dup(),
      new IConst(0),
      new IConst(13),
      new IAStore(),

      new Dup(),
      new IConst(1),
      new IConst(14),
      new IAStore(),

      new Dup(),
      new IConst(2),
      new IConst(15),
      new IAStore(),

      new Dup(),
      new IConst(3),
      new IConst(16),
      new IAStore(),
      new AStore(0),

      // L1
      new IConst(0),
      new IStore(1),
      // L2
      new IConst(0),
      new IStore(2),
      // L3
      new ALoad(0),
      new ArrayLength(),
      new ILoad(2),
      new ISub(),
      new IfEq(12), // L4
      // L5
      new ILoad(1),
      new ALoad(0),
      new ILoad(2),
      new IALoad(),
      new IAdd(),
      new IStore(1),
      // L6
      new ILoad(2),
      new IConst(1),
      new IAdd(),
      new IStore(2),
      new Goto(-15), // L3
      // L4
      new Return()
    };

    // Initialize the VM
    VM vm = new VM(instructions);

    // Execute the VM
    while (!vm.isDone()) {
      vm.step();
    }

    // Assert the final sum value
    assertOperandStackEquals(vm, new int[] {});
    assertRegisterEquals(vm, 1, 58);
  }

  @Test
  void testDynamicDispatch() {

    // var adder = new Adder()
    // adder.sum = 0;
    // adder.add(2);
    // adder.add(3);
    // adder.total();
    // return;
    Instruction[] instructions = {
      // var adder = new Adder()
      new New(1),
      new AStore(0),

      // adder.sum = 0;
      new ALoad(0),
      new IConst(0),
      new PutField("sum"),

      // adder.add(2);
      new ALoad(0),
      new IConst(2),
      new InvokeVirtual("add", 1),

      // adder.add(3);
      new ALoad(0),
      new IConst(3),
      new InvokeVirtual("add", 1),

      // adder.total();
      new ALoad(0),
      new InvokeVirtual("total", 0),

      // return;
      new Return()
    };

    // Initialize the VM
    VM vm = new VM(instructions, classes);

    // Execute the VM
    while (!vm.isDone()) {
      vm.step();
    }

    // Assert the final sum value on the operand stack
    assertOperandStackEquals(vm, new int[] { 5 });
  }
}
