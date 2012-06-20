package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;

/**
 * Classe responsável pela exportação de Nota Fiscal Eletrônica de arquivos TXT.
 * 
 * @author Discover Technology
 * 
 */
public class NFEExporter {

	public static final String SEPARADOR_SECAO = "|"; 

	public static final String MASCARA_DATA = "yyyy-MM-dd"; 
	public static final String MASCARA_NUMBER = "#,#"; 

	public static final String STRING_VAZIA = ""; 
	
	
	/**
	 * Exporta o dados de um objeto da para uma String.
	 * 
	 * @param notaFiscal - Objeto da Nota Fiscal.
	 * @return - String com toda as seções.
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */
	public static <NF> String toString(NF notaFiscal) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String, String> listaSecoes = new HashMap<String, String>();
		
		for (Method metodo : notaFiscal.getClass().getMethods()) {
			Object valor = metodo.invoke(notaFiscal, new Object[]{});

			if (isNumeric(valor) || isDate(valor) || isLiteral(valor)) {
				NFEExports nfeExports = metodo.getAnnotation(NFEExports.class);
				NFEExport nfeExport = metodo.getAnnotation(NFEExport.class);
				
				NFEExport listaNFEExport[] = new NFEExport[(nfeExports != null? nfeExports.value().length : 0) + (nfeExport != null? 1 : 0)];
				int index = 0;
				if (nfeExports != null){
					for (NFEExport nfeExp: nfeExports.value()) {
						listaNFEExport[index++] = nfeExp;
					}
				}
				if (nfeExport != null) {
					listaNFEExport[index++] = nfeExport; 
				}

				for (NFEExport nfeExp: listaNFEExport) {
					if (nfeExp != null) {
						String secao = listaSecoes.get(nfeExp.secao());

						executarAnotacao(secao, nfeExp, valor);

						listaSecoes.put(nfeExp.secao(), secao);
					}
				}
			} else {
				toString(valor);
			}

		}

		for (Field campo : notaFiscal.getClass().getFields()) {
			campo.setAccessible(true);
			Object valor = campo.get(notaFiscal);

			if (isNumeric(valor) || isDate(valor) || isLiteral(valor)) {
				NFEExport nfeExport = campo.getAnnotation(NFEExport.class);

				if (nfeExport != null) {
					String secao = listaSecoes.get(nfeExport.secao());

					executarAnotacao(secao, nfeExport, valor);

					listaSecoes.put(nfeExport.secao(), secao);
				}

			} else {
				toString(valor);
			}
		}

		 return null;
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
	private static String executarAnotacao(String secao, NFEExport nfeExport, Object valor){
		String secaoNome = nfeExport.secao();
		int tamanho = nfeExport.tamanho();
		int posicao = nfeExport.posicao();
		String mascara = nfeExport.mascara();

		if (secao == null) {
			secao = secaoNome + SEPARADOR_SECAO;
		}

		secao = addCampoToSecao(secao,
				converteCampoParaString(valor, mascara, tamanho), posicao);

		return secao;
	}
	
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
	private static String converteCampoParaString(Object valor, String mascara, int tamanho){
		
		String valorString = null;
		String mascaraUsada;
		
		if (isDate(valor)) {

			Date data;
			mascaraUsada = (StringUtil.isEmpty(mascara)) ? MASCARA_DATA : mascara;
			if (valor instanceof Date) {
				data = (Date) valor;
			} else {
				data = ((Calendar) valor).getTime();
			}
			valorString = DateUtil.formatarData(data, mascaraUsada);
		} else if (isNumeric(valor)) {
			
			mascaraUsada = (StringUtil.isEmpty(mascara)) ? MASCARA_NUMBER : mascara;

			valorString = new DecimalFormat(mascaraUsada, new DecimalFormatSymbols(new Locale("en_US"))).format(valor);
			valorString = valorString.replace(",", "");
		} else {
			valorString = valor.toString().substring(0, tamanho);
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
	private static String addCampoToSecao(String secao, String valor, int posicao){
		
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
	private static String repetirStringNoFinal(String valor, String separador, int vezes){
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
	private static int countSeparator(String valor, String separador){
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
	private static String[] divideStringBySeparator(String valor, String separador){

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
	private static boolean isNumeric(Object valor) {
		return (valor instanceof BigDecimal) || (valor instanceof Double)
				|| (valor instanceof Integer) || (valor instanceof Long)
				|| (valor instanceof BigInteger);
	}

	/**
	 * Verifica se é Literal (String ou Character).
	 * 
	 * @param valor - valor a ser comprado.
	 * @return - True se for e False se não for. 
	 */
	private static boolean isLiteral(Object valor) {
		return (valor instanceof String) || (valor instanceof Character);
	}

	/**
	 * Verifica se é Data (Date ou Calendar).
	 * 
	 * @param valor - valor a ser comprado.
	 * @return - True se for e False se não for. 
	 */
	private static boolean isDate(Object valor) {
		return (valor instanceof Date) || (valor instanceof Calendar);
	}
}
