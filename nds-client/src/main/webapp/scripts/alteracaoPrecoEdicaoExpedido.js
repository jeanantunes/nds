var manutencaoPublicacaoController = $.extend(true, {
	
	url:contextPath + "/financeiro/manutencaoPublicacao",
	codigoProduto:null,
	numeroEdicao:null,
	nomeProduto:null,
	
	init : function() {
		
		$("#edicaoProduto", manutencaoPublicacaoController.workspace).numeric();
		
		$('#novoPrecoProduto', manutencaoPublicacaoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		focusSelectRefField($("#codigoProduto"));
	},
	
	pesquisar: function (){
		
		manutencaoPublicacaoController.codigoProduto =  $("#codigoProduto", manutencaoPublicacaoController.workspace).val() ;
		manutencaoPublicacaoController.numeroEdicao = $("#edicaoProduto", manutencaoPublicacaoController.workspace).val();
		manutencaoPublicacaoController.nomeProduto = $("#nomeProduto", manutencaoPublicacaoController.workspace).val();
		
		var data = {codigo:manutencaoPublicacaoController.codigoProduto,
					numeroEdicao:manutencaoPublicacaoController.numeroEdicao};
		
		$.postJSON(manutencaoPublicacaoController.url + "/pesquisarProduto", 
				   data,
				   manutencaoPublicacaoController.renderizarDaodosProduto,
				   manutencaoPublicacaoController.tratraErroProduto);
	},
	
	renderizarDaodosProduto:function(precoProduto){
	
		$("#resultado",manutencaoPublicacaoController.workspace).show();
		$("#txtPrecoProduto",manutencaoPublicacaoController.workspace).text(precoProduto); 
		$("#txtLegenda",manutencaoPublicacaoController.workspace).text(manutencaoPublicacaoController.montarTituloFieldPublicacao());
		$("#novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
		$("#novoPrecoProduto",manutencaoPublicacaoController.workspace).focus();
	},
	
	montarTituloFieldPublicacao:function(){
		
		var nomePublicacao = "Publicação: " ;
		
		return nomePublicacao.concat(manutencaoPublicacaoController.codigoProduto,
									" - ",manutencaoPublicacaoController.nomeProduto,
									" - ",manutencaoPublicacaoController.numeroEdicao);
	},
	
	tratraErroProduto:function(){
		
		$("#resultado",manutencaoPublicacaoController.workspace).hide();
		manutencaoPublicacaoController.codigoProduto = null;
		manutencaoPublicacaoController.numeroEdicao = null;
	},
	
	confirmarAlteracaoPreco:function(){
		
		var data = {codigo:manutencaoPublicacaoController.codigoProduto,
				    numeroEdicao:manutencaoPublicacaoController.numeroEdicao,
				    precoProduto:$("#novoPrecoProduto", manutencaoPublicacaoController.workspace).val()};
		
		$.postJSON(manutencaoPublicacaoController.url + "/confirmarAlteracaoPreco", 
				   data,
				   manutencaoPublicacaoController.precoAlteradoComSucesso);
	},
	
	precoAlteradoComSucesso:function(result){
		
		if(result.listaMensagens){
			exibirMensagem(result.tipoMensagem,result.listaMensagens);
		}
		
		$("#resultado",manutencaoPublicacaoController.workspace).hide();
		$("#codigoProduto", manutencaoPublicacaoController.workspace).val("") ;
		$("#edicaoProduto", manutencaoPublicacaoController.workspace).val("");
		$("#nomeProduto", manutencaoPublicacaoController.workspace).val("");
		$("#novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
		manutencaoPublicacaoController.codigoProduto = null;
		manutencaoPublicacaoController.numeroEdicao = null;
	},
	
	isPublicacaoInformada:function(){
		
		return (manutencaoPublicacaoController.codigoProduto != null
		&& manutencaoPublicacaoController.numeroEdicao != null);
	},
	
	popupConfirmacao:function(){
		
		if(!manutencaoPublicacaoController.isPublicacaoInformada()){
			exibirMensagem("WARNING",["Informe uma publicação para alteração."]);
			return;
		}
		
		$("#dialog-confirmacao-alteracao-preco", manutencaoPublicacaoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					manutencaoPublicacaoController.confirmarAlteracaoPreco();
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
			form: $("#dialog-confirmacao-alteracao-preco", this.workspace).parents("form")
		});
	}

}, BaseController);
//@ sourceURL=manutencaoPublicacao.js
