package util;

public class GerarString {

	public GerarString() { 
	} 


	public static void main(String[] args) { 

		String[] senhas = new String[50000];
		//String[] carct ={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
		String[] carct ={"0","1"}; 

		String senha=""; 

		for (int i = 0; i < 30; i++) {
			int a = 20;
			//int a = (int) (Math.random()*20);
			if (a == 0) {
				a = 2;
			}
			for (int x=0; x<a && a !=0; x++){ 
				int j = (int) (Math.random()*carct.length); 
				senha += carct[j]; 
			}

			System.out.println(senha);
			senhas[i] = senha;
			senha = "";
		}


	} 

} 



