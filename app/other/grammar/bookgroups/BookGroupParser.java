// $ANTLR 3.3 Nov 30, 2010 12:45:30 BookGroup.g 2011-07-16 19:03:42

package other.grammar.bookgroups;

import java.util.HashMap;
import java.util.ArrayList;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class BookGroupParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "WS", "NEWLINE", "LBRACE", "RBRACE", "TITLE", "ISBN", "ISBN13", "AUTHORS", "QUOTEDALPHANUM", "':'"
    };
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


        public BookGroupParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public BookGroupParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return BookGroupParser.tokenNames; }
    public String getGrammarFileName() { return "BookGroup.g"; }


    ArrayList bookGroupLst = new ArrayList();



    // $ANTLR start "prog"
    // BookGroup.g:16:1: prog returns [ArrayList value] : ( WS | NEWLINE )* ( bookgroup ( WS | NEWLINE )* )+ ;
    public final ArrayList prog() throws RecognitionException {
        ArrayList value = null;

        try {
            // BookGroup.g:17:9: ( ( WS | NEWLINE )* ( bookgroup ( WS | NEWLINE )* )+ )
            // BookGroup.g:17:11: ( WS | NEWLINE )* ( bookgroup ( WS | NEWLINE )* )+
            {
            // BookGroup.g:17:11: ( WS | NEWLINE )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>=WS && LA1_0<=NEWLINE)) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // BookGroup.g:
            	    {
            	    if ( (input.LA(1)>=WS && input.LA(1)<=NEWLINE) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // BookGroup.g:18:17: ( bookgroup ( WS | NEWLINE )* )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==LBRACE) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // BookGroup.g:18:18: bookgroup ( WS | NEWLINE )*
            	    {
            	    pushFollow(FOLLOW_bookgroup_in_prog85);
            	    bookgroup();

            	    state._fsp--;

            	    // BookGroup.g:18:28: ( WS | NEWLINE )*
            	    loop2:
            	    do {
            	        int alt2=2;
            	        int LA2_0 = input.LA(1);

            	        if ( ((LA2_0>=WS && LA2_0<=NEWLINE)) ) {
            	            alt2=1;
            	        }


            	        switch (alt2) {
            	    	case 1 :
            	    	    // BookGroup.g:
            	    	    {
            	    	    if ( (input.LA(1)>=WS && input.LA(1)<=NEWLINE) ) {
            	    	        input.consume();
            	    	        state.errorRecovery=false;
            	    	    }
            	    	    else {
            	    	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	    	        throw mse;
            	    	    }


            	    	    }
            	    	    break;

            	    	default :
            	    	    break loop2;
            	        }
            	    } while (true);


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);

            return this.bookGroupLst;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "prog"


    // $ANTLR start "bookgroup"
    // BookGroup.g:22:1: bookgroup : LBRACE ( WS )* ( NEWLINE )* (arr= prop )+ RBRACE ;
    public final void bookgroup() throws RecognitionException {
        String[] arr = null;


        try {
            // BookGroup.g:23:2: ( LBRACE ( WS )* ( NEWLINE )* (arr= prop )+ RBRACE )
            // BookGroup.g:23:4: LBRACE ( WS )* ( NEWLINE )* (arr= prop )+ RBRACE
            {
            HashMap bookGroupDetails = new HashMap();
            match(input,LBRACE,FOLLOW_LBRACE_in_bookgroup166); 
            // BookGroup.g:25:10: ( WS )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==WS) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // BookGroup.g:25:10: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_bookgroup178); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            // BookGroup.g:26:10: ( NEWLINE )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==NEWLINE) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // BookGroup.g:26:10: NEWLINE
            	    {
            	    match(input,NEWLINE,FOLLOW_NEWLINE_in_bookgroup191); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // BookGroup.g:27:10: (arr= prop )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>=TITLE && LA6_0<=AUTHORS)) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // BookGroup.g:27:11: arr= prop
            	    {
            	    pushFollow(FOLLOW_prop_in_bookgroup209);
            	    arr=prop();

            	    state._fsp--;

            	    bookGroupDetails.put(arr[0], arr[1]);

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_bookgroup224); 
            bookGroupLst.add(bookGroupDetails);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "bookgroup"


    // $ANTLR start "prop"
    // BookGroup.g:34:1: prop returns [String[] value] : ( TITLE | ISBN | ISBN13 | AUTHORS ) ':' QUOTEDALPHANUM ( WS )* NEWLINE ;
    public final String[] prop() throws RecognitionException {
        String[] value = null;

        Token TITLE1=null;
        Token ISBN2=null;
        Token ISBN133=null;
        Token AUTHORS4=null;
        Token QUOTEDALPHANUM5=null;

        try {
            // BookGroup.g:36:9: ( ( TITLE | ISBN | ISBN13 | AUTHORS ) ':' QUOTEDALPHANUM ( WS )* NEWLINE )
            // BookGroup.g:36:11: ( TITLE | ISBN | ISBN13 | AUTHORS ) ':' QUOTEDALPHANUM ( WS )* NEWLINE
            {
            value = new String[2];
            // BookGroup.g:37:17: ( TITLE | ISBN | ISBN13 | AUTHORS )
            int alt7=4;
            switch ( input.LA(1) ) {
            case TITLE:
                {
                alt7=1;
                }
                break;
            case ISBN:
                {
                alt7=2;
                }
                break;
            case ISBN13:
                {
                alt7=3;
                }
                break;
            case AUTHORS:
                {
                alt7=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // BookGroup.g:37:18: TITLE
                    {
                    TITLE1=(Token)match(input,TITLE,FOLLOW_TITLE_in_prop310); 
                    value[0] = (TITLE1!=null?TITLE1.getText():null);

                    }
                    break;
                case 2 :
                    // BookGroup.g:38:18: ISBN
                    {
                    ISBN2=(Token)match(input,ISBN,FOLLOW_ISBN_in_prop331); 
                    value[0] = (ISBN2!=null?ISBN2.getText():null);

                    }
                    break;
                case 3 :
                    // BookGroup.g:39:18: ISBN13
                    {
                    ISBN133=(Token)match(input,ISBN13,FOLLOW_ISBN13_in_prop352); 
                    value[0] = (ISBN133!=null?ISBN133.getText():null);

                    }
                    break;
                case 4 :
                    // BookGroup.g:40:18: AUTHORS
                    {
                    AUTHORS4=(Token)match(input,AUTHORS,FOLLOW_AUTHORS_in_prop373); 
                    value[0] = (AUTHORS4!=null?AUTHORS4.getText():null);

                    }
                    break;

            }

            match(input,13,FOLLOW_13_in_prop395); 
            QUOTEDALPHANUM5=(Token)match(input,QUOTEDALPHANUM,FOLLOW_QUOTEDALPHANUM_in_prop414); 
            value[1] = (QUOTEDALPHANUM5!=null?QUOTEDALPHANUM5.getText():null);
            // BookGroup.g:43:17: ( WS )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==WS) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // BookGroup.g:43:17: WS
            	    {
            	    match(input,WS,FOLLOW_WS_in_prop434); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match(input,NEWLINE,FOLLOW_NEWLINE_in_prop454); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return value;
    }
    // $ANTLR end "prop"

    // Delegated rules


 

    public static final BitSet FOLLOW_set_in_prog43 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_bookgroup_in_prog85 = new BitSet(new long[]{0x0000000000000072L});
    public static final BitSet FOLLOW_set_in_prog87 = new BitSet(new long[]{0x0000000000000072L});
    public static final BitSet FOLLOW_LBRACE_in_bookgroup166 = new BitSet(new long[]{0x0000000000000F30L});
    public static final BitSet FOLLOW_WS_in_bookgroup178 = new BitSet(new long[]{0x0000000000000F30L});
    public static final BitSet FOLLOW_NEWLINE_in_bookgroup191 = new BitSet(new long[]{0x0000000000000F30L});
    public static final BitSet FOLLOW_prop_in_bookgroup209 = new BitSet(new long[]{0x0000000000000FB0L});
    public static final BitSet FOLLOW_RBRACE_in_bookgroup224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TITLE_in_prop310 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_ISBN_in_prop331 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_ISBN13_in_prop352 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_AUTHORS_in_prop373 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_prop395 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_QUOTEDALPHANUM_in_prop414 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_WS_in_prop434 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_NEWLINE_in_prop454 = new BitSet(new long[]{0x0000000000000002L});

}