/*
    IKI40800 Parser
    A Sasmito Adibowo – 1299000029
	Bayu Adianto Prabowo – 1200000187
*/

import java.lang.System;
import java.io.*;

class Utility {
  public static void ASSERT(boolean expr) { 
    if (false == expr) {
      throw (new Error("Error: ASSERTion failed."));
    }
  }
  
  private static final String errorMsg[] = {
    "Error: Unmatched end-of-comment punctuation.",
    "Error: Unmatched start-of-comment punctuation.",
    "Error: Unclosed string.",
    "Error: Illegal character."
  };
  
  public static final int E_ENDCOMMENT = 0; 
  public static final int E_STARTCOMMENT = 1; 
  public static final int E_UNCLOSEDSTR = 2; 
  public static final int E_UNMATCHED = 3; 

  public static void error(int code) {
        System.out.println(errorMsg[code]);
  }
}

class Yytoken extends java_cup.runtime.Symbol {
  Yytoken (int index, String text, int line, int charBegin, int charEnd) {
    super(index);
    m_index = index;
    m_text = new String(text);
    m_line = line;
    m_charBegin = charBegin;
    m_charEnd = charEnd;
  }
  public int m_index;
  public String m_text;
  public int m_line;
  public int m_charBegin;
  public int m_charEnd;
  public String toString() {
      return "Token #"+m_index+": "+m_text+" (line "+m_line+")";
  }
}

%%

%notunix

%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol


%{

public int getLine() {
        return yyline;
    }

public String getText() {
        return new String(yytext());
    }

public int num_error= 0;
  /*

     TO DO:
        - unary operators in the parser states

  */
%} 

%line
%char
%state COMMENT

ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\012]
WHITE_SPACE_CHAR=[\n\ \t\b\012]
STRING_TEXT=(\\\"|[^\n\"]|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=.*
EOL=\r\n

%% 

<YYINITIAL> "(" { return (new Yytoken(sym.LPAREN,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ")" { return (new Yytoken(sym.RPAREN,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "." { return (new Yytoken(sym.DEC,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "+" { return (new Yytoken(sym.ADD,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "-" { return (new Yytoken(sym.SUB,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "*" { return (new Yytoken(sym.MUL,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "/" { return (new Yytoken(sym.DIV,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "=" { return (new Yytoken(sym.EQ,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "#" { return (new Yytoken(sym.NE,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "<"  { return (new Yytoken(sym.LT,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "<=" { return (new Yytoken(sym.LTE,yytext(),yyline,yychar,yychar+2)); }
<YYINITIAL> ">"  { return (new Yytoken(sym.GT,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ">=" { return (new Yytoken(sym.GTE,yytext(),yyline,yychar,yychar+2)); }
<YYINITIAL> "&"  { return (new Yytoken(sym.AND,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "|"  { return (new Yytoken(sym.OR,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ":=" { return (new Yytoken(sym.ASSGN,yytext(),yyline,yychar,yychar+2)); }
<YYINITIAL> "~"  { return (new Yytoken(sym.NOT,yytext(),yyline,yychar,yychar+1)); }

<YYINITIAL> "," { return (new Yytoken(sym.LISTSEP,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ":" { return (new Yytoken(sym.AS,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ";" { return (new Yytoken(sym.ENDSTMT,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> ".." { return (new Yytoken(sym.RANGESEP,yytext(),yyline,yychar,yychar+2)); }
<YYINITIAL> "[" { return (new Yytoken(sym.LBRACKET,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "]" { return (new Yytoken(sym.RBRACKET,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "{" { return (new Yytoken(sym.OPEN,yytext(),yyline,yychar,yychar+1)); }
<YYINITIAL> "}" { return (new Yytoken(sym.CLOSE,yytext(),yyline,yychar,yychar+1)); }

<YYINITIAL> "if" { return (new Yytoken(sym.IF,yytext(),yyline,yychar,yychar+2)); }
<YYINITIAL> "then" { return (new Yytoken(sym.THEN,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "else" { return (new Yytoken(sym.ELSE,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "repeat" { return (new Yytoken(sym.REPEAT,yytext(),yyline,yychar,yychar+6)); }
<YYINITIAL> "until" { return (new Yytoken(sym.UNTIL,yytext(),yyline,yychar,yychar+5)); }
<YYINITIAL> "loop" { return (new Yytoken(sym.LOOP,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "exit" { return (new Yytoken(sym.EXIT,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "put" { return (new Yytoken(sym.PUT,yytext(),yyline,yychar,yychar+3)); }
<YYINITIAL> "get" { return (new Yytoken(sym.GET,yytext(),yyline,yychar,yychar+3)); }
<YYINITIAL> "proc" { return (new Yytoken(sym.PROC,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "func" { return (new Yytoken(sym.FUNC,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "var" { return (new Yytoken(sym.VAR,yytext(),yyline,yychar,yychar+3)); }
<YYINITIAL> "integer" { return (new Yytoken(sym.INT,yytext(),yyline,yychar,yychar+7)); }
<YYINITIAL> "boolean" { return (new Yytoken(sym.BOOL,yytext(),yyline,yychar,yychar+7)); }
<YYINITIAL> "end" { return (new Yytoken(sym.END,yytext(),yyline,yychar,yychar+3)); }
<YYINITIAL> "true" { return (new Yytoken(sym.TRUE,yytext(),yyline,yychar,yychar+4)); }
<YYINITIAL> "false" { return (new Yytoken(sym.FALSE,yytext(),yyline,yychar,yychar+5)); }
<YYINITIAL> "skip" { return (new Yytoken(sym.SKIP,yytext(),yyline,yychar,yychar+4)); }

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { }

<YYINITIAL> {EOL} { }

<YYINITIAL> "%" { yybegin(COMMENT); }

<COMMENT> {EOL} {yybegin(YYINITIAL);}

<COMMENT> {COMMENT_TEXT} { }

<YYINITIAL> \"{STRING_TEXT}\" {
        String str =  yytext().substring(1,yytext().length() - 1);
        
        Utility.ASSERT(str.length() == yytext().length() - 2);
        return (new Yytoken(sym.STRCONST,str,yyline,yychar,yychar + str.length()));
}
<YYINITIAL> \"{STRING_TEXT} {
        String str =  yytext().substring(1,yytext().length());
        num_error++;

        Utility.error(Utility.E_UNCLOSEDSTR);
        Utility.ASSERT(str.length() == yytext().length() - 1);
        return (new Yytoken(sym.STRUNCLD,str,yyline,yychar,yychar + str.length()));
} 
<YYINITIAL> {DIGIT}+ { 
        return (new Yytoken(sym.NUMCONST,yytext(),yyline,yychar,yychar + yytext().length()));
}       
<YYINITIAL> ({ALPHA}|_)({ALPHA}|{DIGIT}|_)* {
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}       
<YYINITIAL,COMMENT> . {
        System.out.println("Illegal character: <" + yytext() + ">");
        num_error++;
        Utility.error(Utility.E_UNMATCHED);
}
