#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class Node
{
	public:
	char letter;
	int prob;
	Node *next;
	Node(char c, int data);
};

Node::Node(char c, int data)
{
	letter = c;
	prob = data;
}

class LinkedList
{
	public:	
		int getLength();
		void insert(char c, int data, ofstream &file);
		string print(ofstream &file);
		LinkedList();
		Node getHead();
	
	private:
		Node listHead = Node('z', 0);
		Node listTail = listHead;
		int length = 0;
};

LinkedList::LinkedList()
{
}

int LinkedList::getLength() {
	return length;
}

Node LinkedList::getHead(){
	return listHead;
}

string LinkedList::print(ofstream &file) {
	Node *walker = listHead.next;
	while (walker != NULL) {
		file << walker->letter << " " << walker->prob << " ";
		walker = walker->next;
	}
	file << "\n";
	
	return "";
}

void LinkedList::insert(char c, int data, ofstream &file) {
	Node *n = new Node(c, data);
	Node *walker = listHead.next;
	Node *r = &listHead;
	
	while (walker != NULL && walker->prob < data) {
		r = walker;
		walker = walker->next;
	}
	if (walker == NULL)
		listTail = *n;
	n->next = r->next;
	r->next = n;	
	length++;
	print(file);
}

int main(int argc, char* argv[])
{
	ofstream outfile;
	outfile.open(argv[2]);
	ifstream infile(argv[1]);
	LinkedList myList;
	
	int num;
	char ch;
	char a;
	infile >> a;
	while (infile >> ch) {
		if (!((ch > 47 && ch < 58 ))) {
			if (a != ch) {
				myList.insert(a, num, outfile);
				a = ch;
			}
			num = 0;
		}
		else 
			if (num > 0)
				num = num * 10 + (ch - '0');
			else 
				num += ch - '0';
	}
	myList.insert(a, num, outfile);	
	infile.close();
	outfile.close();
	outfile.open(argv[3]);
	myList.print(outfile);
	outfile.close();
	return 0;	
}
