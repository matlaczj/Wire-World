import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderChangeListener implements ChangeListener { //tylko dla 1 slidera zadzia�a bo inaczej nie b�dziemy wiedzie� z kt�rego bierze warto��
	@Override
	public void stateChanged(ChangeEvent e) {
		int currentSpeed = ((JSlider)e.getSource()).getValue();
		MainWindow.currentSpeedLabel.setText(String.valueOf(currentSpeed));
		
	}
	
}	
