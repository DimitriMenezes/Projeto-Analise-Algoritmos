package kmp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SistemaDeBusca {

	public static void main(String[] args) {
		String fileIn = args[0];
		String fileOut= args[1];

		try{
			FileReader arquivo = new FileReader(fileIn);
			BufferedReader lerArq = new BufferedReader(arquivo);
			String linha = lerArq.readLine();
			int quantidadeDeCadeias = Integer.parseInt(linha);
			//System.out.println(quantidadeDeCadeias);
			Cadeia[] cadeias = new Cadeia[quantidadeDeCadeias];

			int i = 0;
			while (linha != null && i<= cadeias.length-1) {
				linha = lerArq.readLine();
				if(linha != null){
					cadeias[i] = new Cadeia(linha);

					i = i+1;
				}
			}

			String linha2 = lerArq.readLine();
			int quantidadeDePadrao = Integer.parseInt(linha2);
			//System.out.println(quantidadeDePadrao);
			Padrao[] padrao = new Padrao[quantidadeDePadrao];

			i = 0;
			while (linha != null && i<= padrao.length-1) {
				linha = lerArq.readLine();
				if(linha != null){
					padrao[i] = new Padrao(linha); 

					i = i+1;
				}
			}
			//geração do arquivo OUT
			File dir = new File(fileOut);

			if( !dir.exists() )
				dir.createNewFile();
			FileWriter fw = new FileWriter(dir,true);
			StringBuffer stringbu = new StringBuffer("");

			for (int j = 0; j < padrao.length; j++) {
				for (int j2 = 0; j2 < cadeias.length; j2++) {
					Padrao.cadeias = j2;
					Padrao.KMP(cadeias[j2].getVetor(), padrao[j].getVetorPadrao());
				} 

				stringbu.append("[" + padrao[j].getPadrao() + "]");
				if (Padrao.contador > 1) {
					//char ch = '\u0000';
					Padrao.stringbuffer.deleteCharAt(Padrao.stringbuffer.toString().length()-1);
					//Padrao.stringbuffer.setCharAt(Padrao.stringbuffer.toString().length()-1, ch);
					if (Padrao.y > 1) {
						stringbu.append(" " + Padrao.contador + " " + "resultados nas cadeias" + Padrao.stringbuffer.toString());
					}
					else if (Padrao.y == 1){
						stringbu.append(" " + Padrao.contador + " " + "resultados na cadeia" + Padrao.stringbuffer.toString());
					}
				}
				else if(Padrao.contador == 1){
					//char ch = '\u0000';
					//Padrao.stringbuffer.setCharAt(Padrao.stringbuffer.toString().length()-1,ch);
					Padrao.stringbuffer.deleteCharAt(Padrao.stringbuffer.toString().length()-1);
					if (Padrao.y > 1) {
						stringbu.append(" " + Padrao.contador + " " + "resultado na cadeia" + Padrao.stringbuffer.toString());
					}
					else if (Padrao.y == 1){
						stringbu.append(" " + Padrao.contador + " " + "resultado na cadeia" + Padrao.stringbuffer.toString());
					}

				}
				else if (Padrao.contador == 0)
					stringbu.append(" "+ "Sem resultados");

				fw.write(stringbu.toString() + "\r\n");
				Padrao.contador = 0;
				Padrao.cadeias = 0;
				Padrao.y = 0;
				Padrao.stringbuffer = new StringBuffer("");
				stringbu = new StringBuffer("");
			}

			fw.close();
		}

		catch (IOException e) {
		}
	}
}
class Cadeia{
	private String cadeia;
	private char[] vetorCadeia;

	public Cadeia (String cadeia){
		this.cadeia = cadeia;
		vetorCadeia = this.cadeia.toCharArray();
	}

	public String getCadeia(){
		return this.cadeia;
	}
	public char[] getVetor(){
		return this.vetorCadeia;
	}
}
class Padrao {
	private String padrao;
	private char[] vetorPadrao;
	static int cadeias = 0;
	static int contador = 0;
	static String concatCadeia = "";
	static int x =0;
	static int y =0;
	static StringBuffer stringbuffer = new StringBuffer();

	public Padrao(String padrao){
		this.padrao = padrao;
		vetorPadrao = this.padrao.toCharArray();
	}

	public String getPadrao(){
		return this.padrao;
	}
	public char[] getVetorPadrao(){
		return this.vetorPadrao;
	}
	public static void CalculaApr (int [] tab, char [] P)	{
		tab[0] = -1;
		int k = -1;
		for( int q = 1; q < P.length; q++){
			while(k >= 0 && P[k+1] != P[q])  // enquanto a próxima letra não bate,
				// volta no padrão
				k = tab[k];
			if( P[k+1] == P[q]) k++; /* se a letra bateu, anda um
				    caso contrário, k = -1 */
			tab[q] = k;              // valor de Apr[q] determinado
		}

	}

	public static void KMP(char [] cadeia, char [] padrao){
		boolean tem = false;
		int [] tab = new int[padrao.length];
		CalculaApr (tab,padrao);

		int q = -1;
		for( int i = 0 ; i < cadeia.length; i++){
			while (q >= 0 && padrao[q+1] != cadeia[i]) //enquanto a letra não bate...
				q = tab[q];
			if( padrao[q+1] == cadeia[i]) q++; //ou a letra está correta, e andamos no padrão
			// ou q é -1
			if(q == padrao.length - 1){
				contador++;
				x++;

				tem = true;
				q = tab[q];
			}
		}

		if (tem == true){
			stringbuffer.append(" " + cadeias++ + ":" + x + ",");
			y++;

		}
		x = 0;
	}
}
