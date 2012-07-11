package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.util.BigDecimalUtil;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoAdicional;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.TributacaoService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.fiscal.nota.NFEExporter;

import static br.com.abril.nds.client.util.BigDecimalUtil.soma;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.nota.NotaFiscal}
 * 
 * @author Discover Technology
 * 
 */
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {

	private static final Date dataVigencia = null;

	@Autowired
	private NotaFiscalRepository notaFiscalDAO;

	@Autowired
	private ParametroSistemaService parametroSistemaService;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private TributacaoService tributacaoService;
	
	@Autowired
	private SerieRepository serieRepository;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Override
	public Map<Long, Integer> obterTotalItensNotaFiscalPorCotaEmLote(ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		
		Intervalo<Date> periodo = dadosConsultaLoteNotaFiscal.getPeriodoMovimento();
		
		GrupoNotaFiscal grupoNotaFiscal = dadosConsultaLoteNotaFiscal.getTipoNotaFiscal().getGrupoNotaFiscal();
		
		List<Long> listaIdFornecedores = dadosConsultaLoteNotaFiscal.getListaIdFornecedores();
		 
		List<Long> listaIdProdutos = dadosConsultaLoteNotaFiscal.getListaIdProdutos();
		
		Map<Long, Integer> idCotaTotalItensNota = new HashMap<Long, Integer>();
		
		for (Long idCota : dadosConsultaLoteNotaFiscal.getIdsCotasDestinatarias()) {
			
			List<ItemNotaFiscal> itensNotaFiscal = null;
			
			switch (grupoNotaFiscal) {

			case NF_REMESSA_CONSIGNACAO:
				itensNotaFiscal = this.obterItensNFeRemessaEmConsignacao(idCota, periodo, listaIdFornecedores, listaIdProdutos);
				break;

			case NF_DEVOLUCAO_REMESSA_CONSIGNACAO:
				itensNotaFiscal = this.obterItensNFeEntradaDevolucaoRemessaConsignacao(idCota, periodo, listaIdFornecedores, listaIdProdutos);
				break;

			case NF_DEVOLUCAO_SIMBOLICA:
				itensNotaFiscal = this.obterItensNFeVenda(idCota, periodo, listaIdFornecedores, listaIdProdutos);
				break;

			case NF_VENDA:
				itensNotaFiscal = this.obterItensNFeVenda(idCota, periodo, listaIdFornecedores, listaIdProdutos);
				break;
			}
			
			if (itensNotaFiscal != null && itensNotaFiscal.size() > 0) {
				idCotaTotalItensNota.put(idCota, itensNotaFiscal.size());
			}
		}
		
		return idCotaTotalItensNota;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal
	 * (br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public List<RetornoNFEDTO> processarRetornoNotaFiscal(
			List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			NotaFiscal notaFiscal = this.notaFiscalDAO
					.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

			if (notaFiscal != null) {

				IdentificacaoEmitente emitente = notaFiscal
						.getIdentificacaoEmitente();

				String cpfCnpjEmitente = emitente.getPessoaEmitenteReferencia()
						.getDocumento();

				InformacaoEletronica informacaoEletronica = notaFiscal
						.getInformacaoEletronica();

				if (cpfCnpjEmitente.equals(dadosRetornoNFE.getCpfCnpj())) {

					if (StatusProcessamentoInterno.ENVIADA.equals(notaFiscal
							.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(dadosRetornoNFE
								.getStatus())
								|| Status.USO_DENEGADO.equals(dadosRetornoNFE
										.getStatus())) {

							listaDadosRetornoNFEProcessados
									.add(dadosRetornoNFE);
						}

					} else if (StatusProcessamentoInterno.RETORNADA
							.equals(notaFiscal.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(informacaoEletronica
								.getRetornoComunicacaoEletronica().getStatus())
								&& Status.CANCELAMENTO_HOMOLOGADO
										.equals(dadosRetornoNFE.getStatus())) {

							listaDadosRetornoNFEProcessados
									.add(dadosRetornoNFE);
						}
					}
				}
			}
		}

		return listaDadosRetornoNFEProcessados;
	}

	@Override
	public void cancelarNotaFiscal(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void denegarNotaFiscal(Long id) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#autorizarNotaFiscal(br.com
	 * .abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public void autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {

		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(dadosRetornoNFE
				.getIdNotaFiscal());

		InformacaoEletronica informacaoEletronica = notaFiscal
				.getInformacaoEletronica();

		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE
				.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE
				.getProtocolo());
		retornoComunicacaoEletronica.setStatus(dadosRetornoNFE.getStatus());

		informacaoEletronica
				.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);

		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal
				.setStatusProcessamentoInterno(StatusProcessamentoInterno.RETORNADA);

		this.notaFiscalDAO.merge(notaFiscal);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.NotaFiscalService#enviarNotaFiscal(java.lang
	 * .Long)
	 */
	@Override
	@Transactional
	public void enviarNotaFiscal(Long id) {

		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(id);
		
		if (notaFiscal != null) {
			notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.ENVIADA);
			this.notaFiscalDAO.merge(notaFiscal);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.NotaFiscalService#exportarNotasFiscais()
	 */
	@Override
	@Transactional
	public synchronized void exportarNotasFiscais(List<NotaFiscal> notasFiscaisParaExportacao)
			throws FileNotFoundException, IOException {

		String dados = "";

		try {

			dados = gerarArquivoNota(notasFiscaisParaExportacao);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Falha ao gerar arquivo de exportação"));
		}

		ParametroSistema pathNFEExportacao = this.parametroSistemaService
				.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_NFE_EXPORTACAO);

		File diretorioExportacaoNFE = new File(pathNFEExportacao.getValor());

		if (!diretorioExportacaoNFE.isDirectory()) {
			throw new FileNotFoundException(
					"O diretório de exportação parametrizado não é válido!");
		}
		Long time = new Date().getTime();
		File notaExportacao = new File(diretorioExportacaoNFE + File.separator
				+ new File("NFeExportacao" + time + ".txt"));

		FileWriter fileWriter;

		fileWriter = new FileWriter(notaExportacao);

		BufferedWriter buffer = new BufferedWriter(fileWriter);

		buffer.write(dados);

		buffer.close();

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {
			this.enviarNotaFiscal(notaFiscal.getId());
		}
	}

	private String gerarArquivoNota(List<NotaFiscal> notasFiscaisParaExportacao)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		StringBuilder sBuilder = new StringBuilder();

		NFEExporter nfeExporter = new NFEExporter();

		for (NotaFiscal notaFiscal : notasFiscaisParaExportacao) {

			nfeExporter.clear();

			nfeExporter.execute(notaFiscal);

			String s = nfeExporter.gerarArquivo();
			sBuilder.append(s);
		}

		return sBuilder.toString();
	}

	/**
	 * Carrega Grupo das informações de identificação da NF-e
	 * 
	 * @param idTipoNotaFiscal
	 * @param dataEmissao
	 * @return
	 */
	private Identificacao carregaIdentificacao(TipoNotaFiscal tipoNotaFiscal,
			Date dataEmissao) {

		Identificacao identificacao = new Identificacao();
		identificacao.setDataEmissao(dataEmissao);
		identificacao.setTipoOperacao(tipoNotaFiscal.getTipoOperacao());
		identificacao.setDescricaoNaturezaOperacao(tipoNotaFiscal
				.getNopDescricao());
		identificacao.setSerie(tipoNotaFiscal.getSerieNotaFiscal());
		identificacao.setNumeroDocumentoFiscal(serieRepository.next(tipoNotaFiscal.getSerieNotaFiscal()));
		// TODO indPag
		identificacao.setFormaPagamento(FormaPagamento.A_VISTA);

		// TODO NotasReferenciadas

		return identificacao;
	}

	/**
	 * Carrega Grupo de identificação do emitente da NF-e
	 * 
	 * @return
	 */
	private IdentificacaoEmitente carregaEmitente() {
		IdentificacaoEmitente identificacaoEmitente = new IdentificacaoEmitente();

		Distribuidor distribuidor = distribuidorRepository.obter();
		if (distribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor não encontrada!");
		}
		identificacaoEmitente
				.setDocumento(distribuidor.getJuridica().getCnpj());
		identificacaoEmitente.setInscricaoEstual(distribuidor.getJuridica()
				.getInscricaoEstadual());
		identificacaoEmitente.setInscricaoMunicipal(distribuidor.getJuridica()
				.getInscricaoMunicipal());
		identificacaoEmitente.setNome(distribuidor.getJuridica().getNome());
		identificacaoEmitente.setNomeFantasia(distribuidor.getJuridica()
				.getNomeFantasia());

		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor não encontrada!");
		}

		Endereco endereco = enderecoDistribuidor.getEndereco();
		enderecoRepository.detach(endereco);
		endereco.setId(null);
		endereco.setPessoa(null);
		enderecoRepository.adicionar(endereco);
		identificacaoEmitente.setEndereco(endereco);

		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository
				.obterTelefonePrincipal();

		if (telefoneDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Telefone principal do distribuidor não encontrada!");
		}
		Telefone telefone = telefoneDistribuidor.getTelefone();
		telefoneRepository.detach(telefone);
		telefone.setId(null);
		telefone.setPessoa(null);
		telefoneRepository.adicionar(telefone);
		identificacaoEmitente.setTelefone(telefone);
		// TODO: Como definir o Regime Tributario
		identificacaoEmitente
				.setRegimeTributario(RegimeTributario.SIMPLES_NACIONAL);

		return identificacaoEmitente;
	}

	/**
	 * Grupo de identificação do Destinatário da NF-e
	 * 
	 * @param idCota
	 * @return
	 */
	private IdentificacaoDestinatario carregaDestinatario(Long idCota) {
		IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
		Cota cota = cotaRepository.buscarPorId(idCota);
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota
					+ " não encontrada!");
		}

		destinatario.setDocumento(cota.getPessoa().getDocumento());
		destinatario.setEmail(cota.getPessoa().getEmail());

		EnderecoCota enderecoCota = cotaRepository
				.obterEnderecoPrincipal(idCota);
		
		if (enderecoCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal da cota " + idCota + " não encontrada!");
		}
		
		Endereco endereco = enderecoCota.getEndereco();
		enderecoRepository.detach(endereco);
		endereco.setId(null);
		endereco.setPessoa(null);
		enderecoRepository.adicionar(endereco);
		destinatario.setEndereco(endereco);
		

		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			destinatario.setInscricaoEstual(pessoaJuridica
					.getInscricaoEstadual());
			destinatario.setNomeFantasia(pessoaJuridica.getNomeFantasia());
		}
		destinatario.setNome(cota.getPessoa().getNome());
		destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());

		TelefoneCota telefoneCota = telefoneCotaRepository
				.obterTelefonePrincipal(idCota);
		if (telefoneCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Telefone principal da cota " + idCota + " não encontrada!");
		}
		Telefone telefone = telefoneCota.getTelefone();
		
		telefoneRepository.detach(telefone);
		telefone.setId(null);
		telefone.setPessoa(null);
		telefoneRepository.adicionar(telefone);
		destinatario.setTelefone(telefone);

		return destinatario;
	}

	/**
	 * Grupo do detalhamento de Produtos e Serviços da NF-e
	 * 
	 * @param ufOrigem
	 * @param ufDestino
	 * @param raizCNPJ
	 * @param cstICMS
	 * @param raizCNPJ
	 *            ,TipoOperacao tipoOperacao
	 * @return
	 */
	private ProdutoServico carregaProdutoServico(long idProdutoEdicao,
			BigDecimal quantidade, int cfop, TipoOperacao tipoOperacao,
			String ufOrigem, String ufDestino, int naturezaOperacao,
			String codigoNaturezaOperacao, Date dataVigencia,
			BigDecimal valorItem, String raizCNPJ, String cstICMS) {
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.buscarPorId(idProdutoEdicao);
		if (produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição "
					+ idProdutoEdicao + " não encontrado!");
		}

		ProdutoServico produtoServico = new ProdutoServico();

		produtoServico.setCodigoBarras(Long.valueOf(produtoEdicao
				.getCodigoDeBarras()));
		produtoServico.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		produtoServico.setDescricaoProduto(produtoEdicao.getProduto().getDescricao());
		produtoServico.setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
		produtoServico.setProdutoEdicao(produtoEdicao);
		produtoServico.setQuantidade(quantidade);
		produtoServico.setUnidade(produtoEdicao.getProduto().getTipoProduto().getNcm().getUnidadeMedida());
		produtoServico.setValorDesconto(produtoEdicao.getDesconto());
		produtoServico.setCfop(cfop);
		produtoServico.setValorTotalBruto(valorItem.multiply(quantidade));

		EncargoFinanceiroProduto encargoFinanceiroProduto = tributacaoService
				.calcularTributoProduto(raizCNPJ, tipoOperacao, ufOrigem,
						ufDestino, naturezaOperacao, codigoNaturezaOperacao,
						produtoEdicao.getProduto().getTipoProduto()
								.getCodigoNBM(), dataVigencia, cstICMS,
						valorItem);

		produtoServico.setEncargoFinanceiro(encargoFinanceiroProduto);

		return produtoServico;
	}

	@Override
	public void emitiNotaFiscal(long idTipoNotaFiscal, Date dataEmissao,
			Long idCota, List<ItemNotaFiscal> listItemNotaFiscal, InformacaoTransporte transporte, InformacaoAdicional informacaoAdicional) {

		NotaFiscal notaFiscal = new NotaFiscal();

		TipoNotaFiscal tipoNotaFiscal = tipoNotaFiscalRepository
				.buscarPorId(idTipoNotaFiscal);

		if (tipoNotaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Tipo da Nota Fiscal " + idTipoNotaFiscal
							+ " não encontrada!");
		}

		notaFiscal.setIdentificacao(carregaIdentificacao(tipoNotaFiscal,
				dataEmissao));
		notaFiscal.setIdentificacaoDestinatario(carregaDestinatario(idCota));
		notaFiscal.setIdentificacaoEmitente(carregaEmitente());

		String raizCNPJ = notaFiscal.getIdentificacaoEmitente().getDocumento()
				.substring(0, 7);
		String ufOrigem = notaFiscal.getIdentificacaoEmitente().getEndereco()
				.getUf();
		String ufDestino = notaFiscal.getIdentificacaoDestinatario()
				.getEndereco().getUf();

		notaFiscal.setProdutosServicos(new ArrayList<ProdutoServico>(
				listItemNotaFiscal.size()));
		int cfop;
		
		if(ufOrigem.equals(ufDestino)){
			if (tipoNotaFiscal.getCfopEstado() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP do estado para tipo nota fiscal " + idTipoNotaFiscal
								+ " não encontrada!");
			}
			
			
			cfop = Integer.valueOf(tipoNotaFiscal.getCfopEstado().getCodigo());
		}else{
			
			if (tipoNotaFiscal.getCfopOutrosEstados() == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"CFOP para outros estados para tipo nota fiscal " + idTipoNotaFiscal
								+ " não encontrada!");
			}
			cfop = Integer.valueOf(tipoNotaFiscal.getCfopOutrosEstados().getCodigo());
		}
		
		InformacaoValoresTotais informacaoValoresTotais = new InformacaoValoresTotais();		
		notaFiscal.setInformacaoValoresTotais(informacaoValoresTotais);
		
		for (ItemNotaFiscal itemNotaFiscal : listItemNotaFiscal) {

			ProdutoServico produtoServico = carregaProdutoServico(
					itemNotaFiscal.getIdProdutoEdicao(),
					itemNotaFiscal.getQuantidade(), cfop,
					tipoNotaFiscal.getTipoOperacao(), ufOrigem, ufDestino,
					tipoNotaFiscal.getNopCodigo().intValue(),
					tipoNotaFiscal.getNopDescricao(), dataEmissao,
					itemNotaFiscal.getValorUnitario(), raizCNPJ,
					itemNotaFiscal.getCstICMS());

			notaFiscal.getProdutosServicos().add(produtoServico);
			
			EncargoFinanceiro encargoFinanceiro =  produtoServico.getEncargoFinanceiro();
			
			informacaoValoresTotais.setValorProdutos(soma(informacaoValoresTotais.getValorProdutos(),produtoServico.getValorTotalBruto()));					
			
			if(encargoFinanceiro instanceof EncargoFinanceiroProduto){
				EncargoFinanceiroProduto encargoFinanceiroProduto = (EncargoFinanceiroProduto) encargoFinanceiro;
				ICMS icms =  encargoFinanceiroProduto.getIcms();				
				
					informacaoValoresTotais.setValorBaseCalculoICMS(soma(informacaoValoresTotais.getValorBaseCalculoICMS(),icms.getValorBaseCalculo()));				
				
					informacaoValoresTotais.setValorICMS(soma(informacaoValoresTotais.getValorICMS(),icms.getValor()));
			
				
				IPI ipi =  encargoFinanceiroProduto.getIpi();			
			
					informacaoValoresTotais.setValorIPI(soma(informacaoValoresTotais.getValorIPI(),ipi.getValor()));
				
				COFINS cofins =  encargoFinanceiroProduto.getCofins();			
				
				
				informacaoValoresTotais.setValorCOFINS(soma(informacaoValoresTotais.getValorCOFINS(),cofins.getValor()));
				
				
			}
		}
		
		notaFiscal.setInformacaoTransporte(transporte);
		
		notaFiscal.setInformacaoAdicional(informacaoAdicional);
		
		
		notaFiscalDAO.adicionar(notaFiscal);
	}
	
	/**
	 * Obtém Itens para NFes de Envio de Consignado.
	 * 
	 * @param cota 
	 * @param periodo intervalo do periodo de lançamento
	 */
	private List<ItemNotaFiscal> obterItensNFeRemessaEmConsignacao(
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores, List<Long> listaIdProduto) {
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);
	
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota =
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(
						idCota, listaGrupoMovimentoEstoque, periodo, listaIdFornecedores, listaIdProduto);
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {
			
			//TODO: montar lista itens
			
		}
		
		//TODO: lógica de negocio para tipo de nota
		//TODO: desconto
		
		return null;//listaItensNotaFiscal;
	}
	
	/**
	 * Obtém Itens para NFes de Devolução de Consignado.
	 * 
	 * @param cota 
	 * @param periodo intervalo do periodo de lançamento
	 */
	private List<ItemNotaFiscal> obterItensNFeEntradaDevolucaoRemessaConsignacao(
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores, List<Long> listaIdProduto) {
			
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENCALHE_ANTECIPADO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota =
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(
						idCota, listaGrupoMovimentoEstoque, periodo, listaIdFornecedores, listaIdProduto);
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {
			
			//TODO: montar lista itens
			
		}
		
		//TODO: lógica de negocio para tipo de nota
		//TODO: desconto
		
		return null;//listaItensNotaFiscal;
	}
	
	/**
	 * Obtém Itens para NFes de Venda.
	 * Itens de Envio menos Itens de Devolução;
	 * 
	 * @param cota
	 * @param periodo intervalo do periodo de lançamento
	 */
	private List<ItemNotaFiscal> obterItensNFeVenda(
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores, List<Long> listaIdProdutos) {
		
		List<ItemNotaFiscal> itensNFeEnvioConsignado = 
				this.obterItensNFeRemessaEmConsignacao(idCota, periodo, listaIdFornecedores, listaIdProdutos);
		
		List<ItemNotaFiscal> itensNFeDevolucaoConsignado = 
				this.obterItensNFeEntradaDevolucaoRemessaConsignacao(idCota, periodo, listaIdFornecedores, listaIdProdutos);
		
		List<ItemNotaFiscal> itensNFeVenda = new ArrayList<ItemNotaFiscal>();
		
		for (ItemNotaFiscal itemNFeEnvio : itensNFeEnvioConsignado) {
			
			ItemNotaFiscal itemNFeVenda = itemNFeEnvio;
			
			if (itensNFeDevolucaoConsignado.contains(itemNFeEnvio)) {
				
				ItemNotaFiscal itemNFeDevolucao = itensNFeDevolucaoConsignado.get(itensNFeDevolucaoConsignado.indexOf(itemNFeEnvio));
										
				BigDecimal quantidade = itemNFeEnvio.getQuantidade().subtract(itemNFeDevolucao.getQuantidade());
				
				if (quantidade.compareTo(BigDecimal.ZERO) == 0) {
					continue;
				}
				
				itemNFeVenda.setQuantidade(quantidade);		
			}
				
			itensNFeVenda.add(itemNFeEnvio);
		}
		
		return  itensNFeVenda;
	}
	
}
