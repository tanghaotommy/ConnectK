//import connectK.BoardModel;
//
//public class test {
//
//	public static int kLength=0;
//	static class State{
//		public static final int VERTICAL=0;
//		public static final int HORIZONTAL=1;
//		public static final int LEFTDOWN=2;
//		public static final int RIGHTDOWN=3;
//
//		public class Point{
//			public byte value;
//			public boolean[] directions={false,false,false,false};
//
//			public Point(byte value) {
//				// TODO Auto-generated constructor stub
//				this.value=value;
//			}
//		}
//
//		public Point[][] pieces; // [column][row] 1, 2, or null for empty
//		public int alpha=Integer.MIN_VALUE;
//		public int beta=Integer.MAX_VALUE;
//		public State parent;
//		public int depth;
//		public boolean isMe;
//
//		public int CalculatePoint(int i, int j){
//			int utility1=0;
//			int utility2=0;
//			if(pieces[i][j].value==1){
//				if(pieces[i][j].directions[VERTICAL]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j).value==1){ //next is ours
//							count++;
//							getPoint(i+k, j).directions[VERTICAL]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j).value==2 || getPoint(i+k, j).value==-1){
//							count/=2;
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j).value==2 || getPoint(i-k2, j).value==-1){//meet enemy, check last space
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j).value==2 || getPoint(i+k+k2, j).value==-1){ //meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i-1, j).value==2 || getPoint(i-1, j).value==-1){ //check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i-k3, j).value==2 || getPoint(i-k3, j).value==-1){ //check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility1+=count;
//					System.out.println(String.format("%d,%d V(1): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[HORIZONTAL]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i, j+k).value==1){//next is ours
//							count++;
//							getPoint(i, j+k).directions[HORIZONTAL]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i, j+k).value==2 || getPoint(i, j+k).value==-1){//meet enemy, check last space
//							count/=2;
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i, j-k2).value==2 || getPoint(i, j-k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i, j+k+k2).value==2 || getPoint(i, j+k+k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i, j-1).value==2 || getPoint(i, j-1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i, j-k3).value==2 || getPoint(i, j-k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility1+=count;
//					System.out.println(String.format("%d,%d H(1): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[LEFTDOWN]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j+k).value==1){//next is ours
//							count++;
//							getPoint(i+k, j+k).directions[LEFTDOWN]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j+k).value==2 || getPoint(i+k, j+k).value==-1){//meet enemy, check last space
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j-k2).value==2 || getPoint(i-k2, j-k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j+k+k2).value==2 || getPoint(i+k+k2, j+k+k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i-1, j-1).value==2 || getPoint(i-1, j-1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i-k3, j-k3).value==2 || getPoint(i-k3, j-k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility1+=count;
//					System.out.println(String.format("%d,%d L(1): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[RIGHTDOWN]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j-k).value==1){//next is ours
//							count++;
//							getPoint(i+k, j-k).directions[RIGHTDOWN]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j-k).value==2 || getPoint(i+k, j-k).value==-1){//meet enemy, check last space
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j+k2).value==2 || getPoint(i-k2, j+k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j-k-k2).value==2 || getPoint(i+k+k2, j-k-k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i-1, j+1).value==2 || getPoint(i-1, j+1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i-k3, j+k3).value==2 || getPoint(i-k3, j+k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility1+=count;
//					System.out.println(String.format("%d,%d R(1): %d", i,j,count));
//				}
//
//			}
//			else if(pieces[i][j].value==2){
//				if(pieces[i][j].directions[VERTICAL]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j).value==2){ //next is ours
//							count++;
//							getPoint(i+k, j).directions[VERTICAL]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j).value==1 || getPoint(i+k, j).value==-1){
//							count/=2;
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j).value==1 || getPoint(i+k, j).value==-1){//meet enemy, check last space
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j).value==1 || getPoint(i+k+k2, j).value==-1){ //meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){		
//								if(getPoint(i-1, j).value==1 || getPoint(i-1, j).value==-1){ //check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {	
//								if(getPoint(i-k3, j).value==1 || getPoint(i-k3, j).value==-1){ //check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility2+=count;
//					System.out.println(String.format("%d,%d V(2): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[HORIZONTAL]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i, j+k).value==2){//next is ours
//							count++;
//							getPoint(i, j+k).directions[HORIZONTAL]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i, j+k).value==1 || getPoint(i, j+k).value==-1){//meet enemy, check last space
//							count/=2;
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i, j-k2).value==1 || getPoint(i, j-k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i, j+k+k2).value==1 || getPoint(i, j+k+k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){		
//								if(getPoint(i, j-1).value==1 || getPoint(i, j-1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i, j-k3).value==1 || getPoint(i, j-k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility2+=count;
//					System.out.println(String.format("%d,%d H(2): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[LEFTDOWN]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j+k).value==2){//next is ours
//							count++;
//							getPoint(i+k, j+k).directions[LEFTDOWN]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j+k).value==1 || getPoint(i+k, j+k).value==-1){//meet enemy, check last space
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j-k2).value==1 || getPoint(i-k2, j-k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j+k+k2).value==1 || getPoint(i+k+k2, j+k+k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i-1, j-1).value==1 || getPoint(i-1, j-1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {					
//								if(getPoint(i-k3, j-k3).value==1 || getPoint(i-k3, j-k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility2+=count;
//					System.out.println(String.format("%d,%d L(2): %d", i,j,count));
//				}
//				if(pieces[i][j].directions[RIGHTDOWN]==false){
//					int count=1;
//
//					for (int k = 1; k <= kLength-1; k++) {
//						if(getPoint(i+k, j-k).value==2){//next is ours
//							count++;
//							getPoint(i+k, j-k).directions[RIGHTDOWN]=true;
//							if(count==kLength)
//								return Integer.MAX_VALUE-1;
//						}
//						else if(getPoint(i+k, j-k).value==1 || getPoint(i+k, j-k).value==-1){//meet enemy, check last space
//							for (int k2 = 1; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i-k2, j+k2).value==1 || getPoint(i-k2, j+k2).value==-1){
//									count=0;
//									break;
//								}
//							}
//							break;
//						}
//						else {
//							int k2=1;
//							for (; k2 <= kLength-k-1; k2++) {
//								if(getPoint(i+k+k2, j-k-k2).value==1 || getPoint(i+k+k2, j-k-k2).value==-1){//meet enemy, k2 is the position of enemy
//									break;
//								}
//							}
//							if(k2== kLength-k){
//								if(getPoint(i-1, j+1).value==1 || getPoint(i-1, j+1).value==-1){//check if there is enough space
//									count/=2;
//									break;
//								}
//							}
//							for (int k3 = 1; k3 <= kLength-k-k2; k3++) {
//								if(getPoint(i-k3, j+k3).value==1 || getPoint(i-k3, j+k3).value==-1){//check if there is enough space
//									count=0;
//									break;
//								}
//							}
//						}
//					}
//					utility2+=count;
//					System.out.println(String.format("%d,%d R(2): %d", i,j,count));
//				}
//			}
//			
//			if(pieces[i][j].value==1)
//				return utility1;
//			else if(pieces[i][j].value==2)
//				return utility2;
//			return 0;
//		}
//		
//		public int CalculateUtility(){
//			int utility1=0;
//			int utility2=0;
//			for(int i=0;i<pieces.length;i++){
//				for(int j=0;j<pieces[0].length;j++){
//					int utility=CalculatePoint(i, j);
//					if(pieces[i][j].value==1)
//						utility1+=utility;
//					else if(pieces[i][j].value==2)
//						utility2+=utility;
//				}
//			}
//			return utility1-utility2;
//		}
//
//		public Point getPoint(int x,int y){
//			if (x < 0 || x >= pieces.length)
//				return new Point((byte) -1);
//			if  (y < 0 || y >= pieces[0].length)
//				return new Point((byte) -1);
//			return pieces[x][y];
//		}
//
//		public State(byte[][] pieces, boolean isMe, State parent){
//			this.pieces=new Point[pieces.length][pieces[0].length];
//			for(int i=0;i<pieces.length;i++){
//				for(int j=0;j<pieces[0].length;j++){
//					Point p = new Point((byte) 0);
//					p.value = pieces[i][j];
//					this.pieces[i][j] = p;
//				}
//			}
//			this.isMe=isMe;
//			this.parent=parent;
//		}
//	}
//
//
////	public static void main(String[] args) {
////		// TODO Auto-generated method stub
////		kLength=5;
////		byte[][] b={{1,1,1,1,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,0,0},{0,0,0,0,0}};
////		State state=new State(b, true, null);
////		//int n=state.CalculatePoint(0, 0);	
////		int n=state.CalculateUtility();
////		System.out.println(n);
////
////		//state.getPoint(10, 10).value=1;
////	}
//
//}
