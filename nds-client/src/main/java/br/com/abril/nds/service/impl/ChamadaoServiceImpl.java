package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
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
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ChamadaoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.TipoMensagem;

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
	protected EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	protected ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	protected CotaService cotaService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaChamadaoDTO obterConsignados(FiltroChamadaoDTO filtro) {
		
		ConsultaChamadaoDTO consultaChamadaoDTO = new ConsultaChamadaoDTO();
		
		consultaChamadaoDTO.setListaConsignadoCotaChamadaoDTO(
			this.chamadaoRepository.obterConsignadosParaChamadao(filtro));
		
		consultaChamadaoDTO.setResumoConsignadoCotaChamadao(this.obterResumoConsignados(filtro));
		
		Long quantidadeTotalConsignados = this.obterTotalConsignados(filtro);

		consultaChamadaoDTO.setQuantidadeTotalConsignados(quantidadeTotalConsignados);
		
		if (consultaChamadaoDTO.getResumoConsignadoCotaChamadao() != null) {
			
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao()
				.setQtdProdutosTotal(quantidadeTotalConsignados);
		}
		
		return consultaChamadaoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ConsultaChamadaoDTO obterConsignadosComChamadao(FiltroChamadaoDTO filtro) {
		
		ConsultaChamadaoDTO consultaChamadaoDTO = new ConsultaChamadaoDTO();
		
		consultaChamadaoDTO.setListaConsignadoCotaChamadaoDTO(
			this.chamadaoRepository.obterConsignadosComChamadao(filtro));
		
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
								  Usuario usuario,
								  Date novaDataChamadao) {
		
		Date dataChamadao = filtro.getDataChamadao();
		
		Integer numeroCota = filtro.getNumeroCota();
		
		if (chamarTodos) {
			
			if (filtro.isChamadaEncalhe()) {
		
				listaChamadao =
					this.chamadaoRepository.obterConsignadosComChamadao(filtro);
				
			} else {
			
				listaChamadao =
					this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
			}
		}
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaChamadao) {
			
			if (filtro.isChamadaEncalhe()) {
				
				this.alterarChamadao(
					consignadoCotaChamadao, dataChamadao, novaDataChamadao, cota);
				
			} else {
				
				this.gerarChamadaEncalhe(consignadoCotaChamadao, dataChamadao, cota);
			}
		}
		
		this.tratarConfirmacaoChamadao(filtro, cota, usuario);
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
		
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaChamadao) {
			
			ProdutoEdicao produtoEdicao =
				this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
					consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
			
			ChamadaEncalhe chamadaEncalhe =
				this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					produtoEdicao, filtro.getDataChamadao(), TipoChamadaEncalhe.CHAMADAO);
			
			ChamadaEncalheCota chamadaEncalheCotaExcluir =
				this.obterChamadaEncalheCota(cota, chamadaEncalhe);
			
			this.chamadaEncalheCotaRepository.remover(chamadaEncalheCotaExcluir);
			
			this.verificarRemoverChamadaEncalhe(chamadaEncalhe, chamadaEncalheCotaExcluir);
		}
		
		this.cotaRepository.ativarCota(filtro.getNumeroCota());
	}
	
	/**
	 * Gera chamadas de encalhe.
	 * 
	 * @param consignadoCotaChamadao - DTO que contém os consignados da cota
	 * @param dataChamadao - data do chamadão
	 * @param cota - cota
	 */
	private void gerarChamadaEncalhe(ConsignadoCotaChamadaoDTO consignadoCotaChamadao,
									 Date dataChamadao, Cota cota) {
		
		ProdutoEdicao produtoEdicao =
			this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
		
		ChamadaEncalhe chamadaEncalhe =
			this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
				produtoEdicao, dataChamadao, TipoChamadaEncalhe.CHAMADAO);
		
		if (chamadaEncalhe == null) {
			
			chamadaEncalhe = new ChamadaEncalhe();
		
			chamadaEncalhe.setDataRecolhimento(dataChamadao);
			chamadaEncalhe.setProdutoEdicao(produtoEdicao);
			chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
			
		}
		
		Set<Lancamento> lancamentos = chamadaEncalhe.getLancamentos();
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(consignadoCotaChamadao.getIdLancamento());
		
		if (lancamentos == null || lancamentos.isEmpty()) {
			lancamentos = new HashSet<Lancamento>();
		}
		
		lancamentos.add(lancamento);
		
		chamadaEncalhe.setLancamentos(lancamentos);
		
		chamadaEncalhe = this.chamadaEncalheRepository.merge(chamadaEncalhe);
		
		
		ChamadaEncalheCota chamadaEncalheCota = new ChamadaEncalheCota();
		
		chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		chamadaEncalheCota.setFechado(Boolean.FALSE);
		chamadaEncalheCota.setCota(cota);
		
		EstoqueProdutoCota estoqueProdutoCota =
			this.estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(
				produtoEdicao.getId(), cota.getId());
		
		BigInteger qtdPrevista = BigInteger.ZERO;
		
		if (estoqueProdutoCota != null) {
			
			qtdPrevista = estoqueProdutoCota.getQtdeRecebida().subtract(
				estoqueProdutoCota.getQtdeDevolvida());
		}
		
		chamadaEncalheCota.setQtdePrevista(qtdPrevista);
		
		this.chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
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
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Chamada de encalhe não encontrada!");
		}
		
		Set<Lancamento> lancamentos = this.getLancamentos(chamadaEncalheAntiga.getLancamentos());
		
		ChamadaEncalheCota chamadaEncalheCotaAlterar =
			this.obterChamadaEncalheCota(cota, chamadaEncalheAntiga);
		
		ChamadaEncalhe chamadaEncalheNova =
			this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
				produtoEdicao, novaDataChamadao, TipoChamadaEncalhe.CHAMADAO);
			
		if (chamadaEncalheNova == null) {
			
			chamadaEncalheNova = new ChamadaEncalhe();
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
		
		Set<ChamadaEncalheCota> listaChamadaEncalheCota = chamadaEncalhe.getChamadaEncalheCotas();
		
		ChamadaEncalheCota chamadaEncalheCotaAlterar = null;
		
		for (ChamadaEncalheCota chamadaEncalheCota : listaChamadaEncalheCota) {
			
			if (chamadaEncalheCota.getCota().getId().equals(cota.getId())) {
				
				chamadaEncalheCotaAlterar = chamadaEncalheCota;
			}
		}
		
		return chamadaEncalheCotaAlterar;
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
		
		Set<ChamadaEncalheCota> chamadaEncalheCotas = chamadaEncalhe.getChamadaEncalheCotas();
		
		chamadaEncalheCotas.remove(chamadaEncalheCota);
		
		if (chamadaEncalheCotas.isEmpty()) {
			
			this.chamadaEncalheRepository.remover(chamadaEncalhe);
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
		
		cotaService.suspenderCota(idCota, usuario, MotivoAlteracaoSituacao.CHAMADAO);
	}
	
}
