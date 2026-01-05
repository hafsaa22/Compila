package ast;

public class VariableExpr extends Expr {
    public String nom;

    public VariableExpr(String nom) {
        this.nom = nom;
    }
}