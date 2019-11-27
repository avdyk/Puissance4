package Puissance4;


public class Puissance4Struct implements Cloneable {
	private char [][] tableau = new char[7][7];
	private int[] indice = new int[2];
	private char jeton;
	private static char jetonAI;

	Puissance4Struct() {
		jeton = 'X';
		initialisationTableau();		
	}

	Puissance4Struct(Puissance4Struct copie) {
		this.tableau = copie.copieTableau();
		this.jeton = copie.jeton;
	}

	int getNombreCases() {
		return tableau.length * tableau[0].length;
	}

	int getLargeurTableau() {
		return tableau[0].length;
	}

	boolean setIndice(int colonne) { // Place l'indice sur la première ligne possible d'une colonne choisie. Renvoie faux s'il n'y a aps de colonne possible
		int i = 0;

		while (i < tableau.length && tableau[i][colonne] == ' ') {
			i++;
		}

		if (i != 0) {
			indice[0] = i - 1;
			indice[1] = colonne;
			return true;
		}
		else {
			return false;
		}
	}

	int getIndiceLigne(int colonne) { // Copie de setIndice, mais ne modifie pas la variable indice de l'objet
		int i = 0;

		while (i < tableau.length && tableau[i][colonne] == ' ') {
			i++;
		}

		return i;
	}


	char getJeton() {
		return jeton;
	}

	void setJeton(char jeton) {
		if (jeton == 'X' || jeton == 'O')
			this.jeton = jeton;
	}


	void changeJeton() {
		if (jeton == 'O')
			jeton = 'X';
		else {
			jeton = 'O';
		}
	}


	private void initialisationTableau() {
		for (int i = 0; i < tableau.length; i++) {
			for (int j = 0; j < tableau[0].length; j++) {
				tableau[i][j] = ' ';
			}
		}
	}


	void afficherPuissance4() {
		int i = 0, j, k;

		for (; i < tableau[0].length; i++) // Affiche les nombres au dessus des colonnes
			if (i < 10)
				System.out.print("   "+ (i+1));
			else
				System.out.print("  "+ (i+1));
		System.out.println();

		for (i = 0; i < tableau.length; i++) { // Affichage du puissance 4
			for (j = 0; j < tableau[0].length; j++) {
				System.out.print(" | "+ tableau[i][j]);
			}
			System.out.println(" |");
			System.out.print(" ");

			for (k = 0; k < tableau[0].length; k++)
				System.out.print("----");

			System.out.println("-");
		}

	}

	void poseJeton() {
		tableau[indice[0]][indice[1]] = jeton;
	}


	boolean verifGagnant() { // Procède aux vérifications permettant de savoir si le joueur a gagné

		// Le vectorValeur faisant à  chaque fois la vérification du vecteur opposé, il n'est pas nécéssaire de mettre les 8 directions.


		for (int k = -1; k <= 1; k++) {		
			if (vectorValeur( k, -1) >= 4)
				return true;
		}

		if (vectorValeur(1, 0) >= 4)
			return true;

		return false;
	}

	public int vectorValeur (int vectorI, int vectorJ) {
		int recurrence = 0;
		int i = indice[0], j = indice[1];
		int edgeI = calculEdgePointEdge(vectorI, 'i'), edgeJ = calculEdgePointEdge(vectorJ, 'j');
		for (int k = 0; k < 2; k++) {
			do {
				i += vectorI;
				j += vectorJ;
				recurrence++;
			} while (i != edgeI && j != edgeJ && tableau[i][j] == jeton);

			if (k == 0) {
				i = indice[0];
				j = indice[1];				
				vectorI = -vectorI; // Continue à  chercher dans la direction opposée, c'est la raison pour le For avec k
				vectorJ = -vectorJ;
				edgeI = calculEdgePointEdge(vectorI, 'i');
				edgeJ = calculEdgePointEdge(vectorJ, 'j');
			}

		}		
		recurrence -= 1;
		
		return recurrence;
	}


	public int calculEdgePointEdge (int vectorPoint, char indice) { // Donne la valeur pour i ou j à ne pas dépasser
		if (vectorPoint == -1 || vectorPoint == 0)
			return -1;
		else if (indice == 'i') {
			return tableau.length;
		} else {
			return tableau[0].length;
		}
	}


	public int verifTailleCombinaison() { // Donne la valeur de la combinaison testée
		int valCombinaisons = 0;

		for (int k = -1; k <= 1; k++) {		
			valCombinaisons = Math.max(valCombinaisons, vectorValeur(k, -1));

		}

		valCombinaisons = Math.max(valCombinaisons, vectorValeur(1, 0));

		return valCombinaisons;
	}

	boolean IATurn() {
		if (getJeton() == getJetonAI())
			return true;
		else
			return false;
	}

	public char[][] copieTableau() {
		int i = 0, j = 0;
		char[][] copieTableau = new char[tableau.length][tableau[0].length];

		for (; i < copieTableau.length; i++) {
			for (j = 0; j < copieTableau[0].length; j++) {
				copieTableau[i][j] = tableau[i][j];
			}
		}

		return copieTableau;
	}

	public int evaluationTableau() { // Donne une évaluation du tableau actuelle.
		int score = 0, i = 0, j = 0;

		for (; i < tableau[0].length; i++) {
				setIndice(j);
				score += verifTailleCombinaison();
		}
		
		/*changeJeton();

		for (i = 0; i < tableau.length; i++) {
			for (j = 0; j < tableau[0].length; j++) {
				score -= verifTailleCombinaison();
			}
		}

		changeJeton();*/

		return score;
	}

	public int positionScore() {
		int score = 0;

		for (int col = 0; col < tableau[0].length; col++) {
			int ligne = getIndiceLigne(col);
			if (ligne != 0) {
				int tailleCombinaison = verifTailleCombinaison();

				if (tailleCombinaison == 4)
					score += 100;
				else if (tailleCombinaison == 3)
					score += 10;
			}
		}

		return score;
	}

	int getValPos() {
		return (indice[0] + 1)+ 1 * (indice[1] + 1);
	}

	int getBestScorePossible() {
		return -tableau.length * tableau[0].length;
	}

	void effaceJetonColonne(int colonne) {
		int ligne = getIndiceLigne(colonne);
		tableau[ligne][colonne] = ' ';
	}

	public static char getJetonAI() {
		return jetonAI;
	}

	public static void setJetonAI(char jetonAI) {
		if (jetonAI == 'X' || jetonAI == 'O')
			Puissance4Struct.jetonAI = jetonAI;
	}

}
