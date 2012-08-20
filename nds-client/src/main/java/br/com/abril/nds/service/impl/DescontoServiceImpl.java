package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.component.DescontoComponent;
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
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

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

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DescontoComponent descontoComponent;
	
	@Override
	@Transactional(readOnly=true)
	public List<br.com.abril.nds.model.cadastro.TipoDesconto> obterTodosTiposDescontos() {
		
		return this.tipoDescontoRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public br.com.abril.nds.model.cadastro.TipoDesconto obterTipoDescontoPorID(Long id) {

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
				
				DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(desconto.getDataAlteracao());
				desconto.setFornecedores(null);
				descontoDistribuidorRepository.remover(desconto);
				
				//TODO obter desconto anterior, tratar dados para o processamento
				
				//TODO chamar metodo para processamrnto de desconto distribuidor
				
				break;
			case ESPECIFICO:
				
				DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoCota.getDataAlteracao());
				descontoCota.setFornecedores(null);
				descontoCotaRepository.remover(descontoCota);
				
				//TODO obter desconto anterior, tratar dados para o processamento
				
				//TODO chamar metodo para processamrnto de desconto cota
				
				break;
			case PRODUTO:
		
				DescontoProduto descontoProduto = descontoProdutoRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoProduto.getDataAlteracao());
				descontoProduto.setCotas(null);
				descontoProdutoRepository.remover(descontoProduto);
				
				//TODO obter desconto anterior, tratar dados para o processamento
				
				//TODO chamar metodo para processamento de desconto produto edição
				
				break;
				
			default:
				throw new RuntimeException("Tipo de Desconto inválido!");
			}
	}
	
	@Override
	@Transactional
	public void incluirDescontoDistribuidor(BigDecimal valorDesconto, List<Long> fornecedores,Usuario usuario) {
		
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
		
		processarDescontoDistribuidor(desconto.getFornecedores(), valorDesconto);
	}
	
	@Override
	@Transactional
	public void incluirDescontoCota(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario) {
		
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
		
		processarDescontoCota(descontoCota.getCota(), descontoCota.getFornecedores(), valorDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void incluirDescontoProduto(DescontoProdutoDTO desconto, Usuario usuario) {

		validarEntradaDeDadosInclusaoDescontoPorProduto(desconto);

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		Set<Cota> cotas = obterCotas(desconto.getCotas(), desconto.isTodasCotas());

		Set<ProdutoEdicao>produtosEdicao = new HashSet<ProdutoEdicao>();
		
		if (desconto.getEdicaoProduto() != null) {

			ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				desconto.getCodigoProduto(), desconto.getEdicaoProduto()
			);

			DescontoProduto descontoProduto = new DescontoProduto();

			descontoProduto.setDesconto(desconto.getDescontoProduto());
			descontoProduto.setDataAlteracao(new Date());
			descontoProduto.setCotas(cotas);
			descontoProduto.setDistribuidor(distribuidor);
			descontoProduto.setProdutoEdicao(produtoEdicao);
			descontoProduto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));

			this.descontoProdutoRepository.adicionar(descontoProduto);
			
			produtosEdicao.add(produtoEdicao);

		} else if (desconto.getQuantidadeEdicoes() != null) {

			List<ProdutoEdicao> listaProdutoEdicao = 
				this.produtoEdicaoRepository.obterProdutosEdicoesPorCodigoProdutoLimitado(
					desconto.getCodigoProduto(), desconto.getQuantidadeEdicoes()
				);

			for (ProdutoEdicao produtoEdicao : listaProdutoEdicao) {

				DescontoProduto descontoProduto = new DescontoProduto();

				descontoProduto.setDesconto(desconto.getDescontoProduto());
				descontoProduto.setDataAlteracao(new Date());
				descontoProduto.setCotas(cotas);
				descontoProduto.setDistribuidor(distribuidor);
				descontoProduto.setProdutoEdicao(produtoEdicao);
				descontoProduto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));

				this.descontoProdutoRepository.adicionar(descontoProduto);
			}
			
			produtosEdicao.addAll(listaProdutoEdicao);
		}
		
		processarDescontoProduto(produtosEdicao, cotas, desconto.getDescontoProduto());
	}

	@Override
	@Transactional(readOnly=true)
	public List<Fornecedor> busacarFornecedoresAssociadosADesconto(Long idDesconto,TipoDesconto tipoDesconto) {
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		
		switch (tipoDesconto) {
		case GERAL :
			
			DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarPorId(idDesconto);
			
			if(desconto!= null){
				listaFornecedores.addAll(desconto.getFornecedores());
			}
			break;
			
		case ESPECIFICO :
			
			DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);
			
			if(descontoCota!= null){
				listaFornecedores.addAll(descontoCota.getFornecedores());
			}
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
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota) {

		if (idCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota] precisa ser especificada.");
		}
		
		return this.descontoProdutoRepository.obterTiposDescontoProdutoPorCota(idCota);
	}
	
	
	@Override
	@Transactional
	public void processarDescontoDistribuidor(Set<Fornecedor> fornecedores, BigDecimal valorDesconto) {
		
		this.processarDesconto(TipoDesconto.GERAL, fornecedores, null, null, valorDesconto);
	}
	

	@Override
	@Transactional
	public void processarDescontoDistribuidor(BigDecimal valorDesconto) {
		
		this.processarDescontoDistribuidor(null,valorDesconto);
	}
	
	
	@Override
	@Transactional
	public void processarDescontoCota(Cota cota, BigDecimal valorDesconto) {
		
		this.processarDescontoCota(cota, null, valorDesconto);
	}
	
	@Override
	@Transactional
	public void processarDescontoCota(Cota cota, Set<Fornecedor> fornecedores, BigDecimal valorDesconto) {
		
		Set<Cota> cotas = new HashSet<Cota>();
		
		cotas.add(cota);
		
		this.processarDesconto(TipoDesconto.ESPECIFICO, fornecedores, cotas , null, valorDesconto);
	}
	
	@Override
	@Transactional
	public void processarDescontoProduto(ProdutoEdicao produto, BigDecimal valorDesconto) {
		
		Set<ProdutoEdicao> produtos = new HashSet<ProdutoEdicao>();
		
		produtos.add(produto);
		
		this.processarDescontoProduto(produtos, null, valorDesconto); 
	}
	
	@Override
	@Transactional
	public void processarDescontoProduto(Set<ProdutoEdicao> produtos, 
										 Set<Cota> cotas, 
										 BigDecimal valorDesconto) {
		
		this.processarDesconto(TipoDesconto.PRODUTO, null, cotas, produtos, valorDesconto);
	}
	
	/*
	 * Método que efetua o processamento do desconto de acordo com seu tipo.
	 * 
	 * @param tipoDesconto - tipo de desconto
	 * @param fornecedores - lista de fornecedores
	 * @param cotas - list de cotas
	 * @param produtos - lista de produtos
	 * @param valorDesconto - valor do desconto
	 */
	private void processarDesconto(TipoDesconto tipoDesconto, 
								   Set<Fornecedor> fornecedores, 
								   Set<Cota> cotas, 
								   Set<ProdutoEdicao> produtos, 
								   BigDecimal valorDesconto) {

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
					tipoDesconto, fornecedor, cota, produtosParaInclusao, valorDesconto);
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
		
		if (desconto.getEdicaoProduto() == null && desconto.getQuantidadeEdicoes() == null) {
			
			mensagens.add("O campo Edição específica ou Edições deve ser preenchido!");
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
	
}
