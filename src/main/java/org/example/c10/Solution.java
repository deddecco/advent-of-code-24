package org.example.c10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
     // read the file into a grid
     public static char[][] readFile(String filePath) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(filePath));

          List<char[]> gridList = new ArrayList<>();
          String line;
          while ((line = reader.readLine()) != null) {
               gridList.add(line.toCharArray());
          }
          return gridList.toArray(new char[0][]);
     }

     // print out the grid
/*
     public static void display(char[][] grid) {
          for (char[] row : grid) {
               for (char c : row) {
                    System.out.print(c + " ");
               }
               System.out.println();
          }
     }
*/

     // look for every 0 in the grid and count the number of nines reachable from each.
     // keep a running sum and return it
     public static int getScore(char[][] grid) {
          int totalScore = 0;
          for (int i = 0; i < grid.length; i++) {
               for (int j = 0; j < grid[0].length; j++) {
                    // if this condition is true this grid position is a trailhead
                    if (grid[i][j] == '0') {
                         totalScore += countReachableNines(grid, i, j, new boolean[grid.length][grid[0].length]);
                    }
               }
          }

          // each trailhead has a certain number of nines it can get to; the score is the sum of all those totals
          return totalScore;
     }

     private static int countReachableNines(char[][] grid, int row, int col, boolean[][] visited) {
          // no reachable nines from outside the grid, or from already visited locations
          if (isOutOfBounds(grid, row, col) || visited[row][col]) {
               return 0;
          }

          // if a location is a nine, then there is one reachable nine-- itself
          if (grid[row][col] == '9') {
               visited[row][col] = true;
               return 1;
          }

          // when you go somewhere, mark it visited
          visited[row][col] = true;
          // start with a count of 0
          int count = 0;

          // move one unit in one direction at a time-- up, down, left, right-- but no diagonal movement allowed
          int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
          // for each possible move
          // try to go there
          // check if the move is valid
          // if so, count the number of nines, and return the count
          for (int[] move : moves) {
               int newRow = row + move[0];
               int newCol = col + move[1];
               // for each new location, count the number of nines reachable from it
               if (isValidMove(grid, newRow, newCol, visited, grid[row][col])) {
                    count += countReachableNines(grid, newRow, newCol, visited);
               }
          }

          return count;
     }

     // true if there is a problem and a location is out of bounds
     private static boolean isOutOfBounds(char[][] grid, int row, int col) {
          // if any of the following are true: row or column less than 0 or too big
          // then a location is out of bounds
          return (row < 0 || row >= grid.length) || (col < 0 || col >= grid[0].length);
     }

     private static boolean isValidMove(char[][] grid, int row, int col, boolean[][] visited, char prevHeight) {
          // going somewhere you've been, or out of bounds, is not valid
          if (isOutOfBounds(grid, row, col) || visited[row][col]) {
               return false;
          }

          // for a location that is in bounds and not previously visited, a move is valid if the height of the
          // destination is exactly one more than the height of the origin
          return (Character.getNumericValue(grid[row][col]) == (Character.getNumericValue(prevHeight) + 1));
     }

     public static void main(String[] args) throws IOException {
          char[][] grid = readFile(args[0]);
          System.out.println("Total score (sum of reachable 9s from trailheads): " + getScore(grid));
     }
}