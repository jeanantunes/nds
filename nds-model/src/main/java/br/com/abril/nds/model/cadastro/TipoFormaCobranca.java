package br.com.abril.nds.model.cadastro;

public enum TipoFormaCobranca {
		DIARIA("Diária"),
		QUINZENAL("Quinzenal"),
		MENSAL("Mensal"), 
		SEMANAL("Semanal");
		
        private String descricao;
		
		private TipoFormaCobranca(String descricao) {
			this.descricao = descricao;
		}

		/**
		 * @return the descricao
		 */
		public String getDescricao() {
			return descricao;
		}

		/**
		 * @param descricao the descricao to set
		 */
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		
		
		
		
		
}
