package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.component.DescontoComponent;
import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.DescontoEditorDTO;
import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoEditorDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaHistoricoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoEditor;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoFornecedor;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProduto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.model.planejamento.Lancamento;
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
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.HistoricoDescontoCotaProdutoRepository;
import br.com.abril.nds.repository.HistoricoDescontoEditorRepository;
import br.com.abril.nds.repository.HistoricoDescontoFornecedorRepository;
import br.com.abril.nds.repository.HistoricoDescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.HistoricoDescontoProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.DescontoService;
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
	private HistoricoDescontoEditorRepository historicoDescontoEditorRepository;
	
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
	private EditorRepository editorRepository;

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

		List<TipoDescontoProdutoDTO> historicoDesconto = descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
		
		Calendar dataOperacao = Calendar.getInstance();
		dataOperacao.setTime(distribuidorRepository.obterDataOperacaoDistribuidor());
		
		dataOperacao.set(Calendar.AM_PM, 0);
		dataOperacao.set(Calendar.HOUR, 0);
		dataOperacao.set(Calendar.MINUTE, 0);
		dataOperacao.set(Calendar.SECOND, 0);
		dataOperacao.set(Calendar.MILLISECOND, 0);
		
		for(TipoDescontoProdutoDTO tdp : historicoDesconto) {
			
			Desconto desconto = descontoRepository.buscarPorId(tdp.getIdTipoDesconto());
			
			Calendar c1 = Calendar.getInstance();
			
			c1.setTime(tdp.getDataAlteracao());
			
			c1.set(Calendar.AM_PM, 0);
			c1.set(Calendar.HOUR, 0);
			c1.set(Calendar.MINUTE, 0);
			c1.set(Calendar.SECOND, 1);
			c1.set(Calendar.MILLISECOND, 0);
			
			if(isExclusaoBloqueada(c1.before(dataOperacao), desconto, tdp) ) {
				tdp.setExcluivel(false);
			} else {
				tdp.setExcluivel(true);
			}
		}
		
		return historicoDesconto;
	}

	private boolean isExclusaoBloqueada(boolean alteradoNaDiaAtualOP, Desconto desconto, TipoDescontoProdutoDTO tdp) {
       	
	    if(tdp.getQtdeProxLcmt() == null) {
    	    
    	    if(alteradoNaDiaAtualOP|| desconto != null && desconto.isUsado())
    	        return true;	    
    	    else
    	        return false;
	    }
	    
	    if(tdp.getQtdeProxLcmtAtual().equals(0))
	        return true;
	            	    
	    return false;

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
			
		case EDITOR:

			this.excluirDescontoEditor(idDesconto);

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
            Date dataAtual = distribuidorRepository.obterDataOperacaoDistribuidor();

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
				historicoDesconto.setDesconto(desconto);
				historicoDesconto.setUsuario(usuario);
				historicoDesconto.setDataAlteracao(new Date());
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

		Distribuidor distribuidor = distribuidorRepository.obter(); 
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);

		List<Fornecedor> fornecs = fornecedorRepository.obterFornecedoresPorId(fornecedores);
		
		Desconto desconto =  obterDesconto(valorDesconto, numeroCota, usuario);
		
		for(Fornecedor fornecedor : fornecs) {
			 
			/*
             * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
             */
			DescontoCotaProdutoExcessao dpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
					TipoDesconto.ESPECIFICO, null, fornecedor, null, cota, null, null);
			
			if(dpe == null) {
				
				dpe = new DescontoCotaProdutoExcessao();
				dpe.setCota(cota);
				dpe.setDesconto(desconto);
				dpe.setFornecedor(fornecedor);
				dpe.setDistribuidor(distribuidor);
				dpe.setUsuario(usuario);
				dpe.setDescontoPredominante(false);
				dpe.setTipoDesconto(TipoDesconto.ESPECIFICO);
				descontoProdutoEdicaoExcessaoRepository.adicionar(dpe);	
				
				criarNovoHistoricoDescontoCotaProdutoExcessao(desconto, valorDesconto, distribuidor, fornecedor, cota, usuario);
				
			} else if(	dpe.getDesconto() == null ||
						!desconto.getId().equals(dpe.getDesconto().getId())){
					
				dpe.setDesconto(desconto);
				descontoProdutoEdicaoExcessaoRepository.merge(dpe);
				
				criarNovoHistoricoDescontoCotaProdutoExcessao(desconto, valorDesconto, distribuidor, fornecedor, cota, usuario);
				
			}
			
		}

	}
	
	private void criarNovoHistoricoDescontoCotaProdutoExcessao(
			Desconto desconto, 
			BigDecimal valorDesconto, 
			Distribuidor distribuidor, 
			Fornecedor fornecedor,
			Cota cota,
			Usuario usuario) {
		
		HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
		hdcp.setDataAlteracao(new Date());
		hdcp.setDesconto(desconto);
		hdcp.setValor(valorDesconto);
		hdcp.setDistribuidor(distribuidor);
		hdcp.setFornecedor(fornecedor);
		hdcp.setCota(cota);
		hdcp.setUsuario(usuario);
		
		historicoDescontoCotaProdutoRepository.adicionar(hdcp);
		
	}

	    /**
     * Se ja existir desconto com este valor sendo utilizado atualmente na cota em questão o registro do mesmo sera
     * retornado, do contrario será criado novo registro de desconto.
     * 
     * @param valorDesconto
     * @param numeroCota
     * @param usuario
     * 
     * @return Desconto
     */
	private Desconto obterDesconto(BigDecimal valorDesconto, Integer numeroCota, Usuario usuario) {
		
		Desconto desconto = descontoCotaRepository.buscarDescontoCotaProdutoExcessao(
				TipoDesconto.ESPECIFICO, valorDesconto, numeroCota);
		
		if(desconto!=null) {
			return desconto;
		}

		Date dataAtual = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Desconto descontoNew =  new Desconto();
		descontoNew.setDataAlteracao(dataAtual);
		descontoNew.setTipoDesconto(TipoDesconto.ESPECIFICO);
		descontoNew.setUsado(false);
		descontoNew.setUsuario(usuario);
		descontoNew.setValor(valorDesconto);
		Long idDesconto = descontoRepository.adicionar(descontoNew);
		descontoNew = descontoRepository.buscarPorId(idDesconto);
		
		return descontoNew;
		
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

		Date dataAtual = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		/*
		 * Cria um desconto a ser utilizado em um ou mais fornecedores
		 */
		Desconto desconto =  new Desconto();
		desconto.setDataAlteracao(dataAtual);
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
		 *  5 |		X	|		X		|			X		|
		 *  6 |		X	|		X		|			X		|		X
		 * 
		 */
		switch(obterCombinacaoDesconto(descontoDTO, usuario)) {
		
			case 1:
				
				produto = produtoRepository.obterProdutoPorCodigoProdin(descontoDTO.getCodigoProduto());
				
				if(produto != null) {
					produto.setDescontoProduto(desconto);
					produtoRepository.merge(produto);
					
					HistoricoDescontoProduto hdp = new HistoricoDescontoProduto();
					hdp.setDataAlteracao(new Date());
					hdp.setProduto(produto);
					hdp.setDesconto(desconto);
					hdp.setDistribuidor(distribuidor);
					hdp.setFornecedor(produto.getFornecedor());
					hdp.setUsuario(usuario);
					
					historicoDescontoProdutoRepository.merge(hdp);
				}
				
				break;
				
			case 2:
				
				produto = produtoRepository.obterProdutoPorCodigoProdin(descontoDTO.getCodigoProduto());
				
				                                                                                                                        /*
             * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
             */
				for(Integer numeroCota : descontoDTO.getCotas()) {
					
					Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota.intValue());
					
					DescontoCotaProdutoExcessao dcpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
							TipoDesconto.PRODUTO, null, null, null, cota, produto.getId(), null);
					if(dcpe != null) {
						dcpe.setDesconto(desconto);
						dcpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					} else {
						dcpe = new DescontoCotaProdutoExcessao();
						dcpe.setDistribuidor(distribuidor);
						dcpe.setFornecedor(produto.getFornecedor());
						dcpe.setCota(cota);
						dcpe.setUsuario(usuario);
						dcpe.setProduto(produto);
						dcpe.setDesconto(desconto);
						dcpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					}

					dcpe.setTipoDesconto(TipoDesconto.PRODUTO);
					descontoProdutoEdicaoExcessaoRepository.merge(dcpe);	
					
					HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
					hdcp.setDataAlteracao(new Date());
					hdcp.setDesconto(desconto);
					hdcp.setDistribuidor(distribuidor);
					hdcp.setFornecedor(produto.getFornecedor());
					hdcp.setCota(cota);
					hdcp.setUsuario(usuario);
					hdcp.setValor(desconto.getValor());
					hdcp.setProduto(produto);
					
					historicoDescontoCotaProdutoRepository.merge(hdcp);
					
				}
				
				break;
				
			case 3:
				
				produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(descontoDTO.getCodigoProduto(), descontoDTO.getEdicaoProduto());
				
				if(produtoEdicao != null) {

					produtoEdicao.setDescontoProdutoEdicao(desconto);
					produtoEdicaoRepository.merge(produtoEdicao);
					
					hdpe = new HistoricoDescontoProdutoEdicao();
					hdpe.setDataAlteracao(new Date());
					hdpe.setDesconto(desconto);
					hdpe.setProduto(produtoEdicao.getProduto());
					hdpe.setProdutoEdicao(produtoEdicao);
					hdpe.setDistribuidor(distribuidor);
					hdpe.setFornecedor(produtoEdicao.getProduto().getFornecedor());
					hdpe.setUsuario(usuario);
					
					historicoDescontoProdutoEdicaoRepository.merge(hdpe);
				}
				
				break;
				
			case 4:
				
				produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(descontoDTO.getCodigoProduto(), descontoDTO.getEdicaoProduto());
				
				                                                                                                                        /*
             * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
             */
				for(Integer numeroCota : descontoDTO.getCotas()) {
					
					Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota.intValue());
					
					DescontoCotaProdutoExcessao dpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
							TipoDesconto.PRODUTO, null, null, null, cota, produtoEdicao.getProduto().getId(), produtoEdicao.getId());
					if(dpe != null) {
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					} else {
						dpe = new DescontoCotaProdutoExcessao();
						dpe.setDistribuidor(distribuidor);
						dpe.setUsuario(usuario);
						dpe.setCota(cota);
						if(produtoEdicao != null) {
							dpe.setProduto(produtoEdicao.getProduto());
							dpe.setFornecedor(produtoEdicao.getProduto().getFornecedor());
						}
						dpe.setProdutoEdicao(produtoEdicao);
						dpe.setDesconto(desconto);
						dpe.setDescontoPredominante(descontoDTO.isDescontoPredominante());
					}

					dpe.setTipoDesconto(TipoDesconto.PRODUTO);
					descontoProdutoEdicaoExcessaoRepository.merge(dpe);	
					
					HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
					hdcp.setDataAlteracao(new Date());
					hdcp.setDesconto(desconto);
					hdcp.setDistribuidor(distribuidor);
					hdcp.setProdutoEdicao(produtoEdicao);
					if(produtoEdicao != null) {
						hdcp.setFornecedor(produtoEdicao.getProduto().getFornecedor());
					}
					hdcp.setCota(cota);
					hdcp.setUsuario(usuario);
					hdcp.setValor(desconto.getValor());
					if(produtoEdicao != null) {
						hdcp.setProduto(produtoEdicao.getProduto());
					}
					
					historicoDescontoCotaProdutoRepository.merge(hdcp);

				}
				
				break;
				
			case 5:
			case 6:
				
				Set<Cota> cotas = obterCotas(descontoDTO.getCotas());
				
				produto = produtoRepository.obterProdutoPorCodigoProdin(descontoDTO.getCodigoProduto());

				DescontoProximosLancamentos descontoProximosLancamentos = new DescontoProximosLancamentos();

				descontoProximosLancamentos.setDataInicioDesconto(dataAtual);
				descontoProximosLancamentos.setProduto(produto);
				descontoProximosLancamentos.setQuantidadeProximosLancamaentos(descontoDTO.getQuantidadeEdicoes());
				descontoProximosLancamentos.setQuantidadeProximosLancamentosOriginal(descontoDTO.getQuantidadeEdicoes());
				descontoProximosLancamentos.setValorDesconto(desconto.getValor());
				descontoProximosLancamentos.setDesconto(desconto);
				descontoProximosLancamentos.setAplicadoATodasAsCotas(descontoDTO.isTodasCotas());
				
				if(descontoDTO.isTodasCotas()) {
					descontoProximosLancamentos.setCotas(null);
				} else {
					descontoProximosLancamentos.setCotas(cotas);
				}
				
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
		 *  5 |		X	|		X		|			X		|
		 *  6 |		X	|		X		|			X		|		X
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
		
		if(desconto.isIndProdutoEdicao() 
				&& (desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isTodasCotas() ) {
			return 5;
		}
		
		if(desconto.isIndProdutoEdicao() 
				&& (desconto.getQuantidadeEdicoes() != null && desconto.getQuantidadeEdicoes() > 0) 
				&& desconto.isHasCotaEspecifica() ) {
			return 6;
		}
		
		return 0;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Fornecedor> buscarFornecedoresAssociadosADesconto(Long idDesconto,TipoDesconto tipoDesconto) {
		
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();

		switch (tipoDesconto) {
		case GERAL :

			listaFornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoGeral(descontoRepository.buscarPorId(idDesconto));

			break;

		case ESPECIFICO :

			listaFornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoEspecifico(descontoRepository.buscarPorId(idDesconto));
			

			break;
		default:
		    break;
		}

		return listaFornecedores;
	}

	@Override
	public List<Cota> buscarCotasAssociadasAoDescontoEditor(Long idDesconto, TipoDesconto tipoDesconto) {
		
		List<Cota> listaCotas = new ArrayList<Cota>();

		listaCotas = descontoRepository.buscarCotasQueUsamDescontoEditor(descontoRepository.buscarPorId(idDesconto));

		return listaCotas;
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
     * Efetua a validação da exclusão do desconto.
     * @param dataUltimaAlteracao - data da última alteração
     */
	private void validarExclusaoDesconto(Desconto desconto){

		if(desconto.getDataAlteracao().compareTo(this.distribuidorRepository.obterDataOperacaoDistribuidor()) < 0)
            throw new ValidacaoException(TipoMensagem.WARNING, "Desconto não pode ser excluido fora da data vigente!");
				
		if(desconto.isUsado())
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Desconto não pode ser excluido pois já está sendo utilizado!");
			
	}

	/*
     * Valida a entrada de dados para inclusão de desconto por produto.
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
     * @param idsCotas - id's das cotas
     * @param isTodasCotas - flag para obter todas as cotas
     * @return {@link Set} de {@link Cota}
     */
	private Set<Cota> obterCotas(List<Integer> idsCotas) {

		Set<Cota> cotas = null;

		if (idsCotas != null) {

			cotas = new LinkedHashSet<Cota>();

			for (Integer numeroCota : idsCotas) {

				Cota cota = this.cotaRepository.obterPorNumeroDaCota(numeroCota);

				cotas.add(cota);
			}

		}

		return cotas;
	}

	/*
     * Excluir um desconto do distribuidor e atualiza os descontos dos produtos edição
     * @param idDesconto - identificador do desconto a ser removido
     */
	private void excluirDescontoDistribuidor(Long idDesconto){

		Desconto desconto = descontoRepository.buscarPorId(idDesconto);
		
		validarExclusaoDesconto(desconto);
		
		List<Fornecedor> fornecedores = descontoRepository.buscarFornecedoresQueUsamDescontoGeral(desconto);

		for (Fornecedor fornecedor : fornecedores) {

			HistoricoDescontoFornecedor hdf = historicoDescontoFornecedorRepository.buscarHistoricoDescontoFornecedorPor(desconto, fornecedor);
			
			if(hdf != null)
				historicoDescontoFornecedorRepository.remover(hdf);	
			
			HistoricoDescontoFornecedor ultimoValido  = historicoDescontoFornecedorRepository.buscarUltimoDescontoValido(fornecedor);
						
			fornecedor.setDesconto(ultimoValido==null ? null : ultimoValido.getDesconto());
			fornecedorRepository.merge(fornecedor);			
							
		}

	}
	
	/*
     * Excluir um desconto do editor 
     * @param idDesconto - identificador do desconto a ser removido
     */
	private void excluirDescontoEditor(Long idDesconto){

		Desconto desconto = descontoRepository.buscarPorId(idDesconto);

		validarExclusaoDesconto(desconto);
		
		List<Cota> cotas = descontoRepository.buscarCotasQueUsamDescontoEspecifico(desconto);
		
		Editor editor = null;
		
		if(cotas == null || cotas.isEmpty()) {
			
			DescontoCotaProdutoExcessao dcpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(TipoDesconto.EDITOR, desconto, null, null, null, null, null);
			
			if(dcpe == null) {
				
				editor = descontoRepository.buscarEditorUsaDescontoEditor(desconto);

				HistoricoDescontoEditor hde = historicoDescontoEditorRepository.buscarHistoricoDescontoEditorPor(desconto, editor);
				
				if(hde != null)
					historicoDescontoEditorRepository.remover(hde);	
				
				HistoricoDescontoEditor ultimoValido  = historicoDescontoEditorRepository.buscarUltimoDescontoValido(editor);
							
				editor.setDesconto(ultimoValido==null ? null : ultimoValido.getDesconto());
				editorRepository.merge(editor);			
				
			} else {
				
				editor = dcpe.getEditor();
				
				FiltroConsultaHistoricoDescontoDTO filtro = new FiltroConsultaHistoricoDescontoDTO();
				filtro.setIdDesconto(desconto.getId());
				filtro.setIdEditor(editor.getId());
				
				HistoricoDescontoCotaProdutoExcessao hdcpe = historicoDescontoCotaProdutoRepository.buscarUltimoDescontoValido(filtro);
				
				if(dcpe != null)
					descontoProdutoEdicaoExcessaoRepository.remover(dcpe);
				
				if(hdcpe != null)
					historicoDescontoCotaProdutoRepository.remover(hdcpe);
			}
			
		}
		
		for(Cota cota : cotas) {
			
			DescontoCotaProdutoExcessao dcpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(TipoDesconto.EDITOR, desconto, null, null, cota, null, null);
			
			if(editor == null && dcpe != null) {
				editor = dcpe.getEditor();
			} if(editor != null && dcpe != null) {
				editor = dcpe.getEditor();
			}else {
				continue;
			}
				
			FiltroConsultaHistoricoDescontoDTO filtro = new FiltroConsultaHistoricoDescontoDTO();
			filtro.setIdDesconto(desconto.getId());
			filtro.setIdCota(cota.getId());
			filtro.setIdEditor(editor.getId());
			
			HistoricoDescontoCotaProdutoExcessao hdcpe = historicoDescontoCotaProdutoRepository.buscarUltimoDescontoValido(filtro);
			
			if(dcpe != null)
				descontoProdutoEdicaoExcessaoRepository.remover(dcpe);
			
			if(hdcpe != null)
				historicoDescontoCotaProdutoRepository.remover(hdcpe);
			
		}

	}

	/*
	 * 
	 * Excluir desconto(s) da cota.
	 * 
	 * @param idDesconto - identificador do desconto a ser removido
	 */
	private void excluirDescontoCota(Long idDesconto){

		Desconto desconto = descontoRepository.buscarPorId(idDesconto);

		validarExclusaoDesconto(desconto);
		
		List<Cota> cotas = descontoRepository.buscarCotasQueUsamDescontoEspecifico(desconto);
		
		for(Cota cota : cotas) {
			Set<Fornecedor> fornecedores = cota.getFornecedores();
			for(Fornecedor fornecedor : fornecedores) {
				DescontoCotaProdutoExcessao dcpe = descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(TipoDesconto.ESPECIFICO, desconto, fornecedor, null, cota, null, null);
				
				FiltroConsultaHistoricoDescontoDTO filtro = new FiltroConsultaHistoricoDescontoDTO();
				filtro.setIdDesconto(desconto.getId());
				filtro.setIdCota(cota.getId());
				filtro.setIdFornecedor(fornecedor.getId());
				
				HistoricoDescontoCotaProdutoExcessao hdcpe = historicoDescontoCotaProdutoRepository.buscarUltimoDescontoValido(filtro);
				
				if(dcpe != null)
					descontoProdutoEdicaoExcessaoRepository.remover(dcpe);
				
				if(hdcpe != null)
					historicoDescontoCotaProdutoRepository.remover(hdcpe);
			}
		}
	}

	private void cleanFiltroConsultaHistoricoDescontoDTO(FiltroConsultaHistoricoDescontoDTO filtroConsultaHistorico) {
		filtroConsultaHistorico.setIdCota(null);
		filtroConsultaHistorico.setIdDesconto(null);
		filtroConsultaHistorico.setIdFornecedor(null);
		filtroConsultaHistorico.setIdProduto(null);
		filtroConsultaHistorico.setIdProdutoEdicao(null);
	}
	
	private void removerHistoricoDescontoCotaProdutoExcessao(
			FiltroConsultaHistoricoDescontoDTO filtroConsultaHistorico,
			Long idDesconto, Long idProduto, Long idProdutoEdicao){
		
		cleanFiltroConsultaHistoricoDescontoDTO(filtroConsultaHistorico);
				
		filtroConsultaHistorico.setIdDesconto(idDesconto);
		filtroConsultaHistorico.setIdProduto(idProduto);
		filtroConsultaHistorico.setIdProdutoEdicao(idProdutoEdicao);
		
		List<HistoricoDescontoCotaProdutoExcessao> listaHdcpe = historicoDescontoCotaProdutoRepository.buscarListaHistoricoDescontoCotaProdutoExcessao(filtroConsultaHistorico);
		
		if(listaHdcpe == null) {
			return;
		}
		
		for(HistoricoDescontoCotaProdutoExcessao h : listaHdcpe) {
			historicoDescontoCotaProdutoRepository.remover(h);
		}
		
	}
	
	/*
     * Excluir um desconto da cota e atualiza os descontos dos produtos edição.
     * @param idDesconto - identificador do desconto a ser removido
     */
	private void excluirDescontoProduto(Long idDesconto){

		Desconto desconto = descontoRepository.buscarPorId(idDesconto);

		List<Long> idsDescontoProximosLancamento = descontoRepository.buscarProximosLancamentosQueUsamDescontoProduto(desconto);
		
		if(idsDescontoProximosLancamento.isEmpty())
		    validarExclusaoDesconto(desconto);
		
		List<Long> idsProdutosEdicao = descontoRepository.buscarProdutosEdicoesQueUsamDescontoProduto(desconto);
		
		List<Long> idsProduto = descontoRepository.buscarProdutosQueUsamDescontoProduto(desconto);
		
		FiltroConsultaHistoricoDescontoDTO filtroConsultaHistorico = new FiltroConsultaHistoricoDescontoDTO();
		
		if(idsDescontoProximosLancamento!=null && !idsDescontoProximosLancamento.isEmpty()) {
            
            for(Long dpl : idsDescontoProximosLancamento) {
                
                DescontoProximosLancamentos descProximo = descontoProximosLancamentosRepository.buscarPorId(dpl);
                
                boolean utilizado = descProximo.getDesconto().isUsado();
                Integer qtdProximosOriginal =  descProximo.getQuantidadeProximosLancamentosOriginal();
                Integer qtdProximosAtual =  descProximo.getQuantidadeProximosLancamaentos();
                
                if(!utilizado)
                    descontoProximosLancamentosRepository.remover(descProximo);
                else {
                    descProximo.setQuantidadeProximosLancamentosOriginal(qtdProximosOriginal - qtdProximosAtual);
                    descProximo.setQuantidadeProximosLancamaentos(0);
                    descontoProximosLancamentosRepository.merge(descProximo);
                }
            }
            
        }
		
		if(!desconto.isUsado()) {
			
			for(Long idProdutoEdicao : idsProdutosEdicao) {

				Map<String, String> campos = new HashMap<>();
				campos.put("descontoProdutoEdicao", null);
				produtoEdicaoRepository.alterarPorId(idProdutoEdicao, campos);
				
				removerHistoricoDescontoCotaProdutoExcessao(filtroConsultaHistorico, desconto.getId(), null, idProdutoEdicao);
				
				HistoricoDescontoProdutoEdicao hdpe = 
						historicoDescontoProdutoEdicaoRepository.buscarHistoricoPorDescontoEProduto(desconto.getId(), idProdutoEdicao);

				if(hdpe!=null) {
					historicoDescontoProdutoEdicaoRepository.remover(hdpe);
				}
				
				DescontoCotaProdutoExcessao dcpe = 
					descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
							TipoDesconto.PRODUTO, desconto, null, null, null, null, idProdutoEdicao);
				
				if(dcpe!=null) {
					this.descontoProdutoEdicaoExcessaoRepository.remover(dcpe);
				}
				
			}
			
			for(Long idProduto : idsProduto) {

				Map<String, String> campos = new HashMap<>();
				campos.put("descontoProduto", null);
				produtoRepository.alterarPorId(idProduto, campos);

				
				removerHistoricoDescontoCotaProdutoExcessao(filtroConsultaHistorico, desconto.getId(), idProduto, null);
				
				HistoricoDescontoProduto hdp = historicoDescontoProdutoRepository.buscarHistoricoPorDescontoEProduto(desconto.getId(), idProduto);
				
				if(hdp!=null) {
					historicoDescontoProdutoRepository.remover(hdp);
				}
				
				DescontoCotaProdutoExcessao dcpe = 
						descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
								TipoDesconto.PRODUTO, desconto, null, null, null, idProduto, null);
				if(dcpe!=null) {
					this.descontoProdutoEdicaoExcessaoRepository.remover(dcpe);
				}
				

			}
			
			Set<DescontoCotaProdutoExcessao> dcpe = 
				this.descontoProdutoEdicaoExcessaoRepository.obterDescontoProdutoEdicaoExcessao(
					null, desconto, null, null, null);
			
			if(dcpe!=null && !dcpe.isEmpty()) {

				for (DescontoCotaProdutoExcessao d : dcpe){
					this.descontoProdutoEdicaoExcessaoRepository.remover(d);
				}
				
			}
			
			removerHistoricoDescontoCotaProdutoExcessao(filtroConsultaHistorico, desconto.getId(), null, null);
			
			descontoRepository.removerPorId(idDesconto);
			
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
    public Desconto obterDescontoPorCotaProdutoEdicao(Lancamento lancamento,
            										  Long idCota, 
            										  ProdutoEdicao produtoEdicao) {
		
        Validate.notNull(idCota, "Cota não deve ser nula!");
        Validate.notNull(produtoEdicao, "Edição do produto não deve ser nula!");
        
        Desconto desconto = null;
    
        desconto = descontoProdutoEdicaoRepository.obterDescontoPorCotaProdutoEdicao(lancamento, idCota, produtoEdicao);

        if (desconto == null) {
        
        	String produtoEdicaoSemDesconto = "";
            
            if (produtoEdicao != null && produtoEdicao.getProduto() != null) {
            
            	produtoEdicaoSemDesconto = produtoEdicao.getProduto().getNome();
            }
            
            if (produtoEdicao != null) {
            	
            	produtoEdicaoSemDesconto += " / Edição: " + produtoEdicao.getNumeroEdicao();
            }
        	
        	ValidacaoVO validacaoVO = 
                new ValidacaoVO(
                	TipoMensagem.ERROR, 
                "Não existe desconto cadastrado para o Produto: "
                			+ produtoEdicaoSemDesconto);
        	
        	throw new ValidacaoException(validacaoVO);
        }
        
        desconto.setUsado(true);
        
        descontoRepository.alterar(desconto);
          
        return desconto;
    }
	
	@Override
	@Transactional
	public BigDecimal obterValorDescontoPorCotaProdutoEdicao(Lancamento lancamento, Long idCota, ProdutoEdicao produtoEdicao) {

        Validate.notNull(idCota, "Cota não deve ser nula!");
        Validate.notNull(produtoEdicao, "Edição do produto não deve ser nula!");
        Desconto desconto = null;
        if (produtoEdicao.getProduto().isPublicacao()) {
        	
            //Neste caso, o produto possui apenas um fornecedor
            // Recuperar o desconto utilizando a cota, o produto edição e o fornecedor
            desconto = descontoProdutoEdicaoRepository.obterDescontoPorCotaProdutoEdicao(lancamento, idCota, produtoEdicao);
            
        } else {
        	
            //Produto possivelmente com mais de um fornecedor, seguindo
            // a instrução passada, utilizar o desconto do produto
        	desconto = new Desconto();
            desconto.setValor(produtoEdicao.getProduto().getDesconto());
            
        }
        
        return (desconto != null && desconto.getValor() != null) ? desconto.getValor() : BigDecimal.ZERO;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterComissaoCota(Integer numeroCota){
		
		DescontoCotaProdutoExcessao desconto = 
				this.descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaProdutoExcessao(
				TipoDesconto.ESPECIFICO, null, null, null , this.cotaRepository.obterPorNumeroDaCota(numeroCota), null, null);
		
		if (desconto != null){
			
			return desconto.getDesconto().getValor();
		} else {
			
			return this.descontoRepository.obterMediaDescontosFornecedoresCota(numeroCota);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterComissaoParametroDistribuidor(){

		return this.distribuidorRepository.obterDescontoCotaNegociacao();
	}
	@Override
	@Transactional(readOnly = true)
	public List<TipoDescontoDTO> obterMergeDescontosEspecificosEGerais(
			Cota cota, String sortorder, String sortname) {		
		return descontoRepository.obterMergeDescontosEspecificosEGerais(cota, sortorder, sortname);
	}

	@Override
    @Transactional
    public Map<String, DescontoDTO> obterDescontosMapPorLancamentoProdutoEdicao() {
	    return obterDescontosMapPorLancamentoProdutoEdicao(distribuidorRepository.obterDataOperacaoDistribuidor());
	}
	
	@Override
	@Transactional
	public Map<String, DescontoDTO> obterDescontosMapPorLancamentoProdutoEdicao(Date data) {
		
		Map<String, DescontoDTO> descontosMap = new HashMap<String, DescontoDTO>();
		List<DescontoDTO> descontos = descontoProdutoEdicaoRepository.obterDescontosProdutoEdicao();
		
		List<DescontoDTO> descontosProximosLancamentos = descontoProximosLancamentosRepository.obterDescontosProximosLancamentos(data);
		
		for(DescontoDTO desc : descontos) {
			String key = new StringBuilder()
				.append(desc.getCotaId() != null ? "c" : "")
				.append(desc.getCotaId() != null ? desc.getCotaId() : "")
				.append(desc.getEditorId() != null ? "e" : "")
				.append(desc.getEditorId() != null ? desc.getEditorId() : "")
				.append(desc.getFornecedorId() != null ? "f" : "")
				.append(desc.getFornecedorId() != null ? desc.getFornecedorId() : "")
				.append(desc.getProdutoEdicaoId() != null ? "pe" : "")
				.append(desc.getProdutoEdicaoId() != null ? desc.getProdutoEdicaoId() : "")
				.append(desc.getProdutoId() != null ? "p" : "")
				.append(desc.getProdutoId() != null ? desc.getProdutoId() : "")
				.toString();
			descontosMap.put(key, desc);
		}	
		
		for(DescontoDTO desc : descontosProximosLancamentos) {
			String key = new StringBuilder()
				.append(desc.getCotaId() != null ? "c" : "")
				.append(desc.getCotaId() != null ? desc.getCotaId() : "")
				.append(desc.getProdutoId() != null ? "p" : "")
				.append(desc.getProdutoId() != null ? desc.getProdutoId() : "")
				.append(desc.isProximoLancamento() ? "pl" : "")
				.toString();
			descontosMap.put(key, desc);
		}
		
		return descontosMap;
	}
	
	public DescontoDTO obterDescontoPor(Map<String, DescontoDTO> descontos, Long cotaId, Long fornecedorId, Long editorId, Long produtoId, Long produtoEdicaoId) throws Exception {
		
		/**
         * A busca dos descontos é feita diretamente no Map, por chave, agilizando o retorno do resultado
         * 
         * Para os itens abaixo prevalece a ordem de prioridade
         * 
         * Prioridade 	| Produto 	| ProdutoEdicao | QuantidadeEdicoes | Cota Especifica 
         * 1 			| X 		| X 			| X 				| X 
         * 2 			| X 		| X 			| 					| X
         * 3 			| X 		| 				| 					| X 
         * 4 			| X 		| X				| X 				| 
         * 5 			| X 		| X 			| 					| 
         * 6 			| X 		| 				| 					|
         * 
         * LEGENDA: c : Cota f : Fornecedor pe: ProdutoEdicao p : Produto pl: Próximo Lançamento
         * 
         */
		
		DescontoDTO descontoDTO = null;
		
		/**
		 * Desconto de ProdutoEdicao no proximo lancamento com cota especifica
		 */		
		StringBuilder key = new StringBuilder()
					.append(cotaId != null ? "c" : "")
					.append(cotaId != null ? cotaId : "")
					.append(produtoId != null ? "p" : "")
					.append(produtoId != null ? produtoId : "")
					.append("pl");
		
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
         * Desconto de ProdutoEdicao no proximo lancamento para todas as cotas
         */     
        key = new StringBuilder()
                    .append(produtoId != null ? "p" : "")
                    .append(produtoId != null ? produtoId : "")
                    .append("pl");
        
        descontoDTO = descontos.get(key.toString());
        
        if(descontoDTO != null) {
            return descontoDTO;
        }
		
		
		
		/**
         * Desconto de ProdutoEdicao para cota específica
         */
		key = new StringBuilder()
			.append("c")
			.append(cotaId)		
			.append("f")
            .append(fornecedorId)
		    .append("pe")
			.append(produtoEdicaoId)
			.append("p")
			.append(produtoId);
		
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
				
		/**
         * Desconto de Produto para cota específica
         */
		key = new StringBuilder()
			.append("c")
			.append(cotaId)
			.append("p")
			.append(produtoId);
	
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto de ProdutoEdicao
		 */
		key = new StringBuilder()
			.append("pe")
            .append(produtoEdicaoId)
            .append("p")
            .append(produtoId);
		
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto de Produto
		 */
		key = new StringBuilder()
			.append("p")
			.append(produtoId);
	
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto Especifico da Cota
		 */
		key = new StringBuilder()
			.append("c")
			.append(cotaId)
			.append("f")
			.append(fornecedorId);
	
		
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto Especifico da Cota
		 */
		key = new StringBuilder()
			.append("c")
			.append(cotaId)
			.append("e")
			.append(editorId);
		
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto Geral (Fornecedor)
		 */
		key = new StringBuilder()
			.append("e")
			.append(editorId);
	
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
		
		/**
		 * Desconto Geral (Fornecedor)
		 */
		key = new StringBuilder()
			.append("f")
			.append(fornecedorId);
	
		descontoDTO = descontos.get(key.toString());
		
		if(descontoDTO != null) {
			return descontoDTO;
		}
				
		return descontoDTO;
	}
	
	@Override
	@Transactional
	public DescontoDTO obterDescontoPor(Integer numeroCota, String codigoProduto, Long numeroEdicao) throws Exception {
	    
	    ProdutoEdicao pe = null;
	    Cota c = null;
	    Produto p = null; 
        
	    if(codigoProduto != null)
            p = produtoRepository.obterProdutoPorCodigoProdin(codigoProduto);
        	    
	    if(codigoProduto != null && numeroEdicao != null)
	        pe = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
	    	    
	    if(numeroCota != null) 
	        c = cotaRepository.obterPorNumerDaCota(numeroCota);
	    
	    Map<String, DescontoDTO> descontos = this.obterDescontosMapPorLancamentoProdutoEdicao();
	    
	    return  obterDescontoPor(descontos, 
	            c  == null ? null : c.getId(), 
	            pe == null ? null : p.getFornecedor().getId(), 
	            null, 
	            p  == null ? null : p.getId(), pe == null ? null : pe.getId());
	}

	@Override
	public DescontoDTO obterDescontoProximosLancamentosPor(
			Map<String, DescontoDTO> descontos, 
				Long cotaId, Long fornecedorId, Long produtoEdicaoId, Long produtoId) {
	
		DescontoDTO descontoDTO = null;
        
        /**
         * Desconto de ProdutoEdicao no proximo lancamento com cota especifica
         */     
        StringBuilder key = new StringBuilder()
                    .append(cotaId != null ? "c" : "")
                    .append(cotaId != null ? cotaId : "")
                    .append(produtoId != null ? "p" : "")
                    .append(produtoId != null ? produtoId : "")
                    .append("pl");
        
        descontoDTO = descontos.get(key.toString());
        
        if(descontoDTO != null) {
            return descontoDTO;
        }
        
        return descontoDTO;
	}
	
	@Override
    public DescontoDTO obterDescontoProximosLancamentosPorDeTodasCotas(
            Map<String, DescontoDTO> descontos, Long fornecedorId, Long produtoEdicaoId, Long produtoId) {
    
        DescontoDTO descontoDTO = null;
        
        /**
         * Desconto de ProdutoEdicao no proximo lancamento para todas as cotas
         */     
        StringBuilder key = new StringBuilder()
                    .append(produtoId != null ? "p" : "")
                    .append(produtoId != null ? produtoId : "")
                    .append("pl");
        
        descontoDTO = descontos.get(key.toString());
        
        if(descontoDTO != null) {
            return descontoDTO;
        }
        
        return descontoDTO;
    }

	@Override
	@Transactional
	public List<TipoDescontoEditorDTO> buscarTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro) {
		
		List<TipoDescontoEditorDTO> historicoDesconto = descontoProdutoEdicaoExcessaoRepository.buscarTipoDescontoEditor(filtro);
		
		return historicoDesconto;
		
	}

	@Override
	@Transactional
	public Integer buscarQuantidadeTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro) {
		
		return descontoProdutoEdicaoExcessaoRepository.buscarQuantidadeTipoDescontoEditor(filtro);
		
	}

	@Override
	@Transactional
	public void incluirDescontoEditor(DescontoEditorDTO descontoDTO, List<Long> cotas, Usuario usuario) {
		
		validarDescontoEditor(descontoDTO);

		Date dataAtual = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		/*
		 * Cria um desconto a ser utilizado em um ou mais fornecedores
		 */
		Desconto desconto =  new Desconto();
		desconto.setDataAlteracao(dataAtual);
		desconto.setUsado(false);
		desconto.setUsuario(usuario);
		desconto.setValor(descontoDTO.getValorDesconto());
		desconto.setTipoDesconto(TipoDesconto.EDITOR);
		
		descontoRepository.adicionar(desconto);
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();

		Editor editor = editorRepository.obterPorCodigo(descontoDTO.getCodigoEditor());

		if(descontoDTO.getHasCotaEspecifica() != null && descontoDTO.getHasCotaEspecifica()) {
			
			/*
			 * Se existir o desconto, a mesma é atualizada, senão, cria-se uma nova entrada na tabela
			 */
			for(Integer numeroCota : descontoDTO.getCotas()) {
				
				Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota.intValue());
				
				DescontoCotaProdutoExcessao dcpe = 
						descontoProdutoEdicaoExcessaoRepository.buscarDescontoCotaEditorExcessao(TipoDesconto.EDITOR, null, cota, editor);
				
				if(dcpe != null) {
					
					dcpe.setDesconto(desconto);
				} else {
					
					dcpe = new DescontoCotaProdutoExcessao();
					dcpe.setDistribuidor(distribuidor);
					dcpe.setCota(cota);
					dcpe.setEditor(editor);
					dcpe.setUsuario(usuario);
					dcpe.setDesconto(desconto);
				}
				
				dcpe.setTipoDesconto(TipoDesconto.EDITOR);
				descontoProdutoEdicaoExcessaoRepository.merge(dcpe);	
				
				HistoricoDescontoCotaProdutoExcessao hdcp = new HistoricoDescontoCotaProdutoExcessao();
				hdcp.setDataAlteracao(new Date());
				hdcp.setDesconto(desconto);
				hdcp.setDistribuidor(distribuidor);
				hdcp.setCota(cota);
				hdcp.setEditor(editor);
				hdcp.setUsuario(usuario);
				hdcp.setValor(desconto.getValor());
				
				historicoDescontoCotaProdutoRepository.merge(hdcp);
			}
			
		} else {
			
				
			/*
			 * Atualiza o historico do desconto para o editor
			 */
			HistoricoDescontoEditor historicoDesconto = new HistoricoDescontoEditor();
			historicoDesconto.setDesconto(desconto);
			historicoDesconto.setUsuario(usuario);
			historicoDesconto.setDataAlteracao(new Date());
			historicoDesconto.setEditor(editor);
			historicoDesconto.setDistribuidor(distribuidor);
			
			historicoDescontoEditorRepository.adicionar(historicoDesconto);
			
			editor.setDesconto(desconto);
			editorRepository.merge(editor);
		}
		
	}
	
	/*
     * Valida a entrada de dados para inclusão de desconto por produto.
     * @param desconto - dados do desconto de produto
     */
	private void validarDescontoEditor(DescontoEditorDTO desconto) {

		List<String> mensagens = new ArrayList<String>();

		if (desconto.getCodigoEditor() == null || desconto.getCodigoEditor().equals("")) {
			
            mensagens.add("O campo Código deve ser preenchido!");
		}

		if (desconto.getValorDesconto() == null || desconto.getValorDesconto().intValue() <= 0) {

			mensagens.add("O campo Desconto deve ser preenchido!");
		}

		if (!desconto.getHasCotaEspecifica() && !desconto.getIsTodasCotas()) {

			mensagens.add("O campo de cotas deve ser preenchido!");
		}

		if (desconto.getHasCotaEspecifica() && desconto.getCotas() == null) {

			mensagens.add("Ao menos uma cota deve ser selecionada!");
		}

		if (!mensagens.isEmpty()) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}

}