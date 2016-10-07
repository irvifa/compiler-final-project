/*
    IKI40800 Parser
    A Sasmito Adibowo ? 1299000029
	Bayu Adianto Prabowo ? 1200000187
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


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		53
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"46:8,42:2,44,46:2,43,46:18,42,46,47,9,46,45,12,46,1,2,6,4,16,5,3,7,49:10,14" +
",17,10,8,11,46:2,50:26,18,48,19,46,50,46,32,39,37,40,26,23,36,25,22,50,41,2" +
"8,50,27,34,31,50,30,29,24,33,38,50,35,50:2,20,13,21,15,46,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,114,
"0,1:3,2,1:6,3,4,1:2,5,1:7,6,7,1:2,8,9,1:4,10,1:2,10:17,11,1,12,13,14,8,15,1" +
"6,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,4" +
"1,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,10,62,63,64,6" +
"5,66,67,68")[0];

	private int yy_nxt[][] = unpackFromString(69,51,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,56,104,106,80,1" +
"06,107,108,109,81,106,110,106:2,82,106,83,111,106:2,24,55,24,25,26,27,26,28" +
",106,-1:54,29,-1:55,30,-1:50,31,-1:50,32,-1:64,106,33,106:3,112,106:14,-1:7" +
",106:2,-1:42,24,-1,24,-1:7,27:43,-1,27:2,35,57,27:2,-1:49,28,-1:23,106:20,-" +
"1:7,106:2,1,59:42,63,-1,59:6,-1:44,34,-1:28,106:10,113,84,106:8,-1:7,106:2," +
"-1,27:41,62,27,60,27:2,58,57,27:2,-1,59:42,-1:2,59:6,-1:42,60,-1,60,-1:3,27" +
",-1:24,106:18,36,106,-1:7,106:2,-1,27:41,62,27,60,27:2,35,57,27:2,-1:44,54," +
"-1:28,106:2,37,106:17,-1:7,106:2,-1:22,106:2,38,106:17,-1:7,106:2,-1:22,106" +
":8,39,106:11,-1:7,106:2,-1:22,106:15,40,106:4,-1:7,106:2,-1:22,106:5,41,106" +
":14,-1:7,106:2,-1:22,106:4,42,106:15,-1:7,106:2,-1:22,106:4,43,106:15,-1:7," +
"106:2,-1:22,106:2,44,106:17,-1:7,106:2,-1:22,106:9,45,106:10,-1:7,106:2,-1:" +
"22,106:9,46,106:10,-1:7,106:2,-1:22,106:15,47,106:4,-1:7,106:2,-1:22,106:4," +
"48,106:15,-1:7,106:2,-1:22,106:6,49,106:13,-1:7,106:2,-1:22,106:2,50,106:17" +
",-1:7,106:2,-1:22,106:8,51,106:11,-1:7,106:2,-1:22,106:5,52,106:14,-1:7,106" +
":2,-1:22,106:5,61,87,106:6,88,106:6,-1:7,106:2,-1:22,106:8,92,106:2,64,106:" +
"8,-1:7,106:2,-1:22,106:4,65,106:15,-1:7,106:2,-1:22,106:10,66,106:9,-1:7,10" +
"6:2,-1:22,106:5,67,106:14,-1:7,106:2,-1:22,106:4,68,106:15,-1:7,106:2,-1:22" +
",106:11,69,106:8,-1:7,106:2,-1:22,106:7,70,106:12,-1:7,106:2,-1:22,71,106:1" +
"9,-1:7,106:2,-1:22,106:12,72,106:7,-1:7,106:2,-1:22,73,106:19,-1:7,106:2,-1" +
":22,106:9,105,106:10,-1:7,106:2,-1:22,106:12,74,106:7,-1:7,106:2,-1:22,106:" +
"2,97,106:17,-1:7,106:2,-1:22,106:12,98,106:7,-1:7,106:2,-1:22,106:4,99,106:" +
"15,-1:7,106:2,-1:22,106:7,75,106:12,-1:7,106:2,-1:22,76,106:19,-1:7,106:2,-" +
"1:22,106:6,101,106:13,-1:7,106:2,-1:22,106:14,102,106:5,-1:7,106:2,-1:22,10" +
"6:10,77,106:9,-1:7,106:2,-1:22,106:4,103,106:15,-1:7,106:2,-1:22,106:4,78,1" +
"06:15,-1:7,106:2,-1:22,106:10,79,106:9,-1:7,106:2,-1:22,106:3,85,106:4,86,1" +
"06:11,-1:7,106:2,-1:22,106:4,100,106:15,-1:7,106:2,-1:22,106:12,89,106:7,-1" +
":7,106:2,-1:22,106:19,90,-1:7,106:2,-1:22,106:4,91,106:15,-1:7,106:2,-1:22," +
"106:5,93,106:14,-1:7,106:2,-1:22,106:12,94,106:7,-1:7,106:2,-1:22,106:2,95," +
"106:17,-1:7,106:2,-1:22,106:6,96,106:13,-1:7,106:2");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return (new Yytoken(sym.LPAREN,yytext(),yyline,yychar,yychar+1)); }
					case -3:
						break;
					case 3:
						{ return (new Yytoken(sym.RPAREN,yytext(),yyline,yychar,yychar+1)); }
					case -4:
						break;
					case 4:
						{ return (new Yytoken(sym.DEC,yytext(),yyline,yychar,yychar+1)); }
					case -5:
						break;
					case 5:
						{ return (new Yytoken(sym.ADD,yytext(),yyline,yychar,yychar+1)); }
					case -6:
						break;
					case 6:
						{ return (new Yytoken(sym.SUB,yytext(),yyline,yychar,yychar+1)); }
					case -7:
						break;
					case 7:
						{ return (new Yytoken(sym.MUL,yytext(),yyline,yychar,yychar+1)); }
					case -8:
						break;
					case 8:
						{ return (new Yytoken(sym.DIV,yytext(),yyline,yychar,yychar+1)); }
					case -9:
						break;
					case 9:
						{ return (new Yytoken(sym.EQ,yytext(),yyline,yychar,yychar+1)); }
					case -10:
						break;
					case 10:
						{ return (new Yytoken(sym.NE,yytext(),yyline,yychar,yychar+1)); }
					case -11:
						break;
					case 11:
						{ return (new Yytoken(sym.LT,yytext(),yyline,yychar,yychar+1)); }
					case -12:
						break;
					case 12:
						{ return (new Yytoken(sym.GT,yytext(),yyline,yychar,yychar+1)); }
					case -13:
						break;
					case 13:
						{ return (new Yytoken(sym.AND,yytext(),yyline,yychar,yychar+1)); }
					case -14:
						break;
					case 14:
						{ return (new Yytoken(sym.OR,yytext(),yyline,yychar,yychar+1)); }
					case -15:
						break;
					case 15:
						{ return (new Yytoken(sym.AS,yytext(),yyline,yychar,yychar+1)); }
					case -16:
						break;
					case 16:
						{ return (new Yytoken(sym.NOT,yytext(),yyline,yychar,yychar+1)); }
					case -17:
						break;
					case 17:
						{ return (new Yytoken(sym.LISTSEP,yytext(),yyline,yychar,yychar+1)); }
					case -18:
						break;
					case 18:
						{ return (new Yytoken(sym.ENDSTMT,yytext(),yyline,yychar,yychar+1)); }
					case -19:
						break;
					case 19:
						{ return (new Yytoken(sym.LBRACKET,yytext(),yyline,yychar,yychar+1)); }
					case -20:
						break;
					case 20:
						{ return (new Yytoken(sym.RBRACKET,yytext(),yyline,yychar,yychar+1)); }
					case -21:
						break;
					case 21:
						{ return (new Yytoken(sym.OPEN,yytext(),yyline,yychar,yychar+1)); }
					case -22:
						break;
					case 22:
						{ return (new Yytoken(sym.CLOSE,yytext(),yyline,yychar,yychar+1)); }
					case -23:
						break;
					case 23:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -24:
						break;
					case 24:
						{ }
					case -25:
						break;
					case 25:
						{ yybegin(COMMENT); }
					case -26:
						break;
					case 26:
						{
        System.out.println("Illegal character: <" + yytext() + ">");
        num_error++;
        Utility.error(Utility.E_UNMATCHED);
}
					case -27:
						break;
					case 27:
						{
        String str =  yytext().substring(1,yytext().length());
        num_error++;
        Utility.error(Utility.E_UNCLOSEDSTR);
        Utility.ASSERT(str.length() == yytext().length() - 1);
        return (new Yytoken(sym.STRUNCLD,str,yyline,yychar,yychar + str.length()));
}
					case -28:
						break;
					case 28:
						{ 
        return (new Yytoken(sym.NUMCONST,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -29:
						break;
					case 29:
						{ return (new Yytoken(sym.RANGESEP,yytext(),yyline,yychar,yychar+2)); }
					case -30:
						break;
					case 30:
						{ return (new Yytoken(sym.LTE,yytext(),yyline,yychar,yychar+2)); }
					case -31:
						break;
					case 31:
						{ return (new Yytoken(sym.GTE,yytext(),yyline,yychar,yychar+2)); }
					case -32:
						break;
					case 32:
						{ return (new Yytoken(sym.ASSGN,yytext(),yyline,yychar,yychar+2)); }
					case -33:
						break;
					case 33:
						{ return (new Yytoken(sym.IF,yytext(),yyline,yychar,yychar+2)); }
					case -34:
						break;
					case 34:
						{ }
					case -35:
						break;
					case 35:
						{
        String str =  yytext().substring(1,yytext().length() - 1);
        Utility.ASSERT(str.length() == yytext().length() - 2);
        return (new Yytoken(sym.STRCONST,str,yyline,yychar,yychar + str.length()));
}
					case -36:
						break;
					case 36:
						{ return (new Yytoken(sym.END,yytext(),yyline,yychar,yychar+3)); }
					case -37:
						break;
					case 37:
						{ return (new Yytoken(sym.PUT,yytext(),yyline,yychar,yychar+3)); }
					case -38:
						break;
					case 38:
						{ return (new Yytoken(sym.GET,yytext(),yyline,yychar,yychar+3)); }
					case -39:
						break;
					case 39:
						{ return (new Yytoken(sym.VAR,yytext(),yyline,yychar,yychar+3)); }
					case -40:
						break;
					case 40:
						{ return (new Yytoken(sym.FUNC,yytext(),yyline,yychar,yychar+4)); }
					case -41:
						break;
					case 41:
						{ return (new Yytoken(sym.THEN,yytext(),yyline,yychar,yychar+4)); }
					case -42:
						break;
					case 42:
						{ return (new Yytoken(sym.TRUE,yytext(),yyline,yychar,yychar+4)); }
					case -43:
						break;
					case 43:
						{ return (new Yytoken(sym.ELSE,yytext(),yyline,yychar,yychar+4)); }
					case -44:
						break;
					case 44:
						{ return (new Yytoken(sym.EXIT,yytext(),yyline,yychar,yychar+4)); }
					case -45:
						break;
					case 45:
						{ return (new Yytoken(sym.LOOP,yytext(),yyline,yychar,yychar+4)); }
					case -46:
						break;
					case 46:
						{ return (new Yytoken(sym.SKIP,yytext(),yyline,yychar,yychar+4)); }
					case -47:
						break;
					case 47:
						{ return (new Yytoken(sym.PROC,yytext(),yyline,yychar,yychar+4)); }
					case -48:
						break;
					case 48:
						{ return (new Yytoken(sym.FALSE,yytext(),yyline,yychar,yychar+5)); }
					case -49:
						break;
					case 49:
						{ return (new Yytoken(sym.UNTIL,yytext(),yyline,yychar,yychar+5)); }
					case -50:
						break;
					case 50:
						{ return (new Yytoken(sym.REPEAT,yytext(),yyline,yychar,yychar+6)); }
					case -51:
						break;
					case 51:
						{ return (new Yytoken(sym.INT,yytext(),yyline,yychar,yychar+7)); }
					case -52:
						break;
					case 52:
						{ return (new Yytoken(sym.BOOL,yytext(),yyline,yychar,yychar+7)); }
					case -53:
						break;
					case 53:
						{ }
					case -54:
						break;
					case 54:
						{yybegin(YYINITIAL);}
					case -55:
						break;
					case 56:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -56:
						break;
					case 57:
						{
        String str =  yytext().substring(1,yytext().length());
        num_error++;
        Utility.error(Utility.E_UNCLOSEDSTR);
        Utility.ASSERT(str.length() == yytext().length() - 1);
        return (new Yytoken(sym.STRUNCLD,str,yyline,yychar,yychar + str.length()));
}
					case -57:
						break;
					case 58:
						{
        String str =  yytext().substring(1,yytext().length() - 1);
        Utility.ASSERT(str.length() == yytext().length() - 2);
        return (new Yytoken(sym.STRCONST,str,yyline,yychar,yychar + str.length()));
}
					case -58:
						break;
					case 59:
						{ }
					case -59:
						break;
					case 61:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -60:
						break;
					case 62:
						{
        String str =  yytext().substring(1,yytext().length());
        num_error++;
        Utility.error(Utility.E_UNCLOSEDSTR);
        Utility.ASSERT(str.length() == yytext().length() - 1);
        return (new Yytoken(sym.STRUNCLD,str,yyline,yychar,yychar + str.length()));
}
					case -61:
						break;
					case 64:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -62:
						break;
					case 65:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -63:
						break;
					case 66:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -64:
						break;
					case 67:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -65:
						break;
					case 68:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -66:
						break;
					case 69:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -67:
						break;
					case 70:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -68:
						break;
					case 71:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -69:
						break;
					case 72:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -70:
						break;
					case 73:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -71:
						break;
					case 74:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -72:
						break;
					case 75:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -73:
						break;
					case 76:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -74:
						break;
					case 77:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -75:
						break;
					case 78:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -76:
						break;
					case 79:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -77:
						break;
					case 80:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -78:
						break;
					case 81:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -79:
						break;
					case 82:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -80:
						break;
					case 83:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -81:
						break;
					case 84:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -82:
						break;
					case 85:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -83:
						break;
					case 86:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -84:
						break;
					case 87:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -85:
						break;
					case 88:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -86:
						break;
					case 89:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -87:
						break;
					case 90:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -88:
						break;
					case 91:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -89:
						break;
					case 92:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -90:
						break;
					case 93:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -91:
						break;
					case 94:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -92:
						break;
					case 95:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -93:
						break;
					case 96:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -94:
						break;
					case 97:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -95:
						break;
					case 98:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -96:
						break;
					case 99:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -97:
						break;
					case 100:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -98:
						break;
					case 101:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -99:
						break;
					case 102:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -100:
						break;
					case 103:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -101:
						break;
					case 104:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -102:
						break;
					case 105:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -103:
						break;
					case 106:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -104:
						break;
					case 107:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -105:
						break;
					case 108:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -106:
						break;
					case 109:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -107:
						break;
					case 110:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -108:
						break;
					case 111:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -109:
						break;
					case 112:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -110:
						break;
					case 113:
						{
        return (new Yytoken(sym.IDENT,yytext(),yyline,yychar,yychar + yytext().length()));
}
					case -111:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
