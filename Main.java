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

        System.out.println("=== TOKENS GENERES ===");
        for (Token t : tokens) {
            System.out.println(t);
        }
        System.out.println("=====================\n");

        System.out.println("--- Construction de l'AST ---");
        Parser parser = new Parser(tokens);
        List<Instruction> ast = parser.parse();
        
        System.out.println("Succès : L'Arbre de Syntaxe Abstraite (AST) a été généré.");
        System.out.println("Nombre d'instructions détectées : " + ast.size());

        System.out.println("\n=== ANALYSE SEMANTIQUE ===");
        AnalyseurSemantique analyseurSemantique = new AnalyseurSemantique(ast);
        analyseurSemantique.analyser();

        System.out.println("\n=== GENERATION DE CODE ===");

        String langage = tokens.get(2).valeur;

        try {
            CodeGenerator generator;
            String fichierSortie;

            switch (langage.toLowerCase()) {
                case "python":
                    generator = new PythonGenerator();
                    fichierSortie = "output.py";
                    break;
                case "java":
                    generator = new JavaGenerator();
                    fichierSortie = "Output.java";
                    break;
                default:
                    throw new Exception("Langage non supporté ou non spécifié: " + langage);
            }

            FileOutputStream fos = new FileOutputStream(fichierSortie);
            generator.generate(ast, fos);
            fos.close();

            System.out.println("Code généré avec succès dans : " + fichierSortie);
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération de code : " + e.getMessage());
            e.printStackTrace();
        }
    }
}