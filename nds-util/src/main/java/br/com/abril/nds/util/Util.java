package br.com.abril.nds.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Util {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static boolean isNumeric(String valor){
		try {
			Double.parseDouble(valor);
		} catch (NumberFormatException n){
			return false;
		}
		
		return true;
	}
	
	public static boolean isLong(String valor){
		try {
			Long.parseLong(valor);
		} catch (NumberFormatException n){
			return false;
		}
		
		return true;
	}
	
	public static <E extends Enum<E>> E getEnumByStringValue(E[] values, String stringValue) {

		for (E enumConstant : values) {

			if (enumConstant.toString().equals(stringValue)) {

				return enumConstant;
			}
		}

		return null;
	}
	
	public static boolean isAjaxRequest(HttpServletRequest request) {
		
		if (request == null) {
		
			return false;
		}
		
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}
	
	public static boolean isAjaxUpload(HttpServletRequest request) {
		
		if (request == null) {
		
			return false;
		}
		
		boolean isUploadAjax = false;
		
		String formUploadAjax = request.getParameter(Constantes.UPLOAD_AJAX_REQUEST_ATTRIBUTE);
		
		if (formUploadAjax != null) {
			isUploadAjax = new Boolean(formUploadAjax);
		}
		
		return isUploadAjax;
	}
	
	public static String gerarNossoNumeroDistribuidor(
			Integer codigoDistribuidor, 
			Date dtGeracao, 
			String numeroBanco, 
			Long idFornecedor, 
			Long idChamadaEncalheFornecedor){
		
		long n1;
		long n2 = 4;
		long n3;
		
		int[] pesos = {9, 8, 7, 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
		long x = 0;
		
		String codSacado = Util.padLeft(codigoDistribuidor.toString(), "0", 4);
		
		for (int i = codSacado.length(); i > 0; i--){
			x += Double.parseDouble(codSacado.substring(i - 1, i)) * pesos[codSacado.length() - i];
		}
		
		n1 = (x % 11);
		if ((n1 == 0) || (n1 == 10)){
			n1 = 0;
		}
		
		String auxData = DateUtil.formatarData(dtGeracao, "ddMMyy");
		
		n3 = (x % 11);
		if ((n3 == 0) || (n3 == 10)){
			n3 = 0;
		}
		
		NomeBanco nomeBanco = NomeBanco.getByNumeroBanco(numeroBanco);
		
		switch (nomeBanco) {
			case BANCO_ABN_AMRO_REAL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_BRADESCO:
				return padLeft(codSacado + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor), "0", 11);
			
			case BANCO_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_ESTADO_DO_ESPIRITO_SANTO:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
			
			case BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_NORDESTE_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_INTEMEDIUM:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_ITAU:
				return Util.padLeft(idChamadaEncalheFornecedor.toString(), "0", 8);
				
			case BANCO_RURAL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SAFRA:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SANTANDER:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SICREDI:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCOOB:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case CAIXA_ECONOMICA_FEDERAL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case HSBC:
				
				// return Util.padLeft(codSacado + auxData + idMovimentoFinanceiro, "0", 13);
				
            // Foi alterada a forma para composição do nosso número conforme foi
            // pedido.
            // De [número cota + dd/MM/yyyy + id movimento financeiro] para
            // [número cota + id movimento financeiro].
            // A forma antiga estava estourando o limite máximo de 13
            // caracteres.
				return Util.padLeft(codSacado + idChamadaEncalheFornecedor, "0", 13);
				
			case MERCANTIL_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case NOSSA_CAIXA:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
				
			case UNIBANCO:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
	
			default:
				return codSacado + auxData + n1 + n2 + n3 + idChamadaEncalheFornecedor + (idFornecedor == null ? "0" : idFornecedor);
		}

	}
	
	public static String gerarNossoNumero(
			Integer numeroCota, 
			Date dtGeracao, 
			String numeroBanco, 
			Long idFornecedor, 
			Long idDivida,
			Long agencia,
			Long contaCorrente,
			Integer carteira){
		
		String codSacado = Util.padLeft(numeroCota.toString(), "0", 4);
		
		String auxData = DateUtil.formatarData(dtGeracao, "ddMMyy");
		
		NomeBanco nomeBanco = NomeBanco.getByNumeroBanco(numeroBanco);
		
		if (nomeBanco == null){
			
			return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
		}
		
		switch (nomeBanco) {
			case BANCO_ABN_AMRO_REAL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_BRADESCO:
				return Util.padLeft(idDivida.toString(), "0", 11);
			
			case BANCO_DO_BRASIL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_ESTADO_DO_ESPIRITO_SANTO:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
			
			case BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_NORDESTE_DO_BRASIL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_INTEMEDIUM:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_ITAU:
				
//				String nossoNumero = "" + numeroCota + idMovimentoFinanceiro;
//				String nossoNumero = idMovimentoFinanceiro.toString();
//				
//				StringBuilder composto = new StringBuilder();
//				composto.append(agencia)
//						.append(contaCorrente)
//						.append(carteira)
//						.append(nossoNumero);
//				
//				composto.reverse();
//				
//				int multiplicador = 2;
//				
//				StringBuilder compostoAux = new StringBuilder();
//				
//				for (int index = 0 ; index < composto.length() ; index++){
//					
//					compostoAux.insert(0, Character.getNumericValue(composto.charAt(index)) * multiplicador);
//					
//					if (multiplicador == 2){
//						
//						multiplicador = 1;
//					} else {
//						
//						multiplicador = 2;
//					}
//				}
//				
//				int somatorio = 0;
//				for (int index = 0 ; index < compostoAux.length() ; index++){
//					
//					somatorio += Character.getNumericValue(compostoAux.charAt(index));
//				}
//				
//				somatorio = somatorio % 10;
				
            // A composição do nosso número com os cálculos acima estavam
            // ultrapassando a quantidade de caracteres [8] para o banco Itau.
				
				return Util.padLeft(idDivida.toString(), "0", 8);
				
			case BANCO_RURAL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SAFRA:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SANTANDER:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SICREDI:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCOOB:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case CAIXA_ECONOMICA_FEDERAL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case HSBC:
				return Util.padLeft(idDivida.toString(), "0", 13);
				
			case MERCANTIL_DO_BRASIL:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case NOSSA_CAIXA:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
				
			case UNIBANCO:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
	
			default:
				return codSacado + auxData + idDivida + (idFornecedor == null ? "0" : idFornecedor);
		}
	}
	
	public static String calcularDigitoVerificador(String nossoNumero,
												   String codigoCedente,
												   Date dataVencimento) {
		
		if (nossoNumero == null || codigoCedente == null || dataVencimento == null) {
			
			return null;
		}
		
		long primeiroDigito = 0;
		long segundoDigito = 4;
		long terceiroDigito = 0;
		
		primeiroDigito = Util.calcularDigito(nossoNumero);
		
		nossoNumero = nossoNumero + primeiroDigito + segundoDigito;
		
		String dataVencimentoFormatada = DateUtil.formatarData(dataVencimento, "ddMMyy");
		
		String somaValores =
			Util.obterSomaValores(nossoNumero, codigoCedente, dataVencimentoFormatada);
		
		somaValores = Util.padLeft(somaValores, "0", nossoNumero.length());
		
		terceiroDigito = Util.calcularDigito(somaValores);
		
		StringBuilder digitoVeficadorFinal = new StringBuilder();
		
		digitoVeficadorFinal.append(primeiroDigito);
		digitoVeficadorFinal.append(segundoDigito);
		digitoVeficadorFinal.append(terceiroDigito);
		
		return digitoVeficadorFinal.toString();
	}

	private static String obterSomaValores(String nossoNumero,
										   String codigoSacado,
										   String dataVencimentoFormatada) {
					
		BigInteger soma = new BigInteger(nossoNumero)
			.add(new BigInteger(codigoSacado))
			.add(new BigInteger(dataVencimentoFormatada));
			
		return soma.toString();
	}

	private static long calcularDigito(String valor) {
		
		int[] pesos = new int[valor.length()];
		
		int valorPeso = 9;
		
		for (int i = valor.length(); i > 0; i--) {
			
			pesos[i -1] = valorPeso--;
			
			if (valorPeso < 2) {
				valorPeso = 9;
			}
		}
		
		int x = 0;
		
		for (int i = valor.length(); i > 0; i--) {
			x += Integer.valueOf(valor.substring(i - 1, i)) * pesos[i - 1];
		}
		
		long digito = (x % 11);
		
		if (digito == 10) {
			digito = 0;
		}
		
		return digito;
	}
	
	public static String padLeft(String valor, String caractere, int tamanho){
		while (valor.length() < tamanho){
			valor = caractere + valor;
		}
		
		return valor;
	}
	
	public static String adicionarMascaraCNPJ(String cnpj){
		
		if (cnpj == null){
			return "";
		}
		
		cnpj = cnpj.replace("-", "").replace(".", "").replace("/", "");
		
		if (cnpj.length() < 14){
			cnpj = Util.padLeft(cnpj, "0", 14);
		}
		
		StringBuilder formatado = new StringBuilder();
		formatado.append(cnpj.substring(0, 2)).append(".").append(cnpj.substring(2, 5)).append(".").append(cnpj.substring(5, 8)).append("/").append(cnpj.substring(8, 12)).append("-").append(cnpj.substring(12, 14));
		
		return formatado.toString();
	}
	
	public static String adicionarMascaraCPF(String cpf){
		
		if (cpf == null){
			return "";
		}
		
		cpf = cpf.replace("-", "").replace(".", "");
		
		if (cpf.length() < 11){
			cpf = Util.padLeft(cpf, "0", 11);
		}
		
		StringBuilder formatado = new StringBuilder();
		formatado.append(cpf.substring(0, 3)).append(".").append(cpf.substring(3, 6)).append(".").append(cpf.substring(6, 9)).append("-").append(cpf.substring(9, 11));
		
		return formatado.toString();
	}
	
	public static String adicionarMascaraCEP(String cep) {
		
		if (cep == null) return "";

		cep = cep.replace("-", "").trim();
		
		if (cep.length() < 8) {
			cep = Util.padLeft(cep, "0", 8);
		}
		
		StringBuilder formatado = new StringBuilder();
		formatado.append(cep.substring(0, 5)).append("-").append(cep.substring(5, 8));
		
		return formatado.toString();
	}
	
	public static boolean validarEmail(final String email){
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
	
	/** Classe que representa a forma de agrupamento das UFs brasileiras. */
	public enum TipoAgrupamentoUf {
		ORDEM_ALFABETICA,
		DIVISAO_GEOGRAFICA;
	}
	
	        /**
     * Retorna a lista de UFs brasileiras.<br>
     * A lista pode vir ordenada em:<br>
     * <ul>
     * <li>Ordem Alfabética; (Ordenação padrão);</li>
     * <li>Divisão Geográfica (norte, nordestes, centro-oeste, sudeste e sul);</li>
     * </ul>
     * 
     * @param tpUf Determina o tipo de ordenação.<br>
     *            Se for passado <i>null</i> será utilizado a ordenação padrão
     *            (ordem alfabética);
     * 
     * @return
     */
	public static List<UfEnum> getUfs(TipoAgrupamentoUf tpUf) {
		
		List<UfEnum> lst = new ArrayList<UfEnum>();
		
        // Região Norte:
		lst.add(UfEnum.AC);	// Acre
		lst.add(UfEnum.AM);	// Amazonas	
        lst.add(UfEnum.AP); // Amapá
        lst.add(UfEnum.PA); // Pará
        lst.add(UfEnum.RO); // Rondônia
		lst.add(UfEnum.RR);	// Roraima
		lst.add(UfEnum.TO);	// Tocantins
		
        // Região Nordeste:
		lst.add(UfEnum.AL);	// Alagoas
		lst.add(UfEnum.BA);	// Bahia
        lst.add(UfEnum.CE); // Ceará
        lst.add(UfEnum.MA); // Maranhão
        lst.add(UfEnum.PB); // Paraíba
		lst.add(UfEnum.PE);	// Pernambuco
        lst.add(UfEnum.PI); // Piauí
		lst.add(UfEnum.RN);	// Rio Grande do Norte
		lst.add(UfEnum.SE);	// Sergipe
		
        // Região Centro-Oeste:
		lst.add(UfEnum.DF);	// Distrito Federal
        lst.add(UfEnum.GO); // Goiás
		lst.add(UfEnum.MS);	// Mato Grosso do Sul
		lst.add(UfEnum.MT);	// Mato Grosso
		
        // Região Sudeste:
        lst.add(UfEnum.ES); // Espírito Santo
		lst.add(UfEnum.MG);	// Minas Gerais
		lst.add(UfEnum.RJ);	// Rio de Janeiro
        lst.add(UfEnum.SP); // São Paulo
		
        // Região Sul:
        lst.add(UfEnum.PR); // Paraná
		lst.add(UfEnum.RS);	// Rio Grande do Sul
		lst.add(UfEnum.SC);	// Santa Catarina
		
		
        // Ordena por ordem alabética as siglas se for diferente de divisão
        // geográfica:
		if (!TipoAgrupamentoUf.DIVISAO_GEOGRAFICA.equals(tpUf)) {
			Collections.sort(lst, new Comparator<UfEnum>() {
				@Override
				public int compare(UfEnum o1, UfEnum o2) {
					return o1.getSigla().compareTo(o2.getSigla());
				}
			});
		}
		
		return lst;
	}

	    /**
     * Retorna a string enviada criptografada em MD5
     * 
     * @param original
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String md5(String original) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
            throw e;
		}
		BigInteger hash = new BigInteger(1, md.digest(original.getBytes()));  
		return hash.toString(16);
	}
	
	/**
	 * Remove a mascara do CNPJ
	 * @param cnpj
	 * @return
	 */
	public static String removerMascaraCnpj(String cnpj){
		if(cnpj == null){
			return null;
		}		
		return cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
	}
	
	
	        /**
     * Retorna uma substring de parâmetro value quantidade de caracteres maxima
     * igual ao a do parâmetro maxChar.
     * 
     * @param value
     * @param maxChar
     * @return String
     */
	public static String truncarValor(String value, int maxChar) {
		
		if(value == null) {
			return "";
		}
		
		if(value.length()<=maxChar){
			return value;
		}
		
		return value.substring(0, maxChar);
		
		
	}
	
	
	/**
	 * Remove a mascara do CPF
	 * @param cpf
	 * @return
	 */
	public static String removerMascaraCpf(String cpf){
		if(cpf == null){
			return null;
		}		
		return cpf.replaceAll("[.-]", "");
	}
	
    /**
     * Utilitário para tratamento de valor "null" utilizando um valor "padrão"
     * 
     * @param value valor para verificação de nulo
     * @param safeValue valor padrao para utilização caso o valor seja nulo
     * @return value caso não seja nulo, safeValue caso value seja nulo
     */
    public static <T> T nvl(T value, T safeValue) {
        Validate.notNull(safeValue, "Valor padrão não deve ser nulo!");
        if (value == null) {
            return safeValue;
        }
        return value;
    }
    
    
    /**
     * Cria um identificador para o objeto
     * 
     * @param object objeto para criação do identificador
     * @return identificador gerado
     */
    public static <T> Long generateObjectId(T object) {
        return Long.valueOf(System.identityHashCode(object));
    }

    /**
     * Verifica a diferença entre o primeiro e o segundo valor, return true se a
     * dif for menor que o valorIgnorar passado no 3ºparam
     * 
     * Método útil para resolver casos de arredondamento 4 casas p/ 2 decimais
     * 
     * @param v1
     * @param v2
     * @param valorIgnorar
     * @return
     */
    public static Boolean isDiferencaMenorValor(BigDecimal v1, BigDecimal v2, BigDecimal valorIgnorar){
		
    	if(v1 == null && v2 != null)
    		return null;
    	
    	if(v1 != null && v2 == null)
    		return null;
    				
		return v1.subtract(v2).abs().setScale(4).compareTo(valorIgnorar) < 0;
    }
    
    /**
     * Verifica a diferença entre o primeiro e o segundo valor, return true se a
     * dif for menor que 0,005 (meio) centavo
     * 
     * Método útil para resolver casos de arredondamento 4 casas p/ 2 decimais
     * 
     * @param v1
     * @param v2
     * @return
     */
    public static Boolean isDiferencaMenorMeioCentavo(BigDecimal v1, BigDecimal v2){
		
		return isDiferencaMenorValor(v1, v2, new BigDecimal("0.005"));
    }
    
    public static void main(String[] args) {
		BigDecimal v1 = new BigDecimal("7.8549");
		BigDecimal v2 = null;
		
		System.out.println(v1.setScale(2, BigDecimal.ROUND_HALF_EVEN));
		
		System.out.println(isDiferencaMenorMeioCentavo(v1, v2));
	}

    private static String toFirstUpperCase(String string) {
        
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    
    public static Object getValuePath(Object obj,String path){
        
        
        String[] pathList = path.split("\\.");
        
        String att = null;
        if(pathList.length==0){
            att=path;
        }else{
            att = pathList[0];
            
        }
        
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        
        Method getM = null;
        for (Method method : declaredMethods) {
            if(method.getName().equals("get"+toFirstUpperCase(att))){
                getM = method;
            }
        }
        
        if(getM==null){
            return null;
        }
        
        Object result =null;
        try {
             result = getM.invoke(obj, null);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        if(pathList.length==1){
            return result;
        }else{
            
            return getValuePath(result,path.substring(path.indexOf(".")+1));
        }
    }
    
    public static Object getReturnTypePath(Object obj,String path){
        
        
        String[] pathList = path.split("\\.");
        
        String att = null;
        if(pathList.length==0){
            att=path;
        }else{
            att = pathList[0];
            
        }
        
        Method[] declaredMethods = obj.getClass().getDeclaredMethods();
        
        Method getM = null;
        for (Method method : declaredMethods) {
            if(method.getName().equals("get"+toFirstUpperCase(att))){
                getM = method;
            }
        }
        
        if(getM==null){
            return null;
        }
        
        Object result =null;
        try {
             result = getM.invoke(obj, null);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        if(pathList.length==1){
            //return result;
        	return getM.getReturnType();
        }else{
            
            return getValuePath(result,path.substring(path.indexOf(".")+1));
        }
    }

	public static String encriptar(String info) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		byte[] bytesSenha = info.getBytes("UTF-8");
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		//Converte o valor da mensagem digest em base 16 (hex) 
		info = new BigInteger(1, md.digest(bytesSenha)).toString(16);
		
		return info;
	} 
}
