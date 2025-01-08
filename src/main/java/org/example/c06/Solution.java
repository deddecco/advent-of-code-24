package org.example.c06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {

     private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
     private static final char[] DIRECTION_CHARS = {'^', '>', 'v', '<'};
     private int rows, cols;

     public static void main(String[] args) throws IOException {
          if (args.length < 1) {
               System.out.println("Usage: java Solution <filename>");
               return;
          }

          char[][] grid = readFile(args[0]);
          Solution solution = new Solution();

          int visitedCount = solution.predictPatrolPath(grid);

          display(grid);
          System.out.println("visitedCount = " + visitedCount);
     }

     private static void display(char[][] grid) {
          for (char[] row : grid) {
               System.out.println(new String(row));
          }
     }

     public static char[][] readFile(String fileName) throws IOException {
          List<char[]> rows = new ArrayList<>();

          try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
               String line;
               while ((line = reader.readLine()) != null) {
                    rows.add(line.toCharArray());
               }
          }

          char[][] result = new char[rows.size()][];
          for (int i = 0; i < rows.size(); i++) {
               result[i] = rows.get(i);
          }

          return result;
     }

     public int predictPatrolPath(char[][] grid) {
          rows = grid.length;
          cols = grid[0].length;

          int[] guardPosition = findGuard(grid);
          int guardDirection = findInitialDirection(grid[guardPosition[0]][guardPosition[1]]);

          Set<String> visited = new HashSet<>();
          int visitedCount = 0;

          while (true) {
               String posKey = guardPosition[0] + "," + guardPosition[1];
               if (!visited.contains(posKey)) {
                    visited.add(posKey);
                    visitedCount++;
                    grid[guardPosition[0]][guardPosition[1]] = 'X'; // Mark as visited
               }

               int nextRow = guardPosition[0] + DIRECTIONS[guardDirection][0];
               int nextCol = guardPosition[1] + DIRECTIONS[guardDirection][1];

               if (!isInBounds(nextRow, nextCol)) {
                    break;
               }

               if (grid[nextRow][nextCol] == '#') {
                    guardDirection = (guardDirection + 1) % 4;
               } else {
                    guardPosition[0] = nextRow;
                    guardPosition[1] = nextCol;
               }
          }

          return visitedCount;
     }

     private int[] findGuard(char[][] grid) {
          for (int i = 0; i < grid.length; i++) {
               for (int j = 0; j < grid[i].length; j++) {
                    char c = grid[i][j];
                    if (c == '^' || c == '>' || c == 'v' || c == '<') {
                         return new int[]{i, j};
                    }
               }
          }
          throw new IllegalArgumentException("Guard not found on the grid");
     }

     private int findInitialDirection(char guardChar) {
          for (int i = 0; i < DIRECTION_CHARS.length; i++) {
               if (DIRECTION_CHARS[i] == guardChar) {
                    return i;
               }
          }
          throw new IllegalArgumentException("Invalid guard direction");
     }

     private boolean isInBounds(int row, int col) {
          boolean rowInBounds = (row >= 0) && (row < rows);
          boolean colInBounds = (col >= 0) && (col < cols);
          return rowInBounds && colInBounds;
     }
}
