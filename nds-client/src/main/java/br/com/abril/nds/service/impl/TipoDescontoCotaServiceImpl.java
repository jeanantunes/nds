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
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;
import br.com.abril.nds.service.TipoDescontoCotaService;

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
	public void incluirDescontoGeral(TipoDescontoCota tipoDescontoCota) {
		 this.tipoDescontoCotaRepository.adicionar(tipoDescontoCota);
	}
	
	@Transactional
	@Override
	public void atualizarDistribuidos(Long desconto) {
		List<Distribuidor> listaDeDistribuidores = this.distribuidorRepository.buscarTodos();
		for(Distribuidor dist: listaDeDistribuidores){
			BigDecimal descontoBig = new BigDecimal(desconto);
			dist.setFatorDesconto(descontoBig);
			this.distribuidorRepository.alterar(dist);
		}
	}
	
	@Transactional
	@Override
	public List<TipoDescontoCotaVO> obterTipoDescontoGeral() {
		List<TipoDescontoCota> lista =  this.tipoDescontoCotaRepository.buscarTodos();
		List<TipoDescontoCotaVO> listaVO = new ArrayList<TipoDescontoCotaVO>();
		for(TipoDescontoCota desconto: lista){
			TipoDescontoCotaVO vo = new TipoDescontoCotaVO();
			vo.setDataAlteracao(desconto.getDataAlteracao());
			vo.setDesconto(desconto.getDesconto());
			vo.setUsuario(desconto.getUsuario());
			listaVO.add(vo);
		}		
		return listaVO;
	}

}
