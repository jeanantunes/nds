package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ChamadaoRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ChamadaoService;
import br.com.abril.nds.service.CotaService;

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
	
	private Long obterTotalConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterTotalConsignadosParaChamadao(filtro);
	}
	
	private ResumoConsignadoCotaChamadaoDTO obterResumoConsignados(FiltroChamadaoDTO filtro) {
		
		return this.chamadaoRepository.obterResumoConsignadosParaChamadao(filtro);
	}
	
	@Override
	@Transactional
	public void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
								  FiltroChamadaoDTO filtro,
								  boolean chamarTodos, Usuario usuario) {
		
		Date dataChamadao = filtro.getDataChamadao();
		
		if (chamarTodos) {
			
			listaChamadao =
				this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
		}
		
		ProdutoEdicao produtoEdicao = null;
		
		ChamadaEncalhe chamadaEncalhe = null;
		
		ChamadaEncalheCota chamadaEncalheCota = null;
		
		EstoqueProdutoCota estoqueProdutoCota = null;
		
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaChamadao) {
			
			produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				consignadoCotaChamadao.getCodigoProduto(), consignadoCotaChamadao.getNumeroEdicao());
			
			chamadaEncalhe =
				chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					produtoEdicao, dataChamadao, TipoChamadaEncalhe.CHAMADAO);
			
			if (chamadaEncalhe == null) {
				
				chamadaEncalhe = new ChamadaEncalhe();
			
				chamadaEncalhe.setDataRecolhimento(dataChamadao);
				chamadaEncalhe.setProdutoEdicao(produtoEdicao);
				chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.CHAMADAO);
				
				chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
			}
			
			chamadaEncalheCota = new ChamadaEncalheCota();
			
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
			chamadaEncalheCota.setFechado(Boolean.FALSE);
			chamadaEncalheCota.setCota(cota);
			
			estoqueProdutoCota =
				estoqueProdutoCotaRepository.buscarEstoquePorProdutEdicaoECota(
					produtoEdicao.getId(), cota.getId());
			
			BigInteger qtdPrevista = BigInteger.ZERO;
			
			if (estoqueProdutoCota != null) {
				
				qtdPrevista = estoqueProdutoCota.getQtdeRecebida().subtract(
					estoqueProdutoCota.getQtdeDevolvida());
			}
			
			chamadaEncalheCota.setQtdePrevista(qtdPrevista);
			
			chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
		}
		
		this.verificarSuspenderCota(filtro, cota, usuario);
	}
	
	/**
	 * Verifica se todo o consignado da cota foi chamado 
	 * e dependendo do resultado suspende a cota.
	 * 
	 * @param filtro - filtro para a pesquisa
	 * @param cota - cota
	 * @param usuario - usuário
	 */
	private void verificarSuspenderCota(FiltroChamadaoDTO filtro, Cota cota, Usuario usuario) {
		
		if(!SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
		
			filtro.setIdFornecedor(null);
			
			List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadao =
				this.chamadaoRepository.obterConsignadosParaChamadao(filtro);
			
			if (listaConsignadoCotaChamadao == null || listaConsignadoCotaChamadao.isEmpty()) {
				
				this.suspenderCota(cota.getId(), usuario);
			}
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
