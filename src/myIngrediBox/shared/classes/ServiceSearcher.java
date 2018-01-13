package myIngrediBox.shared.classes;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ServiceSearcher
{
	private static AID serviceProviderAgent = null;  

	public static AID searchForService(Agent a, String serviceType) {
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		template.addServices(sd);
		try
		{ 
			DFAgentDescription[] dfds = DFService.search(a, template);

			if (dfds.length > 0)
			{
				serviceProviderAgent = dfds[0].getName();
				System.out.println("Following " + serviceType + "-Provider found: " + serviceProviderAgent);
			}
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
			System.out.println("No " + "-Provider found.");
		}
		
		return serviceProviderAgent;
	}
}
