package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(
			FiltroLancamentoDTO filtro) {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		for (Lancamento lancamento : lancamentos) {
			LancamentoDTO dto = montarDTO(filtro.getData(),lancamento);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data,
			List<Long> idsFornecedores) {
		return lancamentoRepository.sumarioBalanceamentoMatrizLancamentos(data,
				idsFornecedores);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodo(
			Date dataInicial, List<Long> fornecedores) {
		Date dataFinal = DateUtil.adicionarDias(dataInicial, 6);
		List<DistribuicaoFornecedor> distribuicoes = distribuidorRepository
				.buscarDiasDistribuicaoFornecedor(fornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		Set<DiaSemana> diasDistribuicao = EnumSet.noneOf(DiaSemana.class);
		for (DistribuicaoFornecedor distribuicao : distribuicoes) {
			diasDistribuicao.add(distribuicao.getDiaSemana());
		}

		List<Date> periodoDistribuicao = filtrarPeriodoDistribuicao(
				dataInicial, dataFinal, diasDistribuicao);
		List<ResumoPeriodoBalanceamentoDTO> resumos = lancamentoRepository
				.buscarResumosPeriodo(periodoDistribuicao, fornecedores,
						GrupoProduto.CROMO);
		
		return montarResumoPeriodo(periodoDistribuicao, resumos);
	}

	private List<ResumoPeriodoBalanceamentoDTO> montarResumoPeriodo(
			List<Date> periodoDistribuicao,
			List<ResumoPeriodoBalanceamentoDTO> resumos) {
		Map<Date, ResumoPeriodoBalanceamentoDTO> mapa = new HashMap<Date, ResumoPeriodoBalanceamentoDTO>();
		for (ResumoPeriodoBalanceamentoDTO resumo : resumos) {
			mapa.put(resumo.getData(), resumo);
		}
		List<ResumoPeriodoBalanceamentoDTO> retorno = new ArrayList<ResumoPeriodoBalanceamentoDTO>(
				periodoDistribuicao.size());
		for (Date data : periodoDistribuicao) {
			ResumoPeriodoBalanceamentoDTO resumo = mapa.get(data);
			if (resumo == null) {
				resumo = ResumoPeriodoBalanceamentoDTO.empty(data);
			}
			retorno.add(resumo);
		}
		return retorno;
	}

	private List<Date> filtrarPeriodoDistribuicao (Date dataInicial,
			Date dataFinal, Collection<DiaSemana> diasDistribuicao) {
		List<Date> datas = new ArrayList<Date>();
		while (dataInicial.before(dataFinal) || dataInicial.equals(dataFinal)) {
			DiaSemana ds = DiaSemana.getByDate(dataInicial);
			if (diasDistribuicao.contains(ds)) {
				datas.add(dataInicial);
			}
			dataInicial = DateUtil.adicionarDias(dataInicial, 1);
		}
		return datas;
	}

	private LancamentoDTO montarDTO(Date data, Lancamento lancamento) {
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
		Produto produto = produtoEdicao.getProduto();
		LancamentoDTO dto = new LancamentoDTO();
		dto.setCodigoProduto(produto.getCodigo());
		dto.setDataMatrizDistrib(DateUtil.formatarData(
				lancamento.getDataLancamentoDistribuidor(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataPrevisto(DateUtil.formatarData(
				lancamento.getDataLancamentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataRecolhimento(DateUtil.formatarData(
				lancamento.getDataRecolhimentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setId(lancamento.getId());
		dto.setIdFornecedor(1L);
		dto.setNomeFornecedor(produto.getFornecedor().getJuridica().getNomeFantasia());
		dto.setLancamento(lancamento.getTipoLancamento().getDescricao());
		dto.setNomeProduto(produto.getNome());
		dto.setNumEdicao(produtoEdicao.getNumeroEdicao());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		dto.setPreco(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
		dto.setReparte(lancamento.getReparte().toString());
		BigDecimal total = produtoEdicao.getPrecoVenda().multiply(lancamento.getReparte());
		dto.setTotal(CurrencyUtil.formatarValor(total));
		dto.setFisico(lancamento.getTotalRecebimentoFisico().toString());
		Estudo estudo = lancamento.getEstudo();
		if (estudo != null) {
			dto.setQtdeEstudo(estudo.getQtdeReparte().toString());
		} else {
			dto.setQtdeEstudo("0");
		}
		dto.setFuro(lancamento.isFuro());
		dto.setCancelamentoGD(lancamento.isCancelamentoGD());
		dto.setExpedido(lancamento.isExpedido());
		dto.setEstudoFechado(lancamento.isEstudoFechado());
		if (DateUtil.isHoje(data) && lancamento.isSemRecebimentoFisico()) {
			dto.setSemFisico(true);
		}
		return dto;
	}
	
	

}
