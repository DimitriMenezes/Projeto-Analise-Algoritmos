package huffman_rle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


public class Compressao {
	public static void main(String[] args) {
		String fileIn = args[0];
		String fileOut= args[1];
		
		try {
			FileReader arquivo = new FileReader(fileIn);
			BufferedReader lerArq = new BufferedReader(arquivo);
			String linha = lerArq.readLine();
			int quantidadeDeDados = Integer.parseInt(linha);
			Dados[] dados = new Dados[quantidadeDeDados];

			int i = 0;
			while (linha != null && i<= dados.length-1) {
				linha = lerArq.readLine();
				if(linha != null){
					dados[i] = new Dados(linha);
					i = i+1;
				}
			}
			//geração do arquivo OUT
			File dir = new File(fileOut);

			if( !dir.exists() )
				dir.createNewFile();
			FileWriter fw = new FileWriter(dir,true);
			for (int j = 0; j < dados.length; j++) {
				//RLE
				Dados.RLE(dados[j].getDados());
				int[] contador = new int[256];
				
				
				int tamanhoHuffman = dados[j].getDados().length()*8;
				for(char c: dados[j].getDados().toCharArray()){
					contador[c]++;
				}
				
				//HUFFMAN
				No.construirArvore(contador, contador.length, dados[j].getDados());
				
				//COMPARACAO RLE VS HUFFMAN
				if (dados[j].getDiferença() < No.getDiferenca(tamanhoHuffman)) {
					fw.write(j + ":" + " RLE" + " "+ Dados.dadoComprimido +" ("+ (int) dados[j].porcentagem() + "%)" + "\r\n");
					//System.out.println(j + ":" + " RLE" + " "+ Dados.dadoComprimido +" ("+ (int) dados[j].porcentagem() + "%)" );
				}
				else if(dados[j].getDiferença() == No.getDiferenca(tamanhoHuffman)){
					fw.write(j + ":" + " RLE" + " "+ Dados.dadoComprimido +" ("+ (int) dados[j].porcentagem() + "%)" + "\r\n");
					//System.out.println(j + ":" + " RLE" + " "+ Dados.dadoComprimido +" ("+ (int) dados[j].porcentagem() + "%)" );
					fw.write(j + ":" + " HUF" + " "+ No.comprimido.toString() +" ("+ No.getPorcentagem(tamanhoHuffman) + "%)" + "\r\n");
					//System.out.println(j + ":" + " HUF" + " "+ No.comprimido.toString() +" ("+ (int) No.getPorcentagem(tamanhoHuffman) + "%)");
				}
				else{
					fw.write(j + ":" + " HUF" + " "+ No.comprimido.toString() +" ("+ No.getPorcentagem(tamanhoHuffman) + "%)" + "\r\n");
					//System.out.println(j + ":" + " HUF" + " "+ No.comprimido.toString() +" ("+ (int) No.getPorcentagem(tamanhoHuffman) + "%)");
				}

				Dados.dadoComprimido = new StringBuffer("");
				Dados.tamanhoComprimido = 0;
				No.tamanhoComprimido = 0;
				No.string = new StringBuffer("");
				No.stringAuxiliar = "";
				No.comprimido = new StringBuffer("");
				No.contador = 0;
			}
			fw.close();
		} catch (Exception  e) {

		}
	}
}

class Dados{
	private String dados;
	private byte[] vetor;
	static StringBuffer dadoComprimido = new StringBuffer();
	private StringBuffer dadoOriginal = new StringBuffer();
	int tamanhoOriginal;
	static int tamanhoComprimido;

	public Dados(String dados){
		this.dados = dados;
		vetor = dados.getBytes();
		String x;

		for (int i = 0; i < vetor.length; i++) {
			x = Integer.toBinaryString(vetor[i]);
			if (x.length() < 8) {
				for (int j = x.length();j < 8; j++) {
					x = "0" + x;
				}
			}
			dadoOriginal.append(x);
		}
		tamanhoOriginal = dadoOriginal.toString().length();
	}

	public String getDados(){
		return this.dados;
	}

	static String RLE(String codigo) {
		StringBuffer original = new StringBuffer();
		StringBuffer aux = new StringBuffer();
		for (int i = 0; i < codigo.length(); i++) {
			int tamanho = 1;
			while (i+1 < codigo.length() && codigo.charAt(i) == codigo.charAt(i+1)) {
				tamanho++;
				i++;
			}
			//concatenando o código
			aux.append(Integer.toBinaryString(codigo.charAt(i)));
			StringBuffer y = new StringBuffer("");

			if (aux.toString().length() < 8) {
				for (int j = aux.toString().length(); j < 8; j++) {

					y.append("0");
				}
				y.append(aux.toString());

			}
			aux = new StringBuffer("");
			String abom = "";
			boolean flag = false;
			//concatenando o numeral

			do {
				if (tamanho > 15) {
					abom = "1111";
					tamanho = tamanho - 15;
					flag = false;
					aux.append(abom + y.toString());
				}
				else{
					String argh =Integer.toBinaryString(tamanho);
					if (argh.length() < 4 ) {
						for (int j = argh.length(); j < 4; j++) {
							argh = "0" + argh;
						}
					}
					aux.append(argh + y.toString());
					flag = true;
				}

			} while (flag == false);
			original.append(aux.toString());
			aux = new StringBuffer("");
		}

		tamanhoComprimido = original.toString().length();
		Dados.dadoComprimido = original;
		return Dados.dadoComprimido.toString();
	}
	double getDiferença(){
		return (double) (tamanhoComprimido)/(tamanhoOriginal);
	}

	double porcentagem(){
		return Math.round((double) (tamanhoComprimido)/(tamanhoOriginal)*100);
	}
}
class No {
	int freq;
	char simb;
	No direito;
	No esquerdo;
	static int contador;
	static int tamanhoComprimido;
	static StringBuffer string = new StringBuffer();
	static String stringAuxiliar;
	static StringBuffer comprimido = new StringBuffer();
	static prefixo[] prefixo = new prefixo[256];

	public No (int frequencia, char simbolo, No Direito, No Esquerdo){
		this.freq = frequencia;
		this.simb = simbolo;
		this.direito = Direito;
		this.esquerdo = Esquerdo;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public char getSimb() {
		return simb;
	}

	public void setSimb(char simb) {
		this.simb = simb;
	}

	public No getDireito() {
		return direito;
	}

	public void setDireito(No direito) {
		this.direito = direito;
	}

	public No getEsquerdo() {
		return esquerdo;
	}

	public void setEsquerdo(No esquerdo) {
		this.esquerdo = esquerdo;
	}

	@Override
	public String toString() {
		return "No [freq=" + freq + ", simb=" + simb + "]";
	}

	public static No construirArvore(int H[], int N, String s) {
		PriorityQueue fila2 = new PriorityQueue(255);
		for(int i = 0; i < N; i++){
			if(H[i] > 0){
				fila2.inserir(new No(H[i], (char)i, null, null));
			}
		}
		//caso base
		if (fila2.tamanho() == 1) {
			StringBuffer buffer = new StringBuffer();
			buffer.append('0');
			printCodes(fila2.busca(0),buffer, s);
		}
		else{
			while(fila2.tamanho() > 1) {
				No x = fila2.remover();
				No y = fila2.remover();
				fila2.inserir(new No(x.freq + y.freq, '\0', y, x));
			}

			printCodes(fila2.busca(0),new StringBuffer(), s);
		}
		char[] array = s.toCharArray();

		for (int j = 0; j < array.length;j++ ) {
			for (int i = 0; i < prefixo.length; i++) {
				if(prefixo[i] != null){
					if (array[j] == prefixo[i].getCaractere() ) {
						comprimido.append(prefixo[i].getPrefixo());
					}
				}
			}
		}
		tamanhoComprimido = comprimido.toString().length();
		prefixo = new prefixo[256];
		return null;
	}

	static double getDiferenca (int tamanhoOriginal){
		return (double) tamanhoComprimido / tamanhoOriginal;
	}
	static int getPorcentagem (int tamanhoOriginal){
		return (int) Math.round(getDiferenca(tamanhoOriginal)*100);
	}
	public static void printCodes(No tree, StringBuffer prefix, String eita) {
		if (tree.getDireito() == null && tree.getEsquerdo() == null) {
			prefixo[contador++] = new prefixo(tree.getSimb(),prefix.toString());
			//System.out.println(tree.getSimb() + " " +prefix.toString() );

		} else {
			//esquerda
			prefix.append('0');
			printCodes(tree.getEsquerdo(), prefix, eita);
			prefix.deleteCharAt(prefix.length()-1);

			//direita
			prefix.append('1');
			printCodes(tree.getDireito(), prefix, eita);
			prefix.deleteCharAt(prefix.length()-1);
		}

	}
	static class prefixo{
		private char caractere;
		private String prefixo;

		prefixo(char caractere, String prefixo){
			this.caractere = caractere;
			this.prefixo = prefixo;
		}
		public char getCaractere() {
			return caractere;
		}
		public void setCaractere(char caractere) {
			this.caractere = caractere;
		}
		public String getPrefixo() {
			return prefixo;
		}
		public void setPrefixo(String prefixo) {
			this.prefixo = prefixo;
		}
	}
}
class PriorityQueue {
	private int tamanhoMaximo;

	private No[] FilaArray;

	private int tamanhoAtual;

	public PriorityQueue(int s) {
		tamanhoMaximo = s;
		FilaArray = new No[tamanhoMaximo];
		tamanhoAtual = 0;
	}

	public int tamanho(){
		return this.tamanhoAtual;
	}

	public No busca(int indice){
		for (int i = 0; i < FilaArray.length; i++) {
			if (FilaArray[i] == FilaArray[indice]) {
				return FilaArray[i];
			}
		}
		return null;
	}

	public void inserir(No item) {
		int i;

		if (tamanhoAtual == 0)
			FilaArray[tamanhoAtual++] = item; 
		else
		{
			for (i = tamanhoAtual - 1; i >= 0; i--) {
				if (item.getFreq() >= FilaArray[i].getFreq()) 
					FilaArray[i + 1] = FilaArray[i]; 
				else

					break; 
			}
			FilaArray[i + 1] = item; 
			tamanhoAtual++;
		} 
	}

	public No remover(){
		return FilaArray[--tamanhoAtual];
	}

	public boolean vazio(){
		return (tamanhoAtual == 0);
	}

	public boolean cheio(){
		return (tamanhoAtual == tamanhoMaximo);
	}
}