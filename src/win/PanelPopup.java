package win;


import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PanelPopup extends JPanel {
	private static final long serialVersionUID = 1L;

	public PanelPopup(String message) {
		
		Font fo = new Font("DIALOG", Font.ITALIC, 30);
		JLabel label = new JLabel("<html>"+message+"</html>");
		label.setFont(fo);
		this.add(label);
		this.setBorder(new EmptyBorder(10, 50, 10, 50));
	}
	
}
