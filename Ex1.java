
public class Ex1 {
	public static void main(String[] args) {
				Read r=new Read();
				Board b=new Board();
				r.read_from_file(b);
				Solve s=new Solve(b);
				s.solve();
	}
}
