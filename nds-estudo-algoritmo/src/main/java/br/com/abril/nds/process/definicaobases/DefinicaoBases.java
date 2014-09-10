package br.com.abril.nds.process.definicaobases;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.service.EstudoAlgoritmoService;

/**
 * Processo que tem como objetivo buscar as edições de base para o estudo.
 * <p * style="white-space: pre-wrap;">
 * SubProcessos: - {@link BaseParaVeraneio} - {@link BaseParaSaidaVeraneio}
 * Processo Pai: - N/A
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
    private EstudoAlgoritmoService estudoAlgoritmoService;
    
    @Override
    public void executar(final EstudoTransient estudo)  {
    	
    	estudoAlgoritmoService.carregarParametros(estudo);
    	
        if ((estudo.getEdicoesBase() == null) || (estudo.getEdicoesBase().size() == 0)) {
        	
        	if(estudo.isPracaVeraneio() && !estudo.getProdutoEdicaoEstudo().isColecao()) { 
        		
        		baseParaVeraneio.executar(estudo);
        		
        	} else {
        		
        		LinkedList<ProdutoEdicaoEstudo> edicoesBase = estudoAlgoritmoService.getEdicoesBases(estudo.getProdutoEdicaoEstudo());
        		
        		if (!edicoesBase.isEmpty()) {
        			edicoesBase = filtrarClassificacao(edicoesBase, estudo);
        			edicoesBase = estudoAlgoritmoService.limitarEdicoesApenasSeis(edicoesBase, estudo);
        			excluiEdicoesComMaisDeDoisAnos(edicoesBase);
        			excluiMaiorQueQuatroSeColecionavel(edicoesBase, estudo);
        			
        			if(estudo.getProdutoEdicaoEstudo().getPeriodo() != null && estudo.getProdutoEdicaoEstudo().getPeriodo() >= 2){
        				edicoesBase = filtarEdicoesParciais(edicoesBase, estudo);
        			}
        			
        			estudo.setEdicoesBase(edicoesBase);        			
        		}
        	}
        	
        }
    }
    
    private LinkedList<ProdutoEdicaoEstudo> filtrarClassificacao(final LinkedList<ProdutoEdicaoEstudo> edicoesBase, final EstudoTransient estudo) {
        final LinkedList<ProdutoEdicaoEstudo> listaFiltrada = new LinkedList<>();
        final TipoClassificacaoProduto tipoClassificacaoProdutoEstudo = estudo.getProdutoEdicaoEstudo().getTipoClassificacaoProduto();
        for (final ProdutoEdicaoEstudo edicaoBaseEstudo : edicoesBase) {
            if (tipoClassificacaoProdutoEstudo.equals(edicaoBaseEstudo.getTipoClassificacaoProduto())) {
                listaFiltrada.add(edicaoBaseEstudo);
            }
        }
        return listaFiltrada;
    }
    
    private LinkedList<ProdutoEdicaoEstudo> filtarEdicoesParciais(LinkedList<ProdutoEdicaoEstudo> edicoesBase,  EstudoTransient estudo){
    	
    	LinkedList<ProdutoEdicaoEstudo> edicoesBasesMesmaEdicao = new LinkedList<>();
    	
    	for (ProdutoEdicaoEstudo base : edicoesBase) {
    		if(estudo.getProdutoEdicaoEstudo().getNumeroEdicao().equals(base.getNumeroEdicao())){
    			edicoesBasesMesmaEdicao.add(base);
    		}
			
		}
    	
    	return edicoesBasesMesmaEdicao;
    	
    }
    
    private void excluiEdicoesComMaisDeDoisAnos(final List<ProdutoEdicaoEstudo> edicoesBase) {
        int count = TRES_EDICOES - INDEX_CORRECTION;
        while (edicoesBase.size() > count) {
            if (isBeforeTwoYears(edicoesBase.get(count).getDataLancamento())) {
                edicoesBase.remove(count);
            } else {
                count++;
            }
        }
    }
    
    private void excluiMaiorQueQuatroSeColecionavel(final List<ProdutoEdicaoEstudo> edicoesBase, final EstudoTransient estudo) {
        if ((estudo.getProdutoEdicaoEstudo().getNumeroEdicao().compareTo(1L) > 0) && edicoesBase.size() > QUATRO_COLECIONAVEIS && edicoesBase.get(0).isColecao()) {
            edicoesBase.subList(QUATRO_COLECIONAVEIS, edicoesBase.size()).clear();
        }
    }
    
    private boolean isBeforeTwoYears(final Date date) {
        return DateTime.now().minusYears(TWO_YEARS).isAfter(date.getTime());
    }
}
