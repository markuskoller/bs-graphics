package ch.blackspirit.graphics;
import java.util.HashMap;


public class AccessTest implements Access {
	static int width = 1000;
	static int height = 1000;
	static int[] imagei = new int[width * height];
	static byte[] imageb = new byte[width * height * 4];
	
	
	public static void main(String[] args) {
		int count = width*height;
		for(int i = 0; i < count; i++);

		System.out.println("byte");
		int a = 0;
		long time = System.nanoTime();
		int x = 0;
		for(int i = 0; i < count; i++) {
			// colour index calculation (pixel * components + colour)
			x = i * 4 + 2;
			a = imageb[x];
			if(a < 0) a += 255;
		}
		System.out.println(System.nanoTime() - time);
		
		//---------------------------------------------------------------------------------------------------------
		//---------------------------------------------------------------------------------------------------------
		
		System.out.println("int");
		// fastest, because indexes for each colour value don't have to be calculated
		time = System.nanoTime();
		for(int i = 0; i < count; i++) {
			a = (imagei[i] & 0x00ff0000) >> 16;
		}
		System.out.println(System.nanoTime() - time);
		
		//---------------------------------------------------------------------------------------------------------
		//---------------------------------------------------------------------------------------------------------

		System.out.println("method byte");
		Access t = new AccessTest();
		
		time = System.nanoTime();
		for(int i = 0; i < count; i++) {
			a = t.access(i);
		}
		System.out.println(System.nanoTime() - time);

		//---------------------------------------------------------------------------------------------------------
		//---------------------------------------------------------------------------------------------------------

		System.out.println("method int");
		time = System.nanoTime();
		for(int i = 0; i < count; i++) {
			a = t.accessi(i);
		}
		System.out.println(System.nanoTime() - time);
		
		//---------------------------------------------------------------------------------------------------------
		//---------------------------------------------------------------------------------------------------------

		System.out.println("cast");
		Object o = imagei;
		int[] ti;
		time = System.nanoTime();
		for(int i = 0; i < count; i++) {
			if(o instanceof int[]) {
				ti = (int[])o;
			} else {
				
			}
//			System.out.println(a);
		}
		System.out.println(System.nanoTime() - time);

		//---------------------------------------------------------------------------------------------------------
		//---------------------------------------------------------------------------------------------------------

		System.out.println("hash lookup");
		HashMap<Integer, int[]> bla = new HashMap<Integer, int[]>();
		Integer blakey = new Integer(102345);
		bla.put(blakey, imagei);
		time = System.nanoTime();
		for(int i = 0; i < count; i++) {
			ti = bla.get(blakey);
			if(ti == null) {
				
			}
//			System.out.println(a);
		}
		System.out.println(System.nanoTime() - time);
	}
	
	
	/* (non-Javadoc)
	 * @see Acess#accessi(int)
	 */
	public int accessi(int i) {
		return (imagei[i] & 0x00ff0000) >> 16;
	}
	
	int x = 0;
	/* (non-Javadoc)
	 * @see Acess#access(int)
	 */
	public int access(int i) {
		int value = imageb[x];
		x = i * 4 + 2;
		if(value < 0) return value + 255;
		else return value;
	}
}
