package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;

public class ResultadoResumoBalanceamentoVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2795532975752879190L;

	private List<ResumoPeriodoBalanceamentoVO> listaResumoPeriodoBalanceamento;
	
	private boolean bloquearBotoes;
	
	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private List<ProdutoLancamentoCanceladoDTO> listaProdutosLancamentosCancelados;
	
	private boolean utilizaSedeAtendida;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoDeOutraSemana;
	
	/**
	 * Construtor padrão.
	 */
	public ResultadoResumoBalanceamentoVO() {
		
	}

	/**
	 * @return the listaResumoPeriodoBalanceamento
	 */
	public List<ResumoPeriodoBalanceamentoVO> getListaResumoPeriodoBalanceamento() {
		return listaResumoPeriodoBalanceamento;
	}

	/**
	 * @param listaResumoPeriodoBalanceamento the listaResumoPeriodoBalanceamento to set
	 */
	public void setListaResumoPeriodoBalanceamento(
			List<ResumoPeriodoBalanceamentoVO> listaResumoPeriodoBalanceamento) {
		this.listaResumoPeriodoBalanceamento = listaResumoPeriodoBalanceamento;
	}

	/**
	 * @return the bloquearBotoes
	 */
	public boolean isBloquearBotoes() {
		return bloquearBotoes;
	}

	/**
	 * @param bloquearBotoes the bloquearBotoes to set
	 */
	public void setBloquearBotoes(boolean bloquearBotoes) {
		this.bloquearBotoes = bloquearBotoes;
	}

	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public BigInteger getCapacidadeRecolhimentoDistribuidor() {
		return capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setCapacidadeRecolhimentoDistribuidor(
			BigInteger capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @return the listaProdutosLancamentosCancelados
	 */
	public List<ProdutoLancamentoCanceladoDTO> getListaProdutosLancamentosCancelados() {
		return listaProdutosLancamentosCancelados;
	}

	/**
	 * @param listaProdutosLancamentosCancelados the listaProdutosLancamentosCancelados to set
	 */
	public void setListaProdutosLancamentosCancelados(
			List<ProdutoLancamentoCanceladoDTO> listaProdutosLancamentosCancelados) {
		this.listaProdutosLancamentosCancelados = listaProdutosLancamentosCancelados;
	}

	public boolean isUtilizaSedeAtendida() {
		return utilizaSedeAtendida;
	}

	public void setUtilizaSedeAtendida(boolean utilizaSedeAtendida) {
		this.utilizaSedeAtendida = utilizaSedeAtendida;
	}
    
    public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoDeOutraSemana() {
        return produtosRecolhimentoDeOutraSemana;
    }

    public void setProdutosRecolhimentoDeOutraSemana(List<ProdutoRecolhimentoDTO> produtosRecolhimentoDeOutraSemana) {
        this.produtosRecolhimentoDeOutraSemana = produtosRecolhimentoDeOutraSemana;
    }
    
}