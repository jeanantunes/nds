package br.com.abril.nds.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroModalDistribuicao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.util.CurrencyUtil;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Service
public class AlteracaoCotaServiceImpl implements AlteracaoCotaService {

	@Autowired
	private AlteracaoCotaRepository alteracaoCotaRepository;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;

	@Override
	@Transactional(readOnly = true)
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
		return alteracaoCotaRepository.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public int contarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
		return alteracaoCotaRepository.contarAlteracaoCota(filtroAlteracaoCotaDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public FiltroAlteracaoCotaDTO preencherFiltroAlteracaoCotaDTO(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {

		return filtroAlteracaoCotaDTO;
	}

	@Override
	@Transactional
	public void salvarAlteracoes(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {

		boolean isCotasSelecionadas = filtroAlteracaoCotaDTO.getListaLinhaSelecao().size() > 1;

		for (Long idCota : filtroAlteracaoCotaDTO.getListaLinhaSelecao()) {

			// Encontra Cota a Ser Alterada
			Cota cota = cotaService.obterPorId(idCota);

			// ****FORNECEDORES****//
			atribuirValoresFornecedor(filtroAlteracaoCotaDTO, cota, isCotasSelecionadas);

			// ****FINANCEIRO****//
			atribuirValoresFinanceiro(filtroAlteracaoCotaDTO, cota);

			// ****DISTRIBUICAO****//
			atribuirValoresDistribuicao(filtroAlteracaoCotaDTO, cota);

			// --Tipo Entrega
			atribuirValoresTipoEntregaDistribuicao(filtroAlteracaoCotaDTO, cota);

			// --Emissao Documentos
			atribuirValoresEmissaoDocumentosDistribuicao(filtroAlteracaoCotaDTO, cota);

			cotaService.alterarCota(cota);
			
			this.salvarParametrosCobrancaDistribuicaoCota(filtroAlteracaoCotaDTO,cota);

			parametroCobrancaCotaService.alterarParametro(cota.getParametroCobranca());
		}
	}

	private void atribuirValoresFornecedor(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota,
			boolean isCotasSelecionadas) {

		// Altera Fornecedores da Cota
		Set<Fornecedor> fornecedoresCota = new HashSet<Fornecedor>();
		for (Long id : filtroAlteracaoCotaDTO.getFiltroModalFornecedor()
				.getListaFornecedoresSelecionados()) {
			fornecedoresCota.add(fornecedorService.obterFornecedorPorId(id));
		}

		if (isCotasSelecionadas) {
			if (!fornecedoresCota.isEmpty()) {
				cota.getFornecedores().addAll(fornecedoresCota);
			}
		} else {
			cota.setFornecedores(fornecedoresCota);
		}

	}

	/**
	 * Atribui valores dos campos relacionados a parte Financeiro da Cota
	 * 
	 * @param filtroAlteracaoCotaDTO
	 * @param cota
	 */
	private void atribuirValoresFinanceiro(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {

		// Sugere Suspensao
		cota.setSugereSuspensao(filtroAlteracaoCotaDTO
				.getFiltroModalFinanceiro().getIsSugereSuspensao());

		if (cota.getParametroCobranca() == null) {
			cota.setParametroCobranca(new ParametroCobrancaCota());
			cota.getParametroCobranca().setCota(cota);
		}
		// Fator Vencimento
		if (filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getIdVencimento() != null) {
			cota.getParametroCobranca().setFatorVencimento(
					filtroAlteracaoCotaDTO.getFiltroModalFinanceiro()
							.getIdVencimento());
		}

		try {
			// Valor Minimo
			if (filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrMinimo() != null
					&& !filtroAlteracaoCotaDTO.getFiltroModalFinanceiro()
							.getVrMinimo().isEmpty()) {

				cota.setValorMinimoCobranca(
						CurrencyUtil.converterValor(filtroAlteracaoCotaDTO
								.getFiltroModalFinanceiro().getVrMinimo()));
			}

			// Suspensao = true -> Cria Politica de Suspensao
			if (cota.isSugereSuspensao()) {
				
				PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
				
				if (filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getQtdDividaEmAberto() != null) {
					
					politicaSuspensao.setNumeroAcumuloDivida(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getQtdDividaEmAberto());
				}
				if (filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrDividaEmAberto() != null) {
					
					politicaSuspensao.setValor(CurrencyUtil.converterValor(filtroAlteracaoCotaDTO.getFiltroModalFinanceiro().getVrDividaEmAberto()));
				}
				
				cota.setPoliticaSuspensao(politicaSuspensao);
			}

		} catch (NumberFormatException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Valor inválido");
		}
	}

	/**
	 * Atribui valores dos campos especificos relacionados com aba de
	 * Distribuição
	 * 
	 * @param filtroAlteracaoCotaDTO
	 * @param cota
	 */
	private void atribuirValoresDistribuicao(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {

		if (cota.getParametroDistribuicao() == null) {
			cota.setParametroDistribuicao(new ParametroDistribuicaoCota());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmAssitPromoComercial() != null
				&& !filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmAssitPromoComercial().isEmpty()) {
			cota.getParametroDistribuicao().setAssistenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmAssitPromoComercial());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmGerenteComercial() != null
				&& !filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmGerenteComercial().isEmpty()) {
			cota.getParametroDistribuicao().setGerenteComercial(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getNmGerenteComercial());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getObservacao() != null
				&& !filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getObservacao().isEmpty()) {
			cota.getParametroDistribuicao().setObservacao(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getObservacao());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRepartePontoVenda()) {
			cota.getParametroDistribuicao().setRepartePorPontoVenda(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRepartePontoVenda());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isSolicitacaoNumAtrasoInternet()) {
			cota.getParametroDistribuicao().setSolicitaNumAtras(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isSolicitacaoNumAtrasoInternet());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRecebeRecolheProdutosParciais()) {
			cota.getParametroDistribuicao().setRecebeRecolheParciais(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRecebeRecolheProdutosParciais());
		}
		
		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRecebeComplementar() && cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL)) {
			cota.getParametroDistribuicao().setRecebeComplementar(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().isRecebeComplementar());
		}
	}

	/**
	 * Atribui valores dos campos relacionados com Tipo de Entrega
	 * 
	 * @param filtroAlteracaoCotaDTO
	 * @param cota
	 */
	private void atribuirValoresTipoEntregaDistribuicao(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {
		// --Tipo Entrega
		DescricaoTipoEntrega descricaoTipoEntrega = 
				filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getDescricaoTipoEntrega();

		if (descricaoTipoEntrega != null) {
			cota.getParametroDistribuicao().setDescricaoTipoEntrega(descricaoTipoEntrega);
		}

		if (descricaoTipoEntrega != null) {
			
			if (descricaoTipoEntrega.equals(DescricaoTipoEntrega.ENTREGA_EM_BANCA)) {
				
				cota.getParametroDistribuicao().setUtilizaTermoAdesao(
						filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
								.isTermoAdesao());
				cota.getParametroDistribuicao().setTermoAdesaoRecebido(
						filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
								.isTermoAdesaoRecebido());
			} else if(descricaoTipoEntrega.equals(DescricaoTipoEntrega.ENTREGADOR)) {
				
				cota.getParametroDistribuicao().setUtilizaProcuracao(
						filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
								.isProcuracao());
				cota.getParametroDistribuicao().setProcuracaoRecebida(
						filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
								.isProcuracaoRecebida());
			}			
		}
		
		cota.getParametroDistribuicao().setInicioPeriodoCarencia(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getCarenciaInicio());
		cota.getParametroDistribuicao().setFimPeriodoCarencia(filtroAlteracaoCotaDTO.getFiltroModalDistribuicao().getCarenciaFim());
	}

	private void salvarParametrosCobrancaDistribuicaoCota(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {
		
		FiltroModalDistribuicao distribuicao = filtroAlteracaoCotaDTO.getFiltroModalDistribuicao();
		
		if(distribuicao!= null && distribuicao.getDescricaoTipoEntrega()!= null){

			DistribuicaoDTO distribuicaoDTO = new DistribuicaoDTO();
			
			distribuicaoDTO.setBaseCalculo(distribuicao.getBaseCalculo());
			distribuicaoDTO.setDiaCobranca(distribuicao.getDiaCobranca());
			distribuicaoDTO.setDescricaoTipoEntrega(distribuicao.getDescricaoTipoEntrega());
			distribuicaoDTO.setDiaSemanaCobranca(distribuicao.getDiaSemanaCobranca());
			distribuicaoDTO.setModalidadeCobranca(distribuicao.getModalidadeCobranca());
			distribuicaoDTO.setPercentualFaturamento(distribuicao.getPercentualFaturamento());
			distribuicaoDTO.setTaxaFixa(distribuicao.getTaxaFixa());
			distribuicaoDTO.setPeriodicidadeCobranca(distribuicao.getPeriodicidadeCobranca());
			
			cotaService.processarParametrosCobrancaDistribuicao(distribuicaoDTO, cota);
		}
	}

	/**
	 * Atribui os valores dos campos relacionados a Emissão de Documentos (se
	 * valor igual a falso não será alterado)
	 * 
	 * @param filtroAlteracaoCotaDTO
	 * @param cota
	 */
	private void atribuirValoresEmissaoDocumentosDistribuicao(
			FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO, Cota cota) {

		// --Emissao Documentos
		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsSlipEmail()) {
			cota.getParametroDistribuicao().setSlipEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc().getIsSlipEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsSlipImpresso()) {
			cota.getParametroDistribuicao()
					.setSlipImpresso(
							filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
									.getFiltroCheckDistribEmisDoc()
									.getIsSlipImpresso());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsBoletoEmail()) {
			cota.getParametroDistribuicao().setBoletoEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc().getIsBoletoEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsBoletoImpresso()) {
			cota.getParametroDistribuicao().setBoletoImpresso(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsBoletoImpresso());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsBoletoSlipEmail()) {
			cota.getParametroDistribuicao().setBoletoSlipEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsBoletoSlipEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsBoletoSlipImpresso()) {
			cota.getParametroDistribuicao().setBoletoSlipImpresso(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsBoletoSlipImpresso());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsReciboEmail()) {
			cota.getParametroDistribuicao().setReciboEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc().getIsReciboEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsReciboImpresso()) {
			cota.getParametroDistribuicao().setReciboImpresso(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsReciboImpresso());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsNotaEnvioEmail()) {
			cota.getParametroDistribuicao().setNotaEnvioEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsNotaEnvioEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsNotaEnvioImpresso()) {
			cota.getParametroDistribuicao().setNotaEnvioImpresso(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsNotaEnvioImpresso());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsChamdaEncalheEmail()) {
			cota.getParametroDistribuicao().setChamadaEncalheEmail(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsChamdaEncalheEmail());
		}

		if (filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
				.getFiltroCheckDistribEmisDoc().getIsChamdaEncalheImpresso()) {
			cota.getParametroDistribuicao().setChamadaEncalheImpresso(
					filtroAlteracaoCotaDTO.getFiltroModalDistribuicao()
							.getFiltroCheckDistribEmisDoc()
							.getIsChamdaEncalheImpresso());
		}
	}

}
