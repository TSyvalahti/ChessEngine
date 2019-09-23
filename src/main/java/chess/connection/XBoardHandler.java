package chess.connection;
import java.util.Scanner;
import chess.TestBot;
import chess.model.GameState;


public class XBoardHandler {
    private Scanner scanner;
    private TestBot bot;
    private GameState gamestate;

    public XBoardHandler(TestBot bot) {
        this.bot = bot;
        scanner = new Scanner(System.in);
        while (true){
            if (scanner.nextLine().startsWith("protover")){
                System.out.append("feature sigint=0 sigterm=0 reuse=0 myname=\"tiraengine\" done=1\n");
                //set reuse=1 if bot is able to play multiple games in a session
                System.out.flush();
                break;
            }
        }
        this.run();
    }

    public void run() {
        this.gamestate = new GameState();
        while (true) {
            String command = scanner.nextLine();
            switch (command.split(" ")[0]){
                case "new":
                //tells the engine the match has started and it plays as black, may not require implementation at all.
                break;
                
                case "go": 
                //tells the engine to start playing as white
                System.out.append("move " + nextMove() + "\n");
                System.out.flush(); 
                break;

                default:
                handleMove(command.split(" ")[0]);
                System.out.append("move " + nextMove() + "\n");
                System.out.flush();
                break;
            }
        }
    }

    public void handleMove(String move){
        this.gamestate.moves.add(move);
        gamestate.parseLatestMove();
    }

    public String nextMove(){
        String move = bot.nextMove(gamestate);
        gamestate.moves.add(move);
        gamestate.parseLatestMove();
        return move;
    }
}