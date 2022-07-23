/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.engine;

import java.util.ArrayList;
import java.util.Arrays;
import chess.model.Side;
import java.util.stream.Collectors;

import org.json.JSONObject;

/**
 * Stores the state of a game of chess
 * Provides access to currently available moves
 */
public class GameState {
    
    /**
     * Current lichess game id if this gamestate is for lichess game
     */
    public String id;
    
    /**
     * Player name for white side if this is lichess game
     */
    public String playingBlack;
    /**
     * Player name for black side if this is lichess game
     */
    public String playingWhite;
    
    /**
     * The side that the bot, which has this gamestate object, plays (used for both lichess and xboard games)
     */
    public Side playing;
    /**
     * The side that currently has the turn (used for both lichess and xboard games)
     */
    public Side turn = Side.WHITE;
    
    /**
     * Remaining time for white player
     */
    public long whiteTime;

    /**
     * Remaining time for the black player
     */
    public long blackTime;
    
    /**
     * All moves made in this game in, UCI notation
     */
    public ArrayList<String> moves;
    
    public GameState() {
        this.moves = new ArrayList();
    }
    
    /**
     * The amount of moves made in this game
     * @return The amount of moves made in this game
     */
    public int getMoveCount() {
        return moves.size();
    }
    
    /**
     * The amount of current turn number, i.e. when both white and black has made a move it's 1 turn.
     * @return int representing the current turn number of this game
     */
    public int getTurnCount() {
        return 1 + moves.size() / 2;
    }
    
    /**
     * Return the latest move in UCI format
     * @return String representation of the latest move (UCI format)
     */
    public String getLatestMove() {
        return this.moves.get(this.moves.size() - 1);
    }
    
    /**
     * The remaining time for the side that the bot, that has this gamestate, plays. In milliseconds
     * @return remaining time as long, in ms
     */
    public long getRemainingTime() {
        if (playing == Side.WHITE) {
            return this.whiteTime;
        } else {
            return this.blackTime;
        }
    }
    
    /**
     * Time for the opponents side. In milliseconds
     * @return remaining time as long, in ms
     */
    public long getRemainingTimeOpponent() {
        if (playing == Side.WHITE) {
            return this.blackTime;
        } else {
            return this.whiteTime;
        }
    }
    public boolean myTurn() {
        return this.turn == this.playing;
    }
    
    /**
     * Sets time for Player, used by XBoardHandler
     * @param time
     */
    public void setTimePlayer(long time) {
        if (playing == Side.WHITE) {
            this.whiteTime = time;
        } else {
            this.blackTime = time;
        }
    }
    /**
     * Sets time for Opponent, used by XBoardHandler
     * @param time
     */
    public void setTimeOpponent(long time) {
        if (playing == Side.BLACK) {
            this.whiteTime = time;
        } else {
            this.blackTime = time;
        }
    }
    
    /**
     * Sets the current gamestate with moves passed as the parameters.
     * @param moves 0-n moves in UCI format
     */
    public void setMoves(String moves) {
        ArrayList<String> moveList = new ArrayList(Arrays.asList(moves.split(",")));
        this.moves = moveList.stream().map((String string) -> {
            return string.trim().replaceAll("^\\W|\\W$", "");
        }).collect(Collectors
                .toCollection(ArrayList::new));
    }

    // FOLLOWING METHODS ARE USED TO CREATE GAMESTATES FROM JSON
    // THESE METHODS ARE NOT RELEVANT TO CHESS BOT CREATION

    /**
     * Parses a full game state from a gameFull JSON object
     * <b>Note:</b> Use only to gain the initial game state, game state should
     * be updated via updateFromJson()
     *
     * @param json String of JSON data according to https://lichess.org/api#operation/botGameStream
     * @return A full, initial game state
     */
    public static GameState parseFromJson(String json) {
        GameState gameState = new GameState();
        
        JSONObject jsonGameState = new JSONObject(json);
        
        if (jsonGameState.getString("type").equals("gameFull")) {
            gameState.id = jsonGameState.getString("id");
            
            gameState.playingWhite = jsonGameState.getJSONObject("white").optString("id");
            gameState.playingBlack = jsonGameState.getJSONObject("black").optString("id");
            
            String[] moves = jsonGameState.getJSONObject("state").getString("moves").split(" ");
            
            gameState.moves = new ArrayList<>(Arrays.asList(moves));
            
            gameState.whiteTime = jsonGameState.getJSONObject("state").getInt("wtime");
            gameState.blackTime = jsonGameState.getJSONObject("state").getInt("btime");
        }
        
        return gameState;
    }

    /**
     * Update a GameState object from JSON
     *
     * @param json String of JSON data according to https://lichess.org/api#operation/botGameStream
     */
    public void updateFromJson(String json) {
        JSONObject jsonGameState = new JSONObject(json);
        
        if (jsonGameState.getString("type").equals("gameFull")) {
            this.id = jsonGameState.getString("id");
            
            this.playingWhite = jsonGameState.getJSONObject("white").optString("id");
            this.playingBlack = jsonGameState.getJSONObject("black").optString("id");
            
            String[] moves = new String[0];          
            if (!jsonGameState.getJSONObject("state").getString("moves").isEmpty()) {
                moves = jsonGameState.getJSONObject("state").getString("moves").trim().split(" ");
            } 
            
            this.moves = new ArrayList<>(Arrays.asList(moves));
            
            for (String i : this.moves) {
                System.out.println(i);
            }
            
            this.whiteTime = jsonGameState.getJSONObject("state").getInt("wtime");
            this.blackTime = jsonGameState.getJSONObject("state").getInt("btime");
        } else if (jsonGameState.getString("type").equals("gameState")) {
            String[] moves = jsonGameState.getString("moves").split(" ");
            
            this.moves = new ArrayList<>(Arrays.asList(moves));
            
            this.whiteTime = jsonGameState.getInt("wtime");
            this.blackTime = jsonGameState.getInt("btime");
        }     
    }
}
