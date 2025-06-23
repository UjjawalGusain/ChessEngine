package runner;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class App {

	public static void main(String[] args) throws IOException {
//		for(int i = 1; i<=7; i++) {
//			Instant start = Instant.now();
//			Game gameI = new Game(0);
//			gameI.startAutomaticGame(false, i);
//			
//			Instant finish = Instant.now();
//			Duration timeElapsed = Duration.between(start, finish);
//			System.out.printf("Time elapsed: %d seconds, or %d milliseconds\n", timeElapsed.toSeconds(), timeElapsed.toMillis());
//		}
		
//		Game gameI = new Game(0);
//		gameI.startAutomaticGame(false, 3);
		Game game = new Game(0);
		game.startGame();
	}

}
