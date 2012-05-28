package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Periodicidade;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.repository.TipoEntregaRepository;
import br.com.abril.nds.service.TipoEntregaService;

/**
 * Serviço para TipoEntrega.
 * 
 * @author Discover Technology.
 */
@Service
public class TipoEntregaServiceImpl implements TipoEntregaService {

	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
	
	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#obterTodos()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoEntrega> obterTodos() {
		return tipoEntregaRepository.buscarTodos();
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#pesquisarTiposEntrega(java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoEntrega> pesquisarTiposEntrega(Long codigo,
			String descricao, String periodicidade, String sortname,
			String sortorder, int page, int rp) {

		return this.tipoEntregaRepository.pesquisarTiposEntrega(codigo, descricao, periodicidade, 
				sortname, sortorder, page, rp);
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#removerTipoEntrega(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=false)
	public void removerTipoEntrega(Long id) {
		
		try {
			
			TipoEntrega tipoEntrega = 
				this.tipoEntregaRepository.buscarPorId(id);
		 	
			if (tipoEntrega != null) {
				this.tipoEntregaRepository.remover(tipoEntrega);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#pesquisarQuantidadeTiposEntrega(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Integer pesquisarQuantidadeTiposEntrega(Long codigo,
			String descricao, String periodicidade) {
		
		return this.tipoEntregaRepository.pesquisarQuantidadeTiposEntrega(codigo, descricao, periodicidade);
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#salvarTipoEntrega(java.lang.Long, java.lang.String, java.math.BigDecimal, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly=false)
	public void salvarTipoEntrega(Long id, String descricao, BigDecimal taxaFixa, Float percentualFaturamento,
			String baseCalculo, String periodicidadeCadastro, Integer diaSemana, Integer diaMes) {
		
		try {
			
			TipoEntrega tipoEntrega = new TipoEntrega();
			
			tipoEntrega.setId(id);
			tipoEntrega.setDescricao(descricao);
			tipoEntrega.setPeriodicidade(getPeriodicidade(periodicidadeCadastro));
	
			if (taxaFixa != null) {
				tipoEntrega.setTaxaFixa(taxaFixa);
			}
			
			if (baseCalculo != null && !baseCalculo.isEmpty()) {
				tipoEntrega.setBaseCalculo(getBaseCalculo(baseCalculo));
			}
			
			if (percentualFaturamento != null) {
				tipoEntrega.setPercentualFaturamento(percentualFaturamento);
			}
			
			if (diaMes != null) {
				tipoEntrega.setDiaMes(diaMes);
			}
			
			if (diaSemana != null) {
				tipoEntrega.setDiaSemana(DiaSemana.getByCodigoDiaSemana(diaSemana));
			}
			
			if (id != null) {
				this.tipoEntregaRepository.alterar(tipoEntrega);
			} else {
				this.tipoEntregaRepository.adicionar(tipoEntrega);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see br.com.abril.nds.service.TipoEntregaService#obterTipoEntrega(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=false)
	public TipoEntrega obterTipoEntrega(Long id) {

		return this.tipoEntregaRepository.buscarPorId(id);
	}
	
	/**
	 * Retorna a Base de cálculo de acordo com o sua key.
	 * 
	 * @param key
	 * @return
	 */
	private BaseCalculo getBaseCalculo(String key) {

		for (BaseCalculo baseCalculo : BaseCalculo.values()) {

			if (baseCalculo.getKey().equals(key)) {
				return baseCalculo;
			}
		}
		
		return null;
	}
	
	/**
	 * Retorna a Periodicidade de acordo com seu value.
	 * 
	 * @param value
	 * @return
	 */
	private Periodicidade getPeriodicidade(String value) {
		
		for (Periodicidade periodicidade : Periodicidade.values()) {

			if (periodicidade.getValue().equals(value)) {
				return periodicidade;
			}
		}
		
		return null;
	}

}
