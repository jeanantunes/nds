package br.com.abril.nds.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.ProdutoEdicao}
 * 
 * @author Discover Technology
 */
@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private CapaService capaService;
	
	
	@Override
	@Transactional(readOnly = true)
 	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		return produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto(nomeProduto);
	}

	@Override
	@Transactional(readOnly = true)
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()){
			mensagensValidacao.add("Código é obrigatório.");
		}
		
		if (edicao == null){
			mensagensValidacao.add("Edição é obrigatório.");
		}
		
		if (dataLancamento == null){
			mensagensValidacao.add("Data Lançamento é obrigatório.");
		}
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
		
		FuroProdutoDTO furoProdutoDTO = produtoEdicaoRepository.
				obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
						codigo, nomeProduto, edicao, dataLancamento);
		
		if (furoProdutoDTO != null){
			//buscar path de imagens
			ParametroSistema parametroSistema = 
					this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_CAPA);
			
			if (parametroSistema != null){
				furoProdutoDTO.setPathImagem(parametroSistema.getValor() + furoProdutoDTO.getPathImagem());
			}
			
			//buscar proxima data para lançamento
			
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).parse(furoProdutoDTO.getNovaData()));
			} catch (ParseException e) {
				return furoProdutoDTO;
			}
			
			List<Integer> listaDiasSemana = 
					this.distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao(
							furoProdutoDTO.getCodigoProduto(), 
							furoProdutoDTO.getIdProdutoEdicao());
			
			if (listaDiasSemana != null && !listaDiasSemana.isEmpty()){
				int diaSemana = -1;
				for (Integer dia : listaDiasSemana){
					if (dia > calendar.get(Calendar.DAY_OF_WEEK)){
						diaSemana = dia;
						break;
					}
				}
				
				if (diaSemana == -1){
					diaSemana = listaDiasSemana.get(0);
				}
				
				while (calendar.get(Calendar.DAY_OF_WEEK) != diaSemana){
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				furoProdutoDTO.setNovaData(
						new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).format(calendar.getTime()));
			}
		}
		
		return furoProdutoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProdutoEdicao obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao) {

		List<String> mensagensValidacao = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()) {
			
			mensagensValidacao.add("Código é obrigatório.");
		}
		
		if (numeroEdicao == null || numeroEdicao.isEmpty()) {
			
			mensagensValidacao.add("Número edição é obrigatório.");
		}

		if (!Util.isLong(numeroEdicao)) {

			mensagensValidacao.add("Número edição é inválido.");
		}
		
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
		
		return produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				codigoProduto, Long.parseLong(numeroEdicao));
	}

	@Override
	@Transactional
	public List<ProdutoEdicao> obterProdutosEdicaoPorCodigoProduto(
			String codigoProduto) {
		return produtoEdicaoRepository.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
	}

	@Override
	@Transactional
	public void alterarProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicaoRepository.alterar(produtoEdicao);		
	}
	
	@Transactional(readOnly = true)
	public List<ProdutoEdicao> obterProdutoPorCodigoNome(String codigoNomeProduto) {
		
		if (codigoNomeProduto == null || codigoNomeProduto.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Codigo/nome produto é obrigatório.");
		}
		
		return this.produtoEdicaoRepository.obterProdutoPorCodigoNome(codigoNomeProduto);
	}

	/**
	 * Pesquisa as Edições já cadastradas.<br>
	 * Possui como opções de filtro:<br>
	 * <ul>
	 * <li>Código do Produto;</li>
	 * <li>Nome do Produto;</li>
	 * <li>Data de Lançamento;</li>
	 * <li>Situação do Lançamento;</li>
	 * <li>Código de Barra da Edição;</li>
	 * <li>Contém brinde;</li>
	 * </ul>
	 * 
	 * @param dto
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param maxResults
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarEdicoes(ProdutoEdicaoDTO dto,
			String sortorder, String sortname, int page, int maxResults) {
		
		final int initialResult = ((page * maxResults) - maxResults);
		return this.produtoEdicaoRepository.pesquisarEdicoes(dto, sortorder,
				sortname, initialResult, maxResults);
	}
	
	/**
	 * Obtém a quantidade de edições cadastradas filtradas pelos critérios 
	 * escolhidos pelo usuário.
	 * 
	 * @param dto
	 * @return
	 */
	@Transactional(readOnly = true)
	public Long countPesquisarEdicoes(ProdutoEdicaoDTO dto) {
		
		return this.produtoEdicaoRepository.countPesquisarEdicoes(dto);
	}
	
	/**
	 * Pesquisa as últimas edições cadastradas.<br>
	 * 
	 * @param dto
	 * @param maxResults Quantidade das últimas edições cadastradas a ser exibidas.
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoDTO> pesquisarUltimasEdicoes(ProdutoEdicaoDTO dto,
			int maxResults) {
		
		return this.produtoEdicaoRepository.pesquisarEdicoes(dto, "DESC",
				"codigoProduto", 0, 5);
	}
	
	@Transactional
	public void salvarProdutoEdicao(ProdutoEdicaoDTO dto, String codigoProduto, String contentType, InputStream imgInputStream) {
		
		ProdutoEdicao produtoEdicao = null;
		Lancamento lancamento = null;
		if (dto.getId() == null) {
			
			// Salvar novo ProdutoEdicao:
			produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setProduto(produtoRepository.obterProdutoPorCodigo(codigoProduto));
			lancamento = new Lancamento();
		} else {
			
			// Atualizar ProdutoEdicao existente:
			produtoEdicao = produtoEdicaoRepository.buscarPorId(dto.getId());
			lancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(
					produtoEdicao.getId());
			
			/* 
			 * Regrao: Só é permitido alterar o número da edição se a 
			 * "Data de Lançamento do Distribuidor" for depois que a 
			 * "Data 'de Hoje'".
			 */
			if (!produtoEdicao.getNumeroEdicao().equals(dto.getNumeroEdicao())) {
				if (!produtoEdicaoRepository.isProdutoEdicaoJaPublicada(produtoEdicao.getId())) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Não é permitido alterar o número de uma Edição já publicada!");
				}
			}
		}		
		
		
		/* Regra: Se não existir nenhuma edição associada ao produto, salvar n. 1 */
		if (!this.produtoEdicaoRepository.hasProdutoEdicao(produtoEdicao.getProduto())) {
			produtoEdicao.setNumeroEdicao(Long.valueOf(1));
		}
		
		// TODO: Popular ProdutoEdicao:
		// TODO: implementar atributos:
		produtoEdicao.setCodigo(dto.getCodigoProduto());	// View: Codigo da Edição;
		produtoEdicao.setNomeComercial(dto.getNomeComercialProduto());
		produtoEdicao.setNumeroEdicao(dto.getNumeroEdicao());
		produtoEdicao.setPacotePadrao(dto.getPacotePadrao());
		produtoEdicao.setPrecoPrevisto(dto.getPrecoPrevisto());
		produtoEdicao.setPrecoVenda(dto.getPrecoVenda());	// View: Preço real;
		
		BigDecimal repartePrevisto = dto.getRepartePrevisto();
		BigDecimal repartePromocional = dto.getRepartePromocional();
		produtoEdicao.setReparteDistribuido(repartePrevisto.add(repartePromocional));
		
		produtoEdicao.setCodigoDeBarras(dto.getCodigoDeBarras());
		produtoEdicao.setCodigoDeBarraCorporativo(dto.getCodigoDeBarrasCorporativo());
		produtoEdicao.setDesconto(dto.getDesconto());
		produtoEdicao.setChamadaCapa(dto.getChamadaCapa());
		produtoEdicao.setParcial(dto.isParcial());
		produtoEdicao.setPossuiBrinde(dto.isPossuiBrinde());
		
		Dimensao dimEdicao = new Dimensao();
		dimEdicao.setLargura(dto.getLargura());
		dimEdicao.setComprimento(dto.getComprimento());
		dimEdicao.setEspessura(dto.getEspessura());
		produtoEdicao.setDimensao(dimEdicao);
		produtoEdicao.setPeso(dto.getPeso());
		
		Dimensao d = new Dimensao();
		d.setLargura(dto.getLargura());
		d.setComprimento(dto.getComprimento());
		d.setEspessura(dto.getEspessura());
		
		if (produtoEdicao.getId() == null) {
			
			// save
			produtoEdicaoRepository.adicionar(produtoEdicao);
			
			// Salvar na tabela de lançamento: 
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setTipoLancamento(dto.getTipoLancamento());
			lancamento.setDataLancamentoPrevista(dto.getDataLancamentoPrevisto());
			lancamento.setDataLancamentoDistribuidor(dto.getDataLancamento());	// Data Lançamento Real;
			lancamento.setReparte(repartePrevisto);
			lancamento.setRepartePromocional(repartePromocional);
			
		} else {
			// update
			
			// TODO: Regra: Edição - permitir alteração do código de edição se o status não for LANÇADO;
				dto.getCodigoProduto();
				
			
			
			
			// TODO: No final, salvar na tabela de lançamento tb 
			
		}		
		
		
		// Salvar imagem:
		if (imgInputStream != null) {
			capaService.saveCapa(produtoEdicao.getId(), contentType, imgInputStream);
		}
	}
	
	/**
	 * Exclui uma Edição da base de dados.<br>
	 * Os critérios para exclusão são:
	 * <ul>
	 * <li>A Edição não pode ser cadastrado via INTERFACE;</li>
	 * <li>A Edição não pode estar sendo utilizada em outras partes dos sitema;</li>
	 * </ul>
	 * 
	 * @param idProdutoEdicao
	 */
	@Transactional(readOnly = true)
	public void excluirProdutoEdicao(Long idProdutoEdicao) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Por favor, selecione uma Edição existente!");
		}
		
		/* Regra: Se a Edição for originária da Interface, não pode ser excluida! */
		if (produtoEdicao.getOrigemInterface()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por ser originária da INTERFACE!");
		}
		
		// TODO: 
		/* Regra: Não excluir se existir referencias desta edição em outras tabelas. */
		//throw new ValidacaoException(TipoMensagem.ERROR, "Esta Edição não pode ser excluida por estar associada em outras partes do sistema!");
		
		
		produtoEdicaoRepository.remover(produtoEdicao);
	}
	
	
	/**
	 * Transforma o valor em formato String para BigDecimal.<br>
	 * Caso o valor não for válido, irá retornar 0 (zero).
	 * 
	 * @param valor
	 * @return
	 */
	private BigDecimal converterValor(String valor) {
		
		BigDecimal nValor = null;
		try {
			nValor = (new BigDecimal(valor)).setScale(2);
		} catch (Exception e) {
			nValor = BigDecimal.ZERO.setScale(2);
		}
		
		return nValor;
	}
	
}
