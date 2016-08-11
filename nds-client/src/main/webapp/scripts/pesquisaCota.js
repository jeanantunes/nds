function PesquisaCota(workspace) {

	var pesquisaCota = this;
	
	this.workspace = workspace;
		
	//Pesquisa por número da cota
	this.pesquisarPorNumeroCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack, checkValidateResult, isUsarWorkSpace) {
		
		var numeroCota = "";
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			numeroCota = $(idCampoNumeroCota).val();
		}else{
			numeroCota = $(idCampoNumeroCota, pesquisaCota.workspace).val();
		}

		numeroCota = $.trim(numeroCota);
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			$(idCampoNomeCota).val("");
		}else{
			$(idCampoNomeCota, pesquisaCota.workspace).val("");
		}
		
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
					{numeroCota:numeroCota},
				function(result) {
						//metodo validateResult pode ser sobrescrito por outro arquivo *.js para tratar negocialmente a cota pesquisada
						if(checkValidateResult && pesquisaCota.validateResult){
							var msgArray = pesquisaCota.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					pesquisaCota.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack,isUsarWorkSpace); 
				},
				function() {
					pesquisaCota.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack, isUsarWorkSpace); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	this.pesquisarNomeCota = function(idCampoNomeCota, idCampoNumeroCota, isFromModal, successCallBack, errorCallBack) {
		
		var nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();

		nomeCota = $.trim(nomeCota);
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		if (nomeCota && nomeCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNome", {nomeCota:nomeCota},
				function(result) { 
					pesquisaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, successCallBack); 
				},
				function() {
					pesquisaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack); 
				}, 
				isFromModal
			);

		} else {
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por número da cota
	this.pesquisarPorNumeroSuccessCallBack = function(result, idCampoNomeCota, successCallBack, isUsarWorkSpace) {

		pesquisaCota.pesquisaRealizada = true;
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			$(idCampoNomeCota).val(result.nome);
		}else{
			$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
		}
		
		try {
			$(situacaoCadastro, pesquisaCota.workspace).val(result.situacaoCadastro);
		    } catch (ee ) {};
		if (successCallBack) {
			
			successCallBack(result);
		}
	},
	
	
	//Error callback para pesquisa por número da cota
	this.pesquisarPorNumeroErrorCallBack = function(idCampoNumeroCota, errorCallBack, isUsarWorkSpace) {
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			$(idCampoNumeroCota).val("");
			$(idCampoNumeroCota).focus();
		}else{
			$(idCampoNumeroCota, pesquisaCota.workspace).val("");
			$(idCampoNumeroCota, pesquisaCota.workspace).focus();
		}
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	
	//Busca dados para o auto complete do nome da cota
	this.autoCompletarPorNome = function(idCampoNomeCota, isFromModal, isUsarWorkSpace) {
		
		pesquisaCota.pesquisaRealizada = false;
		
		var nomeCota = "";
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			nomeCota = $(idCampoNomeCota).val();
			$(idCampoNomeCota).autocomplete({source: [""]});
		}else{
			nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
			$(idCampoNomeCota, pesquisaCota.workspace).autocomplete({source: [""]});
		}
		
		nomeCota = $.trim(nomeCota);
		
		
		if (nomeCota && nomeCota.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/cota/autoCompletarPorNome", {nomeCota:nomeCota},
				function(result) { 
					pesquisaCota.exibirAutoComplete(result, idCampoNomeCota, 3, isUsarWorkSpace);
				},
				null, 
				isFromModal
			);
		}
	},
	
	this.descricaoAtribuida = true,
	
	this.pesquisaRealizada = false,
	
	this.numeroCotaSelecionada = null,
	
	this.intervalo = null,
	
	//Exibe o auto complete no campo
	this.exibirAutoComplete = function(result, idCampoCota, tamanhoInicial, isUsarWorkSpace) {
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			$(idCampoCota).autocomplete({
				source: result,
				focus : function(event, ui) {
					pesquisaCota.descricaoAtribuida = false;
				},
				close : function(event, ui) {
					pesquisaCota.descricaoAtribuida = true;
					
					if (ui.item){
						pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
					}
				},
				select : function(event, ui) {
					pesquisaCota.descricaoAtribuida = true;
					pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
				},
				minLength: tamanhoInicial,
				delay : 0,
			});
		}else{
			$(idCampoCota, pesquisaCota.workspace).autocomplete({
				source: result,
				focus : function(event, ui) {
					pesquisaCota.descricaoAtribuida = false;
				},
				close : function(event, ui) {
					pesquisaCota.descricaoAtribuida = true;
					
					if (ui.item){
						pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
					}
				},
				select : function(event, ui) {
					pesquisaCota.descricaoAtribuida = true;
					pesquisaCota.numeroCotaSelecionada = ui.item.chave.numero;
				},
				minLength: tamanhoInicial,
				delay : 0,
			});
		}
		
	},
	
	//Pesquisar por nome da cota
	this.pesquisarPorNomeCota = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult, isUsarWorkSpace) {
		
		setTimeout(function() { clearInterval(pesquisaCota.intervalo); }, 10 * 1000);
		
		pesquisaCota.intervalo = $().interval(function() {
			
			if (pesquisaCota.descricaoAtribuida) {
				
				if (pesquisaCota.pesquisaRealizada) {
					
					clearInterval(pesquisaCota.intervalo);
					
					return;
				}
				
				pesquisaCota.pesquisarPorNomeCotaAposIntervalo(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack, checkValidateResult, isUsarWorkSpace);
			}
			
		}, 100);
	},
	
	//Pesquisa por nome da cota após o intervalo
	this.pesquisarPorNomeCotaAposIntervalo = function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack,checkValidateResult, isUsarWorkSpace) {
		
		clearInterval(pesquisaCota.intervalo);
		var nomeCota = "";
		
		if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
			nomeCota = $(idCampoNomeCota).val();
			$(idCampoNumeroCota).val("");
		}else{
			nomeCota = $(idCampoNomeCota, pesquisaCota.workspace).val();
			$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		}
		
		nomeCota = $.trim(nomeCota);
		
		if (nomeCota && nomeCota.length > 0) {
			$.postJSON(
				contextPath + "/cadastro/cota/pesquisarPorNome", 
				{nomeCota:nomeCota},
				function(result) {

					if (result.length > 1){

						if (pesquisaCota.numeroCotaSelecionada){
							
							pesquisaCota.pesquisaRealizada = true;
							
							if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
								$(idCampoNumeroCota).val(pesquisaCota.numeroCotaSelecionada);
							}else{
								$(idCampoNumeroCota, pesquisaCota.workspace).val(pesquisaCota.numeroCotaSelecionada);
							}
						}
					}
					else if(result.numero){

						pesquisaCota.pesquisaRealizada = true;
						
						if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
							$(idCampoNumeroCota).val(result.numero);
						}else{
							$(idCampoNumeroCota, pesquisaCota.workspace).val(result.numero);
						}
						
						//metodo validateResult pode ser sobrescrito por outro arquivo *.js para tratar negocialmente a cota pesquisada
						if(checkValidateResult && pesquisaCota.validateResult){
							var msgArray = pesquisaCota.validateResult(result);
							if(msgArray && msgArray.length>0){
								exibirMensagem("WARNING", msgArray);
							}
							
						}
					}else{
					
					    pesquisaCota.pesquisarPorNomeSuccessCallBack(result, idCampoNumeroCota, idCampoNomeCota, successCallBack, isUsarWorkSpace);
					}
				},
				function() {
					pesquisaCota.pesquisarPorNomeErrorCallBack(idCampoNomeCota, errorCallBack);
				}, 
				isFromModal
			);
		} else {

			if (errorCallBack && typeof(errorCallBack) === 'function') {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por nome da cota
	this.pesquisarPorNomeSuccessCallBack = function(result, idCampoNumeroCota, idCampoNomeCota, successCallBack, isUsarWorkSpace) {
		
		if (result != "") {
			
			if(isUsarWorkSpace != undefined && isUsarWorkSpace == false){
				$(idCampoNumeroCota).val(result.numero);
				$(idCampoNomeCota).val(result.nome);
			}else{
				$(idCampoNumeroCota, pesquisaCota.workspace).val(result.numero);
				$(idCampoNomeCota, pesquisaCota.workspace).val(result.nome);
			}
			
			try {
		   	   $(situacaoCadastro, pesquisaCota.workspace).val(result.situacaoCadastro);
				} catch ( ee ) {};
			if (successCallBack) {
				successCallBack(result);
			}
		}
	},
	
	//Error callback para pesquisa por nome da cota
	this.pesquisarPorNomeErrorCallBack = function(idCampoNomeCota, errorCallBack) {
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		$(idCampoNomeCota, pesquisaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	},
	
	//Obtém uma cota pelo número
	this.obterPorNumeroCota = function(numeroCota, isFromModal, successCallback, errorCallBack) {
		
		if (numeroCota && numeroCota.length > 0) {

			$.postJSON(contextPath + "/cadastro/cota/pesquisarPorNumero",
				{numeroCota:numeroCota},
				function(result) { 
					successCallback(result); 
				},
				function() {
					errorCallBack();
				}, 
				isFromModal
			);
		}
	};
	
	this.obterTipoDistribuicaoPorNumeroCota = function(numeroCota) {
		
		var tipoDistribuicaoCota='';
		
		
		return tipoDistribuicaoCota;
	};
	
}
//@ sourceURL=pesquisaCota.js