public class Kmeans_test2 {
	public static void main(String[] args){
		double [][] data={{3,17,7,7,3,10,4,8.5,4.5},
						  {4,15,9,5,3,10,3,7.5,3},
						  {4,16,8,2,3,12,5,5.33,4},
						  {3,18,6,9,2,9,4,9,3},
						  {5,13,11,1,2,6,4,4.33,3.67},
						  {4,12,12,2,2,5,5,4,4},
						  {5,14,10,2,3,7,4,4.67,3.33},
						  {5,13,11,3,2,6,4,4.33,3.67},
						  {3,19,5,19,1,19,4,19,5},
						  {3,20,4,3,2,16,4,10,4},
						  {4,16,8,5,3,21,3,8,3},
						  {4,17,7,8,2,9,2,5.67,2}};
		int groupcenter[]={1,2,3};
		int x=1;
		double length[]= new double[groupcenter.length];
		int pointresult[] = new int[data.length];
		double center[][]=new double[groupcenter.length][data[0].length];
		for(int i=0;i<groupcenter.length;i++){
			for(int j=0;j<data[0].length;j++){
				center[i][j]=data[groupcenter[i]-1][j];
			}
		}
		double NEWcenter[][]=new double[groupcenter.length][data[0].length];
		int centercount[]=new int[groupcenter.length];
		
		int times=0;
		while(x==1){
			times++;
			double min_value;
		
			
//			//歐幾里得距離
//			for(int i=0;i<data.length;i++){
//				for(int j=0;j<groupcenter.length;j++){
//					length[j]=0;
//					for(int k=0;k<data[0].length;k++){
//						length[j]=length[j]+(data[i][k]-center[j][k])*(data[i][k]-center[j][k]);
//					}
//					length[j]=Math.sqrt(length[j]);
//					System.out.print(length[j]);
//					System.out.print(" ");
//				}
//				System.out.print("\n");
//				//歐曼大小
//				min_value=100000000;
//				for(int j=0;j<groupcenter.length;j++){
//					if(length[j]<min_value){
//						min_value= length[j];
//						pointresult[i]=j;
//					}
//					
//				}
			
//			}	

				
				//曼哈頓距離
				for(int i=0;i<data.length;i++){
					for(int j=0;j<groupcenter.length;j++){
						length[j]=0;
						for(int k=0;k<data[0].length;k++){
							length[j]=length[j]+Math.abs(data[i][k]-center[j][k]);
						}
						System.out.print(length[j]);
						System.out.print(" ");
					}
					System.out.print("\n");
			//歐曼大小
			min_value=100000000;
			for(int j=0;j<groupcenter.length;j++){
				if(length[j]<min_value){
					min_value= length[j];
					pointresult[i]=j;
				}
				
			}
		}
			
				
//			//切比雪夫距離
//			for(int i=0;i<data.length;i++){
//				for(int j=0;j<groupcenter.length;j++){
//					double max_length=0;
//					double lengthcompare=0;
//					for(int k=0;k<data[0].length;k++){
//						lengthcompare=Math.abs(data[i][k]-center[j][k]);
//						if(lengthcompare>max_length){
//							length[j]=lengthcompare;
//						}
//						
//					}
//					System.out.print(length[j]);
//					System.out.print(" ");
//				}
//				System.out.print("\n");
//				//歐曼大小
//				min_value=100000000;
//				for(int j=0;j<groupcenter.length;j++){
//					if(length[j]<min_value){
//						min_value= length[j];
//						pointresult[i]=j;
//					}
//					
//				}
//				
//			}
			
//			for(int i=0;i<data.length;i++){
//				System.out.print(pointresult[i]);
//				System.out.print(" ");
//			}
			
			System.out.print("\n");
			for(int i=0;i<groupcenter.length;i++){
				for(int j=0;j<data[0].length;j++){
					NEWcenter[i][j]=0;
					centercount[i]=0;
				}
			}
			
			
			
			for(int i=0;i<data.length;i++){
				for(int j=0;j<groupcenter.length;j++){
					if(pointresult[i]==j){
						for(int k=0;k<data[0].length;k++){
							NEWcenter[j][k]=NEWcenter[j][k]+data[i][k];
						}	
						centercount[j]++;
					}
					
				}
			}
			
			for(int i=0;i<groupcenter.length;i++){
				System.out.print(centercount[i]);
				System.out.print(" ");
			}
			System.out.print("\n");
			
//			for(int i=0;i<groupcenter.length;i++){
//				for(int j=0;j<data[0].length;j++){
//					System.out.print(NEWcenter[i][j]);
//					System.out.print(" ");
//				}
//			}
			
			for(int i=0;i<groupcenter.length;i++){
				for(int j=0;j<data[0].length;j++){
					NEWcenter[i][j]=NEWcenter[i][j]/centercount[i];
				}				
			}
			
			
		System.out.printf("第%d次\n",times);
			for(int i=0;i<data.length;i++){
				System.out.printf("第%d點",i+1);
				System.out.printf("( ");
				for(int j=0;j<data[0].length;j++){
					System.out.printf("%.2f ",data[i][j]);
				}
				System.out.printf(")");
				System.out.printf("是屬於第%d類\n",pointresult[i]+1);
			}
			
			System.out.printf("舊中心點:");
			for(int i=0;i<NEWcenter.length;i++){
				for(int j=0;j<data[0].length;j++){
					System.out.printf("%.2f ",center[i][j]);
				}
				System.out.print("   ");
			}
			System.out.printf("\n");
			
			System.out.printf("新中心點:");
			for(int i=0;i<NEWcenter.length;i++){
				for(int j=0;j<data[0].length;j++){
					System.out.printf("%.2f ",NEWcenter[i][j]);
				}
				System.out.print("   ");
			}
			System.out.printf("\n");
			int centerresultcount=0;
			for(int i=0;i<center.length;i++){
				for(int j=0;j<data[0].length;j++){
					if(center[i][j]==NEWcenter[i][j]){
						centerresultcount++;
					}
					
				}
			}
			System.out.printf("%d \n",centerresultcount);
				if(centerresultcount==(groupcenter.length*data[0].length))break;
				
				for(int i=0;i<groupcenter.length;i++){
					for(int j=0;j<data[0].length;j++){
						center[i][j]=NEWcenter[i][j];
					}
				}
			
		}
		
		
		
	}
}
