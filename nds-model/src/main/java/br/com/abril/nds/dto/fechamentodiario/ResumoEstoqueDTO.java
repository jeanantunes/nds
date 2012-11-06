package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResumoEstoqueDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private ResumoEstoqueProduto resumoEstoqueProduto;
	
	private ResumoEstoqueExemplar resumEstoqueExemplar;
	
	private ValorResumoEstoque valorResumoEstoque;
	
	public class ResumoEstoqueProduto extends ResumoEstoque{}
	
	public class ResumoEstoqueExemplar extends ResumoEstoque{}
	
	public abstract class ResumoEstoque{
		
		protected Integer quantidadeLancamento;
		
		protected Integer quantidadeJuramentado;
		
		protected Integer quantidadeSuplementar;
		
		protected Integer quantidadeRecolhimento;
		
		protected Integer quantidadeDanificados;

		public Integer getQuantidadeLancamento() {
			return quantidadeLancamento;
		}

		public void setQuantidadeLancamento(Integer quantidadeLancamento) {
			this.quantidadeLancamento = quantidadeLancamento;
		}

		public Integer getQuantidadeJuramentado() {
			return quantidadeJuramentado;
		}

		public void setQuantidadeJuramentado(Integer quantidadeJuramentado) {
			this.quantidadeJuramentado = quantidadeJuramentado;
		}

		public Integer getQuantidadeSuplementar() {
			return quantidadeSuplementar;
		}

		public void setQuantidadeSuplementar(Integer quantidadeSuplementar) {
			this.quantidadeSuplementar = quantidadeSuplementar;
		}

		public Integer getQuantidadeRecolhimento() {
			return quantidadeRecolhimento;
		}

		public void setQuantidadeRecolhimento(Integer quantidadeRecolhimento) {
			this.quantidadeRecolhimento = quantidadeRecolhimento;
		}

		public Integer getQuantidadeDanificados() {
			return quantidadeDanificados;
		}

		public void setQuantidadeDanificados(Integer quantidadeDanificados) {
			this.quantidadeDanificados = quantidadeDanificados;
		}		
	}

	public class ValorResumoEstoque{
		
		protected BigDecimal valorLancamento;
		
		protected BigDecimal valorJuramentado;
		
		protected BigDecimal valorSuplementar;
		
		protected BigDecimal valorRecolhimento;
		
		protected BigDecimal valorDanificados;

		public BigDecimal getValorLancamento() {
			return valorLancamento;
		}

		public void setValorLancamento(BigDecimal valorLancamento) {
			this.valorLancamento = valorLancamento;
		}

		public BigDecimal getValorJuramentado() {
			return valorJuramentado;
		}

		public void setValorJuramentado(BigDecimal valorJuramentado) {
			this.valorJuramentado = valorJuramentado;
		}

		public BigDecimal getValorSuplementar() {
			return valorSuplementar;
		}

		public void setValorSuplementar(BigDecimal valorSuplementar) {
			this.valorSuplementar = valorSuplementar;
		}

		public BigDecimal getValorRecolhimento() {
			return valorRecolhimento;
		}

		public void setValorRecolhimento(BigDecimal valorRecolhimento) {
			this.valorRecolhimento = valorRecolhimento;
		}

		public BigDecimal getValorDanificados() {
			return valorDanificados;
		}

		public void setValorDanificados(BigDecimal valorDanificados) {
			this.valorDanificados = valorDanificados;
		}
	}

	public ResumoEstoqueProduto getResumoEstoqueProduto() {
		return resumoEstoqueProduto;
	}

	public void setResumoEstoqueProduto(ResumoEstoqueProduto resumoEstoqueProduto) {
		this.resumoEstoqueProduto = resumoEstoqueProduto;
	}

	public ResumoEstoqueExemplar getResumoEstoqueExemplar() {
		return resumEstoqueExemplar;
	}

	public void setResumEstoqueExemplar(ResumoEstoqueExemplar resumEstoqueExemplar) {
		this.resumEstoqueExemplar = resumEstoqueExemplar;
	}

	public ValorResumoEstoque getValorResumoEstoque() {
		return valorResumoEstoque;
	}

	public void setValorResumoEstoque(ValorResumoEstoque valorResumoEstoque) {
		this.valorResumoEstoque = valorResumoEstoque;
	}
	
	
}
