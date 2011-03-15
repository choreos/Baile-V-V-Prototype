

public class TestParallel {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		int count = 4;
		
		Thread[] request1 = new Thread[count];
		
		for (int i = 0; i < count; i++) {
			request1[i] = new Thread(new ParallelRequest("Paris"));
		}
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < count; i++) {
			request1[i].start();
		}
		
		for (int i = 0; i < count; i++) {
				request1[i].join();
		}
		
		
		long end = System.currentTimeMillis();
		
		System.out.println("time: " + (end - start));
	}

}
