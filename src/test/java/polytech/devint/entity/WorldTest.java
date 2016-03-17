package polytech.devint.entity;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import polytech.devint.model.Model;

/**
 * Testing a world
 * 
 * @author Gunther Jungbluth (gunther.jungbluth.poirier@gmail.com)
 *
 */
public class WorldTest {

  /**
   * Testing if a world is properly terminating
   */
  @Test
  public void testTerminate() {
    Model model = new Model() {};
    World world = new World(model);
    EmptyComponent entity = spy(new EmptyComponent(model));
    world.addEntity(entity);
    world.terminate();
    verify(entity, times(1)).terminate();
  }

  /**
   * Testing if a world update is properly being cancelled
   */
  @Test
  public void testCancelUpdate() {
    Model model = new Model() {};
    World world = spy(new World(model));
    model.getEventManager().registerObserver(new AntiWorldUpdateEventObserver());
    world.update();
    verify(world, times(0)).updateWorld();
  }

  /**
   * Testing if a world update works liked expected
   */
  @Test
  public void testUpdate() {
    Model model = new Model() {};
    World world = spy(new World(model));
    world.update();
    verify(world, times(1)).updateWorld();
  }

}
