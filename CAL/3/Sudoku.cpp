/*
 * Sudoku.cpp
 *
 */

#include "Sudoku.h"

/** Inicia um Sudoku vazio.
 */
Sudoku::Sudoku()
{
	this->initialize();
}

/**
 * Inicia um Sudoku com um conte�do inicial.
 * Lan�a excep��o IllegalArgumentException se os valores
 * estiverem fora da gama de 1 a 9 ou se existirem n�meros repetidos
 * por linha, coluna ou bloc 3x3.
 *
 * @param nums matriz com os valores iniciais (0 significa por preencher)
 */
Sudoku::Sudoku(int nums[9][9])
{
	this->initialize();

	for (int i = 0; i < 9; i++)
	{
		for (int j = 0; j < 9; j++)
		{
			if (nums[i][j] != 0)
			{
				int n = nums[i][j];
				numbers[i][j] = n;
				lineHasNumber[i][n] = true;
				columnHasNumber[j][n] = true;
				block3x3HasNumber[i / 3][j / 3][n] = true;
				countFilled++;
			}
		}
	}
}

void Sudoku::initialize()
{
	for (int i = 0; i < 9; i++)
	{
		for (int j = 0; j < 9; j++)
		{
			for (int n = 0; n < 10; n++)
			{
				numbers[i][j] = 0;
				lineHasNumber[i][n] = false;
				columnHasNumber[j][n] = false;
				block3x3HasNumber[i / 3][j / 3][n] = false;
			}
		}
	}

	this->countFilled = 0;
}

/**
 * Obtem o conte�do actual (s� para leitura!).
 */
int** Sudoku::getNumbers()
{
	int** ret = new int*[9];

	for (int i = 0; i < 9; i++)
	{
		ret[i] = new int[9];

		for (int a = 0; a < 9; a++)
			ret[i][a] = numbers[i][a];
	}

	return ret;
}

/**
 * Verifica se o Sudoku j� est� completamente resolvido
 */
bool Sudoku::isComplete()
{
	return countFilled == 9 * 9;
}



/**
 * Resolve o Sudoku.
 * Retorna indica��o de sucesso ou insucesso (sudoku imposs�vel).
 */
bool Sudoku::solve()
{
	backtrack(0,1);
}

bool Sudoku::backtrack(int x, int y){

	for (int n = 1; n < 10; n++)
	{
		if (!isPossible(x, y, n)){
			cout << "o " << n << " nao da" << endl;
			continue;
		}
		numbers[x][y] = n;
		cout << "Tento preencher a casa " << x << ", " << y << " com o numero " << n << endl;
		print();

		if (this->isComplete()){
			return true;
			this->print();
		}

		else if (y >= 8){
			cout << "avanço" << endl;
			backtrack(x+1, y);
		}
		else{
			cout << "avanço" << endl;
			backtrack(x, y+1);
		}
	}
	cout << "regresso" << endl;
	return false;
}

bool Sudoku::isPossible(int x, int y, int n)
{
	for (int i = 0; i < 9; i++){
		if (numbers[x][i] == n) return false;
		if (numbers[i][y] == n) return false;
	}

	int i = x/3;
	int j = y/3;
	for (int k = 0; k < 3; k++)
		for (int l = 0; l < 3; l++)
			if (numbers[i+k][j+l] == n) return false;

	cout << "O " << n << " dá" << endl;
	return true;

}

int Sudoku::countPossible(int x, int y)
{
	int i = 0;
	for (int j = i; j < 10; j++)
		if (isPossible(x,y,j)) i++;

	return i;
}


/**
 * Imprime o Sudoku.
 */
void Sudoku::print()
{
	for (int i = 0; i < 9; i++)
	{
		for (int a = 0; a < 9; a++)
			cout << this->numbers[i][a] << " ";

		cout << endl;
	}

}

