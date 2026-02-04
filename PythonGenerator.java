import java.io.*;
import java.util.*;
import ast.*;
import java.nio.charset.StandardCharsets;

public class PythonGenerator implements CodeGenerator {
    private PrintWriter writer;
    private int indentation = 0;
    private Map<String, String> variableTypes = new HashMap<>();

    @Override
    public void generate(List<Instruction> ast, OutputStream out) throws IOException {
        this.writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);

        for (Instruction instruction : ast) {
            if (instruction instanceof VarDecl) {
                VarDecl decl = (VarDecl) instruction;
                if (decl.nom != null && decl.type != null) {
                    variableTypes.put(decl.nom, decl.type.toUpperCase());
                }
            }
        }

        for (Instruction instruction : ast) {
            generateInstruction(instruction);
        }

        writer.flush();
    }

    private void generateInstruction(Instruction instruction) {
        if (instruction instanceof Affectation) {
            generateAffectation((Affectation) instruction);
        } else if (instruction instanceof Affichage) {
            generateAffichage((Affichage) instruction);
        } else if (instruction instanceof LectureInstruction) {
            generateLecture((LectureInstruction) instruction);
        } else if (instruction instanceof SiInstruction) {
            generateSi((SiInstruction) instruction);
        } else if (instruction instanceof TantQueInstruction) {
            generateTantQue((TantQueInstruction) instruction);
        }
    }

    private void generateAffectation(Affectation affectation) {
        write(affectation.nom + " = " + generateExpr(affectation.valeur));
    }

    private void generateAffichage(Affichage affichage) {
        write("print(" + generateExpr(affichage.expression) + ")");
    }

    private void generateLecture(LectureInstruction lecture) {
        String varName = lecture.nomVariable;
        String declaredType = variableTypes.get(varName);

        if (declaredType != null) {
            if (declaredType.equals("ENTIER")) {
                write(varName + " = int(input())");
                return;
            }
            if (declaredType.equals("REEL")) {
                write(varName + " = float(input())");
                return;
            }
        }

        write(varName + " = input()");
    }

    private void generateSi(SiInstruction si) {
        write("if " + generateExpr(si.condition) + ":");
        indentation++;

        for (Instruction ins : si.alorsInstructions) {
            generateInstruction(ins);
        }

        if (si.sinonInstructions != null && !si.sinonInstructions.isEmpty()) {
            indentation--;
            write("else:");
            indentation++;
            for (Instruction ins : si.sinonInstructions) {
                generateInstruction(ins);
            }
        }

        indentation--;
    }

    private void generateTantQue(TantQueInstruction tantQue) {
        write("while " + generateExpr(tantQue.condition) + ":");
        indentation++;

        for (Instruction ins : tantQue.corps) {
            generateInstruction(ins);
        }

        indentation--;
    }

    private String generateExpr(Expr expr) {
        if (expr instanceof LiteralExpr) {
            return generateLiteral((LiteralExpr) expr);
        } else if (expr instanceof VariableExpr) {
            return generateVariable((VariableExpr) expr);
        } else if (expr instanceof BinaryExpr) {
            return generateBinary((BinaryExpr) expr);
        }
        return "None";
    }

    private String generateLiteral(LiteralExpr literal) {
        if (literal.valeur == null) {
            return "None";
        }

        if (literal.valeur instanceof Integer) {
            return literal.valeur.toString();
        }

        if (literal.valeur instanceof Double || literal.valeur instanceof Float) {
            return literal.valeur.toString();
        }

        if (literal.valeur instanceof Boolean) {
            return ((Boolean) literal.valeur) ? "True" : "False";
        }

        if (literal.valeur instanceof String) {
            String str = (String) literal.valeur;
            if (str.startsWith("'") && str.endsWith("'")) {
                return str;
            }
            return "\"" + str.replace("\"", "\\\"") + "\"";
        }

        return literal.valeur.toString();
    }

    private String generateVariable(VariableExpr variable) {
        return variable.nom;
    }

    private String generateBinary(BinaryExpr binary) {
        String gauche = generateExpr(binary.gauche);
        String droite = generateExpr(binary.droite);
        String op = mapOperateur(binary.operateur);

        return gauche + " " + op + " " + droite;
    }

    private String mapOperateur(String operateur) {
        switch (operateur) {
            case "PLUS":
                return "+";
            case "MOINS":
                return "-";
            case "MULT":
                return "*";
            case "DIV":
                return "/";
            case "MOD":
                return "%";
            case "EGAL_EGAL":
                return "==";
            case "DIFFERENT":
                return "!=";
            case "SUP":
                return ">";
            case "SUP_EGAL":
                return ">=";
            case "INF":
                return "<";
            case "INF_EGAL":
                return "<=";
            case "ET":
                return "and";
            case "OU":
                return "or";
            default:
                return operateur;
        }
    }

    private void write(String line) {
        for (int i = 0; i < indentation; i++) {
            writer.print("    ");
        }
        writer.println(line);
    }
}
