package br.com.abril.nds.process.correcaovendas;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CorrecaoVendas}
 * 
 * Processo Anterior: {@link CorrecaoTendencia} Próximo Processo: N/A
 * </p>
 */
public class VendaCrescente extends ProcessoAbstrato {

    private List<ProdutoEdicao> listProdutoEdicaoFechada;

    public VendaCrescente(Estudo estudo,
	    List<ProdutoEdicao> listProdutoEdicaoFechada) {
	super(estudo);
	this.listProdutoEdicaoFechada = listProdutoEdicaoFechada;
    }

    @Override
    protected void executarProcesso() {
	BigDecimal indiceVendaCrescente = BigDecimal.ZERO;
    }

}
