#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>  
#include <stdio.h>
using namespace std;

class Node
{
	public:
	string chString;
	double prob;
	Node *left;
	Node *right;
	int bitNum;
	double enProb;
	string code;
	Node *next;
	Node(string c, double data);
};

Node::Node(string c, double data)
{
	chString = c;
	prob = data;
	left = NULL;
	right = NULL;
	next = NULL;
}

class LinkedList
{
	public:	
		void insert(Node *newNode);
		void print(ofstream &outfile);
		Node* getHead();
		void setHead(Node *T);
		Node* HuffmanBinaryTree(ofstream &outfile);
		LinkedList();
		~LinkedList();
	
	private:
		Node *listHead;
};

LinkedList::LinkedList()
{
	listHead = new Node("dummy",0);
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

void LinkedList::setHead(Node *T) {
	listHead = T;
}

void LinkedList::insert(Node *newNode) {
	static int count = 0;
	Node *walker = listHead;
	while (walker->next != NULL && walker->next->prob < newNode->prob) {
		walker = walker->next;
	}
	newNode->next = walker->next;
	walker->next = newNode;
}

void printLinkedList(Node *listHead, ofstream &outfile, int count) {
	Node *walker = listHead;
	outfile << "Iteration " << count << ": listHead -->";
	while (walker->next != NULL) {
		outfile << "(" << walker->chString << ", " << walker->prob << ", " << walker->next->chString << ")-->";
		walker = walker->next;
	}
	outfile << "(" << walker->chString << ", " << walker->prob << ", NULL)-->NULL \n";
}

int HuffmanLinkedList(LinkedList &myList, ifstream &infile, ofstream &outfile) {
	double probIn;
	char ch;
	int count = 0;
	char c;
	infile >> skipws;
	while (infile >> ch >> probIn) {
		c = (char) count;
		Node *newNode = new Node(string(1,ch), probIn);
		myList.insert(newNode);
		count++;
	//	printLinkedList(myList.getHead(), outfile, count);
	}		
	return count;
}

Node* LinkedList::HuffmanBinaryTree(ofstream &outfile) {
	Node *oldListHead = new Node("dummy", 0);
	oldListHead->next = listHead->next;
	int count = 0;
	
	while (listHead->next->next != NULL) {
		Node *newNode = new Node(listHead->next->chString + listHead->next->next->chString,
								 listHead->next->prob + listHead->next->next->prob);
		newNode->left = listHead->next;
		newNode->right = listHead->next->next;
		listHead->next = listHead->next->next->next;
		insert(newNode);
	//	printLinkedList(myList.getHead(), outfile, ++count);
	}
	
	Node *root = listHead->next;
	root->right->next = root;
	listHead = oldListHead;
	return root;
}

void LinkedList::print(ofstream &outfile) {
	Node *walker = listHead->next;
	while (walker != NULL) {
		outfile << walker->chString << " " << walker->prob << "\n";
		walker = walker->next;
	}
	outfile << "\n";
}

void HuffmanCode(Node *T, string code) {
	Node *walker = T;
	if (walker == NULL)
		return;
	if (walker->chString.length() == 1) {
		walker->code = code;
		return;
	}
	else {
		HuffmanCode(walker->left, code + "0");
		HuffmanCode(walker->right, code + "1");
	}
}

void EntropyTable(Node *listHead, char chArray[], string codeArray[], double probArray[], int bitArray[], double entropyArray []) {
	int index = 1;
	Node *walker = listHead->next;
	while (walker->next != NULL) {
		if (walker->chString.length() == 1) {
			chArray[index] = walker->chString.at(0);
			codeArray[index] = walker->code;
			probArray[index] = walker->prob;
			bitArray[index] = walker->bitNum = walker->code.length();
			entropyArray[index] = walker->enProb = (walker->prob * walker->bitNum);
			index++;
		}
		walker = walker->next;
	}
}

void printEntropyTable(char chArray[], string codeArray[], double probArray[], int bitArray[], double entropyArray [], ofstream &outfile, int count) {	
	double totalEntropy = 0;
	outfile << "Letter | Prob |  Code  | BitNum | Entropy\n";
	for (int i = 1; i <= count; i++) {
		totalEntropy+=entropyArray[i];
		outfile << fixed << setprecision(2) << setfill(' ') << setw(4) << chArray[i] << setw(7) << probArray[i] << setw(12) << codeArray[i] << setw(6) << bitArray[i] << setw(11) << entropyArray[i] <<"\n";
	}
	outfile << setfill(' ') << setw(36) << "Total Entropy: " << totalEntropy/100 << "\n";
}

int* calcHistogram(ifstream &infile, int size) {
	int *Hist = new int[size]();
	unsigned char ch = NULL;
	while (infile >> noskipws >> ch) {
		Hist[(int)ch]++;	
	}
	return Hist;	
}

char* printProbPairs(int Hist[], ofstream &outfile, int size) {
	int total = 0;
	for (int i = 0; i < size; i++) {
		total+= Hist[i];
	}
	int special = 200;
	char *specialArray = new char[10];
	int index = 0;
	int count = 0;
	outfile << fixed << setprecision(2); 
	while (count < size) {
		if (count < 33 && Hist[count] > 0) {
			outfile << (char) special++ << " " << (Hist[count]*1.0/total*100) << "\n";
			specialArray[index++] = (char) count;
		}
		else if (Hist[count] > 0)
			outfile << (char) count << " " << (Hist[count]*1.0/total*100) << "\n";
		count++;	
	}
	return specialArray;
}

string findCode(char chArray[], string codeArray[], char specialArray[], char s) {
	int index = 0;

	for (int i = 0; i < 10; i++) {
		if (s == specialArray[i])
			s = (char) 200+i;
	}

	while (chArray[index] != s) {
		index++;
	}
	return codeArray[index];
}

void printEncode(char chArray[], string codeArray[], char specialArray[], ifstream &infile, ofstream &outfile) {
	char ch;
	while (infile >> noskipws >> ch) {
		outfile << findCode(chArray, codeArray, specialArray, ch);
	}
}

void decode(Node *root,char specialArray[], ifstream &infile, ofstream &outfile) {
	char c;
	Node *walker = root;
	while (infile >> noskipws >> c) {
		if (c == '0')
			walker = walker->left;		
		if (c == '1')
			walker = walker->right;
		if (walker->left == NULL && walker->right == NULL) {
			bool special = false;
			for (int i = 0; i < 10; i++) {
				if (walker->chString == (string(1,(char)200 + i))) {
					outfile << specialArray[i];
					special = true;
				}
			}
			if (!special)
				outfile << walker->chString;
			walker = root;
		}
	}
}

int main(int argc, char* argv[])
{
	LinkedList myList;
	
	int size = 256;
	int *Hist;
	char *specialArray;
	
	ifstream infile(argv[1]);	
	Hist = calcHistogram(infile, size);
	ofstream outfile;
	outfile.open(argv[2]);
	specialArray = printProbPairs(Hist, outfile, size);
	infile.close();
	outfile.close();
	infile.open(argv[2]);
	int count = HuffmanLinkedList(myList, infile, outfile);
	infile.close();	
//	myList.printLinkedList(outfile, ++count);
	
	outfile.open(argv[3]);
	printLinkedList(myList.getHead(), outfile, 0);	
	outfile.close();
	
//	outfile.open(argv[3]);
	Node *root = myList.HuffmanBinaryTree(outfile);
//	outfile.close();
	
	outfile.open(argv[4]);
	printLinkedList(myList.getHead(), outfile, 0);
	outfile.close();
	
	HuffmanCode(root, "");
	
	char *chArray = new char[count+1];
	string *codeArray = new string[count+1];
	double *probArray = new double [count+1], *entropyArray = new double[count+1];
	int *bitArray = new int[count+1]; 
	EntropyTable(myList.getHead(), chArray, codeArray, probArray, bitArray, entropyArray);
	
	outfile.open(argv[5]);
	printEntropyTable(chArray, codeArray, probArray, bitArray, entropyArray, outfile, count);
	outfile.close();
	
	outfile.open(argv[6]);
	infile.open(argv[8]);
	printEncode(chArray, codeArray, specialArray, infile, outfile);
	infile.close();
	outfile.close();
	
	infile.open(argv[6]);
	outfile.open(argv[7]);
	decode(root, specialArray, infile, outfile);
	infile.close();
	outfile.close();
	
	return 0;	
}
