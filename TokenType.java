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
    PLUS, MOINS, MULT, DIV, MOD, EGAL, DIFFERENT, EGAL_EGAL, INF, INF_EGAL, SUP, SUP_EGAL, ET, OU,

    // Symboles
    DEUX_POINTS, PV, PO, PF,

    EOF, ERREUR
}
