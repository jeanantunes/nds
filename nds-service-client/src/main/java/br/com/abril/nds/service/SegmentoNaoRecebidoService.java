package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

public interface SegmentoNaoRecebidoService {

	List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro);

	void excluirSegmentoNaoRecebido(Long segmentoNaoRecebidoId);
		
	void inserirCotasSegmentoNaoRecebido(List<SegmentoNaoRecebido> segmentoNaoRecebido);
	
	List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro);

	List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(FiltroSegmentoNaoRecebidoDTO filtro);
	
	List<TipoSegmentoProduto> obterSegmentosElegiveisParaInclusaoNaCota(FiltroSegmentoNaoRecebidoDTO filtro);
	
	List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(Cota cota);

	/**
	 * Obtem o segmetos não recebidos das cotas base da cota passada via parametro.
	 * @param idCota
	 * @return
	 */
	public abstract List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosCotaBase(
			Long idCota);

}
