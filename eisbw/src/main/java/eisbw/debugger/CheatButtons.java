package eisbw.debugger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Danny & Harm.
 */
public class CheatButtons extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final List<String> actions = new LinkedList<>();
	private final Color buttonBackground;

	/**
	 * Constructor for buttons that inject cheats into the game.
	 */
	public CheatButtons() {
		setLayout(new BorderLayout());

		JLabel label = new JLabel("Cheat actions", SwingConstants.CENTER);
		add(label, BorderLayout.NORTH);

		JButton resources = new JButton("Give resources");
		resources.setActionCommand("show me the money");
		resources.addActionListener(this);

		JButton mapview = new JButton("Show map");
		mapview.setActionCommand("black sheep wall");
		mapview.addActionListener(this);

		JButton godmode = new JButton("Enemy attacks deal 0 damage");
		godmode.setActionCommand("power overwhelming");
		godmode.addActionListener(this);

		JButton buildfaster = new JButton("Reduce build times");
		buildfaster.setActionCommand("operation cwal");
		buildfaster.addActionListener(this);

		JButton norestrictions = new JButton("No tech restrictions");
		norestrictions.setActionCommand("modify the phase variance");
		norestrictions.addActionListener(this);

		JButton nosupplycap = new JButton("No supply cap");
		nosupplycap.setActionCommand("food for thought");
		nosupplycap.addActionListener(this);

		this.buttonBackground = resources.getBackground();

		JPanel cheatbuttons1 = new JPanel();
		cheatbuttons1.add(resources);
		cheatbuttons1.add(godmode);
		cheatbuttons1.add(mapview);
		JPanel cheatbuttons2 = new JPanel();
		cheatbuttons2.add(buildfaster);
		cheatbuttons2.add(norestrictions);
		cheatbuttons2.add(nosupplycap);

		add(cheatbuttons1, BorderLayout.CENTER);
		add(cheatbuttons2, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.actions.add(event.getActionCommand());
		JButton buttonPressed = (JButton) event.getSource();
		if (buttonPressed.getBackground().equals(Color.GRAY)) {
			buttonPressed.setBackground(this.buttonBackground);
		} else {
			buttonPressed.setBackground(Color.GRAY);
		}
	}

	public List<String> getActions() {
		return new LinkedList<>(this.actions);
	}

	/**
	 * Clean the actionqueue.
	 */
	public void clean() {
		this.actions.clear();
	}
}
