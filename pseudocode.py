print("Veuillez saisir votre age :")
AGE = int(input())
print(10 / 2)
print(2 - 1)
print(6 * 3)
print(1024 % 5)
print(True and False)
print(True or False)
if AGE >= 18:
    ESTMAJEUR = True
    LETTRE = "M"
else:
    ESTMAJEUR = False
    LETTRE = "J"
if ESTMAJEUR == True:
    print("Acces autorise. Statut :")
    print(LETTRE)
else:
    print("Acces refuse.")
COMPTEUR = 0
while COMPTEUR < 3:
    print(COMPTEUR)
    COMPTEUR = COMPTEUR + 1
