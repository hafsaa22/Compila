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
