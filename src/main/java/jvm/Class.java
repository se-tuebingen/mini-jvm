package jvm;

import java.util.Map;

public class Class {

  final String className;
  final Map<String, Method> staticMethods;
  final Map<String, Integer> fields;
  final Map<String, Method> instanceMethods;

  public Class(String className, Map<String, Integer> fields, Map<String, Method> staticMethods, Map<String, Method> instanceMethods) {
    this.className = className;
    this.fields = fields;
    this.staticMethods = staticMethods;
    this.instanceMethods = instanceMethods;
  }
}
