public class TimerDisplay {

	private int seconds = 600;
	private int total = 600;
	private int bonus = 40;
	private final int BONUSTIME = 40;
	private final int EXTRATIME = 30;
	
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
	 * Increment the seconds value
	 */
	public void incrementSecond() {
		seconds--;
		if(bonus > 0){
			bonus--;
		}
	}
	
	/**
	 * Add time to current time values. 
	 */
	public void addTime(){
		seconds = seconds + EXTRATIME + bonus;
		total = total + EXTRATIME + bonus;
		bonus = BONUSTIME;
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
	 * Format the time. 
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
