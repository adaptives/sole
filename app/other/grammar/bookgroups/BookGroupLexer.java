// $ANTLR 3.3 Nov 30, 2010 12:45:30 BookGroup.g 2011-07-16 19:03:42
package other.grammar.bookgroups;

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class BookGroupLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__13=13;
    public static final int WS=4;
    public static final int NEWLINE=5;
    public static final int LBRACE=6;
    public static final int RBRACE=7;
    public static final int TITLE=8;
    public static final int ISBN=9;
    public static final int ISBN13=10;
    public static final int AUTHORS=11;
    public static final int QUOTEDALPHANUM=12;

    // delegates
    // delegators

    public BookGroupLexer() {;} 
    public BookGroupLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public BookGroupLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "BookGroup.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:5:7: ( ':' )
            // BookGroup.g:5:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:32:5: ( ( ' ' | '\\t' )+ )
            // BookGroup.g:32:7: ( ' ' | '\\t' )+
            {
            // BookGroup.g:32:7: ( ' ' | '\\t' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // BookGroup.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:46:9: ( '{' )
            // BookGroup.g:46:11: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:49:9: ( ( '\\r' )? '\\n' )
            // BookGroup.g:49:11: ( '\\r' )? '\\n'
            {
            // BookGroup.g:49:11: ( '\\r' )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\r') ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // BookGroup.g:49:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:51:9: ( '}' )
            // BookGroup.g:51:11: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "TITLE"
    public final void mTITLE() throws RecognitionException {
        try {
            int _type = TITLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:53:8: ( 'title' )
            // BookGroup.g:53:10: 'title'
            {
            match("title"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TITLE"

    // $ANTLR start "ISBN"
    public final void mISBN() throws RecognitionException {
        try {
            int _type = ISBN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:55:7: ( 'isbn' )
            // BookGroup.g:55:9: 'isbn'
            {
            match("isbn"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ISBN"

    // $ANTLR start "ISBN13"
    public final void mISBN13() throws RecognitionException {
        try {
            int _type = ISBN13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:57:9: ( 'isbn13' )
            // BookGroup.g:57:11: 'isbn13'
            {
            match("isbn13"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ISBN13"

    // $ANTLR start "AUTHORS"
    public final void mAUTHORS() throws RecognitionException {
        try {
            int _type = AUTHORS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:59:9: ( 'authors' )
            // BookGroup.g:59:11: 'authors'
            {
            match("authors"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AUTHORS"

    // $ANTLR start "QUOTEDALPHANUM"
    public final void mQUOTEDALPHANUM() throws RecognitionException {
        try {
            int _type = QUOTEDALPHANUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // BookGroup.g:62:2: ( '\"' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | ' ' | '.' | ',' )* '\"' )
            // BookGroup.g:62:4: '\"' ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | ' ' | '.' | ',' )* '\"'
            {
            match('\"'); 
            // BookGroup.g:62:8: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | ' ' | '.' | ',' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==' '||LA3_0==','||LA3_0=='.'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // BookGroup.g:
            	    {
            	    if ( input.LA(1)==' '||input.LA(1)==','||input.LA(1)=='.'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUOTEDALPHANUM"

    public void mTokens() throws RecognitionException {
        // BookGroup.g:1:8: ( T__13 | WS | LBRACE | NEWLINE | RBRACE | TITLE | ISBN | ISBN13 | AUTHORS | QUOTEDALPHANUM )
        int alt4=10;
        alt4 = dfa4.predict(input);
        switch (alt4) {
            case 1 :
                // BookGroup.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // BookGroup.g:1:16: WS
                {
                mWS(); 

                }
                break;
            case 3 :
                // BookGroup.g:1:19: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 4 :
                // BookGroup.g:1:26: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 5 :
                // BookGroup.g:1:34: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 6 :
                // BookGroup.g:1:41: TITLE
                {
                mTITLE(); 

                }
                break;
            case 7 :
                // BookGroup.g:1:47: ISBN
                {
                mISBN(); 

                }
                break;
            case 8 :
                // BookGroup.g:1:52: ISBN13
                {
                mISBN13(); 

                }
                break;
            case 9 :
                // BookGroup.g:1:59: AUTHORS
                {
                mAUTHORS(); 

                }
                break;
            case 10 :
                // BookGroup.g:1:67: QUOTEDALPHANUM
                {
                mQUOTEDALPHANUM(); 

                }
                break;

        }

    }


    protected DFA4 dfa4 = new DFA4(this);
    static final String DFA4_eotS =
        "\14\uffff\1\16\2\uffff";
    static final String DFA4_eofS =
        "\17\uffff";
    static final String DFA4_minS =
        "\1\11\6\uffff\1\163\2\uffff\1\142\1\156\1\61\2\uffff";
    static final String DFA4_maxS =
        "\1\175\6\uffff\1\163\2\uffff\1\142\1\156\1\61\2\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\uffff\1\11\1\12\3\uffff\1\10"+
        "\1\7";
    static final String DFA4_specialS =
        "\17\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\2\1\4\2\uffff\1\4\22\uffff\1\2\1\uffff\1\11\27\uffff\1\1"+
            "\46\uffff\1\10\7\uffff\1\7\12\uffff\1\6\6\uffff\1\3\1\uffff"+
            "\1\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\12",
            "",
            "",
            "\1\13",
            "\1\14",
            "\1\15",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | WS | LBRACE | NEWLINE | RBRACE | TITLE | ISBN | ISBN13 | AUTHORS | QUOTEDALPHANUM );";
        }
    }
 

}