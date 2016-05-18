package polytech.devint.util.file;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Loris Friedel
 */
public final class ResourceLoader {

  public ResourceLoader() {
    // does nothing
  }

  /**
   * Concatenates
   *
   * @param folder
   * @param name
   * @return
   */
  private String concat(final String folder, final String name) {
    return rectify(folder) + "/" + rectify(name);
  }

  /**
   * Rectifies the given string to match the path pattern properly.
   *
   * @param str String to be rectified.
   * @return A new String meant to be a proper path.
   */
  private String rectify(final String str) {
    if (!isValidPath(str)) {
      throw new ResourceFileErrorException("Invalid path.");
    }

    String proper = str;
    while (proper.endsWith("/")) {
      proper = proper.substring(0, proper.length() - 1);
    }
    while (proper.startsWith("/")) {
      proper = proper.substring(1, proper.length());
    }

    return proper;
  }

  /**
   * Check if the given string, that should represents a path, is a valid path.
   * I.e. the given path is well formed and math the files pattern.
   *
   * @param path Path we want to check its validity.
   * @return True if the given path is correct, false otherwise.
   */
  private static boolean isValidPath(final String path) {
    try {
      Paths.get(path);
    } catch (InvalidPathException | NullPointerException ex) {
      return false;
    }
    return true;
  }

  /**
   * Loads the image icon from the given folder and with the given name.
   *
   * @param folder Folder (in the resource directory) that contains the file to load.
   * @param name   Name of the image file to load.
   * @return An instance of the image icon corresponding to the loaded image file.
   * @throws ResourceFileErrorException If the file cannot be loaded.
   */
  public final ImageIcon loadImageIcon(final String folder, final String name) {
    try {
      return new ImageIcon(ImageIO.read(loadFileFrom(folder, name)));
    } catch (IOException e) {
      throw new ResourceFileErrorException(e);
    }
  }

  /**
   * Loads the file that is in the given folder with the given name.
   *
   * @param folder Folder in which the file is.
   * @param name   Name of the file we want to load.
   * @return An instance of the desired file.
   */
  public final File loadFileFrom(final String folder, final String name) {
    String completePath = concat(folder, name);
    try {
      return loadFileFrom(getResource(completePath));
    } catch (Exception e) {
      // Try to load the file from input stream
      // if the previous loading method has failed
      return getFileFromIs(completePath);
    }
  }


  /**
   * Loads the file input stream from the file
   * that is in the given folder and that has the given name.
   *
   * @param folder Folder in which the file is.
   * @param name   Name of the file we want to load.
   * @return An input stream of the desired file.
   */
  public final InputStream loadInputStreamFrom(String folder, String name) {
    try {
      return new FileInputStream(loadFileFrom(folder, name));
    } catch (FileNotFoundException e) {
      throw new ResourceFileErrorException(e);
    }
  }

  /**
   * Loads all file (and only file, not folder) that are in the folder pointed by the given URL
   *
   * @param url Url of a folder from which we want to load file.
   * @return A list of all loaded file (and only file, not folder) from the pointed folder.
   * @throws ResourceFileErrorException If the file cannot be loaded.
   */
  public final File loadFileFrom(final URL url) {
    if (url == null) {
      throw new ResourceFileErrorException("URL NULL");
    }
    try {
      return new File(url.toURI());
    } catch (URISyntaxException e) {
      throw new ResourceFileErrorException(e);
    }
  }

  /**
   * Loads all file (and only file, not folder) from the given resource folder path
   * and check if there is at least one file loader.
   * An exception is thrown if the given path doesn't lead to a folder.
   *
   * @param resourceFolderPath Path of the resource folder in which the file we want to load are.
   * @return A list of file, that were in the folder pointed by the given path.
   * @deprecated Does not work when using resources from an executable jar.
   */
  @Deprecated
  public final List<File> loadAllFilesFrom(final String resourceFolderPath) {
    /* Deprecated check
    if (files.isEmpty()) { throw new ResourceFileErrorException(); } */

    try {
      return loadAllFilesFrom(getResource(rectify(resourceFolderPath)));
    } catch (Exception e) {
      final File folder = getFileFromIs(resourceFolderPath);
      return Arrays.asList(folder.listFiles()).stream()
              .filter(File::isFile)
              .collect(Collectors.toList());
    }
  }

  /**
   * Loads all file (and only file, not folder) that are in the folder pointed by the given URL
   * and match the given predicate.
   * An exception is thrown if the given path doesn't lead to a folder.
   *
   * @param url       Url of a folder from which we want to load file.
   * @param predicate Predicate that will filter files.
   * @return A list of all loaded file (and only file, not folder) from the pointed folder.
   * @throws ResourceFileErrorException If an error occurred when trying to load all files.
   * @deprecated Does not work when using resources from an executable jar.
   */
  @Deprecated
  public final List<File> loadAllFilesFrom(final URL url, final Predicate<File> predicate) {
    if (url == null) {
      throw new ResourceFileErrorException();
    }

    List<File> files = new ArrayList<>();
    try (Stream<Path> stream = Files.walk(Paths.get(url.toURI()))) {
      stream.filter(Files::isRegularFile)
              .map(Path::toFile)
              .filter(predicate)
              .forEach(files::add);
    } catch (IOException | URISyntaxException e) {
      throw new ResourceFileErrorException(e);
    }
    return files;
  }

  /**
   * Loads all file (and only file, not folder) that are in the folder pointed by the given URL
   *
   * @param url Url of a folder from which we want to load file
   * @return A list of all loaded file (and only file, not folder) from the pointed folder
   * @deprecated Does not work when using resources from an executable jar.
   */
  @Deprecated
  public final List<File> loadAllFilesFrom(final URL url) {
    return loadAllFilesFrom(url, f -> true);
  }

  /**
   * Convert the given string path to a usable URL
   *
   * @param path String relative path of the desired resource.
   * @return The URL of the desired resource..
   */
  private URL getResource(final String path) {
    return getClass().getClassLoader().getResource(path);
  }

  /**
   * Loads a File from the given path using an input stream conversion.
   *
   * @param path Path of the desired file.
   * @return A File object that retrieved from the given resource path.
   * @throws ResourceFileErrorException If the file cannot be loaded.
   */
  private File getFileFromIs(final String path) {
    try {
      final File result =
              new InputStreamToFile(getClass().getClassLoader().getResourceAsStream(path))
                      .convert()
                      .getFile();
      if (result == null) {
        throw new ResourceFileErrorException("NULL file result from input stream");
      }
      return result;
    } catch (IOException e) {
      throw new ResourceFileErrorException(e);
    }
  }
}