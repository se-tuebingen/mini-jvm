# Didactic JVM Implementation

This project implements a simplified Java Virtual Machine (JVM) in Java. 
The custom JVM can execute a subset of Java bytecode instructions, supporting basic operations 
such as arithmetic, control flow, and method invocations, as well as handling arrays and objects.

## Limitations

- Only `int` and reference types are supported
- No constructors, so objects currently can only be constructed and need to be initialized manually since
- No garbage collection
- Only integer arrays
- No inheritance

## Overview

The custom JVM represents all values as integers, using the following interpretation:
- If the expected type is an integer, the integer value is the value itself.
- If the expected type is a reference, the integer value is the heap address.

The VM consists of:
- A **frame** representing the current execution context.
- A **call stack** of frames for managing method invocations.
- A **heap** for storing objects and arrays.
- A **class table** mapping class IDs to class definitions.

## Instruction Set

The instruction set includes:

### Simple Stack Machine Instructions
- **IConst(int value)**: Push an int constant onto the operand stack.
- **IAdd()**: Add two integers.
- **ISub()**: Subtract two integers.
- **IMul()**: Multiply two integers.
- **Pop()**: Pop the top value from the operand stack.
- **Dup()**: Duplicate the top value on the operand stack.
- **Swap()**: Swap the two top elements on the operand stack.

### Local Control Flow
- **IfEq(int offset)**: Branch if int equals zero.
- **IfNe(int offset)**: Branch if int not equal to zero.
- **Goto(int offset)**: Unconditional branch.

### Simple Register Machine
- **ILoad(int index)**: Load an int value from a local variable.
- **IStore(int index)**: Store an int value into a local variable.
- **ALoad(int index)**: Load a reference from a local variable.
- **AStore(int index)**: Store a reference into a local variable.

### Static Methods
- **InvokeStatic(Integer classId, String methodName)**: Invoke a static method.
- **Return()**: Return from a method.

### Arrays
- **NewArray()**: Create a new array of int.
- **IALoad()**: Load an int from an array.
- **IAStore()**: Store an int into an array.
- **ArrayLength()**: Get the length of an array.

### Objects
- **New(Integer classId)**: Create a new object.
- **InvokeVirtual(String methodName, int numberOfArguments)**: Invoke an instance method.
- **GetField(String fieldName)**: Fetch field from object.
- **PutField(String fieldName)**: Set field in object.

## Example

Consider the following Java code:

```java
var sum = 0;
sum = sum + 5;
sum = sum + 3;
```

This code can be represented using the instruction set as:

```java
Instruction[] instructions = {
  new IConst(0),
  new IStore(0),
  new ILoad(0),
  new IConst(5),
  new IAdd(),
  new IStore(0),
  new ILoad(0),
  new IConst(3),
  new IAdd(),
  new IStore(0),
  new Return()
};
```
## Potential Extensions

Students can extend the custom JVM in various ways, including but not limited to:

1. **More Simple Instructions**:
   - Implement more bytecode instructions such as `if_icmpge`.

2. **Garbage Collection (GC)**:
   - Implement a simple garbage collector, such as mark-and-sweep, which will require changes to the memory layout.

3. **Additional Types**:
   - Support other types. Be aware that types like `long` and `double` will require two registers and two slots on the heap.

4. **Constructors**:
   - Implement support for constructors to initialize objects.

5. **Inheritance**:
   - Add support for inheritance, allowing class hierarchies and method overriding.

6. **Translation of Actual Bytecode**:
   - Implement a translation layer to convert actual Java bytecode into the internal representation of the custom JVM (a challenging task).

7. **Exception Handling**:
   - Add support for exception handling using instructions like `try`, `catch`, `throw`.

...

