/** 
 * @class: Generate
 * This class generates codes as part of the whole
 * compilation process.
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

class Generate {
    final int stackSize   = 102,
              loopMarker  = -1,
              scopeMarker = -1;

    int[] R7R9Stack         = new int[stackSize], // unconditional branches
          R8R10Stack        = new int[stackSize], // conditional branches
          R51R52Stack       = new int[stackSize], // exit statements
          R11R12Stack       = new int[stackSize], // loops
          dynamicArrayStack = new int[stackSize], // allocating arrays
          lastArrayStack    = new int[stackSize], // referencing arrays
          divStack          = new int[stackSize], // divide by zero routine
          printStack        = new int[stackSize], // print routine
          boundStack        = new int[stackSize], // subscript out of range routine
          returnAddrStack   = new int[stackSize]; // fixing up/backpatching return address

    /**
    * IRVI
    * Make the identifier as public static, jadi callable dari kelas lain
    * ini dibutuhkan untuk set base adress-nya dari Context.java
    */
    public static int ll, on, top, addr, kode, cell;

    // current func or proc yang dipanggil
    public String currCallName;
    // current param count
    public int currNumberOfParams;
    
    private String currConst;

    public Generate() {
        stackInit(R7R9Stack);
        stackInit(R8R10Stack);
        stackInit(R51R52Stack);
        stackInit(R11R12Stack);
        stackInit(dynamicArrayStack);
        stackInit(lastArrayStack);
        stackInit(divStack);
        stackInit(printStack);
        stackInit(boundStack);
        stackInit(returnAddrStack);

        cell = 0;
    }

    void stackInit(int[] stack) {
        stack[0] = 1;
    }

    void stackPush(int item, int[] stack) {
        stack[stack[0]] = item;
        stack[0] = stack[0] + 1;
        if (stack[0] > stackSize-1)
            System.out.println("Stack overflow in code generator.");
    }

    int stackPop(int[] stack, int top) {
        stack[0] = stack[0] - 1;
        if (stack[0] < 1) {
            System.out.println("Stack underflow in code generator.");
            System.exit(1);
        }
        top = stack[stack[0]];

        return top;
    }

    boolean isStackEmpty(int[] stack) {
        boolean flag = false;

        if (stack[0] == 1)
            flag = true;

        return flag;
    }

    int stackTop(int[] stack) {
        int ptr;

        ptr = stack[0] - 1;
        top = stack[ptr];

        return top;
    }

    // Method for NOT instruction
    void emitNot() {
        HMachine.memory[cell] = HMachine.PUSH;
        HMachine.memory[cell+1] = 0;
        HMachine.memory[cell+2] = HMachine.EQ;

        cell = cell + 3;
    }

    // Method to retrieve the address of a variable
    void obtainAddress() {
        HMachine.memory[cell] = HMachine.NAME;
        HMachine.memory[cell+1] = 
            Context.symbolHash.find(Context.currentStr).getLexicLev();
        HMachine.memory[cell+2] = 
            Context.symbolHash.find(Context.currentStr).getOrderNum();

        cell = cell + 3;
    }

    // Method to construct a routine to print text,
    // and to backpatch the address which called the routine
    void createPrintRoutine() {
        while (!isStackEmpty(printStack)) {
            top = stackPop(printStack, top);
            HMachine.memory[top] = cell;
        }

        HMachine.memory[cell] = HMachine.FLIP;
        HMachine.memory[cell+1] = HMachine.PRINTC;
        HMachine.memory[cell+2] = HMachine.PUSH;
        HMachine.memory[cell+3] = 1;
        HMachine.memory[cell+4] = HMachine.SUB;
        HMachine.memory[cell+5] = HMachine.PUSH;
        HMachine.memory[cell+6] = 2;
        HMachine.memory[cell+7] = HMachine.DUP;
        HMachine.memory[cell+8] = HMachine.PUSH;
        HMachine.memory[cell+9] = 0;
        HMachine.memory[cell+10] = HMachine.EQ;
        HMachine.memory[cell+11] = HMachine.PUSH;
        HMachine.memory[cell+12] = cell;
        HMachine.memory[cell+13] = HMachine.BF;
        HMachine.memory[cell+14] = HMachine.PUSH;
        HMachine.memory[cell+15] = 1;
        HMachine.memory[cell+16] = HMachine.POP;
        HMachine.memory[cell+17] = HMachine.BR;

        cell = cell + 18;
    }

    // Method to construct a division-by-zero error routine,
    // and to backpatch the address which called the routine
    void createCheckDiv() {
        char[] message = ("Error division by 0 on line ").toCharArray();

        while (!isStackEmpty(divStack)) {
            top = stackPop(divStack, top);
            HMachine.memory[top] = cell;
        }

        HMachine.memory[cell] = HMachine.PUSH;
        HMachine.memory[cell+1] = 0;
        HMachine.memory[cell+2] = HMachine.EQ;
        HMachine.memory[cell+3] = HMachine.FLIP;
        HMachine.memory[cell+4] = HMachine.BF;

        cell = cell + 5;

        for (int i = 0; i < message.length; i++) {
            HMachine.memory[cell] = HMachine.PUSH;
            HMachine.memory[cell+1] = message[message.length - i - 1];

            cell = cell + 2;
        }

        HMachine.memory[cell] = HMachine.PUSH;
        HMachine.memory[cell+1] = message.length;
        HMachine.memory[cell+2] = HMachine.PUSH;
        stackPush(cell+3, printStack);
        HMachine.memory[cell+4] = HMachine.BR;

        cell = cell + 5;
    }

    // Method to construct an array-index-out-of-bounds error routine,
    // and to backpatch the address which called the routine
    void createOutOfRangeMessage() {
        char[] message = ("Error - subscript out of range on line ").toCharArray();

        while (!isStackEmpty(boundStack)) {
            top = stackPop(boundStack, top);
            HMachine.memory[top] = cell;
        }

        HMachine.memory[cell] = HMachine.PUSH;
        HMachine.memory[cell+1] = 2 * message.length + 7 + cell;

        cell = cell + 2;

        for (int i = 0; i < message.length; i++) {
            HMachine.memory[cell] = HMachine.PUSH;
            HMachine.memory[cell+1] = message[message.length - i - 1];

            cell = cell + 2;
        }

        HMachine.memory[cell] = HMachine.PUSH;
        HMachine.memory[cell+1] = message.length;
        HMachine.memory[cell+2] = HMachine.PUSH;
        stackPush(cell+3, printStack);
        HMachine.memory[cell+4] = HMachine.BR;
        HMachine.memory[cell+5] = HMachine.PRINTI;
        HMachine.memory[cell+6] = HMachine.HALT;

        cell = cell + 7;
    }

    // Method to perform the code generation rules
    public void R(int ruleNo) {
        String teks;

        //System.out.println("R" + ruleNo);
        switch(ruleNo) {
            // R0 : assign pc and mt
            case 0:
                HMachine.mt = cell;
                HMachine.pc = 0;
                break;

            // R1 : check if the (lexical level < display size),
            //      contruct instructions to enter statement's scope
            case 1:
                ll = Context.lexicalLevel;
                if (ll > HMachine.displaySize)
                    System.out.println("Too many nested scope.");
                else {
                    HMachine.memory[cell] = HMachine.NAME;
                    HMachine.memory[cell+1] = ll;
                    HMachine.memory[cell+2] = 0;
                    HMachine.memory[cell+3] = HMachine.PUSHMT;
                    HMachine.memory[cell+4] = HMachine.SETD;
                    HMachine.memory[cell+5] = ll;

                    cell = cell + 6;
                    stackPush(scopeMarker, dynamicArrayStack);
                 }
                break;
            
            // R2 : check if (lexical level < display size),
            //      construct instructions to enter expression's scope
            case 2:
                ll = Context.lexicalLevel;
                if (ll > HMachine.displaySize)
                    System.out.println("Too many nested scope.");
                else {
                    HMachine.memory[cell] = HMachine.PUSH;
                    HMachine.memory[cell+1] = HMachine.undefined;
                    HMachine.memory[cell+2] = HMachine.NAME;
                    HMachine.memory[cell+3] = ll;
                    HMachine.memory[cell+4] = 0;
                    HMachine.memory[cell+5] = HMachine.PUSHMT;
                    HMachine.memory[cell+6] = HMachine.SETD;
                    HMachine.memory[cell+7] = ll;

                    cell = cell + 8;
                    stackPush(scopeMarker, dynamicArrayStack);
                 }
                break;

            // R3 : construct instructions to allocate variable
            case 3:
                on = stackPop(dynamicArrayStack, on);
                while (!(on == scopeMarker)) {
                    HMachine.memory[cell] = HMachine.PUSHMT;
                    HMachine.memory[cell+1] = HMachine.NAME;
                    HMachine.memory[cell+2] = Context.lexicalLevel;
                    HMachine.memory[cell+3] = on;
                    HMachine.memory[cell+4] = HMachine.FLIP;
                    HMachine.memory[cell+5] = HMachine.STORE;
                    HMachine.memory[cell+6] = HMachine.PUSH;
                    HMachine.memory[cell+7] = HMachine.undefined;
                    HMachine.memory[cell+8] = HMachine.NAME;
                    HMachine.memory[cell+9] = Context.lexicalLevel;
                    HMachine.memory[cell+10] = on + 1;
                    HMachine.memory[cell+11] = HMachine.LOAD;
                    HMachine.memory[cell+12] = HMachine.DUP;

                    cell = cell + 13;
                    on = stackPop(dynamicArrayStack, on);
                }
                break;

            // R5 : construct instructions to delete variable,
            //      and exit from statement's scope
            case 5:
                ll = Context.lexicalLevel + 1;

                HMachine.memory[cell] = HMachine.PUSHMT;
                HMachine.memory[cell+1] = HMachine.NAME;
                HMachine.memory[cell+2] = ll;
                HMachine.memory[cell+3] = 0;
                HMachine.memory[cell+4] = HMachine.SUB;
                HMachine.memory[cell+5] = HMachine.POP;
                HMachine.memory[cell+6] = HMachine.SETD;
                HMachine.memory[cell+7] = ll;

                cell = cell + 8;
                break;

            // R6 : construct instructions to move calculation result,
            //      delete variable, and exit from expression's scope
            case 6:
                ll = Context.lexicalLevel + 1;

                HMachine.memory[cell] = HMachine.NAME;
                HMachine.memory[cell+1] = ll;
                HMachine.memory[cell+2] = -2;
                HMachine.memory[cell+3] = HMachine.FLIP;
                HMachine.memory[cell+4] = HMachine.STORE;
                HMachine.memory[cell+5] = HMachine.PUSHMT;
                HMachine.memory[cell+6] = HMachine.NAME;
                HMachine.memory[cell+7] = ll;
                HMachine.memory[cell+8] = 0;
                HMachine.memory[cell+9] = HMachine.SUB;
                HMachine.memory[cell+10] = HMachine.POP;
                HMachine.memory[cell+11] = HMachine.SETD;
                HMachine.memory[cell+12] = ll;

                cell = cell + 13;
                break;

            // R7 : construct instructions for forward branch
            case 7:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = HMachine.undefined;
                HMachine.memory[cell+2] = HMachine.BR;
                stackPush(cell+1, R7R9Stack);

                cell = cell + 3;
                break;

            // R8 : construct instructions for conditional branch
            case 8:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = HMachine.undefined;
                HMachine.memory[cell+2] = HMachine.BF;
                stackPush(cell+1, R8R10Stack);

                cell = cell + 3;
                break;

            // R9 : construct instructions to fix address for forward branch
            case 9:
                top = stackPop(R7R9Stack, top);
                HMachine.memory[top] = cell;
                break;

            // R10 : construct instructions to fix address for conditional branch
            case 10:
                top = stackPop(R8R10Stack, top);
                HMachine.memory[top] = cell;
                break;

            // R11 : construct instruction to keep address for backward branch
            case 11:
                stackPush(cell, R11R12Stack);
                break;

            // R12 : construct instructions for backward branch
            case 12:
                HMachine.memory[cell] = HMachine.PUSH;
                top = stackPop(R11R12Stack, top);
                HMachine.memory[cell+1] = top;
                HMachine.memory[cell+2] = HMachine.BR;

                cell = cell + 3;
                break;

            // R13 : construct instructions for unary substract operation
            case 13:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 0;
                HMachine.memory[cell+2] = HMachine.FLIP;
                HMachine.memory[cell+3] = HMachine.SUB;

                cell = cell + 4;
                break;

            // R14 : construct instruction for add operation
            case 14:
                HMachine.memory[cell] = HMachine.ADD;

                cell = cell + 1;
                break;

            // R15 : construct instruction for substract operation
            case 15:
                HMachine.memory[cell] = HMachine.SUB;

                cell = cell + 1;
                break;

            // R16 : construct instruction for multiply operation
            case 16:
                HMachine.memory[cell] = HMachine.MUL;

                cell = cell + 1;
                break;

            // R17 : construct instructions for divide operation
            case 17:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 2;
                HMachine.memory[cell+2] = HMachine.DUP;
                HMachine.memory[cell+3] = HMachine.PUSH;
                HMachine.memory[cell+4] = cell + 12;
                HMachine.memory[cell+5] = HMachine.FLIP;
                HMachine.memory[cell+6] = HMachine.PUSH;
                HMachine.memory[cell+7] = cell + 16;
                HMachine.memory[cell+8] = HMachine.FLIP;
                HMachine.memory[cell+9] = HMachine.PUSH;
                stackPush(cell + 10, divStack);
                HMachine.memory[cell+11] = HMachine.BR;
                HMachine.memory[cell+12] = HMachine.PUSH;
                HMachine.memory[cell+13] = Context.currentLine;
                HMachine.memory[cell+14] = HMachine.PRINTI;
                HMachine.memory[cell+15] = HMachine.HALT;
                HMachine.memory[cell+16] = HMachine.PUSH;
                HMachine.memory[cell+17] = 1;
                HMachine.memory[cell+18] = HMachine.POP;
                HMachine.memory[cell+19] = HMachine.DIVI;

                cell = cell + 20;
                break;

            // R18 : construct instructions for boolean negation operation
            case 18:
                emitNot();
                break;

            // R19 : construct instruction for boolean AND operation
            case 19:
                HMachine.memory[cell] = HMachine.MUL;

                cell = cell + 1;
                break;

            // R20 : construct instruction for boolean OR operation
            case 20:
                HMachine.memory[cell] = HMachine.ORI;

                cell = cell + 1;
                break;

            // R21 : construct instruction for equality operation
            case 21:
                HMachine.memory[cell] = HMachine.EQ;

                cell = cell + 1;
                break;

            // R22 : construct instructions for unequality operation
            case 22:
                HMachine.memory[cell] = HMachine.EQ;

                cell = cell + 1;
                emitNot();
                break;

            // R23 : construct instruction for less-than operation
            case 23:
                HMachine.memory[cell] = HMachine.LT;

                cell = cell + 1;
                break;

            // R24 : construct instruction for less-than-or-equal operation
            case 24:
                HMachine.memory[cell] = HMachine.FLIP;
                HMachine.memory[cell+1] = HMachine.LT;

                cell = cell + 2;
                emitNot();
                break;

            // R25 : construct instruction for larger-than operation
            case 25:
                HMachine.memory[cell] = HMachine.FLIP;
                HMachine.memory[cell+1] = HMachine.LT;

                cell = cell + 2;
                break;

            // R26 : construct instruction for larger-that-or-equal operation
            case 26:
                HMachine.memory[cell] = HMachine.LT;

                cell = cell + 1;
                emitNot();
                break;

            // R27 : construct instructions to read and store integer value
            case 27:
                HMachine.memory[cell] = HMachine.READI;
                HMachine.memory[cell+1] = HMachine.STORE;

                cell = cell + 2;
                break;

            // R28 : construct instruction to print integer value
            case 28:
                HMachine.memory[cell] = HMachine.PRINTI;

                cell = cell + 1;
                break;

            // R29 : construct instructions to print text
            case 29:
                teks = currConst;
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = cell + (2 *teks.length()) + 7;
                for (int i = 1; i <= teks.length(); i++)
                {
                    HMachine.memory[cell+(2*i)] = HMachine.PUSH;
                    HMachine.memory[cell+(2*i)+1] = teks.charAt(teks.length()-i);
                }
                HMachine.memory[cell+2+(2*teks.length())] = HMachine.PUSH;
                HMachine.memory[cell+3+(2*teks.length())] = teks.length();
                HMachine.memory[cell+4+(2*teks.length())] = HMachine.PUSH;
                stackPush(cell+5+(2*teks.length()), printStack);
                HMachine.memory[cell+6+(2*teks.length())] = HMachine.BR;

                cell = HMachine.memory[cell+1];
                break;

            // R30 : construct instructions to move the cursor to a new line
            case 30:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 10;
                HMachine.memory[cell+2] = HMachine.PUSH;
                HMachine.memory[cell+3] = 13;
                HMachine.memory[cell+4] = HMachine.PRINTC;
                HMachine.memory[cell+5] = HMachine.PRINTC;

                cell = cell + 6;
                break;

            // R31 : construct instructions to retrieve variable's address
            case 31:
                obtainAddress();
                break;

            // R32 : construct instructions to retrieve variable's value
            case 32:
                HMachine.memory[cell] = HMachine.LOAD;

                cell = cell + 1;
                break;

            // R33 : construct STORE instruction
            case 33:
                HMachine.memory[cell] = HMachine.STORE;

                cell = cell + 1;
                break;

            // R34 : construct PUSH 0 instruction
            case 34:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 0;

                cell = cell + 2;
                break;

            // R35 : construct PUSH 1 instruction
            case 35:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 1;

                cell = cell + 2;
                break;

            // R36 : construct PUSH instruction for an integer value
            case 36:
                HMachine.memory[cell] = HMachine.PUSH;
                //HMachine.memory[cell+1] = Integer.parseInt(Context.currentStr);
                HMachine.memory[cell+1] = Integer.parseInt(currConst);

                cell = cell + 2;
                break;

            // R37 : construct instructions to allocate space for variable
            case 37:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = HMachine.undefined;

                cell = cell + 2;
                break;

            // R38 : construct HALT instruction
            case 38:
                HMachine.memory[cell] = HMachine.HALT;
                cell = cell + 1;

                if (!isStackEmpty(divStack))
                    createCheckDiv();
                if (!isStackEmpty(boundStack))
                    createOutOfRangeMessage();
                if (!isStackEmpty(printStack))
                    createPrintRoutine();
                break;

            // R39 : construct instructions to check non-negativity of array bounds,
            //       and to allocate array
            case 39:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 2;
                HMachine.memory[cell+2] = HMachine.DUP;
                HMachine.memory[cell+3] = HMachine.PUSH;
                HMachine.memory[cell+4] = 0;
                HMachine.memory[cell+5] = HMachine.LT;
                HMachine.memory[cell+6] = HMachine.PUSH;
                HMachine.memory[cell+7] = cell + 14;
                HMachine.memory[cell+8] = HMachine.BF;
                HMachine.memory[cell+9] = HMachine.PUSH;
                HMachine.memory[cell+10] = Context.currentLine;
                HMachine.memory[cell+11] = HMachine.PUSH;
                stackPush(cell + 12, boundStack);
                HMachine.memory[cell+13] = HMachine.BR;
                HMachine.memory[cell+14] = HMachine.PUSH;
                HMachine.memory[cell+15] = HMachine.undefined;
                HMachine.memory[cell+16] = HMachine.FLIP;

                cell = cell + 17;
                stackPush(Context.orderNumber, dynamicArrayStack);
                break;

            // R40 : construct instructions to retrieve array address
            case 40:
                ll = Context.symbolHash.find(Context.currentStr).getLexicLev();
                on = Context.symbolHash.find(Context.currentStr).getOrderNum();

                HMachine.memory[cell] = HMachine.NAME;
                HMachine.memory[cell+1] = ll;
                HMachine.memory[cell+2] = on;
                HMachine.memory[cell+3] = HMachine.LOAD;

                cell = cell + 4;
                stackPush(ll, lastArrayStack);
                stackPush(on, lastArrayStack);
                break;

            // R41 : construct instructions to check array index is inside the bounds
            case 41:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = 3;
                HMachine.memory[cell+2] = HMachine.DUP;
                HMachine.memory[cell+3] = HMachine.PUSH;
                HMachine.memory[cell+4] = 0;
                HMachine.memory[cell+5] = HMachine.LT;
                HMachine.memory[cell+6] = HMachine.PUSH;
                HMachine.memory[cell+7] = cell + 12;
                HMachine.memory[cell+8] = HMachine.BF;
                HMachine.memory[cell+9] = HMachine.PUSH;
                HMachine.memory[cell+10] = cell + 23;
                HMachine.memory[cell+11] = HMachine.BR;
                HMachine.memory[cell+12] = HMachine.NAME;
                on = stackPop(lastArrayStack, on);
                ll = stackPop(lastArrayStack, ll);
                HMachine.memory[cell+13] = ll;
                HMachine.memory[cell+14] = on + 1;
                HMachine.memory[cell+15] = HMachine.LOAD;
                HMachine.memory[cell+16] = HMachine.LT;
                HMachine.memory[cell+17] = HMachine.PUSH;
                HMachine.memory[cell+18] = 0;
                HMachine.memory[cell+19] = HMachine.EQ;
                HMachine.memory[cell+20] = HMachine.PUSH;
                HMachine.memory[cell+21] = cell + 28;
                HMachine.memory[cell+22] = HMachine.BF;
                HMachine.memory[cell+23] = HMachine.PUSH;
                HMachine.memory[cell+24] = Context.currentLine;
                HMachine.memory[cell+25] = HMachine.PUSH;
                stackPush(cell + 26, boundStack);
                HMachine.memory[cell+27] = HMachine.BR;
                HMachine.memory[cell+28] = HMachine.ADD;

                cell = cell + 29;
                break;

            // R49 : construct instructions similar to R31
            //       for non-function identifier
            case 49:
                kode = Context.symbolHash.find(Context.currentStr).getIdKind();

                if (kode == Bucket.FUNCTION)
                    R(46);
                else
                    R(31);

                break;

            // R50 : construct instructions similar to R32
            //       for non-function identifier
            case 50:
                kode = Context.symbolHash.find(Context.currentStr).getIdKind();

                if (kode == Bucket.FUNCTION)
                    R(47);
                else
                    R(32);

                break;

            // R51 : construct instructions for end of the loop
            case 51:
                top = stackPop(R51R52Stack, top);
                while (!(top == loopMarker))
                {
                    HMachine.memory[top] = cell;
                    top = stackPop(R51R52Stack, top);
                }
                break;

            // R52 : construct instructions for exit statement
            case 52:
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = HMachine.undefined;
                HMachine.memory[cell+2] = HMachine.BR;
                stackPush(cell + 1, R51R52Stack);

                cell = cell + 3;
                break;

            // R53 : construct instruction for start of the loop
            case 53:
                stackPush(loopMarker, R51R52Stack);
                break;

            case 42:
                currCallName = Context.callNameStack.peek();
                currNumberOfParams = Context.symbolHash.find(currCallName).getNumberOfParams();
                if(currNumberOfParams == 0) {
                    HMachine.memory[cell] = HMachine.BR;
                    cell = cell + 1;
                } else {
                    HMachine.memory[cell] = HMachine.PUSHMT;
                    HMachine.memory[cell+1] = HMachine.PUSH;
                    HMachine.memory[cell+2] = currNumberOfParams + 1;
                    HMachine.memory[cell+3] = HMachine.SUB;
                    HMachine.memory[cell+4] = HMachine.FLIP;
                    HMachine.memory[cell+5] = HMachine.STORE;
                    HMachine.memory[cell+6] = HMachine.PUSH;
                    HMachine.memory[cell+7] = currNumberOfParams - 1;
                    HMachine.memory[cell+8] = HMachine.POP;
                    HMachine.memory[cell+9] = HMachine.BR;
                    cell = cell + 10;
                }
                break;
            case 43:
                currCallName = Context.callNameStack.peek();
                currNumberOfParams = Context.symbolHash.find(currCallName).getNumberOfParams();
                if(currNumberOfParams == 0) {
                    HMachine.memory[cell] = HMachine.FLIP;
                    HMachine.memory[cell+1] = HMachine.BR;
                    cell = cell + 2;
                } else if(currNumberOfParams == 1) {
                    HMachine.memory[cell] = HMachine.PUSHMT;
                    HMachine.memory[cell+1] = HMachine.PUSH;
                    HMachine.memory[cell+2] = 3;
                    HMachine.memory[cell+3] = HMachine.SUB;
                    HMachine.memory[cell+4] = HMachine.FLIP;
                    HMachine.memory[cell+5] = HMachine.STORE;
                    HMachine.memory[cell+6] = HMachine.BR;
                    cell = cell + 7;
                } else {
                    HMachine.memory[cell] = HMachine.PUSHMT;
                    HMachine.memory[cell+1] = HMachine.PUSH;
                    HMachine.memory[cell+2] = currNumberOfParams + 2;
                    HMachine.memory[cell+3] = HMachine.SUB;
                    HMachine.memory[cell+4] = HMachine.FLIP;
                    HMachine.memory[cell+5] = HMachine.STORE;
                    HMachine.memory[cell+6] = HMachine.PUSHMT;
                    HMachine.memory[cell+7] = HMachine.PUSH;
                    HMachine.memory[cell+8] = currNumberOfParams;
                    HMachine.memory[cell+9] = HMachine.SUB;
                    HMachine.memory[cell+10] = HMachine.FLIP;
                    HMachine.memory[cell+11] = HMachine.STORE;
                    HMachine.memory[cell+12] = HMachine.PUSH;
                    HMachine.memory[cell+13] = currNumberOfParams - 2;
                    HMachine.memory[cell+14] = HMachine.POP;
                    HMachine.memory[cell+15] = HMachine.BR;
                    cell = cell + 16;
                }
                break;
            case 44:
                // push return address (next pc)
                HMachine.memory[cell] = HMachine.PUSH;
                HMachine.memory[cell+1] = cell + 5;

                // push procedure address
                HMachine.memory[cell+2] = HMachine.PUSH;
                HMachine.memory[cell+3] = Context.symbolHash.find((String) Context.symbolStack.peek()).getBaseAddress();

                HMachine.memory[cell+4] = HMachine.BR;
                cell = cell + 5;
                break;

            // R45: construct instruction for construct block for procedure call
            case 45:
                break;

            // R46: construct instruction for construct block for function call
            case 46:
                break;

            /**
            * IRVI
            * Construct instruction for call function
            */
            case 47:
                //EDIT 2: irvi
                //sama aja kayak R44 (?)
                HMachine.memory[cell] = HMachine.PUSH;
                //ada 5 instruksi yang akan dipanggil di R44
                HMachine.memory[cell+1] = cell + 5;
                HMachine.memory[cell+2] = HMachine.PUSH;
                HMachine.memory[cell+3] = Context.symbolHash.find((String)Context.symbolStack.peek()).getBaseAddress();
                HMachine.memory[cell+4] = HMachine.BR;
                cell = cell + 5;
                break;

            // R48: construct instruction for save arg for procedure & function call
            case 48:
                // belum dipakai
                break;
        }
    }

    // Method to set the current token
    public void setConst(String str) {
        currConst = str;
    }
}