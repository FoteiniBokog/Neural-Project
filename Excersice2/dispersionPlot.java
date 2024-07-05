import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class dispersionPlot extends Frame {

	
	private static final long serialVersionUID = 1L;
	private double[][] x = KM.getDispersionData();
	private int height = 800;
	private int width = 800;
	private double max_y = 0.0;
	private double max_x = 0.0;
	private float ypoint = -width / 70;
	private float xpoint = height / 90;
	private float yAxis = ypoint - 19;
	private float xAxis = xpoint + 38;

	public dispersionPlot(double[][] x) {
		this.x = x;

		calculateAxisMaximums(x);
		prepareGUI();
	}

	
	private void calculateAxisMaximums(double[][] x) {
		for (double[] i : x) {
			max_y = Math.max(max_y, i[1]);
			max_x = Math.max(max_x, i[0]);
		}
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
		Graphics2D graph= (Graphics2D) g;
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Serif", Font.PLAIN, 12);// This is our font.

		graph.setFont(font);
		graph.translate(0, getHeight());
		graph.setColor(Color.BLUE);
		boolean zigZag = false;

		for (int i = 0; i < x.length; i++) {
			zigZag = !zigZag;
			float X = (float) (x[i][0] * (width / (max_x + 1)));
			float Y = -(float) ((x[i][1]) / (height /max_y));

			graph.drawString("*", X, Y);

			graph.drawString(String.valueOf((int) x[i][0]), X, ypoint);
			graph.drawString("|", X + 2, yAxis);

			if (zigZag) {
				graph.drawString(String.valueOf((int) x[i][1]), xpoint, Y);
			} else {
				graph.drawString(String.valueOf((int) x[i][1]), xpoint + 20, Y);
			}

			graph.drawString("__", xAxis, Y - 8);

		}
		// Draw x-axis
		graph.setColor(Color.BLACK);

		for (int x = 0; x < width - 20; x += 6) {
			graph.drawString("_", x, yAxis - 4.5f);
		}
		graph.drawString(">", width - 22, yAxis + 2);

		// Draw y-axis
		for (int y = 0; y < height - 66; y += 11) {
			graph.drawString("|", xAxis, -y);
		}
		graph.drawString("^", xAxis - 2.51f, -(height - 74));

		Font axisFont = new Font("Serif", Font.PLAIN, 18);

		graph.setFont(axisFont);
		graph.drawString("Dispersion Error", 10, -height + 50);
		graph.drawString("M", width - 35, -17);
	}

}
