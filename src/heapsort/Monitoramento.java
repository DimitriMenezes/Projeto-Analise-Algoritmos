package heapsort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.Cronometro;

public class Monitoramento {

	public static void main(String[] args){
		Cronometro crono = new Cronometro();
		crono.start();
		String fileIn = "monitoramento.txt";
		String fileOut= "saidaM.txt";
		try {
			//leitura do arquivo IN
			FileReader arquivo = new FileReader(fileIn);
			BufferedReader lerArq = new BufferedReader(arquivo);
			//leitura da primeira linha que mostra a quantidade de conteiners
			String linha = lerArq.readLine();
			int quantidadeDeDispositivos = Integer.parseInt(linha);
			Dispositivo[] dispositivos = new Dispositivo[quantidadeDeDispositivos];

			int i = 0;
			while (linha != null && i<= quantidadeDeDispositivos) {
				linha = lerArq.readLine();
				if(linha != null){
					dispositivos[i] = new Dispositivo(linha);  
					i = i+1;
				}
			}
			//geração do arquivo OUT
			File dir = new File(fileOut);

			if( !dir.exists() )
				dir.createNewFile();
			FileWriter fw = new FileWriter(dir,true);
			fw.write("0:" + " ");
			for (int j = 0; j < dispositivos.length; j++) {
				fw.write(dispositivos[j].toString());
			}
			fw.close();
			Dispositivo.heapSort(dispositivos, dir );

		}

		catch (FileNotFoundException e) {

			System.out.println("Arquivo nao encontrado");
		} catch (IOException e) {

			e.printStackTrace();
		}
		crono.stop();
		System.out.println(crono.elapsedTime());
	}

}
class Dispositivo{
	private String codigo;
	//apenas a prioridade
	private String priority;
	private int prioridade;
	//só o valor hexadecimal

	static int linhas = 1;

	public Dispositivo (String codigo){
		splitString(codigo);
	}

	public int getPrioridade(){
		return this.prioridade;
	}

	public int getLinhas(){
		return linhas;
	}

	public void splitString(String codigo){
		this.codigo = codigo.substring(0, 9);
		this.priority = codigo.substring(11);
		this.prioridade = Integer.parseInt(priority);
		this.codigo = codigo.substring(2, 10);

	}
	@Override
	public String toString() {
		return this.codigo + " ";
	}

	public static void heapSort(Dispositivo[] vetor, File file){  
		construir(vetor);
		int n = vetor.length;
		for (int i = n - 1; i > 0; i--) { 
			trocar(vetor, i , 0);
			heapify(vetor, 0, --n, file);
		}
	}
	
	private static void construir(Dispositivo[] vetor)	{ 
		for (int i = vetor.length/2 - 1; i >= 0; i--)
			heapify1(vetor, i , vetor.length);
	}
	//método que faz a impressão o heapify com a escrita no arquivo
	private static void heapify(Dispositivo[] vetor, int posicao, int n, File file) { 
		int menor; 
		int esquerdo = 2 * posicao + 1;
		int direito = 2 * posicao + 2;
		
		if ( (esquerdo < n) && (vetor[esquerdo].getPrioridade() < vetor[posicao].getPrioridade()) )		{
			menor = esquerdo;
		}
		else		{
			menor = posicao;
		}
		if (direito < n && vetor[direito].getPrioridade() < vetor[menor].getPrioridade()) 		{ 
			menor = direito;
		}
		if (menor != posicao) {
			trocar(vetor, posicao, menor);
			heapify1(vetor, menor, n);
		}
		
		try {
			FileWriter fw = new FileWriter(file,true);
			fw.write(linhas + ": ");
			for (int k = 0; k < vetor.length; k++) {
				fw.write(vetor[k].toString());
			}
			fw.close();
			linhas++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//metodo que faz o heapify sem a escrita no arquivo
	private static void heapify1(Dispositivo[] vetor, int posicao, int n) { 
		int menor; 
		int esquerdo = (2*posicao) + 1;
		int direito = (2*posicao) + 2;
		
		if ( (esquerdo < n) && (vetor[esquerdo].getPrioridade() < vetor[posicao].getPrioridade()) )		{
			menor = esquerdo;
		}
		else		{
			menor = posicao;
		}
		if (direito < n && vetor[direito].getPrioridade() < vetor[menor].getPrioridade()) 		{ 
			menor = direito;
		}
		if (menor != posicao) {
			trocar(vetor, posicao, menor);
			heapify1(vetor, menor, n);
		}
	}

	public static void trocar( Dispositivo[ ] vetor, int j, int aposJ )	{
		Dispositivo aux = vetor[ j ];
		vetor[ j ] = vetor[ aposJ ];
		vetor[ aposJ ] = aux;
	}
}
