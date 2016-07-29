import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class HEFT {
	static int V;  //工作數
	static int Q;  //處理器數
	static int E;  //有向邊個數
	
	static int[][] dag;
	static double[][] cost;  //各工作在各處理單元上的計算成本
	static double[][] rate;  //個處理器之間的傳輸速率
	
	static double[] meanCost;
	static int[][] PRED;  //各工作的立即先行者
	static int[][] SUCC;  //各工作的立即後繼者
	static double[] ranku;  //各工作的 upward rank
	
	static int[] ss;  //排程順序
	static double[] st; //紀錄各工作的開始執行時間
	static double[] ft; //紀錄各工作的執行完成時間
	static int[] ms; //紀錄各工作分配給哪個處理單元
	
	static ArrayList<Integer>[] processor;  //依序紀錄各處理器被分配到的工作編號
	
	public HEFT(int[][] dag,double[][] cost,int[][] PRED,int[][] SUCC,int Q,int V,int E) throws Exception{
		this.dag=dag;
		this.cost=cost;
		this.PRED=PRED;
		this.SUCC=SUCC;
		this.Q=Q;
		this.V=V;
		this.E=E;
		
		processor=new ArrayList[Q];
		for(int i=0;i<Q;i++)processor[i]=new ArrayList<Integer>();
	
		meanCost=new double[V];
		double sum=0.0;
		for(int i=0;i<cost.length;i++){
			sum=0.0;
			for(int j=0;j<cost[i].length;j++){
				sum+=cost[i][j];
			}
			meanCost[i]=sum/cost[i].length;
		}//meanCost[] 建置完成
		
		ranku=new double[V];
		for(int i=V-1;i>=0;i--){
			double max=0.0;
			for(int j=0;j<SUCC[i].length;j++){  //取最大後繼者的cost+rank
				if(max<ranku[SUCC[i][j]]+dag[i][SUCC[i][j]]){
					max=ranku[SUCC[i][j]]+dag[i][SUCC[i][j]];
				}
			}
			ranku[i]=meanCost[i]+max;
		}//ranku[] 建置完成
		
		
		//sort by nonincreasing order of ranku value
		
		//複製各工作的ranku給tempArray 操作用
		double[] tempArray=new double[V];
		for(int i=0;i<V;i++)tempArray[i]=ranku[i];
		
		ss=new int[V];  //遞減排序的工作編號
		for(int i=0;i<V;i++)ss[i]=i;
		
		for(int i=0;i<V;i++){
			int ti=0;
			double td=0.0;
			for(int j=i;j<V;j++){
				if(tempArray[i]<tempArray[j]){
					td=tempArray[i];
					tempArray[i]=tempArray[j];
					tempArray[j]=td;
					ti=ss[i];
					ss[i]=ss[j];
					ss[j]=ti;
				}
			}
		}//ss[]建置完畢
		
		//排程
		st=new double[V];
		ft=new double[V];
		ms=new int[V];
		
		double minft=-1;
		double theft=-1;
		int minp=-1;
		
		for(int i=0;i<ss.length;i++){   //對於排程ss[i]
						
			minft=-1;
			theft=-1;
			minp=-1;
			
			//分配ss[i]到有最小ft的processor
			for(int j=0;j<Q;j++){
				theft=calEFT(ss[i],j);
				if(j==0){
					minft=theft;
					minp=j;
					continue;
				}
				if(minft>theft){
					processor[minp].remove(processor[minp].indexOf(ss[i]));
					minft=theft;
					minp=j;					
				}else{
					processor[j].remove(processor[j].indexOf(ss[i]));
				}
			}//找到最小ft
			
			ms[ss[i]]=minp;
			ft[ss[i]]=minft;
			st[ss[i]]=minft-cost[ss[i]][minp];
			
		}//結束排程
		
	}//end of constructor
	double calEFT(int n,int p){  //n 工作編號、p處理器編號，回傳 EFT值
		double thest=0.0;  //該工作可以開始時間
		double s=0.0;
		
		for(int i=0;i<PRED[n].length;i++){
			s=0;
			if(p==ms[PRED[n][i]]){  //與先行者位於同一處理單元
				s=ft[PRED[n][i]];
			}else{  //不同處理單元，加上通訊成本
				s=ft[PRED[n][i]]+dag[PRED[n][i]][n];
			}
			if(i==0)thest=s;
			else if(thest<s)thest=s;			
			
		}//end of for-loop  
		
		//此時thest為資料到達該處理器時間 (隨時可以執行)
		
		if(processor[p].size()==0){
			processor[p].add(n);
			return thest+cost[n][p];
		}
		
		for(int i=0;i<processor[p].size();i++){  
			if(ft[(int) processor[p].get(i)]<thest)continue;  //資料還沒到的跳過
			if(thest+cost[n][p]<=st[(int) processor[p].get(i)]){  //可插入
				processor[p].add(i,n);
				break;
			}
			if(thest<ft[(int) processor[p].get(i)])thest=ft[(int) processor[p].get(i)];
			
		}
		if(processor[p].indexOf(n)==-1)processor[p].add(n);  //沒地方可插入，加在最後面
		
		return thest+cost[n][p];
	}//end of calEFT()
	
	public void setss(int[] array){
		for(int i=0;i<array.length;i++)array[i]=ss[i];
	}//end of setss()
	
	public void setms(int[] array){
		for(int i=0;i<array.length;i++)array[i]=ms[i];
	}//end of setms()
	
	
}//end of class HEFT
