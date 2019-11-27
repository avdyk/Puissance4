package Puissance4;

import MyStuff.*;

public class Main {

    public static void main(String[] args) {
        Puissance4Struct jeu = new Puissance4Struct();
        int input = 0, meilleurColonne, valeurMeilleurColonne, valMinMax, nombreCoupsJoues = 0, coupsMaximum =  jeu.getNombreCases();
        int col;
        boolean gagnant = false, AITurn = false;

        System.out.println("Voulez-vous jouer contre une IA ? \n1: Oui | 2: Non");
        input = (int) Verif.Input(1, 2, input);

        if (input == 1) {
            Puissance4Struct.setJetonAI(jeu.getJeton());
            while (!gagnant && nombreCoupsJoues < coupsMaximum) {
                if (AITurn){
                    jeu.changeJeton();

                    meilleurColonne = 0;
                    valeurMeilleurColonne = 0;

                    for (col = 0; col < jeu.getLargeurTableau(); col++) {
                        if (jeu.setIndice(col)) {
                            if (jeu.getJeton() == Puissance4Struct.getJetonAI())
                                jeu.changeJeton();
                            valMinMax = minMax(jeu, 4, col, coupsMaximum - nombreCoupsJoues, 0, true);
                            //negaMax(jeu, 6, col, 0, coupsMaximum);
                            //
                            if (valeurMeilleurColonne > valMinMax){
                                valeurMeilleurColonne = valMinMax;
                                meilleurColonne = col;
                            }

                        }

                    }

                    jeu.setJeton(Puissance4Struct.getJetonAI());
                    jeu.setIndice(meilleurColonne);
                    jeu.poseJeton();
                    ++nombreCoupsJoues;

                    if (nombreCoupsJoues >= 7)
                        gagnant = jeu.verifGagnant();

                    AITurn = false;

                } else {
                    jeu.changeJeton();
                    joueur(jeu);

                    jeu.poseJeton();
                    nombreCoupsJoues++;

                    if (nombreCoupsJoues >= 7)
                        gagnant = jeu.verifGagnant();

                    AITurn = true;
                }

            }

            jeu.afficherPuissance4();
            if (gagnant)
                System.out.println("\nBravo au joueur "+ jeu.getJeton() +" pour avoir gagné en "+ nombreCoupsJoues +" tours !");
            else
                System.out.println("c'est un match nul. Quelle intensité !");

        } else { // Partie joueur contre joueur

            while (!gagnant && nombreCoupsJoues < coupsMaximum) {
                jeu.changeJeton();
                joueur(jeu);

                nombreCoupsJoues++;
                jeu.poseJeton(); // Place le jeton à  l'endroit choisi par le joueur

                if (nombreCoupsJoues >= 7) // La vérification du gagnant n'est utile seulement si 7 coups ou plus sont joués
                    gagnant = jeu.verifGagnant();


                System.out.println();
                jeu.afficherPuissance4();

            }

            if (gagnant)
                System.out.println("\nBravo au joueur "+ jeu.getJeton() +" pour avoir gagné en "+ nombreCoupsJoues +" tours !");
            else
                System.out.println("c'est un match nul. Quelle intensité !");

        }

    }

    public static int negaMax(Puissance4Struct jeu, int profondeur, int colonneTest, int nombreCoups, int nombreCoupsMax) {
        jeu.setIndice(colonneTest);
        jeu.poseJeton();

        if (nombreCoups == nombreCoupsMax || profondeur == 0) {
            jeu.effaceJetonColonne(colonneTest);
            return 0;
        }

        for (int col = 0; col < jeu.getLargeurTableau(); col++) {
            if (jeu.setIndice(col) && jeu.verifGagnant()) {
                jeu.effaceJetonColonne(colonneTest);
                return (jeu.getValPos() - nombreCoups) / 2;
            }
        }

        int bestScore = jeu.getBestScorePossible();

        for (int col = 0; col < jeu.getLargeurTableau(); col++) {
            if (jeu.setIndice(col)) {
                bestScore = Math.max(bestScore, -negaMax(jeu, profondeur-1, col, nombreCoups+1, nombreCoupsMax));
            }
        }

        jeu.effaceJetonColonne(colonneTest);
        return bestScore;
    }

    public static int minMax(Puissance4Struct jeu, int profondeur, int colonneTest, int coupsRestants, int nombreCoups, boolean maximizingPlayer) {
        jeu.changeJeton();
        jeu.setIndice(colonneTest); // Place le jeton dans la colonne testée
        jeu.poseJeton();

        if (profondeur == 0 || coupsRestants == 0) {

            jeu.effaceJetonColonne(colonneTest);
            if (jeu.verifGagnant()) {
                if (jeu.IATurn()) {
                    return 10000;
                } else
                    return -100000;

            } else {
                if (jeu.IATurn())
                    return jeu.evaluationTableau();
                else
                    return -jeu.evaluationTableau();
            }
        }

        if (maximizingPlayer) {
            int value = -100000;

            for (int i = 0; i < jeu.getLargeurTableau(); i++) {
                if (jeu.setIndice(i)) {
                    value = Math.max(value, minMax(jeu, profondeur-1, i, coupsRestants-1, nombreCoups+1, false));
                }
            }

            jeu.effaceJetonColonne(colonneTest);
            return value;
        } else {
            int value = 100000;

            for (int i = 0; i < jeu.getLargeurTableau(); i++) {
                if (jeu.setIndice(i)) {
                    value = Math.min(value, minMax(jeu, profondeur-1, i, coupsRestants-1, nombreCoups+1, true));
                }
            }

            jeu.effaceJetonColonne(colonneTest);
            return value;
        }
    }

    public static void joueur(Puissance4Struct jeu) {
        int input = 0;

        System.out.println("\n\n");
        System.out.println("C'est au tour de joueur "+ jeu.getJeton() +" de jouer.");
        System.out.println("Choisissez une colonne entre 1 et "+ jeu.getLargeurTableau() +"\n");

        jeu.afficherPuissance4();

        input = (int) Verif.Input(1, jeu.getLargeurTableau(), input) - 1; // Décrémente pour réajuster "input" avec les indices du tableau

        while (!jeu.setIndice(input)) {
            System.out.println("La colonne entrée est pleine, prenez-en une autre.");

            input = (int) Verif.Input(1, jeu.getLargeurTableau(), input) - 1;
        }

    }

}


/*
 * poubelle
 * 
	public static int eval(char[][] tableau, int[] indice, char jeton, boolean maximizingPlayer) {
		int eval = -1000;
		for (int i = 0; i < tableau[0].length; i++) {
			eval = Math.max(eval, verifCombinaisonJCIA(tableau, indice, jeton));
		}

		if (maximizingPlayer)
			return eval;
		else
			return -eval;
	}

	public static int verifCombinaisonJCIA (char[][] tableau, int[] indice, char jeton) { // Donne la valeur que la combinaison testée aura
		int valCombinaisons = 0;

		for (int k = -1; k <= 1; k++) {		
			valCombinaisons += vectorValeur(tableau, indice, k, -1, jeton);

		}

		valCombinaisons += vectorValeur(tableau, indice, 1, 0, jeton);

		return valCombinaisons;
	}

	public static int vectorValeur (char[][] tableau, int[] indice, int vectorI, int vectorJ, char jeton) {
		int recurrence = 0;
		int i = indice[0], j = indice[1];
		int edgeI = calculEdgePointEdge(tableau, vectorI, 'i'), edgeJ = calculEdgePointEdge(tableau, vectorJ, 'j');
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
				edgeI = calculEdgePointEdge(tableau, vectorI, 'i');
				edgeJ = calculEdgePointEdge(tableau, vectorJ, 'j');
			}

		}		
		recurrence -= 1;

		if (recurrence >= 4)
				return 15;
		return recurrence;
	}

	public static int calculEdgePointEdge (char[][] tableau, int vectorPoint, char indice) {
		if (vectorPoint == -1 || vectorPoint == 0)
			return -1;
		else if (indice == 'i') {
			return tableau.length;
		} else {
			return tableau[0].length;
		}
	}

	public static char[][] copieTableau(char[][] tableau) {
		int i = 0, j = 0;
		char[][] copieTableau = new char[tableau.length][tableau[0].length];

		for (; i < copieTableau.length; i++) {
			for (j = 0; j < copieTableau[0].length; j++) {
				copieTableau[i][j] = tableau[i][j];
			}
		}

		return copieTableau;
	}

	public static boolean indiceIFinder (char[][] tableau, int[] indice) { 
		int i = 0;

		while (i < tableau.length && tableau[i][indice[1]] == ' ') {
			i++;
		}

		if (i != 0) {
			indice[0] = i - 1;
			return true;
		}
		else {
			return false;
		}
	}

}

/*
 * en construction
	public static int verifCombinaisonJCIA(char[][] tableau, char jeton, int i, int j) {

	}

 */
