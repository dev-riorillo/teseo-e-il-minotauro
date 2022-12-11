package it.almaviva.dta;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Scanner scan = new Scanner(System.in);
		System.out.println(
				"TESEO E IL MINOTAURO -- Rocco Iorillo \n \n" +
						"In questo  semplice applicativo, l'obiettivo di Teseo (T) è quello di trovare la via d'uscita (U) \n" +
						"senza incontrare il minotauro (M) sul proprio cammino, viceversa l'obiettivo del minotauro è \n" +
						"impedire che Teseo riesca nel suo intento.\n \n" + 
						"Seleziona la modalità di gioco:\n" +
						"1) Minotauro statico - Il minotauro bloccherà un passaggio senza muoversi, l'IA di Teseo vincerà sempre.\n" +
						"2) Minotauro dinamico - Il minotauro darà attivamente la caccia a Teseo, mentre quest'ultimo cercherà la via d'uscita.\n" +
						"3) Gioca come minotauro - Impersona il minotauro ed impedisci a Teseo di raggiungere l'uscita. \n" +
						"4) Gioca come Teseo - Impersona Teseo e raggiungi l'uscita evitando il minotauro."
				);
		//Modalità scelta dall'utente;
		int scelta = Integer.valueOf(scan.nextLine());
		System.out.println("Inserisci la dimensione del labirinto generato automaticamente: \n");
		//Ordine della matrice che rappresenta il labirinto;
		int ordine = Integer.valueOf(scan.nextLine());

		//Inizializzazione delle matrici;
		int[][] mat = new int[ordine][ordine];
		int[][] labirinto = generaLabirintoPerfetto(0, 0, mat);

		/* Nella modalità 'minotauro statico' il labirinto presenterà diversi percorsi,
		 * mentre in modalità 'minotauro dinamico' verrà generato un labirinto perfetto,
		 * per evitare che il gioco prosegua all'infinito in una situazione di stallo;
		 *  */
		if(scelta != 2) {
			labirinto = generaPercorsiMultipli(labirinto);
		}

		//Inizializzazione coordinate relative alla posizione di Teseo, del minotauro e dell'uscita;
		int teseoX = 0;
		int teseoY = 0;
		int minotauroX = 0;
		int minotauroY = 0;
		int uscitaX = 0;
		int uscitaY = 0;

		//Inizializza percorsi per Teseo ed il minotauro;
		String percorsoTeseo = ""; 
		String percorsoMinotauro = "";

		//Genera 3 punti casuali diversi tra loro e raggiungibili;
		while(teseoX == minotauroX || minotauroX == uscitaX || uscitaX == teseoX
				|| percorsoTeseo.length() < 1 || percorsoMinotauro.length() < 1 ) {

			// Posizione iniziale teseo;
			teseoX = generaPuntoCasuale(labirinto)[0];
			teseoY = generaPuntoCasuale(labirinto)[1];

			// Posizione iniziale minotauro;
			minotauroX = generaPuntoCasuale(labirinto)[0];
			minotauroY = generaPuntoCasuale(labirinto)[1];

			// Uscita dal labirinto;
			uscitaX = generaPuntoCasuale(labirinto)[0];
			uscitaY = generaPuntoCasuale(labirinto)[1];

			//Percorso iniziale;
			if(scelta == 1) {
				//Durante la modalità 'minotauro statico', il minotauro è considerato un ostacolo;
				labirinto[minotauroX][minotauroY] = 0;
				percorsoTeseo =  trovaPercorso(labirinto, teseoX, teseoY, uscitaX, uscitaY);
				labirinto[minotauroX][minotauroY] = 1;
			} else {
				percorsoTeseo =  trovaPercorso(labirinto, teseoX, teseoY, uscitaX, uscitaY);
			}
			percorsoMinotauro = trovaPercorso(labirinto, minotauroX, minotauroY, teseoX, teseoY);
		}


		//Regola il passaggio del turno;
		boolean isTeseo = true;

		System.out.println("LEGENDA: \n" + "T - TESEO \n" + "M - MINOTAURO \n" + "U - USCITA \n" + "1 - CELLE PERCORRIBILI \n" + "0 - CELLE NON PERCORRIBILI \n");
		stampaLabirinto(labirinto, teseoX, teseoY, minotauroX, minotauroY, uscitaX, uscitaY);
		System.out.println("\nPremi invio per continuare");
		scan.nextLine();

		//Contatore turni;
		int numeroTurno = 0;

		while(true) {
			// Attendi mezzo secondo per ogni iterazione;
			Thread.sleep(500);

			//Durante le modalità 'gioca come Teseo' e 'gioca come minotauro' la posizione viene decisa dal giocatore;
			if(scelta == 3 && !isTeseo) {
				System.out.println("Inserisci la direzione (D, L, U, R): ");
				percorsoMinotauro = scan.nextLine();
			}
			if(scelta == 4 && isTeseo) {
				System.out.println("Inserisci la direzione (D, L, U, R): ");
				percorsoTeseo = scan.nextLine();
			}

			// Effettua mossa Teseo;
			if(isTeseo) {
				boolean isValid = false;
				//Salvataggio posizione attuale, così da poterla resettare in caso di mossa non valida;
				int prevTeseoX = teseoX;
				int prevTeseoY = teseoY;

				while(!isValid) {
					//La mossa viene effettuata;
					switch(percorsoTeseo.charAt(numeroTurno)) {
					case 'D': teseoX += 1;
					break;
					case 'L': teseoY += -1;
					break;
					case 'U': teseoX += -1;
					break;
					case 'R': teseoY += 1;
					break;
					}
					//Verifica della mossa;
					if(labirinto[teseoX][teseoY] == 0) {
						//Posizione resettata in caso di mosssa non valida;
						System.out.println("Posizione non valida, c'è un muro in questa direzione");
						percorsoTeseo = scan.nextLine();
						teseoX = prevTeseoX;
						teseoY = prevTeseoY;
					} else {
						isValid = true;
					}
				}
				System.out.println("Muove Teseo - T");
			}

			/* Ricalcola ad ogni turno il percorso migliore per il minotauro, 
			 * in base alla posizione di Teseo; 
			 * */
			if(!isTeseo && scelta != 3 && scelta != 1) {
				percorsoMinotauro = trovaPercorso(labirinto, minotauroX, minotauroY, teseoX, teseoY);
			}

			//Effettua mossa minotauro;
			if(!isTeseo) {
				boolean isValid = false;
				int prevMinX = minotauroX;
				int prevMinY = minotauroY;

				while(!isValid) {
					switch(percorsoMinotauro.charAt(0)) {
					case 'D': minotauroX += 1;
					break;
					case 'L': minotauroY += -1;
					break;
					case 'U': minotauroX += -1;
					break;
					case 'R': minotauroY += 1;
					break;
					}

					if(labirinto[minotauroX][minotauroY] == 0) {
						System.out.println("Posizione non valida, c'è un muro in questa direzione");
						percorsoMinotauro = scan.nextLine();
						minotauroX = prevMinX;
						minotauroY = prevMinY;
					} else {
						isValid = true;
					}

				}
				System.out.println("Muove minotauro - M");
			}

			//Stampa in console il labirinto;
			stampaLabirinto(labirinto, teseoX, teseoY, minotauroX, minotauroY, uscitaX, uscitaY);
			System.out.println();

			//Passa turno;
			if(scelta != 1) {
				isTeseo = !isTeseo;
			}

			//Dichiara vincitore;
			if(teseoX == uscitaX && teseoY == uscitaY) {
				System.out.println("Teseo ha vinto");
				break;
			} else if(minotauroX == teseoX && minotauroY == teseoY) {
				System.out.println("Il minotauro ha vinto");
				break;
			}

			//Il contatore dei turni resta a 0 in modalità 4 e non aumenta durante i turni del minotauro;
			if(isTeseo && scelta != 4) {
				numeroTurno++;
			}
		}
		scan.close();
	}

	//Stampa in console la matrice rappresentativa del labirinto;
	public static void stampaLabirinto(
			int [][] labirinto, int x, int y, 
			int mx, int my, 
			int ux, int uy
			) {
		for(int i = 0; i < labirinto.length; i++) {
			for(int j = 0; j < labirinto.length; j++) {
				if(i == x && j == y) {
					System.out.print ("T" + " ");
				} else if(i == mx && j == my) {
					System.out.print ("M" + " ");
				} else if(i == ux && j == uy){
					System.out.print ("U" + " ");
				}
				else if(labirinto[i][j] == 1){
					System.out.print("  ");
				}
				else if(labirinto[i][j] == 0) {
					System.out.print("##");
				}
			}
			System.out.println();
		}
	}



	// -- METODI PER LA GENERAZIONE AUTOMATICA E CASUALE DEL LABIRINTO --

	//Genera un punto accessibile e percorribile nel labirinto;
	public static int[] generaPuntoCasuale(int[][] mat) {
		Random rand = new Random();
		int x = rand.nextInt(mat.length);
		int y = rand.nextInt(mat[0].length);
		if(mat[x][y] == 1) {
			int[] coordinate = {x, y};
			return coordinate;
		} else {
			//Invoca nuovamente la funzione nel caso in cui il punto non sia accessibile;
			return generaPuntoCasuale(mat);
		}
	}

	/* Rimuove le dead-ends dei labirinti generati, 
	 * in maniera tale da far sì che ci siano più percorsi per raggiungere un punto;
	 */
	public static int[][] generaPercorsiMultipli(int[][] labirinto) {
		//Itera su ogni cella del labirinto per individure e rimuovere le dead ends;
		for(int i = 0; i < labirinto.length; i++) {
			for(int j = 0; j < labirinto.length; j++) {
				//Contatore dei muri;
				int contatoreMuri = 0;
				//Posizione dei muri;
				String muriIndividuati = "";
				//Verifica che la cella sia percorribile;
				if(labirinto[i][j] == 1) {
					//Verifica l'esistenza di un muro in una delle quattro direzioni possibili;
					if( (i + 1) < labirinto.length) {	
						if(labirinto[i + 1][j] == 0) {
							contatoreMuri += 1;
							muriIndividuati += "D";
						}	
					}

					if( (j - 1) < labirinto.length && (j - 1) >= 0) {	
						if(labirinto[i][j - 1] == 0) {
							contatoreMuri += 1;
							muriIndividuati += "L";
						}
					}

					if( (j + 1) < labirinto.length) {	
						if(labirinto[i][j + 1] == 0) {
							contatoreMuri += 1;
							muriIndividuati += "R";
						}	
					}

					if( (i - 1) < labirinto.length && (i - 1) >= 0) {	
						if(labirinto[i - 1][j] == 0) {
							contatoreMuri += 1;
							muriIndividuati += "U";
						}	
					}
				}
				/* Nel caso in cui la cella attuale fosse circondata da 3 muri, 
				 * allora si tratta di una dead end;
				 */
				if(contatoreMuri == 3) {
					//Seleziona randomicamente un muro da rimuovere;
					Random rand = new Random();
					String muroDaRimuovere = muriIndividuati.split("")[rand.nextInt(3)];
					switch(muroDaRimuovere) {
					case "D": labirinto[i + 1][j] = 1;
					break;
					case "U": labirinto[i - 1][j] = 1;
					break;
					case "R": labirinto[i][j + 1] = 1;
					break;
					case "L": labirinto[i][j - 1] = 1;
					break;	
					}
				}
			}
		}
		return labirinto;
	}
	// Genera un labirinto perfetto, in cui ogni punto è raggiungibile attraverso un solo percorso;
	public static int[][] generaLabirintoPerfetto(int x, int y, int[][] labirinto) {
		// Marchia cella come visitata;
		labirinto[x][y] = 1;
		// Direzioni possibili;
		int direzioni[][] = { { 2, 0 }, // DOWN;
				{ 0, -2 }, // LEFT;
				{ -2, 0 }, // UP;
				{ 0, 2 }, // RIGHT;
		};
		// Randomizza l'indice delle direzioni;
		Random rand = new Random();
		for (int i = 0; i < direzioni.length; i++) {
			int randIndex = rand.nextInt(direzioni.length);
			int[] copiaTemporanea = direzioni[randIndex];
			direzioni[randIndex] = direzioni[i];
			direzioni[i] = copiaTemporanea;
		}
		/* Itera su ogni riga della matrice "direzioni" e determina le coordinate
		 * delle cella adiacenti;
		 */
		for (int[] dir : direzioni) {
			// Coordinate della cella adiacente;
			int xAdiacente = x + dir[0];
			int yAdiacente = y + dir[1];
			// Verifica che la cella adiacente sia valida;
			boolean isValid = xAdiacente < labirinto.length && xAdiacente >= 0 
					&& yAdiacente < labirinto.length
					&& yAdiacente >= 0 && labirinto[xAdiacente][yAdiacente] != 1;
					/* Abbatte un muro in base della direzione scelta 
					 * e collega la cella corrente con quella adiacente;
					 */
					if (isValid) {
						if (dir[0] == 2) {
							labirinto[x + 1][y] = 1;
						}
						if (dir[1] == -2) {
							labirinto[x][y - 1] = 1;
						}
						if (dir[0] == -2) {
							labirinto[x - 1][y] = 1;
						}
						if (dir[1] == 2) {
							labirinto[x][y + 1] = 1;
						}
						// Ripete le stesse istruzioni per la cella adiacente;
						generaLabirintoPerfetto(xAdiacente, yAdiacente, labirinto);
					}
		}
		return labirinto;
	}

	// -- METODI RELATIVI ALL'IA DI TESEO ED IL MINOTAURO --

	//Dato un labirinto, una posizione iniziale ed una finale, calcola tutti i percorsi possibili;
	public static void calcolaPercorso(
			int riga, int colonna, int labirinto[][], 
			ArrayList <String> percorso, String mossa,
			int visitate[][], int rigaFinale, int colonnaFinale
			) {

		if (riga == rigaFinale && colonna == colonnaFinale) {
			percorso.add(mossa);
			return;
		}

		// Muove il personaggio di una cella sotto;
		if (riga + 1 < labirinto.length && visitate[riga + 1][colonna] == 0 && labirinto[riga + 1][colonna] == 1) {
			//Effettua lo spostamento e lo salva nella matrice che tiene conto delle celle che compongono la soluzione;
			visitate[riga][colonna] = 1;

			calcolaPercorso(
					riga + 1, colonna, labirinto, 
					percorso, mossa + 'D', 
					visitate, rigaFinale, colonnaFinale
					);

			visitate[riga][colonna] = 0;
		}
		// Muove il personaggio di una cella a sinistra;
		if (colonna - 1 >= 0 && visitate[riga][colonna - 1] == 0 && labirinto[riga][colonna - 1] == 1) {
			visitate[riga][colonna] = 1;

			calcolaPercorso(
					riga, colonna - 1, labirinto, 
					percorso, mossa  + 'L', visitate, 
					rigaFinale, colonnaFinale
					);

			visitate[riga][colonna] = 0;
		}
		// Muove il personaggio di una cella a destra;
		if (colonna + 1 < labirinto.length && visitate[riga][colonna + 1] == 0 && labirinto[riga][colonna + 1] == 1) {
			visitate[riga][colonna] = 1;

			calcolaPercorso(
					riga, colonna + 1, labirinto, 
					percorso, mossa  + 'R', visitate, 
					rigaFinale, colonnaFinale
					);

			visitate[riga][colonna] = 0;
		}
		// Muove il personaggio di una cella sopra;
		if (riga - 1 >= 0 && visitate[riga - 1][colonna] == 0 && labirinto[riga - 1][colonna] == 1) {
			visitate[riga][colonna] = 1;

			calcolaPercorso(
					riga - 1, colonna, labirinto, 
					percorso, mossa  + 'U', visitate, 
					rigaFinale, colonnaFinale
					);

			visitate[riga][colonna] = 0;
		}
	}

	//Restituisce il percorso migliore;
	public static String trovaPercorso(
			int[][] labirinto, int riga, 
			int colonna, int rigaFinale, 
			int colonnaFinale
			) {

		int visitate[][] = new int[labirinto.length][labirinto.length];

		//Inizializzazione lista percorsi;
		ArrayList <String> percorsi = new ArrayList<>();
		//Inizializzazione variabili mecessaroe per individuare il percorso migliore; 
		int lunghezzaPercorsoMigliore = Integer.MAX_VALUE;
		String percorsoMigliore = "";

		if (labirinto[riga][colonna] == 1) {
			//Calcola tutti i percorsi possibili;
			calcolaPercorso(riga, colonna, labirinto, percorsi, "", visitate, rigaFinale, colonnaFinale);
			//Restituisce il percorso migliore;
			for(String p : percorsi) {
				if(p.length() < lunghezzaPercorsoMigliore) {
					lunghezzaPercorsoMigliore = p.length();
					percorsoMigliore = p;
				}
			}
		}
		return percorsoMigliore;
	}
}



