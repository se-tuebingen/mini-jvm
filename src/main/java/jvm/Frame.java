package jvm;

public class Frame {

  // ----------------
  //   Instructions
  // ----------------
  final Instruction[] instructions;
  int pc;
  void next(int offset) { pc = pc + offset; }
  void next() { pc = pc + 1; }
  Instruction current() { return instructions[pc]; }

  // -------------
  //   Registers
  // -------------
  final int[] registers;

  int register(int index) { return registers[index]; }
  void register(int index, int value) { registers[index] = value; }

  // -----------------
  //   Operand Stack
  // -----------------
  final int[] operands;
  int nextOperand = -1;

  int popOperand() {
    if (nextOperand < 0) {
      throw new IllegalStateException("Operand stack underflow");
    }
    return operands[nextOperand--];
  }
  int peekOperand(int index) {
    if (nextOperand - index < 0) {
      throw new IllegalStateException("Operand stack underflow");
    }
    return operands[nextOperand - index];
  }
  void pushOperand(int value) {
    if (nextOperand >= operands.length - 1) {
      throw new IllegalStateException("Operand stack overflow");
    }
    operands[++nextOperand] = value;
  }
  boolean hasOperand() { return nextOperand >= 0; }

  public Frame(Instruction[] instructions, int maxLocals, int maxStack) {
    this.instructions = instructions;
    this.registers = new int[maxLocals];
    this.operands = new int[maxStack];
    this.pc = 0;
  }
}
