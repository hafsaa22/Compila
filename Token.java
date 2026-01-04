public class Token {
    public TokenType type;
    public String valeur;

    public Token(TokenType type, String valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return (valeur == null) ? type.toString()
                                : type + " (" + valeur + ")";
    }
}
