package eisbw.debugger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eisbw.Game;
import eisbw.debugger.draw.DrawMapInfo;
import eisbw.debugger.draw.DrawUnitInfo;
import eisbw.debugger.draw.IDraw;

/**
 * @author Danny & Harm.
 */
public class DrawButtons extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private final Map<String, IDraw> draw;
	private final Color buttonBackground;

	/**
	 * Toggle switches to draw in the game.
	 *
	 * @param game
	 *            - the game data.
	 */
	public DrawButtons(Game game) {
		setLayout(new BorderLayout());

		JLabel label = new JLabel("Draw actions", SwingConstants.CENTER);
		add(label, BorderLayout.NORTH);

		this.draw = new HashMap<>(2);
		this.draw.put(Draw.MAP.getName(), new DrawMapInfo(game));
		this.draw.put(Draw.UNITS.getName(), new DrawUnitInfo(game));

		JButton mapButton = new JButton("Map info");
		mapButton.setActionCommand(Draw.MAP.getName());
		mapButton.addActionListener(this);
		JButton unitButton = new JButton("Unit info");
		unitButton.setActionCommand(Draw.UNITS.getName());
		unitButton.addActionListener(this);

		this.buttonBackground = mapButton.getBackground();

		JPanel drawPanel = new JPanel();
		drawPanel.add(mapButton);
		drawPanel.add(unitButton);
		add(drawPanel);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.draw.get(event.getActionCommand()).toggle();
		JButton buttonPressed = (JButton) event.getSource();
		if (buttonPressed.getBackground().equals(Color.GRAY)) {
			buttonPressed.setBackground(this.buttonBackground);
		} else {
			buttonPressed.setBackground(Color.GRAY);
		}
	}

	/**
	 * Draw on screen.
	 *
	 * @param api
	 *            - the API.
	 * @throws NoTranslatorException
	 *             iff there is no translator.
	 * @throws TranslationException
	 *             iff translation fails.
	 */
	public void draw(bwapi.Game api) {
		for (Entry<String, IDraw> drawable : this.draw.entrySet()) {
			drawable.getValue().draw(api);
		}
	}
}
