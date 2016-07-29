import java.util.ArrayList;
import java.util.Random;


public class Tool {
	static Random ran=new Random();
	
	public Tool(){
		;
	}//end of constructor
	
	static public double getSchLen(int[] ss,int[] ms,int n,int[][] dag,double[][] cost,int[][] PRED,double[] st,double[] ft){  //ss��ms�r��An ���B�z���Ӽ�
//		long starttime=System.currentTimeMillis();
		
		ArrayList<Integer>[] p =new ArrayList[n];
		for(int i=0;i<n;i++)p[i]=new ArrayList<Integer>();
		
		double dataArriveTime=0.0,s=0.0;
		for(int i=0;i<ss.length;i++){
			dataArriveTime=0.0;
			s=0.0;
			for(int j=0;j<PRED[ss[i]].length;j++){  
				if(ms[ss[i]]==ms[PRED[ss[i]][j]]){  //���ݳq�T����
					s=ft[PRED[ss[i]][j]];
				}else{
					s=ft[PRED[ss[i]][j]]+dag[PRED[ss[i]][j]][ss[i]];
				}
				if(j==0)dataArriveTime=s;
				if(s>dataArriveTime)dataArriveTime=s;
			}
			if(p[ms[ss[i]]].isEmpty()){
				p[ms[ss[i]]].add(ss[i]);
				st[ss[i]]=dataArriveTime;
				ft[ss[i]]=dataArriveTime+cost[ss[i]][ms[ss[i]]];
				continue;
			}
			for(int j=0;j<p[ms[ss[i]]].size();j++){
				if(ft[(int)p[ms[ss[i]]].get(j)]<dataArriveTime)continue;   //����٨S��F
				if(dataArriveTime+cost[ss[i]][ms[ss[i]]]<=st[(int)p[ms[ss[i]]].get(j)]){  //�i���J
					p[ms[ss[i]]].add(j, ss[i]);
					st[ss[i]]=dataArriveTime;
					ft[ss[i]]=dataArriveTime+cost[ss[i]][ms[ss[i]]];
					break;
				}
				if(dataArriveTime<ft[(int)p[ms[ss[i]]].get(j)])dataArriveTime=ft[(int)p[ms[ss[i]]].get(j)];
			}
			if(p[ms[ss[i]]].indexOf(ss[i])==-1){
				p[ms[ss[i]]].add(ss[i]);
				st[ss[i]]=dataArriveTime;
				ft[ss[i]]=dataArriveTime+cost[ss[i]][ms[ss[i]]];
			}			
		}
//		System.out.println("ss:");
//		for(int i=0;i<ss.length;i++){
//			System.out.printf("%d ", ss[i]);
//		}
//		System.out.println("\n"+"ms:");
//		for(int i=0;i<ms.length;i++){
//			System.out.printf("%d ", ms[i]);
//		}
//		System.out.println("\nQ:"+n);
//		System.out.println("dag:");
//		for(int i=0;i<dag.length;i++){
//			for(int j=0;j<dag[i].length;j++){
//				System.out.printf("%d ",dag[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println("cost:");
//		for(int i=0;i<cost.length;i++){
//			for(int j=0;j<cost[i].length;j++){
//				System.out.printf("%.1f ",cost[i][j]);
//			}
//			System.out.println();
//		}
//		System.out.println("PRED:");
//		for(int i=0;i<PRED.length;i++){
//			for(int j=0;j<PRED[i].length;j++){
//				System.out.printf("%d ",PRED[i][j]);
//			}
//			System.out.println();
//		}
		
		
		
//		System.out.println("Using time: "+Long.toString(System.currentTimeMillis()-starttime));
		
		
//				System.out.println("\nP0:");
//				for(int i=0;i<p[0].size();i++)System.out.printf("%d ",p[0].get(i));
//				System.out.println("\nP1:");
//				for(int i=0;i<p[1].size();i++)System.out.printf("%d ",p[1].get(i));
//				System.out.println("\nP2:");
//				for(int i=0;i<p[2].size();i++)System.out.printf("%d ",p[2].get(i));
//				System.out.println("\nP3:");
//				for(int i=0;i<p[3].size();i++)System.out.printf("%d ",p[3].get(i));
		return ft[ss.length-1];		
	}//end of getSchLen()
	static int select(double[] theArray){    //�ǤJ�@double[]�A�^�ǽ��Lselect�쪺���ޭ�
		
		double sum=0.0;
		for(int i=0;i<theArray.length;i++)sum+=theArray[i];
		double[] p=new double[theArray.length];
		for(int i=0;i<p.length;i++)p[i]=theArray[i]/sum;
		double r=ran.nextDouble();
		for(int i=0;i<p.length;i++){
			r-=p[i];
			if(r<=0)return i;
		}
		return 0;
	}//end of  select()
	static boolean isOK(int[] ss,int[][] PRED){   //����ss�r��O�_�X�k(�C���u�@�������b�����̤���)�A���ˬd�ߤ@��
		for(int i=0;i<ss.length;i++){
			for(int j=0;j<PRED[ss[i]].length;j++){   //ss[i]���C�@�ӥ���̳��n�Ass[i]�e��
				for(int k=0;k<ss.length;k++){
					if(PRED[ss[i]][j]==ss[k]&&k>i)return false;					
				}		
			}
		}
		return true;
	}//end of isOK()
	
	
}//end of class Tool
