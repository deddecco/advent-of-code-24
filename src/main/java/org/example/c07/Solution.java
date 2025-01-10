package org.example.c07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {

     public static void main(String[] args) {
          if (args.length == 0) {
               System.err.println("Please provide the file name as a command-line argument.");
               return;
          }

          String fileName = args[0];
          try {
               Solution solution = new Solution();
               long goodResults = solution.findGoodLines(fileName);
               System.out.println("Total of good results: " + goodResults);
          } catch (IOException e) {
               System.err.println("Error reading file: " + e.getMessage());
          }
     }

     // a line is good if the number before the colon can be made from any combination of + and * applied
     // to the numbers in the rest of the line IN THE ORDER THEY APPEAR IN THE LINE
     public long findGoodLines(String fileName) throws IOException {
          List<String> fileContent = readFile(fileName);
          long goodResults = 0;

          for (String line : fileContent) {
               line = line.trim();
               // skip over blanks
               if (line.isEmpty()) {
                    continue;
               }

               // find the result and the operands
               String[] parts = line.split(":");
               long result;
               // make an operand list
               // numbers can get very big, so use longs, not ints
               List<Long> operands = new ArrayList<>();

               try {
                    result = Long.parseLong(parts[0].trim());
                    String[] operandParts = parts[1].trim().split("\\s+");
                    for (String operand : operandParts) {
                         operands.add(Long.parseLong(operand));
                    }
               } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid line (number format issue): " + line);
                    continue;
               }

               // if you can match it, like 190: 10 9 10 10, as in 190 = 10 * 9 + 10 * 10 => 190 is 90 + 100
               // then the result is "good"
               if (doesMatch(result, operands)) {
                    goodResults += result;
               }
          }

          // the answer is the sum of all the good results
          return goodResults;
     }

     private static boolean doesMatch(long result, List<Long> operands) {
          return evaluateExpression(operands, 0, result);
     }

     private static boolean evaluateExpression(List<Long> operands, int index, long result) {
          // does the final result on the right match the target on the left?
          if (index == operands.size() - 1) {
               return operands.get(index) == result;
          }

          long currentValue = operands.get(index);
          long nextValue = operands.get(index + 1);

          // see if multiplying the current pair is correct
          if (tryMult(operands, index, result, currentValue, nextValue)) {
               return true;
          }
          // see if adding the current pair is correct
          if (tryAdd(operands, index, result, currentValue, nextValue)) {
               return true;
          }

          // move to the next adjacent pair
          operands.set(index + 1, nextValue);

          // if adding didn't work, and multiplying didn't work, then there is no answer for the given right hand side
          return false;
     }

     // see if adding works
     private static boolean tryAdd(List<Long> operands, int index, long result, long currentValue, long nextValue) {
          operands.set(index + 1, currentValue * nextValue);
          return evaluateExpression(operands, index + 1, result);
     }

     // see if multiplying works
     private static boolean tryMult(List<Long> operands, int index, long result, long currentValue, long nextValue) {
          operands.set(index + 1, currentValue + nextValue);
          return evaluateExpression(operands, index + 1, result);
     }

     public static List<String> readFile(String fileName) throws IOException {
          List<String> lines = new ArrayList<>();
          try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
               String line;
               while ((line = reader.readLine()) != null) {
                    lines.add(line);
               }
          }
          return lines;
     }
}
