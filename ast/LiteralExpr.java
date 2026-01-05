package ast;

public class LiteralExpr extends Expr {
    public Object valeur;

    public LiteralExpr(Object valeur) {
        this.valeur = valeur;
    }
}