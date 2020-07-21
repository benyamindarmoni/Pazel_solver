import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Read {
	static  boolean withTime;
	static String algName;
	static boolean withOpen;
	static	Set<Integer> black=new HashSet<Integer>();
	static Set<Integer> red=new HashSet<Integer>();
	static	Set<Integer> green=new HashSet<Integer>();
	static 	int numOfLines;
	static int numOfRows;
	static String input="input.txt";
	public void print() {
		System.out.println("withTime: "+withTime+" algName: "+algName+" withOpen: "+withOpen);
	}
	public void read_from_file( Board b) {
		try {
			FileReader fr = new FileReader(input);  
			BufferedReader br = new BufferedReader(fr); 
			algName = br.readLine(); 

			String tmp=br.readLine(); 

			if (tmp.equals("with time"))
				withTime=true;
			else
				withTime=false;
			tmp=br.readLine();
			if (tmp.equals("with open"))
				withOpen=true;
			else
				withOpen=false;

			tmp=br.readLine();
			String size[]=tmp.split("x");

			numOfLines=Integer.parseInt( size[0]);
			numOfRows=Integer.parseInt( size[1]);

			tmp=br.readLine();
			if(tmp.length()>6) {
				tmp=tmp.substring(7);
				size=tmp.split(",");
				for (int i = 0; i < size.length; i++) {
					black.add(Integer.parseInt(size[i]));
				}
			}
			tmp=br.readLine();
			if(tmp.length()>4)		{
				tmp=tmp.substring(5);
				size=tmp.split(",");
				for (int i = 0; i < size.length; i++) {
					red.add(Integer.parseInt(size[i]));
				}
			}
			int amount=numOfLines*numOfRows;
			for (int i = 1; i < amount; i++) 
				if(!(red.contains(i)||black.contains(i)))green.add(i);
			b.board=new int[numOfLines][numOfRows];
			tmp=br.readLine();
			int line=0;
			while(tmp!=null) {
				size=tmp.split(",");
				for(int i=0;i<size.length;i++) {
					if(!size[i].equals("_"))b.board[line][i]=Integer.parseInt(size[i]);
					else {
						b.line=line;
						b.row=i;
					}
				}
				tmp=br.readLine();
				line++;
			}
			br.close();
			fr.close();
		}
		catch(Exception e) {
			System.err.println("error!");
		}
	}
}
