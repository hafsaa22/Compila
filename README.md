# Pseudo-Code

**Options**: Java, C, Python
**Types**: entiers, caractères, booléens
**Opérateurs**: +, -, *, /, =, >, <, ==, !=
**Mots-clés**: OPTION, VAR, SI, ALORS, SINON, FINSI, TANT_QUE, FAIRE, FINTANTQUE, AFFICHER, LIRE

#### Exemple de Pseudo-Code:
```
OPTION: Python;

VAR age : entier;
VAR estMajeur : boolean;
VAR lettre : caractere;
VAR compteur : entier;

AFFICHER("Veuillez saisir votre age :");
LIRE(age);

SI age > 18 ALORS
    estMajeur = true;
    lettre = 'M';
SINON
    estMajeur = false;
    lettre = 'J';
FINSI;

SI estMajeur == true ALORS
    AFFICHER("Acces autorise. Statut :");
    AFFICHER(lettre);
SINON
    AFFICHER("Acces refuse.");
FINSI;

compteur = 0;
TANT_QUE compteur < 3 FAIRE
    AFFICHER(compteur);
    compteur = compteur + 1;
FINTANTQUE;
```
### Grammaire
PROGRAMME := 
OPTION_DECL := "OPTION:" LANGAGE_OPTION
LANGAGE_OPTION := "Python"
DECLARATIONS :=
