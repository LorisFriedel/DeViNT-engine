package polytech.devint.util.file;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * @author Loris Friedel
 */
public class InputStreamToFile {

  private static final String PREFIX = "is_file_tmp";
  private static final String SUFFIX = ".tmp";

  private final InputStream inputStream;
  private File file;

  public InputStreamToFile(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStreamToFile convert() throws IOException {
    final File tempFile = File.createTempFile(PREFIX, SUFFIX);
    tempFile.deleteOnExit();
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(inputStream, out);
    }
    this.file = tempFile;
    return this;
  }

  public File getFile() {
    return file;
  }
}
