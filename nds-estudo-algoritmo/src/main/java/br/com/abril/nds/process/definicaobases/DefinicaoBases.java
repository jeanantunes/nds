package br.com.abril.nds.process.definicaobases;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.service.PreparaEstudoService;

/**
 * Processo que tem como objetivo buscar as edições de base para o estudo.
 * <p * style="white-space: pre-wrap;">
 * SubProcessos:
 * 		- {@link BaseParaVeraneio}
 * 		- {@link BaseParaSaidaVeraneio}
 * Processo Pai:
 * 		- N/A
 * 
 * Processo Anterior: N/A
 * Próximo Processo: {@link SomarFixacoes}
 * </p>
 */
public class DefinicaoBases extends ProcessoAbstrato {

	private static final int INDEX_CORRECTION = 1;
	private static final int TRES_EDICOES = 3;
	private static final int QUATRO_COLECIONAVEIS = 4;
	private static final int TWO_YEARS = 2;

	private PreparaEstudoService estudoService = new PreparaEstudoService();

	public DefinicaoBases(Estudo estudo) {
		super(estudo);
	}

	@Override
	public void executarProcesso() throws Exception {
		Estudo estudo = super.getEstudo();

		List<ProdutoEdicaoBase> edicoesParaEstudo = estudoService.buscaEdicoesPorLancamento(estudo.getProduto());
		validaApenasUmaEdicaoFechada(edicoesParaEstudo);
		limitarEdicoesApenasSeis(edicoesParaEstudo);
		excluiEdicoesComMaisDeDoisAnos(edicoesParaEstudo);
		excluiColecionaveisSeMaiorQueQuatro(edicoesParaEstudo);

		estudo.setEdicoesBase(edicoesParaEstudo);

		BaseParaVeraneio baseParaVeraneio = new BaseParaVeraneio(estudo);
		baseParaVeraneio.executar();

		BaseParaSaidaVeraneio baseParaSaidaVeraneio = new BaseParaSaidaVeraneio(estudo);
		baseParaSaidaVeraneio.executar();

	}

	private void limitarEdicoesApenasSeis(List<ProdutoEdicaoBase> edicoesParaEstudo) {
		// TODO Auto-generated method stub
		
	}

	private void validaApenasUmaEdicaoFechada(List<ProdutoEdicaoBase> objetoEdtudo) throws Exception {
		if(objetoEdtudo.size() == 1 && !objetoEdtudo.get(0).isEdicaoAberta()) {
			throw new Exception("Existe apenas 1 edição fechada, favor incluir mais publicações na base.");
		}
	}

	private void excluiEdicoesComMaisDeDoisAnos(List<ProdutoEdicaoBase> objetoEdtudo) {
		int count = TRES_EDICOES-INDEX_CORRECTION;
		while(objetoEdtudo.size() > count) {
			if(isBeforeTwoYears(objetoEdtudo.get(count).getDataLancamento())) {
				objetoEdtudo.remove(count);
			} else {
				count++;
			}
		}
	}

	private void excluiColecionaveisSeMaiorQueQuatro(List<ProdutoEdicaoBase> objetoEdtudo) {
		if(objetoEdtudo.get(0).isColecao() && objetoEdtudo.size()>QUATRO_COLECIONAVEIS) {
			objetoEdtudo.subList(QUATRO_COLECIONAVEIS+INDEX_CORRECTION, objetoEdtudo.size()).clear();
		}
	}

	private boolean isBeforeTwoYears(Date date) {
		return DateTime.now().minusYears(TWO_YEARS).isAfter(date.getTime());
	}
}
