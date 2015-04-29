package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.CotaReparteDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ChamadaoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação de serviços referentes
 * ao chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Service
public class ChamadaoServiceImpl implements ChamadaoService {

	@Autowired
	protected ChamadaoRepository chamadaoRepository;

	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	protected ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	protected CotaRepository cotaRepository;
	
	@Autowired 
	protected DistribuidorService distribuidorService;
	
	@Autowired
	protected EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	protected MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	protected ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	protected CotaService cotaService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private SituacaoCotaService situacaoCotaService;
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaChamadaoDTO obterConsignados(FiltroChamadaoDTO filtro) {
		
		ConsultaChamadaoDTO consultaChamadaoDTO = new ConsultaChamadaoDTO();
		
		consultaChamadaoDTO.setListaConsignadoCotaChamadaoDTO(this.chamadaoRepository.obterConsignadosParaChamadao(filtro));
		
		consultaChamadaoDTO.setResumoConsignadoCotaChamadao(this.obterResumoConsignados(filtro));
		
		Long quantidadeTotalConsignados = this.obterTotalConsignados(filtro);

		consultaChamadaoDTO.setQuantidadeTotalConsignados(quantidadeTotalConsignados);
		
		if (consultaChamadaoDTO.getResumoConsignadoCotaChamadao() != null) {
			
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao().setQtdProdutosTotal(quantidadeTotalConsignados);
		}
		
		return consultaChamadaoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaChamadaoDTO obterConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		ConsultaChamadaoDTO consultaChamadaoDTO = new ConsultaChamadaoDTO();
		
		consultaChamadaoDTO.setListaConsignadoCotaChamadaoDTO(this.chamadaoRepository.obterConsignadosComChamadao(filtro));
		
		consultaChamadaoDTO.setResumoConsignadoCotaChamadao(this.obterResumoChamadaEncalhe(filtro));
		
		Long quantidadeTotalChamadaEncalhe = this.obterTotalChamadaEncalhe(filtro);

		consultaChamadaoDTO.setQuantidadeTotalConsignados(quantidadeTotalChamadaEncalhe);
		
		if (consultaChamadaoDTO.getResumoConsignadoCotaChamadao() != null) {
			
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao()
				.setQtdProdutosTotal(quantidadeTotalChamadaEncalhe);
		}
		
		return consultaChamadaoDTO;
	}
	
	private Long obterTotalConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterTotalConsignadosParaChamadao(filtro);
	}
	
	private Long obterTotalChamadaEncalhe(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterTotalConsignadosComChamadao(filtro);
	}
	
	private ResumoConsignadoCotaChamadaoDTO obterResumoConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterResumoConsignadosParaChamadao(filtro);
	}
	
	private ResumoConsignadoCotaChamadaoDTO obterResumoChamadaEncalhe(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterResumoConsignadosComChamadao(filtro);
	}
	
	@Override
	@Transactional
	public void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
								  FiltroChamadaoDTO filtro,
								  boolean chamarTodos,
								  List<Long> idsIgnorados,
								  Usuario usuario,
								  Date novaDataChamadao) {
		
		Date dataChamadao = filtro.getDataChamadao();
		
		Integer numeroCota = filtro.getNumeroCota();
		
		if (chamarTodos) {
			
			if (filtro.isChamadaEncalhe()) {
		
				listaChamadao = this.chamadaoRepository.obterConsignadosComChamadao(filtro);
				
			} else {
			
				listaChamadao = this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
			}
		} else {
			for(ConsignadoCotaChamadaoDTO cc : listaChamadao) {
				
				ProdutoEdicao pe = null;
				if(cc.getCodigoProduto() != null && cc.getNumeroEdicao() != null) {
					pe = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(cc.getCodigoProduto(), cc.getNumeroEdicao());
				}
				
				cc.setNomeProduto((pe != null && pe.getProduto() != null) ? pe.getProduto().getNome() : "");
			}
		}
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		listaChamadao = (List<ConsignadoCotaChamadaoDTO>) this.ordenarEmMemoria(listaChamadao, Ordenacao.ASC, "numeroEdicao");
		listaChamadao = (List<ConsignadoCotaChamadaoDTO>) this.ordenarEmMemoria(listaChamadao, Ordenacao.ASC, "nomeProduto");		
				
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaChamadao) {
			
			if (idsIgnorados != null) {
				if (idsIgnorados.contains(consignadoCotaChamadao.getIdLancamento())) {
	
					continue;
				}
			}
			
			if(consignadoCotaChamadao.getDataRecolhimento().equals(novaDataChamadao)) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Já existe Chamada de encalhe para esta data!");
			}
			
			if (filtro.isChamadaEncalhe()) {
				
				this.alterarChamadao(consignadoCotaChamadao, consignadoCotaChamadao.getDataRecolhimento(), novaDataChamadao, cota);
				
			} else {
				
				this.gerarChamadaEncalhe(consignadoCotaChamadao, dataChamadao, cota);
			}
		}
		
		this.tratarConfirmacaoChamadao(filtro, cota, usuario);
	}
	
	/**
	 * Efetua a ordenação de uma lista em memória.
	 * 
	 * @param listaAOrdenar - Lista que será ordenada.
	 * @param ordenacao - Define se a ordenação será ascendente ou descendente.
	 * @param nomeAtributoOrdenacao - nome do atributo que será usada para a ordenação.
	 * 
	 * @return Lista ordenada
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> Collection<T> ordenarEmMemoria(List<T> listaAOrdenar,
															  Ordenacao ordenacao,
															  String nomeAtributoOrdenacao) {
		
		if (listaAOrdenar == null || listaAOrdenar.isEmpty()) {
			
			return listaAOrdenar;
		}
		
		if (ordenacao == null) {
			
			throw new IllegalArgumentException("Tipo de ordenação nulo!");
		}
		
		if (nomeAtributoOrdenacao == null) {
			
			throw new IllegalArgumentException("Nome do atributo para ordenação nulo!");
		}
		
		Collections.sort(listaAOrdenar, new BeanComparator(nomeAtributoOrdenacao, new NullComparator()));

		if (Ordenacao.DESC.equals(ordenacao)) {

			Collections.reverse(listaAOrdenar);
		}
		
		return listaAOrdenar;
	}
	
	@Transactional
	@Override
	public void cancelarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
								 FiltroChamadaoDTO filtro,
								 boolean chamarTodos) {
		
		if (chamarTodos) {
			
			listaChamadao =
				this.chamadaoRepository.obterConsignadosComChamadao(filtro);
		}
		
		Cota cota = cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaChamadao) {
			
			ProdutoEdicao produtoEdicao =
				this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
					consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
			
			ChamadaEncalhe chamadaEncalhe =
				this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					produtoEdicao, consignadoCotaChamadao.getDataRecolhimento(), TipoChamadaEncalhe.CHAMADAO);
			
			ChamadaEncalheCota chamadaEncalheCotaExcluir =
				this.obterChamadaEncalheCota(cota, chamadaEncalhe);
			
			if(chamadaEncalheCotaExcluir!= null){
				this.chamadaEncalheCotaRepository.remover(chamadaEncalheCotaExcluir);
			}
			
			this.verificarRemoverChamadaEncalhe(chamadaEncalhe, chamadaEncalheCotaExcluir);
		}
		
		this.atualizarStatusCotaParaAtivo(cota); 
	}
	
	/**
	 * Atualiza a situação de cadastro da cota para Ativo e gera historico de alteração da situação de cadastro da cota
	 * @param cota
	 */
	private void atualizarStatusCotaParaAtivo(Cota cota){
		
		Date dataDeOperacao = distribuidorService.obterDataOperacaoDistribuidor();		
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setDataEdicao(new Date());
		historico.setNovaSituacao(SituacaoCadastro.ATIVO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuarioService.getUsuarioLogado());
		historico.setMotivo(MotivoAlteracaoSituacao.CHAMADAO);
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);		
		historico.setDataInicioValidade(dataDeOperacao);
		
		situacaoCotaService.atualizarSituacaoCota(historico, dataDeOperacao);
	}
	
	/**
	 * Gera chamadas de encalhe.
	 * 
	 * @param consignadoCotaChamadao - DTO que contém os consignados da cota
	 * @param dataChamadao - data do chamadão
	 * @param cota - cota
	 */
	private void gerarChamadaEncalhe(ConsignadoCotaChamadaoDTO consignadoCotaChamadao, Date dataChamadao, Cota cota) {
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
		
		Long idCota = cota.getId();
		Long idProdutoEdicao = produtoEdicao.getId();
		
		if (this.chamadaEncalheCotaRepository.existeChamadaEncalheCota(idCota, idProdutoEdicao, false, dataChamadao)) {
			
			this.tratarChamadaEncalheCotaExistente(idCota, idProdutoEdicao, dataChamadao);
		}
		
		ChamadaEncalhe chamadaEncalhe = this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(produtoEdicao, dataChamadao, TipoChamadaEncalhe.CHAMADAO);
		
		if (chamadaEncalhe == null) {
			chamadaEncalhe = this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(produtoEdicao, dataChamadao, TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		}
		
		if (chamadaEncalhe == null) {
			
			Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataChamadao);
			
			chamadaEncalhe = new ChamadaEncalhe();
		
			chamadaEncalhe.setDataRecolhimento(dataChamadao);
			chamadaEncalhe.setProdutoEdicao(produtoEdicao);
			chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
			chamadaEncalhe.setSequencia(++sequencia);
		}
		
		Set<Lancamento> lancamentos = chamadaEncalhe.getLancamentos();
		if (lancamentos == null || lancamentos.isEmpty()) {
			
			Lancamento lancamento = this.lancamentoRepository.buscarPorId(consignadoCotaChamadao.getIdLancamento());
			if(lancamento != null) {
				
				lancamentos = new HashSet<Lancamento>(
						lancamentoRepository.obterLancamentosProdutoEdicaoPorDataLancamentoOuDataRecolhimento(produtoEdicao, null, lancamento.getDataRecolhimentoPrevista()));
			}
		}
		
		chamadaEncalhe.setLancamentos(lancamentos);
		
		chamadaEncalhe = this.chamadaEncalheRepository.merge(chamadaEncalhe);
		
		BigInteger qtdPrevista = BigInteger.ZERO;
		if(consignadoCotaChamadao != null && consignadoCotaChamadao.getReparte() != null) {
			
			qtdPrevista = consignadoCotaChamadao.getReparte();
		} else {
			
			List<Lancamento> lancamentosEdicao = lancamentoRepository.obterLancamentosDaEdicao(produtoEdicao.getId());
			Set<Long> idsLancamentos = new HashSet<Long>();
			if(lancamentosEdicao != null) {
				for(Lancamento l : lancamentosEdicao) {
					idsLancamentos.add(l.getId());
				}
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, String.format("Lancamento não encontrado para %s.", produtoEdicao.toString()));
			}
			
			List<CotaReparteDTO> cotasRepartes = movimentoEstoqueCotaRepository.obterReparte(idsLancamentos, cota.getId());
			
			if(cotasRepartes != null && !cotasRepartes.isEmpty()) {
				for(CotaReparteDTO cr : cotasRepartes) {
					qtdPrevista = qtdPrevista.add(cr.getReparte());
				}
			}
		}

		if(BigInteger.ZERO.compareTo(qtdPrevista) < 0) {

			ChamadaEncalheCota chamadaEncalheCota = new ChamadaEncalheCota();
			
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
			chamadaEncalheCota.setFechado(Boolean.FALSE);
			chamadaEncalheCota.setCota(cota);
			
			
			chamadaEncalheCota.setQtdePrevista(qtdPrevista);
			
			this.chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
			
		}
		
	}

	private void tratarChamadaEncalheCotaExistente(Long idCota, Long idProdutoEdicao, Date dataChamadao) {
		
		ChamadaEncalheCota chamadaEncalheCota =
			this.chamadaEncalheCotaRepository.obterChamadaEncalheCota(idCota, idProdutoEdicao, dataChamadao);
		
		if (chamadaEncalheCota != null) {
			
			ChamadaEncalhe chamadaEncalhe = chamadaEncalheCota.getChamadaEncalhe();
			
			this.chamadaEncalheCotaRepository.remover(chamadaEncalheCota);
			
			if(chamadaEncalhe.getChamadaEncalheCotas().isEmpty()){
				chamadaEncalheRepository.remover(chamadaEncalhe);
			}
		}		
	}

	/**
	 * Altera a programação do chamadão.
	 * 
	 * @param consignadoCotaChamadao - DTO que contém os consignados da cota
	 * @param dataChamadao - data do chamadão
	 * @param novaDataChamadao - nova data do chamadão
	 * @param cota - cota
	 */
	private void alterarChamadao(ConsignadoCotaChamadaoDTO consignadoCotaChamadao,
								 Date dataChamadao, Date novaDataChamadao,
								 Cota cota) {
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
		
		ChamadaEncalhe chamadaEncalheAntiga =
			this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
				produtoEdicao, dataChamadao, TipoChamadaEncalhe.CHAMADAO);
		
		if (chamadaEncalheAntiga == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Chamada de encalhe não encontrada!");
		}
		
		Set<Lancamento> lancamentos = this.getLancamentos(chamadaEncalheAntiga.getLancamentos());
		
		ChamadaEncalheCota chamadaEncalheCotaAlterar = this.obterChamadaEncalheCota(cota, chamadaEncalheAntiga);
		
		ChamadaEncalhe chamadaEncalheNova =
			this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
				produtoEdicao, novaDataChamadao, TipoChamadaEncalhe.CHAMADAO);
			
		if (chamadaEncalheNova == null) {
			
			Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(novaDataChamadao);
			
			chamadaEncalheNova = new ChamadaEncalhe();
			chamadaEncalheNova.setSequencia(++sequencia);
		}
		
		chamadaEncalheNova.setDataRecolhimento(novaDataChamadao);
		chamadaEncalheNova.setProdutoEdicao(produtoEdicao);
		chamadaEncalheNova.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
		chamadaEncalheNova.setLancamentos(lancamentos);
		
		chamadaEncalheNova = this.chamadaEncalheRepository.merge(chamadaEncalheNova);
		
		chamadaEncalheCotaAlterar.setChamadaEncalhe(chamadaEncalheNova);
		
		this.chamadaEncalheCotaRepository.merge(chamadaEncalheCotaAlterar);
		
		this.verificarRemoverChamadaEncalhe(chamadaEncalheAntiga, chamadaEncalheCotaAlterar);
	}

	/**
	 * Monta um novo Set de lançamentos de acordo com os lançamentos informados.
	 * 
	 * @param lancamentosAntigos
	 * 
	 * @return Set de lançamentos
	 */
	private Set<Lancamento> getLancamentos(Set<Lancamento> lancamentosAntigos) {
		
		Set<Lancamento> lancamentosNovos = new HashSet<Lancamento>();
		
		for (Lancamento lancamento : lancamentosAntigos) {
			
			lancamentosNovos.add(lancamento);
		}
		
		return lancamentosNovos;
	}

	/**
	 * Obtém a chamada de encalhe da cota a partir da chamada de encalhe informada.
	 * 
	 * @param cota - cota
	 * @param chamadaEncalhe - chamada de encalhe
	 * 
	 * @return {@link ChamadaEncalheCota}
	 */
	private ChamadaEncalheCota obterChamadaEncalheCota(Cota cota,
													   ChamadaEncalhe chamadaEncalhe) {
		
		if(chamadaEncalhe!= null && chamadaEncalhe.getChamadaEncalheCotas()!= null){
			
			Set<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalhe.getChamadaEncalheCotas();
			
			ChamadaEncalheCota chamadaEncalheCotaAlterar = null;
			
			for (ChamadaEncalheCota chamadaEncalheCota : listaChamadaEncalheCota) {
				
				if (chamadaEncalheCota.getCota().getId().equals(cota.getId())) {
					
					chamadaEncalheCotaAlterar = chamadaEncalheCota;
				}
			}
			
			return chamadaEncalheCotaAlterar;
		}
		
		return null;
		
	}

	/**
	 * Verifica se existe chamadas de encalhe para cotas.
	 * Se não existir, remove a chamada de encalhe
	 * 
	 * @param chamadaEncalhe - chamada de encalhe
	 * @param chamadaEncalheCota - chamada de encalhe cota
	 */
	private void verificarRemoverChamadaEncalhe(ChamadaEncalhe chamadaEncalhe,
									   			ChamadaEncalheCota chamadaEncalheCota) {
		
		if(chamadaEncalhe!= null && chamadaEncalhe.getChamadaEncalheCotas()!= null){
			
			Set<ChamadaEncalheCota> chamadaEncalheCotas = chamadaEncalhe.getChamadaEncalheCotas();
			
			chamadaEncalheCotas.remove(chamadaEncalheCota);
			
			if (chamadaEncalheCotas.isEmpty()) {
				
				this.chamadaEncalheRepository.remover(chamadaEncalhe);
			}
		}
	}
	
	/**
	 * Realiza tratamento da confirmação do chamadão para
	 * suspender uma cota e desassociar um fornecedor de uma cota.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * @param cota - cota
	 * @param usuario - usuário
	 */
	private void tratarConfirmacaoChamadao(FiltroChamadaoDTO filtro, Cota cota, Usuario usuario) {
		
		this.verificarSuspenderCota(filtro, cota, usuario);
		
		this.verificarDesassociarFornecedorCota(filtro, cota);
	}

	/**
	 * Verifica se todo o consignado da cota foi chamado.
	 * Caso positivo, suspende a cota.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * @param cota - cota
	 * @param usuario - usuário
	 */
	private void verificarSuspenderCota(FiltroChamadaoDTO filtro, Cota cota, Usuario usuario) {
		
		if(!SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())
				&& filtro.getIdFornecedor() == null) {
			
			List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadao =
				this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
			
			if (listaConsignadoCotaChamadao == null || listaConsignadoCotaChamadao.isEmpty()) {
				
				this.suspenderCota(cota.getId(), usuario);
			}
		}
	}
	
	/**
	 * Verifica se todo o consignado da cota de um fornecedor foi chamado.
	 * Caso positivo, desassocia o fornecedor da cota.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * @param cota - cota
	 */
	private void verificarDesassociarFornecedorCota(FiltroChamadaoDTO filtro, Cota cota) {
		
		if (filtro.getIdFornecedor() != null) {
			
			List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadao =
				this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
			
			if (listaConsignadoCotaChamadao == null || listaConsignadoCotaChamadao.isEmpty()) {
				
				this.desassociarFornecedorDaCota(filtro.getIdFornecedor(), cota);
			}
		}
	}
	
	/**
	 * Desassocia um fornecedor da cota informada.
	 * 
	 * @param idFornecedor - identificador do fornecedor
	 * @param cota - cota
	 */
	private void desassociarFornecedorDaCota(Long idFornecedor, Cota cota) {
		
		Set<Fornecedor> fornecedores = cota.getFornecedores();
		
		Fornecedor fornecedorRemover = null;
		
		for (Fornecedor fornecedor : fornecedores) {
			
			if (fornecedor.getId().equals(idFornecedor)) {
				
				fornecedorRemover = fornecedor;
			}
		}
		
		if (fornecedorRemover != null) {
			
			fornecedores.remove(fornecedorRemover);
		}
	}

	/**
	 * Suspende a cota.
	 * 
	 * @param idCota - identificador da cota
	 * @param usuario - usuário
	 */
	private void suspenderCota(Long idCota, Usuario usuario) {
		
		Date dataInicioValidade = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		cotaService.suspenderCota(idCota, usuario, dataInicioValidade, MotivoAlteracaoSituacao.CHAMADAO);
	}
	
}