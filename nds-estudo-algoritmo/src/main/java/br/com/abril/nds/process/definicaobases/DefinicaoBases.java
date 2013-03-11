package br.com.abril.nds.process.definicaobases;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.service.EstudoServiceEstudo;

/**
 * Processo que tem como objetivo buscar as edições de base para o estudo.
 * <p * style="white-space: pre-wrap;">
 * SubProcessos: - {@link BaseParaVeraneio} - {@link BaseParaSaidaVeraneio} Processo Pai: - N/A
 * 
 * Processo Anterior: N/A Próximo Processo: {@link SomarFixacoes}
 * </p>
 */
@Component
public class DefinicaoBases extends ProcessoAbstrato {

    private static final int INDEX_CORRECTION = 1;
    private static final int TRES_EDICOES = 3;
    private static final int QUATRO_COLECIONAVEIS = 4;
    private static final int TWO_YEARS = 2;

    @Autowired
    private BaseParaVeraneio baseParaVeraneio;

    @Autowired
    private EstudoServiceEstudo estudoServiceEstudo;

    @Override
    public void executarProcesso() throws Exception {
	LinkedList<ProdutoEdicaoBase> edicoesBase = estudoServiceEstudo.buscaEdicoesPorLancamento(getEstudo().getProduto());
	edicoesBase = limitarEdicoesApenasSeis(edicoesBase);
	validaApenasUmaEdicaoFechada(edicoesBase);
	excluiEdicoesComMaisDeDoisAnos(edicoesBase);
	excluiMaiorQueQuatroSeColecionavel(edicoesBase);

	getEstudo().setEdicoesBase(edicoesBase);

	baseParaVeraneio.setEstudo(getEstudo());
	baseParaVeraneio.executar();
    }

    private LinkedList<ProdutoEdicaoBase> limitarEdicoesApenasSeis(List<ProdutoEdicaoBase> edicoesBase) {
	LinkedList<ProdutoEdicaoBase> nova = new LinkedList<>();
	int qtdeParciais = 0;
	for (ProdutoEdicaoBase base : edicoesBase) {
	    if (base.isEdicaoAberta()) {
		if (nova.size() != 0) {
		    continue;
		}
		nova.add(base);
	    } else {
		if (nova.size() < 6) {
		    if (base.isParcial()) {
			if (qtdeParciais < 4) {
			    qtdeParciais++;
			} else {
			    continue;
			}
		    }
		    nova.add(base);
		}
	    }
	    if (nova.size() == 6) {
		break;
	    }
	}
	return nova;
    }

    private void validaApenasUmaEdicaoFechada(List<ProdutoEdicaoBase> edicoesBase) throws Exception {
	if (edicoesBase.size() == 1 && !edicoesBase.get(0).isEdicaoAberta()) {
	    throw new Exception("Existe apenas 1 edição fechada, favor incluir mais publicações na base.");
	}
    }

    private void excluiEdicoesComMaisDeDoisAnos(List<ProdutoEdicaoBase> edicoesBase) {
	int count = TRES_EDICOES - INDEX_CORRECTION;
	while (edicoesBase.size() > count) {
	    if (isBeforeTwoYears(edicoesBase.get(count).getDataLancamento())) {
		edicoesBase.remove(count);
	    } else {
		count++;
	    }
	}
    }

    private void excluiMaiorQueQuatroSeColecionavel(List<ProdutoEdicaoBase> edicoesBase) {
	if ((getEstudo().getProduto().getNumeroEdicao().compareTo(1L) > 0) && edicoesBase.get(0).isColecao()
		&& edicoesBase.size() > QUATRO_COLECIONAVEIS) {
	    edicoesBase.subList(QUATRO_COLECIONAVEIS + INDEX_CORRECTION, edicoesBase.size()).clear();
	}
    }

    private boolean isBeforeTwoYears(Date date) {
	return DateTime.now().minusYears(TWO_YEARS).isAfter(date.getTime());
    }
}
