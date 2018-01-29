package myIngrediBox.shared.behaviours;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

//TODO	Use SimpleBehaviour
public class DFQueryBehaviour extends SimpleBehaviour {
	private static final long serialVersionUID = 1L;
	private String serviceType;
	private Agent a;

	public DFQueryBehaviour(Agent a, String serviceType) {
		this.a = a;
		this.serviceType = serviceType;
	}

	public void action() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		template.addServices(sd);

		try {

			DFAgentDescription[] dfds = DFService.search(a, template);

			ArrayList<AID> providers = new ArrayList<AID>();
			for (DFAgentDescription df : dfds) {
				providers.add(df.getName());
				System.out.println("\n" + serviceType + " Agent found: " + df.getName());
			}

			if (providers.size() > 0) {

				this.getDataStore().put(serviceType, providers);

			}

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	@Override
	public boolean done() {
		boolean ret = false;
		if (this.getDataStore().get(serviceType) != null) {
			ret = true;
		}
		return ret;

	}

	public void reset() {
		super.reset();
		this.a = null;
		this.serviceType = null;
	}

}