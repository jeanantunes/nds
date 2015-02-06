package br.com.abril.nds.process.ajustefinalreparte;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.calculoreparte.CalcularReparte;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link AjusteFinalReparte} Próximo Processo: {@link GravarReparteFinalCota}
 * </p>
 */
@Component
public class ReparteComplementarPorCota extends ProcessoAbstrato {

    @Override
    public void executar(EstudoTransient estudo) throws Exception {
	if (estudo.getReparteComplementar() != null && estudo.getReparteComplementar().compareTo(BigInteger.ZERO) > 0) {
	    distribuirReparteComplementar(estudo, ordenarLista(estudo));
	}
    }

    private LinkedList<CotaEstudo> ordenarLista(EstudoTransient estudo) {
	LinkedList<CotaEstudo> listaOrdenada = new LinkedList<>();

	Collections.sort(estudo.getCotasExcluidas(), new Comparator<CotaEstudo>() {

	    @Override
	    public int compare(CotaEstudo cota1, CotaEstudo cota2) {
	    	if (cota1.getQtdeRankingSegmento().compareTo(cota2.getQtdeRankingSegmento()) < 0) {
	    		return 1;
	        }
	        if (cota1.getQtdeRankingSegmento().compareTo(cota2.getQtdeRankingSegmento()) > 0) {
	        	return -1;
	        }
	        return 0;
	    }
	});
	// Lista de cotas que não receberam as edições-base, porém receberam a edição aberta
	LinkedList<CotaEstudo> listaA = new LinkedList<>();
	// Lista de cotas que não receberam as edições-base
	LinkedList<CotaEstudo> listaB = new LinkedList<>();
	// Lista de cotas que receberam 1 edição base
	LinkedList<CotaEstudo> listaC = new LinkedList<>();
	// Lista de cotas que receberam 2 edições base
	LinkedList<CotaEstudo> listaD = new LinkedList<>();
	// Lista de cotas que receberam 3 ou mais edições das edições base
	LinkedList<CotaEstudo> listaE = new LinkedList<>();
	
	//add validação para excluir banca mix
	
	for (CotaEstudo cota : estudo.getCotasExcluidas()) {
	    if ((cota.getReparteCalculado().compareTo(BigInteger.ZERO) == 0) && cota.isRecebeReparteComplementar()
		    && cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO)) {
			if ((cota.getEdicoesRecebidas().size() == 0) && (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico)) &&
				(cota.isRecebeuUltimaEdicaoAberta())) {
			    listaA.add(cota);
			} else if ((cota.getEdicoesRecebidas().size() == 0) && (cota.getClassificacao().equals(ClassificacaoCota.BancaSemHistorico))) {
			    listaB.add(cota);
			} else if ((cota.getEdicoesRecebidas().size() == 1) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
			    listaC.add(cota);
			} else if ((cota.getEdicoesRecebidas().size() == 2) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
			    listaD.add(cota);
			} else if ((cota.getEdicoesRecebidas().size() >= 3) && (cota.getClassificacao().equals(ClassificacaoCota.BancaComVendaZero))) {
			    listaE.add(cota);
			}
	    }
	}
	listaOrdenada.addAll(listaA);
	listaOrdenada.addAll(listaB);
	listaOrdenada.addAll(listaC);
	listaOrdenada.addAll(listaD);
	listaOrdenada.addAll(listaE);
	return listaOrdenada;
    }

    private void distribuirReparteComplementar(EstudoTransient estudo, LinkedList<CotaEstudo> listaOrdenada) {
	BigInteger reparte = BigInteger.valueOf(2);
	if (estudo.isDistribuicaoPorMultiplos() && estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ONE) > 0) {
	    reparte = estudo.getPacotePadrao();
	}
	if (estudo.getReparteMinimo() != null && estudo.getReparteMinimo().compareTo(reparte) > 0) {
	    reparte = estudo.getReparteMinimo();
	}
	for (CotaEstudo cota : listaOrdenada) {
	    if (estudo.getReparteComplementar().compareTo(reparte) < 0) {
		break;
	    }
	    if (cota.getIntervaloMaximo() != null && cota.getReparteCalculado().compareTo(cota.getIntervaloMaximo()) > 0) {
		cota.setReparteCalculado(cota.getIntervaloMaximo());
	    } else if (cota.getReparteCalculado().compareTo(cota.getIntervaloMinimo()) < 0) {
		cota.setReparteCalculado(cota.getIntervaloMinimo());
	    } else {
		cota.setReparteCalculado(cota.getReparteCalculado().add(reparte));
		cota.setClassificacao(ClassificacaoCota.BancaEstudoComplementar);
		estudo.setReparteComplementar(estudo.getReparteComplementar().subtract(reparte));
	    }
	}
	BigInteger reparteGeral = BigInteger.ONE;
	if (estudo.isDistribuicaoPorMultiplos()) {
	    reparteGeral = reparte;
	}
	while (estudo.getReparteComplementar().compareTo(reparteGeral) >= 0) {
	    for (CotaEstudo cota : estudo.getCotas()) {
		if (estudo.getReparteComplementar().compareTo(reparteGeral) < 0) {
		    break;
		}
		if (cota.getClassificacao().notIn(ClassificacaoCota.ReparteFixado, ClassificacaoCota.MaximoMinimo,
			ClassificacaoCota.BancaMixSemDeterminadaPublicacao, ClassificacaoCota.CotaMix,
			ClassificacaoCota.BancaForaDaRegiaoDistribuicao)) {
		    if (cota.getIntervaloMaximo() != null && cota.getReparteCalculado().compareTo(cota.getIntervaloMaximo()) > 0) {
			cota.setReparteCalculado(cota.getIntervaloMaximo());
		    } else if (cota.getReparteCalculado().compareTo(cota.getIntervaloMinimo()) < 0) {
			cota.setReparteCalculado(cota.getIntervaloMinimo());
		    } else {
			cota.setReparteCalculado(cota.getReparteCalculado().add(reparteGeral));
			estudo.setReparteComplementar(estudo.getReparteComplementar().subtract(reparteGeral));
		    }
		}
	    }
	}
	
	if(estudo.getReparteComplementar().compareTo(BigInteger.ZERO) > 0){
		estudo.setReparteDistribuir(estudo.getReparteDistribuir().add(estudo.getReparteComplementar()));
	}
	
	
	
    }
}
