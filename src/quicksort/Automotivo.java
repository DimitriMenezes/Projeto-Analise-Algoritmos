package quicksort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.Cronometro;

public class Automotivo {
	public static void main(String[] args) {
		Cronometro crono = new Cronometro();
		crono.start();
		String fileIn = "automotivo.txt";
		String fileOut= "saidaA.txt";

		try{
			//leitura do arquivo IN
			FileReader arquivo = new FileReader(fileIn);
			BufferedReader lerArq = new BufferedReader(arquivo);
			//leitura da primeira linha que mostra a quantidade de conteiners
			String linha = lerArq.readLine();
			int quantidadeDeArquivos = Integer.parseInt(linha);
			Arquivo[] arquivos = new Arquivo[quantidadeDeArquivos];

			int i = 0;
			while (linha != null && i<= quantidadeDeArquivos) {
				linha = lerArq.readLine();
				if(linha != null){
					arquivos[i] = new Arquivo(linha);  
					i = i+1;
				}
			}
			
			//geração do arquivo OUT
			File dir = new File(fileOut);
			if( !dir.exists() )
				dir.createNewFile();
			FileWriter fw = new FileWriter(dir,true);
			fw.write(0 + ": ");
			for (int j = 0; j < arquivos.length; j++) {
				fw.write(arquivos[j].toString());
			}
			fw.close();

			Arquivo.quickSort(arquivos,0,arquivos.length-1,dir);
		}
		catch (Exception e) {
		}
		crono.stop();
		System.out.println(crono.elapsedTime());
	}
}

class Arquivo{
	private String nome;
	private static int linhas = 1;


	public Arquivo (String nome){
		this.nome = nome;
	}

	public String getNome(){
		return this.nome;
	}

	public int getLinhas(){
		return linhas;
	}

	@Override
	public String toString() {
		return this.nome  + " ";
	}

	public static void troca(Arquivo[] dados, int posA, int posB) {
		Arquivo temp = dados[posA];
		dados[posA] = dados[posB];
		dados[posB] = temp;
	}

	public static void quickSort(Arquivo[] v, int inicio, int fim, File file) {
		if(inicio < fim) {
			int pivo = particionar(v, inicio, fim,file);
			quickSort(v, inicio, pivo - 1,file);
			quickSort(v, pivo + 1, fim,file);
		}
	}

	public static int particionar(Arquivo V[], int inicio, int fim, File file) {
		Arquivo pivo = V[fim];
		int i = inicio - 1, j;
		//inserção no arquivo

		for(j = inicio; j < fim; j++) {
			if(V[j].getNome().compareTo(pivo.getNome()) < 0) {
				i = i + 1;
				troca(V, i , j);
			}
		}

		troca(V,i + 1,fim);

		try {
			FileWriter fw = new FileWriter(file,true);
			fw.write(linhas + ": ");
			for (int x = 0; x < V.length; x++) {
				fw.write(V[x].toString());
			}
			fw.close();
			linhas++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i + 1;
	}
}