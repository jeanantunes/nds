package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.reflect.AccessibleObject;
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
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.CampoSecao;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoSecao;

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
	
	public List<NFEExporter> listaNFEExporters;
	
	/**
	 * Lista das sessoes do arquivo de exportação
	 */
	private Map<TipoSecao, List<CampoSecao>> mapSecoes = new HashMap<TipoSecao, List<CampoSecao>>();
	
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
		this.mapSecoes = new HashMap<TipoSecao, List<CampoSecao>>();
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
		
		Field[] campos = notaFiscal.getClass().getDeclaredFields();
		
		for (Field campo : campos) {
			campo.setAccessible(true);
			
			Object valor = campo.get(notaFiscal);
			this.processarAnnotations(campo, notaFiscal, valor);
		}		
		
		Method[] metodos = notaFiscal.getClass().getDeclaredMethods();
		
//		for (Method metodo : metodos) {
//			if (metodo.getParameterTypes().length == 0) {
//				Object valor = metodo.invoke(notaFiscal, new Object[] {});
//				this.processarAnnotations(metodo, notaFiscal, valor);
//			}
//		}
		
	}
	
	
	/**
	 * @param objeto
	 * @param notaFiscal
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	private <NF> void processarAnnotations(AccessibleObject objeto, NF notaFiscal, Object valor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		NFEWhens nfeWhens = objeto.getAnnotation(NFEWhens.class);
		NFEExport nfeExport = objeto.getAnnotation(NFEExport.class);
		NFEExports nfeExports = objeto.getAnnotation(NFEExports.class);
		
		if (isNumeric(valor) || isDate(valor) || isLiteral(valor)) {
			
			if(nfeWhens != null){
				for(NFEWhen when: nfeWhens.value()){
					if(when.condition().valid(valor)){						
						addCampoSecao(when.export(), valor);
					}
				}
			}
								
			if (nfeExports != null) {					
				for (NFEExport nfeExp: nfeExports.value()) {
					addCampoSecao(nfeExp, valor);
				}
			}
			
			if (nfeExport != null) {
				addCampoSecao(nfeExport, valor);
			}

		} else if (valor instanceof Collection) {
			
			for (Object valorCollection : (Collection) valor) {
				NFEExporter nfeExporter = new NFEExporter();
				nfeExporter.clear();
				nfeExporter.execute(valorCollection);
				listaNFEExporters.add(nfeExporter);
			}

		} else if (valor != null) {
			execute(valor);
		}
	}
		

	/**
	 * Adiciona um campo em uma seção.
	 * 
	 * @param novoCampo campo
	 */
	private void addCampoSecao(CampoSecao novoCampo) {
			
		List<CampoSecao> camposSecao = mapSecoes.get(novoCampo.getSessao());
		
		if (camposSecao == null || camposSecao.isEmpty()) {
			camposSecao = new ArrayList<CampoSecao>();
		}
		
		camposSecao.add(novoCampo);
		
		this.mapSecoes.put(novoCampo.getSessao(), camposSecao);
	}
	
	
	/**
	 * Cria um campo a partir da annotation e adiciona o campo a seção. 
	 * 
	 * @param nfeExport annotation
	 * @param valor valor do campo
	 */
	public void addCampoSecao(NFEExport nfeExport,Object valor){
		
		CampoSecao campoSessao = new CampoSecao();
		campoSessao.setPosicao(nfeExport.posicao());
		campoSessao.setMascara(nfeExport.mascara());
		campoSessao.setSessao(nfeExport.secao());
		campoSessao.setTamanho(nfeExport.tamanho());
		campoSessao.setValor(valor);
		
		addCampoSecao(campoSessao);
	}
	
	public String gerarArquivo() {
		addCamposDefault();
		
		return toString();
	}

	/**
	 * Adiciona valores padrões no documento.
	 */
	private void addCamposDefault() {				
//		CampoSecao versao = new CampoSecao(TipoSecao.A,  0, "2.0");
//		addCampoSecao(versao);
		//TODO: campos padroes;
	}
		
	
	/**
	 * Converte as seções em Strings.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuffer sBuffer = new StringBuffer();
		
		Set<TipoSecao> secoes = this.mapSecoes.keySet();
		
		ordenaListaNFEEporters();
		
		String primeiraSecao = null;
		
		int indexListaNFEExporters = 0;
		
		if (!this.listaNFEExporters.isEmpty()) { 
			primeiraSecao = this.listaNFEExporters.get(indexListaNFEExporters).getPrimeiraSecao();
		}
		
		for (TipoSecao secao : secoes) {
			
			sBuffer.append(listaNFEExportersToString(primeiraSecao, indexListaNFEExporters, secao));
			
			String sSecao = this.gerarStringSecao(secao);
			
			List<CampoSecao> campos = this.mapSecoes.get(secao);
			
			for (CampoSecao campo : campos) {
				
				String sCampo = this.converteCampoParaString(campo);
				
				sSecao = this.addStringCampoToStringSecao(sSecao, sCampo, campo.getPosicao());
			}
			
			sBuffer.append(sSecao);
		}
		
		sBuffer.append(listaNFEExportersToString(primeiraSecao, indexListaNFEExporters, null));
		
		return sBuffer.toString();
	}
	
	
	/**
	 * @param primeiraSecao
	 * @param indexListaNFEExporters
	 * @param secao
	 * @return
	 */
	private String listaNFEExportersToString(String primeiraSecao, int indexListaNFEExporters, TipoSecao secao){
		
		StringBuffer sBuffer = new StringBuffer();
		
		if (primeiraSecao != null && (secao == null || secao.getSigla().compareToIgnoreCase(primeiraSecao) >= 0)) {
		
			boolean repetir;
			
			do {
			
				if (this.listaNFEExporters.size() > indexListaNFEExporters) {
					sBuffer.append(this.listaNFEExporters.get(indexListaNFEExporters).toString());
					primeiraSecao = this.listaNFEExporters.get(indexListaNFEExporters++).getPrimeiraSecao();
					repetir = primeiraSecao != null && primeiraSecao.compareToIgnoreCase(this.listaNFEExporters.get(indexListaNFEExporters - 1).getPrimeiraSecao()) == 0;
				} else {
					primeiraSecao = null;
					repetir = false;
				}
			
			} while (repetir);

		}
		
		return sBuffer.toString();
	}
	
	
	/**
	 * 
	 */
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
	 * Converte um Tipo Secao para string utilizando pipes para preencher todas as posições que a secao pode ter
	 * 
	 * EX: seção A possui 8 posições
	 * 	   A|||||||||
	 * 
	 * @param secao tipo seção;
	 * @return
	 */
	private String gerarStringSecao(TipoSecao secao) {
		StringBuilder sSecao = new StringBuilder();
			sSecao.append(secao.getSigla());
			sSecao.append(StringUtils.repeat(SEPARADOR_SECAO, secao.getTamanhoMaximo()+1));
			sSecao.append(QUEBRA_LINHA);
		return sSecao.toString();
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
	private String converteCampoParaString(CampoSecao campo){
		
		Object valor = campo.getValor();
		String mascara = campo.getMascara();
		int tamanho = campo.getTamanho();
		
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
	 * Adiciona uma string de campo à uma string da seção.
	 * 
	 * @param secao - Linha da seção atual.
	 * @param valor - Valor a ser incluído.
	 * @param posicao - Posição que será incluído na linha.
	 * @return Linha da seção com o campo adicionado.
	 */
	private String addStringCampoToStringSecao(String secao, String valor, int posicao) {
		
		String[] quebra = secao.split("\\"+SEPARADOR_SECAO);
		
		quebra[posicao+1] = valor;
		
		StringBuilder atualizada = new StringBuilder();
		
		for (int i = 0; i < quebra.length-1; i++) {
			atualizada.append(quebra[i]+SEPARADOR_SECAO);
		}
		atualizada.append(quebra[quebra.length-1]);
		return atualizada.toString();
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
	
	@SuppressWarnings("unchecked")
	public String getPrimeiraSecao(){
		if (!this.mapSecoes.isEmpty()) {
			return ((List<CampoSecao>)this.mapSecoes.values().toArray()[0]).get(0).getSessao().getSigla();
		}else {
			return null;
		}
	}
}
