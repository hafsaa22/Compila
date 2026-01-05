package ast;

public class VarDecl extends Instruction {
    public String nom;
    public String type;

    public VarDecl(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }
}