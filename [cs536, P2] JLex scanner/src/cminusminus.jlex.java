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
	private final int INQUOTE = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		46,
		50
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
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NOT_ACCEPT,
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
		/* 97 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"44:9,43,4,44:2,3,44:18,43,37,24,2,44:2,38,26,32,33,36,34,27,35,29,1,23:10,4" +
"4,28,31,40,30,26,44,22:26,44,25,44:2,22,44,21,8,15,17,13,11,22,20,5,22:2,10" +
",22,6,9,22:2,12,18,7,14,16,19,22:3,41,39,42,44:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,98,
"0,1,2,3,1,4,5,1:4,6,7,1:2,8,9,1,10,11,1:2,12,1,13,1:10,13:11,14,1,15,1,16,1" +
",17,1,13,18,19,20,21,22,23,24,25,26,17,27,28,29,30,31,32,33,34,35,36,37,38," +
"39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59")[0];

	private int yy_nxt[][] = unpackFromString(60,45,
"1,2,3,-1,4,5,54,84,85,54:2,92,96,86,54,74,87,54,97,93,54:3,6,7,53:2,8,9,10," +
"11,12,13,14,15,16,17,18,59,62,19,20,21,22,53,-1:46,23,-1:45,23,-1:47,54,60," +
"54:4,24,54:12,-1:44,6,-1:51,25,-1:9,26,-1:35,27,-1:8,28,-1:38,29,-1:45,30,-" +
"1:49,31,-1:44,34,-1:47,22,-1:6,54:19,-1:21,1,55:3,4,55:19,47,64,55:19,-1,48" +
":5,56:2,48:16,49,-1,56,48:18,1,57:3,51,57:40,-1,48:5,61:2,48:16,61:3,48:18," +
"-1,55:3,-1,55:19,47,52,55:19,-1,56:3,-1,56:19,49,-1,56:19,-1,57:3,-1,57:40," +
"-1:6,61:2,-1:16,61:3,-1:56,32,-1:11,54:2,35,54:16,-1:22,61:3,-1,61:19,47,58" +
",61:19,-1:39,33,-1:10,54,36,54:17,-1:26,54:8,37,54:10,-1:26,54:5,38,54:13,-" +
"1:26,54:8,39,54:10,-1:26,54:2,40,54:16,-1:26,54:12,41,54:6,-1:26,54:8,42,54" +
":10,-1:26,54:8,43,54:10,-1:26,54,44,54:17,-1:26,54:2,45,54:16,-1:26,63,54:3" +
",78,54:14,-1:26,54:9,65,54:9,-1:26,54:4,66,54:14,-1:26,54:13,67,54:5,-1:26," +
"54:9,68,54:9,-1:26,69,54:18,-1:26,54:13,70,54:5,-1:26,54:5,71,54:13,-1:26,5" +
"4:7,72,54:11,-1:26,54:10,73,54:8,-1:26,54:7,75,54:11,-1:26,54:4,76,54:14,-1" +
":26,54:5,77,54:13,-1:26,54:4,79,54:14,-1:26,54:5,80,54:13,-1:26,81,54:18,-1" +
":26,54:9,82,54:9,-1:26,54:9,83,54:9,-1:26,54:16,88,54:2,-1:26,54:15,89,54:3" +
",-1:26,54:2,90,54:16,-1:26,54:7,91,54:11,-1:26,54:8,94,54:10,-1:26,54:2,95," +
"54:16,-1:21");

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
					case 1:
						
					case -2:
						break;
					case 2:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.DIVIDE, new TokenVal(yyline + 1, currentCharNum));
}
					case -3:
						break;
					case 3:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    // Illegal characters
    ErrMsg.fatal(yyline+1, CharNum.num, "illegal character ignored:  " + yytext());
    CharNum.num++;
}
					case -4:
						break;
					case 4:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    CharNum.num = 1; 
}
					case -5:
						break;
					case 5:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -6:
						break;
					case 6:
						{
    int currentCharNum = CharNum.num;
    String stringValue = yytext();
    int value = 0;
    /* check for overflow for bad integer literals (integer literals larger than
        Integer.MAX_VALUE)
        note - NumberFormatException is not specific for overflow errors, but 
        reaching this transition state would exclude parsing errors. */
    try {
        value = Integer.parseInt(stringValue);
    } catch (NumberFormatException e) {
        // the line and column numbers used in the error message should correspond to the position of the first character in the string/integer literal.
        ErrMsg.warn(yyline + 1, currentCharNum,
            "integer literal too large; using max value");
        value = Integer.MAX_VALUE;
    } finally {
        CharNum.num += stringValue.length();
        return new Symbol(sym.INTLITERAL,
            new IntLitTokenVal(yyline + 1, currentCharNum, value));
    }
}
					case -7:
						break;
					case 7:
						{
    yybegin(INQUOTE);
}
					case -8:
						break;
					case 8:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.COMMA, new TokenVal(yyline + 1, currentCharNum));
}
					case -9:
						break;
					case 9:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.SEMICOLON, new TokenVal(yyline + 1, currentCharNum));
}
					case -10:
						break;
					case 10:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.DOT, new TokenVal(yyline + 1, currentCharNum));
}
					case -11:
						break;
					case 11:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.GREATER, new TokenVal(yyline + 1, currentCharNum));
}
					case -12:
						break;
					case 12:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LESS, new TokenVal(yyline + 1, currentCharNum));
}
					case -13:
						break;
					case 13:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LPAREN, new TokenVal(yyline + 1, currentCharNum));
}
					case -14:
						break;
					case 14:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RPAREN, new TokenVal(yyline + 1, currentCharNum));
}
					case -15:
						break;
					case 15:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.PLUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -16:
						break;
					case 16:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.MINUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -17:
						break;
					case 17:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.TIMES, new TokenVal(yyline + 1, currentCharNum));
}
					case -18:
						break;
					case 18:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.NOT, new TokenVal(yyline + 1, currentCharNum));
}
					case -19:
						break;
					case 19:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.ASSIGN, new TokenVal(yyline + 1, currentCharNum));
}
					case -20:
						break;
					case 20:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LCURLY, new TokenVal(yyline + 1, currentCharNum));
}
					case -21:
						break;
					case 21:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RCURLY, new TokenVal(yyline + 1, currentCharNum));
}
					case -22:
						break;
					case 22:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    CharNum.num += yytext().length(); 
}
					case -23:
						break;
					case 23:
						{ yybegin(COMMENT); }
					case -24:
						break;
					case 24:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.IF, new TokenVal(yyline + 1, currentCharNum));
}
					case -25:
						break;
					case 25:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.READ, new TokenVal(yyline + 1, currentCharNum));
}
					case -26:
						break;
					case 26:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.GREATEREQ, new TokenVal(yyline + 1, currentCharNum));
}
					case -27:
						break;
					case 27:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.WRITE, new TokenVal(yyline + 1, currentCharNum));
}
					case -28:
						break;
					case 28:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.LESSEQ, new TokenVal(yyline + 1, currentCharNum));
}
					case -29:
						break;
					case 29:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.PLUSPLUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -30:
						break;
					case 30:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.MINUSMINUS, new TokenVal(yyline + 1, currentCharNum));
}
					case -31:
						break;
					case 31:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.NOTEQUALS, new TokenVal(yyline + 1, currentCharNum));
}
					case -32:
						break;
					case 32:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.AND, new TokenVal(yyline + 1, currentCharNum));
}
					case -33:
						break;
					case 33:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.OR, new TokenVal(yyline + 1, currentCharNum));
}
					case -34:
						break;
					case 34:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.EQUALS, new TokenVal(yyline + 1, currentCharNum));
}
					case -35:
						break;
					case 35:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.INT, new TokenVal(yyline + 1, currentCharNum));
}
					case -36:
						break;
					case 36:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.CIN, new TokenVal(yyline + 1, currentCharNum));
}
					case -37:
						break;
					case 37:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.TRUE, new TokenVal(yyline + 1, currentCharNum));
}
					case -38:
						break;
					case 38:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.BOOL, new TokenVal(yyline + 1, currentCharNum));
}
					case -39:
						break;
					case 39:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.ELSE, new TokenVal(yyline + 1, currentCharNum));
}
					case -40:
						break;
					case 40:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.COUT, new TokenVal(yyline + 1, currentCharNum));
}
					case -41:
						break;
					case 41:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.VOID, new TokenVal(yyline + 1, currentCharNum));
}
					case -42:
						break;
					case 42:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.FALSE, new TokenVal(yyline + 1, currentCharNum));
}
					case -43:
						break;
					case 43:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.WHILE, new TokenVal(yyline + 1, currentCharNum));
}
					case -44:
						break;
					case 44:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.RETURN, new TokenVal(yyline + 1, currentCharNum));
}
					case -45:
						break;
					case 45:
						{
    int currentCharNum = CharNum.num;
    CharNum.num += yytext().length();
    return new Symbol(sym.STRUCT, new TokenVal(yyline + 1, currentCharNum));
}
					case -46:
						break;
					case 46:
						{
    // Unterminated string literals - if there is a newline or end-of-file before the closing quote. (start looking for the next token after the newline)
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "unterminated string literal ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -47:
						break;
					case 47:
						{
    yybegin(YYINITIAL);
    // valid string literal
    int currentCharNum = CharNum.num;
    // put quote back and correct character # for message
    // note: could figure out how to pass state over the transition
    currentCharNum--; 
    String value = "\"" + yytext();
    CharNum.num += value.length();
    return new Symbol(sym.STRINGLITERAL, new StrLitTokenVal(yyline + 1, currentCharNum, value));
}
					case -48:
						break;
					case 48:
						{
    // a string literal that contains a bad escaped character and is unterminated; (start looking for the next token after the newline). Note that a string literal that has a newline immediately after a backslash should be treated as having a bad escaped character and being unterminated.
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "unterminated string literal with bad escaped character ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -49:
						break;
					case 49:
						{
    // Bad string literals - includes a bad "escaped" character; (start looking for the next token after the closing quote). 
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "string literal with bad escaped character ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -50:
						break;
					case 50:
						{
    // ignore
}
					case -51:
						break;
					case 51:
						{ yybegin(YYINITIAL); }
					case -52:
						break;
					case 53:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    // Illegal characters
    ErrMsg.fatal(yyline+1, CharNum.num, "illegal character ignored:  " + yytext());
    CharNum.num++;
}
					case -53:
						break;
					case 54:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -54:
						break;
					case 55:
						{
    // Unterminated string literals - if there is a newline or end-of-file before the closing quote. (start looking for the next token after the newline)
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "unterminated string literal ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -55:
						break;
					case 56:
						{
    // a string literal that contains a bad escaped character and is unterminated; (start looking for the next token after the newline). Note that a string literal that has a newline immediately after a backslash should be treated as having a bad escaped character and being unterminated.
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "unterminated string literal with bad escaped character ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -56:
						break;
					case 57:
						{
    // ignore
}
					case -57:
						break;
					case 59:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    // Illegal characters
    ErrMsg.fatal(yyline+1, CharNum.num, "illegal character ignored:  " + yytext());
    CharNum.num++;
}
					case -58:
						break;
					case 60:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -59:
						break;
					case 61:
						{
    // Unterminated string literals - if there is a newline or end-of-file before the closing quote. (start looking for the next token after the newline)
    int currentCharNum = CharNum.num;
    currentCharNum--; // note: could figure out how to pass state over the transition
    ErrMsg.fatal(yyline + 1, CharNum.num, "unterminated string literal ignored");
    CharNum.num = 1; // reset
    yybegin(YYINITIAL);
}
					case -60:
						break;
					case 62:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    // Illegal characters
    ErrMsg.fatal(yyline+1, CharNum.num, "illegal character ignored:  " + yytext());
    CharNum.num++;
}
					case -61:
						break;
					case 63:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -62:
						break;
					case 64:
						{ 
    yybegin(YYINITIAL); // just to make sure it stays at starting state
    // Illegal characters
    ErrMsg.fatal(yyline+1, CharNum.num, "illegal character ignored:  " + yytext());
    CharNum.num++;
}
					case -63:
						break;
					case 65:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -64:
						break;
					case 66:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -65:
						break;
					case 67:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -66:
						break;
					case 68:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -67:
						break;
					case 69:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -68:
						break;
					case 70:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -69:
						break;
					case 71:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -70:
						break;
					case 72:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -71:
						break;
					case 73:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -72:
						break;
					case 74:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -73:
						break;
					case 75:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -74:
						break;
					case 76:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -75:
						break;
					case 77:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -76:
						break;
					case 78:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -77:
						break;
					case 79:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -78:
						break;
					case 80:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -79:
						break;
					case 81:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -80:
						break;
					case 82:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -81:
						break;
					case 83:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -82:
						break;
					case 84:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -83:
						break;
					case 85:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -84:
						break;
					case 86:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -85:
						break;
					case 87:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -86:
						break;
					case 88:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -87:
						break;
					case 89:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -88:
						break;
					case 90:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -89:
						break;
					case 91:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -90:
						break;
					case 92:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -91:
						break;
					case 93:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -92:
						break;
					case 94:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -93:
						break;
					case 95:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -94:
						break;
					case 96:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -95:
						break;
					case 97:
						{
    int currentCharNum = CharNum.num;
    String value = yytext();
    CharNum.num += value.length();
    return new Symbol(sym.ID, new IdTokenVal(yyline + 1, currentCharNum, value));
}
					case -96:
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
