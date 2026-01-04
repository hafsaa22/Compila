import java.util.*;

public class Lexer {

    private String input;
    private int pos = 0;
    private char current;

    public Lexer(String input) {
        this.input = input;
        current = input.length() > 0 ? input.charAt(0) : '\0';
    }

    private void avancer() {
        pos++;
        current = (pos < input.length()) ? input.charAt(pos) : '\0';
    }

    private void ignorerEspaces() {
        while (Character.isWhitespace(current)) avancer();
    }

    private String lireIdentifiant() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(current) || current == '_') {
            sb.append(current);
            avancer();
        }
        return sb.toString();
    }

    private String lireNombre() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(current)) {
            sb.append(current);
            avancer();
        }
        return sb.toString();
    }

    private String lireChaine() {
        avancer(); // sauter "
        StringBuilder sb = new StringBuilder();
        while (current != '"' && current != '\0') {
            sb.append(current);
            avancer();
        }
        avancer(); // sauter "
        return sb.toString();
    }

    public List<Token> analyser() {
        List<Token> tokens = new ArrayList<>();

        while (current != '\0') {

            if (Character.isWhitespace(current)) {
                ignorerEspaces();
                continue;
            }

            if (Character.isDigit(current)) {
                tokens.add(new Token(TokenType.NUM, lireNombre()));
                continue;
            }

            if (Character.isLetter(current)) {
                String mot = lireIdentifiant().toUpperCase();

                switch (mot) {
                    case "VAR": tokens.add(new Token(TokenType.VAR, null)); break;
                    case "SI": tokens.add(new Token(TokenType.SI, null)); break;
                    case "ALORS": tokens.add(new Token(TokenType.ALORS, null)); break;
                    case "SINON": tokens.add(new Token(TokenType.SINON, null)); break;
                    case "FINSI": tokens.add(new Token(TokenType.FINSI, null)); break;
                    case "TANT_QUE": tokens.add(new Token(TokenType.TANT_QUE, null)); break;
                    case "FAIRE": tokens.add(new Token(TokenType.FAIRE, null)); break;
                    case "FINTANTQUE": tokens.add(new Token(TokenType.FINTANTQUE, null)); break;
                    case "AFFICHER": tokens.add(new Token(TokenType.AFFICHER, null)); break;
                    case "LIRE": tokens.add(new Token(TokenType.LIRE, null)); break;
                    case "ENTIER": tokens.add(new Token(TokenType.ENTIER, null)); break;
                    case "BOOLEAN": tokens.add(new Token(TokenType.BOOLEAN, null)); break;
                    case "CARACTERE": tokens.add(new Token(TokenType.CARACTERE, null)); break;
                    case "TRUE": tokens.add(new Token(TokenType.TRUE, null)); break;
                    case "FALSE": tokens.add(new Token(TokenType.FALSE, null)); break;
                    default: tokens.add(new Token(TokenType.ID, mot));
                }
                continue;
            }

            switch (current) {
                case '+': tokens.add(new Token(TokenType.PLUS, null)); avancer(); break;
                case '<': tokens.add(new Token(TokenType.INF, null)); avancer(); break;
                case '>': tokens.add(new Token(TokenType.SUP, null)); avancer(); break;

                case '=':
                    avancer();
                    if (current == '=') {
                        tokens.add(new Token(TokenType.EGAL_EGAL, null));
                        avancer();
                    } else {
                        tokens.add(new Token(TokenType.EGAL, null));
                    }
                    break;

                case ':': tokens.add(new Token(TokenType.DEUX_POINTS, null)); avancer(); break;
                case ';': tokens.add(new Token(TokenType.PV, null)); avancer(); break;
                case '(' : tokens.add(new Token(TokenType.PO, null)); avancer(); break;
                case ')' : tokens.add(new Token(TokenType.PF, null)); avancer(); break;

                case '"':
                    tokens.add(new Token(TokenType.CHAINE, lireChaine()));
                    break;

                case '\'':
                    avancer();
                    char c = current;
                    avancer(); // lettre
                    avancer(); // '
                    tokens.add(new Token(TokenType.CAR, String.valueOf(c)));
                    break;

                default:
                    tokens.add(new Token(TokenType.ERREUR, String.valueOf(current)));
                    avancer();
            }
        }

        tokens.add(new Token(TokenType.EOF, null));
        return tokens;
    }
}
