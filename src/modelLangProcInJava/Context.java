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
import java.util.LinkedList;

class Context {
    private final int HASH_SIZE = 211;
    private final int INIT = -1;
    private final int INIT_PARAM = 0;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    public static Stack symbolStack;
    public static Stack typeStack;
    public static String currentStr;
    public static int currentLine;
    public static int currBaseAddr;
    private boolean printSymbols;
    public int errorCount;
    public int functionType;
    public int temp;

    /**
    * IRVI: Stack untuk memasukkan ON sebelum masuk bagian pemanggilan func/proc
    */
    public static Stack<Integer> orderNumberStack;

    /**
    * IRVI 2: Stack untuk memasukkan jumlah args
    */
    public static Stack<Integer> argumentCountStack;

    public Context() {
        lexicalLevel = INIT;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        symbolStack = new Stack();
        typeStack = new Stack();
        orderNumberStack = new Stack<Integer>();
        argumentCountStack = new Stack<Integer>();
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

        switch(ruleNo) {
            case 0:
                lexicalLevel++;
                orderNumber = 0;
                break;
            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;
            case 2:
                symbolHash.delete(lexicalLevel);
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
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Integer atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
                if (((Integer)typeStack.peek()).intValue() == Bucket.UNDEFINED) {
                    System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (((Integer)typeStack.peek()).intValue() != Bucket.INTEGER) {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 13:
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Boolean atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
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
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Scalar atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.SCALAR) {
                    System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 21:                
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Array atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.ARRAY) {
                    System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 22:
                /**
                * IRVI
                * Lakukan inisiasi lexical level dan order level dari function atau 
                * procedure ke dalam symbol table
                */
                symbolHash.find(currentStr).setLLON(lexicalLevel,INIT);
                break;
            case 23:
                /**
                * ATHIN & IRVI
                * Rule ini sudah di handle di C36
                */
                break;
            case 24:
                /**
                * IRVI
                * Masukkan procedure ke dalam symbol table
                * IRVI 2
                * Masukkan paraam ke procdure
                */
                symbolHash.find(currentStr).setIdKind(Bucket.PROCEDURE);
                symbolHash.find(currentStr).setParameterList(new LinkedList<Bucket>());
                break;
            case 25:
                // TODO
                break;
            case 26:
                /**
                * IRVI
                * Masukkan function ke dalam symbol table
                * IRVI 2
                * MAsukkan paraam ke function
                */
                symbolHash.find(currentStr).setIdKind(Bucket.FUNCTION);
                symbolHash.find(currentStr).setParameterList(new LinkedList<Bucket>());
                break;
            case 27:
                // TODO
                break;
            case 28:
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Procedure atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Procedure is not defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.PROCEDURE) {
                    System.out.println("Procedure is expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 29:
                /**
                * IRVI 2\3
                * Check apakah function atau procedure tidak memiliki param
                */
                int paramCount = symbolHash.find((String)symbolStack.peek()).getParamCount();
                if(paramCount!=0) {
                    System.out.println("Procedure or Function in line " + currentLine + ": " + currentStr + " is not expected to have any parameters");
                    errorCount++;
                }
                break;
            case 30:
                /**
                * IRVI 2
                * Push jumlah parameter = 0
                */
                argumentCountStack.push(INIT_PARAM);
                break;
            case 31:
                // TODO
                break;
            case 32:
                /**
                * IRVI 3
                * Periksa apakah sudah dilihat apa belum
                * pop jumalh argument
                */
                int currCount = argumentCountStack.pop();
                int paramCount = symbolHash.find((String)symbolStack.peek()).getParamCount();
                if(currCount!=paramCount) {
                    System.out.println("Unmatched number of arguments on line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 33:
                /**
                * ATHIN
                * Lakukan pengecekan apakah entry pada table simbol merupakan Function atau bukan
                * jika bukan maka berikan notifikasi error untuk line tersebut
                */
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Function is not defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                } else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.FUNCTION) {
                    System.out.println("Function is expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 34:
                /**
                * IRVI 3
                * Tambahkan nilai jumlah argument (?)
                */
                int paramCount = argumentCountStack.pop();
                paramCount++;
                argumentCountStack.push(paramCount);
                break;
            case 35:
                // TODO
                break;
            case 36:
                /**
                * ATHIN & IRVI
                * Check apakah return type sudah match dengan expression yang ada dalam function
                */
                functionType = symbolHash.find((String)symbolStack.peek()).getIdType();
                temp = ((Integer)typeStack.peek()).intValue();
                if (temp != functionType) {
                    System.out.println("Unmatched return type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 37:
                /**
                * IRVI
                * Check apakah identifier yang ada merupakan suatu fungsi atau bukan
                * jika merupakan suatu fungsi maka terapkan C33
                * jika bukan, maka terapkan C20
                */
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
                /**
                * ATHIN & IRVI
                * Rule ini sudah di-handle di C9 dan C10
                */
                break;
            case 50:
                orderNumberStack.push(orderNumber);
                break;
            case 51:
                orderNumber = orderNumberStack.pop();
                break;
            case 52:
                currBaseAddr = Generate.cell;
                symbolHash.find(currentStr).setBaseAddress(currBaseAddr);
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