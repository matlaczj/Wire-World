import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickListener implements ActionListener {

	// obsluguje przyciski z menu w MainWindow
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
	
		if(command.equals("goHomeBtn")) {
			;
		}
		else if(command.equals("structBtn")) {
			;
		} 
		else if(command.equals("startBtn")) { //wznowi/uruchomi okreslona gre w przeciwienstwie do pauzy
			if(MainWindow.chosenGame == C.GOL)
			{
				MainWindow.getGolAnimationTimer().start();
			}
			if(MainWindow.chosenGame == C.WW)
			{
				MainWindow.getWwAnimationTimer().start();
			}
		}
		else if(command.equals("pauseBtn")) { //zatrzyma wszystkie gry tak samo
				MainWindow.getGolAnimationTimer().stop();
				MainWindow.getWwAnimationTimer().stop();
		}
		
	}
}
