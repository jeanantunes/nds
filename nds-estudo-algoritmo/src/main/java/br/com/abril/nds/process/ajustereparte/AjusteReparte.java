package br.com.abril.nds.process.ajustereparte;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estudo.ClassificacaoCota;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.process.ProcessoAbstrato;
import br.com.abril.nds.process.redutorautomatico.RedutorAutomatico;
import br.com.abril.nds.process.vendamediafinal.VendaMediaFinal;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Este processo apenas realiza um ajuste no reparte das cotas se a opção
 * "Venda Média + n" estiver marcada na tela de Ajuste de Reparte. Se estiver,
 * ele atribui ao ReparteCalculado da cota a soma da VendaMediaFinal ou o valor
 * informado na tela Ajuste de Reparte (se ele for maior que o Pacote Padrão
 * definido, caso contrário será atribuído o pacote padrão).
 * <p style="white-space: pre-wrap;">SubProcessos:
 * 	- N/A
 * Processo Pai:
 * 	- N/A
 * 
 * Processo Anterior: {@link VendaMediaFinal}
 * Próximo Processo: {@link RedutorAutomatico}
 * </p>
 */
@Component
public class AjusteReparte extends ProcessoAbstrato {
	
	LinkedList<CotaEstudo> cotasComReparteJaCalculado = new LinkedList<>();
	
    @Override
    public void executar(EstudoTransient estudo) throws Exception {
		
    	if ((estudo == null) || (estudo.getCotas() == null)) {
		    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Houve um erro durante a execução do processo Ajuste de Reparte. Erro: objeto Estudo nulo."));
		}
    	
    	BigDecimal repDistribuir = new BigDecimal(estudo.getReparteDistribuir());
		
    	BigDecimal indice = new BigDecimal("0.85");
    	
    	BigDecimal somatorio = estudo.getSomatoriaVendaMedia().divide(repDistribuir, 2,	BigDecimal.ROUND_HALF_UP); 
    	
    	for (CotaEstudo cota : estudo.getCotas()) {
		    
    		if ((cota.getVendaMediaMaisN() != null) && (estudo.getPacotePadrao().compareTo(BigInteger.ZERO) > 0) && (estudo.getPacotePadrao() != null) && (cota.getVendaMediaMaisN().compareTo(BigInteger.ZERO) > 0)) {
    			
    			if(somatorio.compareTo(indice) < 0){
    				
	    			BigInteger ajusteReparte = BigInteger.ZERO;
	    			
	    			BigInteger vendaMedia = cota.getVendaMediaMaisN().add(cota.getVendaMedia().toBigInteger());
				
	    			// add validacao numero parcial e distribuicao multiplos false
	    			
	    			if (vendaMedia.compareTo(estudo.getPacotePadrao()) > 0) {
			    		
			    		BigDecimal verificador = new BigDecimal(vendaMedia).divide(new BigDecimal(estudo.getPacotePadrao()), 0, BigDecimal.ROUND_HALF_UP);
			    		
			    		ajusteReparte = BigInteger.valueOf(verificador.intValue()).multiply(estudo.getPacotePadrao());
			    		
					} else {
					    ajusteReparte = estudo.getPacotePadrao();
					}
				
			    	cota.setReparteCalculado(ajusteReparte, estudo);
			    	cota.setClassificacao(ClassificacaoCota.Ajuste);
			    	cotasComReparteJaCalculado.add(cota);
			    }
    		}else{
    			if(cota.getVendaMediaMaisN() != null && cota.getVendaMediaMaisN().compareTo(BigInteger.ZERO) > 0){
    				cota.setVendaMedia(cota.getVendaMedia().add(new BigDecimal(cota.getVendaMediaMaisN())));
    			}
    		}
    		
	}
	
	estudo.setCotasComReparteJaCalculado(new LinkedList<>(cotasComReparteJaCalculado));
	
	this.cotasComReparteJaCalculado.clear();
	
	estudo.getCotas().removeAll(estudo.getCotasComReparteJaCalculado());
	
    }
}
