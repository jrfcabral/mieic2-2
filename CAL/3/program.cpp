#include <iostream>
#include "Labirinth.h"
using namespace std;

int main()
{
	int lab1[10][10] = {
				{0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,1,1,0,1,0,0},
				{0,1,0,0,0,1,0,1,0,0},
				{0,1,1,0,1,1,1,1,1,0},
				{0,1,0,0,0,1,0,0,0,0},
				{0,1,0,1,0,1,1,1,1,0},
				{0,1,1,1,0,0,1,0,1,0},
				{0,1,0,0,0,0,1,0,1,0},
				{0,1,1,1,0,0,1,1,0,0},
				{0,0,0,0,0,0,0,0,0,0}};


	Labirinth lab(lab1);

	lab.findGoal(1,1);

}
