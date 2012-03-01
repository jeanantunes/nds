package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(
			FiltroLancamentoDTO filtro) {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(filtro);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		for (Lancamento lancamento : lancamentos) {
			LancamentoDTO dto = montarDTO(lancamento);
			dtos.add(dto);
		}
		return dtos;
	}

	private LancamentoDTO montarDTO(Lancamento lancamento) {
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
		dto.setNomeFornecedor("ACME");
		dto.setLancamento(lancamento.getTipoLancamento().getDescricao());
		dto.setNomeProduto(produto.getNome());
		dto.setNumEdicao(produtoEdicao.getNumeroEdicao());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		dto.setPreco(CurrencyUtil.formatarValorMonetario(produtoEdicao.getPrecoVenda()));
		dto.setReparte(lancamento.getReparte().toString());
		BigDecimal total = produtoEdicao.getPrecoVenda().multiply(lancamento.getReparte());
		dto.setTotal(CurrencyUtil.formatarValorMonetario(total));
		return dto;
	}

}
