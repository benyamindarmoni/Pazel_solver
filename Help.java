import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Help {
	static void pushToN(LinkedList<Board> n, Board b) {

		for (int i = 0; i <4; i++) {
			Board tmp=null;
			switch(i) 
			{
			case 0:
				tmp=b.left();
				break;
			case 1:
				tmp=b.up();
				break;
			case 2:
				tmp=b.right();
				break;
			case 3:
				tmp=b.down();
				break;
			}
			if(tmp!=null) {
				tmp.f=tmp.price+tmp.heuristic();
				n.add(tmp);
			}
		}
		n.sort(new Comparator<Board>() {
			@Override
			public int compare(Board o1, Board o2) {
				if(o1.f>o2.f)return 1;
				else if(o1.f<o2.f)return -1;
				else return 0;
			}
		});

	}

	static Board getB(Board l, Iterable<Board> hash) {
		for(Board b:hash) {
			if(b.equals(l))return b;
		}
		return null;
	}

	static boolean isOut(Board l, HashSet<Board> hash) {
		for(Board b:hash) {
			if(b.equals(l)&&b.out)return true;
		}
		return false;
	}
	static void checkBoardAStar(Board l, HashSet<Board> closedList, PriorityQueue<Board> openList) {
		if(l!=null) {
			l.f=l.price+l.heuristic();
			if(!closedList.contains(l)&&!openList.contains(l)) 
				openList.add(l);

			else if(openList.contains(l)&&getB(l,openList).f>l.f){
				openList.remove(l);
				openList.add(l);
			}
		}
	}
	static boolean checkBoardBfs(Board l, HashSet<Board> closedList, Queue<Board> openList,boolean finish,long startTime) {
		if(l!=null&&!closedList.contains(l)&&!openList.contains(l)) {
			if(l.isGoal()) {
				long duration=System.nanoTime()-startTime;
				result(getPath(l).substring(1),Solve.numOfNodes,l.price,duration);	
				finish=true;
			}
		
				openList.add(l);
		}
		return finish;
	}
	static String getPath(Board b) {
		String ans="";
		while(b.last!='n') {
			ans=   "-"+b.lastN+b.last+ans;
			b=b.ptr;
		}
		return ans;
	}
	static LinkedList<Board> rvrs(LinkedList<Board> n) {
		LinkedList<Board>  hlp=new LinkedList<Board>();
		for (int i = n.size()-1; i >=0 ; i--) {
			hlp.add(n.get(i));
		}

		return hlp;
	}

	static void removeRest(LinkedList<Board> n, Board hlp) {
		LinkedList<Board> n2=new LinkedList<Board>();
		for(int i=0;i<n.size();i++) {
			if(n.get(i).equals(hlp)) {
				break;
			}
			else
				n2.add(n.get(i));
		}
		n.clear();
		for (int i = 0; i <n2.size(); i++) {
			n.add(n2.get(i));
		}
	}

	static void print(Iterable<Board> openList) {
		// TODO Auto-generated method stub
		for(Board s : openList) { 
			s.print(); 
		}
	}
	static String getPath(Iterable<Board> openList) {
		// TODO Auto-generated method stub
		String ans="";
		for(Board s : openList) { 

			switch(s.last) 
			{
			case 'U':
				ans+=s.board[s.line-1][s.row];
				break;
			case 'D':
				ans+=s.board[s.line+1][s.row];
				break;
			case 'L':
				ans+=	s.board[s.line][s.row-1];
				break;
			case 'R':
				ans+=s.board[s.line][s.row+1];
				break;

			}
			ans+=s.last+"-";
		}
		ans=ans.substring(2,ans.length()-1);
		return ans;
	}

	static String getPath2(Iterable<Board> stck, Board hlp) {
		// TODO Auto-generated method stub
		Stack<Board> tmp = new Stack<>();
		for(Board b : stck) {
			if(b.out)tmp.add(b);
		}
		tmp.add(hlp);
		return getPath(tmp);
	}
	static void result(String path, int i, int price, long duration) {//results!!
		// TODO Auto-generated method stub
		File file = new File("output.txt");
		//Create the file
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Write Content
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write( path+"\n");
			writer.write("Num: "+i+"\n");
			if(price!=-1)
				writer.write("Cost: "+price+"\n");
			double t=(double) duration / 1_000_000_000;
			if(Read.withTime)writer.write(new DecimalFormat("#.###").format(t)+" seconds"+"\n");
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();		
		}
	}
	static int sumFactorials(int factorial)
	{
		int results = 1;
		for(int i = 1; i <= factorial; i++)
			results = results * i;
		return results;
	}
}
