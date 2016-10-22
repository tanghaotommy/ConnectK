import connectK.CKPlayer;
import connectK.BoardModel;

import java.awt.Point;
import java.util.ArrayList;

import com.sun.xml.internal.ws.wsdl.parser.InaccessibleWSDLException;

public class SparkAI extends CKPlayer {
	static int MAXDEPTH = 1;

	int currentAIPlayer=this.player;

	class MyPoint {
		public byte value;
		public boolean[] directions = { false, false, false, false };
		public int x;
		public int y;

		public MyPoint(byte value) {
			// TODO Auto-generated constructor stub
			this.value = value;
		}
	}

	public class Action {
		public MyPoint point;
		public int value = 0;

		public Action(MyPoint point) {
			this.point = point;
		}
	}

	public static int kLength = 0;

	class State {
		public static final int VERTICAL = 0;
		public static final int HORIZONTAL = 1;
		public static final int LEFTDOWN = 2;
		public static final int RIGHTDOWN = 3;

		public MyPoint[][] pieces; // [column][row] 1, 2, or null for empty
		public int alpha = Integer.MIN_VALUE;
		public int beta = Integer.MAX_VALUE;
		public int value;
		public State parent;
		public int depth;
		public int player;
		public Action action;
		public Action currentBestAction;
		public ArrayList<State> successors;

		public void ClearDirections (){
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[0].length; j++) {
					pieces[i][j].directions[VERTICAL] = false;
					pieces[i][j].directions[HORIZONTAL] = false;
					pieces[i][j].directions[LEFTDOWN] = false;
					pieces[i][j].directions[RIGHTDOWN] = false;
				}
			}
		} 
		public int CalculatePoint(int i, int j) {
			int utility1 = 0;
			int utility2 = 0;
			if (pieces[i][j].value == 1) {
				if (pieces[i][j].directions[VERTICAL] == false) {
					int count = 1;
					int step = 1;
					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j).value == 1) { // next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j).directions[VERTICAL] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j).value == 2
								|| getPoint(i + k, j).value == -1) {
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j).value == 2
										|| getPoint(i - k2, j).value == -1) {// meet
									// enemy,
									// check
									// last
									// space
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j).value == 2
										|| getPoint(i + k + k2, j).value == -1) { // meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j).value == 2
										|| getPoint(i - 1, j).value == -1) { // check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j).value == 2
										|| getPoint(i - k3, j).value == -1) { // check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility1 += count;
					//					System.out.println(String.format("%d,%d V(1): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[HORIZONTAL] == false) {
					int count = 1;
					int step = 1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i, j + k).value == 1) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i, j + k).directions[HORIZONTAL] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i, j + k).value == 2
								|| getPoint(i, j + k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space changed
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i, j - k2).value == 2
										|| getPoint(i, j - k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i, j + k + k2).value == 2
										|| getPoint(i, j + k + k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i, j - 1).value == 2
										|| getPoint(i, j - 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i, j - k3).value == 2
										|| getPoint(i, j - k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility1 += count;
					//					System.out.println(String.format("%d,%d H(1): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[LEFTDOWN] == false) {
					int count = 1;
					int step=1;
					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j + k).value == 1) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j + k).directions[LEFTDOWN] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j + k).value == 2
								|| getPoint(i + k, j + k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space
							count/=2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j - k2).value == 2
										|| getPoint(i - k2, j - k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j + k + k2).value == 2
										|| getPoint(i + k + k2, j + k + k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j - 1).value == 2
										|| getPoint(i - 1, j - 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j - k3).value == 2
										|| getPoint(i - k3, j - k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility1 += count;
					//					System.out.println(String.format("%d,%d L(1): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[RIGHTDOWN] == false) {
					int count = 1;
					int step=1;
					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j - k).value == 1) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j - k).directions[RIGHTDOWN] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j - k).value == 2
								|| getPoint(i + k, j - k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space
							count/=2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j + k2).value == 2
										|| getPoint(i - k2, j + k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j - k - k2).value == 2
										|| getPoint(i + k + k2, j - k - k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j + 1).value == 2
										|| getPoint(i - 1, j + 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j + k3).value == 2
										|| getPoint(i - k3, j + k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility1 += count;
					//					System.out.println(String.format("%d,%d R(1): %d", i, j,
					//							count));
				}

			} else if (pieces[i][j].value == 2) {
				if (pieces[i][j].directions[VERTICAL] == false) {
					int count = 1;
					int step=1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j).value == 2) { // next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j).directions[VERTICAL] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j).value == 1
								|| getPoint(i + k, j).value == -1) {
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j).value == 1
										|| getPoint(i + k, j).value == -1) {// meet
									// enemy,
									// check
									// last
									// space
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j).value == 1
										|| getPoint(i + k + k2, j).value == -1) { // meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j).value == 1
										|| getPoint(i - 1, j).value == -1) { // check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j).value == 1
										|| getPoint(i - k3, j).value == -1) { // check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility2 += count;
					//					System.out.println(String.format("%d,%d V(2): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[HORIZONTAL] == false) {
					int count = 1;
					int step=1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i, j + k).value == 2) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i, j + k).directions[HORIZONTAL] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i, j + k).value == 1
								|| getPoint(i, j + k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i, j - k2).value == 1
										|| getPoint(i, j - k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i, j + k + k2).value == 1
										|| getPoint(i, j + k + k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i, j - 1).value == 1
										|| getPoint(i, j - 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i, j - k3).value == 1
										|| getPoint(i, j - k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility2 += count;
					//					System.out.println(String.format("%d,%d H(2): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[LEFTDOWN] == false) {
					int count = 1;
					int step=1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j + k).value == 2) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j + k).directions[LEFTDOWN] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j + k).value == 1
								|| getPoint(i + k, j + k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space
							count/=2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j - k2).value == 1
										|| getPoint(i - k2, j - k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j + k + k2).value == 1
										|| getPoint(i + k + k2, j + k + k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j - 1).value == 1
										|| getPoint(i - 1, j - 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j - k3).value == 1
										|| getPoint(i - k3, j - k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility2 += count;
					//					System.out.println(String.format("%d,%d L(2): %d", i, j,
					//							count));
				}
				if (pieces[i][j].directions[RIGHTDOWN] == false) {
					int count = 1;
					int step=1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j - k).value == 2) {// next is ours
							count++;
							step++;
							count*=count;
							getPoint(i + k, j - k).directions[RIGHTDOWN] = true;
							if (step == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j - k).value == 1
								|| getPoint(i + k, j - k).value == -1) {// meet
							// enemy,
							// check
							// last
							// space
							count/=2;
							for (int k2 = 1; k2 <= kLength - k; k2++) {
								if (getPoint(i - k2, j + k2).value == 1
										|| getPoint(i - k2, j + k2).value == -1) {
									count = 0;
									break;
								}
							}
							break;
						} else {
							int k2 = 1;
							for (; k2 <= kLength - k - 1; k2++) {
								if (getPoint(i + k + k2, j - k - k2).value == 1
										|| getPoint(i + k + k2, j - k - k2).value == -1) {// meet
									// enemy,
									// k2
									// is
									// the
									// position
									// of
									// enemy
									break;
								}
							}
							if (k2 == kLength - k) {
								if (getPoint(i - 1, j + 1).value == 1
										|| getPoint(i - 1, j + 1).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count /= 2;
									break;
								}
							}
							for (int k3 = 1; k3 <= kLength - k - k2; k3++) {
								if (getPoint(i - k3, j + k3).value == 1
										|| getPoint(i - k3, j + k3).value == -1) {// check
									// if
									// there
									// is
									// enough
									// space
									count = 0;
									break;
								}
							}
						}
					}
					utility2 += count;
					//					System.out.println(String.format("%d,%d R(2): %d", i, j,
					//							count));
				}
			}

			if (pieces[i][j].value == 1)
				return utility1;
			else if (pieces[i][j].value == 2)
				return utility2;
			return 0;
		}

		public int CalculateUtility() {
			int utility1 = 0;
			int utility2 = 0;
			boolean over=false;
			for (int i = 0; i < pieces.length; i++) {
				if(over)
					break;
				for (int j = 0; j < pieces[0].length; j++) {
					int utility = CalculatePoint(i, j);
					if(utility==Integer.MAX_VALUE-1){
						over=true;
						if (pieces[i][j].value == 1) {
							utility1 = utility;
							utility2 = 0;
							break;
						}
						else if (pieces[i][j].value == 2) {
							utility2 = utility;
							utility1 = 0;
							break;
						}
					}
					else{
						if (pieces[i][j].value == 1)
							utility1 += utility;
						else if (pieces[i][j].value == 2)
							utility2 += utility;
					}		
				}
			}
			//System.out.println(new String().format("u1= %d,  u2=%d",utility1,utility2));

			if (currentAIPlayer == 1) {
				this.value = utility1 - utility2;		
			}
			else {
				this.value = utility2 - utility1 ;
			}
			return this.value;
		}

		public MyPoint getPoint(int x, int y) {
			if (x < 0 || x >= pieces.length)
				return new MyPoint((byte) -1);
			if (y < 0 || y >= pieces[0].length)
				return new MyPoint((byte) -1);
			return pieces[x][y];
		}
		
		public int CalculateNumber (int directions, int player, int i, int j, boolean[][] visited){
			int xCo = 0, yCo = 0;
			switch (directions) {
			case HORIZONTAL:
				xCo = 0; yCo = 1;
				break;
			case VERTICAL:
				xCo = 1; yCo = 0;
				break;
			case LEFTDOWN:
				xCo = 1; yCo = 1;
				break;
			case RIGHTDOWN:
				xCo = 1; yCo = -1;
				break;
			}
			int step = 1;
			for (int k = 1; k <= kLength - 1; k++) {
				if (getPoint(i + xCo*k, j + yCo*k).value == player) { // next is ours
					step++;
					getPoint(i + xCo*k, j + yCo*k).directions[directions] = true;
				} else break;
			}
			
			if (step >= kLength - 2) {
				if (getPoint(i - xCo, j - yCo).value == 0) {
					visited[i-xCo][j-yCo]=true;
					int nextPlayer = 0;
					if (player == 1)
						nextPlayer = 2;
					else if (player == 2)
						nextPlayer = 1;
					State state = new State(pieces, nextPlayer, this);
					state.getPoint(i-xCo, j-yCo).value = (byte) player;
					state.depth = this.depth + 1;
					state.alpha = this.alpha;
					state.beta = this.beta;
					Action action = new Action(new MyPoint(
							(byte) nextPlayer));
					action.point.x = i-xCo;
					action.point.y = j-yCo;
					state.action = action;
					successors.add(state);
				}
				if (getPoint(i + xCo*step, j + yCo*step).value == 0) {
					visited[i + xCo*step][j + yCo*step]=true;
					int nextPlayer = 0;
					if (player == 1)
						nextPlayer = 2;
					else if (player == 2)
						nextPlayer = 1;
					State state = new State(pieces, nextPlayer, this);
					state.getPoint(i + xCo*step, j + yCo*step).value = (byte) player;
					state.depth = this.depth + 1;
					state.alpha = this.alpha;
					state.beta = this.beta;
					Action action = new Action(new MyPoint(
							(byte) nextPlayer));
					action.point.x = i + xCo*step;
					action.point.y = j + yCo*step;
					state.action = action;
					successors.add(state);
				}
			}
				
			return step;
		}

		public void GenerateSuccessor() {
			if(successors == null) {
				successors=new ArrayList<>();

				//Calculate for range
				int iMin=pieces.length;
				int iMax=-1;
				int jMin=pieces[0].length;
				int jMax=-1;
				int count=0;
				boolean[][] visited=new boolean[pieces.length][pieces[0].length];
				for (int i = 0; i < pieces.length; i++) {
					for (int j = 0; j < pieces[0].length; j++) {
						if (getPoint(i, j).value == 1 || getPoint(i, j).value == 2) {
							if(i>iMax)
								iMax=i;
							if(i<iMin)
								iMin=i;
							if(j>jMax)
								jMax=j;
							if(j<jMin)
								jMin=j;
							count++;
							if (pieces[i][j].directions[VERTICAL] == false ) {
								pieces[i][j].directions[VERTICAL] = true;
								CalculateNumber(VERTICAL, getPoint(i, j).value, i, j,visited);
							}
							if (pieces[i][j].directions[HORIZONTAL] == false ) {
								pieces[i][j].directions[HORIZONTAL] = true;
								CalculateNumber(HORIZONTAL, getPoint(i, j).value, i, j,visited);
							}
							if (pieces[i][j].directions[LEFTDOWN] == false ) {
								pieces[i][j].directions[LEFTDOWN] = true;
								CalculateNumber(LEFTDOWN, getPoint(i, j).value, i, j,visited);
							}
							if (pieces[i][j].directions[RIGHTDOWN] == false ) {
								pieces[i][j].directions[RIGHTDOWN] = true;
								CalculateNumber(RIGHTDOWN, getPoint(i, j).value, i, j,visited);
							}
						}
					}
				}
				if(iMin==pieces.length && iMax==-1 && jMin==pieces[0].length && jMax==-1){
					iMax=pieces.length/2;
					iMin=pieces.length/2-1;
					jMax=pieces[0].length/2;
					jMin=pieces[0].length/2-1;
				}

				for (int i = iMin; i <=iMax; i++) {
					for (int j = jMin; j <=jMax; j++) {
						if (getPoint(i, j).value == 0) {
							visited[i][j]=true;
							int nextPlayer = 0;
							if (player == 1)
								nextPlayer = 2;
							else if (player == 2)
								nextPlayer = 1;
							State state = new State(pieces, nextPlayer, this);
							state.getPoint(i, j).value = (byte) player;
							state.depth = this.depth + 1;
							state.alpha = this.alpha;
							state.beta = this.beta;
							Action action = new Action(new MyPoint(
									(byte) nextPlayer));
							action.point.x = i;
							action.point.y = j;
							state.action = action;
							successors.add(state);
							//System.out.println(new String().format("depth:%d, action:(%d, %d)",this.depth, i, j));
						}
					}
				}

				int addition=0;
				if(depth<=3)
					addition=1;
				else 
					addition=1;
				if((double)count/(iMax-iMin)/(jMax-jMin)<0.5)
					addition=1;
				iMax = iMax + addition;
				iMax = (iMax > pieces.length-1)?pieces.length-1:iMax;
				iMin = iMin - addition;
				iMin = (iMin < 0)?0:iMin;
				jMax = jMax + addition;
				jMax = (jMax > pieces[0].length-1)?pieces[0].length-1:jMax;
				jMin = jMin - addition;
				jMin = (jMin < 0)?0:jMin;

				for (int i = iMin; i <=iMax; i++) {
					for (int j = jMin; j <=jMax; j++) {
						if (visited[i][j]==false && getPoint(i, j).value == 0) {
							visited[i][j]=true;
							int nextPlayer = 0;
							if (player == 1)
								nextPlayer = 2;
							else if (player == 2)
								nextPlayer = 1;
							State state = new State(pieces, nextPlayer, this);
							state.getPoint(i, j).value = (byte) player;
							state.depth = this.depth + 1;
							state.alpha = this.alpha;
							state.beta = this.beta;
							Action action = new Action(new MyPoint(
									(byte) nextPlayer));
							action.point.x = i;
							action.point.y = j;
							state.action = action;
							successors.add(state);
							//System.out.println(new String().format("depth:%d, action:(%d, %d)",this.depth, i, j));
						}
					}
				}

			}
		}

		public State(MyPoint[][] pieces, int player, State parent) {
			this.player = player;
			this.parent = parent;
			this.pieces = new MyPoint[pieces.length][pieces[0].length];
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[0].length; j++) {
					MyPoint p = new MyPoint((byte) 0);
					p.value = pieces[i][j].value;
					this.pieces[i][j] = p;
				}
			}
		}

		public State(byte[][] pieces, int player, State parent) {
			this.pieces = new MyPoint[pieces.length][pieces[0].length];
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[0].length; j++) {
					MyPoint p = new MyPoint((byte) 0);
					p.value = pieces[i][j];
					this.pieces[i][j] = p;
				}
			}
			this.player = player;
			this.parent = parent;
			this.depth=0;
			GenerateSuccessor();
		}
	}

	public Action Alpha_Beta_Search(State state) {
		int v = Max_Value(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//System.out.println("Max_Value " + v);
		for (State successor : state.successors) {
			if (successor.value == v) {
				state2.value = v;
				state2.currentBestAction = successor.action;
				return successor.action;
			}
		}
		return null;
	}

	public boolean TerminalTest(State state) {
		if (state.depth == MAXDEPTH)
			return true;
		else
			return false;
	}

	public int Max_Value(State state, int alpha, int beta) {
		if (TerminalTest(state))
			return state.CalculateUtility();
		state.value = Integer.MIN_VALUE;
		//System.out.println(new String().format("GenerateSuccessor in Max_Value Depth:%d", state.depth));
		state.GenerateSuccessor();
		for (State s : state.successors) {
			if(System.currentTimeMillis()-startTime>=deadline-300){
				Action action = null;
				if (state3.alpha!=Integer.MIN_VALUE) {
					for (State successor : state3.successors) {
						if (successor.value == state3.value) {
							action = successor.action;
							break;
						}
					}
				} else {
					action = state2.currentBestAction;
				}

				throw new JumpOutMsg(action);
			}
			int m = Min_Value(s, alpha, beta);
			state.value = m > state.value ? m : state.value;
			if (state.value >= beta) {
				//System.out.println("Pruned!");
				return state.value;
			}
			alpha = alpha > state.value ? alpha : state.value;
		}
		return state.value;
	}

	public int Min_Value(State state, int alpha, int beta) {
		if (TerminalTest(state))
			return state.CalculateUtility();
		state.value = Integer.MAX_VALUE;
		//System.out.println(new String().format("GenerateSuccessor in Min_Value Depth:%d", state.depth));
		state.GenerateSuccessor();
		for (State s : state.successors) {
			if(System.currentTimeMillis()-startTime>=deadline-300){
				Action action = null;
				if (state3.alpha!=Integer.MIN_VALUE) {
					for (State successor : state3.successors) {
						if (successor.value == state3.value) {
							action = successor.action;
							break;
						}
					}
				} else {
					action = state2.currentBestAction;
				}
				throw new JumpOutMsg(action);
			}
			int m = Max_Value(s, alpha, beta);
			state.value = m < state.value ? m : state.value;
			if (state.value <= alpha) {
				//System.out.println("Pruned!");
				return state.value;
			}
			beta = beta < state.value ? beta : state.value;
		}
		return state.value;
	}

	public SparkAI(byte player, BoardModel state) {
		super(player, state);
		teamName = "SparkAI";
		kLength = state.kLength;
	}

	class JumpOutMsg extends RuntimeException {
		Action action;

		JumpOutMsg(Action action){
			this.action=action;
		}
	}

	long startTime=0;
	State state2;
	State state3;
	long deadline=5000;
	@Override
	public Point getMove(BoardModel state) {
		// for (int i = 0; i < state.getWidth(); ++i)
		// for (int j = 0; j < state.getHeight(); ++j)
		// if (state.getSpace(i, j) == 0)
		// return new Point(i, j);
		// return null;
		MAXDEPTH = 1;
		startTime=System.currentTimeMillis();
		state2 = new State(state.pieces, currentAIPlayer, null);
		MyPoint myPoint=null;
		try {
			for(int i=1;i<10;i++){
				MAXDEPTH=i;
				System.out.println("Current depth:" + MAXDEPTH);
				state3 = new State(state.pieces, currentAIPlayer, null);
				myPoint = Alpha_Beta_Search(state3).point;
			}
		} catch (JumpOutMsg e) {
			myPoint = e.action.point;
		}
		//System.out.println("x = "+myPoint.x+" y = "+myPoint.y);
		return new Point(myPoint.x, myPoint.y);
	}

	@Override
	public Point getMove(BoardModel state, int deadline) {
		return getMove(state);
	}

}
