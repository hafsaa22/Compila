import java.util.*;

// Classe pour gérer la table des symboles
public class TableDesSymboles {
    private Map<String, EntreeSymbole> table;
    private Stack<Map<String, EntreeSymbole>> pilePortees;
    
    public TableDesSymboles() {
        table = new HashMap<>();
        pilePortees = new Stack<>();
    }
    
    public void entrerPortee() {
        pilePortees.push(new HashMap<>(table));
    }
    
    public void sortirPortee() {
        if (!pilePortees.isEmpty()) {
            table = pilePortees.pop();
        }
    }
    
    public boolean ajouterSymbole(String nom, String type) {
        String nomUpper = nom.toUpperCase();
        if (table.containsKey(nomUpper)) {
            return false; // Échec, variable déjà déclarée
        }
        table.put(nomUpper, new EntreeSymbole(nomUpper, type));
        return true; // Succès
    }
    
    public EntreeSymbole chercherSymbole(String nom) {
        return table.get(nom.toUpperCase());
    }
    
    public boolean existeSymbole(String nom) {
        return table.containsKey(nom.toUpperCase());
    }
    
    public void marquerInitialise(String nom) {
        EntreeSymbole symbole = chercherSymbole(nom);
        if (symbole != null) {
            symbole.initialise = true;
        }
    }
    
    public void marquerUtilise(String nom) {
        EntreeSymbole symbole = chercherSymbole(nom);
        if (symbole != null) {
            symbole.utilise = true;
        }
    }
    
    public void afficherTable() {
        System.out.println("\n=== TABLE DES SYMBOLES ===");
        for (EntreeSymbole symbole : table.values()) {
            System.out.println(symbole);
        }
        System.out.println("==========================\n");
    }
    
    public List<String> getVariablesNonUtilisees() {
        List<String> nonUtilisees = new ArrayList<>();
        for (EntreeSymbole symbole : table.values()) {
            if (!symbole.utilise) {
                nonUtilisees.add(symbole.nom);
            }
        }
        return nonUtilisees;
    }
    
    public List<String> getVariablesNonInitialisees() {
        List<String> nonInitialisees = new ArrayList<>();
        for (EntreeSymbole symbole : table.values()) {
            if (!symbole.initialise) {
                nonInitialisees.add(symbole.nom);
            }
        }
        return nonInitialisees;
    }
}
