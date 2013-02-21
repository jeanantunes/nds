package br.com.abril.nds.process.medias;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.bonificacoes.Bonificacoes;
import br.com.abril.nds.process.correcaovendas.CorrecaoVendas;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link CorrecaoVendas} Próximo Processo:
 * {@link Bonificacoes}
 * </p>
 */
public class Medias extends ProcessoAbstrato {

    public Medias(Cota cota) {
	super(cota);
    }
    
    @Override
    protected void executarProcesso() {
	
//	BigDecimal vendaMediaCorrigida = BigDecimal.ZERO;
//	
//	List<EstoqueProdutoCota> listEstoqueProdutoCota = ((Cota) super.genericDTO).getEstoqueProdutoCotas();
//	
//	int qtdeEdicaoBase = listEstoqueProdutoCota.size();
//	
//	if(qtdeEdicaoBase < 3) {
//	    EstoqueProdutoCota estoqueProdutoCota = 
//	} else {
//	    
//	}
    }

}
