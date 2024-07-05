import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;

public class KM {
	public static int M;// Number of Cluster groups
	public static int maxIterations = 10000; // Max epoxes
	public static int similarity_count;
	public static int loops_count;
	public static double[][] trainingData = new double[1200][2];// This table contains the data(X1,X2) that we read from our CSV
	public static double[][] centroids;// K centoids with x1,x2 values as coordinates in a 2d system
	public static int[] tests = { 3, 5, 7, 9, 11, 13 }; //our test Clusters assigned to variable M
															
	public static double[][] totalDispersions = new double[tests.length][2];//for the plot of the "M/Dispersion" diagram.


	public static double[][] cpy_centroids;
	public static int[][] clusters;
	public static int[] cluster_count;

	public static double[][] best_centroids;
	public static int[][] bestClusters;
	public static int[] bestcluster_count;

	public static double[][] final_centroids;
	public static int[][] final_clusters;
	public static int[] finalcluster_count;
	
	
	public static void main(String[] args) throws IOException {
		 double best_dispersion;
		 double min_distance_dispersion;
	     double final_dispersion = Double.MAX_VALUE;
	
		readFile(trainingData);
		System.out.println(" KM Started\n");
		for (int j = 0; j < tests.length; j++) {
			M = tests[j];
			best_dispersion = Double.MAX_VALUE;
			//here we set the varianbles
			best_centroids = new double[M][2];
			cpy_centroids = new double[M][2];
			clusters = new int[M][1200];
			cluster_count = new int[M];
			centroids = new double[M][2];
			bestClusters = new int[M][1200];
			bestcluster_count = new int[M];
			
			for (int i = 0; i < 20; i++) {
				loops_count = 0;
				similarity_count = 0;
				setCentroids(centroids);
				while (check_loops()==1) {
					find_minEuDistance(trainingData);
					adjustCentroids(centroids);
					
				}
				min_distance_dispersion = findDispersionError();
				if (min_distance_dispersion < best_dispersion) {
					best_dispersion = min_distance_dispersion;
					
					bestClusters = deepCopy(clusters);
					bestcluster_count = Arrays.copyOfRange(cluster_count, 0, cluster_count.length);
					best_centroids = deepCopy(centroids);
				}
			}
			
			totalDispersions[j][0] = M;
			totalDispersions[j][1] = best_dispersion;

			
			if (best_dispersion < final_dispersion) {

				final_clusters = new int[M][1200];
				final_centroids = new double[M][2];
				finalcluster_count = new int[M];
				final_clusters = deepCopy(bestClusters);
				final_centroids = deepCopy(best_centroids);
				finalcluster_count = Arrays.copyOfRange(bestcluster_count, 0, bestcluster_count.length);
				final_dispersion = best_dispersion;
			}
		}

		
		clusterPlot plotObj = new clusterPlot(trainingData, final_centroids, final_clusters, finalcluster_count);
		plotObj.setVisible(true);
		plotObj.setResizable(false);
		System.out.println("Stopped at: " + loops_count + " iterations(epoxes).");
		print(tests, totalDispersions);
		System.out.println("The lowest dispersion found is: [" + final_dispersion + "]");

		
		dispersionPlot dispersionsPlot = new dispersionPlot(totalDispersions);
		dispersionsPlot.setVisible(true);
		dispersionsPlot.setResizable(false);
	}
	
	public static void readFile(double[][] trainingData2) throws IOException {
		
		BufferedReader csvReader = new BufferedReader(new FileReader("groupData.csv"));
		for (int i = 0; i < 1200; i++) {
			String row = csvReader.readLine();
			String[] readcsv = row.split(",");
			trainingData2[i][0] = Double.parseDouble(readcsv[0]);
			trainingData2[i][1] = Double.parseDouble(readcsv[1]);
		}
		csvReader.close();
	}
	public static void setCentroids(double[][] centroids) {
		Random rand = new Random();
		for (int i = 0; i < M; i++) {
			int r = rand.nextInt(600);
			// Neuron X1,X2
			centroids[i][0] = trainingData[r][0];
			centroids[i][1] = trainingData[r][1];
		}
	}
	
	public static void find_minEuDistance(double[][] trainingData) {
		double distance, min_distance;
		int winner_neuron;
		double a,b,c,d;
		
		for (int i = 0; i < M; i++) {
			cluster_count[i] = 0;
		}
		for (int i = 0; i < trainingData.length; i++) {
			a=Math.abs((trainingData[i][0] - centroids[0][0]));
			b=Math.abs((trainingData[i][1] - centroids[0][1]));
			distance = Math.sqrt( Math.pow(a,2) + Math.pow(b,2));
			winner_neuron = 0;
			for (int j = 1; j < M; j++) {
				c=Math.abs((trainingData[i][0] - centroids[j][0]));
				d=Math.abs((trainingData[i][1] - centroids[j][1]));
				min_distance = Math.sqrt( Math.pow(c, 2) + Math.pow(d, 2) );
				if (min_distance < distance) {
					distance = min_distance;
					winner_neuron = j;
				}
			}
			// Connect the trainingData points to a centroid(winner_neuron).
			clusters[winner_neuron][cluster_count[winner_neuron]] = i;
			cluster_count[winner_neuron]++;
		}
	}

	public static void adjustCentroids(double[][] centroids) {
		double newX1, newX2;
		for (int i = 0; i < clusters.length; i++) {
			newX1 = 0.0;
			newX2 = 0.0;
			for (int j = 0; j < cluster_count[i]; j++) {
				newX1 += trainingData[clusters[i][j]][0];
				newX2 += trainingData[clusters[i][j]][1];
			}
			if (cluster_count[i] == 0) {
				centroids[i][0] = 0.0;
				centroids[i][1] = 0.0;
			} else {
				newX1 = newX1 / (double) cluster_count[i];
				newX2 = newX2 / (double) cluster_count[i];
				centroids[i][0] = newX1;
				centroids[i][1] = newX2;
			}

		}
	}
	public static int check_loops() {
		if (Arrays.deepEquals(cpy_centroids, centroids)) {
			similarity_count++;
		} else {
			similarity_count = 0;
			cpy_centroids = deepCopy(centroids);
		}
		if (similarity_count > 0 || loops_count == maxIterations) {
			return 0;
		}
		loops_count++;
		return 1;
	}

	public static double findDispersionError() {
		double distance;
		distance = 0.0;
		double w,q;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < cluster_count[i]; j++) {
				w=Math.abs(trainingData[clusters[i][j]][0] - centroids[i][0]);
				q=Math.abs(trainingData[clusters[i][j]][1] - centroids[i][1]);
				distance += Math.sqrt( Math.pow(w, 2)+ Math.pow(w, 2) );
			}
		}
		return distance;
	}

	// This is a print function for our Dispersion/M correlation.
	public static void print(int[] tests, double[][] matrix) {
		for (int i = 0; i < 6; i++) {
			System.out.println("Dispersion of: [" + matrix[i][1] + "] with M=" + tests[i] + ".");
		}
	}

	
	public static double[][] deepCopy(double[][] original) {

		final double[][] result = new double[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}

	
	public static int[][] deepCopy(int[][] original) {

		final int[][] result = new int[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}

	
	 //here we set the getters for the clusterPlot-dispersionPlot classes
	public static double[][] getTrainingData() {
		return trainingData;
	}

	public static double[][] getCentroids() {
		return centroids;
	}

	public static int[][] getClusters() {
		return clusters;
	}

	public static int[] getcluster_count() {
		return cluster_count;
	}

	public static double[][] getDispersionData() {
		return totalDispersions;
	}

}
