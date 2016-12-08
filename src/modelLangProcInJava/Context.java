/** 
 * @class: Context
 * This class constructs Context object that has attributes : 
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

import java.util.Stack;

class Context {

    private final int HASH_SIZE = 211;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    public static Stack symbolStack;
    public static Stack typeStack;
    public static String currentStr;
    public static int currentLine;
    private boolean printSymbols;
    public int errorCount;
    public int functionType;
    public int temp;
    // Stack untuk memasukkan ON sebelum masuk bagian pemanggilan func/proc
    public static Stack<Integer> orderNumberStack;

    public Context() {
        lexicalLevel = -1;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        symbolStack = new Stack();
        typeStack = new Stack();
        orderNumberStack = new Stack<Integer>();
        printSymbols = false;
        errorCount = 0;
    }

    /**
     * This method chooses which action to be taken
     * @input : ruleNo(type:int)
     * @output: -(type:void)
     */
    public void C(int ruleNo) {
        boolean error = false;

        //System.out.println("C" + ruleNo);
        switch(ruleNo) {
            case 0:
                // Edit1
                //orderNumberStack.push(orderNumber);
                lexicalLevel++;
                orderNumber = 0;
                break;
            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;
            case 2:
                symbolHash.delete(lexicalLevel);
                // Edit1
                //orderNumber = orderNumberStack.peek();
                //orderNumberStack.pop();
                lexicalLevel--;
                break;
            case 3:
                if (symbolHash.isExist(currentStr, lexicalLevel)) {
                    System.out.println("Variable declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                } else {
                    symbolHash.insert(new Bucket(currentStr));
                }
                symbolStack.push(currentStr);
                break;
            case 4:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 5:
                symbolHash.find(currentStr).setIdType(((Integer)typeStack.peek()).intValue());
                break;
            case 6:
                if (!symbolHash.isExist(currentStr)) {
                    System.out.println("Variable undeclared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                } else {
                    symbolStack.push(currentStr);
                }
                break;
            case 7:
                symbolStack.pop();
                break;
            case 8:
                typeStack.push(new Integer(symbolHash.find(currentStr).getIdType()));
                break;
            case 9:
                typeStack.push(new Integer(Bucket.INTEGER));
                break;
            case 10:
                typeStack.push(new Integer(Bucket.BOOLEAN));
                break;
            case 11:
                typeStack.pop();
                break;
            case 12:
                // athin
                if (((Integer)typeStack.peek()).intValue() == Bucket.UNDEFINED) {
                    System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (((Integer)typeStack.peek()).intValue() != Bucket.INTEGER) {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 13:
                // athin
                if (((Integer)typeStack.peek()).intValue() == Bucket.UNDEFINED) {
                    System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (((Integer)typeStack.peek()).intValue() != Bucket.BOOLEAN) {
                    System.out.println("Type of boolean expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 14:
                temp = ((Integer)typeStack.pop()).intValue();
                if (temp != ((Integer)typeStack.peek()).intValue()) {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 15:
                temp = ((Integer)typeStack.pop()).intValue();
                if ((temp != Bucket.INTEGER) && ((Integer)typeStack.peek()).intValue() != Bucket.INTEGER) {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 16:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != ((Integer)typeStack.peek()).intValue()) {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 17:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != Bucket.INTEGER) {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 18:
                symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
                orderNumber++;
                break;
            case 19:
                symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
                orderNumber += 3;
                break;
            case 20:
                // athin
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.SCALAR) {
                    System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 21:
                // athin
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.ARRAY) {
                    System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 22:
                // EDIT1: Push lexicalLevel dan orderLevel
                symbolHash.find(currentStr).setLLON(lexicalLevel,-1);
                break;
            case 23:
                // EDIT1: Udah di-handle C36
                break;
            case 24:
                // EDIT1: Masukkan procedure ke dalam table simbol
                symbolHash.find(currentStr).setIdKind(Bucket.PROCEDURE);
                break;
            case 25:
                // TODO
                break;
            case 26:
                // EDIT1: Masukkan function ke dalam table simbol
                symbolHash.find(currentStr).setIdKind(Bucket.FUNCTION);
                break;
            case 27:
                // TODO
                break;
            case 28:
                // EDIT1: Check apakah currStr berupa procedure atau bukan
                // athin

                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Procedure is not defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.PROCEDURE) {
                    System.out.println("Procedure is expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 29:
                // TODO
                break;
            case 30:
                // TODO
                break;
            case 31:
                // TODO
                break;
            case 32:
                // TODO
                break;
            case 33:
                // EDIT1: Cek apakah merupakan function atau bukan
                // athin
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Function is not defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.FUNCTION) {
                    System.out.println("Function is expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 34:
                // TODO
                break;
            case 35:
                // TODO
                break;
            case 36:
                // EDIT2
                // irvi: check apakah return type sudah match dengan expression yang ada dalam function
                functionType = symbolHash.find((String)symbolStack.peek()).getIdType();
                int temp = ((Integer)typeStack.peek()).intValue();
                if (temp != functionType) {
                    System.out.println("Unmatched return type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 37:
                // EDIT2: 
                // irvi: if identifier merupakan suatu fungsi maka C 33 else C20 
                functionType = symbolHash.find((String)symbolStack.peek()).getIdKind();
                if(functionType==Bucket.FUNCTION) C(33);
                else C(20);
                break;
            case 38:
                // TODO
                break;
            case 39:
                // TODO
                break;
            case 40:
                // EDIT1: sudah di handle di c10 dan c09
                break;
            case 50:
                orderNumberStack.push(orderNumber);
                break;
            case 51:
                orderNumber = orderNumberStack.pop();
                break;
            case 52:
                int curr = Generate.cell;
                symbolHash.find(currentStr).setBaseAddress(curr);
                break;
            
        }
    }

    /**
     * This method sets the current token and line
     * @input : str(type:int), line(type:int)
     * @output: -(type:void)
     */
    public void setCurrent(String str, int line) {
        currentStr = str;
        currentLine = line;
    }

    /**
     * This method sets symbol printing option
     * @input : bool(type:boolean)
     * @output: -(type:void)
     */
    public void setPrint(boolean bool) {
        printSymbols = bool;
    }

}