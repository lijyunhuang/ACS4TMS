import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ACS {

	static double[][][][] pheromone;  //�O���X��
	static double[] start2entry;  //�����}�l��u�@�i�J�I���O���X����
	
	static int times=200;  //���N��
	static int m=10; //���Ƽƶq
	
	static double tau0=0.000019;  //�̧C�O���X�@��
	static double Beta=0.07;   //�ਣ���v��
	static double Rho=0.01;    //�O���X�]�o�v
	static double q0=1;     //�ಾ�W�h���Ѽ�
	
	static double Visibility;  //�ਣ��
	
	static int V;  //�u�@��
	static int Q;  //�B�z����
	static int E;  //���V��Ӽ�
	
	static int[][] dag;
	static double[][] cost;  //�U�u�@�b�U�B�z�椸�W���p�⦨��
	static double[][] rate;  //�ӳB�z���������ǿ�t�v
	
	static int[][] PRED;  //�U�u�@���ߧY�����
	static int[][] SUCC;  //�U�u�@���ߧY���~��
	static long starttime;  //�}�l�ɶ�
	
	static String[] filename={"C:\\test\\n4_00.dag","C:\\test\\n4_02.dag","C:\\test\\n4_04.dag","C:\\test\\n4_06.dag"};

	
	public static void main(String[] args) throws IOException{
	
//		String filename="C:\\Users\\rex\\Desktop\\test\\n4_04.dag";
		
		for(int file_index=0;file_index<4;file_index++){
			
			//��Ʒǳ�
			BufferedReader 	br=new BufferedReader(new FileReader(filename[file_index]));
			String temp=null;
			String[] temps=null;
	
			while ((temp=br.readLine())!=null)   //�o������
				if (temp.charAt(0) == '*')
					break;
	
				temp=br.readLine();
				Q=Integer.parseInt(temp);
				temp=br.readLine();
				V=Integer.parseInt(temp);
				temp=br.readLine();
				E=Integer.parseInt(temp);
							
				while((temp=br.readLine())!=null)   //�o������
					if(temp.charAt(0)=='*')break;
				
				
				rate=new double[Q][Q];
				for(int i=0;i<Q;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					for(int j=0;j<temps.length;j++){
						rate[i][j]=Double.parseDouble(temps[j]);
					}
				}//rate[][] �ظm����
			
				while((temp=br.readLine())!=null)   //�o������
					if(temp.charAt(0)=='*')break;
				
				cost=new double[V][Q];
				for(int i=0;i<V;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					for(int j=0;j<temps.length;j++){
						cost[i][j]=Double.parseDouble(temps[j]);				
					}
				}//cost[][] �ظm����
				
				while((temp=br.readLine())!=null)   //�o������
					if(temp.charAt(0)=='*')break;
				
				dag=new int[V][V];
				for(int i=0;i<V;i++)for(int j=0;j<V;j++)dag[i][j]=-1;  //��l�ơA�C����-1
				
				for(int i=0;i<E;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					dag[Integer.parseInt(temps[0])][Integer.parseInt(temps[1])]=Integer.parseInt(temps[2]);
				}//dag[][] �ظm����
			
				PRED=new int[V][];  
				int[] nPRED=new int[V];  //�O���C�Ӥu�@���ߧY����̭Ӽ�
				
				for(int i=0;i<V;i++){
					for(int j=0;j<V;j++){
						if(dag[j][i]<0)continue;
						else nPRED[i]++;
					}
				}// nPRED[]�ظm����
		
				for(int i=0;i<V;i++)	PRED[i]=new int[nPRED[i]];
				
				for(int j=0;j<V;j++){
					int n=0;
					for(int i=0;i<V;i++){
						if(dag[i][j]==-1)continue;
						else{
							PRED[j][n++]=i;
						}
					}
				}//PRED[][] �ظm����
				
				
				SUCC=new int[V][];
				int[] nSUCC=new int[V];  //�����C�Ӥu�@���ߧY���~�̭Ӽ�
				
				for(int i=0;i<V;i++){
					for(int j=0;j<V;j++){
						if(dag[i][j]==-1)continue;
						else nSUCC[i]++;
					}
				}//nSUCC[] �ظm����
				
				for(int i=0;i<V;i++)SUCC[i]=new int[nSUCC[i]];
				for(int i=0;i<V;i++){
					int n=0;
					for(int j=0;j<V;j++){
						if(dag[i][j]==-1)continue;
						else SUCC[i][n++]=j;
					}
				}//SUCC[][]�ظm����
				br.close();
		////***��ƷǳƵ���***////
				
				starttime=System.currentTimeMillis();  //�����}�lACS�ɶ�
				
				double testsum=0.0;
				long timesum=0;
				for(int testtime=0;testtime<5;testtime++){
					Ant.bestft=null;  //inti Ant
					Ant.bestst=null;
					Ant.bestss=null;
					Ant.bestms=null;
					Ant.bestLen=-1;
					Ant.leaderID=-1;
					Ant.leaderLen=-1;
						
					//��l�ƶO���X��
					start2entry=new double[Q];
					for(int i=0;i<Q;i++)start2entry[i]=tau0;
					pheromone=new double[V][Q][V][Q];
					for(int i=0;i<V;i++)
						for(int j=0;j<Q;j++)
							for(int k=0;k<V;k++)
								for(int l=0;l<Q;l++)
									pheromone[i][j][k][l]=tau0;
					//---�O���X���l�Ƶ���---//
					
					//�HHEFT�ұo�Ѩӥ��[�W�O���X
					Ant hAnt =new Ant(V);
					try {
						HEFT h=new HEFT(dag,cost,PRED,SUCC,Q,V,E);
						h.setss(hAnt.ss);
						h.setms(hAnt.ms);
					} catch (Exception e) {
						//nothing
					}
					hAnt.SchLen=Tool.getSchLen(hAnt.ss, hAnt.ms, Q, dag, cost, PRED, hAnt.st, hAnt.ft);
		//			System.out.println("*** "+hAnt.SchLen+" ***");
					if(Ant.bestLen==-1){  //init
						Ant.bestLen=hAnt.SchLen;
						for(int i=0;i<hAnt.ms.length;i++)Ant.bestms[i]=hAnt.ms[i];
						for(int i=0;i<hAnt.ss.length;i++)Ant.bestss[i]=hAnt.ss[i];
						for(int i=0;i<hAnt.st.length;i++)Ant.bestst[i]=hAnt.st[i];
						for(int i=0;i<hAnt.ft.length;i++)Ant.bestft[i]=hAnt.ft[i];
					}
					GlobalUpdate(hAnt);
					//end of HEFT update
					
					
					
					
					for(int t=0;t<times;t++){
						Ant[] ants=new Ant[m];
						for(int i=0;i<m;i++)ants[i]=new Ant(V);
						
						for(int i=0;i<m;i++){   //�C�����ƨ��X�@�ӸѡA�ù飼�L�����@���o
							getaPath(ants[i]);
							if(i==0){  //init
								Ant.leaderLen=ants[i].SchLen;
								Ant.leaderID=i;
							}else if(Ant.leaderLen>ants[i].SchLen){
								Ant.leaderLen=ants[i].SchLen;
								Ant.leaderID=i;
							}
							LocalUpdate(ants[i]);
						}
						GlobalUpdate(ants[Ant.leaderID]);
				}//end of for-times loop
				
				//print the result
	//			System.out.println();
				System.out.println(Ant.bestLen);
	
	//			System.out.println(Ant.bestft[V-1]);
	//			System.out.println(Tool.getSchLen(Ant.bestss, Ant.bestms, V, dag, cost, PRED, new double[V], new double[V]));
	//			for(int i=0;i<Ant.bestss.length;i++)System.out.printf("%2d ",Ant.bestss[i]);
	//			System.out.println();
	//			for(int i=0;i<Ant.bestms.length;i++)System.out.printf("%2d ",Ant.bestms[i]);
	//			System.out.println();
				long usingTime=System.currentTimeMillis()-starttime;
				timesum+=usingTime;
	//			System.out.println("\n"+Ant.bestLen+"  using: "+usingTime);
	//			System.out.println(Tool.isOK(Ant.bestss, PRED));
				
				testsum+=Ant.bestLen;
				}//end of for-testtime
				System.out.println(filename[file_index]+" average= "+testsum/5+" avg.time= "+timesum/5);
			
		}//end of for-file_index
	}//end of main()
	
	static double calEFT(Ant myAnt,int fromT,int fromP,int toT,int toP){  //�qfrom���t��to���̦������ɶ�
		
		double thest=0.0;  //�Ӥu�@�i�H�}�l�ɶ�
		double s=0.0;
		
		for(int i=0;i<PRED[toT].length;i++){
			s=0;
			if(toP==myAnt.ms[PRED[toT][i]]){  //�P����̦��P�@�B�z�椸
				s=myAnt.ft[PRED[toT][i]];
			}else{  //���P�B�z�椸�A�[�W�q�T����
				s=myAnt.ft[PRED[toT][i]]+dag[PRED[toT][i]][toT];
			}
			if(i==0)thest=s;
			else if(thest<s)thest=s;			
		}//end of for-loop  
		//����thest����ƨ�F�ӳB�z���ɶ� (�H�ɥi�H����)
		
		
		return thest+cost[toT][toP];
	}//end of calcETF()
	
	static void getaPath(Ant myAnt){  
		//�ǤJ�@��Ant �A�����@��
		// q0��ACS��transition rule�ѼơA0~1�A1��local search(���g�L���L)
		//��nss,ms,done,SchLen
		 
		double maxprob=0.0;
		int selected=0;
		int selectedP=0;  //��쪺processor
		int selectedT=0;  //��쪺task
		
		//�qstart ����entry�u�@
		double[] prob=new double[Q];
		for(int i=0;i<Q;i++){
			if(cost[0][i]==0.0)Visibility=(Integer.MAX_VALUE/Q)-1;   //cost��0�|��Inifity
			else Visibility=1/cost[0][i];
			prob[i]=start2entry[i]*Math.pow(Visibility, Beta);
		}
		selected=Tool.select(prob);
		selectedP=selected%Q;
		selectedT=selected/Q; 
		myAnt.ss[0]=selectedT;
		myAnt.ms[selectedT]=selectedP;
		myAnt.done[selectedT]=1;
		
		for(int no=1;no<V;no++){
			
			for(int i=0;i<SUCC[selectedT].length;i++){    //�C�ӥߧY���~�̤����u�@
				if(myAnt.candidate.contains((Integer)SUCC[selectedT][i])||myAnt.done[SUCC[selectedT][i]]==1)continue;  //�Y�ӫ��~�̤w�bcandidate�A�Τw�����h���L
				for(int j=0;j<PRED[SUCC[selectedT][i]].length;j++){  //����̬ҧ������[�Jcandidate
					if(myAnt.done[PRED[SUCC[selectedT][i]][j]]==0)break;
					if(myAnt.done[PRED[SUCC[selectedT][i]][j]]==1&&j==PRED[SUCC[selectedT][i]].length-1)myAnt.candidate.add((Integer)SUCC[selectedT][i]);
				}
			}
			
			//��C�ӭԿ�List�����u�@�A�ݨC��processor�A���O��X(�ਣ��&�O���X���)�A�A�H���L��ܡA�M�w�έ���processor��U�@�Ӥu�@
			
			prob=new double[myAnt.candidate.size()*Q];  //�x�s�C�ӳB�z����U�u�@(�ਣ��&�O���X���)
			for(int i=0,k=0;i<myAnt.candidate.size();i++){
				for(int j=0;j<Q;j++){
					if(cost[myAnt.candidate.get(i)][j]==0.0)Visibility=(Integer.MAX_VALUE/Q)-1;
					else Visibility=1/calEFT(myAnt,selectedT,selectedP,(int)myAnt.candidate.get(i),j);
					prob[k++]=pheromone[selectedT][myAnt.ms[selectedT]][(int)myAnt.candidate.get(i)][j]*Math.pow(Visibility, Beta);
				}
			}
			if(new Random().nextFloat()<=q0){
				maxprob=0.0;  //inti
				selected=0;
				for(int r=0;r<prob.length;r++){
					if(maxprob<prob[r])	{
						maxprob=prob[r];  //maxprob���̰��@��
						selected=r;
					}
				}
			}else 	selected=Tool.select(prob);
				
			selectedT=myAnt.candidate.get((int)(selected/Q)); 
						
			selectedP=selected%Q;  
			myAnt.ss[no]=selectedT;  //��쪺task
			myAnt.ms[selectedT]=selectedP;  //��쪺processor
			myAnt.done[selectedT]=1; 
			myAnt.candidate.remove((Integer)selectedT);
		}//end of for-loop no
		// �즹�@�����ƨ���
		
		myAnt.SchLen=Tool.getSchLen(myAnt.ss, myAnt.ms, Q, dag, cost, PRED, myAnt.st, myAnt.ft);
		
		if(Ant.bestLen>myAnt.SchLen){  //��s����̨�
			Ant.bestLen=myAnt.SchLen;
			for(int i=0;i<myAnt.ms.length;i++)Ant.bestms[i]=myAnt.ms[i];
			for(int i=0;i<myAnt.ss.length;i++)Ant.bestss[i]=myAnt.ss[i];
			for(int i=0;i<myAnt.st.length;i++)Ant.bestst[i]=myAnt.st[i];
			for(int i=0;i<myAnt.ft.length;i++)Ant.bestft[i]=myAnt.ft[i];
		}
		
	}//end of getaPath()
	
	static void LocalUpdate(Ant myAnt){  //�O���X���o
		
		for(int i=0;i<myAnt.ss.length-1;i++){
			pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=(1-Rho)*pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]+(Rho*tau0);
			if(pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]<tau0)pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=tau0;
		}
		
	}//end of LocalUpdate()
	
	static void GlobalUpdate(Ant myAnt){  //�O���X�W�j
		
		for(int i=0;i<myAnt.ss.length-1;i++){
			pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=(1-Rho)*pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]+(Rho*(1/myAnt.SchLen));
			if(pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]<tau0)pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=tau0;
		}
	}//end of GlobalUpdate()
	
	
	
}//end of class ACS
