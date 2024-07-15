package jvm;

/**
 * A selection of JVM instructions
 */
public sealed interface Instruction permits
  // Simple stack machine
  IConst,         // Push an int constant onto the operand stack.
  IAdd,           // Add two integers.
  ISub,           // Subtract two integers.
  IMul,           // Multiply two integers.

  Pop,            // Pop the top value from the operand stack.
  Dup,            // Duplicate the top value on the operand stack.
  Swap,           // Swaps the two top elements on the operand stack.

  // Local control flow
  IfEq,           // Branch if int equals zero.
  IfNe,           // Branch if int not equal to zero.
  Goto,           // Unconditional branch.

  // Simple register machine
  ILoad,          // Load an int value from a local variable.
  IStore,         // Store an int value into a local variable.
  ALoad,          // Load a reference from a local variable.
  AStore,         // Store a reference into a local variable.

  // Static method calls
  InvokeStatic,   // Invoke a static method.
  Return,         // Return from a method.

  // Arrays
  NewArray,       // Create a new array of int.
  IALoad,         // Load an int from an array.
  IAStore,        // Store an int into an array.
  ArrayLength,     // Get the length of an array.

  // Objects
  New,            // Create a new object.
  InvokeVirtual,  // Invoke an instance method.
  GetField,       // Fetch field from object.
  PutField       // Set field in object.

  {}


/**
 * Push an int constant onto the operand stack.
 *
 * <p>
 * The value is an immediate byte, which is sign-extended to an int and pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>iconst value</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .. ->
 *   .., value
 * </pre>
 */
record IConst(int value) implements Instruction {}

/**
 * Load an int value from a local variable.
 *
 * <p>
 * The index is an unsigned byte that must be an index into the local variable array of the current frame.
 * The local variable at index must contain an int. The value of the local variable at index is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>iload index</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .. ->
 *   .., value
 * </pre>
 */
record ILoad(int index) implements Instruction {}

/**
 * Store an int value into a local variable.
 *
 * <p>
 * The index is an unsigned byte that must be an index into the local variable array of the current frame.
 * The value on the top of the operand stack must be of type int. It is popped from the operand stack and the value is stored in the local variable at index.
 *
 * <p>
 * <b>Format</b>: <code>istore index</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   ..
 * </pre>
 */
record IStore(int index) implements Instruction {}

/**
 * Add two integers.
 *
 * <p>
 * Both value1 and value2 must be of type int. They are popped from the operand stack.
 * The int result is value1 + value2. The result is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>iadd</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value1, value2 ->
 *   .., result
 * </pre>
 */
record IAdd() implements Instruction {}

/**
 * Subtract two integers.
 *
 * <p>
 * Both value1 and value2 must be of type int. They are popped from the operand stack.
 * The int result is value1 - value2. The result is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>isub</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value1, value2 ->
 *   .., result
 * </pre>
 */
record ISub() implements Instruction {}

/**
 * Multiply two integers.
 *
 * <p>
 * Both value1 and value2 must be of type int. They are popped from the operand stack.
 * The int result is value1 * value2. The result is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>imul</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value1, value2 ->
 *   .., result
 * </pre>
 */
record IMul() implements Instruction {}

/**
 * Create a new object.
 *
 * <p>
 * The classId must be a valid class reference. It is used to allocate a new object of that class.
 * A reference to the newly created object is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>new classId</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .. ->
 *   .., objectref
 * </pre>
 */
record New(Integer classId) implements Instruction {}


/**
 * Fetch field from object.
 *
 * <p>
 * The objectref must be of type reference and must refer to an object. The field name specifies
 * the field to be fetched. The value of the field is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>getfield fieldName</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., objectref ->
 *   .., value
 * </pre>
 */
record GetField(String fieldName) implements Instruction {}

/**
 * Set field in object.
 *
 * <p>
 * The objectref must be of type reference and must refer to an object. The field name specifies
 * the field to be set. The value is popped from the operand stack and assigned to the specified field.
 *
 * <p>
 * <b>Format</b>: <code>putfield fieldName</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., objectref, value ->
 *   ..
 * </pre>
 */
record PutField(String fieldName) implements Instruction {}

/**
 * Load a reference from a local variable.
 *
 * <p>
 * The index is an unsigned byte that must be an index into the local variable array of the current frame.
 * The local variable at index must contain a reference. The objectref is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>aload index</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .. ->
 *   .., objectref
 * </pre>
 */
record ALoad(int index) implements Instruction {}

/**
 * Store a reference into a local variable.
 *
 * <p>
 * The index is an unsigned byte that must be an index into the local variable array of the current frame.
 * The objectref on the top of the operand stack must be of type reference. It is popped from the operand stack and the value is stored in the local variable at index.
 *
 * <p>
 * <b>Format</b>: <code>astore index</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., objectref ->
 *   ..
 * </pre>
 */
record AStore(int index) implements Instruction {}

/**
 * Invoke a static method.
 *
 * <p>
 * The methodName must be a valid method signature. The arguments for the method are popped from the operand stack.
 * The result, if any, is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>invokestatic classId methodName</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., [arg1, [arg2, ...]] ->
 *   .., [result]
 * </pre>
 */
record InvokeStatic(Integer classId, String methodName) implements Instruction {}

/**
 * Invoke an instance method.
 *
 * <p>
 * The methodName must be a valid method signature. The objectref and arguments for the method are popped from the operand stack.
 * The result, if any, is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>invokevirtual methodName numberOfArguments</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., objectref, [arg1, [arg2, ...]] ->
 *   .., [result]
 * </pre>
 */
record InvokeVirtual(String methodName, int numberOfArguments) implements Instruction {}

/**
 * Return from a method.
 *
 * <p>
 * The top value on the operand stack must be of the appropriate type for the method's return type.
 * It is popped from the operand stack and used as the return value.
 *
 * <p>
 * <b>Format</b>: <code>return</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   [empty]
 * </pre>
 */
record Return() implements Instruction {}

/**
 * Pop the top value from the operand stack.
 *
 * <p>
 * The top value on the operand stack must be of type int, float, or reference. It is popped from the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>pop</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   ..
 * </pre>
 */
record Pop() implements Instruction {}

/**
 * Duplicate the top value on the operand stack.
 *
 * <p>
 * The top value on the operand stack must be of type int, float, or reference. It is duplicated and both copies are pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>dup</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   .., value, value
 * </pre>
 */
record Dup() implements Instruction {}

/**
 * Swap the top two values on the operand stack.
 *
 * <p>
 * The top two values on the operand stack must be of type int, float, or reference.
 * They are swapped.
 *
 * <p>
 * <b>Format</b>: <code>swap</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value1, value2 ->
 *   .., value2, value1
 * </pre>
 */
record Swap() implements Instruction {}

/**
 * Branch if int equals zero.
 *
 * <p>
 * The value must be of type int. It is popped from the operand stack.
 * If the value is zero, the signed offset is used to calculate the address of the instruction to branch to.
 *
 * <p>
 * <b>Format</b>: <code>ifeq offset</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   ..
 * </pre>
 */
record IfEq(int offset) implements Instruction {}

/**
 * Branch if int not equal to zero.
 *
 * <p>
 * The value must be of type int. It is popped from the operand stack.
 * If the value is not zero, the signed offset is used to calculate the address of the instruction to branch to.
 *
 * <p>
 * <b>Format</b>: <code>ifne offset</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., value ->
 *   ..
 * </pre>
 */
record IfNe(int offset) implements Instruction {}

/**
 * Unconditional jump.
 *
 * <p>
 * The unsigned byte offset is used to calculate the address of the instruction to branch to.
 *
 * <p>
 * <b>Format</b>: <code>goto offset</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .. ->
 *   ..
 * </pre>
 */
record Goto(int offset) implements Instruction {}


/**
 * Create a new array of int.
 *
 * <p>
 * The size must be of type int. It is popped from the operand stack.
 * A new array of int of the given size is allocated and a reference arrayref to it is pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>newarray</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., length ->
 *   .., arrayref
 * </pre>
 */
record NewArray() implements Instruction {}

/**
 * Load an int from an array.
 *
 * <p>
 * The arrayref must be of type reference and must refer to an array whose components are of type int.
 * The index must be of type int. Both arrayref and index are popped from the operand stack.
 * The int value in the component of the array at index is retrieved and pushed onto the operand stack.
 *
 * <p>
 * <b>Format</b>: <code>iaload</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., arrayref, index ->
 *   .., value
 * </pre>
 */
record IALoad() implements Instruction {}

/**
 * Store an int into an array.
 *
 * <p>
 * The arrayref must be of type reference and must refer to an array whose components are of type int.
 * The index and value must both be of type int. The arrayref, index, and value are popped from the operand stack.
 * The int value is stored as the component of the array at index.
 *
 * <p>
 * <b>Format</b>: <code>iastore</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., arrayref, index, value ->
 *   ..
 * </pre>
 */
record IAStore() implements Instruction {}

/**
 * Get the length of an array.
 *
 * <p>
 * The arrayref must be of type reference and must refer to an array.
 * It is popped from the operand stack.
 * The length of the array it references is determined.
 * That length is pushed onto the operand stack as an int.
 *
 * <p>
 * <b>Format</b>: <code>arraylength</code><br>
 * <b>Operand Stack</b>:
 * <pre>
 *   .., arrayref ->
 *   .., length
 * </pre>
 */
record ArrayLength() implements Instruction {}
