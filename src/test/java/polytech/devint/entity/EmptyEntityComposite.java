package polytech.devint.entity;

import polytech.devint.model.Model;

/**
 * An empty composite entty for tetsing purpose
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class EmptyEntityComposite extends EntityComposite {

  public EmptyEntityComposite(Model model) {
    super(model);
  }

  public EmptyEntityComposite(Model model, EntityManager entityManager) {
    super(model, entityManager);
  }

  @Override
  public void updateComposite() {}

  @Override
  public void activate() {}

  @Override
  public void terminate() {}

}
