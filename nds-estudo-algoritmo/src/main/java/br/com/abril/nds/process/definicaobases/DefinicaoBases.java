package br.com.abril.nds.process.definicaobases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.somarfixacoes.SomarFixacoes;
import br.com.abril.nds.service.PreparaEstudoService;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o 
 * perfil definido no setup do estudo, levando em consideração todas as variáveis também definidas no setup.
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
    
    private List<ProdutoEdicaoBase> edicoesRecebidasParaEstudoRaw;
    
    private PreparaEstudoService estudoService = new PreparaEstudoService();

    public DefinicaoBases(Estudo estudo) {
	super(estudo);
    }

    @Override
    public void executarProcesso() throws Exception {
	// TODO Popular o estudo - Criar Logica para chamar subProcesso
	// FIXME Retirar esse trecho
	Estudo estudo = super.getEstudo();

	// TODO: implementar método calcular do Processo DefinicaoBases
	//recebe edições da interface ou manualmente (é indiferente a origem, a principio)
	
	List<ProdutoEdicaoBase> edicoesRecebidasRaw = getEdicoesRecebidasParaEstudoRaw();
	
	List<ProdutoEdicaoBase> edicoesParaEstudo = new ArrayList<ProdutoEdicaoBase>();
	for (ProdutoEdicaoBase produtoEdicao : edicoesRecebidasRaw) {
	    List<ProdutoEdicaoBase> objetoEdtudo = estudoService.buscaEdicoesPorLancamento(produtoEdicao);
	    validaApenasUmaEdicaoFechada(objetoEdtudo);
	    excluiEdicoesComMaisDeDoisAnos(objetoEdtudo);
	    excluiColecionaveisSeMaiorQueQuatro(objetoEdtudo);
	    edicoesParaEstudo.addAll(objetoEdtudo);
	}
	
	estudo.setEdicoesBase(edicoesParaEstudo);
	
	BaseParaVeraneio baseParaVeraneio = new BaseParaVeraneio(estudo);
	baseParaVeraneio.executar();
	
	BaseParaSaidaVeraneio baseParaSaidaVeraneio = new BaseParaSaidaVeraneio(estudo);
	baseParaSaidaVeraneio.executar();
	
//	super.genericDTO = baseParaSaidaVeraneio.getGenericDTO();
    }

    private void validaApenasUmaEdicaoFechada(List<ProdutoEdicaoBase> objetoEdtudo) throws Exception {
	if(objetoEdtudo.size() == 1 && !objetoEdtudo.get(0).isEdicaoAberta()) {
	    //FIXME rever este throw
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
	/*
	long now = System.currentTimeMillis();
	long twoYears = (long) 1000*60*60*24*365;
	Date date = new Date(now - twoYears);
	*/
	return DateTime.now().minusYears(TWO_YEARS).isAfter(date.getTime());
    }

    public List<ProdutoEdicaoBase> getEdicoesRecebidasParaEstudoRaw() {
	return edicoesRecebidasParaEstudoRaw;
    }

    public void setEdicoesRecebidasParaEstudoRaw(
	    List<ProdutoEdicaoBase> edicoesRecebidasParaEstudoRaw) {
	this.edicoesRecebidasParaEstudoRaw = edicoesRecebidasParaEstudoRaw;
    }
}
