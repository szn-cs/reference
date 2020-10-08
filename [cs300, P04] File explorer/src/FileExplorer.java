//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: File explorer program (P04 assignment)
// Course: CS 300 Fall 2020
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// Lecturer: Hobbes LeGault
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * FileExplorer class responsible for traversing through the file system, and returning files with
 * filtering capabilities.
 * <p>
 * Implements recursive algorithms to achieve filesystem traversal.
 * 
 * @author Safi
 */
public class FileExplorer {

  /**
   * Retrieve folder's immediate contents/dirents.
   * 
   * @param currentFolder target folder to list it's contents
   * @return list of the names of all immediate dirents (files & directories) in given folder
   * @throws NotDirectoryException current folder does not exist or not a directory
   */
  public static ArrayList<String> listContents(File currentFolder) throws NotDirectoryException {
    // validate input path: exists & is directory
    if (!currentFolder.exists() || !currentFolder.isDirectory())
      throw new NotDirectoryException("Target dirent is not a directory.");

    ArrayList<String> dirents = new ArrayList<String>(); // holds directory contents
    dirents.addAll(Arrays.asList(currentFolder.list())); // read immediate contents

    // validate contents exist
    if (dirents.size() == 0)
      return new ArrayList<String>(); // empty list (redundant for statisfying requirement)

    return dirents;
  }

  /**
   * Retrieve folder's files recursively, searching all sub-directories.
   * <p>
   * Implementation using recursive algorithm
   * 
   * @param currentFolder target folder to list it's contents recursively.
   * @return list of the names of all files in given folder & sub-directories
   * @throws NotDirectoryException current folder does not exist or not a directory
   */
  public static ArrayList<String> deepListContents(File currentFolder)
      throws NotDirectoryException {
    ArrayList<File> files = new ArrayList<File>(); // hold files descriptors
    ArrayList<File> folders = new ArrayList<File>(); // hold folders descriptors
    ArrayList<String> filenames = new ArrayList<>(); // hold names of files

    // Get dirents & extract files from folders
    {
      // list immediate files & folders names
      ArrayList<String> direntsName = listContents(currentFolder);
      // open file descriptors for dirents in the current folder
      ArrayList<File> direntsDiscriptor =
          getDescriptorsFromName(direntsName, currentFolder.getPath());
      // arrange files from folders
      arrangeDirents(direntsDiscriptor, files, folders);
      // convert back the files to names
      files.forEach(f -> filenames.add(f.getName()));
    }

    // base terminating case: no sub-directories in the current folder
    if (folders.size() == 0)
      return filenames;

    // reduction / recursive case: search in sub-directories
    for (File f : folders)
      filenames.addAll(deepListContents(f));

    return filenames;
  }

  /**
   * Deep search for a file by exact match to provided filename.
   * <p>
   * Implemented with a recursive algorithm.
   * 
   * @param currentFolder target parent folder to search in.
   * @param fileName      an exact name of file to search for.
   * @return path to the file, if it exists.
   * @throws NoSuchElementException search operation returns with no results found
   */
  public static String searchByName(File currentFolder, String fileName)
      throws NoSuchElementException {
    if (fileName == null)
      throw new NoSuchElementException("Arguments invalid: fileName cannot be null.");

    String filePath = ""; // path to found file if any
    ArrayList<File> files = new ArrayList<File>(); // hold files descriptors
    ArrayList<File> folders = new ArrayList<File>(); // hold folders descriptors
    ArrayList<String> filenames = new ArrayList<>(); // hold names of files

    // Get dirents & extract files from folders
    try {
      // list immediate files & folders names
      ArrayList<String> direntsName = listContents(currentFolder);
      // open file descriptors for dirents in the current folder
      ArrayList<File> direntsDiscriptor =
          getDescriptorsFromName(direntsName, currentFolder.getPath());
      // arrange files from folders
      arrangeDirents(direntsDiscriptor, files, folders);
      // convert back the files to names
      files.forEach(f -> filenames.add(f.getName()));
    } catch (NotDirectoryException e) {
      // propagate error as different exception per specification
      throw new NoSuchElementException(
          "Argument invalid: currentFolder not directory or inexistent.");
    }

    // search for matching filename in current directory
    for (File file : files) {
      if (fileName.equals(file.getName())) {
        filePath = fileName;
        break;
      }
    }

    // base case: if no directories or the file was found
    if (filePath.length() > 0)
      return currentFolder.getName() + File.separator + filePath;
    else if (folders.size() == 0)
      return filePath;

    // multiple found files will return the first one found.
    for (File folder : folders) {
      try {
        filePath = searchByName(folder, fileName); // search in nested folders
        // check if file found
        if (filePath.length() > 0)
          return currentFolder.getName() + File.separator + filePath;
      } catch (NoSuchElementException e) {
        // ignore for nested calls.
      }
    }

    // file not found
    throw new NoSuchElementException("Requested file not found: " + fileName);
  }

  /**
   * Recursive method that searches the given folder and its subfolders for ALL files that contain
   * the given key in part of their name.
   * 
   * @param currentFolder target folder to search in.
   * @param key           the partial filename/string to search for in the filenames
   * @return arraylist of all the names of files that match
   */
  public static ArrayList<String> searchByKey(File currentFolder, String key) {
    ArrayList<String> foundFiles = new ArrayList<String>(); // hold matching files
    ArrayList<File> files = new ArrayList<File>(); // hold files descriptors
    ArrayList<File> folders = new ArrayList<File>(); // hold folders descriptors
    ArrayList<String> filenames = new ArrayList<>(); // hold names of files

    if (key == null)
      return foundFiles;

    // Get dirents & extract files from folders
    try {
      // list immediate files & folders names
      ArrayList<String> direntsName = listContents(currentFolder);
      // open file descriptors for dirents in the current folder
      ArrayList<File> direntsDiscriptor =
          getDescriptorsFromName(direntsName, currentFolder.getPath());
      // arrange files from folders
      arrangeDirents(direntsDiscriptor, files, folders);
      // convert back the files to names
      files.forEach(f -> filenames.add(f.getName()));
    } catch (NotDirectoryException e) {
      // do not propagate error, instead return empty array.
      return foundFiles;
    }

    // search for matching filename in current directory
    for (File file : files) {
      if (file.getName().contains(key))
        foundFiles.add(file.getName());
    }

    // base case: if no directories
    if (folders.size() == 0)
      return foundFiles;

    // multiple found files will return the first one found.
    for (File folder : folders)
      foundFiles.addAll(searchByKey(folder, key)); // search in nested folders

    // return found files, if any.
    return foundFiles;
  }

  /**
   * Recursive method that searches the given folder and its subfolders for ALL files whose size is
   * within the given max and min values, inclusive.
   * 
   * @param currentFolder target folder to search in.
   * @param sizeMin       lower size boundary
   * @param sizeMax       upper size boundary
   * @return arraylist names of all files whose size are within the boundaries
   */
  public static ArrayList<String> searchBySize(File currentFolder, long sizeMin, long sizeMax) {
    ArrayList<String> foundFiles = new ArrayList<String>(); // hold matching files
    ArrayList<File> files = new ArrayList<File>(); // hold files descriptors
    ArrayList<File> folders = new ArrayList<File>(); // hold folders descriptors
    ArrayList<String> filenames = new ArrayList<>(); // hold names of files

    try {
      // list immediate files & folders names
      ArrayList<String> direntsName = listContents(currentFolder);
      // open file descriptors for dirents in the current folder
      ArrayList<File> direntsDiscriptor =
          getDescriptorsFromName(direntsName, currentFolder.getPath());
      // arrange files from folders
      arrangeDirents(direntsDiscriptor, files, folders);
      // convert back the files to names
      files.forEach(f -> filenames.add(f.getName()));
    } catch (NotDirectoryException e) {
      // do not propagate error, instead return empty array.
      return foundFiles;
    }

    // search for matching filename in current directory
    for (File file : files) {
      if (file.length() >= sizeMin && file.length() <= sizeMax)
        foundFiles.add(file.getName());
    }

    // base case: if no directories
    if (folders.size() == 0)
      return foundFiles;

    // multiple found files will return the first one found.
    for (File folder : folders)
      foundFiles.addAll(searchBySize(folder, sizeMin, sizeMax)); // search in nested folders

    // return found files, if any.
    return foundFiles;
  }

  /**
   * Arranges a list of dirents into a separate list for files and another for folders.
   * 
   * @param dirents list of file and folder descriptors (Note: list is cleared after usage)
   * @param files   target list for arranging files to.
   * @param folders target list for arranging folders to.
   */
  private static void arrangeDirents(ArrayList<File> dirents, ArrayList<File> files,
      ArrayList<File> folders) {
    // check if current dirent is a file or folder
    for (File f : dirents) {
      if (f.isDirectory())
        folders.add(f);
      else if (f.isFile())
        files.add(f);
    }
    dirents.clear(); // free up memory
  }

  /**
   * Open file descriptors for each of the dirent names in the input list.
   * 
   * @param dirents  list of files/folders names (Note: list is cleared after usage)
   * @param basePath location path to the listed files
   * @return file descriptors list
   */
  private static ArrayList<File> getDescriptorsFromName(ArrayList<String> dirents,
      String basePath) {
    ArrayList<File> descriptors = new ArrayList<>();
    for (int i = 0; i < dirents.size(); i++) {
      File f = new File(basePath + File.separator + dirents.get(i));
      descriptors.add(f);
    }
    dirents.clear(); // freeup memory
    return descriptors;
  }
}
