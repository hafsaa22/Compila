package ast;

public class Affectation extends Instruction {
    public String nom;
    public Expr valeur;

    public Affectation(String nom, Expr valeur) {
        this.nom = nom;
        this.valeur = valeur;
    }
}