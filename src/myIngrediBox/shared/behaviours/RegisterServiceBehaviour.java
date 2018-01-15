package myIngrediBox.shared.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterServiceBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private String serviceType;
	private Agent a;

	public RegisterServiceBehaviour(Agent a, String serviceType) {
		this.a = a;
		this.serviceType = serviceType;
	}

	@Override
	public void action()

	{

		// Register a service with DF
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(a.getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		sd.setName(a.getLocalName() + "-" + serviceType);

		// add Ontologies, Languages, Interaction Protocols here
		// sd.addOntologies( this.ontology.getName() );

		dfd.addServices(sd);

		try {
			DFService.register(a, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void reset() {
		super.reset();
		this.a = null;
		this.serviceType = null;
	}

}
