package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import input.InputProcessor;
/**
 * A class responsible for GUI
 * @author Pavel Straka
 */
public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;

	JTextField inputPath = new JTextField(20), outputPath = new JTextField(20);
	JButton openButton = new JButton("Vybrat"), saveButton = new JButton("Vybrat"), convertButton = new JButton("Konvertovat");

	public Gui() {
		// window title
		super("Konverze VFK do ISO 19152");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		addItem(panel1, new JLabel("Vstupní soubor (VFK):"), 0, 0, 1, 1, GridBagConstraints.EAST);
		addItem(panel1, new JLabel("Výstupní soubor (ISO 19152):"), 0, 1, 1, 1, GridBagConstraints.EAST);
		addItem(panel1, convertButton, 0, 2, 1, 1, GridBagConstraints.EAST);

		addItem(panel1, inputPath, 1, 0, 2, 1, GridBagConstraints.WEST);
		addItem(panel1, outputPath, 1, 1, 1, 1, GridBagConstraints.WEST);

		inputPath.setEditable(false);
		outputPath.setEditable(false);

		addItem(panel1, openButton, 2, 0, 1, 1, GridBagConstraints.WEST);
		addItem(panel1, saveButton, 2, 1, 1, 1, GridBagConstraints.WEST);

		openButton.addActionListener(new OpenL());
		saveButton.addActionListener(new SaveL());
		convertButton.addActionListener(new ConvertL());

		this.add(panel1);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	/**
	 * Adds components to panel
	 * @param p Jpanel
	 * @param c Jcomponent
	 * @param x x position
	 * @param y y position
	 * @param width
	 * @param height
	 * @param align
	 */
	private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth = width;
		gc.gridheight = height;
		gc.weightx = 100.0;
		gc.weighty = 100.0;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = align;
		gc.fill = GridBagConstraints.NONE;
		p.add(c, gc);
	}

	/**
	 * Handles the action for open button
	 *
	 */
	class OpenL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(Gui.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				inputPath.setText(c.getSelectedFile().getAbsolutePath());
			}
			/*if (rVal == JFileChooser.CANCEL_OPTION) {
	        inputPath.setText("You pressed cancel");
	      }*/
		}
	}

	/**
	 * Handles the action for save button
	 *
	 */
	class SaveL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showSaveDialog(Gui.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				outputPath.setText(c.getSelectedFile().getAbsolutePath());
			}
			/*if (rVal == JFileChooser.CANCEL_OPTION) {
	        outputPath.setText("You pressed cancel");
	      }*/
		}
	}

	/**
	 * Handles the action for convert button
	 *
	 */
	class ConvertL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!outputPath.getText().isEmpty() && !inputPath.getText().isEmpty()) {
				new InputProcessor(inputPath.getText(), outputPath.getText());
				JOptionPane.showMessageDialog(null, "Konverze provedena.");
				System.exit(0);
			}
			else {
				JOptionPane.showMessageDialog(null, "Zvolte prosím vstupní a výstupní soubor.");
			}
		}
	}
}