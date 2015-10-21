package lukasz.controller;

/**
 * @author Lukasz
 */
public class Main {

	public static void main(String[] args) { 
		
		final Pololu pololu = Pololu.getInstance();
		pololu.connect("COM4", new Interaction() {

			@Override
			public void loop() throws InterruptedException {
				pololu.blink(0, 500);
			}
		});
	}
}
