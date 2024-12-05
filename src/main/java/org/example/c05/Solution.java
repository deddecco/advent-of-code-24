package org.example.c05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;

public class Solution {

     /**
      * list to keep rules
      */
     public static List<Rule> ruleList = new ArrayList<>();
     /**
      * list to store update lists
      */
     public static List<List<Integer>> updateList = new ArrayList<>();


     /**
      * read through the file and create a list of rule objects
      */
     public static List<Rule> populateRuleList(String filePath) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(filePath));
          String line;
          while ((line = reader.readLine()) != null) {
               if (line.contains("|")) {
                    String[] lineArray = line.split("\\|");
                    Rule newRule = new Rule(parseInt(lineArray[0]), parseInt(lineArray[1]));
                    ruleList.add(newRule);
               }
          }
          return ruleList;
     }


     /**
      * read through the file and create a list of update lists
      */
     public static List<List<Integer>> populateUpdateList(String filePath) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(filePath));
          String line;
          while ((line = reader.readLine()) != null) {
               if (line.contains(",")) {
                    String[] lineArray = line.split(",");
                    List<Integer> currentUpdate = new ArrayList<>();
                    for (String s : lineArray) {
                         currentUpdate.add(parseInt(s));
                    }
                    updateList.add(currentUpdate);
               }
          }
          return updateList;
     }

     /**
      * check if an update follows the rules in the rule list
      */
     public static boolean isUpdateCorrectlyOrdered(List<Integer> update, List<Rule> ruleList) {
          for (Rule rule : ruleList) {
               // if both pages in update object
               if (update.contains(rule.getFirst()) && update.contains(rule.getSecond())) {
                    int firstIndex = update.indexOf(rule.getFirst());
                    int secondIndex = update.indexOf(rule.getSecond());

                    /*
                     page that should be first comes after page that should be second
                     if this happens, update is bad
                    */
                    if (firstIndex > secondIndex) {
                         return false;
                    }
               }
          }

          // pages are properly ordered
          return true;
     }

     // find the middle of updates that follow the rules and add those middles to a list
     public List<Integer> getMiddleOfGoodUpdates(List<List<Integer>> goodUpdateList) {
          List<Integer> goodMiddles = new ArrayList<>();

          for (List<Integer> updateList : goodUpdateList) {
               int middleIndex = updateList.size() / 2;
               goodMiddles.add(updateList.get(middleIndex));
          }

          return goodMiddles;
     }


     /**
      * Method to generate the list of "good" updates (those that are correctly ordered)
      */
     public List<List<Integer>> generateGoodUpdates(List<List<Integer>> updateList, List<Rule> ruleList) {
          List<List<Integer>> goodUpdates = new ArrayList<>();

          for (List<Integer> update : updateList) {
               if (isUpdateCorrectlyOrdered(update, ruleList)) {
                    goodUpdates.add(update);
               }
          }

          return goodUpdates;
     }

     /**
      * go through the list of good middle numbers and add them
      */
     private int solve(List<Integer> goodMiddles) {
          int result = 0;

          for (Integer goodMiddle : goodMiddles) {
               result += goodMiddle;
          }
          return result;
     }


     /**
      * populate the rule and update lists, then find the good updates, get their middle indices, and sum them
      */
     public static void main(String[] args) throws IOException {
          ruleList = populateRuleList(args[0]);
          updateList = populateUpdateList(args[0]);

          Solution solution = new Solution();
          List<List<Integer>> goodUpdates = solution.generateGoodUpdates(updateList, ruleList);
          List<Integer> goodMiddles = solution.getMiddleOfGoodUpdates(goodUpdates);

          int result = solution.solve(goodMiddles);
          System.out.println("Final result: " + result);
     }
}