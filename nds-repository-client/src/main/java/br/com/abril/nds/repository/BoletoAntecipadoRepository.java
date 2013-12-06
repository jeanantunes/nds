package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.StatusDivida;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.BoletoAntecipado}  
 * 
 * @author Discover Technology
 *
 */
public interface BoletoAntecipadoRepository extends Repository<BoletoAntecipado,Long> {
	
	/**
	 * Obtem BoletoAntecipado por ChamadaEncalheCota
	 * @param idCE
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	BoletoAntecipado obterBoletoAntecipadoPorCECota(Long idCE, List<StatusDivida> listaStatus);
	
	/**
	 * Obtem BoletoAntecipado por Nosso Numero
	 * @param nossoNumero
	 * @return BoletoAntecipado
	 */
	BoletoAntecipado obterBoletoAntecipadoPorNossoNumero(String nossoNumero);
	
	/**
	 * Obtem BoletoAntecipado por Data de Recolhimento e Cota
	 * @param idCota
	 * @param dataRecolhimento
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	BoletoAntecipado obterBoletoAntecipadoPorDataRecolhimentoECota(Long idCota, Date dataRecolhimento, List<StatusDivida> listaStatus);

	/**
	 * Obtem Boletos Antecipados por Periodo Recolhimento CE e cota
	 * @param numeroCota
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @return List<BoletoAntecipado>
	 */
	List<BoletoAntecipado> obterBoletosAntecipadosPorPeriodoRecolhimentoECota(Integer numeroCota, 
			                                                                  Date dataRecolhimentoCEDe, 
			                                                                  Date dataRecolhimentoCEAte);

	/**
	 * Obtem BoletoAntecipado por ChamadaEncalheCota e Periodo de Recolhimento selecionado na Emissao
	 * @param idCE
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @param listaStatus
	 * @return BoletoAntecipado
	 */
	BoletoAntecipado obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(Long idCE, 
			                                                            Date dataRecolhimentoCEDe, 
			                                                            Date dataRecolhimentoCEAte,
			                                                            List<StatusDivida> listaStatus);
}
