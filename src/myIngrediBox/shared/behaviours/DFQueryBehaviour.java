package myIngrediBox.shared.behaviours;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

//TODO	Use SimpleBehaviour
public class DFQueryBehaviour extends SequentialBehaviour {
	private static final long serialVersionUID = 1L;
	private String serviceType;
	private Agent a;

	public DFQueryBehaviour(Agent a, String serviceType) {
		this.a = a;
		this.serviceType = serviceType;
	}

	public void onStart() {
		SimpleBehaviour sb = new SimpleBehaviour() {

			public void action() {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType(serviceType);
				template.addServices(sd);

				try {
					// TODO make possible: more than one Agent result
					DFAgentDescription[] dfds = DFService.search(a, template);
					if (dfds.length > 0) {
						this.getDataStore().put(serviceType, dfds[0].getName());

						System.out.println("\n" + serviceType + "Agent found: " + this.getDataStore().get(serviceType));
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

		}; // SimpleBehaviour anonym class

		// use same Datastore for Subbehaviour-Communication
		sb.setDataStore(this.getDataStore());
		this.addSubBehaviour(sb);

	}

	public void reset() {
		super.reset();
		this.a = null;
		this.serviceType = null;
	}

}