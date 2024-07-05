import java.io.FileWriter;
import java.io.IOException;

public class create {
	public static void main(String[] args) throws IOException {
		FileWriter dataWriter = new FileWriter("groupData.csv");
		// 1st dataset [0.75,1.25]x[0.75,1.25]
		for (int i = 0; i < 150; i++) {
			float x1 = (float) (0.75f + Math.random() * (1.25f - 0.75f));
			float x2 = (float) (0.75f + Math.random() * (1.25f - 0.75f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 2nd dataset [0,0.5]x[0,0.5]
		for (int i = 0; i < 150; i++) {

			float x1 = (float) (0.0f + Math.random() * (0.5f - 0.0f));
			float x2 = (float) (0.0f + Math.random() * (0.5f - 0.0f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 3rd dataset [0,0.5] x [1.5,2]
		for (int i = 0; i < 150; i++) {
			float x1 = (float) (0.0f + Math.random() * (0.5f - 0.0f));
			float x2 = (float) (1.5f + Math.random() * (2.0f - 1.5f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 4th dataset [1.5,2]x[0,0.5]
		for (int i = 0; i < 150; i++) {
			float x1 = (float) (1.5f + Math.random() * (2.0f - 1.5f));
			float x2 = (float) (0.0f + Math.random() * (0.5f - 0.0f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 5th dataset [1.5,2] x [1.5,2]
		for (int i = 0; i < 150; i++) {
			float x1 = (float) (1.5f + Math.random() * (2.0f - 1.5f));
			float x2 = (float) (1.5f + Math.random() * (2.0f - 1.5f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 6th dataset [0.6,0.8]x[0,0.4]
		for (int i = 0; i < 75; i++) {
			float x1 = (float) (0.0f + Math.random() * (0.8f - 0.6f));
			float x2 = (float) (0.0f + Math.random() * (0.4f - 0.0f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 7th dataset [0.6,0.8]x[1.6,2.0]
		for (int i = 0; i < 75; i++) {
			float x1 = (float) (0.0f + Math.random() * (0.8f - 0.6f));
			float x2 = (float) (0.0f + Math.random() * (2.0f - 1.6f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 8th dataset [1.2,1.4]x[0.0,0.4]
		for (int i = 0; i < 75; i++) {
			float x1 = (float) (0.0f + Math.random() * (1.4f - 1.2f));
			float x2 = (float) (0.0f + Math.random() * (0.4f - 0.0f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 9th dataset [1.2,1.4]x[1.6,2.0]
		for (int i = 0; i < 75; i++) {
			float x1 = (float) (0.0f + Math.random() * (1.4f - 1.2f));
			float x2 = (float) (0.0f + Math.random() * (2.0f - 1.6f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		// 10th dataset [0.0,2.0]x[0.0,2.0]
		for (int i = 0; i < 150; i++) {
			float x1 = (float) (0.0f + Math.random() * (2.0f - 0.0f));
			float x2 = (float) (0.0f + Math.random() * (2.0f - 0.0f));
			dataWriter.append(x1 + "," + x2 + "\n");
		}
		
		
		dataWriter.flush();
		dataWriter.close();
	}
}
