package br.com.abril.nds.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.util.DateUtil;

@Service
public class FormaCobrancaServiceImpl implements FormaCobrancaService {

	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaCota(Integer numeroCota, List<Long> fornecedoresId, Date data, BigDecimal valor) {
		
		Integer diaDoMes = DateUtil.obterDiaDoMes(data);
		
		Integer diaDaSemana = DateUtil.obterDiaDaSemana(data);
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(numeroCota, fornecedoresId, diaDoMes, diaDaSemana, valor);

		return formaCobranca;
	}
	
	/**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaDistribuidor(List<Long> fornecedoresId, Date data, BigDecimal valor) {
		
		Integer diaDoMes = DateUtil.obterDiaDoMes(data);
		
		Integer diaDaSemana = DateUtil.obterDiaDaSemana(data);
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(fornecedoresId, diaDoMes, diaDaSemana, valor);

		return formaCobranca;
	}
	
	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não encontre, busca FormaCobranca do Distribuidor 
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobranca(Integer numeroCota, List<Long> fornecedoresId, Date data, BigDecimal valor) {
		
		FormaCobranca formaCobranca = this.obterFormaCobrancaCota(numeroCota, fornecedoresId, data, valor);

		if (formaCobranca == null){
			
			formaCobranca = this.obterFormaCobrancaDistribuidor(fornecedoresId, data, valor);
		}
		
		return formaCobranca;
	}

	/**
     * Obtem FormaCobranca principal da Cota
     * @param idCota
     * @return FormaCobranca
     */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota) {
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(idCota);

		return formaCobranca;
	}

	/**
     * Obtem FormaCobranca principal do Distribuidor
     * @return FormaCobranca
     */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalDistribuidor() {
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca();

		return formaCobranca;
	}
}