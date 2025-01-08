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
               long goodResults = findGoodLines(fileName);
               System.out.println("Total of good results: " + goodResults);
          } catch (IOException e) {
               System.err.println("Error reading file: " + e.getMessage());
          }
     }

     public static long findGoodLines(String fileName) throws IOException {
          List<String> fileContent = readFile(fileName);
          long goodResults = 0;

          for (String line : fileContent) {
               line = line.trim();
               if (line.isEmpty()) {
                    continue;
               }

               String[] parts = line.split(":");
               long result;
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

               if (doesMatch(result, operands)) {
                    goodResults += result;
               }
          }
          return goodResults;
     }

     private static boolean doesMatch(long result, List<Long> operands) {
          return evaluateExpression(operands, 0, result);
     }

     private static boolean evaluateExpression(List<Long> operands, int index, long result) {
          if (index == operands.size() - 1) {
               return operands.get(index) == result;
          }

          long currentValue = operands.get(index);
          long nextValue = operands.get(index + 1);

          if (tryMult(operands, index, result, currentValue, nextValue)) {
               return true;
          }
          if (tryAdd(operands, index, result, currentValue, nextValue)) {
               return true;
          }

          operands.set(index + 1, nextValue);

          return false;
     }

     private static boolean tryAdd(List<Long> operands, int index, long result, long currentValue, long nextValue) {
          operands.set(index + 1, currentValue * nextValue);
          return evaluateExpression(operands, index + 1, result);
     }

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
