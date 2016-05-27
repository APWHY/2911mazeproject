/**
 *  This class is responsible for storing the game timer values for bonus, total and current times. It also handles a toString conversion from seconds to HH:MM:SS format
 *
 */
public class TimerDisplay {
	//Class constants
		private final int BONUSTIME = 40;
		private final int EXTRATIME = 30;
	
	//Class variables
		private int seconds = 600;
		private int total = 600;
		private int bonus = 40;

	
	/**
	 * Constructor.
	 * 
	 * @param startTime Initial Game time
	 */
	public TimerDisplay(int startTime){
		seconds = startTime;
		bonus = BONUSTIME;
		total = seconds;
	}
	/**
	 * Increment the seconds value
	 */
	public void incrementSecond() {
		seconds--;
		if(bonus > 0){
			bonus--;
		}
	}
	
	/**
	 * Add time to current, total and bonus time values. 
	 */
	public void addTime(){
		seconds = seconds + EXTRATIME + bonus;
		total = total + EXTRATIME + bonus;
		bonus = BONUSTIME;
	}
	
	/**
	 * Get the seconds field.
	 * 
	 * @return int seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Get the time in hh:mm:ss format.
	 * 
	 * @return time String
	 */
	public String getTotal() {
		return hourMinSecFormat(total);
	}
	
	/**
	 * Get time.
	 * 
	 * @return time String
	 */
	public String getTime(){
		return hourMinSecFormat(this.seconds);
	}
	
	/**
	 * Get the bonus time.
	 * 
	 * @return time String
	 */
	public String getBonus(){
		return hourMinSecFormat(this.bonus);
	}
	
	/**
	 * Format the time into HH:MM:SS. 
	 * 
	 * @param seconds time as int
	 * @return time String
	 */
	private String hourMinSecFormat(int seconds) {
		//Code from: http://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds
		//By: Bigtoes
		int hours   = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
