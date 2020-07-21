import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class Solve {
	Board board;
	static int numOfNodes=1;

	public Solve(Board b) {
		board=b;
	}

	public void solve() {
		if(board.isGoal()) {
			Help.result(Help.getPath(board),1, board.price, 0); 
			return;
		}
		else if(board.notPossible()) {
			Help.result("no path",1,-1,0);
			return;
		}
		switch(Read.algName) 
		{
		case "BFS":
			BFS();
			break;
		case "A*":
			AStar();
			break;
		case "DFID":
			DFID();
			break;
		case "IDA*":
			IDAStar();
			break;
		case "DFBnB":
			DFBnB();
			break;
		}
	}

	private void DFBnB() {       
		long startTime=System.nanoTime();
		int threshold =Math.min(Integer.MAX_VALUE, Help.sumFactorials(Read.green.size()+Read.red.size()+1));
		//int threshold =Integer.MAX_VALUE;

		HashSet<Board> hash = new HashSet<Board>(); 
		Stack<Board> stck = new Stack<>();
		board.f=board.heuristic();
		stck.add(board);
		hash.add(board);
		String result = "";
		while(!stck.isEmpty()) {
			if(Read.withOpen) 
				Help.print(stck);

			Board b=stck.pop();
			if(b.out) {
				hash.remove(b);
			}

			else {
				b.out=true;
				stck.add(b);
				LinkedList<Board> n = new LinkedList <>();
				Help.pushToN(n,b);                      //pushing all operators to n!! 
				
				for(int i=0;i<n.size();i++) {
					Board g=n.get(i);
					if(g.f>=threshold) {						
						Help.removeRest(n,g);
					}
					else if(hash.contains(g)&& Help.isOut(g,hash)) {
						n.remove(g);
						i--;
					}
					else if(hash.contains(g)&&! Help.isOut(g,hash)) {
						if( Help.getB(g,hash).f<=g.f) {
							n.remove(g);
							i--;
						}
						else
						{
							stck.remove( Help.getB(g,hash));;
							hash.remove( Help.getB(g,hash));
						}
					}
					else if(g.isGoal()) {
						threshold=g.f;
						result= Help.getPath2(stck,g);	
						board.price=g.price;
						Help.removeRest(n,g);
					}
				}
				n= Help.rvrs(n);
				while(!n.isEmpty()) {
					hash.add(n.element());
					stck.add(n.poll());
				}
			}
		}
		long duration=System.nanoTime()-startTime;
		if(result.length()>0) {
			Help.result(result,numOfNodes, board.price, duration);	
		}
		else
			Help.result("no path",numOfNodes,-1,duration);
	}


	private void IDAStar() {
		long startTime=System.nanoTime();
		boolean finish = false;
		HashSet<Board> hash = new HashSet<Board>(); 
		Stack<Board> openList = new Stack<>();
		int threshold=board.heuristic()-1;

		while(threshold!=Integer.MAX_VALUE&&!finish) {
			threshold++;
			int minF=Integer.MAX_VALUE;
			board.out=false;
			openList.add(board);hash.add(board);
			while(!openList.isEmpty()&&!finish) {
				if(Read.withOpen) {
					Help.print(openList);
				}
				Board hlp=openList.pop();
				if(hlp.out)	{
					hash.remove(hlp);
				}
				else {
					hlp.out=true;
					openList.add(hlp);
					//for each operator
					for (int i = 0; i <4; i++) {
						Board l=null;

						switch(i) 
						{
						case 0:
							l=hlp.left();
							if(l==null)continue;
							break;

						case 1:
							l=hlp.up();
							if(l==null)continue;
							break;

						case 2:
							l=hlp.right();
							if(l==null)continue;
							break;
						case 3:
							l=hlp.down();
							if(l==null)continue;
							break;
						}
						l.f=l.price+l.heuristic();	
						if(l.f>threshold) {
							minF=Math.min(minF, l.f);
							continue;
						}
						if(hash.contains(l)&& Help.isOut(l,hash)) {
							continue;
						}
						if(hash.contains(l)&&!Help.isOut(l,hash)) {
							if( Help.getB(l,hash).f>l.f) {
								hash.remove(l);
								openList.remove(l);
							}
							else
								continue;
						}
						if(l.isGoal()) {
							long duration=System.nanoTime()-startTime;
							Help.result(Help.getPath(l).substring(1), numOfNodes, l.price, duration);
							finish=true;
							break;
						}
						hash.add(l);
						openList.add(l);
					}
				}
			}
			threshold=minF;
		}
		if(!finish) {
			long duration=System.nanoTime()-startTime;
			Help.result("no path", numOfNodes, -1, duration);
			finish=true;	
		}
	}

	private void AStar() {
		long startTime = System.nanoTime();
		boolean finish=false;
		PriorityQueue<Board> openList = new PriorityQueue <>();
		HashSet<Board> closedList = new HashSet<Board>(); 
		board.f=board.heuristic();
		openList.add(board);

		while (!openList.isEmpty()&&!finish) {
			if(Read.withOpen) {
				Help.print(openList);
			}
			Board curr=openList.poll();
			if(curr.isGoal()) {
				long duration=System.nanoTime()-startTime;
				Help.result(Help.getPath(curr).substring(1),numOfNodes,curr.price,duration);	
				finish=true;
				break;
			}
			closedList.add(curr);
			//operators!
			Board l=curr.left(); 
			Help.checkBoardAStar(l,closedList,openList);
			//operator up
			Board u=curr.up();
			Help.checkBoardAStar(u,closedList,openList);
			//operator right
			Board r=curr.right();
			Help.checkBoardAStar(r,closedList,openList);
			//operator down
			Board d=curr.down();
			Help.checkBoardAStar(d,closedList,openList);
		}
		if(!finish) {	
			long duration = System.nanoTime()-startTime;
			Help.result("no path",openList.size()+closedList.size(),-1,duration);
		}
	}


	private void DFID() {
		long startTime = System.nanoTime();
		HashSet<Board> h = new HashSet<Board>(); 
		boolean fail=false;
		boolean cutoff=false;
		int level=1;
		String rslt;

		while(true) {
			//recursive,no closedlist, with loopAvoidence-check if node exp is on branch or in stack, no act and opposite
			rslt=Limited_DFS(board,level,h,fail,cutoff);
			if(!rslt.equals("cutoff")&&!rslt.equals("fail")) {
				long duration=System.nanoTime()-startTime;
//				if(Help.getPath(board).length()==2) Help.result("no path",numOfNodes , -1, duration);
//				else  {	
					Help.result(Help.getPath(board).substring(1),numOfNodes ,board.price , duration);	
				//}
				break;
			}
			if(rslt.equals("fail")) {
				long duration=System.nanoTime()-startTime;
				Help.result("no path",numOfNodes , -1, duration);
				break;
			}
		
			level++;
			cutoff=false;
			fail=false;
		}
	}
	private String Limited_DFS(Board board, int limit, HashSet<Board> h, boolean fail, boolean cutoff) {
		if(board.isGoal()) {
			this.board=board;
			return "path";
		}
		else if(limit==0) {
			cutoff=true;
			return "cutoff";
		}
		else {
			if(Read.withOpen) 
				Help.print(h);
			h.add(board);
			cutoff=false;
			//for each operators
			Board l=board.left(); 	

			if(l!=null&&!h.contains(l)) {
				String rslt=Limited_DFS(l,limit-1 ,h,fail,cutoff);
				if(rslt.equals("cutoff"))cutoff=true;
				else if(rslt!="fail")return rslt;
			}
			//operator up
			Board u=board.up();
			if(u!=null&&!h.contains(u)) {
				String rslt=Limited_DFS(u,  limit-1, h,  fail,  cutoff);
				if(rslt.equals("cutoff"))cutoff=true;
				else if(!fail)return rslt;
			}

			//operator right

			Board r=board.right();	
			if(r!=null&&!h.contains(r)) {
				String rslt=Limited_DFS(r,  limit-1, h,  fail,  cutoff);
				if(rslt.equals("cutoff"))cutoff=true;
				else if(!fail)return rslt;
			}
			//operator down
			Board d=board.down();	
			if(d!=null&&!h.contains(d)) {
				String rslt=Limited_DFS(d,  limit-1, h,  fail,  cutoff);
				if(rslt.equals("cutoff"))cutoff=true;
				else if(!fail)return rslt;
			}
			h.remove(board);
			if(cutoff)return "cutoff";
			else {
				fail=true;
				return "fail";
			}
		}
	}
	private void BFS(){
		long startTime = System.nanoTime();
		boolean finish=false;
		Queue<Board> openList = new LinkedList <>();
		HashSet<Board> closedList = new HashSet<Board>(); 
		openList.add(board);

		//main loop
		while(!openList.isEmpty()&&!finish) {
			if(Read.withOpen) 
				 Help.	print(openList);
			Board hlp=openList.poll();		
			closedList.add(hlp);
			//operators!
			Board l=hlp.left(); 
			if(l!=null)
				finish= Help.checkBoardBfs(l,closedList,openList,finish,startTime);
			//operator up
			Board u=hlp.up(); 
			if(u!=null)
				finish= Help.checkBoardBfs(u,closedList,openList,finish,startTime);
			//operator right
			Board r=hlp.right();
			if(r!=null)	
				finish= Help.checkBoardBfs(r,closedList,openList,finish,startTime);
			//operator down
			Board d=hlp.down();
			if(d!=null)
				finish= Help.checkBoardBfs(d,closedList,openList,finish,startTime);

		}
		if(!finish) {	
			long Time = System.nanoTime()-startTime;
			 Help.result("no path",openList.size()+closedList.size(),-1,Time);	
		}
	}
}