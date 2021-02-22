import java_cup.runtime.*; // defines the Symbol class
// The generated scanner will return a Symbol for each token that it finds.
// A Symbol contains an Object field named value; that field will be of type
// TokenVal, defined below.
//
// A TokenVal object contains the line number on which the token occurs as
// well as the number of the character on that line that starts the token.
// Some tokens (literals and IDs) also include the value of the token.
class TokenVal {
  // fields
    int linenum;
    int charnum;
  // constructor
    TokenVal(int line, int ch) {
        linenum = line;
        charnum = ch;
    }
}
class IntLitTokenVal extends TokenVal {
  // new field: the value of the integer literal
    int intVal;
  // constructor
    IntLitTokenVal(int line, int ch, int val) {
        super(line, ch);
        intVal = val;
    }
}
class IdTokenVal extends TokenVal {
  // new field: the value of the identifier
    String idVal;
  // constructor
    IdTokenVal(int line, int ch, String val) {
        super(line, ch);
    idVal = val;
    }
}
class StrLitTokenVal extends TokenVal {
  // new field: the value of the string literal
    String strVal;
  // constructor
    StrLitTokenVal(int line, int ch, String val) {
        super(line, ch);
        strVal = val;
    }
}
// The following class is used to keep track of the character number at which
// the current token starts on its line.
class CharNum {
    static int num=1;
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
	private final int STATE3 = 3;
	private final int STATE2 = 2;
	private final int YYINITIAL = 0;
	private final int STATE_RESERVED = 1;
	private final int STATE4 = 4;
	private final int yy_state_dtrans[] = {
		0,
		48,
		48,
		48,
		48
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
		/* 0 */ YY_NO_ANCHOR,
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
		/* 20 */ YY_NOT_ACCEPT,
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
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
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
		/* 86 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"40:9,39,3,40:2,0,40:18,39,33,40,2,40:2,34,40,28,29,32,30,24,31,25,1,22:10,4" +
"0,23,27,36,26,40:2,21:26,40:4,21,40,20,7,14,16,12,10,21,19,4,21:2,9,21,5,8," +
"21:2,11,17,6,13,15,18,21:3,37,35,38,40:2,41:2")[0];

	private int yy_rmap[] = unpackFromString(1,87,
"0,1,2,3:4,4,5,3:2,6,7,3,8,9,3:2,10,3,11,12,3:11,12:11,13,3,14,15,16,12,17,1" +
"8,3,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42" +
",43,44,45,46,47,48,49,50,51,52")[0];

	private int yy_nxt[][] = unpackFromString(53,42,
"-1,1,2,3,46,49,73,74,49:2,81,85,75,49,63,76,49,86,82,49:4,4,5,6,7,8,9,10,11" +
",12,13,14,47,50,15,16,17,18,52,19,-1,20,-1:42,20,-1:107,22,-1:9,23,-1:32,24" +
",-1:8,25,-1:35,26,-1:42,27,-1:46,28,-1:41,31,-1:44,18,-1:5,32,-1:42,49:19,-" +
"1:41,44,-1:23,49,51,49:4,21,49:12,-1:53,29,-1:8,52:2,3,52:18,44,52:7,45,52:" +
"8,18,52,19,-1:35,30,-1:10,49:2,33,49:16,-1:23,49,34,49:17,-1:23,49:8,35,49:" +
"10,-1:23,49:5,36,49:13,-1:23,49:8,37,49:10,-1:23,49:2,38,49:16,-1:23,49:12," +
"39,49:6,-1:23,49:8,40,49:10,-1:23,49:8,41,49:10,-1:23,49,42,49:17,-1:23,49:" +
"2,43,49:16,-1:23,53,49:3,67,49:14,-1:23,49:9,54,49:9,-1:23,49:4,55,49:14,-1" +
":23,49:13,56,49:5,-1:23,49:9,57,49:9,-1:23,58,49:18,-1:23,49:13,59,49:5,-1:" +
"23,49:5,60,49:13,-1:23,49:7,61,49:11,-1:23,49:10,62,49:8,-1:23,49:7,64,49:1" +
"1,-1:23,49:4,65,49:14,-1:23,49:5,66,49:13,-1:23,49:4,68,49:14,-1:23,49:5,69" +
",49:13,-1:23,70,49:18,-1:23,49:9,71,49:9,-1:23,49:9,72,49:9,-1:23,49:16,77," +
"49:2,-1:23,49:15,78,49:3,-1:23,49:2,79,49:16,-1:23,49:7,80,49:11,-1:23,49:8" +
",83,49:10,-1:23,49:2,84,49:16,-1:19");

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

    return new Symbol(sym.EOF);
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
					case 0:
						{
}
					case -2:
						break;
					case 1:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.DIVIDE, new TokenVal(yyline + 1, currentCharNum));
}
					case -3:
						break;
					case 2:
						{ 
    ErrMsg.fatal(yyline+1, CharNum.num, "ignoring illegal character: " + yytext());
    CharNum.num++;
    }
					case -4:
						break;
					case 3:
						{ 
    CharNum.num = 1; 
    }
					case -5:
						break;
					case 4:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.COMMA, new TokenVal(yyline + 1, currentCharNum));
}
					case -6:
						break;
					case 5:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.SEMICOLON, new TokenVal(yyline + 1, currentCharNum));
}
					case -7:
						break;
					case 6:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.DOT, new TokenVal(yyline + 1, currentCharNum));
}
					case -8:
						break;
					case 7:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.GREATER, new TokenVal(yyline + 1, currentCharNum));
}
					case -9:
						break;
					case 8:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LESS, new TokenVal(yyline + 1, currentCharNum));
}
					case -10:
						break;
					case 9:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LPAREN, new TokenVal(yyline + 1, currentCharNum));
}
					case -11:
						break;
					case 10:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RPAREN, new TokenVal(yyline + 1, currentCharNum));
}
					case -12:
						break;
					case 11:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.PLUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -13:
						break;
					case 12:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.MINUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -14:
						break;
					case 13:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.TIMES, new TokenVal(yyline + 1, currentCharNum));
}
					case -15:
						break;
					case 14:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.NOT, new TokenVal(yyline + 1, currentCharNum));
}
					case -16:
						break;
					case 15:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.ASSIGN, new TokenVal(yyline + 1, currentCharNum));
}
					case -17:
						break;
					case 16:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LCURLY, new TokenVal(yyline + 1, currentCharNum));
}
					case -18:
						break;
					case 17:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RCURLY, new TokenVal(yyline + 1, currentCharNum));
}
					case -19:
						break;
					case 18:
						{ 
    CharNum.num += yytext().length(); 
    }
					case -20:
						break;
					case 19:
						
					case -21:
						break;
					case 21:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.IF, new TokenVal(yyline + 1, currentCharNum));
}
					case -22:
						break;
					case 22:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.READ, new TokenVal(yyline + 1, currentCharNum));
}
					case -23:
						break;
					case 23:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.GREATEREQ, new TokenVal(yyline + 1, currentCharNum));
}
					case -24:
						break;
					case 24:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.WRITE, new TokenVal(yyline + 1, currentCharNum));
}
					case -25:
						break;
					case 25:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LESSEQ, new TokenVal(yyline + 1, currentCharNum));
}
					case -26:
						break;
					case 26:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.PLUSPLUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -27:
						break;
					case 27:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.MINUSMINUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -28:
						break;
					case 28:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.NOTEQUALS, new TokenVal(yyline + 1, currentCharNum));
}
					case -29:
						break;
					case 29:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.AND, new TokenVal(yyline + 1, currentCharNum));
}
					case -30:
						break;
					case 30:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.OR, new TokenVal(yyline + 1, currentCharNum));
}
					case -31:
						break;
					case 31:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.EQUALS, new TokenVal(yyline + 1, currentCharNum));
}
					case -32:
						break;
					case 32:
						{
    // ignore comments 
}
					case -33:
						break;
					case 33:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.INT, new TokenVal(yyline + 1, currentCharNum));
}
					case -34:
						break;
					case 34:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.CIN, new TokenVal(yyline + 1, currentCharNum));
}
					case -35:
						break;
					case 35:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.TRUE, new TokenVal(yyline + 1, currentCharNum));
}
					case -36:
						break;
					case 36:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.BOOL, new TokenVal(yyline + 1, currentCharNum));
}
					case -37:
						break;
					case 37:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.ELSE, new TokenVal(yyline + 1, currentCharNum));
}
					case -38:
						break;
					case 38:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.COUT, new TokenVal(yyline + 1, currentCharNum));
}
					case -39:
						break;
					case 39:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.VOID, new TokenVal(yyline + 1, currentCharNum));
}
					case -40:
						break;
					case 40:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.FALSE, new TokenVal(yyline + 1, currentCharNum));
}
					case -41:
						break;
					case 41:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.WHILE, new TokenVal(yyline + 1, currentCharNum));
}
					case -42:
						break;
					case 42:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RETURN, new TokenVal(yyline + 1, currentCharNum));
}
					case -43:
						break;
					case 43:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.STRUCT, new TokenVal(yyline + 1, currentCharNum));
}
					case -44:
						break;
					case 44:
						{ 
    // NOTE: the following computation of the integer value does NOT
    // check for overflow.  This must be modified.
    int val = Integer.parseInt(yytext());
    Symbol s = new Symbol(sym.INTLITERAL, new IntLitTokenVal(yyline+1, CharNum.num, val));
    CharNum.num += yytext().length();
    return s;
    // or 
    // double d = Double.parseDouble(yytext()); // convert String to double
// INSERT CODE HERE TO CHECK FOR BAD VALUE -- SEE ERRORS AND WARNINGS BELOW
// int k =  Integer.parseInt(yytext());    // convert to int
    }
					case -45:
						break;
					case 45:
						{ 
    // change state example:
    // yybegin(STATE2);
    // return back to initial
    // yybegin(YYINITIAL);
    Symbol s = new Symbol(sym.PLUS, new TokenVal(yyline+1, CharNum.num));
    CharNum.num++;
    return s;
    }
					case -46:
						break;
					case 46:
						{
}
					case -47:
						break;
					case 47:
						{ 
    ErrMsg.fatal(yyline+1, CharNum.num, "ignoring illegal character: " + yytext());
    CharNum.num++;
    }
					case -48:
						break;
					case 49:
						{
}
					case -49:
						break;
					case 50:
						{ 
    ErrMsg.fatal(yyline+1, CharNum.num, "ignoring illegal character: " + yytext());
    CharNum.num++;
    }
					case -50:
						break;
					case 51:
						{
}
					case -51:
						break;
					case 52:
						{ 
    ErrMsg.fatal(yyline+1, CharNum.num, "ignoring illegal character: " + yytext());
    CharNum.num++;
    }
					case -52:
						break;
					case 53:
						{
}
					case -53:
						break;
					case 54:
						{
}
					case -54:
						break;
					case 55:
						{
}
					case -55:
						break;
					case 56:
						{
}
					case -56:
						break;
					case 57:
						{
}
					case -57:
						break;
					case 58:
						{
}
					case -58:
						break;
					case 59:
						{
}
					case -59:
						break;
					case 60:
						{
}
					case -60:
						break;
					case 61:
						{
}
					case -61:
						break;
					case 62:
						{
}
					case -62:
						break;
					case 63:
						{
}
					case -63:
						break;
					case 64:
						{
}
					case -64:
						break;
					case 65:
						{
}
					case -65:
						break;
					case 66:
						{
}
					case -66:
						break;
					case 67:
						{
}
					case -67:
						break;
					case 68:
						{
}
					case -68:
						break;
					case 69:
						{
}
					case -69:
						break;
					case 70:
						{
}
					case -70:
						break;
					case 71:
						{
}
					case -71:
						break;
					case 72:
						{
}
					case -72:
						break;
					case 73:
						{
}
					case -73:
						break;
					case 74:
						{
}
					case -74:
						break;
					case 75:
						{
}
					case -75:
						break;
					case 76:
						{
}
					case -76:
						break;
					case 77:
						{
}
					case -77:
						break;
					case 78:
						{
}
					case -78:
						break;
					case 79:
						{
}
					case -79:
						break;
					case 80:
						{
}
					case -80:
						break;
					case 81:
						{
}
					case -81:
						break;
					case 82:
						{
}
					case -82:
						break;
					case 83:
						{
}
					case -83:
						break;
					case 84:
						{
}
					case -84:
						break;
					case 85:
						{
}
					case -85:
						break;
					case 86:
						{
}
					case -86:
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
