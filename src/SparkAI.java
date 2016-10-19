import connectK.CKPlayer;
import connectK.BoardModel;

import java.awt.Point;
import java.util.ArrayList;

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
		public ArrayList<State> successors;

		public int CalculatePoint(int i, int j) {
			int utility1 = 0;
			int utility2 = 0;
			if (pieces[i][j].value == 1) {
				if (pieces[i][j].directions[VERTICAL] == false) {
					int count = 1;

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j).value == 1) { // next is ours
							count++;
							getPoint(i + k, j).directions[VERTICAL] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j).value == 2
								|| getPoint(i + k, j).value == -1) {
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i, j + k).value == 1) {// next is ours
							count++;
							getPoint(i, j + k).directions[HORIZONTAL] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i, j + k).value == 2
								|| getPoint(i, j + k).value == -1) {// meet
																	// enemy,
																	// check
																	// last
																	// space
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j + k).value == 1) {// next is ours
							count++;
							getPoint(i + k, j + k).directions[LEFTDOWN] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j + k).value == 2
								|| getPoint(i + k, j + k).value == -1) {// meet
																		// enemy,
																		// check
																		// last
																		// space
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j - k).value == 1) {// next is ours
							count++;
							getPoint(i + k, j - k).directions[RIGHTDOWN] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j - k).value == 2
								|| getPoint(i + k, j - k).value == -1) {// meet
																		// enemy,
																		// check
																		// last
																		// space
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j).value == 2) { // next is ours
							count++;
							getPoint(i + k, j).directions[VERTICAL] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j).value == 1
								|| getPoint(i + k, j).value == -1) {
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i, j + k).value == 2) {// next is ours
							count++;
							getPoint(i, j + k).directions[HORIZONTAL] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i, j + k).value == 1
								|| getPoint(i, j + k).value == -1) {// meet
																	// enemy,
																	// check
																	// last
																	// space
							count /= 2;
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j + k).value == 2) {// next is ours
							count++;
							getPoint(i + k, j + k).directions[LEFTDOWN] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j + k).value == 1
								|| getPoint(i + k, j + k).value == -1) {// meet
																		// enemy,
																		// check
																		// last
																		// space
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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

					for (int k = 1; k <= kLength - 1; k++) {
						if (getPoint(i + k, j - k).value == 2) {// next is ours
							count++;
							getPoint(i + k, j - k).directions[RIGHTDOWN] = true;
							if (count == kLength)
								return Integer.MAX_VALUE - 1;
						} else if (getPoint(i + k, j - k).value == 1
								|| getPoint(i + k, j - k).value == -1) {// meet
																		// enemy,
																		// check
																		// last
																		// space
							for (int k2 = 1; k2 <= kLength - k - 1; k2++) {
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
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[0].length; j++) {
					int utility = CalculatePoint(i, j);
					if (pieces[i][j].value == 1)
						utility1 += utility;
					else if (pieces[i][j].value == 2)
						utility2 += utility;
				}
			}
			System.out.println(new String().format("u1= %d,  u2=%d",utility1,utility2));
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

		public void GenerateSuccessor() {
			if(successors == null) {
				successors=new ArrayList<>();
				for (int i = 0; i < pieces.length; i++) {
					for (int j = 0; j < pieces[0].length; j++) {
						if (getPoint(i, j).value == 0) {
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
							System.out.println(new String().format("depth:%d, action:(%d, %d)",this.depth, i, j));
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
			GenerateSuccessor();
		}
	}

	public Action Alpha_Beta_Search(State state) {
		int v = Max_Value(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println("Max_Value " + v);
		for (State successor : state.successors) {
			if (successor.value == v) {
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
		System.out.println(new String().format("GenerateSuccessor in Max_Value Depth:%d", state.depth));
		state.GenerateSuccessor();
		for (State s : state.successors) {
			int m = Min_Value(s, alpha, beta);
			state.value = m > state.value ? m : state.value;
			if (state.value >= beta) {
				System.out.println("Pruned!");
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
		System.out.println(new String().format("GenerateSuccessor in Min_Value Depth:%d", state.depth));
		state.GenerateSuccessor();
		for (State s : state.successors) {
			int m = Max_Value(s, alpha, beta);
			state.value = m < state.value ? m : state.value;
			if (state.value <= alpha) {
				System.out.println("Pruned!");
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

	@Override
	public Point getMove(BoardModel state) {
		// for (int i = 0; i < state.getWidth(); ++i)
		// for (int j = 0; j < state.getHeight(); ++j)
		// if (state.getSpace(i, j) == 0)
		// return new Point(i, j);
		// return null;
			MAXDEPTH = 3;
			State state2 = new State(state.pieces, currentAIPlayer, null);
			MyPoint myPoint = Alpha_Beta_Search(state2).point;
			return new Point(myPoint.x, myPoint.y);
	}

	@Override
	public Point getMove(BoardModel state, int deadline) {
		return getMove(state);
	}

}
