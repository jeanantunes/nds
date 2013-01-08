package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.FechamentoCEIntegracaoVO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FechamentoCEIntegracaoServiceImpl implements FechamentoCEIntegracaoService {
	
	@Autowired
	private FechamentoCEIntegracaoRepository fechamentoCEIntegracaoRepository;

	@Transactional
	public List<FechamentoCEIntegracaoDTO> buscarFechamentoEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalhe(filtro);
	}
	
	/*@Override
	public List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento) {
		List<FechamentoCEIntegracaoDTO> lista = new ArrayList<FechamentoCEIntegracaoDTO>();
		int sequencial = 1;
		for(FechamentoCEIntegracaoDTO dto: listaFechamento){
			dto.setVenda(dto.getReparte().subtract(dto.getEncalhe()));
			double valorDaVenda = dto.getVenda().doubleValue() * dto.getPrecoCapa().doubleValue();
			dto.setvalorVendaFormatado(CurrencyUtil.formatarValor(valorDaVenda));
			dto.setSequencial(sequencial);
			sequencial++;
			lista.add(dto);
		}
		return lista;		
	}*/

	@Override
	@Transactional
	public void fecharCE(Long encalhe, ProdutoEdicao produtoEdicao) {
		this.fechamentoCEIntegracaoRepository.fecharCE(encalhe, produtoEdicao); 
		
	}

	@Override
	@Transactional
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {		 
		return this.fechamentoCEIntegracaoRepository.verificarStatusSemana(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoVO construirFechamentoCEIntegracaoVO(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.buscarFechamentoEncalhe(filtro);
		
		if(listaFechamento.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> tableModel = new TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFechamento));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaFechamento.size());
		
		FechamentoCEIntegracaoVO fechamentoCEIntegracaoVO = new FechamentoCEIntegracaoVO();

		FechamentoCEIntegracaoConsolidadoDTO totalFechamento = this.buscarFechamentoEncalheTotal(filtro);
		fechamentoCEIntegracaoVO.setTotalBruto(totalFechamento.getTotalBruto());
		fechamentoCEIntegracaoVO.setTotalDesconto(totalFechamento.getTotalDesconto());
		fechamentoCEIntegracaoVO.setTotalLiquido(totalFechamento.getTotalLiquido());

		fechamentoCEIntegracaoVO.setListaFechamento(tableModel);
		fechamentoCEIntegracaoVO.setSemanaFechada(this.verificarStatusSemana(filtro));

		// TODO: TOTAIS AQUI
		
		return fechamentoCEIntegracaoVO;
	}

	@Override
	@Transactional(readOnly=true)
	public FechamentoCEIntegracaoConsolidadoDTO buscarFechamentoEncalheTotal(FiltroFechamentoCEIntegracaoDTO filtro) {
		return this.fechamentoCEIntegracaoRepository.buscarConferenciaEncalheTotal(filtro);
	}
	
}
