package polytech.devint.controller.input;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Loris Friedel
 */
public class InputConfiguration {

  // TODO find a BETTER way to handle multiple configuration
  private final Map<String, IConfiguration> configMap;

  public InputConfiguration() {
    this.configMap = new HashMap<>();
  }

  /**
   * Find the config that has the given key in the config map.
   * @param key Key of the wanted configuration.
   * @param <C> Class of the desired configuration.
   * @return If present, the configuration casted to the right class that has the given key.
   * @throws ConfigNotFoundException If the configuration is not in the configurations map.
   */
  public <C extends IConfiguration> C getConfig(String key) {
    if(configMap.containsKey(key)) {
      return (C) configMap.get(key);
    }
    throw new ConfigNotFoundException(key);
  }

  /**
   * Add an input configuration in the map of all configurations.
   *
   * @param key Key that the configuration will have in the map.
   * @param configuration Configuration to be added.
   * @return The current object modified.
   */
  public InputConfiguration addConfig(String key, IConfiguration configuration) {
    configMap.put(key, configuration);
    return this;
  }
}
