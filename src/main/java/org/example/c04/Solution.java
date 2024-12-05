package org.example.c04;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.readAllLines;

public class Solution {

     private static final String[] TARGETS = {"XMAS", "SAMX"};

     public int findXMAS(String filePath) throws IOException {
          List<String> lines = readAllLines(Paths.get(filePath));
          int numRows = lines.size();
          int numCols = lines.get(0).length();
          char[][] grid = populateGrid(numRows, numCols, lines);

          int total = 0;

          for (int i = 0; i < numRows; i++) {
               for (int j = 0; j < numCols - 3; j++) {
                    String horizontal = "" + grid[i][j] + grid[i][j + 1] + grid[i][j + 2] + grid[i][j + 3];

                    for (String target : TARGETS) {
                         if (horizontal.equals(target) || new StringBuilder(horizontal).reverse().toString().equals(target)) {
                              total++;
                              break;
                         }
                    }
               }
          }

          for (int i = 0; i < numRows - 3; i++) {
               for (int j = 0; j < numCols; j++) {
                    String vertical = "" + grid[i][j] + grid[i + 1][j] + grid[i + 2][j] + grid[i + 3][j];
                    for (String target : TARGETS) {
                         if (vertical.equals(target)) {
                              total++;
                              break;
                         }
                    }
               }
          }

          for (int i = 0; i < numRows - 3; i++) {
               for (int j = 0; j < numCols - 3; j++) {
                    String diagonal = "" + grid[i][j] + grid[i + 1][j + 1] + grid[i + 2][j + 2] + grid[i + 3][j + 3];
                    for (String target : TARGETS) {
                         if (diagonal.equals(target)) {
                              total++;
                              break;
                         }
                    }
               }
          }

          for (int i = 3; i < numRows; i++) {
               for (int j = 0; j < numCols - 3; j++) {
                    String diagonal = "" + grid[i][j] + grid[i - 1][j + 1] + grid[i - 2][j + 2] + grid[i - 3][j + 3];
                    for (String target : TARGETS) {
                         if (diagonal.equals(target)) {
                              total++;
                              break;
                         }
                    }
               }
          }

          return total;
     }

     private static char[][] populateGrid(int numRows, int numCols, List<String> lines) {
          char[][] grid = new char[numRows][numCols];
          for (int i = 0; i < numRows; i++) {
               grid[i] = lines.get(i).toCharArray();
          }
          return grid;
     }

     public static void main(String[] args) {
          try {
               String filePath = args[0];
               Solution solution = new Solution();
               int result = solution.findXMAS(filePath);
               System.out.println("Total occurrences of XMAS and SMAX: " + result);
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}