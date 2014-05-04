grammar Board;

// A grammar that specifies the format of an input file from which a Board can be created. 
// Impelemented by: asolei
@header {
package pingball.parser;
}

// This adds code to the generated lexer and parser.
@members {
    /**
     * Call this method to have the lexer or parser throw a RuntimeException if
     * it encounters an error.
     */
    public void reportErrorsAsExceptions() {
        addErrorListener(new ExceptionThrowingErrorListener());
    }
    
    private static class ExceptionThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new RuntimeException(msg);
        }
    }
}

/*
 * These are the lexical rules. They define the tokens used by the lexer.
 * *** ANTLR requires tokens to be CAPITALIZED. 
 * Implemented by: asolei
 */
 
 XVAL : 'x' WHITESPACE? EQUALS WHITESPACE? NUMBER ;
 YVAL : 'y' WHITESPACE? EQUALS WHITESPACE? NUMBER ;
 XVEL : 'xVelocity' WHITESPACE? EQUALS WHITESPACE? FLOAT ;
 YVEL : 'yVelocity' WHITESPACE? EQUALS WHITESPACE? FLOAT ;
 FRICTION1 : 'friction1' WHITESPACE? EQUALS WHITESPACE? FLOAT ;
 FRICTION2 : 'friction2' WHITESPACE? EQUALS WHITESPACE? FLOAT ; 
 GRAVITY : 'gravity' WHITESPACE? EQUALS WHITESPACE? FLOAT ;
 WIDTH : 'width' WHITESPACE? EQUALS WHITESPACE? INTEGER ;
 HEIGHT : 'height' WHITESPACE? EQUALS WHITESPACE? INTEGER ;
 NAMEVAL : 'name' WHITESPACE? EQUALS WHITESPACE? NAME;
 
 BOARD : 'board';
 BALL : 'ball'; 
 SQUARE : 'squareBumper'; 
 TRIANGLE : 'triangleBumper';
 CIRCLE : 'circleBumper';
 LEFT : 'leftFlipper';
 RIGHT : 'rightFlipper';
 ABS : 'absorber';
 FIRE : 'fire';
 TRIGGER : 'trigger' WHITESPACE? EQUALS WHITESPACE? NAME ;
 ACTION : 'action' WHITESPACE? EQUALS WHITESPACE? NAME ;
 ORI : 'orientation' WHITESPACE? EQUALS WHITESPACE? DIR ;
 
 fragment INTEGER : [0-9]+ ;
 fragment NUMBER : ([0-9]+|'-'?([0-9]+'.'[0-9]*|'.'?[0-9]+)) ;
 fragment DIR : '90' | '0' | '180' | '270' ;
 fragment FLOAT : '-'?([0-9]+'.'[0-9]*|'.'?[0-9]+) ;
 fragment NAME : [A-Za-z_][A-Za-z_0-9]* ;
 fragment EQUALS : '=';
 
 WHITESPACE : [ \t]+ -> skip ;
 NEWLINE : '\r'? '\n' ;
 COMMENT : '#'\~('\r'|'\n')* NEWLINE? -> skip ;

 /*
 * These are the parser rules. They define the structures used by the parser.
 * *** ANTLR requires grammar nonterminals to be lowercase
 * Implemented by: asolei
 */
 file : board attribute+ EOF ;
 board : BOARD NAMEVAL GRAVITY? FRICTION1? FRICTION2? NEWLINE*;
 
 attribute : ball | bumper | actions | fire;
 ball : BALL NAMEVAL XVAL YVAL XVEL YVEL NEWLINE*;
 bumper : square | circle | triangle ;
 square : SQUARE NAMEVAL XVAL YVAL NEWLINE*;
 circle : CIRCLE NAMEVAL XVAL YVAL NEWLINE*;
 triangle : TRIANGLE NAMEVAL XVAL YVAL ORI NEWLINE*;
 
 actions : flipper | absorber ;
 flipper : (LEFT | RIGHT) NAMEVAL XVAL YVAL ORI NEWLINE*;
 absorber : ABS NAMEVAL XVAL YVAL WIDTH HEIGHT NEWLINE*;
 
 fire : FIRE TRIGGER ACTION NEWLINE*;