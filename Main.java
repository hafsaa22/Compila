import java.io.*;
import java.util.*;

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

        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}
