import java.io.*;
import java.util.*;
import ast.*; 

public class Main {
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("pseudocode.txt"));
        StringBuilder code = new StringBuilder();
        String ligne;

        while ((ligne = br.readLine()) != null) {
            code.append(ligne).append("\n");
        }
        br.close();

        Lexer lexer = new Lexer(code.toString());
        List<Token> tokens = lexer.analyser();

        System.out.println("--- Liste des Tokens ---");
        for (Token t : tokens) {
            System.out.println(t);
        }

        System.out.println("\n--- Construction de l'AST ---");
        try {
            Parser parser = new Parser(tokens);
            List<Instruction> ast = parser.parse();
            
            System.out.println("Succès : L'Arbre de Syntaxe Abstraite (AST) a été généré.");
            System.out.println("Nombre d'instructions détectées : " + ast.size());

            // Ici, on passera la liste 'ast' au membre suivant pour la sémantique
            // AnalyseurSemantique semantique = new AnalyseurSemantique(ast);
            // semantique.verifier();

        } catch (Exception e) {
            System.err.println("Erreur durant le parsing : " + e.getMessage());
        }
    }
}