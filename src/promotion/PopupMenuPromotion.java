package promotion;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class PopupMenuPromotion extends JPopupMenu implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	public PopupMenuPromotion() {
		Font fo = new Font("DIALOG", Font.ITALIC, 30);
		JLabel label = new JLabel("Promote pawn to: ");
		label.setFont(fo);
		
		this.add(label);
		
		JMenuItem m1 = new JMenuItem("Queen");
        JMenuItem m2 = new JMenuItem("Rook");
        JMenuItem m3 = new JMenuItem("Bishop");
        JMenuItem m4 = new JMenuItem("Knight");
        
        this.setBorder(new EmptyBorder(10, 50, 10, 50));
        
        m1.addMouseListener(this);
        m2.addMouseListener(this);
        m3.addMouseListener(this);
        m4.addMouseListener(this);

        
        m1.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("He");
            }
            
            public void mousePressed(MouseEvent e) {}
        	public void mouseReleased(MouseEvent e) {}
        	public void mouseEntered(MouseEvent e) {}
        	public void mouseExited(MouseEvent e) {}
        });
        
        this.add(m1);
        this.add(m2);
        this.add(m3);
        this.add(m4);
        
        
	}
	
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		System.out.println("Hello");
//		System.out.println(e);
//    	JMenuItem item = (JMenuItem) e.getComponent();
//    	System.out.println(item);
//    }
//
//	public void mousePressed(MouseEvent e) {}
//	public void mouseReleased(MouseEvent e) {}
//	public void mouseEntered(MouseEvent e) {}
//	public void mouseExited(MouseEvent e) {}
//	
}
