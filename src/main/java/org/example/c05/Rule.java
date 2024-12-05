package org.example.c05;

public class Rule {
     int first;
     int second;

     public Rule(int first, int second) {
          this.first = first;
          this.second = second;
     }

     public int getFirst() {
          return first;
     }

     public void setFirst(int first) {
          this.first = first;
     }

     public int getSecond() {
          return second;
     }

     public void setSecond(int second) {
          this.second = second;
     }

     public String toString() {
          return "{first: " + this.first + "; second: " + this.second + "}";
     }
}

