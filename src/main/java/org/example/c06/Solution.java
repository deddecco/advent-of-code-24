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

     // print out the grid
     private static void display(char[][] grid) {
          for (char[] row : grid) {
               System.out.println(new String(row));
          }
     }

     // read the file into a grid
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

     // the main logic of the puzzle
     // depending on where the guard is, where it faces, and what is in front of it,
     // move or turn as appropriate, marking wherever is vcisited as such
     public int predictPatrolPath(char[][] grid) {
          rows = grid.length;
          cols = grid[0].length;

          int[] guardPosition = findGuard(grid);
          // guard position gives the coordinates of the guard at the start
          // guardPosition[0] gives the row
          // guardPosition[1] gives the column
          int guardDirection = findInitialDirection(grid[guardPosition[0]][guardPosition[1]]);

          Set<String> visited = new HashSet<>();
          int visitedCount = 0;

          while (true) {
               String posKey = guardPosition[0] + "," + guardPosition[1];
               // if a place hasn't been visited,
               // visit it
               // increment the count of visited places
               // and mark on the grid that the place is X
               if (!visited.contains(posKey)) {
                    visited.add(posKey);
                    visitedCount++;
                    grid[guardPosition[0]][guardPosition[1]] = 'X'; // Mark as visited
               }

               // this is where the guard will go next
               int nextRow = guardPosition[0] + DIRECTIONS[guardDirection][0];
               int nextCol = guardPosition[1] + DIRECTIONS[guardDirection][1];

               // when the guard cannot visit a location because it doesn't exist, don't try, and stop
               if (!isInBounds(nextRow, nextCol)) {
                    break;
               }

               // obstacles
               if (grid[nextRow][nextCol] == '#') {
                    // turn right 90deg
                    guardDirection = (guardDirection + 1) % 4;
               }
               // not an obstacle, not already visited
               else {
                    guardPosition[0] = nextRow;
                    guardPosition[1] = nextCol;
               }
          }

          // the answer-- how many places the guard visited
          return visitedCount;
     }

     // find the guard in the puzzle
     private int[] findGuard(char[][] grid) {
          for (int i = 0; i < grid.length; i++) {
               for (int j = 0; j < grid[i].length; j++) {
                    char c = grid[i][j];
                    if (c == '^' || c == '>' || c == 'v' || c == '<') {
                         return new int[]{i, j};
                    }
               }
          }
          // handle the error where there is no guard
          throw new IllegalArgumentException("Guard not found on the grid");
     }

     // when the puzzle starts, which way does the guard face?
     private int findInitialDirection(char guardChar) {
          for (int i = 0; i < DIRECTION_CHARS.length; i++) {
               if (DIRECTION_CHARS[i] == guardChar) {
                    return i;
               }
          }
          throw new IllegalArgumentException("Invalid guard direction");
     }

     // determine if a position is inside the grid or not
     private boolean isInBounds(int row, int col) {
          boolean rowInBounds = (row >= 0) && (row < rows);
          boolean colInBounds = (col >= 0) && (col < cols);
          return rowInBounds && colInBounds;
     }
}
