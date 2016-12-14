package demo.pluto.maven;

import java.util.*;

/**
 * @author adgar@google.com (Mike Edgar)
 */
public class ArrayToStringPositiveCases {

  public void intArray() {
    int[] a = {1, 2, 3};

    // BUG: Diagnostic contains: Arrays.toString(a)
    if (a.toString().isEmpty()) {
      System.out.println("int array string is empty!");
    } else {
      System.out.println("int array string is nonempty!");
    }
  }

  public void objectArray() {
    Object[] a = new Object[3];

    // BUG: Diagnostic contains: Arrays.toString(a)
    if (a.toString().isEmpty()) {
      System.out.println("object array string is empty!");
    } else {
      System.out.println("object array string is nonempty!");
    }
  }

  public void firstMethodCall() {
    String s = "hello";

    // BUG: Diagnostic contains: Arrays.toString(s.toCharArray())
    if (s.toCharArray().toString().isEmpty()) {
      System.out.println("char array string is empty!");
    } else {
      System.out.println("char array string is nonempty!");
    }
  }

  public void secondMethodCall() {
    char[] a = new char[3];

    // BUG: Diagnostic contains: Arrays.toString(a)
    if (a.toString().isEmpty()) {
      System.out.println("array string is empty!");
    } else {
      System.out.println("array string is nonempty!");
    }
  }
  
  public void throwable() {
    Exception e = new RuntimeException();
    // BUG: Diagnostic contains: Throwables.getStackTraceAsString(e)
    System.out.println(e.getStackTrace().toString());
  }
}