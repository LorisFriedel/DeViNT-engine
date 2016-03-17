package polytech.devint.event.entity;

import polytech.devint.entity.World;

/**
 * Represents a world update event
 *
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 */
public class WorldUpdateEvent extends EntityEvent<World> {

  /**
   * Instantiates a new world update event
   *
   * @param world The world
   */
  public WorldUpdateEvent(World world) {
    super(world);
  }

}
