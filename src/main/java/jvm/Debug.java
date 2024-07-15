package jvm;

import java.util.Stack;

public class Debug {
  private static final int WIDTH = 60;

  public static void printVMState(VM vm, int stepCount) {
    Frame frame = vm.frame;
    Stack<Frame> callStack = vm.callStack;
    Heap heap = vm.heap;

    System.out.println("┌" + "─".repeat(WIDTH - 2) + "┐");

    // Step count and current instruction
    System.out.printf("│ Step: %-5d  Instruction: %-" + (WIDTH - 29) + "s│\n", stepCount, frame.current());

    // Program counter and call stack depth
    System.out.printf("│ PC: %-6d  Call Stack Depth: %-" + (WIDTH - 33) + "d│\n", frame.pc, callStack.size() + 1);

    // Operand Stack
    System.out.println("├" + "─".repeat(WIDTH - 2) + "┤");
    System.out.println("│ Operand Stack (top to bottom):" + " ".repeat(WIDTH - 33) + "│");
    if (frame.nextOperand >= 0) {
      for (int i = frame.nextOperand; i >= 0; i--) {
        System.out.printf("│  [%d] %-" + (WIDTH - 8) + "d│\n", i, frame.operands[i]);
      }
    } else {
      System.out.println("│  (empty)" + " ".repeat(WIDTH - 11) + "│");
    }

    // Local Variables
    System.out.println("├" + "─".repeat(WIDTH - 2) + "┤");
    System.out.println("│ Local Variables:" + " ".repeat(WIDTH - 19) + "│");
    for (int i = 0; i < frame.registers.length; i++) {
      System.out.printf("│  [%d] %-" + (WIDTH - 8) + "d│\n", i, frame.registers[i]);
    }

    // Heap
    System.out.println("├" + "─".repeat(WIDTH - 2) + "┤");
    System.out.println("│ Heap:" + " ".repeat(WIDTH - 8) + "│");
    for (int i = 0; i < heap.nextFree; i++) {
      System.out.printf("│  [%d] %-" + (WIDTH - 11) + "d│\n", i + heap.offset, heap.read(i + heap.offset));
    }

    System.out.println("└" + "─".repeat(WIDTH - 2) + "┘");
  }
}
