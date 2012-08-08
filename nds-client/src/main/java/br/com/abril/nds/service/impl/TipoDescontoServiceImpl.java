package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.service.TipoDescontoService;

@Service
public class TipoDescontoServiceImpl implements TipoDescontoService {

	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<TipoDesconto> obterTodosTiposDescontos() {
		
		return this.tipoDescontoRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public TipoDesconto obterTipoDescontoPorID(Long id) {

		return this.tipoDescontoRepository.buscarPorId(id);
	}

	@Override
	public List<TipoDescontoDTO> buscarTipoDesconto(FiltroTipoDescontoDTO filtro) {
		// FIXME Implementar a consulta paginada de desconto
		return getMock();
	}

	@Override
	public Integer buscarQntTipoDesconto(FiltroTipoDescontoDTO filtro) {
		// FIXME Implementar a consulta de desconto
		return getMock().size();
	}

	@Override
	public List<TipoDescontoCotaDTO> buscarTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		// FIXME Implementar a consulta paginada de desconto
		return getMockEspecia();
	}

	@Override
	public Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		// FIXME Implementar a consulta  de desconto
		return getMockEspecia().size();
	}

	@Override
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		// FIXME Implementar a consulta paginada de desconto
		return getMockProduto();
	}

	@Override
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		// FIXME Implementar a consulta de desconto
		return getMockProduto().size();
	}
	
	private List<TipoDescontoCotaDTO> getMockEspecia(){
		
		List<TipoDescontoCotaDTO> listaDescontoCotaDTO = new ArrayList<TipoDescontoCotaDTO>();
		
		for (int i = 0; i < 10; i++) {
			
			TipoDescontoCotaDTO x = new TipoDescontoCotaDTO();
			x.setDataAlteracao(new Date());
			x.setDesconto(BigDecimal.TEN);
			x.setFornecedor("Fornecedor");
			x.setNomeCota("jose MAria");
			x.setNomeUsuario("Mane");
			x.setNumeroCota(123);
			x.setIdTipoDesconto(new Long(i));
			
			listaDescontoCotaDTO.add(x);
		}
		
		return listaDescontoCotaDTO;
	}

	
	private List<TipoDescontoDTO> getMock(){
		
		List<TipoDescontoDTO> lista = new ArrayList<TipoDescontoDTO>();
		
		for (int i = 0; i < 10; i++) {
			
			TipoDescontoDTO tp  = new TipoDescontoDTO(); 
			
			tp.setDataAlteracao(new Date());
			tp.setDesconto(BigDecimal.TEN);
			tp.setFornecedor("Fornecedor");
			tp.setIdTipoDesconto((new Long(i)));
			tp.setUsuario("Usuario");
			tp.setSequencial((new Long(i)));
			
			lista.add(tp);
		}
		
		return lista;
	}
	
	private List<TipoDescontoProdutoDTO> getMockProduto(){
		
		List<TipoDescontoProdutoDTO> listaDescontoCotaDTO = new ArrayList<TipoDescontoProdutoDTO>();
		
		for (int i = 0; i < 10; i++) {
			
			TipoDescontoProdutoDTO x = new TipoDescontoProdutoDTO();
			
			x.setCodigoProduto("Prod - " + i);
			x.setDataAlteracao(new Date());
			x.setDesconto(BigDecimal.TEN);
			x.setNomeProduto("Nome" + i);
			x.setNomeUsuario("Mane");
			x.setNumeroEdicao(i);
			x.setIdTipoDesconto(new Long(i));
			
			listaDescontoCotaDTO.add(x);
		}
		
		return listaDescontoCotaDTO;
	}

}
