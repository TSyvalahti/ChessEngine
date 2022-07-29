package datastructureproject;

import chess.bot.ChessBot;
import chess.bot.TestBot;
import datastructureproject.TMin;

public class test {
    public static void main(String[] args) {
        ChessBot bot = new TMin();
        bot.nextMove(gamestate);
    }
}

