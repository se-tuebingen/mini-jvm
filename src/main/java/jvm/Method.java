package jvm;

public class Method {

  final String methodName;
  final int parameters;
  final int maxLocals;
  final int maxStack;
  final Instruction[] instructions;

  public Method(String methodName, int parameters, int maxLocals, int maxStack, Instruction[] instructions) {
    this.methodName = methodName;
    this.parameters = parameters;
    this.maxLocals = maxLocals;
    this.maxStack = maxStack;
    this.instructions = instructions;
  }
}
