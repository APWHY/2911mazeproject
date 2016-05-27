public class TimerDisplay {
	private int seconds = 600;
	private int total = 600;
	private int bonus = 40;
	private final int BONUSTIME = 40;
	private final int EXTRATIME = 30;
	public TimerDisplay(int startTime){
		seconds = startTime;
		bonus = BONUSTIME;
		total = seconds;
	}
	
	public int getSeconds() {
		return seconds;
	}

	public String getTotal() {
		return hourMinSecFormat(total);
	}

	public void incrementSecond() {
		seconds--;
		if(bonus > 0){
			bonus--;
		}
	}
	
	public void addTime(){
		seconds = seconds + EXTRATIME + bonus;
		total = total + EXTRATIME + bonus;
		bonus = BONUSTIME;
	}
	
	public String getTime(){
		return hourMinSecFormat(this.seconds);
	}
	
	public String getBonus(){
		return hourMinSecFormat(this.bonus);
	}
	
	private String hourMinSecFormat(int seconds) {
		//Code from: http://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds
		//By: Bigtoes
		int hours   = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
