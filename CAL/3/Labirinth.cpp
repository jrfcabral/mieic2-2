/*
 * labirinth.cpp
 */

#include "Labirinth.h"

#include <iostream>
using namespace std;



Labirinth::Labirinth(int values[10][10])
{
	for (int i = 0; i < 10; i++)
		for (int j = 0; j < 10; j++)
			labirinth[i][j]=values[i][j];

}


void Labirinth::initializeVisited(int x, int y)
{
	for (int i = 0; i < 10; i++)
		for (int a = 0; a < 10; a++)
			visited[i][a] = false;

}




void  Labirinth::printLabirinth()
{
	for (int i = 0; i < 10; i++)
	{
		for (int a = 0; a < 10; a++)
			cout << labirinth[i][a] << " ";

		cout << endl;
	}
}


bool Labirinth::findGoal(int x, int y)
{
	this->initializeVisited(x,y);
	return this->backtrack(x, y);
}

bool Labirinth::backtrack(int x, int y)
{
	if (labirinth[x][y] == 0 || visited[x][y])
		return false;

	visited[x][y] = 1;
	cout << "visitei " << x << " , " << y << endl;

	if (labirinth[x][y] == 2){
		cout << "A saida estava em " << x << "," << y << endl;
		return true;
	}

	if (backtrack(x+1,y))	return true;
	if (backtrack(x-1, y))  return true;
	if (backtrack (x, y+1)) return true;
	if (backtrack(x, y-1))  return true;
	else{
		cout << "por aqui não deu " << x << ", " << y << endl;
		return false;
	}


}



