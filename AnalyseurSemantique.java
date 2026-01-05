import java.util.List;
import java.util.ArrayList;
import ast.*;

public class AnalyseurSemantique {
    private List<Instruction> ast;
    private TableDesSymboles tableSymboles;
    private GestionnaireErreurs gestionnaireErreurs;
    
    public AnalyseurSemantique(List<Instruction> ast) {
        this.ast = ast;
        this.tableSymboles = new TableDesSymboles();
        this.gestionnaireErreurs = new GestionnaireErreurs();
    }
    
    public void analyser() {
        System.out.println("Début de l'analyse sémantique...\n");
        
        for (Instruction instruction : ast) {
            try {
                analyserInstruction(instruction);
            } catch (Exception e) {
                gestionnaireErreurs.ajouterErreur("Erreur lors de l'analyse: " + e.getMessage(), 0);
            }
        }
        
        effectuerVerificationsFinales();
        
        System.out.println("Analyse sémantique terminée.");
        
        if (gestionnaireErreurs.aErreurs()) {
            gestionnaireErreurs.afficherErreurs();
        } else {
            System.out.println("Aucune erreur sémantique détectée.");
        }
        
        tableSymboles.afficherTable();
    }
    
    private void analyserInstruction(Instruction instr) {
        if (instr instanceof VarDecl) {
            analyserDeclarationVariable((VarDecl) instr);
        } else if (instr instanceof Affectation) {
            analyserAffectation((Affectation) instr);
        } else if (instr instanceof SiInstruction) {
            analyserCondition((SiInstruction) instr);
        } else if (instr instanceof TantQueInstruction) {
            analyserBoucle((TantQueInstruction) instr);
        } else if (instr instanceof Affichage) {
            analyserAfficher((Affichage) instr);
        } else if (instr instanceof LectureInstruction) {
            analyserLire((LectureInstruction) instr);
        }
    }
    
    private void analyserDeclarationVariable(VarDecl decl) {
        String nomVar = decl.nom;
        String typeVar = decl.type;
        
        if (!tableSymboles.ajouterSymbole(nomVar, typeVar)) {
            gestionnaireErreurs.ajouterErreur("Variable '" + nomVar + "' déjà déclarée", 0);
        } else {
            System.out.println("Variable déclarée: " + nomVar + " : " + typeVar);
        }
    }
    
    private void analyserAffectation(Affectation affect) {
        String nomVar = affect.nom;
        
        EntreeSymbole symbole = tableSymboles.chercherSymbole(nomVar);
        if (symbole == null) {
            gestionnaireErreurs.ajouterErreur("Variable '" + nomVar + "' non déclarée", 0);
        } else {
            tableSymboles.marquerUtilise(nomVar);
        }
        
        String typeExpression = analyserExpression(affect.valeur);
        
        if (symbole != null && typeExpression != null && !typeExpression.equals("INCONNU")) {
            if (!estTypeCompatible(symbole.type, typeExpression)) {
                gestionnaireErreurs.ajouterErreur(
                    "Type incompatible: impossible d'affecter " + typeExpression + " à " + symbole.type,
                    0
                );
            }
        }
        
        if (symbole != null) {
            tableSymboles.marquerInitialise(nomVar);
        }
    }
    
    private String analyserExpression(Expr expr) {
        if (expr == null) return "INCONNU";
        
        if (expr instanceof LiteralExpr) {
            LiteralExpr lit = (LiteralExpr) expr;
            Object valeur = lit.valeur;
            
            if (valeur instanceof Integer) return "ENTIER";
            if (valeur instanceof Boolean) return "BOOLEAN";
            if (valeur instanceof String) {
                String str = (String) valeur;
                // Si la chaîne représente un caractère (ex: "M", "J")
                if (str.length() == 1 && !Character.isDigit(str.charAt(0))) {
                    return "CARACTERE";
                }
                // Si c'est une chaîne entre guillemets
                if (str.startsWith("\"") && str.endsWith("\"")) {
                    return "CHAINE";
                }
                // Si c'est un nombre
                try {
                    Integer.parseInt(str);
                    return "ENTIER";
                } catch (NumberFormatException e) {
                    // Ce n'est pas un nombre
                }
            }
            return "INCONNU";
            
        } else if (expr instanceof VariableExpr) {
            VariableExpr varExpr = (VariableExpr) expr;
            String nomVar = varExpr.nom;
            EntreeSymbole symbole = tableSymboles.chercherSymbole(nomVar);
            
            if (symbole == null) {
                gestionnaireErreurs.ajouterErreur("Variable '" + nomVar + "' non déclarée", 0);
                return "INCONNU";
            } else {
                tableSymboles.marquerUtilise(nomVar);
                return symbole.type;
            }
            
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr bin = (BinaryExpr) expr;
            String typeGauche = analyserExpression(bin.gauche);
            String typeDroite = analyserExpression(bin.droite);
            
            // CORRECTION: Gérer correctement les opérateurs
            String operateur = bin.operateur;
            
            if (operateur.equals("PLUS") || operateur.equals("+")) {
                if (!typeGauche.equals("ENTIER") || !typeDroite.equals("ENTIER")) {
                    gestionnaireErreurs.ajouterErreur("Addition impossible entre " + typeGauche + " et " + typeDroite, 0);
                    return "INCONNU";
                }
                return "ENTIER";
            } else if (operateur.equals("SUP") || operateur.equals(">") || 
                      operateur.equals("INF") || operateur.equals("<") ||
                      operateur.equals("EGAL_EGAL") || operateur.equals("==")) {
                // Ces opérateurs produisent un booléen
                if (estComparaisonValide(typeGauche, typeDroite, operateur)) {
                    return "BOOLEAN";
                } else {
                    gestionnaireErreurs.ajouterErreur("Comparaison impossible entre " + typeGauche + " et " + typeDroite, 0);
                    return "INCONNU";
                }
            }
        }
        
        return "INCONNU";
    }
    
    private boolean estComparaisonValide(String type1, String type2, String operateur) {
        // Pour EGAL_EGAL, on peut comparer n'importe quel type avec lui-même
        if (operateur.equals("EGAL_EGAL") || operateur.equals("==")) {
            return type1.equals(type2);
        }
        
        // Pour INF et SUP, seulement les entiers
        if (operateur.equals("INF") || operateur.equals("<") || 
            operateur.equals("SUP") || operateur.equals(">")) {
            return type1.equals("ENTIER") && type2.equals("ENTIER");
        }
        
        return false;
    }
    
    private boolean estTypeCompatible(String typeVar, String typeExpr) {
        if (typeExpr.equals("INCONNU")) return true;
        
        // CORRECTION: CARACTERE est compatible avec CARACTERE
        if (typeVar.equals("CARACTERE") && typeExpr.equals("CARACTERE")) {
            return true;
        }
        
        return typeVar.equals(typeExpr);
    }
    
    private void analyserCondition(SiInstruction si) {
        String typeCondition = analyserExpression(si.condition);
        
        if (!typeCondition.equals("BOOLEAN") && !typeCondition.equals("INCONNU")) {
            gestionnaireErreurs.ajouterErreur("Condition du SI doit être booléenne, mais type " + typeCondition + " trouvé", 0);
        }
        
        if (si.alorsInstructions != null) {
            for (Instruction instr : si.alorsInstructions) analyserInstruction(instr);
        }
        
        if (si.sinonInstructions != null) {
            for (Instruction instr : si.sinonInstructions) analyserInstruction(instr);
        }
    }
    
    private void analyserBoucle(TantQueInstruction tantQue) {
        String typeCondition = analyserExpression(tantQue.condition);
        
        if (!typeCondition.equals("BOOLEAN") && !typeCondition.equals("INCONNU")) {
            gestionnaireErreurs.ajouterErreur("Condition du TANT_QUE doit être booléenne, mais type " + typeCondition + " trouvé", 0);
        }
        
        if (tantQue.corps != null) {
            for (Instruction instr : tantQue.corps) analyserInstruction(instr);
        }
    }
    
    private void analyserAfficher(Affichage aff) {
        String typeExpr = analyserExpression(aff.expression);
        
        if (typeExpr.equals("INCONNU")) {
            // Ne pas créer d'erreur pour les chaînes littérales
            if (!(aff.expression instanceof LiteralExpr)) {
                gestionnaireErreurs.ajouterErreur("Expression invalide dans AFFICHER", 0);
            }
        }
        
        if (aff.expression instanceof VariableExpr) {
            VariableExpr varExpr = (VariableExpr) aff.expression;
            EntreeSymbole symbole = tableSymboles.chercherSymbole(varExpr.nom);
            if (symbole != null) tableSymboles.marquerUtilise(varExpr.nom);
        }
    }
    
    private void analyserLire(LectureInstruction lire) {
        String nomVar = lire.nomVariable;
        EntreeSymbole symbole = tableSymboles.chercherSymbole(nomVar);
        
        if (symbole == null) {
            gestionnaireErreurs.ajouterErreur("Variable '" + nomVar + "' non déclarée pour LIRE", 0);
        } else {
            tableSymboles.marquerUtilise(nomVar);
            tableSymboles.marquerInitialise(nomVar);
        }
    }
    
    private void effectuerVerificationsFinales() {
        List<String> nonUtilisees = tableSymboles.getVariablesNonUtilisees();
        for (String var : nonUtilisees) {
            gestionnaireErreurs.ajouterErreur("Variable '" + var + "' déclarée mais jamais utilisée", 0);
        }
        
        List<String> nonInitialisees = tableSymboles.getVariablesNonInitialisees();
        for (String var : nonInitialisees) {
            gestionnaireErreurs.ajouterErreur("Variable '" + var + "' déclarée mais jamais initialisée", 0);
        }
    }
    
    public TableDesSymboles getTableSymboles() {
        return tableSymboles;
    }
    
    public GestionnaireErreurs getGestionnaireErreurs() {
        return gestionnaireErreurs;
    }
}