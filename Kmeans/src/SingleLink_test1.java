import java.util.Scanner;
public class SingleLink_test1 {
	public static void main(String[] args){
		double [][] data={{3,20,4,20,1,20,3,20,2},
						  {4,15,9,5,3,10,3,7.5,3},
						  {2,21,3,21,3,21,3,21,3},
						  {2,22,2,22,2,22,2,22,2},
						  {3,17,7,7,3,10,4,8.5,4.5},
						  {4,15,9,5,3,10,3,7.5,3},
						  {4,16,8,2,3,12,5,5.33,4},
						  {3,18,6,9,2,9,4,9,3}};
		System.out.printf("請輸入欲分的群數:");
		Scanner input =new Scanner(System.in);
		int groupnumber=input.nextInt();

		double distance[][]= new double[data.length][data.length];
		int pointresult[] = new int[data.length];
		
		for(int i=0;i<data.length;i++){
			for(int k=0;k<data.length;k++){
					distance[i][k]=0;
			}
			
		}
		
		for(int i=0;i<data.length;i++){
			for(int k=0;k<data.length;k++){
				for(int j=0;j<data[0].length;j++){
					distance[i][k]=distance[i][k]+(data[i][j]-data[k][j])*(data[i][j]-data[k][j]);
				}
			}		
		}
		
		for(int i=0;i<data.length;i++){
			for(int k=0;k<data.length;k++){
					System.out.printf("%5.2f ",distance[i][k]);
			}
			System.out.printf("\n");
		}
		
		int min_valuegroup1=0;
		int min_valuegroup2=0;
		int min_valuegroup3=0;
		int min_valuegroup4=0;
		if(groupnumber==2){
			double min_value=10000000;
			double secondmin_value=10000000;
			for(int i=0;i<data.length;i++){
				for(int k=0;k<data.length;k++){
						if(distance[i][k]<min_value && distance[i][k]!=0){
							min_value=distance[i][k];
							min_valuegroup1=i;
							min_valuegroup2=k;
						}else if(distance[i][k]<secondmin_value){
							secondmin_value=distance[i][k];
							min_valuegroup3=i;
							min_valuegroup4=k;
						}
				}
			}	
				pointresult[min_valuegroup1]=0;
				pointresult[min_valuegroup2]=0;
				pointresult[min_valuegroup3]=1;
				pointresult[min_valuegroup4]=1;
				
				
					for(int k=0;k<data.length;k++){
						double a= distance[min_valuegroup1][k];
						double b= distance[min_valuegroup2][k];
						double c= distance[min_valuegroup3][k];
						double d= distance[min_valuegroup4][k];
						double min_valuecompare1=0;
						double min_valuecompare2=0;
						if(a>b){
							min_valuecompare1=b;
						}else{
							min_valuecompare1=a;
						}
						if(c>d){
							min_valuecompare2=d;
						}else{
							min_valuecompare2=c;
						}
						if(min_valuecompare1>min_valuecompare2){
							pointresult[k]=1;
						}else{
							pointresult[k]=0;
						}
						
					}
					for(int i=0;i<data.length;i++){
						System.out.printf("%d ",pointresult[i]);
					}
				
			
		}else{
			
		}
		
		
		
		
		
	}
}
