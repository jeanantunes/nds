package br.com.abril.nds.process;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;

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

    public void executar(Date data, Integer fornecedor) {
        
    	CorrecaoVendas correcaoVendas = new CorrecaoVendas();
       
        correcaoVendas.executar();
        /*
        Medias medias = new Medias(correcaoVendas.getEstudo());
        medias.executar();
        */
        Estudo estudoReturn = correcaoVendas.getEstudo();
        System.out.println("Estudo Output : "+ estudoReturn.getCotas());
    }
    
    public static void main(String args[]) {
        new Principal().executar(new Date(), 1);
    }
    
    public List<Cota> loadCotas() {
    	return new CotaDAO().getCotas();
    }
    
    public void carregarParametros() {
    	
    }
}
