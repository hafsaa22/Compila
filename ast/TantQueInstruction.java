package ast;
import java.util.List;

public class TantQueInstruction extends Instruction {
    public Expr condition;
    public List<Instruction> corps;

    public TantQueInstruction(Expr condition, List<Instruction> corps) {
        this.condition = condition;
        this.corps = corps;
    }
}