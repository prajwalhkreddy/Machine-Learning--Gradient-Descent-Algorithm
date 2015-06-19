import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/* Prajwal Halasahally KeshavaReddy
 * Implementation of Gradient Descent Algorithm by training a single layer perceptron with sigmoid function
 * The program takes 4 inputs, the training file, test file, learning rate and number of iterations
 */
public class GradientDescent {

	int lineLength;
	int trainCounter = 0;
	int classifyCounter = 0;
	int weightCounter = 0;
	int neg = 0;
	int pos = 0;
	int neg1 = 0;
	int pos1 = 0;
	int instanceCount = 0;
	public static double learningRate = 0;
	public static int num;
	ArrayList<Double> weights = new ArrayList<Double>();

	// HashMaps to store outputlabels
	HashMap<Integer, Integer> resultTemp = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> resultTest = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

	// HashMaps to store trained perceptron output and test perceptron output
	static HashMap<Integer, Integer> perceptronOutput = new HashMap<Integer, Integer>();
	static HashMap<Integer, Integer> testOutput = new HashMap<Integer, Integer>();

	public static void main(String[] args) throws Exception {

		GradientDescent gd = new GradientDescent();
		learningRate = Double.parseDouble(args[2]);
		num = Integer.parseInt(args[3]);

		gd.readtrainData(args[0]);

		gd.readclassifyData(args[0]);
		gd.calcTestAccuracy(args[1]);

	}

	// Reading the training data row-wise and calling the training data function
	// for each row
	public int readtrainData(String filename) throws Exception {

		FileInputStream in = null;

		String input;
		int lineCounter = -1;

		try {

			File ipfile = new File(filename);

			in = new FileInputStream(ipfile);

		} catch (Exception e) {

			System.out.println("Not able to open file: " + filename + "\n" + e);
			return 0;

		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		while (instanceCount <= num) {
			instanceCount++;
			// System.out.println("I Count:"+instanceCount);

			if ((input = br.readLine()) != null) {

				lineCounter++;

				// checking for blank line
				if (lineCounter == 0) {
					br.mark(1000000);
					continue;
				} else {

					String temp[] = input.split("\\s");
					lineLength = temp.length;
					if (input.length() == 0) {
						continue;
					} else {
						// adding the output labels to a hash map
						resultTemp.put(lineCounter,
								Integer.parseInt(temp[lineLength - 1]));
					}

					// calling the train function

					trainData(temp, lineCounter);

				}

			} else {
				br.reset();
			}

		}
		// System.out.println(resultTemp);
		br.close();

		return 1;

	}

	// Reading the training data row-wise and calling the training data function
	// for each row to test the accuracy of trained data
	public int readclassifyData(String filename) throws Exception {

		// System.out.println("Classification");
		FileInputStream in = null;

		String input;
		int lineCounter = -1;

		try {

			File ipfile = new File(filename);

			in = new FileInputStream(ipfile);

		} catch (Exception e) {

			System.out.println("Not able to open file: " + filename + "\n" + e);
			return 0;

		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		double finalAccuracy = 0.0;
		while ((input = br.readLine()) != null) {

			lineCounter++;
			// checking for null input
			if (lineCounter == 0) {
				continue;
			} else {

				String temp[] = input.split("\\s");
				lineLength = temp.length;

				result.put(lineCounter, Integer.parseInt(temp[lineLength - 1]));

				// System.out.println("classifying");
				finalAccuracy = classifyTraining(temp, lineCounter);

			}

		}
		System.out.println("Train Data Accuracy:" + finalAccuracy + "%");
		// System.out.println("Perceptron Output: " + perceptronOutput);
		br.close();

		return 1;

	}

	// Calculating sigmoid function
	private double sigmoidFunction(double out) {

		double s = 1 / (1 + Math.exp(-out));
		return s;
	}

	// Initializing weights to 0 in the first run
	public void initializeWeights(double i) {

		for (int j = 0; j < lineLength - 1; j++)
			weights.add(i);

	}

	// Perceptron training
	public void trainData(String[] temp, int lineCounter) {

		double out = 0.0;

		// initial weight initialization to 0
		if (weightCounter == 0) {
			initializeWeights(0.0);
			weightCounter++;
		}

		// calculating the summation
		for (int i = 0; i < lineLength - 1; i++) {
			out = out + Double.parseDouble(temp[i]) * weights.get(i);

		}

		double sigmoid = sigmoidFunction(out);
		double error = resultTemp.get(lineCounter) - sigmoid;
		double derivative = sigmoid * (1 - sigmoid);

		double var = 0.0;

		// calculating the weights for each row and updating the weights
		for (int i = 0; i < lineLength - 1; i++) {
			var = weights.get(i)
					+ (learningRate * error * derivative * Double
							.parseDouble(temp[i]));

			weights.set(i, var);
		}

	}

	// Calculating the accuracy of the training set
	public double classifyTraining(String[] temp, int lineCounter) {
		double out = 0.0;

		for (int i = 0; i < lineLength - 1; i++) {
			out = out + Double.parseDouble(temp[i]) * weights.get(i);

		}

		double sigmoid = sigmoidFunction(out);

		// Applying the sigmoid threshold and then redirecting the output to a
		// hash map
		if (sigmoid < 0.5) {
			perceptronOutput.put(lineCounter, 0);
			if (result.get(lineCounter) == 0) {
				pos++;
			} else {
				neg++;
			}

		} else {
			perceptronOutput.put(lineCounter, 1);
			if (result.get(lineCounter) == 1) {
				pos++;
			} else {
				neg++;
			}

		}

		double accuracy = 0.00;
		accuracy = ((double) pos / (double) (lineCounter)) * 100;
		return accuracy;
	}

	// Reading the test file, using the weights from the trained perceptron and
	// calculating the accuracy
	public double calcTestAccuracy(String filename) throws Exception {

		FileInputStream in = null;

		String input;
		int lineCounter = -1;

		try {

			File ipfile = new File(filename);

			in = new FileInputStream(ipfile);

		} catch (Exception e) {

			System.out.println("Not able to open file: " + filename + "\n" + e);
			return 0;

		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		while ((input = br.readLine()) != null) {

			lineCounter++;

			if (lineCounter == 0) {
				continue;
			} else {

				String temp1[] = input.split("\\s");
				lineLength = temp1.length;

				if (input.length() == 0) {
					continue;
				} else {

					resultTest.put(lineCounter,
							Integer.parseInt(temp1[lineLength - 1]));
				}
				double out = 0.0;
				//Calculating the summation
				for (int i = 0; i < lineLength - 1; i++) {
					out = out + Double.parseDouble(temp1[i]) * weights.get(i);

				}

				double sigmoid = sigmoidFunction(out);

				// Applying the sigmoid threshold and re-directing the
				// respective outputs to a hashmap and calculating accuracy
				if (sigmoid < 0.5) {
					testOutput.put(lineCounter, 0);
					if (resultTest.get(lineCounter) == 0) {
						pos1++;
					} else {
						neg1++;
					}

				} else {
					testOutput.put(lineCounter, 1);
					if (resultTest.get(lineCounter) == 1) {
						pos1++;
					} else {
						neg1++;
					}

				}
				// System.out.println("Pos:" + pos + "Neg:" + neg);

			}

		}
		double accuracy = 0.00;
		accuracy = ((double) pos1 / (double) (lineCounter)) * 100;

		br.close();

		System.out.println("Test Data Accuracy:" + accuracy + "%");

		return 1;

	}

}
