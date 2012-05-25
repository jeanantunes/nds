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

@Service
public class TipoEntregaServiceImpl implements TipoEntregaService {

	@Autowired
	private TipoEntregaRepository tipoEntregaRepository;
		
	@Override
	@Transactional(readOnly=true)
	public List<TipoEntrega> obterTodos() {
		return tipoEntregaRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoEntrega> pesquisarTiposEntrega(Long codigo,
			String descricao, String periodicidade, String sortname,
			String sortorder, int page, int rp) {

		return this.tipoEntregaRepository.pesquisarTiposEntrega(codigo, descricao, periodicidade, 
				sortname, sortorder, page, rp);
	}

	@Override
	@Transactional
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

	@Override
	@Transactional(readOnly=true)
	public Integer pesquisarQuantidadeTiposEntrega(Long codigo,
			String descricao, String periodicidade) {
		
		return this.tipoEntregaRepository.pesquisarQuantidadeTiposEntrega(codigo, descricao, periodicidade);
	}

	@Override
	@Transactional(readOnly=false)
	public void salvarTipoEntrega(Long id, String descricao, BigDecimal taxaFixa, Integer percentualFaturamento,
			String baseCalculo, String periodicidadeCadastro, Integer diaSemana, Integer diaMes) {

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
			tipoEntrega.setPercentualFaturamento(percentualFaturamento.floatValue());
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
	}

	private BaseCalculo getBaseCalculo(String value) {

		for (BaseCalculo baseCalculo : BaseCalculo.values()) {

			if (baseCalculo.getValue().equals(value)) {
				return baseCalculo;
			}
		}
		
		return null;
	}
	
	private Periodicidade getPeriodicidade(String value) {
		
		for (Periodicidade periodicidade : Periodicidade.values()) {

			if (periodicidade.getValue().equals(value)) {
				return periodicidade;
			}
		}
		
		return null;
	}

}
