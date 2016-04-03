package polytech.devint.controller.input;

/**
 * @author Loris Friedel
 */
public class ConfigNotFoundException extends RuntimeException {

  public ConfigNotFoundException(String key) {
    super(key);
  }
}
