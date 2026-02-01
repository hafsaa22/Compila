import java.io.*;
import java.util.*;
import ast.*;

public interface CodeGenerator {
    void generate(List<Instruction> ast, OutputStream out) throws IOException;
}
