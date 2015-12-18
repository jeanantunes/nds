package br.com.abril.nds.process.bonificacoes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.medias.Medias;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Processo que tem como objetivo efetuar o cálculo da divisão do reparte entre as cotas encontradas para o perfil definido no
 * setup do estudo, levando em consideração todas as variáveis também definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - N/A
 * 
 * Processo Anterior: {@link Medias} Próximo Processo: {@link AjusteCota}
 * </p>
 */
@Component
public class Bonificacoes extends ProcessoAbstrato {

    @Autowired
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Override
    public void executar(EstudoTransient estudo) {

	if ((estudo.getBonificacoes() != null) && (!estudo.getBonificacoes().isEmpty())) {
	    
		for (BonificacaoDTO bonificacao : estudo.getBonificacoes()) {
		
			// validando reparte minimo
			BigInteger reparteMinimo = BigInteger.ZERO;
			
			if (bonificacao.getReparteMinimoBigInteger() != null && bonificacao.getReparteMinimoBigInteger().compareTo(BigInteger.ZERO) > 0) {
			    
				// verificacao se o reparte minimo da bonificacao e multiplo do pacote padrao
			    // TODO: melhorar logica ou encontrar alguma funcao da api mais simples
				if (estudo.getPacotePadrao() != null && estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) {
					
					BigDecimal quebrado = new BigDecimal(bonificacao.getReparteMinimoBigInteger()).divide(new BigDecimal(estudo.getPacotePadrao()), 4, BigDecimal.ROUND_HALF_UP);
					BigDecimal inteiro = new BigDecimal(bonificacao.getReparteMinimoBigInteger()).divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP);
				
					if (quebrado.compareTo(inteiro) != 0) {
						throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, String.format("O reparte mínimo da bonificação deve ser múltiplo de %s.", estudo.getPacotePadrao())));
					}
			    }
				
			    reparteMinimo = bonificacao.getReparteMinimoBigInteger();
			}

			// validando indiceBonificacao
			BigDecimal indiceBonificacao = BigDecimal.ZERO;
			
			if (bonificacao.getBonificacaoBigDecimal() != null && bonificacao.getBonificacaoBigDecimal().compareTo(BigDecimal.ZERO) > 0) {
			    indiceBonificacao = BigDecimal.valueOf(100).add(bonificacao.getBonificacaoBigDecimal()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
			}

			// inserindo valor nas cotas do estudo
			bonicacaoParaCotas(estudo.getCotas(), bonificacao, reparteMinimo, indiceBonificacao);
			
			bonicacaoParaCotas(estudo.getCotasExcluidas(), bonificacao, reparteMinimo, indiceBonificacao);

			// se todas as cotas estiver selecionado, insere reparte minimo para as bancas SH e VZ fora do estudo
			if (bonificacao.isTodasAsCotas() && bonificacao.getComponente() != null && bonificacao.getElemento() != null) {
			    
				String[] vetor = {bonificacao.getElemento()};
				
				int cotasClassificacaoVendaZeroOuSemHistorico = 0;
				for (CotaEstudo cota : estudo.getCotasExcluidas()) {
					if (cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico) &&	estudoAlgoritmoService.isCotaDentroDoComponenteElemento(bonificacao.getComponente(), vetor, cota)) {
						cotasClassificacaoVendaZeroOuSemHistorico++;
					}
				}
				
				if(reparteMinimo.multiply(BigInteger.valueOf(cotasClassificacaoVendaZeroOuSemHistorico)).compareTo(estudo.getReparteDistribuir()) > 0) {
					
					throw new ValidacaoException(TipoMensagem.WARNING, "A soma do reparte mínimo das cotas excede o reparte total.");
				}
				
				for (CotaEstudo cota : estudo.getCotasExcluidas()) {
					if (cota.getClassificacao().in(ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico) &&	estudoAlgoritmoService.isCotaDentroDoComponenteElemento(bonificacao.getComponente(), vetor, cota)) {
					    
						if (reparteMinimo.compareTo(cota.getReparteMinimoFinal()) > 0) {
					    	cota.setReparteMinimoFinal(reparteMinimo);
					    	cota.setReparteCalculado(reparteMinimo, estudo);
					    	cota.setClassificacao(ClassificacaoCota.BonificacaoParaCotas);
					    }
						
					}
			    }
			}
	    }
	}
	
    }

	private void bonicacaoParaCotas(LinkedList<CotaEstudo> cotas, BonificacaoDTO bonificacao, BigInteger reparteMinimo, BigDecimal indiceBonificacao) {
		
		for (CotaEstudo cota : cotas) {
		    
			String[] vetor = {bonificacao.getElemento()};
		    
			if (bonificacao.getComponente() != null && bonificacao.getElemento() != null && cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO) && estudoAlgoritmoService.isCotaDentroDoComponenteElemento(bonificacao.getComponente(), vetor, cota)) {
				
				if(cota.getClassificacao().in(ClassificacaoCota.BancaMixSemDeterminadaPublicacao, ClassificacaoCota.BancaComVendaZero, ClassificacaoCota.BancaSemHistorico)){
					continue;
				}
				
				if (indiceBonificacao.compareTo(cota.getIndiceTratamentoRegional()) > 0) {
				    cota.setIndiceTratamentoRegional(indiceBonificacao);
				    cota.setClassificacao(ClassificacaoCota.BonificacaoParaCotas);
				}

				if (reparteMinimo.compareTo(cota.getReparteMinimoFinal()) > 0) {
				    cota.setReparteMinimoFinal(reparteMinimo);
				    cota.setClassificacao(ClassificacaoCota.BonificacaoParaCotas);
				}
		    }
		}
	}
}
