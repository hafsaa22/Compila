import java.util.Scanner;

public class Output {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String COMPTEUR;
        boolean ESTMAJEUR;
        String LETTRE;
        int AGE;

        System.out.println("Veuillez saisir votre age :");
        AGE = scanner.nextInt();
        System.out.println(10 / 2);
        System.out.println(2 - 1);
        System.out.println(6 * 3);
        System.out.println(1024 % 5);
        System.out.println(true && false);
        System.out.println(true || false);
        if (AGE >= 18) {
            ESTMAJEUR = true;
            LETTRE = 'M';
        } else {
            ESTMAJEUR = false;
            LETTRE = 'J';
        }
        if (ESTMAJEUR == true) {
            System.out.println("Acces autorise. Statut :");
            System.out.println(LETTRE);
        } else {
            System.out.println("Acces refuse.");
        }
        COMPTEUR = 0;
        while (COMPTEUR < 3) {
            System.out.println(COMPTEUR);
            COMPTEUR = COMPTEUR + 1;
        }

        scanner.close();
    }
}
