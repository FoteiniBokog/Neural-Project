import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MLP2 {

	public static int loops = 0;
	public static double totalError = 0.0;
	public static double lastError = 1.0;
	
	//erwthma1
	public static int d = 2; // Number of Inputs
	public static int K = 4; // Number of categories
	public static int H1; // Hidden neurons in the first layer
	public static int H2; // Hidden neurons in the second layer
	public static int H3; // Hidden neurons in the third layer
	
	
	public static double learningRate = 0.01;
	public static double threshold = 0.0001;
	
	public static int mini_batches;//minibatches 
	public static int N = 4000; //N=4000

	public static int maxEpoxes = 1000;//Max iterations before accepting
	public static int minEpoxes = 700; // min iterations before accepting
	public static int average_loops = 1;

	// CHANGE THESE TO TEST THE MLP
	//if choose_func = true then use tanh
	//if choose_func = false then use relu
	public static boolean choose_func= true;
	public static int batches[] = {1,N/10,N/100,N}; //mini batches
	public static int neuronSets[][] = {{6,3,2},{7,4,3},{9,6,2}}; //number of neuron for every example

	


	public static double[][] trainingData = new double[N][3];//The 2d array containing our trainingSet
	public static double[][] testData = new double[N][3];//The 2d array containing our testSet

	//Weights for each of the 4 layers
	public static double[][] weights1;
	public static double[][] weights2;
	public static double[][] weights3;
	public static double[][] weights4;

	//Transposed weights for all layers
	public static double[][] weights_1transposed;
	public static double[][] weights_2transposed;
	public static double[][] weights_3transposed;
	public static double[][] weights_4transposed;

	//Bias per layer
	public static double[] bias1;
	public static double[] bias2;
	public static double[] bias3;
	public static double[] bias4;

	//Variables which are important for the backprpagation implementation
	public static double[] arrayOut1;
	public static double[] arrayOut2;
	public static double[] arrayOut3;
	public static double[] forward_Output;

	public static double[] gradientH1;
	public static double[] gradientH2;
	public static double[] gradientH3;
	public static double[] gradientOut;

	public static double[] error1;
	public static double[] error2;
	public static double[] error3;
	public static double[] error4;
	
	//The rate at which our weights will be updated
	public static double[][] delta1;
	public static double[][] delta2;
	public static double[][] delta3;
	public static double[][] delta4;

	public static double[] newBias1;
	public static double[] newBias2;
	public static double[] newBias3;
	public static double[] newBias4;

	//These variables below are used to obtain the data of the best H1,H2,Batch,activation-func combination.
	public static boolean[] tempCorrectElements = new boolean[N];
	public static boolean[] CorrectElements = new boolean[N];

	public static double average_Percentage = 0.0;
	public static double best_Percentage = 0.0;

	public static boolean best_choose_func = false;
	public static int best_H1 = 0;
	public static int best_H2 = 0;
	public static int best_H3 = 0;
	public static int best_Batches = 0;
	
	//for the range of [-1,1]
	public static double min=-1;
	public static double max=1;
	public static void main(String[] args) throws IOException {
		System.out.print("Start the program with three hidden layers!\n");
		readFiles(trainingData, testData);//Read our csv data
		//Proceed to test every example for a number of average loops in order to get an accurate average accuracy.
		for (int i = 0; i < neuronSets.length; i++) {
			for (int j = 0; j < batches.length; j++) {
				for (int w = 0; w < 3; w++) {
					for (int q = 0; q < average_loops; q++) {
						H1 = neuronSets[i][0];//Get the H1 neurons in the 1st layer
						H2 = neuronSets[i][1];//Get the H2 neurons in the 1st layer
						H3 =neuronSets[i][2];//Get the H3 neurons in the 1st layer
						mini_batches = batches[j];////Get the batches number
						
						initializer();//Initialize temporary vars needed
						
						//Randomize weights for every layer
						randWeights(weights1);
						randWeights(weights2);
						randWeights(weights3);
						randWeights(weights4);
						
						//Transpose weights for every layer of back prop
						weights_1transposed = transposeMatrix(weights1);
						weights_2transposed = transposeMatrix(weights2);
						weights_3transposed = transposeMatrix(weights3);
						weights_4transposed = transposeMatrix(weights4);

						//Randomize bias for every layer
						randBias(bias1);
						randBias(bias2);
						randBias(bias3);
						randBias(bias4);
						
						//Finally train our set
						train();
						//And test it
						test();
						loops = 0;
					}

					System.out.println("\nchoose_func=" + choose_func + "\n H1 = " + H1 + " H2 = "
							+ H2 +" H3 = "+ H3 +" and with a mini  batch of : " + mini_batches + " elements.");
					//Total error in the end of each epoch is printed here
					System.out.println("Total error: " + totalError / (double) N);
					
					System.out.println("Hit percentage is: " + average_Percentage / (double) average_loops);
					if (average_Percentage > best_Percentage) {
						best_Percentage = average_Percentage;
						CorrectElements = Arrays.copyOf(tempCorrectElements,N);
						best_choose_func = choose_func;
						best_H1 = H1;
						best_H2 = H2;
						best_H3 = H3;
						best_Batches = mini_batches; //minibacthes
					}

					choose_func = !choose_func;
					average_Percentage = 0;
				}
			}
		}
		//Best example found is printed here
		System.out.println("\nTHE GREATEST OF THEM ALL IS:");
		System.out.println("choose_func=" + best_choose_func + "\n with H1 = " + best_H1 + " H2 = "
				+ best_H2+" H3 = "+best_H3 + " and with a batch of : " + best_Batches + " elements.");
		//Total error of the best example is printed here
		System.out.println("Total error: " + totalError / (double) N);
		System.out.println("Hit percentage is: " + best_Percentage / (double) average_loops);
		
		//And is plotted here
		graphPlot plotObj = new graphPlot(testData, CorrectElements);
		plotObj.setVisible(true);
		plotObj.setResizable(false);
	}
	public static void test() {
		//The output of each layer
		arrayOut1 = new double[H1];
		arrayOut2 = new double[H2];
		arrayOut3 = new double[H3];
		forward_Output = new double[K + 1];

		int correct = 0;//Hit-ratio counter
		for (int i = 0; i < N; i++) {
			//start by filling each output array with 0 values
			Arrays.fill(arrayOut1, 0, arrayOut1.length, 0.0);
			Arrays.fill(arrayOut2, 0, arrayOut2.length, 0.0);
			Arrays.fill(arrayOut3, 0, arrayOut3.length, 0.0);
			Arrays.fill(forward_Output, 0, forward_Output.length, 0.0);
			int value = 0;//A temp value to find the maximum output
			
			forwardPass(testData[i],d,arrayOut1,arrayOut2,arrayOut3, forward_Output,K);//forwardpass
			//Get the maximum output value
			double max_out = forward_Output[0];
			for (int j = 1; j < K; j++) {
				if (max_out < forward_Output[j]) {
					max_out = forward_Output[j];
					value = j;
				}
			}

			if ((value + 1) == forward_Output[K]) {
				tempCorrectElements[i] = true;
				correct++;
			} else {
				tempCorrectElements[i] = false;
			}
		}

		//Calculate the training Accuracy
		average_Percentage += correct / (double) N;
	}
	 //This function returns a transposed matrix(2d-array)
	public static double[][] transposeMatrix(double[][] matrix) {
		double[][] temp = new double[matrix[0].length][matrix.length];
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				temp[j][i] = matrix[i][j];
		return temp;
	}
	 //This function initialized the arrays that are required for each and every different example.
	public static void initializer() {
		weights1 = new double[H1][d];
		weights2 = new double[H2][H1];
		weights3 = new double[H3][H2];
		weights4 = new double[K][H3];

		weights_1transposed = new double[d][H1];
		weights_2transposed = new double[H1][H2];
		weights_3transposed = new double[H3][H2];
		weights_4transposed = new double[H3][K];

		bias1 = new double[H1];
		bias2 = new double[H2];
		bias3 = new double[H3];
		bias4 = new double[K];

		// backprop
		arrayOut1 = new double[H1];
		arrayOut2 = new double[H2];
		arrayOut3 = new double[H3];
		forward_Output = new double[K + 1];

		gradientH1 = new double[H1];
		gradientH2 = new double[H2];
		gradientH3 = new double[H3];
		gradientOut = new double[K];

		error1 = new double[H1];
		error2 = new double[H2];
		error3 = new double[H3];
		error4 = new double[K];

		delta1 = new double[H1][d];
		delta2 = new double[H2][H1];
		delta3 = new double[H3][H2];
		delta4 = new double[K][H3];

		newBias1 = new double[H1];
		newBias2 = new double[H2];
		newBias3 = new double[H3];
		newBias4 = new double[K];
	}
	// This function returns a shuffled array in order to have a random sequence of inputs
	public static int[] shuffleArray() {
		int array[] = new int[N];
		for (int i = 0; i < N; i++) {
			array[i] = i;
		}

		Random rand = new Random();
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = (rand).nextInt(array.length);
			int temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		return array;
	}
	public static void train() {
		for (int i = 0; i < maxEpoxes; i++) {
			if (((Math.abs(lastError - totalError)) > threshold) || i < minEpoxes) {
				lastError = totalError;
			} else {
				return;
			}
			loops++;
			totalError = 0.0;
			int[] myArray = shuffleArray();

			for (int j = 0; j <= N - mini_batches; j += mini_batches) {
				zeroDeltas();//Reset deltas for every batch
				for (int k = j; k < j + mini_batches; k++) {
					backProp(trainingData[myArray[k]]);//back-prop

				}
				//Update our weights-bias using our deltas
				updateWeights();
				updateBias();
				resetBias();
			}
			
			totalError = totalError / (double) N;//calculate total error
			
		}

	}
	public static void backProp(double[] trainingData){
		
		resetVariables();
		
		forwardPass(trainingData,d,arrayOut1,arrayOut2,arrayOut3,forward_Output,K);
		
		// Calculate errors
		
		// error4 is error for the output layer
		for (int i = 0; i < K; i++) {
			if (forward_Output[K] != (i + 1)) {
				error4[i] = 0.0 - forward_Output[i];
			} else {
				error4[i] = 1.0 - forward_Output[i];
			}
		}
		for (int i = 0; i < K; i++) {
			totalError += Math.pow((error4[i]), 2) * 0.5;
		}
		
		// error3 is error for the third layer
		for (int i = 0; i < H3; i++) {
			for (int j = 0; j < K; j++) {
				error3[i] += weights_4transposed[i][j] * error4[j];
			}
		}

		// error2 is error for the 2nd layer
		for (int i = 0; i < H2; i++) {
			for (int j = 0; j < H3; j++) {
				error2[i] += weights_3transposed[i][j] * error3[j];
			}
		}
		// error1 is error for the 1st layer
		for (int i = 0; i < H1; i++) {
			for (int j = 0; j < H2; j++) {
				error1[i] += weights_2transposed[i][j] * error2[j];
			}
		}
		//Calculate gradients
		
		// Calculate gradient for the output layer
		for (int i = 0; i < K; i++) {
			gradientOut[i] = error4[i] * derivativeL(forward_Output[i]) * learningRate;
			newBias4[i] += gradientOut[i];
		}
		// Calculate gradient for the 3nd layer
		if (choose_func) {

			for (int i = 0; i < H3; i++) {
				gradientH3[i] = error3[i] * derTanh(arrayOut3[i]) * learningRate;
				newBias3[i] += gradientH3[i];
			}
		} else {
			for (int i = 0; i < H3; i++) {
				gradientH3[i] = error3[i] * (derRelu(arrayOut3[i])) * learningRate;
				newBias3[i] += gradientH3[i];
			}
		}
		
		// Calculate gradient for the 2nd layer
		if (choose_func) {

			for (int i = 0; i < H2; i++) {
				gradientH2[i] = error2[i] * derTanh(arrayOut2[i]) * learningRate;
				newBias2[i] += gradientH2[i];
			}
		} else {
			for (int i = 0; i < H2; i++) {
				gradientH2[i] = error2[i] * (derRelu(arrayOut2[i])) * learningRate;
				newBias2[i] += gradientH2[i];
			}
		}
		
		
		// Calculate gradient for the 1st layer
		for (int i = 0; i < H1; i++) {
			gradientH1[i] = error1[i] * derivativeL(arrayOut1[i]) * learningRate;
			newBias1[i] += gradientH1[i];
		}
		// Calculate Deltas
		// calculate delta for output layer
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < H3; j++) {

				delta4[i][j] += gradientOut[i] * arrayOut3[j];
			}
		}
		// calculate delta for 3nd layer
		for (int i = 0; i < H3; i++) {
			for (int j = 0; j < H2; j++) {

				delta3[i][j] += gradientH3[i] * arrayOut2[j];
			}
		}
		// calculate delta for 2nd layer
		for (int i = 0; i < H2; i++) {
			for (int j = 0; j < H1; j++) {

				delta2[i][j] += gradientH2[i] * arrayOut1[j];
			}
		}
		// calculate delta for 1st layer
		for (int i = 0; i < H1; i++) {
			for (int j = 0; j < d; j++) {

				delta1[i][j] += gradientH1[i] * trainingData[j];
			}
		}
	}
	//This function resets the bias arrays that are required for each example.
	public static void resetBias() {
		Arrays.fill(newBias1, 0, newBias1.length, 0.0);
		Arrays.fill(newBias2, 0, newBias2.length, 0.0);
		Arrays.fill(newBias3, 0, newBias3.length, 0.0);
		Arrays.fill(newBias4, 0, newBias4.length, 0.0);
	}
	
	// This updates our layer bias.
	public static void updateBias() 
	{
		// CHANGE BIAS FOR H1 LAYER
		for (int i = 0; i < H1; i++) {
			bias1[i] += newBias1[i];
		}
		// CHANGE BIAS FOR H2 LAYER
		for (int i = 0; i < H2; i++) {
			bias2[i] += newBias2[i];
		}
		// CHANGE BIAS FOR H3 LAYER
		for (int i = 0; i < H3; i++) {
			bias3[i] += newBias3[i];
		}
		// CHANGE BIAS FOR OUTPUT LAYER
		for (int i = 0; i < K; i++) {
			bias4[i] += newBias4[i];
		}
	}
	
	 // This function updates our weights using the previously calculated deltas.
	public static void updateWeights() {
		// CHANGE WEIGHTS FOR H1 LAYER
		for (int i = 0; i < H1; i++) {
			for (int j = 0; j < d; j++) {

				weights1[i][j] += delta1[i][j];
				weights_1transposed[j][i] += delta1[i][j];
			}
		}
		// CHANGE WEIGHTS FOR H2 LAYER
		for (int i = 0; i < H2; i++) {
			for (int j = 0; j < H1; j++) {
				weights2[i][j] += delta2[i][j];
				weights_2transposed[j][i] += delta2[i][j];
			}
		}
		// CHANGE WEIGHTS FOR H3 LAYER
		for (int i = 0; i < H3; i++) {
			for (int j = 0; j < H2; j++) {
				weights3[i][j] += delta3[i][j];
				weights_3transposed[j][i] += delta3[i][j];
			}
		}
		
		// CHANGE WEIGHTS FOR H1 LAYER
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < H3; j++) {
				weights4[i][j] += delta4[i][j];
				weights_4transposed[j][i] += delta4[i][j];

			}
		}
	}
	// This function resets the deltas arrays required for each example. 
	public static void zeroDeltas() {
		//zero(delta1);
		for (int i = 0; i < delta1.length; i++) {
			for (int j = 0; j < delta1[0].length; j++) {
				delta1[i][j] = 0.0;
			}

		}
		//zero(delta2);
		for (int i = 0; i < delta2.length; i++) {
			for (int j = 0; j < delta2[0].length; j++) {
				delta2[i][j] = 0.0;
			}

		}
		//zero(delta3);
		for (int i = 0; i < delta3.length; i++) {
			for (int j = 0; j < delta3[0].length; j++) {
				delta3[i][j] = 0.0;
			}

		}
		//zero(delta4);
		for (int i = 0; i < delta4.length; i++) {
			for (int j = 0; j < delta4[0].length; j++) {
				delta4[i][j] = 0.0;
			}

		}
	}
	//This function resets variables required for each example.
	public static void resetVariables() {
		Arrays.fill(error1, 0, error1.length, 0.0);
		Arrays.fill(error2, 0, error2.length, 0.0);
		Arrays.fill(error3, 0, error3.length, 0.0);
		Arrays.fill(error4, 0, error4.length, 0.0);

		Arrays.fill(arrayOut1, 0, arrayOut1.length, 0.0);
		Arrays.fill(arrayOut2, 0, arrayOut2.length, 0.0);
		Arrays.fill(arrayOut3, 0, arrayOut3.length, 0.0);
		Arrays.fill(forward_Output, 0, forward_Output.length, 0.0);
	}
	public static void forwardPass(double[] trainingData,int d,double[] arrayOut1,double[] arrayOut2,double[] arrayOut3,double[] forward_Output,int K) {

		//firstLayer(trainingData, arrayOut1);
		for (int i = 0; i < H1; i++) {
			for (int j = 0; j < d; j++) {
				arrayOut1[i] += trainingData[j] * weights1[i][j];
			}
			arrayOut1[i] += bias1[i];
			arrayOut1[i] = logistic(arrayOut1[i]);
		}
		//**************secondLayer(arrayOut1, arrayOut2);
		if (choose_func) {
			for (int i = 0; i < H2; i++) {
				for (int j = 0; j < H1; j++) {
					arrayOut2[i] += arrayOut1[j] * weights2[i][j];
				}
				arrayOut2[i] += bias2[i];
				arrayOut2[i] = Math.tanh(arrayOut2[i]);
			}
		} else {
			for (int i = 0; i < H2; i++) {
				for (int j = 0; j < H1; j++) {
					arrayOut2[i] += arrayOut1[j] * weights2[i][j];
				}
				arrayOut2[i] += bias2[i];
				arrayOut2[i] = relu(arrayOut2[i]);
			}
		}
		//**************thirdLayer(arrayOut3, arrayOut2);
		if (choose_func) {
			for (int i = 0; i < H3; i++) {
				for (int j = 0; j < H2; j++) {
					arrayOut3[i] += arrayOut2[j] * weights3[i][j];
				}
				arrayOut3[i] += bias3[i];
				arrayOut3[i] = Math.tanh(arrayOut3[i]);
			}
		} else {
			for (int i = 0; i < H3; i++) {
				for (int j = 0; j < H2; j++) {
					arrayOut3[i] += arrayOut2[j] * weights3[i][j];
				}
				arrayOut3[i] += bias3[i];
				arrayOut3[i] = relu(arrayOut3[i]);
			}
		}
		
		//*******************finalLayer(arrayOut3, fwpOutput);
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < H3; j++) {
				forward_Output[i] += arrayOut3[j] * weights4[i][j];
			}
			forward_Output[i] += bias4[i];
			forward_Output[i] = logistic(forward_Output[i]);
		}
		forward_Output[K] = trainingData[2];
	}
	
	// This function  prints a table. 
	public static void print(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
	}
	public static void readFiles(double[][] trainingData, double[][] testData) throws IOException {
		BufferedReader csvReader1 = new BufferedReader((new FileReader("trainingWriter.csv")));

		for (int i = 0; i < N; i++) {
			
			String row = csvReader1.readLine();
			String[] readcsv1 = row.split(",");
			trainingData[i][0] = Double.parseDouble(readcsv1[0]);
			trainingData[i][1] = Double.parseDouble(readcsv1[1]);
			trainingData[i][2] = Double.parseDouble(readcsv1[2]);
		}
		csvReader1.close();

		BufferedReader csvReader2 = new BufferedReader((new FileReader("trainingWriter.csv")));
		for (int i = 0; i < N; i++) {
			
			String row = csvReader2.readLine();
			String[] readcsv2 = row.split(",");
			testData[i][0] = Double.parseDouble(readcsv2[0]);
			testData[i][1] = Double.parseDouble(readcsv2[1]);
			testData[i][2] = Double.parseDouble(readcsv2[2]);
		}
		csvReader2.close();

	}
	// The activation/logistic function
	private static double logistic(double x) {

		return (1 / ((double) (1 + Math.exp(-x))));
	}
	
	// The tanh function
	public static double tanh(double number) {
		return Math.tanh(number);
	}

	//  Randomizes the weights with values in the range of [-1.1].
	private static void randWeights(double[][] weights) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				weights[i][j] = (min + Math.random() * (max - min));
			}

		}
	}
	
	//Randomizes the bias with values in the range of [-1.1].
	private static void randBias(double[] bias) {
		for (int i = 0; i < bias.length; i++) {
			bias[i] = (min + Math.random() * (max - min));
		}
	}
	
	//This function returns the derivative of tanh.
	public static double derTanh(double num) {
		return (1 - Math.pow(num, 2));
	}
	
	 //This function returns the derivative of logistic.
	public static double derivativeL(double num) {
		return num * (1 - num);
	}
	// Relu function
	public static double relu(double n){
		if(n>=0){
			return n;
		}else{
			return 0;
		}
	}
	//function that returns the derivative of relu
	public static double derRelu(double n){
		if(n>=0){
			return 1;
		}else{
			return 0;
		}
	}

}
	
	
	
	
	
	