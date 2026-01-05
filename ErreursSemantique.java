import java.util.*;

// Classe pour gérer les erreurs sémantiques
class ErreurSemantique {
    public String message;
    public int ligne; // approximative
    
    public ErreurSemantique(String message, int ligne) {
        this.message = message;
        this.ligne = ligne;
    }
    
    @Override
    public String toString() {
        return "Erreur sémantique ligne " + ligne + ": " + message;
    }
}