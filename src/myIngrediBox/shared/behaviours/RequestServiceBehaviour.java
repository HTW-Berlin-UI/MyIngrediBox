package myIngrediBox.shared.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RequestServiceBehaviour extends OneShotBehaviour
{

	private static final long serialVersionUID = 1L;

	private Agent a;
	private String serviceType;
	//private AID serviceProviderAgent = null;
	private static AID serviceProviderAgent = null; 
	//eventuell über getter und setter

	public RequestServiceBehaviour(Agent a, String serviceType)
	{
		this.a = a;
		this.serviceType =serviceType;
	}

	@Override
	public void action()
	{
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType);
		template.addServices(sd);
		try
		{ 
			DFAgentDescription[] dfds = DFService.search(a, template);

			if (dfds.length > 0)
			{
				setServiceProviderAgent(dfds[0].getName());
				System.out.println("Following " + serviceType + "-Provider found: " + getServiceProviderAgent());
			}
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
			System.out.println("No " + "-Provider found.");
		}
		this.getDataStore().put("ServiceProviderAgent", getServiceProviderAgent());
	}

	public static AID getServiceProviderAgent()
	{
		return serviceProviderAgent;
	}

	public static void setServiceProviderAgent(AID serviceProviderAgent)
	{
		RequestServiceBehaviour.serviceProviderAgent = serviceProviderAgent;
	}
}
