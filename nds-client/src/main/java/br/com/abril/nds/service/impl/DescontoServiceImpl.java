package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class DescontoServiceImpl implements DescontoService {

	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Autowired
	private DescontoDistribuidorRepository descontoDistribuidorRepository;
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;
	
	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CotaRepository cotaRepository;

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
		
		return this.descontoCotaRepository.obterDescontoCota(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		
		return this.descontoCotaRepository.obterQuantidadeDescontoCota(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		// FIXME Implementar a consulta paginada de desconto
		return getMockProduto();
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {
		// FIXME Implementar a consulta de desconto
		return getMockProduto().size();
	}
	
	@Override
	@Transactional
	public void excluirDesconto(Long idDesconto,br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto) {
		
		switch (tipoDesconto) {
			case GERAL:
				
				DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(desconto.getDataAlteracao());
				desconto.setFornecedores(null);
				descontoDistribuidorRepository.remover(desconto);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			case ESPECIFICO:
				
				DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoCota.getDataAlteracao());
				descontoCota.setFornecedores(null);
				descontoCotaRepository.remover(descontoCota);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			case PRODUTO:
		
				DescontoProduto descontoProduto = descontoProdutoRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoProduto.getDataAlteracao());
				descontoProduto.setCotas(null);
				descontoProdutoRepository.remover(descontoProduto);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			}
	}
	
	private void validarExclusaoDesconto(Date dataUltimaAlteracao){
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if(dataUltimaAlteracao.compareTo(distribuidor.getDataOperacao()) < 0){
			throw new ValidacaoException(TipoMensagem.WARNING,"Desconto não pode ser excluido fora da data vigente!");
		}
	}
	
	@Override
	@Transactional
	public void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Usuario usuario) {
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		DescontoDistribuidor desconto = new DescontoDistribuidor();
		desconto.setDesconto(valorDesconto);
		desconto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));
		desconto.setDataAlteracao(new Date());
		desconto.setFornecedores(new HashSet<Fornecedor>());
		desconto.getFornecedores().addAll(fornecedorRepository.obterFornecedoresPorId(fornecedores));
		desconto.setDistribuidor(distribuidorRepository.obter());
		
		descontoDistribuidorRepository.adicionar(desconto);
		
		//FIXME chamar componente de geração de desconto PRODUTO EDIÇÂO
	}
	
	@Override
	@Transactional
	public void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario) {
		
		if(numeroCota == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Cota deve ser preenchido!");
		}
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		DescontoCota descontoCota = new DescontoCota();
		descontoCota.setDesconto(valorDesconto);
		descontoCota.setCota(cotaRepository.obterPorNumerDaCota(numeroCota));
		descontoCota.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));
		descontoCota.setDataAlteracao(new Date());
		descontoCota.setFornecedores(new HashSet<Fornecedor>());
		descontoCota.getFornecedores().addAll(fornecedorRepository.obterFornecedoresPorId(fornecedores));
		descontoCota.setDistribuidor(distribuidorRepository.obter());
		
		descontoCotaRepository.adicionar(descontoCota);
		
		//FIXME chamar componente de geração de desconto PRODUTO EDIÇÂO
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
