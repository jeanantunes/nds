package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DetalheItemNotaFiscalDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaFiscalEntradaFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.vo.PaginacaoVO;

public interface NotaFiscalEntradaRepository extends Repository<NotaFiscalEntrada, Long> {

	List<Long> pesquisarItensNotaExpedidos(Long idNota);
	
	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
		
	List<NotaFiscalEntradaFornecedor> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<NotaFiscalEntradaFornecedorDTO> obterNotasFiscaisCadastradasDTO(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheItemNotaFiscalDTO> obterDetalhesNotaFical(Long idNotaFiscal, PaginacaoVO paginacao);

	void inserirNotaFiscal(NotaFiscalEntrada notaFiscal);
	
	List<NotaFiscalEntrada> obterNotaFiscalEntrada(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);

	List<ItemDTO<Long, String>> obterListaFornecedorNotaFiscal(List<Long> listaIdNotaFiscal);

	boolean existeNotaFiscalEntradaFornecedor(Long numeroNotaEnvio, Long idPessoaJuridica, Date dataEmissao);
}