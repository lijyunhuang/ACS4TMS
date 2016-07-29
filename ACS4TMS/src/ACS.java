import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ACS {

	static double[][][][] pheromone;  //費洛蒙表
	static double[] start2entry;  //虛擬開始到工作進入點的費洛蒙嗅跡表
	
	static int times=200;  //迭代數
	static int m=10; //螞蟻數量
	
	static double tau0=0.000019;  //最低費洛蒙濃度
	static double Beta=0.07;   //能見度權重
	static double Rho=0.01;    //費洛蒙蒸發率
	static double q0=1;     //轉移規則的參數
	
	static double Visibility;  //能見度
	
	static int V;  //工作數
	static int Q;  //處理器數
	static int E;  //有向邊個數
	
	static int[][] dag;
	static double[][] cost;  //各工作在各處理單元上的計算成本
	static double[][] rate;  //個處理器之間的傳輸速率
	
	static int[][] PRED;  //各工作的立即先行者
	static int[][] SUCC;  //各工作的立即後繼者
	static long starttime;  //開始時間
	
	static String[] filename={"C:\\test\\n4_00.dag","C:\\test\\n4_02.dag","C:\\test\\n4_04.dag","C:\\test\\n4_06.dag"};

	
	public static void main(String[] args) throws IOException{
	
//		String filename="C:\\Users\\rex\\Desktop\\test\\n4_04.dag";
		
		for(int file_index=0;file_index<4;file_index++){
			
			//資料準備
			BufferedReader 	br=new BufferedReader(new FileReader(filename[file_index]));
			String temp=null;
			String[] temps=null;
	
			while ((temp=br.readLine())!=null)   //濾掉註解
				if (temp.charAt(0) == '*')
					break;
	
				temp=br.readLine();
				Q=Integer.parseInt(temp);
				temp=br.readLine();
				V=Integer.parseInt(temp);
				temp=br.readLine();
				E=Integer.parseInt(temp);
							
				while((temp=br.readLine())!=null)   //濾掉註解
					if(temp.charAt(0)=='*')break;
				
				
				rate=new double[Q][Q];
				for(int i=0;i<Q;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					for(int j=0;j<temps.length;j++){
						rate[i][j]=Double.parseDouble(temps[j]);
					}
				}//rate[][] 建置完成
			
				while((temp=br.readLine())!=null)   //濾掉註解
					if(temp.charAt(0)=='*')break;
				
				cost=new double[V][Q];
				for(int i=0;i<V;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					for(int j=0;j<temps.length;j++){
						cost[i][j]=Double.parseDouble(temps[j]);				
					}
				}//cost[][] 建置完成
				
				while((temp=br.readLine())!=null)   //濾掉註解
					if(temp.charAt(0)=='*')break;
				
				dag=new int[V][V];
				for(int i=0;i<V;i++)for(int j=0;j<V;j++)dag[i][j]=-1;  //初始化，每項為-1
				
				for(int i=0;i<E;i++){
					temp=br.readLine();
					temps=temp.split(" ");
					dag[Integer.parseInt(temps[0])][Integer.parseInt(temps[1])]=Integer.parseInt(temps[2]);
				}//dag[][] 建置完成
			
				PRED=new int[V][];  
				int[] nPRED=new int[V];  //記錄每個工作的立即先行者個數
				
				for(int i=0;i<V;i++){
					for(int j=0;j<V;j++){
						if(dag[j][i]<0)continue;
						else nPRED[i]++;
					}
				}// nPRED[]建置完畢
		
				for(int i=0;i<V;i++)	PRED[i]=new int[nPRED[i]];
				
				for(int j=0;j<V;j++){
					int n=0;
					for(int i=0;i<V;i++){
						if(dag[i][j]==-1)continue;
						else{
							PRED[j][n++]=i;
						}
					}
				}//PRED[][] 建置完畢
				
				
				SUCC=new int[V][];
				int[] nSUCC=new int[V];  //紀錄每個工作的立即後繼者個數
				
				for(int i=0;i<V;i++){
					for(int j=0;j<V;j++){
						if(dag[i][j]==-1)continue;
						else nSUCC[i]++;
					}
				}//nSUCC[] 建置完畢
				
				for(int i=0;i<V;i++)SUCC[i]=new int[nSUCC[i]];
				for(int i=0;i<V;i++){
					int n=0;
					for(int j=0;j<V;j++){
						if(dag[i][j]==-1)continue;
						else SUCC[i][n++]=j;
					}
				}//SUCC[][]建置完畢
				br.close();
		////***資料準備結束***////
				
				starttime=System.currentTimeMillis();  //紀錄開始ACS時間
				
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
						
					//初始化費洛蒙表
					start2entry=new double[Q];
					for(int i=0;i<Q;i++)start2entry[i]=tau0;
					pheromone=new double[V][Q][V][Q];
					for(int i=0;i<V;i++)
						for(int j=0;j<Q;j++)
							for(int k=0;k<V;k++)
								for(int l=0;l<Q;l++)
									pheromone[i][j][k][l]=tau0;
					//---費洛蒙表初始化結束---//
					
					//以HEFT所得解來先加上費洛蒙
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
						
						for(int i=0;i<m;i++){   //每隻螞蟻走出一個解，並對走過的路作揮發
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
	
	static double calEFT(Ant myAnt,int fromT,int fromP,int toT,int toP){  //從from分配給to的最早完成時間
		
		double thest=0.0;  //該工作可以開始時間
		double s=0.0;
		
		for(int i=0;i<PRED[toT].length;i++){
			s=0;
			if(toP==myAnt.ms[PRED[toT][i]]){  //與先行者位於同一處理單元
				s=myAnt.ft[PRED[toT][i]];
			}else{  //不同處理單元，加上通訊成本
				s=myAnt.ft[PRED[toT][i]]+dag[PRED[toT][i]][toT];
			}
			if(i==0)thest=s;
			else if(thest<s)thest=s;			
		}//end of for-loop  
		//此時thest為資料到達該處理器時間 (隨時可以執行)
		
		
		return thest+cost[toT][toP];
	}//end of calcETF()
	
	static void getaPath(Ant myAnt){  
		//傳入一個Ant ，完成一解
		// q0為ACS的transition rule參數，0~1，1為local search(不經過輪盤)
		//填好ss,ms,done,SchLen
		 
		double maxprob=0.0;
		int selected=0;
		int selectedP=0;  //選到的processor
		int selectedT=0;  //選到的task
		
		//從start 走到entry工作
		double[] prob=new double[Q];
		for(int i=0;i<Q;i++){
			if(cost[0][i]==0.0)Visibility=(Integer.MAX_VALUE/Q)-1;   //cost為0會變Inifity
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
			
			for(int i=0;i<SUCC[selectedT].length;i++){    //每個立即後繼者中的工作
				if(myAnt.candidate.contains((Integer)SUCC[selectedT][i])||myAnt.done[SUCC[selectedT][i]]==1)continue;  //若該後繼者已在candidate，或已完成則略過
				for(int j=0;j<PRED[SUCC[selectedT][i]].length;j++){  //先行者皆完成的加入candidate
					if(myAnt.done[PRED[SUCC[selectedT][i]][j]]==0)break;
					if(myAnt.done[PRED[SUCC[selectedT][i]][j]]==1&&j==PRED[SUCC[selectedT][i]].length-1)myAnt.candidate.add((Integer)SUCC[selectedT][i]);
				}
			}
			
			//對每個候選List中的工作，看每個processor，分別算出(能見度&費洛蒙嗅跡)，再以輪盤選擇，決定用哪個processor到下一個工作
			
			prob=new double[myAnt.candidate.size()*Q];  //儲存每個處理器到各工作(能見度&費洛蒙嗅跡)
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
						maxprob=prob[r];  //maxprob為最高濃度
						selected=r;
					}
				}
			}else 	selected=Tool.select(prob);
				
			selectedT=myAnt.candidate.get((int)(selected/Q)); 
						
			selectedP=selected%Q;  
			myAnt.ss[no]=selectedT;  //選到的task
			myAnt.ms[selectedT]=selectedP;  //選到的processor
			myAnt.done[selectedT]=1; 
			myAnt.candidate.remove((Integer)selectedT);
		}//end of for-loop no
		// 到此一隻螞蟻走完
		
		myAnt.SchLen=Tool.getSchLen(myAnt.ss, myAnt.ms, Q, dag, cost, PRED, myAnt.st, myAnt.ft);
		
		if(Ant.bestLen>myAnt.SchLen){  //更新全域最佳
			Ant.bestLen=myAnt.SchLen;
			for(int i=0;i<myAnt.ms.length;i++)Ant.bestms[i]=myAnt.ms[i];
			for(int i=0;i<myAnt.ss.length;i++)Ant.bestss[i]=myAnt.ss[i];
			for(int i=0;i<myAnt.st.length;i++)Ant.bestst[i]=myAnt.st[i];
			for(int i=0;i<myAnt.ft.length;i++)Ant.bestft[i]=myAnt.ft[i];
		}
		
	}//end of getaPath()
	
	static void LocalUpdate(Ant myAnt){  //費洛蒙揮發
		
		for(int i=0;i<myAnt.ss.length-1;i++){
			pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=(1-Rho)*pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]+(Rho*tau0);
			if(pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]<tau0)pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=tau0;
		}
		
	}//end of LocalUpdate()
	
	static void GlobalUpdate(Ant myAnt){  //費洛蒙增強
		
		for(int i=0;i<myAnt.ss.length-1;i++){
			pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=(1-Rho)*pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]+(Rho*(1/myAnt.SchLen));
			if(pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]<tau0)pheromone[myAnt.ss[i]][myAnt.ms[myAnt.ss[i]]][myAnt.ss[i+1]][myAnt.ms[myAnt.ss[i+1]]]=tau0;
		}
	}//end of GlobalUpdate()
	
	
	
}//end of class ACS
