function PesquisaEntregador(workspace) {

	var T = this;
	
	this.workspace = workspace;

	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeEntregador, isFromModal) {
		
		T.pesquisaRealizada = false;
		
		var nome = $(idCampoNomeEntregador, T.workspace).val();
		
		nome = $.trim(nome);
		
		$(idCampoNomeEntregador, T.workspace).autocomplete({source: ""});
		
		if (nome && nome.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/entregador/autoCompletarPorNome", "nome=" + nome,
				function(result) { 
					T.exibirAutoComplete(result, idCampoNomeEntregador); 
				},
				null, 
				isFromModal
			);
		}
	},
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.intervalo = null,
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoNomeEntregador) {
		
		$(idCampoNomeEntregador, T.workspace).autocomplete({
			source: result,
			focus : function(event, ui) {
				T.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				T.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				T.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeEntregador = function(idCampoIdEntregador, idCampoNomeEntregador, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(T.intervalo); }, 10 * 1000);
		
		T.intervalo = $().interval(function() {
			
			if (T.descricaoAtribuida) {
				
				if (T.pesquisaRealizada) {
					
					clearInterval(T.intervalo);
					
					return;
				}
				
				T.pesquisarPorNomeEntregadorAposIntervalo(idCampoIdEntregador, idCampoNomeEntregador, isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeEntregadorAposIntervalo = function(idCampoIdEntregador, idCampoNomeEntregador, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(T.intervalo);
		
		T.pesquisaRealizada = true;
		
		var nome = $(idCampoNomeEntregador, T.workspace).val();
		
		nome = $.trim(nome);
		
		$(idCampoIdEntregador, T.workspace).val("");
		
		if (nome && nome.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/entregador/pesquisarPorNome", 
				"nome=" + nome,
				function(result) {
					T.pesquisarPorNomeSuccessCallBack(result, idCampoIdEntregador, idCampoNomeEntregador, successCallBack);
				},
				function() {
					T.pesquisarPorNomeErrorCallBack(idCampoNomeEntregador, errorCallBack);
				}, 
				isFromModal
			);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por nome da cota
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampoIdEntregador, idCampoNomeEntregador, successCallBack) {
		
		if (result != "") {
			
			$(idCampoIdEntregador, T.workspace).val(result.idEntregador);
			$(idCampoNomeEntregador, T.workspace).val(result.nomeEntregador);
			
			if (successCallBack) {
				
				successCallBack();
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeEntregador, errorCallBack) {
		
		$(idCampoNomeEntregador, T.workspace).val("");
		
		$(idCampoNomeEntregador, T.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	};
	
}