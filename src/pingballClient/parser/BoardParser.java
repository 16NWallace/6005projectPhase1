// Generated from Board.g4 by ANTLR 4.0

package pingballClient.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BoardParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		XVAL=1, YVAL=2, XVEL=3, YVEL=4, FRICTION1=5, FRICTION2=6, GRAVITY=7, WIDTH=8, 
		HEIGHT=9, NAMEVAL=10, BOARD=11, BALL=12, SQUARE=13, TRIANGLE=14, CIRCLE=15, 
		LEFT=16, RIGHT=17, ABS=18, FIRE=19, TRIGGER=20, ACTION=21, ORI=22, WHITESPACE=23, 
		NEWLINE=24, COMMENT=25;
	public static final String[] tokenNames = {
		"<INVALID>", "XVAL", "YVAL", "XVEL", "YVEL", "FRICTION1", "FRICTION2", 
		"GRAVITY", "WIDTH", "HEIGHT", "NAMEVAL", "'board'", "'ball'", "'squareBumper'", 
		"'triangleBumper'", "'circleBumper'", "'leftFlipper'", "'rightFlipper'", 
		"'absorber'", "'fire'", "TRIGGER", "ACTION", "ORI", "WHITESPACE", "NEWLINE", 
		"COMMENT"
	};
	public static final int
		RULE_file = 0, RULE_board = 1, RULE_attribute = 2, RULE_ball = 3, RULE_bumper = 4, 
		RULE_square = 5, RULE_circle = 6, RULE_triangle = 7, RULE_actions = 8, 
		RULE_flipper = 9, RULE_absorber = 10, RULE_fire = 11;
	public static final String[] ruleNames = {
		"file", "board", "attribute", "ball", "bumper", "square", "circle", "triangle", 
		"actions", "flipper", "absorber", "fire"
	};

	@Override
	public String getGrammarFileName() { return "Board.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }


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

	public BoardParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FileContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public TerminalNode EOF() { return getToken(BoardParser.EOF, 0); }
		public BoardContext board() {
			return getRuleContext(BoardContext.class,0);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24); board();
			setState(26); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(25); attribute();
				}
				}
				setState(28); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BALL) | (1L << SQUARE) | (1L << TRIANGLE) | (1L << CIRCLE) | (1L << LEFT) | (1L << RIGHT) | (1L << ABS) | (1L << FIRE))) != 0) );
			setState(30); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoardContext extends ParserRuleContext {
		public TerminalNode FRICTION2() { return getToken(BoardParser.FRICTION2, 0); }
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode GRAVITY() { return getToken(BoardParser.GRAVITY, 0); }
		public TerminalNode BOARD() { return getToken(BoardParser.BOARD, 0); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode FRICTION1() { return getToken(BoardParser.FRICTION1, 0); }
		public BoardContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_board; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterBoard(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitBoard(this);
		}
	}

	public final BoardContext board() throws RecognitionException {
		BoardContext _localctx = new BoardContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_board);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32); match(BOARD);
			setState(33); match(NAMEVAL);
			setState(35);
			_la = _input.LA(1);
			if (_la==GRAVITY) {
				{
				setState(34); match(GRAVITY);
				}
			}

			setState(38);
			_la = _input.LA(1);
			if (_la==FRICTION1) {
				{
				setState(37); match(FRICTION1);
				}
			}

			setState(41);
			_la = _input.LA(1);
			if (_la==FRICTION2) {
				{
				setState(40); match(FRICTION2);
				}
			}

			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(43); match(NEWLINE);
				}
				}
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public BallContext ball() {
			return getRuleContext(BallContext.class,0);
		}
		public BumperContext bumper() {
			return getRuleContext(BumperContext.class,0);
		}
		public FireContext fire() {
			return getRuleContext(FireContext.class,0);
		}
		public ActionsContext actions() {
			return getRuleContext(ActionsContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_attribute);
		try {
			setState(53);
			switch (_input.LA(1)) {
			case BALL:
				enterOuterAlt(_localctx, 1);
				{
				setState(49); ball();
				}
				break;
			case SQUARE:
			case TRIANGLE:
			case CIRCLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(50); bumper();
				}
				break;
			case LEFT:
			case RIGHT:
			case ABS:
				enterOuterAlt(_localctx, 3);
				{
				setState(51); actions();
				}
				break;
			case FIRE:
				enterOuterAlt(_localctx, 4);
				{
				setState(52); fire();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BallContext extends ParserRuleContext {
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public TerminalNode XVEL() { return getToken(BoardParser.XVEL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode BALL() { return getToken(BoardParser.BALL, 0); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public TerminalNode YVEL() { return getToken(BoardParser.YVEL, 0); }
		public BallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ball; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterBall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitBall(this);
		}
	}

	public final BallContext ball() throws RecognitionException {
		BallContext _localctx = new BallContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_ball);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55); match(BALL);
			setState(56); match(NAMEVAL);
			setState(57); match(XVAL);
			setState(58); match(YVAL);
			setState(59); match(XVEL);
			setState(60); match(YVEL);
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(61); match(NEWLINE);
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BumperContext extends ParserRuleContext {
		public TriangleContext triangle() {
			return getRuleContext(TriangleContext.class,0);
		}
		public CircleContext circle() {
			return getRuleContext(CircleContext.class,0);
		}
		public SquareContext square() {
			return getRuleContext(SquareContext.class,0);
		}
		public BumperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bumper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterBumper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitBumper(this);
		}
	}

	public final BumperContext bumper() throws RecognitionException {
		BumperContext _localctx = new BumperContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_bumper);
		try {
			setState(70);
			switch (_input.LA(1)) {
			case SQUARE:
				enterOuterAlt(_localctx, 1);
				{
				setState(67); square();
				}
				break;
			case CIRCLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(68); circle();
				}
				break;
			case TRIANGLE:
				enterOuterAlt(_localctx, 3);
				{
				setState(69); triangle();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SquareContext extends ParserRuleContext {
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode SQUARE() { return getToken(BoardParser.SQUARE, 0); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public SquareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_square; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterSquare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitSquare(this);
		}
	}

	public final SquareContext square() throws RecognitionException {
		SquareContext _localctx = new SquareContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_square);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); match(SQUARE);
			setState(73); match(NAMEVAL);
			setState(74); match(XVAL);
			setState(75); match(YVAL);
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(76); match(NEWLINE);
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CircleContext extends ParserRuleContext {
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode CIRCLE() { return getToken(BoardParser.CIRCLE, 0); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public CircleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterCircle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitCircle(this);
		}
	}

	public final CircleContext circle() throws RecognitionException {
		CircleContext _localctx = new CircleContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_circle);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); match(CIRCLE);
			setState(83); match(NAMEVAL);
			setState(84); match(XVAL);
			setState(85); match(YVAL);
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(86); match(NEWLINE);
				}
				}
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriangleContext extends ParserRuleContext {
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode TRIANGLE() { return getToken(BoardParser.TRIANGLE, 0); }
		public TerminalNode ORI() { return getToken(BoardParser.ORI, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public TriangleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triangle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterTriangle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitTriangle(this);
		}
	}

	public final TriangleContext triangle() throws RecognitionException {
		TriangleContext _localctx = new TriangleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_triangle);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); match(TRIANGLE);
			setState(93); match(NAMEVAL);
			setState(94); match(XVAL);
			setState(95); match(YVAL);
			setState(96); match(ORI);
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(97); match(NEWLINE);
				}
				}
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionsContext extends ParserRuleContext {
		public AbsorberContext absorber() {
			return getRuleContext(AbsorberContext.class,0);
		}
		public FlipperContext flipper() {
			return getRuleContext(FlipperContext.class,0);
		}
		public ActionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterActions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitActions(this);
		}
	}

	public final ActionsContext actions() throws RecognitionException {
		ActionsContext _localctx = new ActionsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_actions);
		try {
			setState(105);
			switch (_input.LA(1)) {
			case LEFT:
			case RIGHT:
				enterOuterAlt(_localctx, 1);
				{
				setState(103); flipper();
				}
				break;
			case ABS:
				enterOuterAlt(_localctx, 2);
				{
				setState(104); absorber();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FlipperContext extends ParserRuleContext {
		public TerminalNode RIGHT() { return getToken(BoardParser.RIGHT, 0); }
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode LEFT() { return getToken(BoardParser.LEFT, 0); }
		public TerminalNode ORI() { return getToken(BoardParser.ORI, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public FlipperContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_flipper; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterFlipper(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitFlipper(this);
		}
	}

	public final FlipperContext flipper() throws RecognitionException {
		FlipperContext _localctx = new FlipperContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_flipper);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !(_la==LEFT || _la==RIGHT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(108); match(NAMEVAL);
			setState(109); match(XVAL);
			setState(110); match(YVAL);
			setState(111); match(ORI);
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(112); match(NEWLINE);
				}
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbsorberContext extends ParserRuleContext {
		public TerminalNode ABS() { return getToken(BoardParser.ABS, 0); }
		public TerminalNode NAMEVAL() { return getToken(BoardParser.NAMEVAL, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode HEIGHT() { return getToken(BoardParser.HEIGHT, 0); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode YVAL() { return getToken(BoardParser.YVAL, 0); }
		public TerminalNode XVAL() { return getToken(BoardParser.XVAL, 0); }
		public TerminalNode WIDTH() { return getToken(BoardParser.WIDTH, 0); }
		public AbsorberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_absorber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterAbsorber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitAbsorber(this);
		}
	}

	public final AbsorberContext absorber() throws RecognitionException {
		AbsorberContext _localctx = new AbsorberContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_absorber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118); match(ABS);
			setState(119); match(NAMEVAL);
			setState(120); match(XVAL);
			setState(121); match(YVAL);
			setState(122); match(WIDTH);
			setState(123); match(HEIGHT);
			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(124); match(NEWLINE);
				}
				}
				setState(129);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FireContext extends ParserRuleContext {
		public TerminalNode FIRE() { return getToken(BoardParser.FIRE, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(BoardParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(BoardParser.NEWLINE, i);
		}
		public TerminalNode ACTION() { return getToken(BoardParser.ACTION, 0); }
		public TerminalNode TRIGGER() { return getToken(BoardParser.TRIGGER, 0); }
		public FireContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fire; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).enterFire(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BoardListener ) ((BoardListener)listener).exitFire(this);
		}
	}

	public final FireContext fire() throws RecognitionException {
		FireContext _localctx = new FireContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_fire);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130); match(FIRE);
			setState(131); match(TRIGGER);
			setState(132); match(ACTION);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(133); match(NEWLINE);
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\2\3\33\u008e\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b"+
		"\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\3\2\3\2\6\2\35\n\2\r\2\16\2"+
		"\36\3\2\3\2\3\3\3\3\3\3\5\3&\n\3\3\3\5\3)\n\3\3\3\5\3,\n\3\3\3\7\3/\n"+
		"\3\f\3\16\3\62\13\3\3\4\3\4\3\4\3\4\5\48\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\7\5A\n\5\f\5\16\5D\13\5\3\6\3\6\3\6\5\6I\n\6\3\7\3\7\3\7\3\7\3\7\7"+
		"\7P\n\7\f\7\16\7S\13\7\3\b\3\b\3\b\3\b\3\b\7\bZ\n\b\f\b\16\b]\13\b\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\7\te\n\t\f\t\16\th\13\t\3\n\3\n\5\nl\n\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\7\13t\n\13\f\13\16\13w\13\13\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\7\f\u0080\n\f\f\f\16\f\u0083\13\f\3\r\3\r\3\r\3\r\7\r\u0089\n"+
		"\r\f\r\16\r\u008c\13\r\3\r\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\3\3\22"+
		"\23\u0093\2\32\3\2\2\2\4\"\3\2\2\2\6\67\3\2\2\2\b9\3\2\2\2\nH\3\2\2\2"+
		"\fJ\3\2\2\2\16T\3\2\2\2\20^\3\2\2\2\22k\3\2\2\2\24m\3\2\2\2\26x\3\2\2"+
		"\2\30\u0084\3\2\2\2\32\34\5\4\3\2\33\35\5\6\4\2\34\33\3\2\2\2\35\36\3"+
		"\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37 \3\2\2\2 !\7\1\2\2!\3\3\2\2\2\""+
		"#\7\r\2\2#%\7\f\2\2$&\7\t\2\2%$\3\2\2\2%&\3\2\2\2&(\3\2\2\2\')\7\7\2\2"+
		"(\'\3\2\2\2()\3\2\2\2)+\3\2\2\2*,\7\b\2\2+*\3\2\2\2+,\3\2\2\2,\60\3\2"+
		"\2\2-/\7\32\2\2.-\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\5\3"+
		"\2\2\2\62\60\3\2\2\2\638\5\b\5\2\648\5\n\6\2\658\5\22\n\2\668\5\30\r\2"+
		"\67\63\3\2\2\2\67\64\3\2\2\2\67\65\3\2\2\2\67\66\3\2\2\28\7\3\2\2\29:"+
		"\7\16\2\2:;\7\f\2\2;<\7\3\2\2<=\7\4\2\2=>\7\5\2\2>B\7\6\2\2?A\7\32\2\2"+
		"@?\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\t\3\2\2\2DB\3\2\2\2EI\5\f\7"+
		"\2FI\5\16\b\2GI\5\20\t\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2\2I\13\3\2\2\2JK\7"+
		"\17\2\2KL\7\f\2\2LM\7\3\2\2MQ\7\4\2\2NP\7\32\2\2ON\3\2\2\2PS\3\2\2\2Q"+
		"O\3\2\2\2QR\3\2\2\2R\r\3\2\2\2SQ\3\2\2\2TU\7\21\2\2UV\7\f\2\2VW\7\3\2"+
		"\2W[\7\4\2\2XZ\7\32\2\2YX\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\\17"+
		"\3\2\2\2][\3\2\2\2^_\7\20\2\2_`\7\f\2\2`a\7\3\2\2ab\7\4\2\2bf\7\30\2\2"+
		"ce\7\32\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\21\3\2\2\2hf\3\2"+
		"\2\2il\5\24\13\2jl\5\26\f\2ki\3\2\2\2kj\3\2\2\2l\23\3\2\2\2mn\t\2\2\2"+
		"no\7\f\2\2op\7\3\2\2pq\7\4\2\2qu\7\30\2\2rt\7\32\2\2sr\3\2\2\2tw\3\2\2"+
		"\2us\3\2\2\2uv\3\2\2\2v\25\3\2\2\2wu\3\2\2\2xy\7\24\2\2yz\7\f\2\2z{\7"+
		"\3\2\2{|\7\4\2\2|}\7\n\2\2}\u0081\7\13\2\2~\u0080\7\32\2\2\177~\3\2\2"+
		"\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\27"+
		"\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\7\25\2\2\u0085\u0086\7\26\2\2"+
		"\u0086\u008a\7\27\2\2\u0087\u0089\7\32\2\2\u0088\u0087\3\2\2\2\u0089\u008c"+
		"\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\31\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\21\36%(+\60\67BHQ[fku\u0081\u008a";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}