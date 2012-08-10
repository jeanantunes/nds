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
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.service.DescontoService;

@Service
public class DescontoServiceImpl implements DescontoService {

	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Autowired
	private DescontoDistribuidorRepository descontoDistribuidorRepository;
	
	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
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
	@Transactional(readOnly=true)
	public List<TipoDescontoDTO> buscarTipoDesconto(FiltroTipoDescontoDTO filtro) {

		return descontoDistribuidorRepository.buscarDescontos(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQntTipoDesconto(FiltroTipoDescontoDTO filtro) {
	
		return descontoDistribuidorRepository.buscarQuantidadeDescontos(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoCotaDTO> buscarTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		// FIXME Implementar a consulta paginada de desconto
		return getMockEspecia();
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		// FIXME Implementar a consulta  de desconto
		return getMockEspecia().size();
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		return this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		return this.descontoProdutoRepository.buscarQuantidadeTipoDescontoProduto(filtro);
	}
	
	@Override
	@Transactional
	public void excluirDesconto(Long idDesconto,br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto) {
		// FIXME Implementar a exclusão do desconto informado
	}
	
	@Override
	@Transactional
	public void incluirDesconto(DescontoCota desconto) {
		// FIXME Implementar a inclusão do desconto da cota
	}
	
	@Override
	@Transactional
	public void incluirDesconto(DescontoDistribuidor desconto) {
		// FIXME Implementar a inclusão do desconto da cota
	}
	
	@Override
	@Transactional
	public void incluirDesconto(DescontoProduto desconto) {
		// FIXME Implementar a inclusão do desconto da cota
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
}
