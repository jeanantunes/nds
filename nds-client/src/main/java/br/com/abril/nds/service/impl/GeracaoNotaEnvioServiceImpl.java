package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
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
	private TelefoneRepository telefoneRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;

	@Autowired
	private NotaEnvioRepository notaEnvioRepository;

	@Autowired
	private ItemNotaEnvioRepository itemNotaEnvioRepository;

	@Autowired
	private RotaRepository rotaRepository;

	//@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro) {
		Distribuidor distribuidor = this.distribuidorRepository.obter();


		Set<Long> idsCotasDestinatarias = this.cotaRepository
				.obterIdsCotasComNotaEnvioEntre(filtro);

		List<ConsultaNotaEnvioDTO> listaCotaExemplares = new ArrayList<ConsultaNotaEnvioDTO>();

		for (Long idCota : idsCotasDestinatarias) {

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			ConsultaNotaEnvioDTO cotaExemplares = new ConsultaNotaEnvioDTO();

			cotaExemplares.setIdCota(cota.getId());
			cotaExemplares.setNomeCota(cota.getPessoa().getNome());
			cotaExemplares.setNumeroCota(cota.getNumeroCota());	
			if(cota.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO)) {
				cotaExemplares.setCotaSuspensa(true);
			}

			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = obterItensNotaVenda(
					distribuidor, idCota, filtro.getIntervaloMovimento(), filtro.getIdFornecedores());

			if (listaMovimentoEstoqueCota!= null && !listaMovimentoEstoqueCota.isEmpty()) {
				this.sumarizarTotalItensNota(listaMovimentoEstoqueCota, cotaExemplares,cota);
				listaCotaExemplares.add(cotaExemplares);
			}
		}
		
		String sortname = filtro.getPaginacaoVO().getSortColumn();
		String sortorder= filtro.getPaginacaoVO().getSortOrder();
				
		if(sortname == null || "".equals(sortname)) 
			sortname = "numeroCota";
		
		BeanComparator beanComparator = null;
		if(sortorder != null && !sortorder.isEmpty() && "desc".equals(sortorder))
			beanComparator = new BeanComparator(sortname, new ReverseComparator(new ComparableComparator()));
		else 
			beanComparator = new BeanComparator(sortname);
		
		Collections.sort(listaCotaExemplares, beanComparator);
		
		return listaCotaExemplares;
	}
	
	@Override
	@Transactional
	public Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro) {
		
		Set<Long> idsCotasDestinatarias = this.cotaRepository
				.obterIdsCotasComNotaEnvioEntre(filtro);
		return idsCotasDestinatarias.size();
	}

	private List<MovimentoEstoqueCota> obterItensNotaVenda(Distribuidor distribuidor,
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores) {

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();

		listaGrupoMovimentoEstoques
				.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		return movimentoEstoqueCotaRepository
				.obterMovimentoEstoqueCotaPor(distribuidor, idCota,
						listaGrupoMovimentoEstoques, periodo,
						listaIdFornecedores);

	}

	private List<ItemNotaEnvio> gerarItensNotaEnvio(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Long idCota) {

		List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>(
				listaMovimentoEstoqueCota.size());

		Cota cota = cotaRepository.buscarPorId(idCota);
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

			ProdutoEdicao produtoEdicao = movimentoEstoqueCota
					.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			BigDecimal percentualDesconto = movimentoEstoqueCota.getValoresAplicados().getValorDesconto();

			BigInteger quantidade = movimentoEstoqueCota.getQtde();

			List<MovimentoEstoqueCota> listaMovimentoEstoqueItem = new ArrayList<MovimentoEstoqueCota>();

			listaMovimentoEstoqueItem.add(movimentoEstoqueCota);

			ItemNotaEnvio itemNotaEnvio = new ItemNotaEnvio();

			itemNotaEnvio.setProdutoEdicao(produtoEdicao);
			itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto()
					.getCodigo());
			itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
			itemNotaEnvio.setDesconto(percentualDesconto);
			itemNotaEnvio.setReparte(quantidade);
			itemNotaEnvio.setPrecoCapa(precoVenda);
			itemNotaEnvio
					.setListaMovimentoEstoqueCota(listaMovimentoEstoqueItem);

			listItemNotaEnvio.add(itemNotaEnvio);
		}

		return listItemNotaEnvio;
	}

	private void sumarizarTotalItensNota(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota,
			ConsultaNotaEnvioDTO cotaExemplares, Cota cota) {

		BigInteger quantidade = BigInteger.ZERO;
		BigDecimal preco = BigDecimal.ZERO;
		BigDecimal precoComDesconto = BigDecimal.ZERO;
		cotaExemplares.setNotaImpressa(false);

		for (MovimentoEstoqueCota movimento : listaMovimentoEstoqueCota) {
			BigDecimal precoVenda = new BigDecimal("20.90"); //movimento.getValoresAplicados().getPrecoVenda();
			BigDecimal valorDesconto = new BigDecimal("10"); // movimento.getValoresAplicados().getPrecoComDesconto();			
			quantidade = quantidade.add(movimento.getQtde());
			preco = preco.add(precoVenda.multiply(new BigDecimal(movimento.getQtde())));
			precoComDesconto = precoComDesconto.add(precoVenda.subtract(valorDesconto).multiply(new BigDecimal(movimento.getQtde())));	
			
			List<ItemNotaEnvio> itens = movimento.getListaItemNotaEnvio();
			
			if( itens!= null && !itens.isEmpty() ){
				cotaExemplares.setNotaImpressa(true);
			}
			
		}

		cotaExemplares.setTotal(preco);
		cotaExemplares.setTotalDesconto(precoComDesconto.setScale(2, BigDecimal.ROUND_HALF_UP));
		cotaExemplares.setExemplares(quantidade.longValue());

	}
	
	@Override
	@Transactional(readOnly=false)
	public NotaEnvio visualizar(Integer numeroCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao,
			Date dataEmissao, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores){
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		Long idCota = cota.getId();
		
		NotaEnvio notaEnvio = criarNotaEnvio(idCota, idRota, chaveAcesso,
				codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
				distribuidor, cota);

		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = obterItensNotaVenda(
				distribuidor, idCota, periodo, listaIdFornecedores);
		
		 List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(listaMovimentoEstoqueCota, idCota);
		if (listaItemNotaEnvio.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não é possível gerar Nota de Envio para a Cota "
							+ cota.getNumeroCota());
		}
		int sequencia = 0;
		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {
			itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio,
					++sequencia));
			
		}

		notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);		
		return notaEnvio;
	}

	@Override
	@Transactional
	public NotaEnvio gerar(Long idCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao,
			Date dataEmissao, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores) {
		Distribuidor distribuidor = distribuidorRepository.obter();
		Cota cota = cotaRepository.buscarPorId(idCota);

		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = obterItensNotaVenda(
				distribuidor, idCota, periodo, listaIdFornecedores);
		
		 List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(listaMovimentoEstoqueCota, idCota);
		if (listaItemNotaEnvio.isEmpty()) {
			return null;
			// Comentado devido a não possibilitar a gravação de cotas sem reparte 
			/*throw new ValidacaoException(TipoMensagem.ERROR,
					"Não é possível gerar Nota de Envio para a Cota "
							+ cota.getNumeroCota());*/
		}

		if(idRota == null) {
			Long idRoteiro = null;
			List<Roteiro> roteiros = cota.getBox().getRoteirizacao().getRoteiros();
			for(Roteiro r : roteiros) {
				if(!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {
					idRoteiro = r.getId();
				}
			}
			idRota = (Long) cota.getBox().getRoteirizacao().getRoteiro(idRoteiro).getRotas().get(0).getId();
		}
		
		NotaEnvio notaEnvio = criarNotaEnvio(idCota, idRota, chaveAcesso,
				codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
				distribuidor, cota);

		notaEnvioRepository.adicionar(notaEnvio);
		int sequencia = 0;
		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {
			itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio,
					++sequencia));
			itemNotaEnvioRepository.adicionar(itemNotaEnvio);
		}

		notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);

		notaEnvio = notaEnvioRepository.merge(notaEnvio);
		return notaEnvio;
	}

	/**
	 * @param idCota
	 * @param idRota
	 * @param chaveAcesso
	 * @param codigoNaturezaOperacao
	 * @param descricaoNaturezaOperacao
	 * @param dataEmissao
	 * @param distribuidor
	 * @param cota
	 * @return
	 * @throws ValidacaoException
	 */
	private NotaEnvio criarNotaEnvio(Long idCota, Long idRota,
			String chaveAcesso, Integer codigoNaturezaOperacao,
			String descricaoNaturezaOperacao, Date dataEmissao,
			Distribuidor distribuidor, Cota cota) throws ValidacaoException {
		NotaEnvio notaEnvio = new NotaEnvio();
		
		
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
		return notaEnvio;
	}

	/**
	 * @param distribuidor
	 * @return
	 * @throws ValidacaoException
	 */
	private IdentificacaoEmitente carregarEmitente(Distribuidor distribuidor)
			throws ValidacaoException {
		PessoaJuridica pessoaEmitente = distribuidor.getJuridica();

		IdentificacaoEmitente emitente = new IdentificacaoEmitente();

		// Corrigido devo ao fato da tabela pessoa gravar os documentos com máscara, embora o campo documento da tabela nota_envio esperar apenas 14 caracteres
		String documento = pessoaEmitente.getDocumento().replaceAll("[-+.^:,/]","");
		
		emitente.setDocumento(documento);
		emitente.setNome(pessoaEmitente.getNome());
		emitente.setPessoaEmitenteReferencia(pessoaEmitente);
		emitente.setInscricaoEstadual(pessoaEmitente.getInscricaoEstadual());

		EnderecoDistribuidor enderecoDistribuidor = distribuidorRepository
				.obterEnderecoPrincipal();

		if (enderecoDistribuidor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal do distribuidor n�o encontrada!");
		}

		try {
			emitente.setEndereco(cloneEndereco(enderecoDistribuidor
					.getEndereco()));
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
		destinatario.setNumeroCota(cota.getNumeroCota());
		destinatario.setDocumento(cota.getPessoa().getDocumento());

		EnderecoCota enderecoCota = cotaRepository.obterEnderecoPrincipal(cota
				.getId());

		if (enderecoCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Endereço principal da cota " + cota.getId()
							+ " não encontrada!");
		}

		try {
			destinatario.setEndereco(cloneEndereco(enderecoCota.getEndereco()));
		} catch (CloneNotSupportedException e) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Erro ao adicionar o endereço do Emitente!");
		}

		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			destinatario.setInscricaoEstadual(pessoaJuridica
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
		destinatario.setCodigoBox(cota.getBox().getCodigo());
		destinatario.setBoxReferencia(cota.getBox());
		destinatario.setCodigoBox(cota.getBox().getCodigo());
		destinatario.setNomeBox(cota.getBox().getNome());

		if (idRota != null) {
			Rota rota = rotaRepository.buscarPorId(idRota);
			if (rota == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Rota não encontrada!");
			}
			destinatario.setRotaReferencia(rota);
			destinatario.setCodigoRota(rota.getId().toString());
			destinatario.setDescricaoRota(rota.getDescricaoRota());
		}
		return destinatario;
	}

	private Endereco cloneEndereco(Endereco endereco)
			throws CloneNotSupportedException {
		Endereco novoEndereco = endereco.clone();
		enderecoRepository.detach(novoEndereco);
		novoEndereco.setId(null);
		novoEndereco.setPessoa(null);
		if (novoEndereco.getCep() != null) {
			novoEndereco.setCep(novoEndereco.getCep().replace("-", ""));
		}
		if (novoEndereco.getCodigoUf() == null
				&& novoEndereco.getCodigoCidadeIBGE() != null) {
			if (novoEndereco.getCodigoCidadeIBGE().toString().length() > 2) {
				novoEndereco.setCodigoUf(novoEndereco
						.getCodigoCidadeIBGE().toString().substring(0, 2));
			} else {
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE().toString());
			}
		}
		enderecoRepository.adicionar(novoEndereco);
		return novoEndereco;
	}

	@Override
	@Transactional
	public List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro, List<Long> idCotasSuspensasAusentes) {
		
		List<NotaEnvio> listaNotaEnvio = new ArrayList<NotaEnvio>();
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(SituacaoCadastro.ATIVO);
		situacoesCadastro.add(SituacaoCadastro.SUSPENSO);
		
		Set<Long> listaIdCotas = this.cotaRepository
				.obterIdCotasEntre(filtro.getIntervaloCota(), filtro.getIntervaloBox(),
						situacoesCadastro, filtro.getIdRoteiro(), filtro.getIdRota(), null, null, null, null);
		
		if (idCotasSuspensasAusentes != null) {
			listaIdCotas.addAll(idCotasSuspensasAusentes);
		}

		for(Long idCota : listaIdCotas) {
			
			NotaEnvio notaEnvio = this.gerar(idCota, filtro.getIdRota(), null, null, null, 
					filtro.getDataEmissao(), filtro.getIntervaloMovimento(), filtro.getIdFornecedores());
			
			if (notaEnvio != null) {
				listaNotaEnvio.add(notaEnvio);
			}
		}
		
		return listaNotaEnvio;
	}

}