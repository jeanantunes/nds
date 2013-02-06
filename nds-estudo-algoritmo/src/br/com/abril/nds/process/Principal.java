package br.com.abril.nds.process;

import java.util.Date;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estudo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link DefinicaoBases}
 *      - {@link SomarFixacoes}
 *      - {@link VerificarTotalFixacoes}
 *      - {@link MontaTabelaEstudos}
 *      - {@link CorrecaoVendas}
 *      - {@link Medias}
 *      - {@link Bonificacoes}
 *      - {@link AjusteCota}
 *      - {@link JornaleirosNovos}
 *      - {@link VendaMediaFinal}
 *      - {@link AjusteReparte}
 *      - {@link RedutorAutomatico}
 *      - {@link ReparteMinimo}
 *      - {@link ReparteProporcional}
 *      - {@link EncalheMaximo}
 *      - {@link ComplementarAutomatico}
 *      - {@link CalcularReparte}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: N/A
 * Próximo Processo: N/A</p>
 */
public class Principal {

	private Estudo estudo = new Estudo();
	
    public void executar(Date data, Integer fornecedor) {
        try {
			new DefinicaoBases().executar(estudo);
			new CalcularReparte().executar(estudo);
        	System.out.println("Resultado: "+ estudo);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main(String args[]) {
        new Principal().executar(new Date(), 1);
    }
    
    public void loadCotas() {
    	// TODO: carregar os Ajustes de Reparte junto com as cotas ou após o carregamento delas
    	estudo.setCotas(new CotaDAO().getCotas());
    }
    
    public void carregarParametros() {
    	
    }
}
