import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Project5 {
	public static void main(String[] args) {
		int[][] costMatrix;
		boolean[] marked;
		int[] bestCost;
		int[] parent;
		
		Scanner infile;
		int size;
		int source;
		
		try {
			infile = new Scanner(new File(args[0]));
			size = infile.nextInt();
			costMatrix = createMatrix(infile, size);
			
			source = Integer.parseInt(args[1]);
			
			marked = new boolean[size+1];
			bestCost = new int[size+1];
			parent = new int[size+1];
			
			for(int i = 1; i <= size; i++) {
				parent[i] = source;
				bestCost[i] = costMatrix[source][i];
			}
			
			int minNode = source;
			int newNode, tempCost;
			
			for (int iteration = 1; iteration <= size; iteration++) {
				int temp = 0;
				marked[minNode] = true;
				
				for(int i = 1; i <= size; i++) {	
					if(!marked[i]) {
						newNode = i;
						tempCost = bestCost[minNode] + costMatrix[minNode][newNode];
						if(tempCost < bestCost[newNode]) {
							bestCost[newNode] = tempCost;
							parent[newNode] = minNode;
						}
						temp = bestCost[i];
					}
				}
			
				for(int i = 1; i <= size; i++) {
					if(!marked[i] && bestCost[i] <= temp) {
						temp = bestCost[i];
						minNode = i;
					}
				}
			}
			
			infile = new Scanner(new File(args[0]));
			File outfile = new File(args[2]);
			print(infile, outfile, costMatrix, size, source, parent, bestCost);	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int[][] createMatrix(Scanner infile, int size) {
		int[][] costMatrix = new int[size+1][size+1];
		int endA, endB, cost;
		
		for(int i = 1; i <= size; i++) {
			for(int j = 1; j <= size; j++) {
				if(i == j)
					costMatrix[i][j] = 0;
				else
					costMatrix[i][j] = 999;
			}
		}
		
		while(infile.hasNext()) {
			endA = infile.nextInt();
			endB = infile.nextInt();
			cost = infile.nextInt();
			costMatrix[endA][endB] = cost;
		}	
		return costMatrix;
	}
	
	public static void print(Scanner infile, File outfile, int[][] costMatrix, int size, int source, int[] parent, int[] bestCost) {
		String ch;
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(outfile, true);
			
			infile.useDelimiter("");
			while(infile.hasNext()) {
				ch = infile.next();
				writer.write(ch);
			}
			writer.write(String.format("%n%s%n","Cost Matrix:"));
			
			for(int i = 1; i <= size; i++) {
				for(int j = 1; j <= size; j++) {
					if(costMatrix[i][j] == 999)
						writer.write(String.format("%4s","NA"));
					else
						writer.write(String.format("%4s",costMatrix[i][j]));
				}
				writer.write(System.getProperty("line.separator"));
			}	
			writer.write(String.format("%n%1s%d%n%n","Source Node: ",source));
			
			for(int i = 1; i <= size; i++) {
				writer.write(source + " to n" + i + " " + bestCost[i] + " and path: n" + i + " <-- ");
				int father = parent[i];
				
				while (father != source) {
					writer.write("n" + father + " <-- ");
					father = parent[father];
				}
				writer.write(String.format("%d%n", source));
			}	
			writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
}

	