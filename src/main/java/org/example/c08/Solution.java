package org.example.c08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Solution {
     public static void main(String[] args) throws IOException {
          char[][] grid = readFile(args[0]);
          display(grid);
          Solution solution = new Solution();
          int uniqueAntinodes = solution.calculateAntinodes(grid);
          System.out.println("Total unique antinodes: " + uniqueAntinodes);
     }

     public static char[][] readFile(String filePath) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(filePath));
          int i = 0;
          char[][] grid = new char[50][50];
          String line;
          while ((line = reader.readLine()) != null) {
               char[] lineArray = line.toCharArray();
               grid[i] = lineArray;
               i++;
          }

          return grid;
     }

     public static void display(char[][] grid) {
          for (char[] row : grid) {
               for (char c : row) {
                    System.out.print(c + " ");
               }
               System.out.println();
          }
     }

     // is a particular spot inside the grid?
     private static boolean isInBounds(char[][] grid, int row, int col) {
          boolean rowIsInBounds = (row >= 0) && (row < grid.length);
          boolean colIsInBounds = (col >= 0) && (col < grid[0].length);
          return rowIsInBounds && colIsInBounds;
     }

     // "Each antenna is tuned to a specific frequency indicated by a single lowercase letter,
     // uppercase letter, or digit. You create a map (your puzzle input) of these antennas"
     private static Map<Character, List<int[]>> findAntennas(char[][] grid) {
          Map<Character, List<int[]>> antennas = new HashMap<>();
          // loop through each position
          for (int i = 0; i < grid.length; i++) {
               for (int j = 0; j < grid[i].length; j++) {
                    // find a character at a position
                    char c = grid[i][j];
                    // if it is alphanumeric, it is an antenna
                    // if it is ., it isn't an antenna
                    if (Character.isLetterOrDigit(c)) {
                         if (!antennas.containsKey(c)) {
                              antennas.put(c, new ArrayList<>());
                         }
                         // add the position of that particular alphanumeric antenna
                         antennas.get(c).add(new int[]{i, j});
                    }
               }
          }
          // all the antennas and where they are
          return antennas;
     }


     // an antinode occurs at any point that is perfectly in line with two antennas of the same frequency
     // - but only when one of the antennas is twice as far away as the other.
     // This means that for any pair of antennas with the same frequency, there are
     // two antinodes, one on either side of them.
     public int calculateAntinodes(char[][] grid) {
          Map<Character, List<int[]>> antennas = findAntennas(grid);
          Set<String> uniqueAntinodes = new HashSet<>();

          for (Entry<Character, List<int[]>> entry : antennas.entrySet()) {
               List<int[]> positions = entry.getValue();

               for (int i = 0; i < positions.size(); i++) {
                    for (int j = 0; j < positions.size(); j++) {
                         if (i != j) {
                              int[] a = positions.get(i);
                              int[] b = positions.get(j);

                              // determine where an antinode is if one exists
                              int rowAntinode = a[0] + 2 * (b[0] - a[0]);
                              int colAntinode = a[1] + 2 * (b[1] - a[1]);

                              if (isInBounds(grid, rowAntinode, colAntinode)) {
                                   uniqueAntinodes.add(rowAntinode + "," + colAntinode);
                              }


                              // antinodes come in pairs because of the 2:1 ratio
                              rowAntinode = b[0] + 2 * (a[0] - b[0]);
                              colAntinode = b[1] + 2 * (a[1] - b[1]);

                              if (isInBounds(grid, rowAntinode, colAntinode)) {
                                   uniqueAntinodes.add(rowAntinode + "," + colAntinode);
                              }
                         }

                    }
               }
          }

          return uniqueAntinodes.size();
     }
}