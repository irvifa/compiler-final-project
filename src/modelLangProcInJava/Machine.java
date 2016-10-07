/** 
 * @class: Machine
 * This class prints the generated codes and executes the codes.
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.StringTokenizer;

class Machine
{
    // Main method to execute the object codes from input file
    public static void main(String[] args)
    {
        String filename = null;
        BufferedReader file = null;

        try
        {
            filename = args[0];
            file = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

            String line = null;
            StringTokenizer tokens = null;
            int cell = 0;
            while ((line = file.readLine()) != null)
            {
                tokens = new StringTokenizer(line);
                tokens.nextToken();
                HMachine.memory[cell] = HMachine.toOpNumber(tokens.nextToken());
                cell = cell + 1;
                while (tokens.hasMoreTokens())
                {
                    HMachine.memory[cell] = Integer.parseInt(tokens.nextToken());
                    cell = cell + 1;
                }
            }
            HMachine.mt = cell;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Usage: java Machine <filename>");
            System.exit(0);
        }
        catch (java.io.FileNotFoundException e)
        {
            System.out.println("Unable to open \"" + filename + 
                               "\".\nMake sure the filename is correct");
            System.exit(0);
        }
        catch (java.io.IOException e)
        {
            System.out.println("I/O error occured.");
            System.exit(1);
        }

        execute();
    }

    Machine()
    {
        for (int i = 0; i < HMachine.displaySize; i++)
            HMachine.display[i] = 0;
        for (int i = 0; i < HMachine.memorySize; i++)
            HMachine.memory[i] = 0;
        HMachine.pc = HMachine.mt = 0;
    }

    // Method to print the object codes stored at the memory,
    // starting from index 0 to the memory top (mt)
    void memoryDump()
    {
        int cell = 0, opcode;

        //System.out.println("Cell object");
        while (cell < HMachine.mt)
        {
            System.out.print(cell);
            opcode = HMachine.memory[cell];
            if ((opcode >= 0) && (opcode < 23))
                System.out.print("   " + HMachine.operation[opcode]);
            else
                System.out.print("   illegal operation code");
            cell = cell + 1;

            if ((opcode == HMachine.NAME) || (opcode == HMachine.PUSH) ||
                (opcode == HMachine.SETD))
            {
                System.out.print(" " + HMachine.memory[cell]);
                cell = cell + 1;
                if (opcode == HMachine.NAME)
                {
                    System.out.print(" " + HMachine.memory[cell]);
                    cell = cell + 1;
                }
            }
            System.out.println();
        }
    }

    // Method to execute the object codes from the memory
    static void execute()
    {
        boolean forever = true;
        int temp, temp1;
        char ch = 'a';

        while (forever) {
        switch (HMachine.memory[HMachine.pc])
        {
            case HMachine.NAME:
                HMachine.memory[HMachine.mt] = HMachine.display[HMachine.memory[HMachine.pc+1]]
                                               + HMachine.memory[HMachine.pc+2];
                HMachine.mt = HMachine.mt + 1;
                HMachine.pc = HMachine.pc + 3;
                break;
            case HMachine.LOAD:
                temp = HMachine.memory[HMachine.mt-1];
                if (HMachine.memory[temp] == HMachine.undefined)
                {
                    System.out.println("\nExecution error at location " + HMachine.pc);
                    System.out.println("Undefined value at location " + temp + " will be loaded.");
                    System.exit(1);
                }
                else
                {
                    HMachine.memory[HMachine.mt-1] = HMachine.memory[temp];
                    HMachine.pc = HMachine.pc + 1;
                }
                break;
            case HMachine.STORE:
                HMachine.mt = HMachine.mt - 2;
                HMachine.memory[HMachine.memory[HMachine.mt]] = HMachine.memory[HMachine.mt+1];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.PUSH:
                HMachine.memory[HMachine.mt] = HMachine.memory[HMachine.pc+1];
                HMachine.mt = HMachine.mt + 1;
                HMachine.pc = HMachine.pc + 2;
                break;
            case HMachine.PUSHMT:
                HMachine.memory[HMachine.mt] = HMachine.mt;
                HMachine.mt = HMachine.mt + 1;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.SETD:
                HMachine.mt = HMachine.mt - 1;
                HMachine.display[HMachine.memory[HMachine.pc+1]] = HMachine.memory[HMachine.mt];
                HMachine.pc = HMachine.pc + 2;
                break;
            case HMachine.POP:
                HMachine.mt = HMachine.mt - 1 - HMachine.memory[HMachine.mt-1];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.DUP:
                temp = HMachine.memory[HMachine.mt-1];
                temp1 = HMachine.memory[HMachine.mt-2];
                for (int i = HMachine.mt-1; i < HMachine.mt-2+temp; i++)
                    HMachine.memory[i] = temp1;
                HMachine.mt = HMachine.mt - 2 + temp;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.BR:
                HMachine.mt = HMachine.mt - 1;
                HMachine.pc = HMachine.memory[HMachine.mt];
                break;
            case HMachine.BF:
                HMachine.mt = HMachine.mt - 2;
                if (HMachine.memory[HMachine.mt] == 0)
                    HMachine.pc = HMachine.memory[HMachine.mt+1];
                else
                    HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.ADD:
                HMachine.mt = HMachine.mt - 1;
                HMachine.memory[HMachine.mt-1] = HMachine.memory[HMachine.mt-1]
                                                 + HMachine.memory[HMachine.mt];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.SUB:
                HMachine.mt = HMachine.mt - 1;
                HMachine.memory[HMachine.mt-1] = HMachine.memory[HMachine.mt-1]
                                                 - HMachine.memory[HMachine.mt];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.MUL:
                HMachine.mt = HMachine.mt - 1;
                HMachine.memory[HMachine.mt-1] = HMachine.memory[HMachine.mt-1]
                                                 * HMachine.memory[HMachine.mt];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.DIVI:
                HMachine.mt = HMachine.mt - 1;
                HMachine.memory[HMachine.mt-1] = HMachine.memory[HMachine.mt-1]
                                                 / HMachine.memory[HMachine.mt];
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.EQ:
                HMachine.mt = HMachine.mt - 1;
                if (HMachine.memory[HMachine.mt-1] == HMachine.memory[HMachine.mt])
                    HMachine.memory[HMachine.mt-1] = 1;
                else
                    HMachine.memory[HMachine.mt-1] = 0;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.LT:
                HMachine.mt = HMachine.mt - 1;
                if (HMachine.memory[HMachine.mt-1] < HMachine.memory[HMachine.mt])
                    HMachine.memory[HMachine.mt-1] = 1;
                else
                    HMachine.memory[HMachine.mt-1] = 0;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.ORI:
                HMachine.mt = HMachine.mt - 1;
                if ((HMachine.memory[HMachine.mt-1] == 1) || (HMachine.memory[HMachine.mt] == 1))
                    HMachine.memory[HMachine.mt-1] = 1;
                else
                    HMachine.memory[HMachine.mt-1] = 0;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.FLIP:
                temp = HMachine.memory[HMachine.mt-2];
                HMachine.memory[HMachine.mt-2] = HMachine.memory[HMachine.mt-1];
                HMachine.memory[HMachine.mt-1] = temp;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.READC:
                try
                {
                    ch = (char) new BufferedReader(new InputStreamReader(System.in)).read();
                }
                catch (java.io.IOException e)
                {
                }
                HMachine.memory[HMachine.mt] = ch;
                HMachine.mt = HMachine.mt + 1;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.PRINTC:
                HMachine.mt = HMachine.mt - 1;
                System.out.print((char)HMachine.memory[HMachine.mt]);
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.READI:
                try
                {
                    HMachine.memory[HMachine.mt] = 
                        Integer.parseInt(new BufferedReader(
                            new InputStreamReader(System.in)).readLine());
                }
                catch (java.io.IOException e)
                {
                }
                HMachine.mt = HMachine.mt + 1;
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.PRINTI:
                HMachine.mt = HMachine.mt - 1;
                System.out.print(HMachine.memory[HMachine.mt]);
                HMachine.pc = HMachine.pc + 1;
                break;
            case HMachine.HALT:
                System.exit(0);
                break;
            default:
                System.out.println("\nExecution error at location " + HMachine.pc +
                                   " : illegal operation code.");
        }  }
    }
}