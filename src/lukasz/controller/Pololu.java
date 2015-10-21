package lukasz.controller;
public class Pololu {

	private static Pololu instance = null;
	private MaestroController controller;
	public static boolean isAlive = false;
	public static final int SEKUNDY = 5;
	public static final int MIN_POS = 600;
	public static final int MAX_POS = 2000;

	/**
	 * 
	 * @param port
	 *            - Windows: "COM4", Linux: "ttyACM0"
	 */
	public static Pololu getInstance() {
		if (instance == null) {
			instance = new Pololu();
		}
		return instance;
	}

	/**
	 * @param portName
	 *            - Windows: "COM4", Linux: "ttyACM0"
	 * @param interaction
	 *            - overload method loop() of this interface, and use as you
	 *            want
	 */
	public void connect(String portName, Interaction interaction) {
		if (interaction == null)
			throw new NullPointerException("Interaction cannot be null.");
		try {
			controller = new MaestroController(portName, true);
			isAlive = true;
			Thread.sleep(1000);
			System.out.println("Pololu initialized.");
			while (isAlive) {
				// TODO:
				interaction.loop();
			}
			controller.goHome();
			controller.close();
		} catch (Exception e) {
			e.printStackTrace();
			isAlive = false;
		}
	}

	/**
	 * @param channel
	 *            - 0,1..23
	 * @param position
	 *            - from MIN_POS to MAX_POS
	 */
	public void moveChannel(int channel, int position) {
		if (position > MAX_POS || position < MIN_POS) {
			throw new IllegalStateException("Position out of range.");
		}
		if (controller == null)
			throw new NullPointerException("Controller cannot be null.");
		controller.setPosition(channel, position);
	}

	/**
	 * @param channel
	 *            - 0,1..23
	 * @param position
	 *            - from MIN_POS to MAX_POS
	 * @param speed
	 *            - speed size (0 is as fast as possible)
	 * @param acc
	 *            - acceleration size (0 is as fast as possible)
	 */
	public void moveChannel(int channel, int position, int speed, int acc) {
		if (position > MAX_POS || position < MIN_POS) {
			throw new IllegalStateException("Position out of range.");
		}
		if (controller == null)
			throw new NullPointerException("Controller cannot be null.");
		controller.setPosition(channel, position, speed, acc);
	}

	/**
	 * @param channel
	 *            - which channel should be moved
	 * @param delayTimeMillis
	 *            - time between 2 selected positions
	 * @throws InterruptedException
	 *             - exception during controller's work
	 */
	public void blink(int channel, int delayTimeMillis)
			throws InterruptedException {
		controller.setPositionFastest(channel, MIN_POS);
		Thread.sleep(delayTimeMillis);
		controller.setPositionFastest(channel, MAX_POS);
		Thread.sleep(delayTimeMillis);
	}

	public void blinkBetween(int channel, int delayTimeMillis, int minPos,
			int maxPos) throws InterruptedException {
		if (minPos < MIN_POS || MAX_POS < maxPos)
			throw new IllegalStateException("Position out of range.");
		controller.setPositionFastest(channel, minPos);
		Thread.sleep(delayTimeMillis);
		controller.setPositionFastest(channel, maxPos);
		Thread.sleep(delayTimeMillis);
	}
}
