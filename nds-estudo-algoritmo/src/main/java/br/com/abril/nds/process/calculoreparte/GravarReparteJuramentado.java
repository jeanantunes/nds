package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.MovimentoEstoqueCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.ajustefinalreparte.AjusteFinalReparte;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link MinimoMaximo} Próximo Processo: {@link AjusteFinalReparte}
 * </p>
 */
@Component
public class GravarReparteJuramentado extends ProcessoAbstrato {

    @Autowired
    private ProdutoEdicaoDAO produtoEdicaoDao;

    @Autowired
    private MovimentoEstoqueCotaDAO movimentoEstoqueCotaDAO;

    @Override
    public void executarProcesso() {

	if (getEstudo().getProduto().isParcial()) {
	    for (Cota cota : getEstudo().getCotas()) {

    			int qtdeVezesEnviada = 0;
    			for(ProdutoEdicao pe :cota.getEdicoesRecebidas()){
    				if(getEstudo().getProduto().getId().equals(pe.getId())){
    					qtdeVezesEnviada++;
    				}
    			}

		if (qtdeVezesEnviada >= 2) {

		    // Verificar se tem reparte juramentado A SER FATURADO
		    BigDecimal reparteJuramentadoAFaturar = movimentoEstoqueCotaDAO
			    .retornarReparteJuramentadoAFaturar(cota, getEstudo().getProduto());

		    if (reparteJuramentadoAFaturar.compareTo(BigDecimal.ZERO) == 1) {
			// Gravar ReparteJura Cota na tabela
			cota.setReparteJuramentadoAFaturar(reparteJuramentadoAFaturar);

			// Se (ReparteCalculadol Cota < ReparteJura Cota) => ReparteCalculado Cota = 0
			if (cota.getReparteCalculado().compareTo(cota.getReparteJuramentadoAFaturar()) == -1) {
			    cota.setReparteCalculado(BigDecimal.ZERO);
			} else {
			    // ReparteCalculado Cota = ReparteCalculado Cota � ReparteJura Cota
			    cota.setReparteCalculado(cota.getReparteCalculado().subtract(cota.getReparteJuramentadoAFaturar()));

			    // Se Distribui��o por M�ltiplos = SIM
			    if (getEstudo().isDistribuicaoPorMultiplos()) {
				// RepCalculado Cota = ARRED( RepCalculado Cota
				// / Pacote-Padr�o ; 0 )* Pacote-Padr�o
				BigDecimal pacotePadrao = getEstudo().getPacotePadrao();
				cota.setReparteCalculado(cota.getReparteCalculado().divide(pacotePadrao).multiply(pacotePadrao)
					.setScale(2, BigDecimal.ROUND_FLOOR));
			    }
			}
		    }
		}
	    }
	}
	// this.fimProcesso();
    }

    public void fimProcesso() {
	// Fim do sub processo
	// Se houver saldo no reparte total distribu�do, n�o considerando-se o total de reparte juramentado:
	// Indice de Sobra ou Falta = ( 'sum'ReparteCalculado Cota / ReparteCalculado) * ReparteCalculado Cota (n�o

	BigDecimal sumReparteCalculadoCota = BigDecimal.ZERO;
	for (Cota cota : getEstudo().getCotas()) {
	    sumReparteCalculadoCota = sumReparteCalculadoCota.add(cota.getReparteCalculado());
	}

	Comparator<Cota> orderCotaDesc = new Comparator<Cota>() {
	    @Override
	    public int compare(Cota c1, Cota c2) {
		return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
	    }
	};

	Collections.sort(getEstudo().getCotas(), orderCotaDesc);

	if (getEstudo().getReparteDistribuir().compareTo(BigDecimal.ZERO) == -1 || getEstudo().getReparteDistribuir().compareTo(BigDecimal.ZERO) == 0) {
	    return;
	}

	Collections.sort(getEstudo().getCotas(), orderCotaDesc);

	for (Cota cota : getEstudo().getCotas()) {

	    if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado) && !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)) {

		BigDecimal indicedeSobraouFalta = sumReparteCalculadoCota.divide(getEstudo().getReparteDistribuir()).multiply(
			cota.getReparteCalculado());

		// Se ainda houver saldo, subtrair ou somar 1 exemplar por cota do maior para o menor reparte
		// (exceto repartes fixados (FX), quantidades M�XIMAS E M�NIMAS (MM)
		// e bancas com MIX (MX)).

		if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
			&& !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)
			&& !cota.getClassificacao().equals(ClassificacaoCota.BancaMixSemDeterminadaPublicacao)) {

		    if (indicedeSobraouFalta.compareTo(BigDecimal.ZERO) == 1)
			cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
		    else if (indicedeSobraouFalta.compareTo(BigDecimal.ZERO) == -1)
			cota.setReparteCalculado(cota.getReparteCalculado().subtract(BigDecimal.ONE));
		}

		else if (indicedeSobraouFalta.compareTo(BigDecimal.ZERO) == -1)
		    cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
	    }
	}
    }
}
