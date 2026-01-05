public enum TokenType {

    // Mots-clés
    OPTION, VAR, SI, ALORS, SINON, FINSI,
    TANT_QUE, FAIRE, FINTANTQUE,
    AFFICHER, LIRE,

    // Types
    ENTIER, BOOLEAN, CARACTERE,

    // Valeurs
    TRUE, FALSE,

    // Identificateur & constantes
    ID, NUM, CHAINE, CAR,

    // Opérateurs
    PLUS, EGAL, EGAL_EGAL, INF, SUP,

    // Symboles
    DEUX_POINTS, PV, PO, PF,

    EOF, ERREUR
}
