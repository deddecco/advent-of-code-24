package org.example.c01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

public class Solution {
     static List<Integer> leftList = new ArrayList<>();
     static List<Integer> rightList = new ArrayList<>();

     public static void main(String[] args) throws IOException {
          Solution solution = new Solution();
          solution.readFile(args[0]);
          sort(leftList);
          sort(rightList);

          int result = solution.calculateDistance(leftList, rightList);
          System.out.println("total distance: " + result);

          int similarity = solution.calculateSimilarity(leftList, rightList);
          System.out.println("similarity = " + similarity);
     }

     private void readFile(String fileName) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(fileName));

          String line;
          while ((line = reader.readLine()) != null) {
               String[] arr = line.split("\t");
               leftList.add(Integer.parseInt(arr[0]));
               rightList.add(Integer.parseInt(arr[1]));
          }
     }

     public int calculateDistance(List<Integer> leftList, List<Integer> rightList) {
          int totalDistance = 0;

          // pair the smallest numbers together
          // then the next smallest
          // etc.
          // and calculate the running sum of the absolute distances between the pairs
          for (int i = 0; i < leftList.size(); i++) {
               totalDistance += Math.abs(leftList.get(i) - rightList.get(i));
          }


          // return the sum
          return totalDistance;
     }

     public int calculateSimilarity(List<Integer> leftList, List<Integer> rightList) {
          int similarity = 0;


          // multiply each number in the left list by how many ocurrences it has in the right list
          // and keep a running total of these products
          for (int elem : leftList) {
               int count = frequency(rightList, elem);
               similarity += (count * elem);
          }

          // return the total
          return similarity;
     }


}
