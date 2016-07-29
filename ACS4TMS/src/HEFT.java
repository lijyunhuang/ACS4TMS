import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class HEFT {
	static int V;  //�u�@��
	static int Q;  //�B�z����
	static int E;  //���V��Ӽ�
	
	static int[][] dag;
	static double[][] cost;  //�U�u�@�b�U�B�z�椸�W���p�⦨��
	static double[][] rate;  //�ӳB�z���������ǿ�t�v
	
	static double[] meanCost;
	static int[][] PRED;  //�U�u�@���ߧY�����
	static int[][] SUCC;  //�U�u�@���ߧY���~��
	static double[] ranku;  //�U�u�@�� upward rank
	
	static int[] ss;  //�Ƶ{����
	static double[] st; //�����U�u�@���}�l����ɶ�
	static double[] ft; //�����U�u�@�����槹���ɶ�
	static int[] ms; //�����U�u�@���t�����ӳB�z�椸
	
	static ArrayList<Integer>[] processor;  //�̧Ǭ����U�B�z���Q���t�쪺�u�@�s��
	
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
		}//meanCost[] �ظm����
		
		ranku=new double[V];
		for(int i=V-1;i>=0;i--){
			double max=0.0;
			for(int j=0;j<SUCC[i].length;j++){  //���̤j���~�̪�cost+rank
				if(max<ranku[SUCC[i][j]]+dag[i][SUCC[i][j]]){
					max=ranku[SUCC[i][j]]+dag[i][SUCC[i][j]];
				}
			}
			ranku[i]=meanCost[i]+max;
		}//ranku[] �ظm����
		
		
		//sort by nonincreasing order of ranku value
		
		//�ƻs�U�u�@��ranku��tempArray �ާ@��
		double[] tempArray=new double[V];
		for(int i=0;i<V;i++)tempArray[i]=ranku[i];
		
		ss=new int[V];  //����ƧǪ��u�@�s��
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
		}//ss[]�ظm����
		
		//�Ƶ{
		st=new double[V];
		ft=new double[V];
		ms=new int[V];
		
		double minft=-1;
		double theft=-1;
		int minp=-1;
		
		for(int i=0;i<ss.length;i++){   //���Ƶ{ss[i]
						
			minft=-1;
			theft=-1;
			minp=-1;
			
			//���tss[i]�즳�̤pft��processor
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
			}//���̤pft
			
			ms[ss[i]]=minp;
			ft[ss[i]]=minft;
			st[ss[i]]=minft-cost[ss[i]][minp];
			
		}//�����Ƶ{
		
	}//end of constructor
	double calEFT(int n,int p){  //n �u�@�s���Bp�B�z���s���A�^�� EFT��
		double thest=0.0;  //�Ӥu�@�i�H�}�l�ɶ�
		double s=0.0;
		
		for(int i=0;i<PRED[n].length;i++){
			s=0;
			if(p==ms[PRED[n][i]]){  //�P����̦��P�@�B�z�椸
				s=ft[PRED[n][i]];
			}else{  //���P�B�z�椸�A�[�W�q�T����
				s=ft[PRED[n][i]]+dag[PRED[n][i]][n];
			}
			if(i==0)thest=s;
			else if(thest<s)thest=s;			
			
		}//end of for-loop  
		
		//����thest����ƨ�F�ӳB�z���ɶ� (�H�ɥi�H����)
		
		if(processor[p].size()==0){
			processor[p].add(n);
			return thest+cost[n][p];
		}
		
		for(int i=0;i<processor[p].size();i++){  
			if(ft[(int) processor[p].get(i)]<thest)continue;  //����٨S�쪺���L
			if(thest+cost[n][p]<=st[(int) processor[p].get(i)]){  //�i���J
				processor[p].add(i,n);
				break;
			}
			if(thest<ft[(int) processor[p].get(i)])thest=ft[(int) processor[p].get(i)];
			
		}
		if(processor[p].indexOf(n)==-1)processor[p].add(n);  //�S�a��i���J�A�[�b�̫᭱
		
		return thest+cost[n][p];
	}//end of calEFT()
	
	public void setss(int[] array){
		for(int i=0;i<array.length;i++)array[i]=ss[i];
	}//end of setss()
	
	public void setms(int[] array){
		for(int i=0;i<array.length;i++)array[i]=ms[i];
	}//end of setms()
	
	
}//end of class HEFT
