import java.util.*;

// Classe pour représenter une entrée dans la table des symboles
class EntreeSymbole {
    public String nom;
    public String type;
    public boolean initialise;
    public boolean utilise;
    
    public EntreeSymbole(String nom, String type) {
        this.nom = nom;
        this.type = type;
        this.initialise = false;
        this.utilise = false;
    }
    
    @Override
    public String toString() {
        return nom + " : " + type + " [init=" + initialise + ", util=" + utilise + "]";
    }
}
