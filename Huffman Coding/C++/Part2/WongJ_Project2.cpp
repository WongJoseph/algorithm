#include <iostream>
#include <fstream>
#include <string>
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
		void insert(Node *newNode, ofstream &file);
		void binaryTree(ofstream &file);
		void print(ofstream &file);
		void printDebug(ofstream &file, int count);
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

void LinkedList::insert(Node *newNode, ofstream &file) {
	static int count = 0;
	Node *walker = listHead;
	while (walker->next != NULL && walker->next->prob < newNode->prob) {
		walker = walker->next;
	}
	newNode->next = walker->next;
	walker->next = newNode;
}

void LinkedList::binaryTree(ofstream &file) {
	Node *oldListHead = new Node("dummy", 0);
	oldListHead = listHead->next;
	int count = 0;
	
	while (listHead->next->next != NULL) {
		Node *newNode = new Node(listHead->next->chString + listHead->next->next->chString,
								 listHead->next->prob + listHead->next->next->prob);
		newNode->left = listHead->next;
		newNode->right = listHead->next->next;
		listHead->next = listHead->next->next->next;
		insert(newNode, file);
		printDebug(file, ++count);
	}
	
	Node *root = listHead->next;
	listHead = oldListHead;
}

void LinkedList::print(ofstream &file) {
	Node *walker = listHead->next;
	while (walker != NULL) {
		file << walker->chString << " " << walker->prob << " ";
		walker = walker->next;
	}
	file << "\n";
}

void LinkedList::printDebug(ofstream &file, int count) {
	Node *walker = listHead->next;
	file << "Iteration " << count << ": listHead -->";
	while (walker->next != NULL) {
		file << "(" << walker->chString << ", " << walker->prob << ", " << walker->next->chString << ")-->";
		walker = walker->next;
	}
	file << "(" << walker->chString << ", " << walker->prob << ", NULL)-->NULL \n";
}

int main(int argc, char* argv[])
{
	ofstream outfile;
//	outfile.open(argv[2]);
	ifstream infile(argv[1]);
	LinkedList myList;
	
	int probIn;
	char ch;
	char a;
	infile >> a;
	int count = 0;
	
	while (infile >> ch) {
		if (!((ch > 47 && ch < 58 ))) {
			if (a != ch) {
				Node *newNode = new Node(string(1,a), probIn);
				myList.insert(newNode, outfile);
//				myList.printDebug(outfile, ++count);
				a = ch;
			}
			probIn = 0;
		}
		else 
			if (probIn > 0)
				probIn = probIn * 10 + (ch - '0');
			else 
				probIn += ch - '0';
	}	
	Node *newNode = new Node(string(1,a), probIn);
	myList.insert(newNode, outfile);	
//	myList.printDebug(outfile, ++count);
	
	infile.close();
//	outfile.close();
	
	outfile.open(argv[2]);
	myList.print(outfile);
	outfile.close();
	
	outfile.open(argv[3]);
	myList.binaryTree(outfile);
	outfile.close();
	
	return 0;	
}
