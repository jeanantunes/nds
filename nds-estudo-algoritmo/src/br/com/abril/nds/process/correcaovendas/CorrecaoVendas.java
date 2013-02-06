package br.com.abril.nds.process.correcaovendas;

import br.com.abril.nds.dao.CotaDAO;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.Medias;
import br.com.abril.nds.process.MontaTabelaEstudos;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.VendaCrescente;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil
 * definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">SubProcessos:
 *      - {@link CorrecaoIndividual}
 *      - {@link CorrecaoTendencia}
 *      - {@link VendaCrescente}
 * Processo Pai:
 *      - N/A
 * 
 * Processo Anterior: {@link MontaTabelaEstudos}
 * Próximo Processo: {@link Medias}</p>
 */
public class CorrecaoVendas extends ProcessoAbstrato {

	public CorrecaoVendas(Estudo estudo) {
		super(estudo);
	}

	public CorrecaoVendas() {
	}

	@Override
    protected void executarProcesso() {
    	
    	//Criar Logica para chamar subProcesso
		super.estudo = new Estudo();
		super.estudo.setCotas(new CotaDAO().getCotas());
		
    	CorrecaoIndividual correcaoIndividual = new CorrecaoIndividual(super.estudo);
    	
    	correcaoIndividual.executar();
    	
    	CorrecaoTendencia correcaoTendencia = new CorrecaoTendencia(super.estudo);
    	
    	correcaoTendencia.executar();
    	
    	super.estudo = correcaoIndividual.getEstudo();
    	
    }

    @Override
    protected void calcular() {
        // TODO: implementar método calcular do Processo CorrecaoVendas
    }
    
}
