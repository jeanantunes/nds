package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;
import br.com.abril.nds.service.TipoDescontoCotaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

@Service
public class TipoDescontoCotaServiceImpl implements TipoDescontoCotaService {
	
	@Autowired
	private TipoDescontoCotaRepository  tipoDescontoCotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	/**
	 * Método responsável por incluir um desconto
	 * @param {@link br.com.abril.nds.model.cadastro.TipoDescontoCota} 
	 */
	@Transactional
	@Override
	public void incluirDesconto(TipoDescontoCota tipoDescontoCota) {
		 this.tipoDescontoCotaRepository.adicionar(tipoDescontoCota);
	}
	
	@Transactional
	@Override
	public void atualizarDistribuidores(BigDecimal desconto) {
		List<Distribuidor> listaDeDistribuidores = this.distribuidorRepository.buscarTodos();
		for(Distribuidor dist: listaDeDistribuidores){
			dist.setFatorDesconto(desconto);
			this.distribuidorRepository.alterar(dist);
		}
	}
	

	@Override
	@Transactional
	public int obterUltimoSequencial() {		
		return this.tipoDescontoCotaRepository.obterSequencial();
	}

	@Override
	@Transactional
	public List<TipoDescontoCotaVO> obterTipoDescontoDistribuidor() {
		List<TipoDescontoDistribuidor> lista = this.tipoDescontoCotaRepository.obterTipoDescontosDistribuidor();
		List<TipoDescontoCotaVO> listaVO = new ArrayList<TipoDescontoCotaVO>();
		for(TipoDescontoDistribuidor desconto: lista){
			TipoDescontoCotaVO vo = new TipoDescontoCotaVO();
			vo.setId(desconto.getId().toString());
			vo.setDataAlteracao(DateUtil.formatarData(desconto.getDataAlteracao(), Constantes.DATE_PATTERN_PT_BR));
			vo.setDesconto(desconto.getDesconto().toString());
			vo.setUsuario(desconto.getUsuario().getNome());
			vo.setSequencial(desconto.getSequencial().toString());			
			listaVO.add(vo);
		}		
		return listaVO;
	}

	@Override
	@Transactional
	public void excluirDesconto(TipoDescontoCota tipoDescontoCota) {		
		this.tipoDescontoCotaRepository.remover(tipoDescontoCota);
		
	}

	@Override
	@Transactional
	public TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto) {		
		return this.tipoDescontoCotaRepository.buscarPorId(idDesconto);
	}

	@Override
	@Transactional
	public List<Distribuidor> obterDistribuidores() {		 
		return this.distribuidorRepository.buscarTodos();
	}

	@Override
	@Transactional
	public Integer buscarTotalDescontosPorCota() {		 
		return this.tipoDescontoCotaRepository.buscarTotalDescontoPorCota();
	}

}
