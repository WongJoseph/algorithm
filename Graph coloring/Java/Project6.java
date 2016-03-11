import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Project6 {
	public static void main(String[] args) {
		int[][] adjacentMatrix;
		int[] nodeColor;
		
		Scanner infile;
		int size;
		
		try {
			infile = new Scanner(new File(args[0]));
			size = infile.nextInt();
			adjacentMatrix = createMatrix(infile, size);
			nodeColor = new int[size+1];
			
			int newColor = 0;
			int newNode;
			int index;
			
			while(checked(nodeColor, size)) {
				newColor++;
				
				for(index = 1; index <= size; index++) {
					if(nodeColor[index] == 0) {
						newNode = index;
						boolean sameColor = false;
						for(int i = 1; i <= size; i++) {
							if(adjacentMatrix[newNode][i] == 1 && nodeColor[i] == newColor)
								sameColor = true;
						}
						if(!sameColor)
							nodeColor[newNode] = newColor;
					}
					System.out.println(newColor + " " + index + " " + nodeColor[index]);
				}
			}
			
			for(int i = 1; i <= size; i++)
				adjacentMatrix[i][i] = nodeColor[i];
			
			infile = new Scanner(new File(args[0]));
			File outfile = new File(args[1]);
			print(infile, outfile, adjacentMatrix, newColor, size);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] createMatrix(Scanner infile, int size) {
		int[][] adjacentMatrix = new int[size+1][size+1];
		int endA, endB;
		
		while(infile.hasNext()) {
			endA = infile.nextInt();
			endB = infile.nextInt();
			adjacentMatrix[endA][endB] = 1;
		}	
		return adjacentMatrix;
	}
	
	public static boolean checked(int[] nodeColor, int size) {
		for(int i = 1; i <= size; i++)
			if(nodeColor[i] == 0)
				return true;
		return false;
	}
	
	public static void print(Scanner infile, File outfile, int[][] adjacentMatrix, int newColor, int size) {
		String ch;
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(outfile, true);
			
			infile.useDelimiter("");
			while(infile.hasNext()) {
				ch = infile.next();
				writer.write(ch);
			}
			writer.write(String.format("%n%n%s%n","Adjacent Matrix:"));
			
			for(int i = 1; i <= size; i++) {
				for(int j = 1; j <= size; j++) {
					writer.write(String.format("%3s",adjacentMatrix[i][j]));
				}
				writer.write(System.getProperty("line.separator"));
			}	
			writer.write(String.format("%n%1s%d%n","Number of color used: ",newColor));

		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
}
