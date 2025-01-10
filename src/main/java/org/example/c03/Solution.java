package org.example.c03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Solution {

     public static long runningSum = 0;

     public static void main(String[] args) throws IOException {
          String filePath = args[0];

          Solution solution = new Solution();
          List<int[]> multiplicationPairs = solution.extractToListOfArrays(filePath);

          // add the product of the good pairs to the sum
          for (int[] pair : multiplicationPairs) {
               System.out.println("pair = " + Arrays.toString(pair));
               addToRunningSum(pair);
          }
          System.out.println("Final runningSum = " + runningSum);
     }

     // keep the running sum updated
     private static void addToRunningSum(int[] pair) {
          int intermediateResult = pair[0] * pair[1];
          runningSum += intermediateResult;
     }

     // come up with a list of arrays of numbers to be multiplied together
     public List<int[]> extractToListOfArrays(String filePath) throws IOException {
          List<int[]> result = new ArrayList<>();
          String regex = "(mul\\((\\d+),\\s*(\\d+)\\)|do\\(\\)|don't\\(\\))";
          Pattern pattern = Pattern.compile(regex);

          boolean shouldProcess = true;

          try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
               String line;
               while ((line = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                         String instruction = matcher.group(1);
                         if (instruction.equals("do()")) {
                              shouldProcess = true;
                         } else if (instruction.equals("don't()")) {
                              shouldProcess = false;
                         } else if (shouldProcess && instruction.startsWith("mul(")) {
                              int num1 = parseInt(matcher.group(2));
                              int num2 = parseInt(matcher.group(3));
                              result.add(new int[]{num1, num2});
                         }
                    }
               }
          }

          return result;
     }
}