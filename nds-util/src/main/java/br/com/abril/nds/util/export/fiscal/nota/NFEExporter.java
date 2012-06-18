package br.com.abril.nds.util.export.fiscal.nota;

import java.util.Map;

/**
 * Classe responsável pela exportação de Nota Fiscal Eletrônica de arquivos TXT.
 * 
 * @author Discover Technology
 * 
 */
public class NFEExporter {

	public static final String SEPARADOR_SECAO = "|"; 

	public static final String MASCARA_DATA = "yyyy-MM-dd"; 
	
	private Map<String, String> listaSecoes;
	
	/**
	 * Exporta o dados de um objeto para uma String.
	 * 
	 * @param notaFiscal - Objeto da Nota Fiscal.
	 * @return
	 */
	public <NF> String toString(NF notaFiscal) {
		/*String secaoNome;
		String secao;
		
		this.listaSecoes = new HashMap<String, String>();
		
		secao = this.listaSecoes.get(secaoNome);

		this.listaSecoes.put(secaoNome, secao);*/
		
		return null;
	}
	
	private String addCampoToSecao(String secao, String valor, int position){
		String camposSecao[] = secao.split(SEPARADOR_SECAO);
		return null;
	}
}
