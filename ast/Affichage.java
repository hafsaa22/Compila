package ast;

public class Affichage extends Instruction {
    public Expr expression;

    public Affichage(Expr expression) {
        this.expression = expression;
    }
}