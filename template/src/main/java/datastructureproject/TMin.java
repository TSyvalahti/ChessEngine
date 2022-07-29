package datastructureproject;

import chess.bot.ChessBot;
import chess.engine.GameState;
    
public class TMin implements ChessBot {
    private Board b;

    public TMin() {
        this.b = new Board();
    }
    
    /**
     * @param gs Current gamestate
     * @return UCI String representation of a move
     */
    @Override
    public String nextMove(GameState gs) {
        return null;
    }

    public Board getBoard() {
        return this.b;
    }
}
