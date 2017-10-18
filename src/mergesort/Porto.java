package mergesort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util.Cronometro;

public class Porto{
	public static void main(String[] args){
		Cronometro crono = new Cronometro();
		crono.start();
		String fileIn = "entrada.txt";
		String fileOut= "saida.out";
		try {
			//leitura do arquivo IN
			FileReader arquivo = new FileReader(fileIn);
			BufferedReader lerArq = new BufferedReader(arquivo);
			//leitura da primeira linha que mostra a quantidade de conteiners
			String linha = lerArq.readLine();
			int quantidadeDeConteiner = Integer.parseInt(linha);

			//criando e adicionando conteiners ao vetor
			Conteiner[] conteiners = new Conteiner[quantidadeDeConteiner];
			int i = 0;
			while (linha != null && i<= quantidadeDeConteiner) {
				linha = lerArq.readLine();

				if(linha != null){
					conteiners[i] = new Conteiner(linha);  
					i = i+1;
				}
			}

			//geração do arquivo OUT
			File dir = new File(fileOut);
			if( !dir.exists() )
				dir.createNewFile();
			FileWriter fw = new FileWriter(dir,true);
			fw.write("0:");
			for (int j = 0; j < conteiners.length; j++) {
				fw.write(conteiners[j].toString());
			}

			fw.close();
			//chamando o merge
			Conteiner.MergeSort(conteiners,0, conteiners.length-1, dir);

		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado");
		} catch (IOException e) {
			e.printStackTrace();
		}
		crono.stop();
		System.out.println(crono.elapsedTime());
	}
	
}

class Conteiner{
	//codigo completo 
	private String codigo;
	//apenas a prioridade
	private String priority;
	private int prioridade;
	//os 3 primeiros caracteres
	private String hexadecimal;
	private int valorEmHexa;

	static int linhas = 1;

	public Conteiner(String codigo){
		splitString(codigo);
	}

	public int getPrioridade(){
		return this.prioridade;
	}

	public int getLinhas(){
		return linhas;
	}

	public int getValorEmHexa(){
		return this.valorEmHexa;
	}

	public void splitString(String codigo){
		this.codigo = codigo.substring(0, 10);
		this.priority = codigo.substring(11);
		this.prioridade = Integer.parseInt(priority);
		this.hexadecimal = codigo.substring(2, 5);
		this.valorEmHexa = Integer.valueOf(this.hexadecimal, 16);
		this.codigo = codigo.substring(2, 10);
	}

	@Override
	public String toString() {
		return this.codigo + " ";
	}

	public static void MergeSort(Conteiner[] lista, int primeiro, int ultimo, File file) {
		if (primeiro < ultimo) {
			int Meio = (ultimo + primeiro) / 2;
			MergeSort(lista, primeiro, Meio, file);
			MergeSort(lista, Meio + 1, ultimo, file);
			intercalar(lista, primeiro, Meio, ultimo, file);
		}
	}

	private static void intercalar(Conteiner[] lista, int primeiro, int meio, int ultimo, File file) {
		Conteiner[] Temp = new Conteiner[lista.length];
		int proxEsquerda = primeiro;
		int proxDireita = meio + 1;
		int i = primeiro;

		while ( (proxEsquerda <= meio) && (proxDireita <= ultimo)) {
			if (lista[proxEsquerda].getValorEmHexa() < lista[proxDireita].getValorEmHexa()) {
				Temp[i++] = lista[proxEsquerda++];
			} else if (lista[proxEsquerda].getValorEmHexa() == lista[proxDireita].getValorEmHexa()) {
				if(lista[proxEsquerda].getPrioridade() > lista[proxDireita].getPrioridade() ){
					Temp[i++] = lista[proxEsquerda++];
				}
				else if (lista[proxEsquerda].getPrioridade() == lista[proxDireita].getPrioridade() ){
					Temp[i++] = lista[proxEsquerda++];
				}
				else{
					Temp[i++] = lista[proxDireita++];
				}
			}
			else{
				Temp[i++] = lista[proxDireita++];
			}
		}

		while (proxEsquerda <= meio) {
			Temp[i++] = lista[proxEsquerda++];
		}
		while (proxDireita <= ultimo) {			
			Temp[i++] = lista[proxDireita++];
		}

		try {
			FileWriter fw = new FileWriter(file, true);
			fw.write(linhas + ":");
			for (i = primeiro; i <= ultimo; i++) {
				lista[i] = Temp[i];
			}
			for (int j = 0; j < Temp.length; j++) {
				fw.write(lista[j].toString());
			}
			fw.close();
			linhas++;
		} catch (IOException e) {

		}
	}
}