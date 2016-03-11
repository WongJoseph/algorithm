#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <iomanip> 

using namespace std;

class Node
{
	public:
	int jobId;
	int time;
	Node *next;
	Node(int id);
	Node(int id, int t);
};

Node::Node(int id) {
	jobId = id;
	next = NULL;
}

Node::Node(int id, int t)
{
	jobId = id;
	time = t;
	next = NULL;
}

class LinkedList
{
	public:	
		void append(Node *newNode);
		Node* remove();
		bool isEmpty();
		Node* getHead();
		LinkedList();
		~LinkedList();
	
	private:
		Node *listHead;
};

LinkedList::LinkedList()
{
	listHead = new Node(0,0);
}

LinkedList::~LinkedList() 
{
	Node *walker = listHead, *current = NULL;
	
	while(walker != NULL) {
		current = walker;
		walker = walker->next;
		delete(current);
	}
}

Node* LinkedList::getHead() {
	return listHead;
}

void LinkedList::append(Node *newNode) {
	Node *walker = listHead;
	while (walker->next != NULL) {
		walker = walker->next;
	}
	newNode->next = walker->next;
	walker->next = newNode;
}

Node *LinkedList::remove() {
	Node *job;
	job = listHead->next;
	listHead->next = job->next;
	return job;
}

bool LinkedList::isEmpty() {
	if(listHead->next == NULL)
		return true;
	else
		return false;
}

int *createJobTime(ifstream &infile, int numberNodes) {
	int *jobTime = new int[numberNodes+1];
	int index;
	int time;
	
	while(infile >> index >> time) {
		jobTime[index] = time;
	}
	return jobTime;
}

int addTime(int *jobTime, int numberNodes) {
	int totalTime = 0;
	for(int i = 1; i <= numberNodes; i++) {
		totalTime+= jobTime[i];
	}
	return totalTime;
}

int **createScheduleTable(int numberNodes, int totalTime) {
	int **scheduleTable= (int **)malloc((numberNodes+1) * sizeof(int *));
	
    for(int i = 0; i < (numberNodes+1); i++)
         scheduleTable[i] = (int *)malloc((totalTime) * sizeof(int));
	 
	for(int i = 1; i <= numberNodes; i++) {
		for(int j = 0; j < totalTime; j++) {
				scheduleTable[i][j] = 0;
		}
	}
	return scheduleTable;
}

LinkedList **createHashTable(ifstream &infile, int *parentCount) {
	int size;
	infile >> size;
	LinkedList **hashTable = new LinkedList*[size+1];
	for(int i = 0; i < (size+1); i++) {
		hashTable[i] = new LinkedList;
	}
	int nodeA, nodeB;
	while(infile >> nodeA >> nodeB) {
		Node *newNode = new Node(nodeB);
		hashTable[nodeA]->append(newNode);
		parentCount[nodeB]++;
	}
	return hashTable;
}

bool remainingJob(bool *jobDone, int numberNodes) {
	for(int i = 1; i <= numberNodes; i++) {
		if(jobDone[i] == false)
			return true;
	}
	return false;
}

int findAvailProc(int *processJob, int numberNodes) {
	for(int i = 1; i <= numberNodes; i++) {
		if(processJob[i] == 0)
			return i;
	}
}

void updateSchedule(int **scheduleTable, int time, Node* newJob, int availProc) {
	for(int i = time; i < time + newJob->time; i++)
		scheduleTable[availProc][i] = newJob->jobId;
}

bool empty(int *parentCount, int numberNodes) {
	for(int i = 1; i <= numberNodes; i++) {
		if(parentCount[i] != 0)
			return false;
	}
	return true;
}

void debugToConsole(int time, int procUsed, int *processJob, int *processTime, int *parentCount, int *jobTime, bool *jobDone, bool *jobMarked, int numberNodes) {
	cout << "Current Time: " << time << "\n";
	cout << "Processors used: " << procUsed << "\n";
	cout << "Process Jobs: ";
	for(int i = 1; i <= numberNodes; i++) {
		cout <<	setfill(' ') << setw(4) << processJob[i];
	}
	cout << "\nProcess Time: ";
	for(int i = 1; i <= numberNodes; i++) {
		cout << setfill(' ') << setw(4) <<  processTime[i];
	}
	cout << "\nParent Count: ";
	for(int i = 1; i <= numberNodes; i++) {
		cout << setfill(' ') << setw(4) <<  parentCount[i];
	}
	cout << "\nJob Time:     ";
	for(int i = 1; i <= numberNodes; i++) {
		cout << setfill(' ') << setw(4) << jobTime[i];
	}
	cout << "\nJob Done:     ";
	for(int i = 1; i <= numberNodes; i++) {
		cout << setfill(' ') << setw(4) << jobDone[i];
	}
	cout << "\nJob Marked:   ";
	for(int i = 1; i <= numberNodes; i++) {
		cout << setfill(' ') << setw(4) << jobMarked[i];
	}
	cout << "\n\n";
}

void deleteEdges(LinkedList **hashTable, int *parentCount, int job, int numberNodes) {
	Node *walker = hashTable[job]->getHead()->next;
	while(walker != NULL) {
		parentCount[walker->jobId]--;
		walker = walker->next;
	}
}

void print(ifstream &infile1, ifstream &infile2, ofstream &outfile, int procNeed) {
	char c;
	while(infile1 >> noskipws >> c) {
		outfile << c;
	}
	outfile << "\n";
	while(infile2 >> noskipws >> c) {
		outfile << c;
	}
	outfile << "\nProcessors Need: " << procNeed << "\n\n";
}

void printSchedule(int **scheduleTable, ofstream &outfile, int procNeed, int time) {
	outfile << "Schedule:	Time = " << time << "\n";
	for(int i = 1; i <= procNeed; i++) {
		for(int j = 0; j < time; j++) {
			outfile << setfill(' ') << setw(3) << scheduleTable[i][j];
		}
		outfile << "\n";
	}
	outfile << "\n";
}

int main(int argc, char* argv[]) {
	int **scheduleTable;
	LinkedList OPEN;
	LinkedList **hashTable;
	int *processJob;
	int *processTime;
	int *parentCount;
	int *jobTime;
	bool *jobDone;
	bool *jobMarked;
	
	ifstream infile(argv[2]);
	int numberNodes;
	infile >> numberNodes;
	jobTime = createJobTime(infile, numberNodes);
	
	infile.close();
	int totalTime = addTime(jobTime, numberNodes);
	scheduleTable = createScheduleTable(numberNodes, totalTime);

	processJob = new int[numberNodes+1]();
	processTime = new int[numberNodes+1]();
	parentCount = new int[numberNodes+1]();
	jobDone = new bool[numberNodes+1]();
	jobMarked = new bool[numberNodes+1]();
	infile.open(argv[1]);
	hashTable = createHashTable(infile, parentCount);
	infile.close();

	ofstream outfile(argv[4], std::ofstream::app);

	int procNeed = atoi(argv[3]);
	
	ifstream infile1(argv[1]);
	ifstream infile2(argv[2]);
	print(infile1, infile2, outfile, procNeed);
	infile1.close();
	infile2.close();
	
	if(procNeed > numberNodes)
		procNeed = numberNodes;
	
	int procUsed = 0;
	int time = 0;

	// step 11:
	while(remainingJob(jobDone, numberNodes)) {
		// step 1:
		for(int i = 1; i <= numberNodes; i++) {
			if(!jobMarked[i] && parentCount[i] == 0) {
				int orphanNode = i;
				jobMarked[orphanNode] = true;
				Node *newNode = new Node(orphanNode, jobTime[orphanNode]);
				OPEN.append(newNode);
			}
		}
		// step 3:
		while(!OPEN.isEmpty() && procUsed < procNeed) {
			// step 2:
			int availProc;
			procUsed++;
			if(procUsed <= procNeed) {
				Node *newJob = OPEN.remove();
				availProc = findAvailProc(processJob, procNeed);
				processJob[availProc] = newJob->jobId;
				processTime[availProc] = newJob->time;
				updateSchedule(scheduleTable, time, newJob, availProc);
				delete(newJob);
			}	
		}
		// step 4:
		if(OPEN.isEmpty() && !empty(parentCount, numberNodes) && empty(processJob, numberNodes)) {
			cout << "There is a cycle in the graph.";
			return 0;
		}
		// step 5:
		debugToConsole(time, procUsed, processJob, processTime, parentCount, jobTime, jobDone, jobMarked, numberNodes);
	
		// step 6:
		time++;
	
		// step 9:
		for(int i = 1; i <= numberNodes; i++) {
			// step 7
			processTime[i]--;
			// step 8
			if(processTime[i] == 0) {
				procUsed--;
				int done = i;
				int job = processJob[done];
				processJob[done] = 0;
				jobDone[job] = true;
				deleteEdges(hashTable, parentCount, job, numberNodes);
			}
		}
		// step 10:
		debugToConsole(time, procUsed, processJob, processTime, parentCount, jobTime, jobDone, jobMarked, numberNodes);
	}
	
	printSchedule(scheduleTable, outfile, procNeed, time);
	outfile.close();
	
	for(int i = 0; i <= numberNodes; ++i) {
    	delete[] scheduleTable[i];
	}
	delete[] scheduleTable;
	delete[] processJob;
	delete[] processTime;
	delete[] parentCount;
	delete[] jobTime;
	delete[] jobDone;
	delete[] jobMarked;
	
	return 0;

}
