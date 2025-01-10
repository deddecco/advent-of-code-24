package org.example.c09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Character.getNumericValue;

public class Solution {
     // Reads the disk map string from the file
     public String readFile(String fileName) throws IOException {
          BufferedReader reader = new BufferedReader(new FileReader(fileName));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
               sb.append(line);
          }
          return sb.toString();
     }

     // Parse the disk map string into an array of DataSpace objects
     public DataSpace[] parseDataSpace(String diskMap) {
          int length = diskMap.length();

          int arraySize = 0;
          int i = 0;
          arraySize = calculateArraySize(diskMap);

          // Create the array and fill it
          DataSpace[] dataSpaces = new DataSpace[arraySize];
          int index = 0;
          int fileId = 0;
          while (i < length) {
               // store the file length
               int fileLength = getNumericValue(diskMap.charAt(i));
               for (int j = 0; j < fileLength; j++) {
                    dataSpaces[index] = new DataSpace(true, fileId);
                    index++;
               }
               i++;

               if (i < length) {
                    // Read the free space length
                    int freeSpaceLength = getNumericValue(diskMap.charAt(i));
                    // move ahead that many spaces
                    for (int j = 0; j < freeSpaceLength; j++) {
                         dataSpaces[index++] = new DataSpace(false, -1);
                    }
                    i++;
               }
               fileId++;
          }

          return dataSpaces;
     }

     // find the size of the array
     private static int calculateArraySize(String diskMap) {
          int arraySize = 0;

          // Iterate through the diskMap string
          for (int i = 0; i < diskMap.length(); i++) {
               arraySize += getNumericValue(diskMap.charAt(i));

               // if the next character doesn't take us out of bounds...
               if (i % 2 == 0 && (i + 1) < diskMap.length()) {
                    i++;
                    // Skip the free space length;
                    // every other number is a free space length indicator,
                    // so only half of them are file indicators
                    arraySize += getNumericValue(diskMap.charAt(i));
               }

          }

          return arraySize;
     }

     // Move the files to the leftmost free space
     public void move(DataSpace[] dataSpaces) {
          // assume a move has happened
          boolean moved = true;
          while (moved) {
               // we have not moved this particular dataspace
               moved = false;
               // Look for dataspace that are not at the leftmost position
               for (int i = dataSpaces.length - 1; i >= 0; i--) {
                    if (dataSpaces[i].getFileStatus()) {
                         // Look for the leftmost free space where this dataspace can move
                         for (int j = 0; j < i; j++) {  // Inner loop remains the same
                              if (!dataSpaces[j].getFileStatus()) {
                                   // Move the file block to the free space
                                   dataSpaces[j] = new DataSpace(true, dataSpaces[i].getFileId());
                                   dataSpaces[i] = new DataSpace(false, -1);
                                   // there has now been a move
                                   moved = true;
                                   // don't move this one anymore, now go to the next one
                                   break;
                              }
                         }
                         // Break the outer loop if a file has been moved
                         if (moved) {
                              break;
                         }
                    }
               }
          }
     }

     // display the current configuration of the array of DataSpaces
     public static void display(DataSpace[] dataSpaces) {
          StringBuilder sb = new StringBuilder();
          for (DataSpace dataSpace : dataSpaces) {
               if (dataSpace.getFileStatus()) {
                    sb.append(dataSpace.getFileId());
               } else {
                    sb.append('.');
               }
          }
          System.out.println(sb);
     }

     // checksum is sum of all (id * position) of files
     public static long generateCheckSum(DataSpace[] dataSpaces) {
          long checkSum = 0;

          for (int i = 0; i < dataSpaces.length; i++) {
               if (dataSpaces[i].isFile) {
                    checkSum += (long) dataSpaces[i].fileId * i;
               }
          }
          return checkSum;
     }

     // DataSpace class represents a block of space (either a file or free space)
     public static class DataSpace {

          // true if this is a file block, false if free space
          private final int fileId;
          // The file ID if it's a file, or -1 for free space
          private final boolean isFile;

          public DataSpace(boolean isFile, int fileId) {
               this.isFile = isFile;
               this.fileId = fileId;
          }

          public boolean getFileStatus() {
               return isFile;
          }

          public int getFileId() {
               return fileId;
          }

          @Override
          public String toString() {
               return "DataSpace { " + "isFile = " + isFile + ", fileId = " + fileId + '}';
          }

     }

     public static void main(String[] args) throws IOException {
          Solution solution = new Solution();
          // read in the file from args
          String diskMap = solution.readFile(args[0]);
          System.out.println("Initial disk map:");
          // Parse the disk map into files and free spaces
          DataSpace[] dataSpaces = solution.parseDataSpace(diskMap);
          // print the map
          display(dataSpaces);
          // move things as appropriate-- to the leftmost free position
          solution.move(dataSpaces);
          // Print the final disk map only once
          System.out.println("Final disk map:");
          // display the map again after all moves are done
          display(dataSpaces);
          // generate the checksum -- the answer to the puzzle
          System.out.println("checksum: " + generateCheckSum(dataSpaces));
     }
}