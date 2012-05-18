package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.TipoDescontoVO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.service.TipoDescontoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

@Service
public class TipoDescontoServiceImpl implements TipoDescontoService {
	
	@Autowired
	private TipoDescontoRepository  tipoDescontoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	/**
	 * Método responsável por incluir um desconto
	 * @param {@link br.com.abril.nds.model.cadastro.TipoDescontoCota} 
	 */
	@Transactional
	@Override
	public void incluirDescontoGeral(TipoDesconto tipoDesconto) {
		 this.tipoDescontoRepository.adicionar(tipoDesconto);
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
	
	@Transactional
	@Override
	public List<TipoDescontoVO> obterTipoDescontoGeral() {
		List<TipoDesconto> lista =  this.tipoDescontoRepository.buscarTodos();
		List<TipoDescontoVO> listaVO = new ArrayList<TipoDescontoVO>();
		for(TipoDesconto desconto: lista){
			TipoDescontoVO vo = new TipoDescontoVO();			
			vo.setDataAlteracao(DateUtil.formatarData(desconto.getDataAlteracao(),
					  Constantes.DATE_PATTERN_PT_BR));
			vo.setDesconto(desconto.getPorcentagem().toString());
			vo.setUsuario(desconto.getUsuario());
			listaVO.add(vo);
		}		
		return listaVO;
	}

	@Override
	public Cota obterCota(int numeroCota) {
		 
		return null;
	}

}
