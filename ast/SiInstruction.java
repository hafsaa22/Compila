package ast;
import java.util.List;

public class SiInstruction extends Instruction {
    public Expr condition;
    public List<Instruction> alorsInstructions;
    public List<Instruction> sinonInstructions;

    public SiInstruction(Expr condition, List<Instruction> alors, List<Instruction> sinon) {
        this.condition = condition;
        this.alorsInstructions = alors;
        this.sinonInstructions = sinon;
    }
}