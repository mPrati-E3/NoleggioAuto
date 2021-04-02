package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	//coda eventi
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();
	
	//paramentri di simulazione----------------------------------------------------
	private int NC = 10;
	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES);
	private final LocalTime oraApertura = LocalTime.of(8, 00);
	private final LocalTime oraChiusura = LocalTime.of(17, 00);
	
	//stato del mondo---------------------------------------------------------------
	private int nAuto;
	
	//variabili da calcolare--------------------------------------------------------
	private int clienti;
	private int insoddisfatti;
	
	//metodi per calcolare le variabili---------------------------------------------
	
	public void setNumCars(int n) {
		this.NC=n;
		
	}


	public void setClientFrequency(Duration of) {
		this.T_IN=of;
		
	}

	//metodi per ritornare le variabili---------------------------------------------
	public int getTotClients() {
		return this.clienti;
	}


	public int getDissatisfied() {
		return this.insoddisfatti;
	}
	
	//metodo di simulazione vero e prorpio------------------------------------------
	public void run() {
		
		//simulazione iniziale (mondo+coda eventi)
		this.nAuto=NC;
		this.clienti=0;
		this.insoddisfatti=0;
		
		this.queue.clear();
		LocalTime oraArrivoCliente = this.oraApertura;
		do {
			Event e = new Event(oraArrivoCliente, EventType.NEW_CLIENT);
			queue.add(e);
			oraArrivoCliente=oraArrivoCliente.plus(T_IN);
			
		} while (oraArrivoCliente.isBefore(this.oraChiusura));
		
		//esecuzione del ciclo di simulazione
		
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}
	
	private void processEvent(Event e) {
		switch (e.getType()) {
		
			case NEW_CLIENT:
				
				if (this.nAuto>0) {
					
					//cliente servito + auto noleggiata
					
					//aggiorna modello del mondo
					this.nAuto--;
					
					//aggiorna risultati
					this.clienti++;
					
					//genera nuovi eventi
					double num = Math.random();
					Duration travel;
					if (num<1.0/3.0) {
						travel = Duration.of(1, ChronoUnit.HOURS);
					} else if (num<2.0/3.0) {
						travel = Duration.of(2, ChronoUnit.HOURS);
					} else {
						travel = Duration.of(3, ChronoUnit.HOURS);
					}
					
					Event nuovo = new Event(e.getTime().plus(travel), EventType.CAR_RETURNED);
					
					this.queue.add(nuovo);
					
				} else {
					//cliente insoddisfatto
					this.clienti++;
					this.insoddisfatti++;
				}
				break;
				
			case CAR_RETURNED:
				
				this.nAuto++;
				
				break;
		}
	}

}
