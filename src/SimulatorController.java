import java.util.Random;

public class SimulatorController {
	private static final int MAX_NUMBER = 100000;
	private static final int WIN_MAX = 68764;
	private static final double WIN_RETURN_MULTIPLIER = 0.4;
	private static final Random RNG = new Random();
	
	private int rollResult = 0;
	private int wager;
	private int profit = 0;
	
	public SimulatorController (int rollResult, int wager, int profit) {
		this.rollResult = rollResult;
		this.wager = wager;
		this.profit = profit;
	}
	
	public int getRollResult () {
		return rollResult;
	}
	
	public void setRollResult (int rollResult) {
		this.rollResult = rollResult;
	}
	
	public int getWager () {
		return wager;
	}
	
	public void setWager (int wager) {
		this.wager = wager;
	}
	
	public int getProfit () {
		return profit;
	}
	
	public void setProfit (int profit) {
		this.profit = profit;
	}
	
	public boolean rollAndReturnResult(){
		this.setRollResult(RNG.nextInt(MAX_NUMBER));
		if (this.getRollResult() <= WIN_MAX){
			setProfit(Double.valueOf(Double.valueOf(wager).doubleValue() * WIN_RETURN_MULTIPLIER).intValue());
			return true;
		} else{
			setProfit(wager * -1);
			return false;
		}
	}
}
