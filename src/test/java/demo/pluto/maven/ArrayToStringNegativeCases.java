package demo.pluto.maven;

import java.util.*;

/**
 * @author adgar@google.com (Mike Edgar)
 */
public class ArrayToStringNegativeCases {
  public void objectEquals() {
    Object a = new Object();

    if (a.toString().isEmpty()) {
      System.out.println("string is empty!");
    } else {
      System.out.println("string is not empty!");
    }
  }
}