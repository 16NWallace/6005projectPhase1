// Generated from Board.g4 by ANTLR 4.0

package pingballClient.parser;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface BoardListener extends ParseTreeListener {
	void enterAbsorber(BoardParser.AbsorberContext ctx);
	void exitAbsorber(BoardParser.AbsorberContext ctx);

	void enterBall(BoardParser.BallContext ctx);
	void exitBall(BoardParser.BallContext ctx);

	void enterTriangle(BoardParser.TriangleContext ctx);
	void exitTriangle(BoardParser.TriangleContext ctx);

	void enterFile(BoardParser.FileContext ctx);
	void exitFile(BoardParser.FileContext ctx);

	void enterAttribute(BoardParser.AttributeContext ctx);
	void exitAttribute(BoardParser.AttributeContext ctx);

	void enterFlipper(BoardParser.FlipperContext ctx);
	void exitFlipper(BoardParser.FlipperContext ctx);

	void enterCircle(BoardParser.CircleContext ctx);
	void exitCircle(BoardParser.CircleContext ctx);

	void enterSquare(BoardParser.SquareContext ctx);
	void exitSquare(BoardParser.SquareContext ctx);

	void enterBumper(BoardParser.BumperContext ctx);
	void exitBumper(BoardParser.BumperContext ctx);

	void enterBoard(BoardParser.BoardContext ctx);
	void exitBoard(BoardParser.BoardContext ctx);

	void enterFire(BoardParser.FireContext ctx);
	void exitFire(BoardParser.FireContext ctx);

	void enterActions(BoardParser.ActionsContext ctx);
	void exitActions(BoardParser.ActionsContext ctx);
}