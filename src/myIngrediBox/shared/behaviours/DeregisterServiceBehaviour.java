package myIngrediBox.shared.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class DeregisterServiceBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private Agent a;

	// use this behaviour in Agent->TakeDown()'s

	public DeregisterServiceBehaviour(Agent a) {
		this.a = a;
	}

	@Override
	public void action() {
		// clean up: de-register all services from DF
		try {
			DFService.deregister(a);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void reset() {
		super.reset();
		this.a = null;
	}

}
