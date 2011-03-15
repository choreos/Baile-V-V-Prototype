package br.usp.ime.booktrip.peer;



import org.openk.peer.Peer;



public class PeerPublisher extends Peer
{
	public void init(String[] args)
	{
		super.init(args);
		
		// Publish the IM
		publishIM("lcc/booktrip.lcc", "booktrip");
	}
	


	public static void main(String args[])
	{
		PeerPublisher peer = new PeerPublisher();
		peer.init(args);
	}
}
