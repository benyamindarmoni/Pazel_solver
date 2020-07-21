import java.util.Objects;
public class Board implements Comparable<Board>{
	int board[][];
	int line; 
	int row;
	int price=0;                  //actual price until here
	int f=0;                     //price+heuristic
	char last='n';
	int lastN=0;
	boolean out=false;
	Board ptr;
	
	public Board() {}
	public Board(int [][] b, int l1,int r1, int prc,char c,Board ptr,int lastN) {
		price=prc;
		board=b;
		line=l1;
		row=r1;
		last=c;
		this.ptr=ptr;
		this.lastN=lastN;
	}
	public boolean isGoal() {
		int err=0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j]!=(board[0].length)*(i)+j+1)err++;
			}
		}
		return err==1;
	}

	public Board left() {
		if(row==Read.numOfRows-1||last=='R')return null;
		else {
			int num=board[line][row+1];
			if(Read.black.contains(num))return null;
			else if(Read.red.contains(num)) {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line,row+1),line,row+1,price+30,'L',this,num);
			}
			else {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line,row+1),line,row+1,price+1,'L',this,num);
			}
		}
	}

	public Board up() {
		if(line==Read.numOfLines-1||last=='D')return null;
		else {
			int num=board[line+1][row];
			if(Read.black.contains(num))return null;
			else if(Read.red.contains(num)) {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line+1,row),line+1,row,price+30,'U',this,num);
			}
			else {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line+1,row),line+1,row,price+1,'U',this,num);
			}
		}
	}

	public Board down() {
		if(line==0||last=='U')return null;
		else {
			int num=board[line-1][row];
			if(Read.black.contains(num))return null;
			else if(Read.red.contains(num)) {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line-1,row),line-1,row,price+30,'D',this,num);
			}

			else {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line-1,row),line-1,row,price+1,'D',this,num);

			}
		}
	}

	public Board right() {
		if(row==0||last=='L')return null;
		else {
			int num=board[line][row-1];
			if(Read.black.contains(num))return null;
			else if(Read.red.contains(num)) {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line,row-1),line,row-1,price+30,'R',this,num);
			}
			else {
				Solve.numOfNodes++;
				return new Board(matrix(board,line,row,line,row-1),line,row-1,price+1,'R',this,num);
			}
		}
	}

	@Override
	public int compareTo(Board emp) {
		if(this.f > emp.f) {
			return 1;
		} else if (this.f <= emp.f) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object emp) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j]!=((Board)emp).board[i][j])return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash();
	}

	public void print() {
		System.out.println("price = "+price);
		System.out.println("f = "+f);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("---------");
	}

	private int[][] matrix(int[][] board2, int l2, int r2,int l3,int r3) {
		int ans[][]= new int [board2.length][board2[0].length];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < ans[0].length; j++) {
				ans[i][j]=board2[i][j];
			}
		}
		int tmp=ans[l2][r2];
		ans[l2][r2]=ans[l3][r3];
		ans[l3][r3]=tmp;
		return ans;
	}

	public int heuristic() {
		int sum=0;
		//sum the distance of each elem from his correct place 
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				sum+=distance(board[i][j]-1,board[0].length,i,j);
			}
		}
		return sum;
	}

	private int distance(int num, int length, int i, int j) {
		if(num==-1) 
			return 0;
		int cost=1;
		if(Read.red.contains(num+1))cost=30;
		else if(Read.black.contains(num+1))cost=Integer.MAX_VALUE;
		int ans=(Math.abs((num/length) - i)+Math.abs((num%length)-j))*cost;
		return ans;
	}

	public boolean notPossible() {
		int c=1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j]!=c&&Read.black.contains(board[i][j]))return true;
				c++;	
			}
		}
		return false;
	}
}
