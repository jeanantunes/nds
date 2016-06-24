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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.fiscal.nota.NotaFiscalEnum;
import br.com.abril.nds.util.CampoSecao;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.comparator.NFESecaoComparator;

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
	 * Lista de objetos usados para recursividade
	 */
	public List<NFEExporter> listaNFEExporters;
	
	    /**
     * Contador de iteração da listaNFEExporters
     */
	private Integer indexListaNFEExporters;
	
	    /**
     * Lista das sessoes do arquivo de exportação
     */
	private TreeMap<TipoSecao, List<CampoSecao>> mapSecoes = new TreeMap<TipoSecao, List<CampoSecao>>(new NFESecaoComparator());
	
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
		this.mapSecoes = new TreeMap<TipoSecao, List<CampoSecao>>(new NFESecaoComparator());
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
		this.execute(notaFiscal, new ArrayList<Object>(), null);
	}
	
	    /**
     * Faz a varredura de um objeto buscando as anotações NFEExport ou
     * NFEExports e adicona os dados nas lista de seções. E mantem o históricos
     * dos objetos Pais.
     * 
     * @param notaFiscal
     * @param listaParents
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	private <NF> void execute(NF notaFiscal, List<Object> listaParents, TipoSecao[] secaoVazia) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		if (listaParents != null) {
			listaParents.add(notaFiscal);
		}
		
		if (secaoVazia != null) {
			for (TipoSecao tipoSecao: secaoVazia) {
				if (tipoSecao != null && !TipoSecao.EMPTY.equals(tipoSecao)){
					addSecaoVazia(tipoSecao);
				}
			}
		}
		
		List<Field> campos = new ArrayList<Field>();
		List<Method> metodos = new ArrayList<Method>();
		
		Class<?> clazz = null;
		
		if (notaFiscal != null) 
			clazz = notaFiscal.getClass();
		
		while (clazz != null) {
			campos.addAll(Arrays.asList(clazz.getDeclaredFields()));
			metodos.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			clazz = clazz.getSuperclass();
		}

		for (Field campo : campos) {
			campo.setAccessible(true);
			Object valor = campo.get(notaFiscal);
			this.processarAnnotations(campo, listaParents, notaFiscal, valor);
		}		
	
		for (Method metodo : metodos) {
			NFEExportType nfeExportType =  metodo.getAnnotation(NFEExportType.class);
			NFEWhens nfeWhens = metodo.getAnnotation(NFEWhens.class);
			NFEWhen nfeWhen = metodo.getAnnotation(NFEWhen.class);
			NFEExport nfeExport = metodo.getAnnotation(NFEExport.class);
			NFEExports nfeExports = metodo.getAnnotation(NFEExports.class);
			
			if (nfeWhens != null || nfeWhen != null || nfeExport!= null || nfeExports != null || nfeExportType != null) {
				if (metodo.getParameterTypes().length == 0 && !metodo.getReturnType().equals(Void.TYPE)) {
					Object valor = metodo.invoke(notaFiscal, new Object[] {});
					this.processarAnnotations(metodo, listaParents, notaFiscal, valor);
				}
			}
		}
		
		if (listaParents != null) {
			listaParents.remove(notaFiscal);
		}
	}
	
	/**
	 * 
	 * @param objeto
	 * @param listaParents
	 * @param notaFiscal
	 * @param valor
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	private <NF> void processarAnnotations(AccessibleObject objeto, List<Object> listaParents, NF notaFiscal, Object valor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		NFEExportType nfeExportType =  objeto.getAnnotation(NFEExportType.class);
		NFEWhens nfeWhens = objeto.getAnnotation(NFEWhens.class);
		NFEWhen nfeWhen = objeto.getAnnotation(NFEWhen.class);
		NFEExport nfeExport = objeto.getAnnotation(NFEExport.class);
		NFEExports nfeExports = objeto.getAnnotation(NFEExports.class);
		
		if (isNumeric(valor) || isDate(valor) || isLiteral(valor) || isEnum(valor)) {
			
			if(nfeWhens != null){
				for(NFEWhen when: nfeWhens.value()){
					if(when.condition().valid(valor) && when.condition().validParent(notaFiscal) && when.condition().validParents(listaParents)){						
						addCampoSecao(when.export(), valor);
					}
				}
			}
								
			if(nfeWhen != null){
				if(nfeWhen.condition().valid(valor) && nfeWhen.condition().validParent(notaFiscal) && nfeWhen.condition().validParents(listaParents)){						
					addCampoSecao(nfeWhen.export(), valor);
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

		} else if (valor instanceof Collection && nfeExportType != null) {
			
			for (Object valorCollection : (Collection) valor) {
				TipoSecao secaoVazia[] = nfeExportType.secaoPadrao();
				if (TipoSecao.EMPTY.equals(secaoVazia)) {
					secaoVazia = null;
				}
				NFEExporter nfeExporter = new NFEExporter();
				nfeExporter.clear();
				nfeExporter.execute(valorCollection, listaParents, secaoVazia);
				listaNFEExporters.add(nfeExporter);
			}

		} else if (valor != null && nfeExportType != null) {
            TipoSecao secaoVazias[] = nfeExportType.secaoPadrao();
            for (TipoSecao secaoVazia : secaoVazias) {
                if (TipoSecao.EMPTY.equals(secaoVazia)) {
                    secaoVazia = null;
                }
            }
            
            execute(valor, listaParents, secaoVazias);
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
	 * Adiciona secao vazia
	 * 
	 * @param secao
	 */
	private void addSecaoVazia(TipoSecao secao){
		List<CampoSecao> camposSecao = mapSecoes.get(secao);
		
		if (camposSecao == null || camposSecao.isEmpty()) {
			camposSecao = new ArrayList<CampoSecao>();
		}
		
		this.mapSecoes.put(secao, camposSecao);
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
		CampoSecao versao = new CampoSecao(TipoSecao.A,  0, "3.10");
		addCampoSecao(versao);
		CampoSecao modeloDocuemtno = new CampoSecao(TipoSecao.B,  4, "55");
		addCampoSecao(modeloDocuemtno);
		CampoSecao tipoImpressao = new CampoSecao(TipoSecao.B,  12, "1");
		addCampoSecao(tipoImpressao);
		CampoSecao tipoAmbiente = new CampoSecao(TipoSecao.B,  15, "1");
		addCampoSecao(tipoAmbiente);
		CampoSecao processoEmissao = new CampoSecao(TipoSecao.B,  17, "0");
		addCampoSecao(processoEmissao);
		CampoSecao codigoPaisEmitente = new CampoSecao(TipoSecao.C05,  8, "1058");
		addCampoSecao(codigoPaisEmitente);
		CampoSecao nomePaisEmitente = new CampoSecao(TipoSecao.C05,  9, "BRASIL");
		addCampoSecao(nomePaisEmitente);
		CampoSecao codigoPaisDestinatario = new CampoSecao(TipoSecao.E05,  8, "1058");
		addCampoSecao(codigoPaisDestinatario);
		CampoSecao nomePaisDestinatario = new CampoSecao(TipoSecao.E05,  9, "BRASIL");
		addCampoSecao(nomePaisDestinatario);
	}
	
	    /**
     * Converte as seções em Strings.
     * 
     * @see java.lang.Object#toString()
     */
	public String toString() {
		
		StringBuilder sBuilder = new StringBuilder();
		
		ordenaListaNFEEporters();
		
		String primeiraSecao = null;
		
		this.indexListaNFEExporters = 0;
		
		if (!this.listaNFEExporters.isEmpty()) { 
			primeiraSecao = this.listaNFEExporters.get(this.indexListaNFEExporters).getPrimeiraSecao();
		}
		
		for (Entry<TipoSecao, List<CampoSecao>> entry : this.mapSecoes.entrySet()) {
			
			sBuilder.append(listaNFEExportersToString(primeiraSecao, entry.getKey()));
			
			String sSecao = this.gerarStringSecao(entry.getKey());
			
			List<CampoSecao> campos = entry.getValue();
			
			for (CampoSecao campo : campos) {
				
				String sCampo = this.converteCampoParaString(campo);
				
				sSecao = this.addStringCampoToStringSecao(sSecao, sCampo, campo.getPosicao());
			}
			
			sBuilder.append(sSecao);
		}
		
		sBuilder.append(listaNFEExportersToString(primeiraSecao, null));
		
		return sBuilder.toString();
	}
	
	
	/**
	 * @param primeiraSecao
	 * @param indexListaNFEExporters
	 * @param secao
	 * @return
	 */
	private String listaNFEExportersToString(String primeiraSecao, TipoSecao secao){
		
		StringBuilder sBuilder = new StringBuilder();
		
		if (primeiraSecao != null && (secao == null || secao.getSigla().compareToIgnoreCase(primeiraSecao) >= 0)) {
		
			boolean repetir;
			
			do {
			
				if (this.listaNFEExporters.size() > this.indexListaNFEExporters) {
					sBuilder.append(this.listaNFEExporters.get(this.indexListaNFEExporters).toString());
					primeiraSecao = this.listaNFEExporters.get(this.indexListaNFEExporters++).getPrimeiraSecao();
					repetir = primeiraSecao != null && primeiraSecao.compareToIgnoreCase(this.listaNFEExporters.get(this.indexListaNFEExporters - 1).getPrimeiraSecao()) == 0;
				} else {
					primeiraSecao = null;
					repetir = false;
				}
			
			} while (repetir);

		}
		
		return sBuilder.toString();
	}
	
	
	/**
	 * 
	 */
	private void ordenaListaNFEEporters(){
		NFEExporter nfeExporterAux;

		for (int i = 0; i < this.listaNFEExporters.size() - 1; i++) {
			for (int j = i + 1; j < this.listaNFEExporters.size(); j++) {
				if (this.listaNFEExporters.get(i).getPrimeiraSecao() != null && this.listaNFEExporters.get(i).getPrimeiraSecao().isEmpty()) {
					if(this.listaNFEExporters.get(i).getPrimeiraSecao()
							.compareToIgnoreCase(this.listaNFEExporters.get(j).getPrimeiraSecao()) > 0 ){
						nfeExporterAux = this.listaNFEExporters.get(i);
						this.listaNFEExporters.set(i, this.listaNFEExporters.get(j));
						this.listaNFEExporters.set(j, nfeExporterAux);
					}
				}
			}
		}
	}
	
	
	    /**
     * Converte um Tipo Secao para string utilizando pipes para preencher todas
     * as posições que a secao pode ter
     * 
     * EX: seção A possui 8 posições A|||||||||
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
		Integer tamanho = campo.getTamanho();
		
		String valorString = STRING_VAZIA;
		String mascaraUsada;
		boolean isNotaFiscalEnum = false;
		
		if (valor != null) {
			
			if (isEnum(valor)) {
				for (Class<?> interfaceClazz: valor.getClass().getInterfaces()) {
					isNotaFiscalEnum = isNotaFiscalEnum || NotaFiscalEnum.class.equals(interfaceClazz); 
				}
			}
			
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
			
			} else if (isNotaFiscalEnum) {
				valorString = ((NotaFiscalEnum)valor).getIntValue().toString();
			} else {
				
				valorString = String.valueOf(valor);
				
				if(!StringUtil.isEmpty(valorString)) {
					if ((tamanho != null && tamanho > 0) && (valorString.length() > tamanho)) {
						valorString = valorString.replace(SEPARADOR_SECAO, STRING_VAZIA).substring(0, tamanho);
					}
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
		return (valor != null)
				&& ((valor instanceof BigDecimal) || (valor instanceof Double)
						|| (valor instanceof Integer)
						|| (valor instanceof Long)
						|| (valor instanceof BigInteger)
						|| (valor instanceof Byte) || (valor instanceof Short) || (valor instanceof Float));
	}

	    /**
     * Verifica se é Literal (String ou Character).
     * 
     * @param valor - valor a ser comprado.
     * @return - True se for e False se não for.
     */
	private boolean isLiteral(Object valor) {
		return (valor != null)
				&& ((valor instanceof String) || (valor instanceof Character));
	}

	    /**
     * Verifica se é Data (Date ou Calendar).
     * 
     * @param valor - valor a ser comprado.
     * @return - True se for e False se não for.
     */
	private boolean isDate(Object valor) {
		return (valor != null)
				&& ((valor instanceof Date) || (valor instanceof Calendar));
	}
	
	    /**
     * Verifica se é Enum
     * 
     * @param valor
     * @return
     */
	private boolean isEnum(Object valor) {
		return (valor != null) && valor.getClass().getEnumConstants() != null;
	}
	
	public String getPrimeiraSecao(){
		if (this.mapSecoes != null && !this.mapSecoes.isEmpty()) {
			return ((TipoSecao)this.mapSecoes.keySet().toArray()[0]).getSigla();
		}else {
			return null;
		}
	}
}
