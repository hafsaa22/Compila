import ast.*;
import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }


    private Token peek() { return tokens.get(current); }
    private boolean isAtEnd() { return peek().type == TokenType.EOF; }
    
    private Token consume(TokenType type, String message) {
        if (peek().type == type) return tokens.get(current++);
        throw new RuntimeException(message + " à " + peek().type);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (peek().type == type) {
                current++;
                return true;
            }
        }
        return false;
    }


    public List<Instruction> parse() {
        List<Instruction> instructions = new ArrayList<>();

        if (!isAtEnd() && peek().type == TokenType.OPTION) {
            consume(TokenType.OPTION, "Attendu le mot-clé OPTION");
            consume(TokenType.DEUX_POINTS, "Attendu :");
        

            Token langage = consume(TokenType.ID, "Attendu le langage cible");
            System.out.println("Cible de compilation détectée : " + langage.valeur);
        
            consume(TokenType.PV, "Attendu ;");
        }

        while (!isAtEnd()) {
            if (match(TokenType.PV)) continue; 
            instructions.add(declarationOuInstruction());
        }
        return instructions;
    }

    private Instruction parseLecture() {
        consume(TokenType.PO, "Attendu ( après LIRE");
        String nom = consume(TokenType.ID, "Attendu identifiant dans LIRE").valeur;
        consume(TokenType.PF, "Attendu ) après l'identifiant");
        consume(TokenType.PV, "Attendu ; après LIRE");
        return new LectureInstruction(nom); 
    }

    private Instruction declarationOuInstruction() {
        if (match(TokenType.VAR)) return parseVarDecl();
        if (match(TokenType.SI)) return parseSi();
        if (match(TokenType.TANT_QUE)) return parseTantQue();
        if (match(TokenType.AFFICHER)) return parseAffichage();
        if (match(TokenType.LIRE)) return parseLecture();
        return parseAffectation();
    }

    private Instruction parseVarDecl() {
        String nom = consume(TokenType.ID, "Attendu nom variable").valeur;
        consume(TokenType.DEUX_POINTS, "Attendu :");
        Token typeToken = tokens.get(current++); 
        consume(TokenType.PV, "Attendu ;");
        return new VarDecl(nom, typeToken.type.toString());
    }

    private Instruction parseSi() {
        Expr cond = parseExpression();
        consume(TokenType.ALORS, "Attendu ALORS");
        List<Instruction> alors = new ArrayList<>();
        while (peek().type != TokenType.SINON && peek().type != TokenType.FINSI) {
            alors.add(declarationOuInstruction());
        }
        List<Instruction> sinon = null;
        if (match(TokenType.SINON)) {
            sinon = new ArrayList<>();
            while (peek().type != TokenType.FINSI) {
                sinon.add(declarationOuInstruction());
            }
        }
        consume(TokenType.FINSI, "Attendu FINSI");
        match(TokenType.PV); 
        return new SiInstruction(cond, alors, sinon);
    }

    private Instruction parseAffichage() {
        consume(TokenType.PO, "Attendu (");
        Expr exp = parseExpression();
        consume(TokenType.PF, "Attendu )");
        consume(TokenType.PV, "Attendu ;");
        return new Affichage(exp);
    }


    private Expr parseExpression() {
        Expr gauche = parsePrimaire();
        if (match(TokenType.SUP, TokenType.INF, TokenType.EGAL_EGAL, TokenType.PLUS)) {
            String op = tokens.get(current-1).type.toString();
            Expr droite = parsePrimaire();
            return new BinaryExpr(gauche, op, droite);
        }
        return gauche;
    }

    private Expr parsePrimaire() {
        if (match(TokenType.NUM)) return new LiteralExpr(tokens.get(current-1).valeur);
        if (match(TokenType.ID)) return new VariableExpr(tokens.get(current-1).valeur);
        if (match(TokenType.TRUE)) return new LiteralExpr(true);
        if (match(TokenType.FALSE)) return new LiteralExpr(false);
        if (match(TokenType.CHAINE, TokenType.CAR)) return new LiteralExpr(tokens.get(current-1).valeur);
        throw new RuntimeException("Expression attendue");
    }

    private Instruction parseAffectation() {
        String nom = consume(TokenType.ID, "Attendu identifiant").valeur;
        consume(TokenType.EGAL, "Attendu =");
        Expr val = parseExpression();
        consume(TokenType.PV, "Attendu ;");
        return new Affectation(nom, val);
    }


    private Instruction parseTantQue() {
        Expr cond = parseExpression();
        consume(TokenType.FAIRE, "Attendu FAIRE");
        List<Instruction> corps = new ArrayList<>();
        while (peek().type != TokenType.FINTANTQUE) {
            corps.add(declarationOuInstruction());
        }
        consume(TokenType.FINTANTQUE, "Attendu FINTANTQUE");
        return new TantQueInstruction(cond, corps);
    }
}