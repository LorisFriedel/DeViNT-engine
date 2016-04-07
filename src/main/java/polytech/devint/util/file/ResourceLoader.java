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
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Loris Friedel
 */
public final class ResourceLoader {

  public ResourceLoader() {
    // does nothing
  }

  private String concat(String folder, String name) {
    return rectify(folder) + "/" + rectify(name);
  }

  private String rectify(String str) {
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

  private static boolean isValidPath(String path) {
    try {
      Paths.get(path);
    } catch (InvalidPathException | NullPointerException ex) {
      return false;
    }
    return true;
  }

  /**
   * Load the image icon from the given folder and with the given name
   *
   * @param folder folder (in the resource directory) that contains the file to load
   * @param name   name of the image file to load
   * @return an instance of the image icon corresponding to the loaded image file
   */
  public final ImageIcon loadImageIcon(String folder, String name) {
    try {
      return new ImageIcon(ImageIO.read(loadFileFrom(folder, name)));
    } catch (IOException e) {
      throw new ResourceFileErrorException(e);
    }
  }


  /**
   * Load the file that is in the given folder with the given name.
   *
   * @param folder Folder in which the file is.
   * @param name   Name of the file we want to load.
   * @return An instance of the desired file.
   */
  public final File loadFileFrom(String folder, String name) {
    return loadFileFrom(getClass().getClassLoader().getResource(concat(folder, name)));
  }

  /**
   * Load the file input stream from the file
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
   * Load all file (and only file, not folder) that are in the folder pointed by the given URL
   *
   * @param url Url of a folder from which we want to load file.
   * @return A list of all loaded file (and only file, not folder) from the pointed folder.
   */
  public final File loadFileFrom(URL url) {
    if (url == null) {
      throw new ResourceFileErrorException();
    }
    try {
      return new File(url.toURI());
    } catch (URISyntaxException e) {
      throw new ResourceFileErrorException(e);
    }
  }

  /**
   * Load all file (and only file, not folder) from the given resource folder path
   * and check if there is at least one file loader.
   * An exception is thrown if the given path doesn't lead to a folder.
   *
   * @param resourceFolderPath Path of the resource folder in which the file we want to load are.
   * @return A list of file, non-empty, that were in the folder pointed by the given path.
   */
  public List<File> loadAllFilesFrom(String resourceFolderPath) {
    /* Deprecated check
    if (files.isEmpty()) { throw new ResourceFileErrorException(); } */

    return loadAllFilesFrom(getClass().getClassLoader().getResource(rectify(resourceFolderPath)));
  }

  /**
   * Load all file (and only file, not folder) that are in the folder pointed by the given URL
   * and match the given predicate.
   * An exception is thrown if the given path doesn't lead to a folder.
   *
   * @param url       Url of a folder from which we want to load file.
   * @param predicate Predicate that will filter files.
   * @return A list of all loaded file (and only file, not folder) from the pointed folder.
   */
  public final List<File> loadAllFilesFrom(URL url, Predicate<File> predicate) {
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
   * Load all file (and only file, not folder) that are in the folder pointed by the given URL
   *
   * @param url Url of a folder from which we want to load file
   * @return A list of all loaded file (and only file, not folder) from the pointed folder
   */
  public final List<File> loadAllFilesFrom(URL url) {
    return loadAllFilesFrom(url, f -> true);
  }
}
