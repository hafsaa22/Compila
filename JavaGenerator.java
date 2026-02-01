import java.io.*;
import java.util.*;
import ast.*;
import java.nio.charset.StandardCharsets;

public class JavaGenerator implements CodeGenerator {
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

        writeHeader();

        write("public static void main(String[] args) throws Exception {");
        indentation++;

        write("Scanner scanner = new Scanner(System.in);");
        writer.println();


        for (String varName : variableTypes.keySet()) {
            String javaType = mapTypeToJava(variableTypes.get(varName));
            write(javaType + " " + varName + ";");
        }

        if (!variableTypes.isEmpty()) {
            writer.println();
        }

        for (Instruction instruction : ast) {
            generateInstruction(instruction);
        }

        writer.println();
        write("scanner.close();");
        indentation--;
        write("}");
        indentation--;
        write("}");

        writer.flush();
    }

    private void writeHeader() {
        write("import java.util.Scanner;");
        write("");
        write("public class Output {");
        indentation++;
    }

    private void generateInstruction(Instruction instruction) {
        if (instruction instanceof VarDecl) {
            return;
        } else if (instruction instanceof Affectation) {
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
        write(affectation.nom + " = " + generateExpr(affectation.valeur) + ";");
    }

    private void generateAffichage(Affichage affichage) {
        write("System.out.println(" + generateExpr(affichage.expression) + ");");
    }

    private void generateLecture(LectureInstruction lecture) {
        String varName = lecture.nomVariable;
        String declaredType = variableTypes.get(varName);

        if (declaredType != null) {
            if (declaredType.equals("ENTIER") || declaredType.equals("INT")) {
                write(varName + " = scanner.nextInt();");
                return;
            }
            if (declaredType.equals("REEL") || declaredType.equals("FLOTTANT") || declaredType.equals("FLOAT")) {
                write(varName + " = scanner.nextDouble();");
                return;
            }
            if (declaredType.equals("BOOLEAN")) {
                write(varName + " = scanner.nextBoolean();");
                return;
            }
            if (declaredType.equals("CARACTERE")) {
                write(varName + " = scanner.nextLine().charAt(0);");
                return;
            }
        }

        write(varName + " = scanner.nextLine();");
    }

    private void generateSi(SiInstruction si) {
        write("if (" + generateExpr(si.condition) + ") {");
        indentation++;

        for (Instruction ins : si.alorsInstructions) {
            generateInstruction(ins);
        }

        if (si.sinonInstructions != null && !si.sinonInstructions.isEmpty()) {
            indentation--;
            write("} else {");
            indentation++;
            for (Instruction ins : si.sinonInstructions) {
                generateInstruction(ins);
            }
        }

        indentation--;
        write("}");
    }

    private void generateTantQue(TantQueInstruction tantQue) {
        write("while (" + generateExpr(tantQue.condition) + ") {");
        indentation++;

        for (Instruction ins : tantQue.corps) {
            generateInstruction(ins);
        }

        indentation--;
        write("}");
    }

    private String generateExpr(Expr expr) {
        if (expr instanceof LiteralExpr) {
            return generateLiteral((LiteralExpr) expr);
        } else if (expr instanceof VariableExpr) {
            return generateVariable((VariableExpr) expr);
        } else if (expr instanceof BinaryExpr) {
            return generateBinary((BinaryExpr) expr);
        }
        return "null";
    }

    private String generateLiteral(LiteralExpr literal) {
        if (literal.valeur == null) {
            return "null";
        }

        if (literal.valeur instanceof Integer) {
            return literal.valeur.toString();
        }

        if (literal.valeur instanceof Double || literal.valeur instanceof Float) {
            return literal.valeur.toString();
        }

        if (literal.valeur instanceof Boolean) {
            return ((Boolean) literal.valeur) ? "true" : "false";
        }

        if (literal.valeur instanceof String) {
            String str = (String) literal.valeur;

            if (str.length() == 1) {
                return "'" + str + "'";
            }

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
                return "&&";
            case "OU":
                return "||";
            default:
                return operateur;
        }
    }

    private String mapTypeToJava(String pseudoType) {
        switch (pseudoType.toUpperCase()) {
            case "ENTIER":
            case "INT":
                return "int";
            case "REEL":
            case "FLOTTANT":
            case "FLOAT":
                return "double";
            case "BOOLEAN":
                return "boolean";
            case "CARACTERE":
                return "char";
            case "CHAINE":
                return "String";
            default:
                return "Object";
        }
    }

    private void write(String line) {
        for (int i = 0; i < indentation; i++) {
            writer.print("    ");
        }
        writer.println(line);
    }
}
