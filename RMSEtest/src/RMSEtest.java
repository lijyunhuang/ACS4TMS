
public class RMSEtest{
	public static void main(String[] args){
		double[][] M={{5.0,2.0,4.0,4.0,3.0},{3.0,1.0,2.0,4.0,1.0},{2.0,0.0,3.0,1.0,4.0},{2.0,5.0,4.0,3.0,5.0},{4.0,4.0,5.0,4.0,0.0}};
		double[][] U={{1.0,1.0},{1.0,1.0},{1.0,1.0},{1.0,1.0},{1.0,1.0}};
		double[][] V={{1.0,1.0,1.0,1.0,1.0},{1.0,1.0,1.0,1.0,1.0}};
		int[] s={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
		
		
		System.out.println(calcRMSE(M,U,V,s));
	}//end of main
	
	static double calcRMSE(double[][] M,double[][] U,double[][] V,int[] s){
		int i,j,k,f; 
		int m,n;
		m=M.length;
		n=M[0].length;
		
		 for(f=0;f<(m+n)*2;f++){
			  if(s[f]<m*2){
			  	i=s[f]/2;
			  	j=s[f]%2; //U的編碼
				double x=0.0;
		   		double ax=0.0;
		  		 for(k=0;k<m;k++){
		    		if(M[i][k]!=0){
		    		 if(j==0){
		   			   x=x+V[0][k]*(M[i][k]-U[i][1]*V[1][k]);
		    		  ax=ax+V[0][k]*V[0][k];
		    		 }
		  		   else{
		    		  x=x+V[1][k]*(M[i][k]-U[i][0]*V[0][k]);
		  			    ax=ax+V[1][k]*V[1][k];	
		  		   }	
		  		  }
		  		 }
		   		U[i][j]=x/ax;
			  }
			  else{
			  	i=s[f]%2;
			  	j=s[f]/2-m;//V的編碼  
			  	
				 double y=0.0;
		  		 double ay=0.0;
		  		 for(k=0;k<n;k++){
		  		  if(M[k][j]!=0){
		  		   if(i==0){
		   			   y=y+U[k][0]*(M[k][j]-U[k][1]*V[1][j]);
		   			   ay=ay+U[k][0]*U[k][0];
		   			  }
				 else{
		   		   y=y+U[k][1]*(M[k][j]-U[k][0]*V[0][j]);
		   		   ay=ay+U[k][1]*U[k][1];	
		    		 }	
		   		 }
		  		 }
		   V[i][j]=y/ay;
			  }
			  System.out.printf("%2d    %d%d",s[f],i,j);
			  System.out.printf("\n");
		} 

		for(i=0;i<m;i++){
		  for(j=0;j<2;j++){
			  System.out.printf("%.3f",U[i][j]); 
			  System.out.printf("\n");
		}
		}
		System.out.printf("\n");
		System.out.printf("\n");
		 
		 for(i=0;i<2;i++){
		  for(j=0;j<n;j++){
			  System.out.printf("%.3f",V[i][j]); 
			  System.out.printf("\n");
		}
		}
		//UV相乘等於MB 

		 double[][] MB=new double[m][n];
		 for(i=0;i<m;i++){
		  for(j=0;j<n;j++){
		  MB[i][j]=0;
		 }	
		 }
		 double[][][] MBB=new double[m][n][2];
		 for(k=0;k<m;k++){
		  for(j=0;j<n;j++){
		   for(i=0;i<2;i++){
		    MBB[k][j][i]=U[k][i]*V[i][j];
		   }
		  }
		 }
		 for(k=0;k<m;k++){
		  for(j=0;j<n;j++){
		   for(i=0;i<2;i++){
		    MB[k][j]=MB[k][j]+MBB[k][j][i];
		   }
		  }
		 }
		//M與MB的RMSE值 
		double[][] RB=new double[m][n];
		 for(i=0;i<m;i++){
		  for(j=0;j<n;j++){
		  RB[i][j]=0;
		 }	
		 }
		 double RMSEB=0.0;
		 int b=0;
		 for(i=0;i<m;i++){
		  for(j=0;j<n;j++){
		   if(M[i][j]==0){
		    b=b+1;
		    RB[i][j]=0;
		   }
		   else{
		    RB[i][j]=(M[i][j]-MB[i][j])*(M[i][j]-MB[i][j]);
		   }
		  }
		 }

		 for(i=0;i<m;i++){
		  for(j=0;j<n;j++){
		   RMSEB=RMSEB+RB[i][j];
		  }
		 }
		 RMSEB=Math.sqrt(RMSEB/(m*n-b));
		 System.out.printf("\n");
		 System.out.printf("%.10f",RMSEB);
		 System.out.printf("\n");
		 System.out.printf("\n");

		 return RMSEB;
	}//end of calsRMSE()


}//end of class RMSEtest