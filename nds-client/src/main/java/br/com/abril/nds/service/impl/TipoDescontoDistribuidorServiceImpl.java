package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.TipoDescontoDistribuidorRepository;
import br.com.abril.nds.service.TipoDescontoDistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

@Service
public class TipoDescontoDistribuidorServiceImpl implements
		TipoDescontoDistribuidorService {
	
	@Autowired
	private TipoDescontoDistribuidorRepository tipoDescontoDistribuidorRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Override
	@Transactional
	public List<TipoDescontoCotaVO> obterTipoDescontoDistribuidor() {
		List<TipoDescontoDistribuidor> lista = this.tipoDescontoDistribuidorRepository.obterTipoDescontosDistribuidor();
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
	public void incluirDescontoDistribuidor(TipoDescontoDistribuidor tipoDescontoDistribuidor) {
		 this.tipoDescontoDistribuidorRepository.adicionar(tipoDescontoDistribuidor);
	}

	@Override
	@Transactional
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
		return this.tipoDescontoDistribuidorRepository.obterSequencial();
	}

	@Override
	public void excluirDesconto(TipoDescontoDistribuidor tipoDescontoDistribuidor) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public List<Distribuidor> obterDistribuidores() {		 
		return this.distribuidorRepository.buscarTodos();
	}

}
