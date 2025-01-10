package org.example.c02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.abs;

public class Solution {

     private int countSafe = 0;


     // preprocess the file
     private int[][] transformFile(String fileName) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(fileName));

          String line;
          int[][] file = new int[1000][];
          int fileIdx = 0;
          while ((line = reader.readLine()) != null) {
               String[] arr = line.trim().split("\\s+");
               int[] lineArray = new int[arr.length];
               for (int i = 0; i < arr.length; i++) {
                    lineArray[i] = Integer.parseInt(arr[i]);
               }
               file[fileIdx] = lineArray;
               fileIdx++;
          }

          reader.close();

          return file;
     }

     // This method will be responsible for determining the safety of the levels
     public String levelIsSafe(int[] level) {
          int badCount = 0;
          int badIndex = -1;

          // Checking the differences between adjacent levels
          for (int i = 1; i < level.length; i++) {
               int diff = level[i] - level[i - 1];

               // If the difference is 0 or greater than 3, it's considered bad
               if (abs(diff) < 1 || abs(diff) > 3) {
                    badCount++;
                    badIndex = i;
                    if (badCount > 1) {
                         return "Unsafe regardless of which level is removed.";
                    }
               }
          }

          //safe
          if (badCount == 0) {
               return "Safe without removing any level.";
          }

          // one bad diff; investigate further
          int[] newLevel = new int[level.length - 1];
          for (int i = 0, j = 0; i < level.length; i++) {
               if (i != badIndex) {
                    newLevel[j] = level[i];
                    j++;
               }
          }

          if (isLevelSafe(newLevel)) {
               return "Safe by removing the level, " + level[badIndex] + ".";
          }

          // unsafe
          return "Unsafe regardless of which level is removed.";
     }

     // determine if a level is safe according to the parameters of fthe pizzle
     private boolean isLevelSafe(int[] level) {
          for (int i = 1; i < level.length; i++) {
               int diff = level[i] - level[i - 1];
               if (abs(diff) < 1 || abs(diff) > 3) {
                    return false;
               }
          }
          return true;
     }

     // look through the file for safe levels
     public int readThroughFile(int[][] file) {
          for (int[] ints : file) {
               String result = levelIsSafe(ints);
               System.out.println(Arrays.toString(ints) + ": " + result);

               if (result.startsWith("Safe without removing any level") ||
                       result.startsWith("Safe by removing the level")) {
                    countSafe++;
               }
          }
          return countSafe;
     }

     public static void main(String[] args) throws IOException {
          Solution solution = new Solution();
          int[][] fileArray = solution.transformFile(args[0]);
          int safeLevels = solution.readThroughFile(fileArray);
          System.out.println("safeLevels = " + safeLevels);
     }
}
