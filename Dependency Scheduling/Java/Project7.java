import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Project7 {
	
	public static int[] createJobTime(Scanner infile, int numberNodes) {
		int[] jobTime = new int[numberNodes+1];
		int index;
		int time;
		
		while(infile.hasNext()) {
			index = infile.nextInt();
			time = infile.nextInt();
			jobTime[index] = time;
		}
		return jobTime;
	}
	
	public static int addTime(int[] jobTime, int numberNodes) {
		int totalTime = 0;
		for(int i = 1; i <= numberNodes; i++) {
			totalTime+= jobTime[i];
		}
		return totalTime;
	}
	
	public static LinkedList[] createHashTable(Scanner infile, int[] parentCount) {
		int size;
		size = infile.nextInt();
		LinkedList[] hashTable = new LinkedList[size+1];
		for(int i = 0; i < (size+1); i++) {
			hashTable[i] = new LinkedList();
		}
		int nodeA, nodeB;
		while(infile.hasNext()) {
			nodeA = infile.nextInt();
			nodeB = infile.nextInt();
			Node newNode = new Node(nodeB);
			hashTable[nodeA].append(newNode);
			parentCount[nodeB]++;
		}
		return hashTable;
	}
	
	public static boolean remainingJob(boolean[] jobDone, int numberNodes) {
		for(int i = 1; i <= numberNodes; i++) {
			if(jobDone[i] == false)
					return true;
		}
		return false;
	}
	
	public static int findAvailProc(int[] processJob, int numberNodes) {
		for(int i = 1; i <= numberNodes; i++) {
			if(processJob[i] == 0)
				return i;
		}
		return 0;
	}
	
	public static void updateSchedule(int[][] scheduleTable, int time, Node newJob, int availProc) {
		for(int i = time; i < time + newJob.time; i++) {
			scheduleTable[availProc][i] = newJob.jobId;
		}
	}
	
	public static boolean empty(int[] parentCount, int numberNodes) {
		for(int i = 1; i <= numberNodes; i++) {
			if(parentCount[i] != 0) 
				return false;
		}
		return true;
	}
	
	public static void debugToConsole(int time, int procUsed, int[] processJob, int[] processTime, int[] parentCount, int[] jobTime, boolean[] jobDone, boolean[] jobMarked, int numberNodes) {
		System.out.println("Current Time: " + time);
		System.out.println("Processors Used: " + procUsed);
		System.out.print("Process Jobs: ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", processJob[i]));
		}
		System.out.print("\nProcess Time: ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", processTime[i]));
		}
		System.out.print("\nParent Count: ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", parentCount[i]));
		}
		System.out.print("\nJob Time:     ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", jobTime[i]));
		}
		System.out.print("\nJob Done:     ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", jobDone[i]));
		}  
		System.out.print("\nJob Marked:   ");
		for(int i = 1; i <= numberNodes; i++) {
			System.out.print(String.format("%6s", jobMarked[i]));
		}
		System.out.print("\n\n");
	}
	
	public static void deleteEdges(LinkedList[] hashTable, int[] parentCount, int job, int numberNodes) {
		Node walker = hashTable[job].getHead().next;
		while(walker != null) {
			parentCount[walker.jobId]--;
			walker = walker.next;
		}
	}
	
	public static void print(Scanner infile1, Scanner infile2, File outfile, int procNeed) {
		String ch;
		FileWriter writer = null;
		try {
			writer = new FileWriter(outfile, true);
			
			infile1.useDelimiter("");
			while(infile1.hasNext()) {
				ch = infile1.next();
				writer.write(ch);
			}
			writer.write(System.getProperty("line.separator"));
			infile2.useDelimiter("");
			while(infile2.hasNext()) {
				ch = infile2.next();
				writer.write(ch);
			}
			writer.write(String.format("%nProcessors Need:%d ", procNeed));
			writer.write(System.getProperty("line.separator"));
			writer.write(System.getProperty("line.separator"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
	
	public static void printSchedule(int[][] scheduleTable, File outfile, int procNeed, int time) {
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(outfile, true);
			writer.write(String.format("Schedule:	Time = %d ", time));
			writer.write(System.getProperty("line.separator"));
			for(int i = 1; i <= procNeed; i++) {
				for(int j = 0; j < time; j++) {
					writer.write(String.format("%3s",scheduleTable[i][j]));
				}
				writer.write(System.getProperty("line.separator"));
			}
			writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
	
	public static void main(String[] args) {
		int[][] scheduleTable;
		LinkedList OPEN = new LinkedList();
		LinkedList[] hashTable;
		int[] processJob;
		int[] processTime;
		int[] parentCount;
		int[] jobTime;
		boolean[] jobDone;
		boolean[] jobMarked;
		
		Scanner infile;
		try {
			infile = new Scanner(new File(args[1]));
			int numberNodes;
			numberNodes = infile.nextInt();
			jobTime = createJobTime(infile, numberNodes);
			
			int totalTime = addTime(jobTime, numberNodes);
			scheduleTable = new int[numberNodes+1][totalTime];
			
			processJob = new int[numberNodes+1];
			processTime = new int[numberNodes+1];
			parentCount = new int[numberNodes+1];
			jobDone = new boolean[numberNodes+1];
			jobMarked = new boolean[numberNodes+1];
			infile = new Scanner(new File(args[0]));
			hashTable = createHashTable(infile, parentCount);
			
			File outfile = new File(args[3]);
			
			int procNeed = Integer.parseInt(args[2]);
			
			Scanner infile1 = new Scanner(new File(args[0]));
			Scanner infile2 = new Scanner(new File(args[1]));
			print(infile1, infile2, outfile ,procNeed);
			
			if(procNeed > numberNodes)
				procNeed = numberNodes;
			
			int procUsed = 0;
			int time = 0;
			
			while(remainingJob(jobDone, numberNodes)) {
				for(int i = 1; i <= numberNodes; i++) {
					if(!jobMarked[i] && parentCount[i] == 0) {
						int orphanNode = i;
						jobMarked[i] = true;
						Node newNode = new Node(orphanNode, jobTime[orphanNode]);
						OPEN.append(newNode);
					}
				}
				
				while(!OPEN.isEmpty() && procUsed < procNeed) {
					int availProc;
					procUsed++;
					if(procUsed <= procNeed) {
						Node newJob = OPEN.remove();
						availProc = findAvailProc(processJob, procNeed);
						processJob[availProc] = newJob.jobId;
						processTime[availProc] = newJob.time;
						updateSchedule(scheduleTable, time, newJob, availProc);
					}
				}
				
				if(OPEN.isEmpty() && !empty(parentCount, numberNodes) && empty(processJob, numberNodes)) {
					System.out.println("There is a cycle in the graph.");
					System.exit(0);
				}
					
				debugToConsole(time, procUsed, processJob, processTime, parentCount, jobTime, jobDone, jobMarked, numberNodes);
					
				time++;
					
				for(int i = 1; i <= numberNodes; i++) {
					processTime[i]--;
					if(processTime[i] == 0) {
						procUsed--;
						int done = i;
						int job = processJob[done];
						processJob[done] = 0;
						jobDone[job] = true;
						deleteEdges(hashTable, parentCount, job, numberNodes);
					}
				}
				
				debugToConsole(time, procUsed, processJob, processTime, parentCount, jobTime, jobDone, jobMarked, numberNodes);
			}
			
			printSchedule(scheduleTable, outfile, procNeed, time);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

