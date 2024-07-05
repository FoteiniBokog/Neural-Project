import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.util.*;

public class clusterPlot extends Frame {
	// The variables below are used to paint our points in a specific way so as to
	// ensure that our plot displays all the points.
	private static final long serialVersionUID = 1L;
	private double[][] x = KM.getTrainingData();
	private double[][] c = KM.getCentroids();
	private int[][] clusters = KM.getClusters();
	private int[] clusterCount = KM.getcluster_count();
	private Color[] colors = new Color[clusterCount.length]; // This is a random table of colors used to differentiate
																// our cluster groups.

	private int height = 800;
	private int width = 800;
	private double maxy = 0.0;
	private double maxx = 0.0;

	public clusterPlot(double[][] x, double[][] c, int[][] clusters, int[] clusterCount) {
		this.clusters = clusters;
		this.clusterCount = clusterCount;
		this.x = x;
		this.c = c;

		// This variable will be used to generate random colors for each cluster-group
		// this way we will be able to differentiate them in the plot.
		Random rand = new Random();
		for (int i = 0; i < colors.length; i++) {
			Color randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			colors[i] = randomColor;
		}
		calculateAxisMaximums(x);
		prepareGUI();
	}

	/**
	 * @calculateMaximums This function calculates the max values of our X/Y
	 *                    graph.They will be used to draw our axis within the
	 *                    calculated range.
	 */
	private void calculateAxisMaximums(double[][] x) {
		for (double[] i : x) {
			maxy = Math.max(maxy, i[1]);
			maxx = Math.max(maxx, i[0]);
		}
	}

	/**
	 * @prepareGUI This function simply creates the window in which we shall draw
	 *             our plot. Dimensions are set to 800x800
	 */
	private void prepareGUI() {
		setSize(1000, 820);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	/**
	 * @paint This runs for every frame.It re-paints our point coordinates and our
	 *        X-Y axis.Furthermore it draws the cluster groups centroid with '*' and
	 *        all the points that were assigned to it are drawn as '+' in the same
	 *        color. The color picked for each cluster group is assigned randomly.
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Serif", Font.PLAIN, 26);

		g2.setFont(font);
		g2.translate(0, getHeight());
		g2.setColor(Color.BLUE);

		for (int i = 0; i < clusterCount.length; i++) {
			for (int j = 0; j < clusterCount[i]; j++) {
				float X = (float) ((x[clusters[i][j]][0] + 0.03) * (width / maxx + 50));
				float Y = (float) ((x[clusters[i][j]][1] + 0.03) * (-height / maxy + 40));
				g2.setColor(colors[i]);
				g2.drawString("+", X, Y);
			}
		}

		for (int i = 0; i < c.length; i++) {
			float X = (float) ((c[i][0] + 0.03) * (width / maxx + 50));
			float Y = (float) ((c[i][1] + 0.03) * (-height / maxy + 40));
			g2.setColor(Color.BLUE);
			g2.drawString("*", X, Y);
		}
	}
}
