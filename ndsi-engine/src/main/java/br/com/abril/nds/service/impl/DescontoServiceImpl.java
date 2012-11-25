package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.component.DescontoComponent;
import br.com.abril.nds.component.singleton.ProcessoCadastroDescontoSingleton;
import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProduto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoExcessaoRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;
import br.com.abril.nds.repository.DescontoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.HistoricoDescontoCotaProdutoRepository;
import br.com.abril.nds.repository.HistoricoDescontoFornecedorRepository;
import br.com.abril.nds.repository.HistoricoDescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.HistoricoDescontoProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class DescontoServiceImpl implements DescontoService {

	@Autowired
	private DescontoRepository descontoRepository;
	
	@Autowired
	private DescontoDistribuidorRepository descontoDistribuidorRepository;
	
	@Autowired
	private HistoricoDescontoFornecedorRepository historicoDescontoFornecedorRepository;
	
	@Autowired
	private HistoricoDescontoCotaProdutoRepository historicoDescontoCotaProdutoRepository;
	
	@Autowired
	private HistoricoDescontoProdutoRepository historicoDescontoProdutoRepository;
	
	@Autowired
	private HistoricoDescontoProdutoEdicaoRepository historicoDescontoProdutoEdicaoRepository;
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;

	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private DescontoComponent descontoComponent;

	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;

	@Autowired
	private DescontoProdutoEdicaoExcessaoRepository descontoProdutoEdicaoExcessaoRepository;

	@Autowired
	private DescontoProximosLancamentosRepository descontoProximosLancamentosRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

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

		return this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		return this.descontoProdutoRepository.buscarQuantidadeTipoDescontoProduto(filtro);
	}

	@Override
	@Transactional
	public void excluirDesconto(Long idDesconto,TipoDesconto tipoDesconto) {

		switch (tipoDesconto) {
		case GERAL:

			this.excluirDescontoDistribuidor(idDesconto);

			break;
		case ESPECIFICO:

			this.excluirDescontoCota(idDesconto);

			break;
		case PRODUTO:

			this.excluirDescontoProduto(idDesconto);

			break;

		default:
			throw new RuntimeException("Tipo de Desconto inválido!");
		}
	}


	@Override
	@Transactional
	public void incluirDescontoDistribuidor(BigDecimal valorDesconto, List<Long> fornecedores, Usuario usuario) throws ValidacaoException {

		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}

		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}

		if(fornecedores != null && !fornecedores.isEmpty()) {
			
			Distribuidor distribuidor = distribuidorRepository.obter();
			Date dataAtual = new Date();
			
			/*
			 * Cria um desconto a ser utilizado em um ou mais fornecedores
			 */
			Desconto desconto =  new Desconto();
			desconto.setDataAlteracao(dataAtual);
			desconto.setTipoDesconto(TipoDesconto.GERAL);
			desconto.setUsado(false);
			desconto.setUsuario(usuario);
			desconto.setValor(valorDesconto);
			
			Long idDesconto = descontoRepository.adicionar(desconto);
			
			desconto = descontoRepository.buscarPorId(idDesconto);
			
			for(Long idFornecedor : fornecedores) {
				
				Fornecedor fornecedor = fornecedorRepository.buscarPorId(idFornecedor);
				fornecedor.setDesconto(desconto);
				fornecedorRepository.merge(fornecedor);
				
				/*
				 * Atualiza o historico do desconto para o fornecedor
				 */
				HistoricoDescontoFornecedor historicoDesconto = new HistoricoDescontoFornecedor();
				historicoDesconto.setValor(valorDesconto);
				historicoDesconto.setDesconto(desconto);
				historicoDesconto.setUsuario(usuario);
				historicoDesconto.setDataAlteracao(dataAtual);
				historicoDesconto.setFornecedor(fornecedor);
				historicoDesconto.setDistribuidor(distribuidor);
				
				historicoDescontoFornecedorRepository.adicionar(historicoDesconto);
				
			}
		}
		
	}

	@Override
	@Transactional
	public void incluirDescontoCota(BigDecimal valorDesconto, List<Long> fornecedores, Integer numeroCota, Usuario usuario) {

		if(numeroCota == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Cota deve ser preenchido!");
		}

		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}

		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}

		Date dataAtual = new Date();
		
		Distribuidor distribuidor = distribuidorRepository.obter(); 
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);

		List<Fornecedor> fornecs = fornecedorRepository.obterFornecedoresPorId(fornecedores);
		
		/*
		 * Cria um desconto a ser utilizado em um ou mais fornecedores
		 */
		Desconto desconto =  new Desconto();
		desconto.setDataAlteracao(dataAtual);
		desconto.setTipoDesconto(TipoDesconto.ESPECIFICO);
		desconto.setUsado(false);
		desconto.setUsuario(usuario);
		desconto.setValor(valorDesconto);
		
		Long idDesconto = descontoRepository.adicionar(desconto);
		
		desconto = descontoRepository.buscarPorId(idDesconto);
		
		for(Fornecedor fornecedor : fornecs) {
			/*
			 * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
			 */
			DescontoCotaProdutoExcessao dpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
					TipoDesconto.ESPECIFICO, fornecedor, cota, null, null);
			if(dpe != null) {
				dpe.setDesconto(desconto);
			} else {
				dpe = new DescontoCotaProdutoExcessao();
				dpe.setCota(cota);
				dpe.setDesconto(desconto);
				dpe.setFornecedor(fornecedor);
				dpe.setDistribuidor(distribuidor);
				dpe.setUsuario(usuario);
				dpe.setDescontoPredominante(false);
			}

			dpe.setTipoDesconto(TipoDesconto.ESPECIFICO);
			descontoProdutoEdicaoExcessaoRepository.merge(dpe);	
			
			HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
			hdcp.setDataAlteracao(dataAtual);
			hdcp.setDesconto(desconto);
			hdcp.setValor(valorDesconto);
			hdcp.setDistribuidor(distribuidor);
			hdcp.setFornecedor(fornecedor);
			hdcp.setCota(cota);
			hdcp.setUsuario(usuario);
			
			historicoDescontoCotaProdutoRepository.merge(hdcp);
		}


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void incluirDescontoProduto(DescontoProdutoDTO descontoDTO, Usuario usuario) {

		Produto produto;
		ProdutoEdicao produtoEdicao;
		HistoricoDescontoProdutoEdicao hdpe;
		
		validarEntradaDeDadosInclusaoDescontoPorProduto(descontoDTO);
		
		/*
		 * Cria um desconto a ser utilizado em um ou mais fornecedores
		 */
		Desconto desconto =  new Desconto();
		desconto.setDataAlteracao(new Date());
		desconto.setUsado(false);
		desconto.setUsuario(usuario);
		desconto.setValor(descontoDTO.getDescontoProduto());
		desconto.setTipoDesconto(TipoDesconto.PRODUTO);
		
		Long idDesconto = descontoRepository.adicionar(desconto);
		
		desconto = descontoRepository.buscarPorId(idDesconto);
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();

		/**
		 * 		Produto | ProdutoEdicao | QuantidadeEdicoes | Cota Especifica
		 *  1 |		X	|				|					|
		 *  2 |		X	|				|					|		X
		 *  3 |		X	|		X		|					|
		 *  4 |		X	|		X		|					|		X
		 *  5 |		X	|				|			X		|
		 *  6 |		X	|				|			X		|		X
		 * 
		 */
		switch(obterCombinacaoDesconto(descontoDTO, usuario)) {
		
			case 1:
				
				produto = produtoRepository.obterProdutoPorCodigo(descontoDTO.getCodigoProduto());
				
				if(produto != null) {
					produto.setDesconto(desconto.getValor());
					produtoRepository.merge(produto);
					
					HistoricoDescontoProduto hdp = new HistoricoDescontoProduto();
					hdp.setDataAlteracao(new Date());
					hdp.setProduto(produto);
					hdp.setDesconto(descontoDTO.getDescontoProduto());
					hdp.setDistribuidor(distribuidor);
					hdp.setFornecedor(produto.getFornecedor());
					hdp.setUsuario(usuario);
					
					historicoDescontoProdutoRepository.merge(hdp);
				}
				
				break;
				
			case 2:
				
				produto = produtoRepository.obterProdutoPorCodigo(descontoDTO.getCodigoProduto());
				
				/*
				 * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
				 */
				for(Integer cotaId : descontoDTO.getCotas()) {
					
					Cota cota = cotaRepository.buscarPorId(cotaId.longValue());
					
					DescontoCotaProdutoExcessao dpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
							TipoDesconto.PRODUTO, null, cota, produto, null);
					if(dpe != null) {
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					} else {
						dpe = new DescontoCotaProdutoExcessao();
						dpe.setDistribuidor(distribuidor);
						dpe.setFornecedor(null);
						dpe.setCota(cota);
						dpe.setProduto(produto);
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					}

					dpe.setTipoDesconto(TipoDesconto.PRODUTO);
					descontoProdutoEdicaoExcessaoRepository.merge(dpe);	
					
					HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
					hdcp.setDataAlteracao(new Date());
					hdcp.setDesconto(desconto);
					hdcp.setDistribuidor(distribuidor);
					hdcp.setFornecedor(null);
					hdcp.setUsuario(usuario);
					
					historicoDescontoCotaProdutoRepository.merge(hdcp);
				}
				
				break;
				
			case 3:
				
				produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(descontoDTO.getCodigoProduto(), descontoDTO.getEdicaoProduto());
				
				if(produtoEdicao != null) {
					produtoEdicao.setDesconto(desconto.getValor());
					produtoEdicaoRepository.merge(produtoEdicao);
				}
				
				hdpe = new HistoricoDescontoProdutoEdicao();
				hdpe.setDataAlteracao(new Date());
				hdpe.setDesconto(descontoDTO.getDescontoProduto());
				hdpe.setDistribuidor(distribuidor);
				hdpe.setFornecedor(produtoEdicao.getProduto().getFornecedor());
				hdpe.setUsuario(usuario);
				
				historicoDescontoProdutoEdicaoRepository.merge(hdpe);
				
				break;
				
			case 4:
				
				produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(descontoDTO.getCodigoProduto(), descontoDTO.getEdicaoProduto());
				
				/*
				 * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
				 */
				for(Integer cotaId : descontoDTO.getCotas()) {
					
					Cota cota = cotaRepository.buscarPorId(cotaId.longValue());
					
					DescontoCotaProdutoExcessao dpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
							TipoDesconto.PRODUTO, null, cota, produtoEdicao.getProduto(), produtoEdicao);
					if(dpe != null) {
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					} else {
						dpe = new DescontoCotaProdutoExcessao();
						dpe.setFornecedor(null);
						dpe.setCota(cota);
						dpe.setProduto(produtoEdicao.getProduto());
						dpe.setProdutoEdicao(produtoEdicao);
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					}

					dpe.setTipoDesconto(TipoDesconto.PRODUTO);
					descontoProdutoEdicaoExcessaoRepository.merge(dpe);	
					
					hdpe = new HistoricoDescontoProdutoEdicao();
					hdpe.setDataAlteracao(new Date());
					hdpe.setDesconto(descontoDTO.getDescontoProduto());
					hdpe.setDistribuidor(distribuidor);
					hdpe.setFornecedor(produtoEdicao.getProduto().getFornecedor());
					hdpe.setUsuario(usuario);
					
					historicoDescontoProdutoEdicaoRepository.merge(hdpe);
				}
				
				break;
				
			case 5:
			case 6:
				
				Set<Cota> cotas = obterCotas(descontoDTO.getCotas(), descontoDTO.isTodasCotas());
				
				produto = produtoRepository.obterProdutoPorCodigo(descontoDTO.getCodigoProduto());

				DescontoProximosLancamentos descontoProximosLancamentos = new DescontoProximosLancamentos();

				descontoProximosLancamentos.setDataInicioDesconto(new Date());
				descontoProximosLancamentos.setProduto(produto);
				descontoProximosLancamentos.setQuantidadeProximosLancamaentos(descontoDTO.getQuantidadeEdicoes());
				descontoProximosLancamentos.setValorDesconto(descontoDTO.getDescontoProduto());
				descontoProximosLancamentos.setCotas(cotas);
				descontoProximosLancamentos.setUsuario(usuario);
				descontoProximosLancamentos.setDistribuidor(distribuidor);
				this.descontoProximosLancamentosRepository.adicionar(descontoProximosLancamentos);
				
				break;
				
		}
		
	}

	private int obterCombinacaoDesconto(DescontoProdutoDTO desconto, Usuario usuario) {
		
		/**
		 * 		Produto | ProdutoEdicao | QuantidadeEdicoes | Cota Especifica
		 *  1 |		X	|				|					|
		 *  2 |		X	|				|					|		X
		 *  3 |		X	|		X		|					|
		 *  4 |		X	|		X		|					|		X
		 *  5 |		X	|				|			X		|
		 *  6 |		X	|				|			X		|		X
		 * 
		 */
		
		if(!desconto.isIndProdutoEdicao() 
				&& !(desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isTodasCotas() ) {
			return 1;
		}
		
		if(!desconto.isIndProdutoEdicao() 
				&& !(desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isHasCotaEspecifica() ) {
			return 2;
		}
		
		if(desconto.isIndProdutoEdicao() 
				&& !(desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isTodasCotas() ) {
			return 3;
		}
		
		if(desconto.isIndProdutoEdicao() 
				&& !(desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isHasCotaEspecifica() ) {
			return 4;
		}
		
		if(!desconto.isIndProdutoEdicao() 
				&& (desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isTodasCotas() ) {
			return 5;
		}
		
		if(!desconto.isIndProdutoEdicao() 
				&& (desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isHasCotaEspecifica() ) {
			return 6;
		}
		
		return 0;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Fornecedor> busacarFornecedoresAssociadosADesconto(Long idDesconto,TipoDesconto tipoDesconto) {
		
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();

		switch (tipoDesconto) {
		case GERAL :

			//List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresPorDesconto(idDesconto);
			listaFornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoGeral(descontoRepository.buscarPorId(idDesconto));

			break;

		case ESPECIFICO :

			listaFornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoEspecifico(descontoRepository.buscarPorId(idDesconto));
			
			//DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);

			break;
		}

		return listaFornecedores;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao) {

		if (idDescontoProduto == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "O desconto precisa ser especificado.");
		}

		return this.descontoProdutoRepository.obterCotasDoTipoDescontoProduto(idDescontoProduto, ordenacao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname) {

		if (idCota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota] precisa ser especificada.");
		}

		return this.descontoProdutoRepository.obterTiposDescontoProdutoPorCota(idCota, sortorder, sortname);
	}


	@Override
	@Transactional
	public void processarDescontoDistribuidor(Set<Fornecedor> fornecedores, BigDecimal valorDesconto) {

		if (fornecedores == null) {
			fornecedores = new HashSet<Fornecedor>(this.fornecedorRepository.obterFornecedores());
		}

		Set<ProdutoEdicao> produtosEdicao = produtoEdicaoRepository.filtrarDescontoProdutoEdicaoPorDistribuidor(fornecedores);

		for (Fornecedor fornecedor : fornecedores) {

			for (Cota cota : fornecedor.getCotas()) {

				this.descontoComponent.persistirDesconto(TipoDesconto.GERAL, fornecedor, cota, produtosEdicao, valorDesconto, null);

			}
		}

		//this.processarDesconto(TipoDesconto.GERAL, fornecedores, null, null, valorDesconto, null);
	}


	@Override
	@Transactional
	public void processarDescontoDistribuidor(BigDecimal valorDesconto) {

		this.processarDescontoDistribuidor(null,valorDesconto);
	}


	@Override
	@Transactional
	public void processarDescontoCota(Cota cota, BigDecimal valorDesconto) {

		this.processarDescontoCota(cota, cota.getFornecedores(), valorDesconto);
	}

	@Override
	@Transactional
	public void processarDescontoCota(Cota cota, Set<Fornecedor> fornecedores, BigDecimal valorDesconto) {

		Set<ProdutoEdicao> produtosEdicao = produtoEdicaoRepository.filtrarDescontoProdutoEdicaoPorCota(cota, fornecedores);

		for (Fornecedor fornecedor : fornecedores) {
			this.descontoComponent.persistirDesconto(TipoDesconto.ESPECIFICO, fornecedor, cota, produtosEdicao, valorDesconto, null);
		}

		/*Set<Cota> cotas = new HashSet<Cota>();

		cotas.add(cota);

		this.processarDesconto(TipoDesconto.ESPECIFICO, fornecedores, cotas , null, valorDesconto, null);*/
	}

	@Override
	@Transactional
	public void processarDescontoProduto(ProdutoEdicao produto, BigDecimal valorDesconto, Boolean descontoPredominante) {

		Set<ProdutoEdicao> produtos = new HashSet<ProdutoEdicao>();

		produtos.add(produto);

		this.processarDescontoProduto(produtos, null, valorDesconto, descontoPredominante); 
	}

	@Override
	@Transactional
	public void processarDescontoProduto(Set<ProdutoEdicao> produtos, 
			Set<Cota> cotas, 
			BigDecimal valorDesconto,
			Boolean descontoPredominante) {

		//Set<ProdutoEdicao> produtosEdicao = produtoEdicaoRepository.filtrarDescontoProdutoEdicaoPorCota(cota);

		boolean obterCotas = (cotas == null);

		for (ProdutoEdicao produtoEdicao : produtos) {

			for (Fornecedor fornecedor : produtoEdicao.getProduto().getFornecedores()) {

				if (obterCotas) {
					cotas = fornecedor.getCotas();
				}

				for (Cota cota : cotas) {
					this.descontoComponent.persistirDesconto(TipoDesconto.PRODUTO, fornecedor, cota, produtos, valorDesconto, null);
				}

			}
		}

		//this.processarDesconto(TipoDesconto.PRODUTO, null, cotas, produtos, valorDesconto, descontoPredominante);
	}

	/*
	 * Método que efetua o processamento do desconto de acordo com seu tipo.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * @param fornecedores - lista de fornecedores
	 * @param cotas - list de cotas
	 * @param produtos - lista de produtos
	 * @param valorDesconto - valor do desconto
	 * @param descontoPredominante - desconto predominante
	 */
	private void processarDesconto(TipoDesconto tipoDesconto, 
			Set<Fornecedor> fornecedores, 
			Set<Cota> cotas, 
			Set<ProdutoEdicao> produtos, 
			BigDecimal valorDesconto,
			Boolean descontoPredominante) {

		boolean obterFornecedores = fornecedores == null || fornecedores.isEmpty();

		boolean obterProdutos = produtos == null || produtos.isEmpty();

		boolean obterCotas = cotas == null || cotas.isEmpty();

		if (obterFornecedores) {

			fornecedores = new HashSet<Fornecedor>(this.fornecedorRepository.obterFornecedores());
		}

		Set<ProdutoEdicao> produtosParaInclusao = new HashSet<ProdutoEdicao>();

		for (Fornecedor fornecedor : fornecedores) {

			Set<ProdutoEdicao> produtosParaDesconto = new HashSet<ProdutoEdicao>();

			if (obterProdutos) {

				produtos =
						this.produtoEdicaoRepository.obterProdutosEdicaoPorFornecedor(fornecedor.getId());
			}

			for (ProdutoEdicao produtoEdicao : produtos) {

				Set<Fornecedor> fornecedoresProduto = produtoEdicao.getProduto().getFornecedores();

				if (fornecedoresProduto == null ||
						!fornecedoresProduto.contains(fornecedor)) {

					continue;
				}

				produtosParaDesconto.add(produtoEdicao);
			}

			if (obterCotas) {

				cotas = this.cotaRepository.obterCotasPorFornecedor(fornecedor.getId());
			}

			for (Cota cota : cotas) {

				Set<Fornecedor> fornecedoresCota = cota.getFornecedores();

				if (!obterCotas &&
						(fornecedoresCota == null
						|| !fornecedoresCota.contains(fornecedor))) {

					continue;
				}

				produtosParaInclusao = 
						this.descontoComponent.filtrarProdutosPassiveisDeDesconto(
								tipoDesconto, fornecedor,cota ,produtosParaDesconto);

				this.descontoComponent.persistirDesconto(
						tipoDesconto, fornecedor, cota,
						produtosParaInclusao, valorDesconto, descontoPredominante);
			}
		}
	}

	/*
	 * Efetua a validação da exclusão do desconto.
	 * 
	 * @param dataUltimaAlteracao - data da última alteração
	 */
	private void validarExclusaoDesconto(Date dataUltimaAlteracao){

		Distribuidor distribuidor = distribuidorRepository.obter();

		if(dataUltimaAlteracao.compareTo(distribuidor.getDataOperacao()) < 0){
			throw new ValidacaoException(TipoMensagem.WARNING,"Desconto não pode ser excluido fora da data vigente!");
		}
	}

	/*
	 * Valida a entrada de dados para inclusão de desconto por produto.
	 * 
	 * @param desconto - dados do desconto de produto
	 */
	private void validarEntradaDeDadosInclusaoDescontoPorProduto(DescontoProdutoDTO desconto) {

		List<String> mensagens = new ArrayList<String>();

		if (desconto.getCodigoProduto() == null || desconto.getCodigoProduto().isEmpty()) {
			mensagens.add("O campo Código deve ser preenchido!");
		}

		if(desconto.isIndProdutoEdicao()) {
			if (desconto.getEdicaoProduto() == null && desconto.getQuantidadeEdicoes() == null) {
				mensagens.add("O campo Edição específica ou Edições deve ser preenchido!");
			}
		}

		Integer quantidadeEdicoesExistentes = 
				this.produtoEdicaoRepository.obterQuantidadeEdicoesPorCodigoProduto(desconto.getCodigoProduto());

		if (desconto.getQuantidadeEdicoes() != null && 
				quantidadeEdicoesExistentes < desconto.getQuantidadeEdicoes()) {

			mensagens.add("O número máximo de edições que pode ser informado é: " + quantidadeEdicoesExistentes);
		}

		if (desconto.getDescontoProduto() == null) {

			mensagens.add("O campo Desconto deve ser preenchido!");
		}

		if (!desconto.isHasCotaEspecifica() && !desconto.isTodasCotas()) {

			mensagens.add("O campo de cotas deve ser preenchido!");
		}

		if (desconto.isHasCotaEspecifica() && desconto.getCotas() == null) {

			mensagens.add("Ao menos uma cota deve ser selecionada!");
		}

		if (!mensagens.isEmpty()) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}

	/*
	 * Obtém as cotas por id ou todas.
	 * 
	 * @param idsCotas - id's das cotas
	 * @param isTodasCotas - flag para obter todas as cotas
	 * 
	 * @return {@link Set} de {@link Cota}
	 */
	private Set<Cota> obterCotas(List<Integer> idsCotas, boolean isTodasCotas) {

		Set<Cota> cotas = null;

		if (idsCotas != null) {

			cotas = new LinkedHashSet<Cota>();

			for (Integer numeroCota : idsCotas) {

				Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);

				cotas.add(cota);
			}

		} else if (isTodasCotas) {

			cotas = new HashSet<Cota>(this.cotaRepository.buscarTodos());
		}

		return cotas;
	}

	/*
	 * 
	 * Excluir um desconto do distribuidor e atualiza os descontos dos produtos edição
	 * 
	 * @param idDesconto - identificador do desconto a ser removido
	 */
	private void excluirDescontoDistribuidor(Long idDesconto){

		Desconto desconto = descontoRepository.buscarPorId(idDesconto);
		
		validarExclusaoDesconto(desconto.getDataAlteracao());
		
		List<Fornecedor> fornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoGeral(desconto);

		for (Fornecedor fornecedor : fornecedores) {

			Desconto ultimoDescontoValido = descontoRepository.buscarUltimoDescontoValido(idDesconto, fornecedor);

			if (ultimoDescontoValido != null) {

				if (ultimoDescontoValido.getDataAlteracao().before(desconto.getDataAlteracao())) {

					fornecedor.setDesconto(null);
					fornecedorRepository.merge(fornecedor);
					
					//processarDescontoDistribuidor(fornecARemoverDesconto, ultimoDesconto.getDesconto());
				}

			}
			
		}

	}

	/*
	 * 
	 * Excluir um desconto da cota e atualiza os descontos dos produtos edição.
	 * 
	 * @param idDesconto - identificador do desconto a ser removido
	 */
	private void excluirDescontoCota(Long idDesconto){

		DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);

		validarExclusaoDesconto(descontoCota.getDataAlteracao());

		for (Fornecedor fornecedor : descontoCota.getFornecedores()) {

			DescontoCota ultimoDescontoCota =
					descontoCotaRepository.buscarUltimoDescontoValido(idDesconto, fornecedor, descontoCota.getCota());

			if (ultimoDescontoCota != null) {

				if(ultimoDescontoCota.getDataAlteracao().before(descontoCota.getDataAlteracao())){

					Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();

					fornecedores.add(fornecedor);

					processarDescontoCota(descontoCota.getCota(), fornecedores, ultimoDescontoCota.getDesconto());
				}
			} else {

				descontoComponent.removerDescontos(fornecedor, descontoCota.getCota(), TipoDesconto.ESPECIFICO);

				processarExclusaoPorDistribuidor(descontoCota.getCota(), fornecedor);
			}
		}

		descontoCota.setFornecedores(null);

		descontoCotaRepository.remover(descontoCota);
	}

	/*
	 * 
	 * Excluir um desconto da cota e atualiza os descontos dos produtos edição.
	 * 
	 * @param idDesconto - identificador do desconto a ser removido
	 */
	private void excluirDescontoProduto(Long idDesconto){

		DescontoProduto descontoProduto = descontoProdutoRepository.buscarPorId(idDesconto);

		validarExclusaoDesconto(descontoProduto.getDataAlteracao());

		for (Cota cota : descontoProduto.getCotas()) {

			DescontoProduto ultimoDescontoProduto =
					descontoProdutoRepository.buscarUltimoDescontoValido(idDesconto, cota, descontoProduto.getProdutoEdicao());

			if (ultimoDescontoProduto != null) {

				if (ultimoDescontoProduto.getDataAlteracao().before(descontoProduto.getDataAlteracao())) {

					Set<ProdutoEdicao> produtosEdicao = new HashSet<ProdutoEdicao>();
					produtosEdicao.add(descontoProduto.getProdutoEdicao());

					Set<Cota> cotas = new HashSet<Cota>();
					cotas.add(cota);

					processarDescontoProduto(produtosEdicao, cotas, ultimoDescontoProduto.getDesconto(), null);
				}
			} else {

				processarExclusaoDescontoProdutoCota(descontoProduto, cota);
			}
		}

		descontoProduto.setCotas(null);

		descontoProdutoRepository.remover(descontoProduto);
	}

	/*
	 * Processa desconto Produto para exclusão de desconto de produto
	 * 
	 * @param descontoProduto - desconto produto
	 * 
	 * @param cota - cota
	 */
	private void processarExclusaoDescontoProdutoCota(DescontoProduto descontoProduto, Cota cota) {

		for(Fornecedor fornecedor : cota.getFornecedores()){

			DescontoCota ultimoDescontoCota = descontoCotaRepository.buscarUltimoDescontoValido(fornecedor,cota);

			descontoComponent.removerDescontos(fornecedor,cota, descontoProduto.getProdutoEdicao() ,TipoDesconto.PRODUTO);

			if(ultimoDescontoCota!= null){

				Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();

				fornecedores.add(fornecedor);

				processarDescontoCota(cota, fornecedores, ultimoDescontoCota.getDesconto());
			}
			else{

				processarExclusaoPorDistribuidor(cota, fornecedor);
			} 
		}
	}

	/*
	 * Processa desconto Distribuidor para exclusão de desconto Cota ou de Produto.
	 * 
	 * @param cota - cota
	 * 
	 * @param fornecedor - fornecedor
	 */
	private void processarExclusaoPorDistribuidor(Cota cota,Fornecedor fornecedor) {

		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		Set<Cota> cotas = new HashSet<Cota>();

		fornecedores.add(fornecedor);	
		cotas.add(cota);

		DescontoDistribuidor ultimoDescontoDistribuidor = descontoDistribuidorRepository.buscarUltimoDescontoValido(fornecedor);

		BigDecimal valorDescontoProduto = (ultimoDescontoDistribuidor!= null)
				?ultimoDescontoDistribuidor.getDesconto():BigDecimal.ZERO; 

				processarDesconto(TipoDesconto.GERAL, fornecedores,cotas,null,valorDescontoProduto, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
    public BigDecimal obterDescontoPorCotaProdutoEdicao(Cota cota,
            ProdutoEdicao produtoEdicao) {
        Validate.notNull(cota, "Cota não deve ser nula!");
        Validate.notNull(produtoEdicao, "Edição do produto não deve ser nula!");
        BigDecimal percentual = null;
        if (produtoEdicao.getProduto().isPublicacao()) {
            //Neste caso, o produto possui apenas um fornecedor
            //Recuperar o desconto utilizando a cota, o produto edição e o fornecedor
            Fornecedor fornecedor = produtoEdicao.getProduto().getFornecedor();
            // TODO: Sergio, favor setar o valor de desconto correto neste ponto após a funcionalidade ficar disponível
            percentual = new BigDecimal(10);//descontoProdutoEdicaoRepository.obterDescontoPorCotaProdutoEdicao(cota.getId(), produtoEdicao.getId(), fornecedor.getId());
        } else {
            //Produto possivelmente com mais de um fornecedor, seguindo
            // a instrução passada, utilizar o desconto do produto
            percentual = produtoEdicao.getProduto().getDesconto();
        }
        return Util.nvl(percentual, BigDecimal.ZERO);
    }
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterComissaoCota(Integer numeroCota){

		BigDecimal desc = BigDecimal.ZERO;

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		filtro.setNumeroCota(numeroCota);

		List<TipoDescontoCotaDTO> descontos = this.descontoCotaRepository.obterDescontoCota(filtro);

		for (TipoDescontoCotaDTO dto : descontos){

			desc = desc.add(dto.getDesconto());
		}

		return desc.compareTo(BigDecimal.ZERO) > 0 ? desc.divide(new BigDecimal(descontos.size())) : BigDecimal.ZERO;
	}

	@Override
	@Async
	@Transactional
	public Future<String> executarDescontoEspecifico(final Integer numeroCota, final BigDecimal desconto, final List<Long> fornecedores, Usuario usuario) {

		ProcessoCadastroDescontoSingleton.iniciarProcesso(TipoDesconto.ESPECIFICO);

		try {

			incluirDescontoCota(desconto, fornecedores, numeroCota, usuario);

		} finally {
			ProcessoCadastroDescontoSingleton.encerrarProcesso(TipoDesconto.ESPECIFICO);
		}

		return new AsyncResult<String>("Desconto cadastrado com sucesso !");
	}

	@Override
	@Async
	@Transactional
	public Future<String> executarDescontoProduto(DescontoProdutoDTO desconto, List<Integer> cotas, Usuario usuario) {

		ProcessoCadastroDescontoSingleton.iniciarProcesso(TipoDesconto.PRODUTO);

		try {

			desconto.setCotas(cotas);
			incluirDescontoProduto(desconto, usuario);

		} finally {
			ProcessoCadastroDescontoSingleton.encerrarProcesso(TipoDesconto.PRODUTO);
		}

		return new AsyncResult<String>("Desconto cadastrado com sucesso !");
	}	

	@Override
	@Async
	@Transactional
	public Future<String> executarDescontoGeral(BigDecimal desconto, List<Long> fornecedores, Usuario usuario) {

		ProcessoCadastroDescontoSingleton.iniciarProcesso(TipoDesconto.GERAL);

		try {

			incluirDescontoDistribuidor(desconto, fornecedores, usuario);

		} finally {
			ProcessoCadastroDescontoSingleton.encerrarProcesso(TipoDesconto.GERAL);
		}

		return new AsyncResult<String>("Desconto cadastrado com sucesso !");
	}

}