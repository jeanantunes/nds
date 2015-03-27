package br.com.abril.nfe;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GerarChaveAcessoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(GerarChaveAcessoTest.class);
	
	@Test
	public void montarChaveAcesso() throws Exception {
		String chaveAcesso = "NFe42110585785244000199550010000000202000000050";
		
		try {  
            String cUF = "42"; // Código da UF do emitente do Documento Fiscal.  
            String dataAAMM = "1105"; // Ano e Mês de emissão da NF-e.  
            String cnpjCpf = "85.785.244/0001-99"; // CNPJ do emitente.  
            String mod = "55"; // Modelo do Documento Fiscal.  
            String serie = "1"; // Série do Documento Fiscal.  
            String tpEmis = "2"; // Forma de emissão da NF-e  
            String nNF = "20"; // Número do Documento Fiscal.  
            String cNF = "5"; // Código Numérico que compõe a Chave de Acesso.  
              
            StringBuilder chave = new StringBuilder();  
            chave.append(lpadTo(cUF, 2, '0'));  
            chave.append(lpadTo(dataAAMM, 4, '0'));  
            chave.append(lpadTo(cnpjCpf.replaceAll("\\D",""), 14, '0'));  
            chave.append(lpadTo(mod, 2, '0'));  
            chave.append(lpadTo(serie, 3, '0'));  
            chave.append(lpadTo(String.valueOf(nNF), 9, '0'));  
            chave.append(lpadTo(tpEmis, 1, '0'));  
            chave.append(lpadTo(cNF, 8, '0'));  
            chave.append(gerarChaveAcesso(chave.toString()));  
  
            chave.insert(0, "NFe");  
            
            
            Assert.assertEquals(chave.toString(), chaveAcesso);
            info("Chave NF-e: " + chave.toString());
		} catch (Exception e) {  
			LOGGER.error("Exception: "+ e.getMessage());  
		}  
	}	
    
	
	@Test
	public void validarChaveAcesso() throws Exception {
		String chaveAcessoInformada = "42110585785244000199550010000000202000000050";
		
		try {  
            String cUF = chaveAcessoInformada.substring(0,2); // Código da UF do emitente do Documento Fiscal.  
            String dataAAMM = chaveAcessoInformada.substring(2,6); // Ano e Mês de emissão da NF-e.  
            String cnpjCpf = chaveAcessoInformada.substring(6, 20); // CNPJ do emitente.  
            String mod = chaveAcessoInformada.substring(20, 22); // Modelo do Documento Fiscal.  
            String serie = chaveAcessoInformada.substring(22, 25); // Série do Documento Fiscal.  
            String nNF = chaveAcessoInformada.substring(25, 33); // Número do Documento Fiscal.  
            String tpEmis = chaveAcessoInformada.substring(33, 35); // Forma de emissão da NF-e  
            String cNF = chaveAcessoInformada.substring(35, 43); // Código Numérico que compõe a Chave de Acesso.  
              
            StringBuilder chave = new StringBuilder();  
            chave.append(cUF);  
            chave.append(dataAAMM);  
            chave.append(cnpjCpf);  
            chave.append(mod);  
            chave.append(serie);  
            chave.append(nNF);  
            chave.append(tpEmis);  
            chave.append(cNF);  
            chave.append(gerarChaveAcesso(chave.toString()));              
            
            Assert.assertEquals(chave.toString(), chaveAcessoInformada);
            info("Chave NF-e: " + chave.toString());
		} catch (Exception e) {  
			LOGGER.error("Exception: "+ e.getMessage());  
		}  
	}
	
    public static int gerarChaveAcesso(String chave) {  
        int total = 0;  
        int peso = 2;  
              
        for (int i = 0; i < chave.length(); i++) {  
            total += (chave.charAt((chave.length()-1) - i) - '0') * peso;  
            peso ++;  
            if (peso == 10)  
                peso = 2;  
        }  
        int resto = total % 11;  
        return (resto == 0 || resto == 1) ? 0 : (11 - resto);  
    }  
  
    public static String lpadTo(String input, int width, char ch) {  
        String strPad = "";  
  
        StringBuffer sb = new StringBuffer(input.trim());  
        while (sb.length() < width)  
            sb.insert(0,ch);  
        strPad = sb.toString();  
          
        if (strPad.length() > width) {  
            strPad = strPad.substring(0,width);  
        }  
        return strPad;  
    }  
  
    /** 
     * Log ERROR. 
     * @param error 
     */  
    private static void error(String error) {  
        System.out.println("| ERROR: " + error);  
    }  
  
    /** 
     * Log INFO. 
     * @param info 
     */  
    private static void info(String info) {  
        System.out.println("| INFO: " + info);  
    }  
	
}
