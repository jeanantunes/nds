package br.com.abril.nds.process.calculoreparte;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

import br.com.abril.nds.dao.MovimentoEstoqueCotaDAO;
import br.com.abril.nds.dao.ProdutoEdicaoDAO;
import br.com.abril.nds.model.ClassificacaoCota;
import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.process.ProcessoAbstrato;

/**
 * Processo que tem como objetivo efetuar o c√°lculo da divis√£o do reparte entre
 * as cotas encontradas para o perfil definido no setup do estudo, levando em
 * considera√ß√£o todas as vari√°veis tamb√©m definidas no setup.
 * <p style="white-space: pre-wrap;">
 * SubProcessos: - N/A Processo Pai: - {@link CalcularReparte}
 * 
 * Processo Anterior: {@link MinimoMaximo} Pr√≥ximo Processo:
 * {@link AjusteFinalReparte}
 * </p>
 */
public class GravarReparteJuramentado extends ProcessoAbstrato {

	private ProdutoEdicaoDAO produtoEdicaoDao;
	private MovimentoEstoqueCotaDAO movimentoEstoqueCotaDAO;
    
	public GravarReparteJuramentado(Estudo estudo) {
		super(estudo);
		produtoEdicaoDao = new ProdutoEdicaoDAO();
		movimentoEstoqueCotaDAO = new MovimentoEstoqueCotaDAO();
	}

    @Override
    public void executarProcesso() {
    	
    	if(estudo.getProduto().getParcial() ){
    		for(Cota cota:estudo.getCotas()){

    			int qtdeVezesEnviada = produtoEdicaoDao.getQtdeVezesReenviadas(cota, estudo.getProduto());
    			
    			if(qtdeVezesEnviada>=2){
    				
    				//Verificar se tem reparte juramentado A SER FATURADO
    				BigDecimal reparteJuramentadoAFaturar = movimentoEstoqueCotaDAO.retornarReparteJuramentadoAFaturar(cota, estudo.getProduto());
    				
					if (reparteJuramentadoAFaturar.compareTo(BigDecimal.ZERO) == 1) {
						// Gravar ReparteJura Cota na tabela
						cota.setReparteJuramentadoAFaturar(reparteJuramentadoAFaturar);
						
						// Se (ReparteCalculadol Cota < ReparteJura Cota) => ReparteCalculado Cota = 0
						if (cota.getReparteCalculado().compareTo(
								cota.getReparteJuramentadoAFaturar()) == -1) {
							cota.setReparteCalculado(BigDecimal.ZERO);
						} else {
							// ReparteCalculado Cota = ReparteCalculado Cota ñ ReparteJura Cota
							cota.setReparteCalculado(cota.getReparteCalculado()
									.subtract(cota.getReparteJuramentadoAFaturar()));

							// Se DistribuiÁ„o por M˙ltiplos = SIM
							if (estudo.getParametro()
									.isDistribuicaoPorMultiplos()) {
								// RepCalculado Cota = ARRED( RepCalculado Cota
								// / Pacote-Padr„o ; 0 )* Pacote-Padr„o
								BigDecimal pacotePadrao = new BigDecimal(estudo
										.getProduto().getPacotePadrao());
								cota.setReparteCalculado(cota
										.getReparteCalculado()
										.divide(pacotePadrao)
										.multiply(pacotePadrao)
										.setScale(2, BigDecimal.ROUND_FLOOR));
							}

						}

					}
    				
    			}
        	}
    	}
    	
    	
    	
    	
    }

	public void fimProcesso() {
		
		//Fim do sub processo
//    	Se houver saldo no reparte total distribuÌdo, n„o considerando-se o total de reparte juramentado:
//    	Indice de Sobra ou Falta = ( 'sum'ReparteCalculado Cota / ReparteCalculado) * ReparteCalculado Cota (n„o
    	
    		BigDecimal sumReparteCalculadoCota = BigDecimal.ZERO;
    		for(Cota cota:estudo.getCotas()){
    			sumReparteCalculadoCota = sumReparteCalculadoCota.add(cota.getReparteCalculado());
    		}
    			
    			Comparator<Cota> orderCotaDesc = new Comparator<Cota>(){
    				@Override
    				public int compare(Cota c1, Cota c2) {
    					return c2.getReparteCalculado().compareTo(c1.getReparteCalculado());
    				}
    			};
    			
    			Collections.sort(estudo.getCotas(),orderCotaDesc);

    			for (Cota cota : estudo.getCotas()) {

				if (!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
						&& !cota.getClassificacao().equals(	ClassificacaoCota.MaximoMinimo)) {

					BigDecimal indicedeSobraouFalta = sumReparteCalculadoCota
							.divide(estudo.getReparteDistribuir()).multiply(
									cota.getReparteCalculado());
					
					
//    	Se ainda houver saldo, subtrair ou somar 1 exemplar por cota do maior para o menor reparte 
//    	(exceto repartes fixados (FX), quantidades M¡XIMAS E MÕNIMAS (MM) 
//    	e bancas com MIX (MX)).
					
					if(	!cota.getClassificacao().equals(ClassificacaoCota.ReparteFixado)
	    					&& !cota.getClassificacao().equals(ClassificacaoCota.MaximoMinimo)
	    					&& !cota.getClassificacao().equals(ClassificacaoCota.BancaMixSemDeterminadaPublicacao)){
	    				
						if(indicedeSobraouFalta.compareTo(BigDecimal.ZERO)==1)
							cota.setReparteCalculado(cota.getReparteCalculado().add(BigDecimal.ONE));
						else if(indicedeSobraouFalta.compareTo(BigDecimal.ZERO)==-1)
							cota.setReparteCalculado(cota.getReparteCalculado().subtract(BigDecimal.ONE));
	    			}
					
				}
    			
    		}
	}
}
