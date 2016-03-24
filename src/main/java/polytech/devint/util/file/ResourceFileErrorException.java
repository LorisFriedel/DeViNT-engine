package polytech.devint.util.file;

/**
 * @author Loris Friedel
 */
public class ResourceFileErrorException extends RuntimeException {
  public ResourceFileErrorException(Throwable e) {
    super(e);
  }

  public ResourceFileErrorException() {
    super();
  }
}
