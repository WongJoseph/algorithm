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
	int prob;
	Node *left;
	Node *right;
	int bitNum;
	int enProb;
	string code;
	Node *next;
	Node(string c, int data);
};

Node::Node(string c, int data)
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
		void print(ofstream &file);
		Node* getHead();
		void setHead(Node *T);
		Node* HuffmanBinaryTree(ofstream &file);
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

void printLinkedList(Node *listHead, ofstream &file, int count) {
	Node *walker = listHead;
	file << "Iteration " << count << ": listHead -->";
	while (walker->next != NULL) {
		file << "(" << walker->chString << ", " << walker->prob << ", " << walker->next->chString << ")-->";
		walker = walker->next;
	}
	file << "(" << walker->chString << ", " << walker->prob << ", NULL)-->NULL \n";
}

int HuffmanLinkedList(LinkedList &myList, ifstream &infile, ofstream &outfile) {
	int probIn;
	char ch;
	int count = 0;
	
	while (infile >> ch >> probIn) {
		Node *newNode = new Node(string(1,ch), probIn);
		myList.insert(newNode);
		count++;
	//	printLinkedList(myList.getHead(), outfile, count);
	}		
	return count;
}

Node* LinkedList::HuffmanBinaryTree(ofstream &file) {
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
	//	printLinkedList(myList.getHead(), file, ++count);
	}
	
	Node *root = listHead->next;
	root->right->next = root;
	listHead = oldListHead;
	return root;
}

void LinkedList::print(ofstream &file) {
	Node *walker = listHead->next;
	while (walker != NULL) {
		file << walker->chString << " " << walker->prob << " ";
		walker = walker->next;
	}
	file << "\n";
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

void EntropyTable(Node *listHead, char chArray[], string codeArray[], int probArray[], int bitArray[], int entropyArray []) {
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

void printEntropyTable(char chArray[], string codeArray[], int probArray[], int bitArray[], int entropyArray [], ofstream &file, int count) {
	
	int totalEntropy = 0;
	file << "Letter | Prob |  Code  | BitNum | Entropy\n";
	for (int i = 1; i <= count; i++) {
		totalEntropy+=entropyArray[i];
		file << fixed << setprecision(2) << setfill(' ') << setw(4) << chArray[i] << setw(7) << probArray[i] << setw(12) << codeArray[i] << setw(6) << bitArray[i] << setw(11) << entropyArray[i] <<"\n";
	}
	file << setfill(' ') << setw(36) << "Total Entropy: " << totalEntropy*1.0/100 << "\n";
}

int main(int argc, char* argv[])
{
	LinkedList myList;
	
	ifstream infile(argv[1]);
	ofstream outfile;
	outfile.open(argv[2]);
	int count = HuffmanLinkedList(myList, infile, outfile);
	infile.close();	
//	myList.printLinkedList(outfile, ++count);
	
	myList.print(outfile);	
	outfile.close();
	
//	outfile.open(argv[3]);
	Node *root = myList.HuffmanBinaryTree(outfile);
//	outfile.close();
	
	outfile.open(argv[3]);
	printLinkedList(myList.getHead(), outfile, 0);
	outfile.close();
	
	HuffmanCode(root, "");
	
	char *chArray = new char[count+1];
	string *codeArray = new string[count+1];
	int *probArray = new int[count+1], *bitArray = new int[count+1], *entropyArray = new int[count+1];
	EntropyTable(myList.getHead(), chArray, codeArray, probArray, bitArray, entropyArray);
	
	outfile.open(argv[4]);
	printEntropyTable(chArray, codeArray, probArray, bitArray, entropyArray, outfile, count);
	outfile.close();
	
	return 0;	
}
