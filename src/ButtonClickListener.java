import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickListener implements ActionListener {

	// obsluguje przyciski z menu w MainWindow
	// ale juz nie dla wszystkich
	//bycmoze usune pozniej jesli nie bedzie miala sensu ta klasa
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
	
		if(command.equals("goHomeBtn")) {
			;
		}
		else if(command.equals("structBtn")) {
			;
		} 
		
	}
}
