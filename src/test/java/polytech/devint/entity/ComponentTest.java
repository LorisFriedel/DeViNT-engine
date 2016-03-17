package polytech.devint.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import polytech.devint.model.Model;

public class ComponentTest {

  /**
   * Testing if the component has been added to the obvserver list
   */
  @Test
  public void test() {
    Model model = new Model() {};
    Component component = new EmptyComponent(model);
    assertTrue(model.getEventManager().hasObserver(component));
  }

  /**
   * Testing the update process on a component
   */
  @Test
  public void testUpdate() {
    Model model = new Model() {};
    Component component = spy(new EmptyComponent(model));
    Entity entity = mock(Entity.class);
    when(entity.isAlive()).thenReturn(true).thenReturn(false);
    component.addEntity(entity);
    component.update();
    component.removeEntity(entity);
    component.update();
    verify(component, times(2)).updateComponent();
    verify(entity, times(1)).update();
  }

  /**
   * Testing if a component properly cancels its update from a composite event cancel
   */
  @Test
  public void testUpdateCancelledFromComposite() {
    Model model = new Model() {};
    Component component = spy(new EmptyComponent(model));
    model.getEventManager().registerObserver(new AntiCompositeUpdateEventObserver());
    component.update();
    verify(component, times(0)).updateComponent();
    verify(component, times(0)).updateComposite();
  }

  /**
   * Testing if a component properly cancels its update from a component event cancel
   */
  @Test
  public void testUpdateCancelledFromComponent() {
    Model model = new Model() {};
    Component component = spy(new EmptyComponent(model));
    model.getEventManager().registerObserver(new AntiComponentUpdateEventObserver());
    component.update();
    verify(component, times(0)).updateComponent();
    verify(component, times(1)).updateComposite();
  }

  /**
   * Testing if a component properly terminates
   */
  @Test
  public void testTerminate() {
    Model model = new Model() {};
    Component component = spy(new EmptyComponent(model));
    model.getEventManager().registerObserver(new AntiCompositeUpdateEventObserver());
    component.terminate();
    verify(component, times(1)).onRemove();
    assertFalse(model.getEventManager().hasObserver(component));
  }

  /**
   * Testing if a component properly cancel it's termination after an event cancel
   */
  @Test
  public void testCancelTerminate() {
    Model model = new Model() {};
    Component component = spy(new EmptyComponent(model));
    model.getEventManager().registerObserver(new AntiComponentRemoveEventObserver());
    component.terminate();
    verify(component, times(0)).onRemove();
  }

}
