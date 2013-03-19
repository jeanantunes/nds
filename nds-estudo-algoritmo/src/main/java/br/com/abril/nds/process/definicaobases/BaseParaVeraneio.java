package br.com.abril.nds.process.definicaobases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.MonthDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.EstudoServiceEstudo;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link DefinicaoBases}
 * 
 * Processo Anterior: N/A Próximo Processo: {@link BaseParaSaidaVeraneio}
 * </p>
 */
@Component
public class BaseParaVeraneio extends ProcessoAbstrato {

    @Autowired
    private EstudoServiceEstudo estudoServiceEstudo;

    @Override
    protected void executarProcesso() throws Exception {
	Estudo estudo = super.getEstudo();
	// copia lista para não afetar o loop após modificações.
	List<ProdutoEdicaoBase> edicoes = new ArrayList<ProdutoEdicaoBase>(estudo.getEdicoesBase());

	for (ProdutoEdicaoBase produtoEdicao : edicoes) {
	    if (estudo.isPracaVeraneio()) {
		if (validaPeriodoVeranio(produtoEdicao.getDataLancamento())) {
		    produtoEdicao.setPeso(BigDecimal.valueOf(2));
		    adicionarEdicoesAnterioresAoEstudo(produtoEdicao);
		} else {
		    adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(produtoEdicao);
		}
	    }
	}
    }

    private void adicionarEdicoesAnterioresAoEstudoSaidaVeraneio(ProdutoEdicaoBase produtoEdicao) {
	List<ProdutoEdicaoBase> edicoesAnosAnterioresSaidaVeraneio = estudoServiceEstudo.buscaEdicoesAnosAnterioresSaidaVeraneio(produtoEdicao);
	if (!edicoesAnosAnterioresSaidaVeraneio.isEmpty()) {
	    super.getEstudo().getEdicoesBase().addAll(edicoesAnosAnterioresSaidaVeraneio);
	}
    }

    private void adicionarEdicoesAnterioresAoEstudo(ProdutoEdicaoBase produtoEdicaoBase) throws Exception {
	List<ProdutoEdicaoBase> edicoesAnosAnteriores = estudoServiceEstudo.buscaEdicoesAnosAnterioresVeraneio(produtoEdicaoBase);
	if (edicoesAnosAnteriores.isEmpty()) {
	    throw new Exception("Não foram encontradas outras bases para veraneio, favor inserir bases manualmente.");
	}
	for (ProdutoEdicaoBase edicao : edicoesAnosAnteriores) {
	    edicao.setPeso(BigDecimal.valueOf(2));
	}
	super.getEstudo().getEdicoesBase().addAll(edicoesAnosAnteriores);
    }

    private boolean validaPeriodoVeranio(Date dataLancamento) {
	MonthDay inicioVeraneio = MonthDay.parse(DataReferencia.DEZEMBRO_20.getData());
	MonthDay fimVeraneio = MonthDay.parse(DataReferencia.FEVEREIRO_15.getData());
	MonthDay dtLancamento = new MonthDay(dataLancamento);

	return dtLancamento.isAfter(inicioVeraneio) || dtLancamento.isBefore(fimVeraneio);
    }

}
