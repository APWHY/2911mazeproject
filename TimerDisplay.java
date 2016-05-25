public class TimerDisplay {
	private int seconds = 0;
	
	public void incrementSecond() {
		seconds++;
	}
	
	public String getTime(){
		return hourMinSecFormat(this.seconds);
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
