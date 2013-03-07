package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.enums.TipoMensagem;
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
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.envio.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.envio.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvio;
import br.com.abril.nds.model.envio.nota.ItemNotaEnvioPK;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaEnvioRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TelefoneRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNotaEnvioServiceImpl implements GeracaoNotaEnvioService {

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

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

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Transactional
	public List<ConsultaNotaEnvioDTO> busca(FiltroConsultaNotaEnvioDTO filtro) {

		return cotaRepository
				.obterDadosCotasComNotaEnvio(filtro);

	}

	@Override
	@Transactional
	public Integer buscaCotasNotasDeEnvioQtd(FiltroConsultaNotaEnvioDTO filtro) {
		return cotaRepository.obterCountCotasComNotaEnvioEntre(filtro);
	}

	private List<ItemNotaEnvio> gerarItensNotaEnvio(
			List<EstudoCota> listaEstudoCota, Cota cota) {

		List<ItemNotaEnvio> listItemNotaEnvio = new ArrayList<ItemNotaEnvio>(
				listaEstudoCota.size());

		for (EstudoCota estudoCota : listaEstudoCota) {

			ProdutoEdicao produtoEdicao = estudoCota.getEstudo()
					.getProdutoEdicao();

			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigInteger quantidadeResultante = BigInteger.ZERO;
			
			for(RateioDiferenca rateioDiferenca : estudoCota.getRateiosDiferenca()) {
				
				for(MovimentoEstoqueCota mec : estudoCota.getMovimentosEstoqueCota()) {
					
					if(mec.getEstudoCota().getId() == rateioDiferenca.getEstudoCota().getId()) {
						
						if(rateioDiferenca.getDiferenca().getTipoDiferenca().equals(TipoDiferenca.FALTA_DE)
								|| rateioDiferenca.getDiferenca().getTipoDiferenca().equals(TipoDiferenca.FALTA_EM)) {
							
							quantidadeResultante = quantidadeResultante.add(rateioDiferenca.getQtde().negate());
							
						} else {
							
							quantidadeResultante = quantidadeResultante.add(rateioDiferenca.getQtde());
							
						}
					}
				}
			}
			
			List<MovimentoEstoqueCota> movimentos = new ArrayList<>();			
			movimentos.addAll(
					movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(
					estudoCota.getEstudo().getLancamento().getDataLancamentoDistribuidor()
					, cota.getId()
					, GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE)
				);
			
			for(MovimentoEstoqueCota movimentoEstoqueCota : movimentos) {
				quantidadeResultante = quantidadeResultante.add(movimentoEstoqueCota.getQtde());
			}
			
			Desconto percentualDesconto = this.descontoService
					.obterDescontoPorCotaProdutoEdicao(estudoCota.getEstudo()
							.getLancamento(), cota, produtoEdicao);

			BigInteger quantidade = quantidadeResultante.add(estudoCota.getQtdeEfetiva());

			if (estudoCota.getItemNotaEnvios().isEmpty()) {

				ItemNotaEnvio itemNotaEnvio = criarNovoItemNotaEnvio(
						estudoCota,
						produtoEdicao,
						precoVenda,
						((percentualDesconto != null && percentualDesconto
								.getValor() != null) ? percentualDesconto
								.getValor() : BigDecimal.ZERO), 
						quantidade);
				listItemNotaEnvio.add(itemNotaEnvio);
			} else{
				listItemNotaEnvio.addAll(estudoCota.getItemNotaEnvios());
			}
			
		}

		sortItensByProdutoNome(listItemNotaEnvio);
		
		return listItemNotaEnvio;
	}

	private void sortItensByProdutoNome(List<ItemNotaEnvio> listItemNotaEnvio) {
		Collections.sort(listItemNotaEnvio, new Comparator<ItemNotaEnvio>(){
			@Override
			public int compare(ItemNotaEnvio o1, ItemNotaEnvio o2) {
				return getNomeProdutoEdicao(o1).compareTo(getNomeProdutoEdicao(o2));
			}
			
		});
	}

	private String getNomeProdutoEdicao(ItemNotaEnvio itemNotaEnvio) {
		
		String nomeProduto = "";
		
		if (itemNotaEnvio.getProdutoEdicao() != null) {
			if(itemNotaEnvio.getProdutoEdicao().getProduto() != null) {
				nomeProduto = itemNotaEnvio.getProdutoEdicao().getProduto().getNome();
			}
		}
		
		return nomeProduto;
	}
	
	private ItemNotaEnvio criarNovoItemNotaEnvio(EstudoCota estudoCota,
			ProdutoEdicao produtoEdicao, BigDecimal precoVenda,
			BigDecimal percentualDesconto, BigInteger quantidade) {

		ItemNotaEnvio itemNotaEnvio = new ItemNotaEnvio();

		itemNotaEnvio.setProdutoEdicao(produtoEdicao);
		itemNotaEnvio.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
		itemNotaEnvio.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		itemNotaEnvio.setPublicacao(produtoEdicao.getProduto().getNome());
		itemNotaEnvio.setDesconto(percentualDesconto);
		itemNotaEnvio.setReparte(quantidade);
		itemNotaEnvio.setPrecoCapa(precoVenda);
		itemNotaEnvio.setEstudoCota(estudoCota);

		return itemNotaEnvio;
	}

	
	@Override
	@Transactional(readOnly = false)
	public NotaEnvio visualizar(Integer numeroCota, Long idRota,
			String chaveAcesso, Integer codigoNaturezaOperacao,
			String descricaoNaturezaOperacao, Date dataEmissao,
			Intervalo<Date> periodo, List<Long> listaIdFornecedores) {

		Distribuidor distribuidor = distribuidorRepository.obter();

		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);

		Long idCota = cota.getId();

		if (idRota == null) {

			Long idRoteiro = null;

			List<Roteiro> roteiros = cota.getBox().getRoteirizacao()
					.getRoteiros();

			for (Roteiro r : roteiros) {

				if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {

					idRoteiro = r.getId();
				}
			}

			idRota = (Long) cota.getBox().getRoteirizacao()
					.getRoteiro(idRoteiro).getRotas().get(0).getId();
		}

		NotaEnvio notaEnvio = criarNotaEnvio(idCota, idRota, chaveAcesso,
				codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
				distribuidor, cota);


		List<EstudoCota> listaEstudosCota = this.estudoCotaRepository
				.obterEstudosCotaParaNotaEnvio(idCota, periodo,
						listaIdFornecedores);

		List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(
				listaEstudosCota, cota);

		if (listaItemNotaEnvio.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.ERROR,
					"Não é possível gerar Nota de Envio para a Cota "
							+ cota.getNumeroCota());
		}

		int sequencia = 0;

		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {

			if (itemNotaEnvio.getItemNotaEnvioPK() == null) {
				itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio,
						++sequencia));
			}
		}

		notaEnvio.setListaItemNotaEnvio(listaItemNotaEnvio);

		return notaEnvio;
	}

	private NotaEnvio gerar(Long idCota, Long idRota, String chaveAcesso,
			Integer codigoNaturezaOperacao, String descricaoNaturezaOperacao,
			Date dataEmissao, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores) {

		Distribuidor distribuidor = distribuidorRepository.obter();

		Cota cota = cotaRepository.buscarPorId(idCota);

		List<EstudoCota> listaEstudosCota = this.estudoCotaRepository
				.obterEstudosCotaParaNotaEnvio(idCota, periodo,
						listaIdFornecedores);

		List<ItemNotaEnvio> listaItemNotaEnvio = gerarItensNotaEnvio(
				listaEstudosCota, cota);

		if (listaItemNotaEnvio.isEmpty()) {

			return null;
		}

		if (idRota == null) {

			Long idRoteiro = null;

			List<Roteiro> roteiros = cota.getBox().getRoteirizacao()
					.getRoteiros();

			for (Roteiro r : roteiros) {

				if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {

					idRoteiro = r.getId();
				}
			}

			idRota = (Long) cota.getBox().getRoteirizacao()
					.getRoteiro(idRoteiro).getRotas().get(0).getId();
		}

		ItemNotaEnvioPK itemNotaEnvioPK = listaItemNotaEnvio.get(0)
				.getItemNotaEnvioPK();

		NotaEnvio notaEnvio = (itemNotaEnvioPK == null) ? null
				: itemNotaEnvioPK.getNotaEnvio();

		if (notaEnvio != null)
			return notaEnvio;

		notaEnvio = criarNotaEnvio(idCota, idRota, chaveAcesso,
				codigoNaturezaOperacao, descricaoNaturezaOperacao, dataEmissao,
				distribuidor, cota);

		notaEnvioRepository.adicionar(notaEnvio);

		int sequencia = 0;

		for (ItemNotaEnvio itemNotaEnvio : listaItemNotaEnvio) {
			if(itemNotaEnvio.getItemNotaEnvioPK() == null){
				itemNotaEnvio.setItemNotaEnvioPK(new ItemNotaEnvioPK(notaEnvio,
						++sequencia));

				itemNotaEnvioRepository.adicionar(itemNotaEnvio);
			}

			
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

		// Corrigido devo ao fato da tabela pessoa gravar os documentos com
		// máscara, embora o campo documento da tabela nota_envio esperar apenas
		// 14 caracteres
		String documento = pessoaEmitente.getDocumento().replaceAll(
				"[-+.^:,/]", "");

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
		destinatario.setCodigoBox(cota.getBox().getCodigo());
		destinatario.setNomeBox(cota.getBox().getNome());

		if (idRota != null) {
			Rota rota = rotaRepository.buscarPorId(idRota);
			if (rota == null) {
				throw new ValidacaoException(TipoMensagem.ERROR,
						"Rota não encontrada!");
			}
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
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE()
						.toString().substring(0, 2));
			} else {
				novoEndereco.setCodigoUf(novoEndereco.getCodigoCidadeIBGE()
						.toString());
			}
		}
		enderecoRepository.adicionar(novoEndereco);
		return novoEndereco;
	}

	@Override
	@Transactional
	public List<NotaEnvio> gerarNotasEnvio(FiltroConsultaNotaEnvioDTO filtro,
			List<Long> idCotasSuspensasAusentes) {

		List<NotaEnvio> listaNotaEnvio = new ArrayList<NotaEnvio>();
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(SituacaoCadastro.ATIVO);
		situacoesCadastro.add(SituacaoCadastro.SUSPENSO);

		Set<Long> listaIdCotas = this.cotaRepository.obterIdCotasEntre(
				filtro.getIntervaloCota(), filtro.getIntervaloBox(),
				situacoesCadastro, filtro.getIdRoteiro(), filtro.getIdRota(),
				null, null, null, null);

		if (idCotasSuspensasAusentes != null) {
			listaIdCotas.addAll(idCotasSuspensasAusentes);
		}

		validarRoteirizacaoCota(filtro, listaIdCotas);
		
		for (Long idCota : listaIdCotas) {

			NotaEnvio notaEnvio = this.gerar(idCota, filtro.getIdRota(), null,
					null, null, filtro.getDataEmissao(),
					filtro.getIntervaloMovimento(), filtro.getIdFornecedores());

			if (notaEnvio != null) {
				listaNotaEnvio.add(notaEnvio);
			}
		}

		return listaNotaEnvio;
	}

	private void validarRoteirizacaoCota(FiltroConsultaNotaEnvioDTO filtro,
			Set<Long> listaIdCotas) {
		List<String> cotasSemRoteirizacao = new ArrayList<String>();
		
		for (Long idCota : listaIdCotas) {
			
			Cota cota = cotaRepository.buscarPorId(idCota);

			Long idRota = filtro.getIdRota();

			if (idRota == null) {

				Long idRoteiro = null;

				List<Roteiro> roteiros = cota.getBox().getRoteirizacao()
						.getRoteiros();

				for (Roteiro r : roteiros) {

					if (!r.getTipoRoteiro().equals(TipoRoteiro.ESPECIAL)) {

						idRoteiro = r.getId();
					}
				}

				try {
					idRota = (Long) cota.getBox().getRoteirizacao()
							.getRoteiro(idRoteiro).getRotas().get(0).getId();
				} catch (Exception e) {
					if(cotasSemRoteirizacao.size() == 0) {
						cotasSemRoteirizacao.add("Cota(s) com problemas de Roteirização:");
					}
					StringBuilder cotaSemRoteirizacao = new StringBuilder("Cota: "+ cota.getNumeroCota() +" / "+ cota.getPessoa().getNome());
					cotasSemRoteirizacao.add(cotaSemRoteirizacao.toString());
				}
			}
			
		}
		
		if(cotasSemRoteirizacao.size() > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, cotasSemRoteirizacao);
		}
	}

}