function TabCota(tabName) {
	
	var T = this;

	this.funcaoSalvar = null;

	this.possuiDadosObrigatorios = null;
	
	this.novaAba=0;
		
	/**
	 * Verifica qual aba foi selecionada, define o metodo de salvamento da mesma e chama seu método carregamento.
	 */
	this.carregaDadosNovaAba = function(abaSelecionada) {
			
		switch(abaSelecionada) {
		
		/**DADOS CADASTRAIS**/
		case 0: 
			MANTER_COTA.carregarDadosCadastrais();
			MANTER_COTA.confirmado = false;
			break;
			
		/**ENDERECOS**/
		case 1:
			MANTER_COTA.carregarEnderecos();
			MANTER_COTA.confirmado = false;
			break;
		
		/**TELEFONES**/
		case 2: 
			MANTER_COTA.carregarTelefones();
			MANTER_COTA.confirmado = false;
			break;

		/**PDV**/
		case 3: 
			MANTER_COTA.carregarPDV();
			MANTER_COTA.confirmado = false;
			break;
		
		/**GARANTIA**/
		case 4: 
			T.funcaoSalvar = function(callBack){
				return tipoCotaGarantia.salva(callBack);
			};
			tipoCotaGarantia.onOpen(MANTER_COTA.tipoCotaSelecionada);
			MANTER_COTA.confirmado = false;
			break;
		
		/**Fornecedor**/
		case 5:

			MANTER_COTA.carregarFornecedores();
			MANTER_COTA.confirmado = false;

			break;

		/**Desconto**/		
		case 6:
			MANTER_COTA.carregarDescontos();
			MANTER_COTA.confirmado = false;
			break;
		
		/**Financeiro**/
		case 7:
			MANTER_COTA.carregaFinanceiroCota();
			MANTER_COTA.confirmado = false;
			break;
		
		/**Distribuidor**/
		case 8:
			MANTER_COTA.carregarDistribuicao();
			MANTER_COTA.confirmado = false;
			break;
		
		/**Socios**/
		case 9:
			MANTER_COTA.carregarDadosSocio();
			MANTER_COTA.confirmado = false;
			break;
	}
		
	},
		
		
	$(function() {
		
		if(T.funcaoSalvar) {
			T.funcaoSalvar();
    	}
		
		$( "#" + tabName ).tabs();
				
		$("#" + tabName).tabs({
						
		    select: function(event, ui) {
		    	
		    	if(ui.index == 3){
		    		$("#btn_confirmar_cota").hide();
		    	}
		    	else{
		    		$("#btn_confirmar_cota").show();
		    	}
		    	
		    	if(!T.funcaoSalvar) {
		    		return;
		    	}
		    	
		    	var abaAnterior = T.novaAba;

		    	if(abaAnterior == 0 && !T.possuiDadosObrigatorios){
		    		
		    		exibirMensagemDialog(
							"WARNING", 
							["Os dados cadastrais precisam ser confirmados para prosseguir com o cadastro da cota!"],"dialog-cota"
						);
						
		    		return false;
		    	}
		    	
		    	T.novaAba = ui.index;
		    	
		    	return true;
		    }
		});	
		
		$("#" + tabName).tabs({
			
		    show: function(event, ui) {		
		    	T.funcaoSalvar = null;
		    	T.carregaDadosNovaAba(ui.index);
		    }
		});		
		
	});
		
}

//@ sourceURL=scriptTabCota.js
