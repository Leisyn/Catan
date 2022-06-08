package catan;

import java.util.Scanner;

import catan.model.Game;
import catan.model.player.Human;
import catan.model.player.Robot;
import catan.view.View;

import java.util.LinkedList;

public class Launcher {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String answer = null;
		
		while (answer == null) {
			System.out.println("Do you wish to play with the graphical version or the textual one? [G/T]");
			answer = sc.next().toUpperCase();
			if (!answer.equals("G") && !answer.equals("T")) {
				System.out.println("Wrong.\n");
				answer = null;
			}
		}
		
		if (answer.equals("G")) {
			new View(sc);
		} else {
			while (answer == null) {
				System.out.println("How many players? [3/4]");
				answer = sc.next();
				if (!answer.equals("3") && !answer.equals("4")) {
					System.out.println("Wrong.\n");
					answer = null;
				}
			}
			System.out.println();
			
			int numPlayer = Integer.parseInt(answer);
			Game game = new Game(numPlayer, sc);
			answer = null;
			
			while (answer == null) {
				System.out.println("How many AI? " + "[" + numPlayer + " maximum]");
				answer = sc.next();
				if (!answer.matches("\\d*")) {
					System.out.println("Wrong.\n");
					answer = null;
				} else if (Integer.parseInt(answer) < 0 || Integer.parseInt(answer) > numPlayer) {
					System.out.println("Wrong.\n");
					answer = null;
				}
			}
			System.out.println();
			int numAI = Integer.parseInt(answer);
			LinkedList<Human> players = new LinkedList<>();
			int numHuman = numPlayer - numAI;
				
			for (int i = 1; i <= numHuman; i++) {
				System.out.println("Player " + i + ": What will be your name?");
				players.add(new Human(sc.next(), game));
				System.out.println();
			}
			
			for (int i = numHuman + 1; i <= numPlayer; i++)
				players.add(new Robot("IA " + i, game));
	
			game.iniPlayers(players);
			game.game();
		}
		sc.close();
	}
}
