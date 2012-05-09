function TabCota(tabName) {
	
	var T = this;
	
	this.funcaoSalvar=null;
	
	this.novaAba=null;
		
	/**
	 * Verifica qual aba foi selecionada, define o metodo de salvamento da mesma e chama seu método carregamento.
	 */
	this.carregaDadosNovaAba = function(abaSelecionada) {
			
		switch(abaSelecionada) {
		
		/**DADOS CADASTRAIS**/
		case 0: 
			break;
		
		/**ENDERECOS**/
		case 1:
			
			ENDERECO_COTA.popularGridEnderecos();
			break;
		
		/**TELEFONES**/
		case 2: 
			COTA.carregarTelefones();
			break;

		/**PDV**/
		case 3: 
			carregarPDV();
			break;
		
		/**GARANTIA**/
		case 4: 
			void(0);
			break;
		
		/**FINANCEIRO**/
		case 5:
			carregaFinanceiro();
			break;

		/**BANCOS**/		
		case 6:
			T.funcaoSalvar = function(){return false;};
			break;
		
		/**DISTRIBUICAO**/
		case 7:
			T.funcaoSalvar = DISTRIB_COTA_CPF.salvar;
			DISTRIB_COTA_CPF.carregarDadosDistribuicaoCota( $('#_idCotaRef').val() );
			break;
		
		/**FORNECEDOR**/
		case 8:
			break;
		}
		
	},
	
	this.abrirNovaAba = function() {
		
		T.funcaoSalvar = null;
		
		$("#" + tabName).tabs('select', T.novaAba);
	};
		
	$(function() {
		
		$( "#" + tabName ).tabs();
				
		$("#" + tabName).tabs({
						
		    select: function(event, ui) {
		    	
		    	if(!T.funcaoSalvar) {
		    		return;
		    	}
		    	
		    	T.novaAba = ui.index;
		    	T.funcaoSalvar(T.abrirNovaAba);
		    	
		    	return false;
		    }
		});	
		
		$("#" + tabName).tabs({
			
		    show: function(event, ui) {				
		    	T.carregaDadosNovaAba(ui.index);
		    }
		});		
		
	});
		
}