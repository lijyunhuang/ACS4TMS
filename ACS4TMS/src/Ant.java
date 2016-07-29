import java.util.ArrayList;


public class Ant {

	double SchLen;
	int[] ss;
	int[] ms;
	double[] st;
	double[] ft;
	int[] done;
	ArrayList<Integer> candidate;  //匡
	
	static int[] bestss=null;  //办程ㄎ
	static int[] bestms=null;
	static double bestLen=-1;
	static double[] bestst=null;
	static double[] bestft=null;
	
	static int leaderID=-1;//讽程ㄎID
	static double leaderLen=-1;
	
	public Ant(int V){   // V计
		
		ss=new int[V];
		ms=new int[V];
		st=new double[V];
		ft=new double[V];
		done=new int[V];  //魁琌ЧΘЧΘ1
		candidate=new ArrayList<Integer>();
		
		if(bestss==null)bestss=new int[V];
		if(bestms==null)bestms=new int[V];
		if(bestst==null)bestst=new double[V];
		if(bestft==null)bestft=new double[V];
		
	}//end of constructor
	
	
}//end of class Ant
