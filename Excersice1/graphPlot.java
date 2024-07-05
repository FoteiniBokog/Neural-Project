import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.*;

public class graphPlot extends Frame {

	private static final long serialVersionUID = 1L;
	private double[][] x;
	private boolean[] correct;

	private int height = 800;
	private int width = 800;

	public graphPlot(double[][] x, boolean[] correct) {
		this.x = x;
		this.correct = correct;
		prepareGUI();
	}

	private void prepareGUI() {
		setSize(800, 800);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Myfont1", Font.PLAIN, 14);

		// DRAW AXIS
		g2.setFont(font);
		g2.translate(0, getHeight());
		g2.setColor(Color.BLACK);
		g2.drawLine(width / 2, -0, width / 2, -height);
		g2.drawLine(0, -height / 2, width, -height / 2);

		for (float x = -1; x <= 1; x += 0.5) {
			if (x == 0) {
				continue;
			}
			g2.drawString(String.valueOf(x), (x + 1.1f) * width / 2.3f, -1.1f * (height) / (float) 2.3f);
			g2.drawString("|", (x + 1.1f) * width / 2.3f, -1.1f * (height) / (float) 2.3f - 0.015f * height);
		}
		for (float y = -1; y <= 1; y += 0.5) {

			if (y == 0) {
				continue;
			}
			g2.drawString(String.valueOf(y), 1.05f * width / 2.3f, -(y + 1.1f) * height / 2.3f);
			g2.drawString("_", 1.1f * width / 2.3f + 0.015f * width, -(y + 1.12f) * height / 2.3f);
		}

		// DRAW POINTS
		for (int i = 0; i < x.length; i++) {
			if (correct[i]) {
				g2.setColor(Color.BLUE);
				g2.drawString("+", (float) ((x[i][0] + 1.1) * width / 2.3), -(float) ((x[i][1] + 1.1) * height / 2.3));
			} else {
				g2.setColor(Color.RED);

				g2.drawString("-", (float) ((x[i][0] + 1.1) * width / 2.3), -(float) ((x[i][1] + 1.1) * height / 2.3));

			}
		}
	}

}
