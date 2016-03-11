#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <iomanip>  
using namespace std;

int** createMatrix(ifstream &infile, int size) {	
	int **costMatrix = (int **)malloc((size+1) * sizeof(int *));
	
    for (int i = 0; i < (size+1); i++)
         costMatrix[i] = (int *)malloc((size+1) * sizeof(int));
	int endA, endB, cost;
	
	for (int i = 1; i <= size; i++) {
		for (int j = 1; j <= size; j++) {
			if (i == j)
				costMatrix[i][j] = 0;
			else
				costMatrix[i][j] = 999;
		}	
	}

	while (infile >> endA >> endB >> cost) {
		costMatrix[endA][endB] = cost;
	}
	return costMatrix;
}

void print(ifstream &infile, ofstream &outfile, int **costMatrix, int size, int source, int *parent, int *bestCost) {	
	char ch;
	while (infile >> noskipws >> ch) {
		outfile << ch;
	}
	outfile << "\n";
	outfile << "Cost Matrix:\n";
	
	for (int i = 1; i <= size; i++) {
		for (int j = 1; j <= size; j++) {
			if(costMatrix[i][j] == 999)
				outfile << setfill(' ') << setw(4) << "NA";
			else
				outfile << setfill(' ') << setw(4) << costMatrix[i][j]; 
		}
		outfile << "\n";
	}
	outfile << "\nSource Node: " << source << "\n\n";
	
	for (int i = 1; i <= size; i++) { 
		outfile << source << " to n" << i << " " << bestCost[i] << " and path: n" << i << " <-- ";
		
		int father = parent[i];
		
		while (father != source) {
			outfile << "n" << father << " <-- ";
			father = parent[father];
		}
		outfile << source << "\n";
	}
	outfile << "\n";
}

int main(int argc, char* argv[]) 
{
	int **costMatrix;
	bool *marked;
	int *bestCost;
	int *parent;
	
	ifstream infile(argv[1]);
	int size;
	infile >> size;
	costMatrix = createMatrix(infile, size);
	infile.close();	
	int source = atoi(argv[2]);
	
	marked = new bool[size+1]();
	bestCost = new int[size+1];
	parent = new int[size+1];
	
	for (int i = 1; i <= size; i++) {
		parent[i] = source;
		bestCost[i] = costMatrix[source][i];
	}
	int minNode = source;
	int newNode;
	int tempCost;
	
	for (int iteration = 1; iteration <= size; iteration++) {
		int temp;
		marked[minNode] = true;
		
		for (int i = 1; i <= size; i++) {
			if (!marked[i]) {
				newNode = i;
				tempCost = bestCost[minNode] + costMatrix[minNode][newNode];
				if (tempCost < bestCost[newNode]) {
					bestCost[newNode] = tempCost;
					parent[newNode] = minNode;
				}
				temp = bestCost[i];
			}		
		}
		
		for (int i = 1; i <= size; i++) {
			if(!marked[i] && bestCost[i] <= temp) {
				temp = bestCost[i];
				minNode = i;
			}
		}
	}
	ofstream outfile;
	infile.open(argv[1]);
	outfile.open(argv[3], std::ofstream::app);
	print(infile, outfile, costMatrix, size, source, parent, bestCost);
	infile.close();
	outfile.close();
	
	for (int i = 0; i <= size; ++i) {
    	delete [] costMatrix[i];
	}
	delete [] costMatrix;
	delete [] marked;
	delete [] bestCost;
	delete [] parent;
	
	return 0;
}
