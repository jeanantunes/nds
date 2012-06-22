package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.abril.nds.util.CampoSecao;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoSessao;

/**
 * Classe responsável pela exportação de Nota Fiscal Eletrônica de arquivos TXT.
 * 
 * @author Discover Technology
 * 
 */
public class NFEExporter {

	/**
	 * Separador de campos nas seções.
	 */
	public static final String SEPARADOR_SECAO = "|"; 

	/**
	 * Máscara padrão de data.
	 */
	public static final String MASCARA_DATA = "yyyy-MM-dd";
	
	/**
	 * Máscara padrão de número.
	 */
	public static final String MASCARA_NUMBER = "#.####"; 

	/**
	 * String vázia.
	 */
	public static final String STRING_VAZIA = ""; 
	
	/**
	 * String quebra de linha.
	 */
	public static final String QUEBRA_LINHA = "\n"; 
	
	/**
	 * Lista de seções atuais.
	 */
	private Map<String, String> listaSecoes;
	
	/**
	 * Para utilizar em atributos Collections
	 */
	private List<NFEExporter> listaNFEExporters;
	
	/**
	 * Lista das sessoes do arquivo de exportação
	 */
	private Map<TipoSessao, List<CampoSecao>> mapSecoes;
	
	/**
	 * Construtor padrão
	 */
	public NFEExporter(){
		this.clear();
	}
	
	/**
	 * Limpa todas as seções atuais.
	 */
	public void clear(){
		this.listaSecoes = new HashMap<String, String>();
		this.listaNFEExporters = new ArrayList<NFEExporter>(); 
	}
	
	/**
	 * Faz a varredura de um objeto buscando as anotações NFEExport ou
	 * NFEExports e adicona os dados nas lista de seções.
	 * 
	 * @param notaFiscal - Objeto da Nota Fiscal.
	 * @return - String com toda as seções.
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <NF> void execute(NF notaFiscal) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		Method[] metodos = notaFiscal.getClass().getDeclaredMethods();
		
		for (Method metodo : metodos) {
			this.proccessMethodAnnotations(metodo, notaFiscal);
		}
		
		Field[] campos = notaFiscal.getClass().getDeclaredFields();
		
		for (Field campo : campos) {
			this.proccessFieldAnnotations(campo, notaFiscal);
		}		
	}
	
	/**
	 * 
	 * 
	 * @param metodo
	 * @param notaFiscal
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	private <NF> void proccessMethodAnnotations(Method metodo, NF notaFiscal) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				
		NFEExports nfeExports = metodo.getAnnotation(NFEExports.class);
			
		NFEExport nfeExport = metodo.getAnnotation(NFEExport.class);
			
		List<NFEExport> listaNFEExport = new ArrayList<NFEExport>();	
			
		if (nfeExports != null){
			for (NFEExport nfeExp: nfeExports.value()) {
				listaNFEExport.add(nfeExp);
			}
		}
			
		if (nfeExport != null) {
			listaNFEExport.add(nfeExport); 
		}

		if (listaNFEExport != null && !listaNFEExport.isEmpty()) {
	
			Object valor = metodo.invoke(notaFiscal, new Object[] {});
			
			if (isNumeric(valor) || isDate(valor) || isLiteral(valor)) {
			
				for (NFEExport nfeExp : listaNFEExport) {
				
					if (nfeExp != null) {
//						String secao = this.listaSecoes.get(nfeExp.secao());
//						secao = executarAnotacao(secao, nfeExp, valor);
//						this.listaSecoes.put(nfeExp.secao(), secao);
						addCampoSecao(nfeExp, valor);
					}

				}
			
			} else if (valor instanceof Collection) {
				for (Object valorCollection: (Collection)valor) {
					NFEExporter nfeExporter = new NFEExporter();
					nfeExporter.clear();
					nfeExporter.execute(valorCollection);
					this.listaNFEExporters.add(nfeExporter);
				}
			} else if (valor != null) {
				execute(valor);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private <NF> void proccessFieldAnnotations(Field campo, NF notaFiscal) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			
		campo.setAccessible(true);
			
		Object valor = campo.get(notaFiscal);

		if (isNumeric(valor) || isDate(valor) || isLiteral(valor)) {
			
			NFEExports nfeExports = campo.getAnnotation(NFEExports.class);
			NFEExport nfeExport = campo.getAnnotation(NFEExport.class);
				
			List<NFEExport> listaNFEExport = new ArrayList<NFEExport>();
								
			if (nfeExports != null) {
					
				for (NFEExport nfeExp: nfeExports.value()) {
						
					if (!StringUtil.isEmpty(nfeExp.documento())) {
						
						String documento = valor.toString();
							
						if (documento.length() == nfeExp.tamanho()) {
							listaNFEExport.add(nfeExp);
						}
						
					} else {
						listaNFEExport.add(nfeExp);
					}
				}
			}
			
			if (nfeExport != null) {
				listaNFEExport.add(nfeExport); 
			}

			if (listaNFEExport != null && !listaNFEExport.isEmpty()) {
					
				for (NFEExport nfeExp : listaNFEExport) {
						
					if (nfeExp != null) {
//						String secao = this.listaSecoes.get(nfeExp.secao());
//
//						secao = executarAnotacao(secao, nfeExp, valor);
//
//						this.listaSecoes.put(nfeExp.secao(), secao);
						addCampoSecao(nfeExp, valor);
						
					}
				}
			}	
		} else if (valor instanceof Collection) {
			
			for (Object valorCollection: (Collection)valor) {
				NFEExporter nfeExporter = new NFEExporter();
				nfeExporter.clear();
				nfeExporter.execute(valorCollection);
				this.listaNFEExporters.add(nfeExporter);
			}
		
		} else if (valor != null) {
			execute(valor);
		}
	}
	
	/**
	 * @param nfeExport annotation do campo
	 * @param valor valor do campo
	 */
	private void addCampoSecao(NFEExport nfeExport, Object valor) {
		
		CampoSecao campo = new CampoSecao();
		campo.setPosicao(nfeExport.posicao());
		campo.setMascara(nfeExport.mascara());
		campo.setSessao(nfeExport.secao());
		campo.setTamanho(nfeExport.tamanho());
		campo.setValor(valor);
		
		List<CampoSecao> camposSecao = mapSecoes.get(campo.getSessao());
		
		if (camposSecao == null || camposSecao.isEmpty()) {
			camposSecao = new ArrayList<CampoSecao>();
		}
		
		camposSecao.add(campo);
		
		this.mapSecoes.put(campo.getSessao(), camposSecao);
	}
	
	/**
	 * Extrai a lista de seções para um String.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		String conteudo = STRING_VAZIA;
		
		ordenaListaNFEEporters();
		String primeiraSecao = null;
		int indexListaNFEExporters = 0;
		if (!this.listaNFEExporters.isEmpty()) { 
			primeiraSecao = this.listaNFEExporters.get(indexListaNFEExporters).getPrimeiraSecao();
		}
		
		for (String key : this.listaSecoes.keySet()) {
			if (primeiraSecao != null && key.compareTo(primeiraSecao) >= 0) {
				boolean repetir;
				do {
					if (this.listaNFEExporters.size() > indexListaNFEExporters) {
						conteudo += this.listaNFEExporters.get(indexListaNFEExporters).toString();
						primeiraSecao = this.listaNFEExporters.get(indexListaNFEExporters++).getPrimeiraSecao();
						repetir = primeiraSecao != null && primeiraSecao.compareToIgnoreCase(this.listaNFEExporters.get(indexListaNFEExporters - 1).getPrimeiraSecao()) == 0;
					} else {
						primeiraSecao = null;
						repetir = false;
					}
				} while (repetir);

			}

			conteudo += this.listaSecoes.get(key) + QUEBRA_LINHA;
		}
		
		return conteudo;
	}
	
	private void ordenaListaNFEEporters(){
		NFEExporter nfeExporterAux;

		for (int i = 0; i < this.listaNFEExporters.size() - 1; i++) {
			for (int j = i + 1; j < this.listaNFEExporters.size(); j++) {
				if (this.listaNFEExporters.get(i).getPrimeiraSecao().compareToIgnoreCase(this.listaNFEExporters.get(j).getPrimeiraSecao()) > 0 ){
					nfeExporterAux = this.listaNFEExporters.get(i);
					this.listaNFEExporters.set(i, this.listaNFEExporters.get(j));
					this.listaNFEExporters.set(j, nfeExporterAux);
				}
			}
		}
	}
	
	/**
	 * Executa a inclusão dos atributos da anotação NFEExport e o valor do
	 * Object.
	 * 
	 * @param secao - Seção que o valor será incluído.
	 * @param nfeExport - Anotação NFEExport do atributo.
	 * @param valor - Valor a ser incluido na seção.
	 * @return - Linha com todos os campos da seção.
	 */
//	private String executarAnotacao(String secao, NFEExport nfeExport, Object valor){
//		String secaoNome = nfeExport.secao();
//		int tamanho = nfeExport.tamanho();
//		int posicao = nfeExport.posicao();
//		String mascara = nfeExport.mascara();
//
//		if (StringUtil.isEmpty(secao)) {
//			secao = secaoNome + SEPARADOR_SECAO;
//		}
//		
//		secao = addCampoToSecao(secao,
//				converteCampoParaString(valor, mascara, tamanho), posicao);
//
//		return secao;
//	}
	

	/**
	 * Converte o campo para String com a mascara especificada, se não tiver a
	 * mascara utiliza a mascara padrão, e trunca o campo de acordo com o
	 * tamanho.
	 * 
	 * @param valor - Valor a ser convertido.
	 * @param mascara - Mascara para o valor.
	 * @param tamanho - Tamanho a ser truncado.
	 * @return - Valor convertido, mascarado e truncado.
	 */
	private String converteCampoParaString(Object valor, String mascara, int tamanho){
		
		String valorString = STRING_VAZIA;
		String mascaraUsada;
		
		if (valor != null) {
			if (isDate(valor)) {
				Date data;
				mascaraUsada = (StringUtil.isEmpty(mascara)) ? MASCARA_DATA	: mascara;
				if (valor instanceof Date) {
					data = (Date) valor;
				} else {
					data = ((Calendar) valor).getTime();
				}
				valorString = DateUtil.formatarData(data, mascaraUsada);
			} else if (isNumeric(valor)) {

				mascaraUsada = (StringUtil.isEmpty(mascara)) ? MASCARA_NUMBER : mascara;

				valorString = new DecimalFormat(mascaraUsada, new DecimalFormatSymbols(new Locale("en_US"))) .format(valor);
				if (StringUtil.isEmpty(mascara)) {
					valorString = valorString.replace(",", "");
				}
			} else {
				valorString = valor.toString();
				if (tamanho > 0) {
					valorString = valorString.replace(SEPARADOR_SECAO, STRING_VAZIA).substring(0, tamanho);
				}
			}
		} else {
			valorString = STRING_VAZIA;
		}
		
		return valorString.replace(SEPARADOR_SECAO, STRING_VAZIA);
	}
	
	/**
	 * Adiciona campo à linha da seção.
	 * 
	 * @param secao - Linha da seção atual.
	 * @param valor - Valor a ser incluído.
	 * @param posicao - Posição que será incluído na linha.
	 * @return - Linha da seção com o campo adicionado.
	 */
	private String addCampoToSecao(String secao, String valor, int posicao){
		
		if (!secao.endsWith(SEPARADOR_SECAO)) {
			secao += SEPARADOR_SECAO;
		}

		String secaoAtualizada = STRING_VAZIA;
		int qtCampos = countSeparator(secao, SEPARADOR_SECAO);
		int positionInLayout = posicao + 1;
		
		if (qtCampos > positionInLayout ) { 
			String[]camposSecao = divideStringBySeparator(secao, SEPARADOR_SECAO);

			int campoAtual = 0;
			for (String campo: camposSecao) {
				if (campoAtual == positionInLayout) {
					secaoAtualizada += valor.concat(SEPARADOR_SECAO);
				} else {
					secaoAtualizada += campo.concat(SEPARADOR_SECAO);
				}
				campoAtual++;
			}
		} else {
			secaoAtualizada = repetirStringNoFinal(secao, SEPARADOR_SECAO, positionInLayout - qtCampos) + valor + SEPARADOR_SECAO;
		}
		
		return secaoAtualizada;
	}
	
	/**
	 * Adiciona o separado em uma quantidade de vezes especificada no parâmetro vezes.
	 * 
	 * @param valor - Valor da String.
	 * @param separador - Caracteer a ser repetido.
	 * @param vezes - Quantidade de vezes.
	 * @return - String com os valores repetidos.
	 */
	private String repetirStringNoFinal(String valor, String separador, int vezes){
		String repeticao = STRING_VAZIA;
		for (int i = 0; i < vezes; i++) {
			repeticao += SEPARADOR_SECAO;
		}
		return valor + repeticao;
	}
	
	/**
	 * Conta a quantidade de separadores que contém o valor.
	 * 
	 * @param valor - Valor a ser verificado. 
	 * @param separador - Valor que será contado.
	 * @return - Quantidade de separadores achados.
	 */
	private int countSeparator(String valor, String separador){
		int count = 0; 
		int passaSeparador = 0;
		for (int i = 0; i <= valor.length() - separador.length(); i++) {
			if (valor.substring(i, i + separador.length()).equals(separador) && passaSeparador == 0) {
				count++;
				passaSeparador = separador.length() - 1;
			} else if (passaSeparador > 0) {
				passaSeparador--;
			}
		}
		return count;
	}
	
	/**
	 * Divide uma String em um Array de String divididos pelo separador.
	 * 
	 * @param valor - Valor que será dividido.
	 * @param separador - String de separação.
	 * @return Array de String.
	 */
	private String[] divideStringBySeparator(String valor, String separador){

		int count = countSeparator(valor, separador); 
		int index = 0;
		int passaSeparador = 0;
		String valorCapturado = STRING_VAZIA;
		String arrayValor[] = new String[count];
		for (int i = 0; i <= valor.length() - separador.length(); i++) {
			if (valor.substring(i, i + separador.length()).equals(separador) && passaSeparador == 0) {
				arrayValor[index++] = valorCapturado;
				passaSeparador = separador.length() - 1;
				valorCapturado = STRING_VAZIA;
			} else if (index < count) {
				if (passaSeparador > 0) {
					passaSeparador--;
				}
				valorCapturado += valor.substring(i, i + 1);
			} 
		}
		return arrayValor;
	}
	
	/**
	 * Verifica se é Numérico (BigDecimal, Double, Integer, Long ou BigInteger).
	 * 
	 * @param valor - valor a ser comprado.
	 * @return - True se for e False se não for. 
	 */
	private boolean isNumeric(Object valor) {
		return ((valor instanceof BigDecimal) || (valor instanceof Double)
				|| (valor instanceof Integer) || (valor instanceof Long)
				|| (valor instanceof BigInteger) || (valor instanceof Byte)
				|| (valor instanceof Short) || (valor instanceof Float))
				&& (valor != null);
	}

	/**
	 * Verifica se é Literal (String ou Character).
	 * 
	 * @param valor - valor a ser comprado.
	 * @return - True se for e False se não for. 
	 */
	private boolean isLiteral(Object valor) {
		return ((valor instanceof String) || (valor instanceof Character))
				&& (valor != null);
	}

	/**
	 * Verifica se é Data (Date ou Calendar).
	 * 
	 * @param valor - valor a ser comprado.
	 * @return - True se for e False se não for. 
	 */
	private boolean isDate(Object valor) {
		return ((valor instanceof Date) || (valor instanceof Calendar))
				&& (valor != null);
	}
	
	public String getPrimeiraSecao(){
		if (!this.listaSecoes.isEmpty()) {
			return divideStringBySeparator(((String)this.listaSecoes.values().toArray()[0]), SEPARADOR_SECAO)[0];
		}else {
			return null;
		}
	}
}
