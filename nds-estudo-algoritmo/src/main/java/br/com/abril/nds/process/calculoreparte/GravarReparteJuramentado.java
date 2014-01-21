package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dao.MovimentoEstoqueCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
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
    public void executar(EstudoTransient estudo) {

	if (estudo.getProdutoEdicaoEstudo().isParcial()) {
	    for (CotaEstudo cota : estudo.getCotas()) {

		int qtdeVezesEnviada = 0;
		for(ProdutoEdicaoEstudo pe :cota.getEdicoesRecebidas()){
		    if(estudo.getProdutoEdicaoEstudo().getId().equals(pe.getId())){
			qtdeVezesEnviada++;
		    }
		}

		if (qtdeVezesEnviada >= 2) {

		    // Verificar se tem reparte juramentado A SER FATURADO
		    BigInteger reparteJuramentadoAFaturar = movimentoEstoqueCotaDAO
			    .retornarReparteJuramentadoAFaturar(cota, estudo.getProdutoEdicaoEstudo()).setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();

		    if (reparteJuramentadoAFaturar.compareTo(BigInteger.ZERO) == 1) {
			// Gravar ReparteJura Cota na tabela
			cota.setReparteJuramentadoAFaturar(reparteJuramentadoAFaturar);

			// Se (ReparteCalculadol Cota < ReparteJura Cota) => ReparteCalculado Cota = 0
			if (cota.getReparteCalculado().compareTo(cota.getReparteJuramentadoAFaturar()) == -1) {
			    cota.setReparteCalculado(BigInteger.ZERO, estudo);
			} else {
			    // ReparteCalculado Cota = ReparteCalculado Cota - ReparteJura Cota
			    cota.setReparteCalculado(cota.getReparteCalculado().subtract(cota.getReparteJuramentadoAFaturar()), estudo);

			    // Se Distribuição por Multiplos = SIM
			    if (estudo.isDistribuicaoPorMultiplos()) {
				// RepCalculado Cota = ARRED( RepCalculado Cota
				// / Pacote-Padrão ; 0 )* Pacote-Padrão
				BigInteger pacotePadrao = estudo.getPacotePadrao();
				cota.setReparteCalculado(new BigDecimal(cota.getReparteCalculado()).divide(new BigDecimal(pacotePadrao), 0, BigDecimal.ROUND_HALF_UP).toBigInteger(), estudo);
			    }
			}
		    }
		}
	    }
	}
    }
}
