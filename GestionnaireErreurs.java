import java.util.*;

// Classe pour gérer la liste des erreurs
class GestionnaireErreurs {
    private List<ErreurSemantique> erreurs;
    
    public GestionnaireErreurs() {
        erreurs = new ArrayList<>();
    }
    
    public void ajouterErreur(String message, int ligne) {
        erreurs.add(new ErreurSemantique(message, ligne));
    }
    
    public boolean aErreurs() {
        return !erreurs.isEmpty();
    }
    
    public void afficherErreurs() {
        System.out.println("\n=== ERREURS SEMANTIQUES ===");
        for (ErreurSemantique erreur : erreurs) {
            System.out.println(erreur);
        }
        System.out.println("===========================\n");
    }
    
    public List<ErreurSemantique> getErreurs() {
        return erreurs;
    }
}