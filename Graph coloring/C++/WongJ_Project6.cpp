#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <iomanip> 
using namespace std;

int** createMatrix(ifstream &infile, int size) {	
	int **adjacentMatrix = (int **)malloc((size+1) * sizeof(int *));
	
    for(int i = 0; i < (size+1); i++)
         adjacentMatrix[i] = (int *)malloc((size+1) * sizeof(int));
	int endA, endB;
	
	for(int i = 1; i <= size; i++) {
		for(int j = 1; j <= size; j++) {
				adjacentMatrix[i][j] = 0;
		}
	}

	while(infile >> endA >> endB) {
		adjacentMatrix[endA][endB] = 1;
	}
	return adjacentMatrix;
}

bool checked(int *nodeColor, int size) {
	for(int i = 1; i <= size; i++)
		if(nodeColor[i] == 0)
			return true;
	return false;
}

void print(ifstream &infile, ofstream &outfile, int **adjacentMatrix, int newColor, int size) {
	char ch;
	while(infile >> noskipws >> ch) {
		outfile << ch;
	}
	outfile << "\n\nAdjacent Matrix:\n";
	
	for(int i = 1; i <= size; i++) {
		for(int j = 1; j <= size; j++) {
			outfile << setfill(' ') << setw(3) << adjacentMatrix[i][j];
		}
		outfile << "\n";
	}
	outfile << "\nNumber of color used: " << newColor << endl;
}

int main(int argc, char* argv[]) {
	int **adjacentMatrix;
	int *nodeColor;
	
	ifstream infile(argv[1]);
	int size;
	infile >> size;
	adjacentMatrix = createMatrix(infile, size);
	infile.close();
	
	nodeColor = new int[size+1]();
	
	int newColor = 0;
	int newNode;
	int index;
	
	while(checked(nodeColor, size)) {
		newColor++;
	
		for(index = 1; index <= size; index++) {
			if(nodeColor[index] == 0) {
				newNode = index;
				bool sameColor = false;
				for(int i = 1; i <= size; i++) {
					if(adjacentMatrix[newNode][i] && nodeColor[i] == newColor)
						sameColor = true;
				}
				if(!sameColor)
					nodeColor[newNode] = newColor;
			}
			cout << newColor << " " << index << " " << nodeColor[index] << endl;
		}
	}
	
	for(int i = 1; i <= size; i++)
		adjacentMatrix[i][i] = nodeColor[i];
			
	ofstream outfile;
	infile.open(argv[1]);
	outfile.open(argv[2], std::ofstream::app);
	print(infile, outfile, adjacentMatrix, newColor, size);
	infile.close();
	outfile.close();
	
	for (int i = 0; i <= size; ++i) {
    	delete [] adjacentMatrix[i];
	}
	delete [] adjacentMatrix;
	delete [] nodeColor;
	
	return 0;
}
