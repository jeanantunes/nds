package br.com.abril.nds.process.definicaobases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.MonthDay;

import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.PreparaEstudoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link DefinicaoBases}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link BaseParaSaidaVeraneio}
 * </p>
 */
public class BaseParaVeraneio extends ProcessoAbstrato {

    private PreparaEstudoService preparaEstudoService = new PreparaEstudoService();

    public BaseParaVeraneio(Estudo estudo) {
	super(estudo);
    }

    @Override
    protected void executarProcesso() throws Exception {
	Estudo estudo = super.getEstudo();
	//copia lista para não afetar o loop após modificações.
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>(estudo.getEdicoesBase());
	
	for (ProdutoEdicao produtoEdicao : edicoes) {
	    if(estudo.isPracaVeraneio()) {
		if(validaPeriodoVeranio(produtoEdicao.getDataLancamento())) {
		    produtoEdicao.setPeso(2);
		    adicionarEdicoesAnterioresAoEstudo(produtoEdicao);
		} else {
		    //TODO: adicionar bases de saida de veraneio
		}
	    }
	}
    }

    private void adicionarEdicoesAnterioresAoEstudo(ProdutoEdicao produtoEdicao) throws Exception {
	List<ProdutoEdicao> edicoesAnosAnteriores = preparaEstudoService.buscaEdicoesAnosAnterioresVeraneio(produtoEdicao);
	if(edicoesAnosAnteriores.isEmpty()) {
	    throw new Exception("Não foram encontradas outras bases para veraneio, favor inserir bases manualmente.");
	}
	for (ProdutoEdicao edicao : edicoesAnosAnteriores) {
	    edicao.setPeso(2);
	}
	super.getEstudo().getEdicoesBase().addAll(edicoesAnosAnteriores);
    }

    private boolean validaPeriodoVeranio(Date dataLancamento) {
	MonthDay md20Dezembro = MonthDay.parse(DataReferencia.DEZEMBRO_20.getData());
	MonthDay md28Fevereiro = MonthDay.parse(DataReferencia.FEVEREIRO_28.getData());
	MonthDay dtLancamento = new MonthDay(dataLancamento);

	return dtLancamento.isAfter(md20Dezembro) || dtLancamento.isBefore(md28Fevereiro);
    }

}
