package it.almaviva.dta;

import java.util.Arrays;
import java.util.Random;

public class Maze {

	private int order;
	private boolean isPerfect;
	private int[][] representation;
	private int[] teseoPos;
	private int[] minotaurPos;
	private int[] exitPos;
	private String winner;

	public Maze(int order, boolean isPerfect) {
		this.order = order;
		this.isPerfect = isPerfect;
		this.representation = this.generateMaze(0, 0, new int[this.order][this.order]);
		if (!this.isPerfect) {
			this.representation = this.generateMultiplePaths(this.representation);
		}
		do {
			this.teseoPos = this.generateRandomPoint();
			this.minotaurPos = this.generateRandomPoint();
			this.exitPos = this.generateRandomPoint();
		} while(this.teseoPos[0] == this.minotaurPos[0] || this.teseoPos[0] == this.exitPos[0]);

		this.winner = "NONE";
	}

	//Ottieni la matrice che rappresenta il labirinto;s
	public int[][] getRepresentation() {
		return representation;
	}

	//Ottieni coordinate teseo;
	public int[] getTeseoPos() {
		return teseoPos;
	}

	//Imposta coordinate teseo;
	public void setTeseoPos(int[] teseoPos) {
		this.teseoPos = teseoPos;
	}

	//Ottieni coordinate minotauro;
	public int[] getMinotaurPos() {
		return minotaurPos;
	}

	//Imposta posizione minotauro;
	public void setMinotaurPos(int[] minotaurPos) {
		this.minotaurPos = minotaurPos;
	}

	//Ottieni coordinate dell'uscita;
	public int[] getExitPos() {
		return exitPos;
	}

	//Ottieni nome del vincitore;
	public String getWinner() {
		return winner;
	}

	@Override
	public String toString() {
		return "Maze [order=" + order + ", isPerfect=" + isPerfect + ", teseoPos=" + Arrays.toString(teseoPos)
		+ ", minotaurPos=" + Arrays.toString(minotaurPos) + ", exitPos=" + Arrays.toString(exitPos)
		+ ", winner=" + winner + "]";
	}

	// stampa in console la matrice rappresentativa del labirinto;
	public void printMaze() {
		for (int i = 0; i < this.representation.length; i++) {
			for (int j = 0; j < this.representation.length; j++) {
				if (i == this.teseoPos[0] && j == this.teseoPos[1]) {
					System.out.print("T" + " ");
				} else if (i == this.minotaurPos[0] && j == this.minotaurPos[1]) {
					System.out.print("M" + " ");
				} else if (i == this.exitPos[0] && j == this.exitPos[1]) {
					System.out.print("U" + " ");
				} else if (this.representation[i][j] == 1) {
					System.out.print("  ");
				} else if (this.representation[i][j] == 0) {
					System.out.print("##");
				}
			}
			System.out.println();
		}
	}

	// controlla l'esistenza di un vincitore;
	public boolean checkWinner() {
		if (this.representation[teseoPos[0]] == this.representation[minotaurPos[0]]
				&& this.representation[teseoPos[1]] == this.representation[minotaurPos[1]]) {
			this.winner = "Minotaur";
			return true;
		}

		if (this.representation[teseoPos[0]] == this.representation[exitPos[0]]
				&& this.representation[teseoPos[1]] == this.representation[exitPos[1]]) {
			this.winner = "Teseo";
			return true;
		}

		return false;
	}

	// genera un punto casuale nel labirinto;
	private int[] generateRandomPoint() {
		Random rand = new Random();
		int x = rand.nextInt(this.representation.length);
		int y = rand.nextInt(this.representation.length);
		if (this.representation[x][y] == 1) {
			int[] coordinate = { x, y };
			return coordinate;
		} else {
			// invoca nuovamente la funzione nel caso in cui il punto non sia accessibile;
			return generateRandomPoint();
		}
	}

	/*
	 * rimuove le dead-ends dei labirinti generati, in maniera tale da far sì che ci
	 * siano più percorsi per raggiungere un punto;
	 */
	private int[][] generateMultiplePaths(int[][] mat) {
		// itera su ogni cella del labirinto per individure e rimuovere le dead ends;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				// contatore dei muri;
				int wallCounter = 0;
				// posizione dei muri;
				String foundWalls = "";
				// verifica che la cella sia percorribile;
				if (mat[i][j] == 1) {
					// verifica l'esistenza di un muro in una delle quattro direzioni possibili;
					if ((i + 1) < mat.length) {
						if (mat[i + 1][j] == 0) {
							wallCounter += 1;
							foundWalls += "D";
						}
					}

					if ((j - 1) < mat.length && (j - 1) >= 0) {
						if (mat[i][j - 1] == 0) {
							wallCounter += 1;
							foundWalls += "L";
						}
					}

					if ((j + 1) < mat.length) {
						if (mat[i][j + 1] == 0) {
							wallCounter += 1;
							foundWalls += "R";
						}
					}

					if ((i - 1) < mat.length && (i - 1) >= 0) {
						if (mat[i - 1][j] == 0) {
							wallCounter += 1;
							foundWalls += "U";
						}
					}
				}
				/*
				 * nel caso in cui la cella attuale fosse circondata da 3 muri, allora si tratta
				 * di una dead end;
				 */
				if (wallCounter == 3) {
					// seleziona randomicamente un muro da rimuovere;
					Random rand = new Random();
					String muroDaRimuovere = foundWalls.split("")[rand.nextInt(3)];
					switch (muroDaRimuovere) {
					case "D":
						mat[i + 1][j] = 1;
						break;
					case "U":
						mat[i - 1][j] = 1;
						break;
					case "R":
						mat[i][j + 1] = 1;
						break;
					case "L":
						mat[i][j - 1] = 1;
						break;
					}
				}
			}
		}
		return mat;
	}

	/*
	 * genera un labirinto perfetto, in cui ogni punto è raggiungibile attraverso un
	 * solo percorso;
	 */
	private int[][] generateMaze(int x, int y, int[][] mat) {
		// marchia cella come visitata;
		mat[x][y] = 1;
		// direzioni possibili;
		int dirs[][] = { { 2, 0 }, // DOWN;
				{ 0, -2 }, // LEFT;
				{ -2, 0 }, // UP;
				{ 0, 2 }, // RIGHT;
		};
		// randomizza l'indice delle direzioni;
		Random rand = new Random();
		for (int i = 0; i < dirs.length; i++) {
			int randIndex = rand.nextInt(dirs.length);
			int[] temp = dirs[randIndex];
			dirs[randIndex] = dirs[i];
			dirs[i] = temp;
		}
		/*
		 * itera su ogni riga della matrice "direzioni" e determina le coordinate delle
		 * cella adiacenti;
		 */
		for (int[] dir : dirs) {
			// coordinate della cella adiacente;
			int nextX = x + dir[0];
			int nextY = y + dir[1];
			// verifica che la cella adiacente sia valida;
			boolean isValid = nextX < mat.length && nextY >= 0 && nextY < mat.length && nextY >= 0
					&& mat[nextX][nextY] != 1;
			/*
			 * abbatte un muro in base della direzione scelta e collega la cella corrente
			 * con quella adiacente;
			 */
			if (isValid) {
				if (dir[0] == 2) {
					mat[x + 1][y] = 1;
				}
				if (dir[1] == -2) {
					mat[x][y - 1] = 1;
				}
				if (dir[0] == -2) {
					mat[x - 1][y] = 1;
				}
				if (dir[1] == 2) {
					mat[x][y + 1] = 1;
				}
				// ripete le stesse istruzioni per la cella adiacente;
				this.generateMaze(nextX, nextY, mat);
			}
		}
		return mat;
	}

}
