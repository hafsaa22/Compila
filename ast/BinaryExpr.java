package ast;

public class BinaryExpr extends Expr {
    public Expr gauche;
    public String operateur;
    public Expr droite;

    public BinaryExpr(Expr gauche, String operateur, Expr droite) {
        this.gauche = gauche;
        this.operateur = operateur;
        this.droite = droite;
    }
}