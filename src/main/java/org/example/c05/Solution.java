package org.example.c05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.Collections.reverse;

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
     public static int solve(List<List<Integer>> badUpdates, List<Rule> ruleList) {
          int result = 0;

          for (List<Integer> update : badUpdates) {
               List<Integer> reorderedUpdate = reorderUpdate(update, ruleList);
               if (reorderedUpdate.isEmpty()) {
                    continue;
               }
               int middleIndex = reorderedUpdate.size() / 2;
               result += reorderedUpdate.get(middleIndex);
          }
          return result;
     }


     /**
      * cycle detection by dfs search; true = cycle found, false = no cycle
      */
     public static boolean dfs(int pageNumber,
                               Map<Integer, List<Integer>> graph,
                               Set<Integer> visited,
                               Set<Integer> recursionStack,
                               List<Integer> result) {
          boolean res = false;
          boolean finished = false;
          // recursion stack keeps track of path, so if path already contains a given page, and we want to go there again, that's a cycle
          if (recursionStack.contains(pageNumber)) {
               return true;
          }
          // if we haven't found a cycle yet
          else if (!visited.contains(pageNumber)) {
               // add the page to the stack
               recursionStack.add(pageNumber);
               // for each neighbor of the page, dfs
               for (int neighbor : graph.get(pageNumber)) {
                    if (dfs(neighbor, graph, visited, recursionStack, result)) {
                         res = true;
                         finished = true;
                         break;
                    }
               }
               // if not finished searching
               if (!finished) {
                    // pop a page from the stack
                    recursionStack.remove(pageNumber);
                    // mark it visited
                    visited.add(pageNumber);
                    // add it to the result list
                    result.add(pageNumber);
               }
          }

          // if this is true, there is a cycle; if false, no cycle
          return res;
     }

     /**
      * for part 2, the rules stipulate that "For each of the incorrectly-ordered updates,
      * use the page ordering rules to put the page numbers in the right order.
      */
     public static List<Integer> reorderUpdate(List<Integer> update, List<Rule> ruleList) {
          Map<Integer, List<Integer>> graph = new HashMap<>();

          // put each page in the map
          for (int pageNum : update) {
               graph.put(pageNum, new ArrayList<>());
          }

          // for each rule, if both the first number and second number in the rule are in the update
          // build a list with the pages that have to come after "first," and add "second" to that list
          for (Rule rule : ruleList) {
               if (update.contains(rule.getFirst()) && update.contains(rule.getSecond())) {
                    graph.get(rule.getFirst()).add(rule.getSecond());
               }
          }

          // reordered
          List<Integer> result = new ArrayList<>();
          // keep track of where we've been
          Set<Integer> visited = new HashSet<>();
          Set<Integer> recursionStack = new HashSet<>();

          // for each page in the update list, if the page has been seen, go on;
          for (int page : update) {
               // don't revisit visited pages
               if (visited.contains(page)) {
                    continue;
               }

               // cycle detection by dfs-- if this is true, there was a cycle, so return a new list
               if (dfs(page, graph, visited, recursionStack, result)) {
                    return new ArrayList<>();
               }
          }

          // reverse the reverse, since dfs will reverse the list because of pushing/popping
          reverse(result);
          return result;
     }

     /**
      * populate the rule and update lists, then find the good updates, get their middle indices, and sum them
      */
     public static void main(String[] args) throws IOException {
          ruleList = populateRuleList(args[0]);
          updateList = populateUpdateList(args[0]);

          List<List<Integer>> badUpdates = new ArrayList<>();

          List<List<Integer>> goodUpdates = new ArrayList<>();
          for (List<Integer> update : updateList) {
               if (isUpdateCorrectlyOrdered(update, ruleList)) {
                    goodUpdates.add(update);
               } else {
                    badUpdates.add(update);
               }
          }

          // Solve for the bad updates
          int result = solve(badUpdates, ruleList);

          // Output the result
          System.out.println("Final result: " + result);
     }
}