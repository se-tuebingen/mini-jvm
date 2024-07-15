package jvm;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * We represent all values as integers using the following interpretation:
 *
 * - if the expected type is an integer, the integer value is the value itself
 * - if the expected type is a reference, the integer value is the heap address
 */
public class VM {

  // The current frame under evaluation
  Frame frame;

  // The callstack of frames other than the current
  final Stack<Frame> callStack;

  // The heap containing objects and arrays
  final Heap heap;

  // The class table
  final Map<Integer, Class> classes;

  public VM(Instruction[] main, Map<Integer, Class> classes) {
    this.frame = new Frame(main, 32, 32);
    this.classes = classes;
    this.callStack = new Stack<>();
    this.heap = new Heap(4096);
  }

  public VM(Instruction[] main) {
    this(main, new HashMap<>());
  }

  // ------------
  //   Stepping
  // ------------

  public boolean isDone() {
    return callStack.isEmpty() && frame.current() instanceof Return;
  }

  public void step() {
    Instruction instruction = frame.current();

    switch (instruction) {
      // Simple stack machine
      case IConst(int value) -> { frame.pushOperand(value); frame.next(); }
      case Pop() -> { frame.popOperand(); frame.next(); }
      case Dup() -> {
        var value = frame.popOperand();
        frame.pushOperand(value);
        frame.pushOperand(value);
        frame.next();
      }
      case Swap() -> {
        var first = frame.popOperand();
        var second = frame.popOperand();
        frame.pushOperand(first);
        frame.pushOperand(second);
        frame.next();
      }
      case IAdd() -> {
        var first = frame.popOperand();
        var second = frame.popOperand();
        frame.pushOperand(first + second);
        frame.next();
      }
      case ISub() -> {
        var first = frame.popOperand();
        var second = frame.popOperand();
        frame.pushOperand(second - first);
        frame.next();
      }
      case IMul() -> {
        var first = frame.popOperand();
        var second = frame.popOperand();
        frame.pushOperand(first * second);
        frame.next();
      }

      // Local control flow
      case IfEq(int offset) -> { if (frame.popOperand() == 0) frame.next(offset); else frame.next(); }
      case IfNe(int offset) -> { if (frame.popOperand() != 0) frame.next(offset); else frame.next(); }
      case Goto(int offset) -> { frame.next(offset); }

      // Simple register machine
      case ILoad(int index) -> { frame.pushOperand(frame.register(index)); frame.next(); }
      case IStore(int index) -> { frame.register(index, frame.popOperand()); frame.next(); }
      case ALoad(int index) -> { frame.pushOperand(frame.register(index)); frame.next(); }
      case AStore(int index) -> { frame.register(index, frame.popOperand()); frame.next(); }

      // Static methods
      case InvokeStatic(Integer classId, String methodName) -> {
        var cls = classes.get(classId);
        var method = cls.staticMethods.get(methodName);

        var caller = frame;
        var callee = new Frame(method.instructions, method.maxLocals, method.maxStack);

        // transfer arguments
        for (int i = method.parameters - 1; i >= 0; i--) {
          callee.register(i, caller.popOperand());
        }

        // push caller frame onto the stack
        caller.next();
        callStack.push(caller);
        frame = callee;
      }
      case Return() -> {
        var caller = callStack.pop();
        var callee = frame;

        frame = caller;
        while (callee.hasOperand()) { caller.pushOperand(callee.popOperand()); }
      }

      // Arrays
      case NewArray() -> {
        var length = frame.popOperand();

        // We store arrays with the following layout in memory:
        //
        //     +--------+---------+---------+-------+
        //     | length | index 0 | index 1 | ...   |
        //     +--------+---------+---------+-------+
        var elementSize = 1;
        var address = heap.allocate(1 + length * elementSize);
        heap.write(address, length);
        frame.pushOperand(address);
        frame.next();
      }
      case IALoad() -> {
        var index = frame.popOperand();
        var address = frame.popOperand();
        frame.pushOperand(heap.read(address + 1 + index));
        frame.next();
      }
      case IAStore() -> {
        var value = frame.popOperand();
        var index = frame.popOperand();
        var address = frame.popOperand();
        heap.write(address + 1 + index, value);
        frame.next();
      }
      case ArrayLength() -> {
        frame.pushOperand(heap.read(frame.popOperand()));
        frame.next();
      }

      // Objects
      case New(Integer classId) -> {
        var cls = classes.get(classId);

        // We store objects with the following layout in memory:
        //
        //     +-------+--------+---------+-------+
        //     | class | field 0 | field 1  | ...   |
        //     +-------+--------+---------+-------+
        var numberOfFields = cls.fields.size();
        var fieldSize = 1;
        var address = heap.allocate(numberOfFields * fieldSize + 1);
        heap.write(address, classId);
        frame.pushOperand(address);
        frame.next();
      }
      case GetField(String fieldName) -> {
        var address = frame.popOperand();
        var classId = heap.read(address);
        var cls = classes.get(classId);
        var fieldIndex = cls.fields.get(fieldName);
        frame.pushOperand(heap.read(address + 1 + fieldIndex));
        frame.next();
      }
      case PutField(String fieldName) -> {
        var value = frame.popOperand();
        var address = frame.popOperand();
        var classId = heap.read(address);
        var cls = classes.get(classId);
        var fieldIndex = cls.fields.get(fieldName);
        heap.write(address + 1 + fieldIndex, value);
        frame.next();
      }
      case InvokeVirtual(String methodName, int numberOfArguments) -> {
        // ..,  objectref, [arg1, [arg2, ..., argn]]
        var address = frame.peekOperand(numberOfArguments);
        var classId = heap.read(address);
        var cls = classes.get(classId);
        var method = cls.instanceMethods.get(methodName);

        var caller = frame;
        var callee = new Frame(method.instructions, method.maxLocals, method.maxStack);

        assert(numberOfArguments == method.parameters);

        // transfer arguments
        for (int i = method.parameters; i > 0; i--) {
          callee.register(i, caller.popOperand());
        }
        // `this` is the first local
        callee.register(0, caller.popOperand());

        // push caller frame onto the stack
        caller.next();
        callStack.push(caller);
        frame = callee;
      }

      default -> throw new IllegalStateException("Unexpected value: " + instruction);
    }
  }
}
