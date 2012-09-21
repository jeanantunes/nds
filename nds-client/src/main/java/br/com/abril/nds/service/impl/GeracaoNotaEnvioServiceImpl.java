package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.envio.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.envio.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.PessoaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class GeracaoNotaEnvioServiceImpl implements GeracaoNotaEnvioService {

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Autowired
	private DescontoService descontoService;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private NotaEnvioRepository notaEnvioRepository;
	
	@Autowired
	private ItemNotaEnvioRepository itemNotaEnvioRepository;
	
	@Autowired
	private RotaRepository rotaRepository;

	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, String sortname, String sortorder,
			Integer resultsPage, Integer page,
			SituacaoCadastro situacaoCadastro, Long idRoteiro, Long idRota) {

		Set<Long> idsCotasDestinatarias = this.cotaRepository
				.obterIdCotasEntre(intervalorCota, intervaloBox,
						situacaoCadastro, idRoteiro, idRota);

		Map<Long, QuantidadePrecoItemNotaDTO> cotasTotalItens = obterTotalItensNotaEnvioPorCota(
				intervaloDateMovimento, listIdFornecedor, idsCotasDestinatarias);

		Set<Long> idCotas = cotasTotalItens.keySet();

		List<CotaExemplaresDTO> listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();

		for (Long idCota : idCotas) {

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();

			cotaExemplares.setIdCota(cota.getId());
			cotaExemplares.setNomeCota(cota.getPessoa().getNome());
			cotaExemplares.setNumeroCota(cota.getNumeroCota());
			
			
			QuantidadePrecoItemNotaDTO quantidadePrecoItemNotaDTO = cotasTotalItens.get(cota);
			
			if(quantidadePrecoItemNotaDTO != null){
				cotaExemplares.setTotal(quantidadePrecoItemNotaDTO.getPreco());
				cotaExemplares.setTotalDesconto(quantidadePrecoItemNotaDTO.getPrecoComDesconto());
				cotaExemplares.setExemplares(quantidadePrecoItemNotaDTO.getQuantidade().longValue());
			}else{
				cotaExemplares.setTotal(BigDecimal.ZERO);
				cotaExemplares.setTotalDesconto(BigDecimal.ZERO);
				cotaExemplares.setExemplares(0L);
			}

			listaCotaExemplares.add(cotaExemplares);

		}

		return listaCotaExemplares;
	}

	public Map<Long, QuantidadePrecoItemNotaDTO> obterTotalItensNotaEnvioPorCota(
			Intervalo<Date> periodo, List<Long> listaIdFornecedores,
			Set<Long> idsCotasDestinatarias) {

		Map<Long, QuantidadePrecoItemNotaDTO> idCotaTotalItensNota = new HashMap<Long, QuantidadePrecoItemNotaDTO>();

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		for (Long idCota : idsCotasDestinatarias) {

			List<ItemNotaEnvio> itensNotaEnvio = obterItensNotaVenda(
					distribuidor, idCota, periodo, listaIdFornecedores);

			if (itensNotaEnvio != null && !itensNotaEnvio.isEmpty()) {
				idCotaTotalItensNota.put(idCota,
						this.sumarizarTotalItensNota(itensNotaEnvio));
			}
		}

		return idCotaTotalItensNota;
	}

	private List<ItemNotaEnvio> obterItensNotaVenda(Distribuidor distribuidor,
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores) {

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();

		listaGrupoMovimentoEstoques
				.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = movimentoEstoqueCotaRepository
				.obterMovimentoEstoqueCotaPor(distribuidor, idCota,
						listaGrupoMovimentoEstoques, periodo,
						listaIdFornecedores);

		return gerarItensNotaEnvio(listaMovimentoEstoqueCota, idCota);

	}

	private List<ItemNotaEnvio> gerarItensNotaEnvio(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Long idCota) {

		List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>(listaMovimentoEstoqueCota.size());

		Cota cota = cotaRepository.buscarPorId(idCota);
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

			ProdutoEdicao produtoEdicao = movimentoEstoqueCota
					.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			BigDecimal percentualDesconto = descontoService
					.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(
					precoVenda, percentualDesconto);

			BigDecimal valorUnitario = precoVenda.subtract(valorDesconto);

			BigInteger quantidade = movimentoEstoqueCota.getQtde();

			List<MovimentoEstoqueCota> listaMovimentoEstoqueItem = new ArrayList<MovimentoEstoqueCota>();

			listaMovimentoEstoqueItem.add(movimentoEstoqueCota);

			ItemNotaEnvio itemNotaEnvio = new ItemNotaEnvio();

			itemNotaEnvio.setProdutoEdicao(produtoEdicao);
			itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
			itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
			itemNotaEnvio.setDesconto(percentualDesconto);
			itemNotaEnvio.setReparte(quantidade);
			itemNotaEnvio.setPrecoCapa(valorUnitario);
			itemNotaEnvio
					.setListaMovimentoEstoqueCota(listaMovimentoEstoqueItem);

			listItemNotaEnvio.add(itemNotaEnvio);
		}

		return listItemNotaEnvio;
	}

	private QuantidadePrecoItemNotaDTO sumarizarTotalItensNota(
			List<ItemNotaEnvio> listaItemNotaEnvio) {

		QuantidadePrecoItemNotaDTO dto = new QuantidadePrecoItemNotaDTO();

		BigInteger quantidade = BigInteger.ZERO;
		BigDecimal preco = BigDecimal.ZERO;
		BigDecimal precoComDesconto = BigDecimal.ZERO;

		for (ItemNotaEnvio item : listaItemNotaEnvio) {
			quantidade = quantidade.add(item.getReparte());
			preco = preco.add(item.getPrecoCapa().multiply(
					new BigDecimal(quantidade)));

			BigDecimal desconto = this.descontoService
					.obterDescontoPorCotaProdutoEdicao(item
							.getListaMovimentoEstoqueCota().get(0).getCota(),
							item.getProdutoEdicao());

			precoComDesconto = precoComDesconto.add(
					item.getPrecoCapa()
							.subtract(desconto, new MathContext(3))
							.multiply(item.getPrecoCapa())
							.divide(new BigDecimal(100))).multiply(
					new BigDecimal(item.getReparte()));
		}

		dto.setQuantidade(quantidade);
		dto.setPreco(preco);
		dto.setPrecoComDesconto(precoComDesconto);

		return dto;
	}
	
	
	@Override
	@Transactional
	public NotaEnvio gerar(Long idCota, Long idRota, String chaveAcesso, Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao, Date dataEmissao, Intervalo<Date> periodo, List<Long> listaIdFornecedores){
		NotaEnvio notaEnvio = new NotaEnvio();		
		Distribuidor distribuidor =  distribuidorRepository.obter();
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + idCota
					+ " não encontrada!");
		}
		notaEnvio.setEmitente(carregarEmitente(distribuidor));
		notaEnvio.setDestinatario(carregaDestinatario(cota, idRota));
		
		notaEnvio.setChaveAcesso(chaveAcesso);
		notaEnvio.setCodigoNaturezaOperacao(codigoNaturezaOperacao);
		notaEnvio.setDescricaoNaturezaOperacao(descricaoNaturezaOperacao);
		notaEnvio.setDataEmissao(dataEmissao);
		
		notaEnvioRepository.adicionar(notaEnvio);
		List<ItemNotaEnvio> listaItemNotaEnvio = obterItensNotaVenda(distribuidor, idCota, periodo, listaIdFornecedores);
		
		
		if(listaItemNotaEnvio.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Não é possivel gerar nota de envio para Cota " + cota.getNumeroCota());
		}
		int sequencia = 0;
		for(ItemNotaEnvio itemNotaEnvio:listaItemNotaEnvio){
			itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio, ++sequencia));
			itemNotaEnvioRepository.adicionar(itemNotaEnvio);
		}
		
		notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);
		
		
		
		notaEnvio = notaEnvioRepository.merge(notaEnvio);
		return notaEnvio;
	}

	/**
	 * @param distribuidor 
	 * @return
	 * @throws ValidacaoException
	 */
	private IdentificacaoEmitente carregarEmitente(Distribuidor distribuidor) throws ValidacaoException {
		PessoaJuridica pessoaEmitente = distribuidor.getJuridica();
		
		IdentificacaoEmitente emitente = new IdentificacaoEmitente();
		
		
		emitente.setDocumento(pessoaEmitente.getDocumento());
		emitente.setNome(pessoaEmitente.getNome());
		emitente.setPessoaEmitenteReferencia(pessoaEmitente);
		emitente.setInscricaoEstual(pessoaEmitente.getInscricaoEstadual());
		
		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor não encontrada!");
		}

		try {
			emitente.setEndereco(cloneEndereco(enderecoDistribuidor.getEndereco()));
		} catch (Exception exception) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do distribuidor!");
		}
		
		TelefoneDistribuidor telefoneDistribuidor = distribuidorRepository
				.obterTelefonePrincipal();

		if (telefoneDistribuidor != null) {
			Telefone telefone = telefoneDistribuidor.getTelefone();
			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			emitente.setTelefone(telefone);
		}
		return emitente;
	}
	
	private IdentificacaoDestinatario carregaDestinatario(Cota cota, Long idRota) {
		IdentificacaoDestinatario destinatario = new IdentificacaoDestinatario();
		

		destinatario.setDocumento(cota.getPessoa().getDocumento());

		EnderecoCota enderecoCota = cotaRepository
				.obterEnderecoPrincipal(cota.getId());
		
		if (enderecoCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal da cota " + cota.getId() + " não encontrada!");
		}
		
		try {
			destinatario.setEndereco(cloneEndereco(enderecoCota.getEndereco()));
		} catch (CloneNotSupportedException e) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do Emitente!");
		}
		

		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			destinatario.setInscricaoEstual(pessoaJuridica
					.getInscricaoEstadual());
		}
		destinatario.setNome(cota.getPessoa().getNome());
		destinatario.setPessoaDestinatarioReferencia(cota.getPessoa());

		TelefoneCota telefoneCota = telefoneCotaRepository
				.obterTelefonePrincipal(cota.getId());
		if (telefoneCota != null) {
			Telefone telefone = telefoneCota.getTelefone();
			
			telefoneRepository.detach(telefone);
			telefone.setId(null);
			telefone.setPessoa(null);
			telefoneRepository.adicionar(telefone);
			destinatario.setTelefone(telefone);
		}
		destinatario.setBoxReferencia(cota.getBox());
		destinatario.setCodigoBox(cota.getBox().getCodigo());
		destinatario.setNomeBox(cota.getBox().getNome());
		
		Rota rota = rotaRepository.buscarPorId(idRota);
		
		if(rota ==null ){
			throw new ValidacaoException(TipoMensagem.ERROR, "Rota não encontrada!");
		}
		destinatario.setRotaReferencia(rota);		
		
		destinatario.setCodigoRota(rota.getCodigoRota());
		destinatario.setCodigoRota(rota.getDescricaoRota());

		return destinatario;
	}
	
	private Endereco cloneEndereco(Endereco endereco) throws CloneNotSupportedException {
		Endereco novoEndereco = endereco.clone();
		enderecoRepository.detach(novoEndereco);
		novoEndereco.setId(null);
		novoEndereco.setPessoa(null);
		if (novoEndereco.getCep() != null) {
			novoEndereco.setCep(novoEndereco.getCep().replace("-", ""));
		}
		if (novoEndereco.getCodigoUf() == null && novoEndereco.getCodigoCidadeIBGE() != null) {
			if(novoEndereco.getCodigoCidadeIBGE().toString().length() > 2){
			novoEndereco.setCodigoUf(Integer.parseInt(novoEndereco.getCodigoCidadeIBGE().toString().substring(0, 2)));
			}else{
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE());
			}
		}
		enderecoRepository.adicionar(novoEndereco);
		return novoEndereco;
	}
}