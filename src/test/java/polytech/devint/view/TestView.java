package polytech.devint.view;

import javax.swing.JFrame;

import polytech.devint.model.Model;
import polytech.devint.view.swing.SwingView;
import static org.mockito.Mockito.*;

public class TestView extends SwingView<Model> {

  public TestView() {
    super(mock(JFrame.class), new ContextHelp());
  }

  @Override
  public void setupContent() {

  }


  @Override
  public void destroyContent() {

  }


  @Override
  public void update() {

  }

  @Override
  public void initCustomContent() {

  }

  @Override
  public void destroyCustomContent() {

  }
}
