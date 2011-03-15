package br.usp.ime.booktrip.peer;


import org.openk.core.OKC.AcceptPolicy;
import org.openk.peer.InteractionTask;
import org.openk.peer.Peer;



public class TravelAgency extends Peer
{
	public void init(String[] args)
	{
		super.init(args);
		
		// Create and add the OKCs this peer has
		manager.storeComponent(createOKC("travelAgency", "br.usp.ime.booktrip.okc.TravelAgencyOKC", null));
		
		// Get the IM from the DS (and then subscribe to the various interactions)
		InteractionTask task = new InteractionTask(this, "travelAgency", AcceptPolicy.ACCEPT_ONE, "booktrip", false);
		String[] q = { "booktrip" };
		task.attempt(q);
	}
	


	public static void main(String args[])
	{
		TravelAgency peer = new TravelAgency();
		peer.init(args);
	}
}
