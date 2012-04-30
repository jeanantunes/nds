package br.com.abril.nds.model.cadastro;

public enum TipoFormaCobranca {

		MENSAL("Mensal"), 
		SEMANAL("Semanal");
		
        private String descricao;
		
		private TipoFormaCobranca(String descricao) {
			this.descricao = descricao;
		}
		
		@Override
		public String toString() {
			return this.descricao;
		}
}
