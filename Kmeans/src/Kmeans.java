import java.util.*;
public class Kmeans {
	public static void main(String[] args){
		int groupnum;
		double [][] data={{2,10},{2,5},
						  {8,4},{5,8},
						  {7,5},{6,4},
						  {1,2},{4,9}};
		Scanner input = new Scanner(System.in);
		//System.out.printf("請輸入欲分群:");
		//groupnum = input.nextInt();
		int groupcenter[]={1,4,7};
		int x=1;
		double length1;
		double length2;
		double length3;
		double newgroupcenter[]= new double[groupcenter.length];
		int pointresult[] = new int[data.length];
		
		double center1X=data[0][0];
		double center1Y=data[0][1];
		double center2X=data[3][0];
		double center2Y=data[3][1];
		double center3X=data[6][0];
		double center3Y=data[6][1];
		double Ncenter1X=0;
		double Ncenter1Y=0;
		double Ncenter2X=0;
		double Ncenter2Y=0;
		double Ncenter3X=0;
		double Ncenter3Y=0;
		int times=0;
		while(x==1){
			times++;
			
			for(int i=0;i<data.length;i++){
				length1=(data[i][0]-center1X)*(data[i][0]-center1X)+(data[i][1]-center1Y)*(data[i][1]-center1Y);
				length2=(data[i][0]-center2X)*(data[i][0]-center2X)+(data[i][1]-center2Y)*(data[i][1]-center2Y);
				length3=(data[i][0]-center3X)*(data[i][0]-center3X)+(data[i][1]-center3Y)*(data[i][1]-center3Y);
				if(length1>length2&&length3>length2){
					pointresult[i] =2;
				}else if(length3>length1){
					pointresult[i] =1;
				}else{
					pointresult[i] =3;
				}	
			}
			Ncenter1X=0;
			Ncenter1Y=0;
			Ncenter2X=0;
			Ncenter2Y=0;
			Ncenter3X=0;
			Ncenter3Y=0;
			int center1count=0;
			int center2count=0;
			int center3count=0;
			for(int i=0;i<data.length;i++){
				if(pointresult[i] ==1){
					Ncenter1X=Ncenter1X+data[i][0];
					Ncenter1Y=Ncenter1Y+data[i][1];
					center1count++;
				}else if(pointresult[i] ==2){
					Ncenter2X=Ncenter2X+data[i][0];
					Ncenter2Y=Ncenter2Y+data[i][1];
					center2count++;

				}else{
					Ncenter3X=Ncenter3X+data[i][0];
					Ncenter3Y=Ncenter3Y+data[i][1];
					center3count++;
				}
			}
			Ncenter1X=Ncenter1X/center1count;
			Ncenter1Y=Ncenter1Y/center1count;
			Ncenter2X=Ncenter2X/center2count;
			Ncenter2Y=Ncenter2Y/center2count;
			Ncenter3X=Ncenter3X/center3count;
			Ncenter3Y=Ncenter3Y/center3count;
			System.out.printf("第%d次\n",times);
			for(int i=0;i<data.length;i++){
				System.out.printf("第%d點[%.1f][%.1f]屬於第%d類\n",i+1,data[i][0],data[i][1],pointresult[i]);
			}
			System.out.printf("%.1f,%.1f\n",Ncenter1X,Ncenter1Y);
			System.out.printf("%.1f,%.1f\n",Ncenter2X,Ncenter2Y);
			System.out.printf("%.1f,%.1f\n",Ncenter3X,Ncenter3Y);
			
			if((center1X == Ncenter1X)&&(center1Y == Ncenter1Y)&&(center2X == Ncenter2X)&&(center2Y == Ncenter2Y)&&(center3Y == Ncenter3Y)&&(center3X == Ncenter3X))break;
			center1X=Ncenter1X;
			center1Y=Ncenter1Y;
			center2X=Ncenter2X;
			center2Y=Ncenter2Y;
			center3X=Ncenter3X;
			center3Y=Ncenter3Y;
			
		}
		
		
		
	}
}
