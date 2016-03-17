package polytech.devint.entity;

import polytech.devint.model.Model;

/**
 * An empty component used for testing
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class EmptyComponent extends Component {

  public EmptyComponent(Model model) {
    super(model);
  }

  @Override
  public void onRemove() {}

  @Override
  public void updateComponent() {}

  @Override
  public void activate() {}

}
