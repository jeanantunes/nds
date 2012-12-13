package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.seguranca.Usuario;

public interface RecebimentoFisicoService {
	 
	 List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	 
	 void inserirDadosRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual);
	 
	 List<CFOP> obterListaCFOP();
	 
	 List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao);
	 
	 void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscalEntrada notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual);
	 
	 void cancelarNotaFiscal(Long idNotaFiscal);
	 
	 void apagarItemRecebimentoItemNota(RecebimentoFisicoDTO recebimento);
	 
	 RecebimentoFisico obterRecebimentoFisicoPorNotaFiscal(Long idNotaFiscal);
	 
	 public void validarExisteNotaFiscal(NotaFiscalEntradaFornecedor notaFiscal);
	 
}
