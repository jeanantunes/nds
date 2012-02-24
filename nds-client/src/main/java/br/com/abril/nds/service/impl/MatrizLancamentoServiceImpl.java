package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.Util;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(Date inicio,
			Date fim, Long... idsFornecedores) {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(inicio, fim,
						idsFornecedores);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		for (Lancamento lancamento : lancamentos) {
			ProdutoEdicao prodEdicao = lancamento.getProdutoEdicao();
			Produto produto = prodEdicao.getProduto();

			LancamentoDTO dto = new LancamentoDTO();
			dto.setId(lancamento.getId());
			dto.setCodigoProduto(produto.getCodigo());
			dto.setDataMatrizDistrib(Util.formatarData(
					lancamento.getDataLancamentoDistribuidor(),
					FORMATO_DATA_LANCAMENTO));
			dto.setDataPrevisto(Util.formatarData(
					lancamento.getDataLancamentoPrevista(),
					FORMATO_DATA_LANCAMENTO));
			dto.setDataRecolhimento(Util.formatarData(
					lancamento.getDataRecolhimentoPrevista(),
					FORMATO_DATA_LANCAMENTO));
			dto.setFornecedor(produto.getFornecedorUnico().getJuridica()
					.getNomeFantasia());
			dto.setIdFornecedor(produto.getFornecedorUnico().getId());
			dto.setLancamento(lancamento.getTipoLancamento().getDescricao());
			dto.setNomeProduto(produto.getNome());
			dto.setNumEdicao(prodEdicao.getNumeroEdicao());
			dto.setPacotePadrao(prodEdicao.getPacotePadrao());
			dto.setPreco(prodEdicao.getPrecoVenda());
			Long reparte = 0L;
			Estudo estudo = lancamento.getEstudo();
			if (estudo != null) {
				reparte = Double.valueOf(estudo.getQtdeReparte()).longValue();
			}
			dto.setReparte(reparte);
			dtos.add(dto);
		}
		return dtos;
	}

}
