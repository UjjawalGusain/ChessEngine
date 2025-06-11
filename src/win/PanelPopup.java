package win;


import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelPopup extends JPanel {
	private static final long serialVersionUID = 1L;

	public PanelPopup(String message) {
		
		this.setBounds(900, 500, 500, 500);
		JLabel label = new JLabel(message);
		this.add(label);
		
		
	}
	
}
