package jvm;

public class Heap {

  int[] memory;

  int nextFree = 0;

  // we use this special marker to identify addresses when debugging
  final int offset = 1337;

  Heap(int heapSize) {
    this.memory = new int[heapSize];
  }

  // Simple "bump" allocation, we never free memory.
  public int allocate(int n) {
    var address = nextFree;
    nextFree += n;
    return address + offset;
  }
  public int read(int index) { return memory[index - offset]; }
  public void write(int index, int value) { memory[index - offset] = value; }

}
