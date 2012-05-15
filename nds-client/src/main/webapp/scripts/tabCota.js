function TabCota(tabName) {
	
	var T = this;

	this.funcaoSalvar = null;

	this.novaAba=null;
		
	/**
	 * Verifica qual aba foi selecionada, define o metodo de salvamento da mesma e chama seu método carregamento.
	 */
	this.carregaDadosNovaAba = function(abaSelecionada) {
			
		switch(abaSelecionada) {
		
		/**DADOS CADASTRAIS**/
		case 0: 
			MANTER_COTA.carregarDadosCadastrais();
			break;
			
		/**ENDERECOS**/
		case 1:
			MANTER_COTA.carregarEnderecos();
			break;
		
		/**TELEFONES**/
		case 2: 
			MANTER_COTA.carregarTelefones();
			break;

		/**PDV**/
		case 3: 
			MANTER_COTA.carregarPDV();
			break;
		
		/**GARANTIA**/
		case 4: 

			T.funcaoSalvar = tipoCotaGarantia.controller.salva;
			tipoCotaGarantia.onOpen();

			break;
		
		/**Fornecedor**/
		case 5:

			MANTER_COTA.carregarFornecedores();

			break;

		/**Desconto**/		
		case 6:
			MANTER_COTA.carregarDescontos();
			break;
		
		/**Financeiro**/
		case 7:
			MANTER_COTA.carregaFinanceiroCota();
			break;
		
		/**Distribuidor**/
		case 8:
			MANTER_COTA.carregarDistribuicao();
			break;
		
		/**Socios**/
		case 9:
			MANTER_COTA.carregarDadosSocio();
			break;
	}
		
	},
	
	this.fecharDialog = function() {
		
		T.funcaoSalvar = null;
		
		$( "#dialog-cota" ).dialog( "close" );
		
	},
	
	this.abrirNovaAba = function() {
		
		T.funcaoSalvar = null;
		
		$("#" + tabName).tabs('select', T.novaAba);
	};
		
	$(function() {
		
		if(T.funcaoSalvar) {
			T.funcaoSalvar();
    	}
		
		$( "#" + tabName ).tabs();
				
		$("#" + tabName).tabs({
						
		    select: function(event, ui) {
		    	
		    	if(!T.funcaoSalvar) {
		    		return;
		    	}
		    	
		    	T.novaAba = ui.index;
		    	
		    	return T.funcaoSalvar(T.abrirNovaAba);
		    	
		    	//return false;
		    }
		});	
		
		$("#" + tabName).tabs({
			
		    show: function(event, ui) {				
		    	T.carregaDadosNovaAba(ui.index);
		    }
		});		
		
	});
		
}
