package polytech.devint.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Antoine Aubé (aube.antoine@gmail.com)
 */
public abstract class ConfigurationLoader {

  private final InputStream configurationFile;

  /**
   * Initiate a configuration loader
   *
   * @param configurationFile
   */
  public ConfigurationLoader(InputStream configurationFile) {
    this.configurationFile = configurationFile;
  }

  /**
   * @return the JSONObject containing the content of the loader
   * @throws JSONException
   * @throws IOException
   */
  public JSONObject getContentAsJSONObject() throws JSONException, IOException {
    return new JSONObject(IOUtils.toString(configurationFile));
  }

  public InputStream getConfigurationFile() {
    return configurationFile;
  }
}
