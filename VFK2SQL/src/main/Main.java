package main;

import api.Api;
import gui.Gui;
/**
 * Hlavni trida, urcena pro spusteni programu ve dvou variantach GUI i API.
 * @author Jan Malek
 *
 */
public class Main {
	public static void main(String[] args) {
		if (args.length > 0) Api.input(args);
		else new Gui();
	}
}
