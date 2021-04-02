package polito.it.noleggio.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TestSimulator {

	public static void main(String args[]) {
		Simulator sim = new Simulator();
		
		sim.setNumCars(10) ;
		sim.setClientFrequency(Duration.of(10, ChronoUnit.MINUTES)) ;
		
		sim.run() ;
		
		int totClients = sim.getTotClients() ;
		int dissatisfied = sim.getDissatisfied() ;
		
		System.out.format("Arrived %d clients, %d were dissatisfied\n", 
				totClients, dissatisfied);
	}
	
}
