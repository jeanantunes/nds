package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Map;

import br.com.abril.nds.model.cadastro.Cota;

public class MapaCotaDTO implements Serializable {

		private static final long serialVersionUID = 8749680908347698341L;
		
		private Integer numeroCota;
		private String nomeCota;
		
		private Map<Long, ProdutoMapaCotaDTO> produtos;
		
		private Cota cota;
		
		public MapaCotaDTO() {
		
		}
		
		
		
		public MapaCotaDTO(Integer numeroCota, String nomeCota,	Map<Long, ProdutoMapaCotaDTO> produtos) {
			super();
			this.numeroCota = numeroCota;
			this.nomeCota = nomeCota;
			this.produtos = produtos;
		}
		
		
		
		/**
		* @return the numeroCota
		*/
		public Integer getNumeroCota() {
			return numeroCota;
		}
		/**
		* @param numeroCota the numeroCota to set
		*/
		public void setNumeroCota(Integer numeroCota) {
			this.numeroCota = numeroCota;
		}
		/**
		* @return the nomeCota
		*/
		public String getNomeCota() {
			return nomeCota;
		}
		/**
		* @param nomeCota the nomeCota to set
		*/
		public void setNomeCota(String nomeCota) {
			this.nomeCota = nomeCota;
		}
		/**
		* @return the produtos
		*/
		public Map<Long, ProdutoMapaCotaDTO> getProdutos() {
			return produtos;
		}
		/**
		* @param produtos the produtos to set
		*/
		public void setProdutos(Map<Long, ProdutoMapaCotaDTO> produtos) {
			this.produtos = produtos;
		}
		
		
		
		public Cota getCota() {
			return cota;
		}
		
		
		
		public void setCota(Cota cota) {
			this.cota = cota;
		}


}