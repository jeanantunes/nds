package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.AlteracaoPrecoDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.OutraMovimentacaoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO.TipoResumo;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.exception.FechamentoDiarioException;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FecharDiaService {
	
	/**
	 * Verifica se tem cobrança para o dia(D-1) de operação do distribuidor.
	 * 
	 * @param dataOperacaoDistribuidor
	 * @return
	 */
	boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem nota fiscal com recebimento lógico mas não tem recebimento fisíco
	 * @param dataOperacaoDistribuidor 
	 * 
	 * @return boolean
	 */
	boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor);
	
	/**
	 * Retorna uma lista com as notas fiscais de entrada que não tiveram seu recebimento fisico confirmado
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return List<ValidacaoRecebimentoFisicoFecharDiaDTO>
	 */
	List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem algum produto que não teve sua expedição confirmada.
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return boolean
	 */
	Boolean existeConfirmacaoDeExpedicao(Date dataOperacao);
	

	/**
	 * Retorna uma lista com os produtos que não tiveram sua expedição confirmada
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>
	 */
	List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem algum produto com lançamento de faltas e sobras pendentes
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return boolean
	 */
	Boolean existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem algum produto com lançamento de faltas e sobras pendentes
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return boolean
	 */
	Boolean existeMatrizRecolhimentoSalva(Date dataOperacaoDistribuidor);
	
	/**
	 * Retorna uma lista de produtos com diferenças 
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return List<ValidacaoLancamentoFaltaESobraFecharDiaDTO>
	 */
	List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> obterLancamentoFaltasESobras(Date dataOperacaoDistribuidor);
	
	/**
	 * Retorna verdadeiro caso exista movimentos pendentes de aprovacao
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return boolean
	 */
	boolean existePendenciasDeAprovacao(Date dataOperacao);

	/**
	 * @see DividaService#sumarizacaoDividasReceberEm(Date)
	 */
    List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data);
    
    /**
     * @see DividaService#sumarizacaoDividasVencerApos(Date)
     */
    List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data);
    
    
    /**
     * @see DividaService#obterDividasReceberEm(Date, PaginacaoVO)
     */
    List<DividaDTO> obterDividasReceberEm(Date data, PaginacaoVO paginacao);
    
    /**
     * @see DividaService#obterDividasVencerApos(Date, PaginacaoVO)
     */
    List<DividaDTO> obterDividasVencerApos(Date data, PaginacaoVO paginacao);


    /**
     * @see DividaService#contarDividasReceberEm(Date)
     */
    long contarDividasReceberEm(Date data);
    
    
    /**
     * @see DividaService#contarDividasVencerApos(Date)
     */
    long contarDividasVencerApos(Date data);
    
    /**
	 * Obtém o resumo do fechamento diário de cotas.
	 * 
	 * @param dataFechamento - data do fechamento.
	 * 
	 * @return {@link ResumoFechamentoDiarioCotasDTO}
	 */
	ResumoFechamentoDiarioCotasDTO obterResumoCotas(Date dataFechamento);
	
	/**
	 * Obtém o resumo do fechamento diário do consignado.
	 * 
	 * @param dataFechamento - data do fechamento
	 * 
	 * @return {@link ResumoFechamentoDiarioConsignadoDTO}
	 */
	ResumoFechamentoDiarioConsignadoDTO obterResumoConsignado(Date dataFechamento);
	
	/**
	 * Retorna o resumo de estoque de produtos e exemplares do dia em operação.
	 * 
	 * @param dataOperacao - data de operação do sistema
	 * 
	 * @return ResumoEstoqueDTO
	 */
	ResumoEstoqueDTO obterResumoEstoque(Date dataOperacao);
	
	/**
	 * Efetua processamento de fechamento do dia, efetua controle de aprovação e salva resumo do fechamento.
	 * 
	 * @param usuario - usuario
	 * 
	 * @param dataFechamento - data de fechamento
	 * 
	 * @return DTO com as informações do fechamento diário
	 */
	FechamentoDiarioDTO processarFechamentoDoDia(Usuario usuario, Date dataFechamento);
	

	/**
	 * Efetua o Lock/unlock do banco de dados. Usado para travar o banco para insert/update/delete na fechar dia.
	 * 
	 * @param lockBancoDeDados 
	 * 
	 * @return void
	 */
	void setLockBancoDeDados(boolean lockBancoDeDados);
 

    /**
     * Obtém as diferenças lançadas da na data
     * 
     * @param data
     *            data para recuperação das diferenças
     * @return lista de diferenças lançadas na data
     */
	List<Diferenca> obterDiferencas(Date data);
	
	/**
	 * Efetua a transferência das diferenças para estoque de Perda/Ganho.
	 * 
	 * @param dataOperacao - data de operação
	 */
	void transferirDiferencasParaEstoqueDePerdaGanho(Date dataOperacao, Long idUsuario);

	/**
	 * Verifica se cotas a vista tiveram seus movimentos financeiros consolidados
	 * @param data
	 * @return boolean
	 */
	boolean isConsolidadoCotaAVista(Date data);
	
	/**
	 * Verifica se o dia informado ja foi fechado
	 * 
	 * @param dataFechamento data informada para o fechamento
	 * 
	 * @return boolean
	 */
	boolean isDiaComFechamentoRealizado(Date dataFechamento);
	
	List<CotaResumoDTO> obterDetalheCotaFechamentoDiario(Date dataFechamento,TipoResumo tipoResumo);

	List<AlteracaoPrecoDTO> obterAlteracaoPreco(Date dataFechamento);
	
	List<OutraMovimentacaoDTO> obterOutraMovimentacao(final Date dataFechamento);
	
	FechamentoDiarioDTO obterResumoFechamentoDiario( Date dataFechamento)throws FechamentoDiarioException;
	
}