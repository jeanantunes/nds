var manutencaoPublicacaoController = $.extend(true, {
	
	url:contextPath + "/financeiro/manutencaoPublicacao",
	codigoProduto:null,
	numeroEdicao:null,
	nomeProduto:null,
	
	init : function() {
		
		$("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).numeric();
		
		$('#manut-publicacao-novoPrecoProduto', manutencaoPublicacaoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		focusSelectRefField($("#codigoProduto"));
	},
	
	pesquisar: function (){
		
		manutencaoPublicacaoController.codigoProduto =  $("#manut-publicacao-codigoProduto", manutencaoPublicacaoController.workspace).val() ;
		manutencaoPublicacaoController.numeroEdicao = $("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).val();
		manutencaoPublicacaoController.nomeProduto = $("#manut-publicacao-nomeProduto", manutencaoPublicacaoController.workspace).val();
		
		var data = {codigo:manutencaoPublicacaoController.codigoProduto,
					numeroEdicao:manutencaoPublicacaoController.numeroEdicao};
		
		$.postJSON(manutencaoPublicacaoController.url + "/pesquisarProduto", 
				   data,
				   manutencaoPublicacaoController.renderizarDaodosProduto,
				   manutencaoPublicacaoController.tratraErroProduto);
	},
	
	renderizarDaodosProduto:function(precoProduto){
	
		$("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).show();
		$("#manut-publicacao-txtPrecoProduto",manutencaoPublicacaoController.workspace).text(precoProduto); 
		$("#manut-publicacao-txtLegenda",manutencaoPublicacaoController.workspace).text(manutencaoPublicacaoController.montarTituloFieldPublicacao());
		$("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
		$("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).focus();
	},
	
	montarTituloFieldPublicacao:function(){
		
		var nomePublicacao = "Publicação: " ;
		
		return nomePublicacao.concat(manutencaoPublicacaoController.codigoProduto,
									" - ",manutencaoPublicacaoController.nomeProduto,
									" - ",manutencaoPublicacaoController.numeroEdicao);
	},
	
	tratraErroProduto:function(){
		
		$("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).hide();
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
		
		$("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).hide();
		$("#manut-publicacao-codigoProduto", manutencaoPublicacaoController.workspace).val("") ;
		$("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).val("");
		$("#manut-publicacao-nomeProduto", manutencaoPublicacaoController.workspace).val("");
		$("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
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
