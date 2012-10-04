package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.CobrancaVO;

/**
 * Classe de interface de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.BaixaCobranca}
 * @author InfoA2
 */
@Service
public interface BaixaCobrancaService {

	/**
	 * Baixa última baixa automática realizada no dia
	 * @param dataOperacao
	 * @return BaixaAutomatica
	 */
	public Date buscarUltimaBaixaAutomaticaDia(Date dataOperacao);
	
	/**
	 * Baixa a data da última baixa automática
	 * @return Date
	 */
	public Date buscarDiaUltimaBaixaAutomatica();
	
	public List<CobrancaVO> buscarCobrancasBaixadas(Integer numCota, String nossoNumero);
	
}
