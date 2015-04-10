package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.ValidacaoVO;

public interface RecebimentoFisicoService {
	 
	 List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	 
	 void inserirDadosRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual);
	 
	 List<CFOP> obterListaCFOP();
	 
	 List<NaturezaOperacao> obterTiposNotasFiscais(TipoOperacao tipoOperacao, Distribuidor distribuidor);
	 
	 void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual, Boolean pularValidacao);
	 
	 void cancelarNotaFiscal(Long idNotaFiscal);
	 
	 void apagarItemRecebimentoItemNota(RecebimentoFisicoDTO recebimento);
	 
	 RecebimentoFisico obterRecebimentoFisicoPorNotaFiscal(Long idNotaFiscal);
	 
	 public void validarExisteNotaFiscal(NotaFiscalEntradaFornecedor notaFiscal);

	RecebimentoFisicoDTO obterRecebimentoFisicoDTO(String codigo, String edicao);

	void excluirNota(Long id);
	
	ValidacaoVO validarDescontoProduto(Origem origemNota, List<RecebimentoFisicoDTO> listaItensNota);
	 
}
