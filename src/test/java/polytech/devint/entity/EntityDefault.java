package polytech.devint.entity;

import polytech.devint.model.Model;

/**
 * @author Loris Friedel
 */
public class EntityDefault extends Entity {

  public EntityDefault(Model model) {
    super(model);
  }

  int cptTest = 0;

  @Override
  public void activate() {
    setAlive();
  }

  @Override
  public void update() {
    cptTest++;
  }

  @Override
  public void terminate() {
    setDead();
  }
}
