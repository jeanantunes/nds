/**
 * Transferência de Estoque de Parciais
 */

var transferenciaEstoqueParcialController = $.extend(true, {
	
	path : contextPath + '/estoque/transferencia/parcial',
	
	limparProduto : function() {
		$("#estoqueProdutoInput", transferenciaEstoqueParcialController.workspace).text('');
		$("#edicaoProdutoDestinoInput", transferenciaEstoqueParcialController.workspace).val('');
	},
	
	limparTodosCampos : function() {
		$("#codigoProdutoInput", transferenciaEstoqueParcialController.workspace).val('');
		$("#nomeProdutoInput", transferenciaEstoqueParcialController.workspace).val('');
		$("#edicaoProdutoInput", transferenciaEstoqueParcialController.workspace).val('');
		$("#estoqueProdutoInput", transferenciaEstoqueParcialController.workspace).text('');
		$("#edicaoProdutoDestinoInput", transferenciaEstoqueParcialController.workspace).val('');
	},
	
	buscarEstoqueProdutoEdicaoOrigem : function() {
		
		var data = {
			codigoProduto: $("#codigoProdutoInput", transferenciaEstoqueParcialController.workspace).val(),
			numeroEdicao: $("#edicaoProdutoInput", transferenciaEstoqueParcialController.workspace).val()
		};
		
		$.postJSON(
			transferenciaEstoqueParcialController.path + "/buscarQtdeEstoqueParaTransferenciaParcial",
			data, 
			function(result) {
				$("#estoqueProdutoInput", transferenciaEstoqueParcialController.workspace).text(result);
				$.postJSON(
						transferenciaEstoqueParcialController.path + "/validarLancamento",
						data, 
						function(result) {
						},
						function(erro) {
							if (erro.mensagens) {
								exibirMensagem(
									erro.mensagens.tipoMensagem, 
									erro.mensagens.listaMensagens
								);
								
								
							}
						}
					);
			},
			function(erro) {
				if (erro.mensagens) {
					exibirMensagem(
						erro.mensagens.tipoMensagem, 
						erro.mensagens.listaMensagens
					);
					
					transferenciaEstoqueParcialController.limparProduto();
				}
			}
		);
		
		
	},
	
	transferir : function() {
		
		var data = {
			codigoProduto: $("#codigoProdutoInput", transferenciaEstoqueParcialController.workspace).val(),
			numeroEdicaoOrigem: $("#edicaoProdutoInput", transferenciaEstoqueParcialController.workspace).val(),
			numeroEdicaoDestino: $("#edicaoProdutoDestinoInput", transferenciaEstoqueParcialController.workspace).val()
		};
		
		$.postJSON(
			transferenciaEstoqueParcialController.path + "/transferir",
			data, 
			function(result) {
				if (result) {
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					transferenciaEstoqueParcialController.limparTodosCampos();
				}
			},
			function(erro) {
				if (erro.mensagens) {
					exibirMensagem(
						erro.mensagens.tipoMensagem, 
						erro.mensagens.listaMensagens
					);
				}
			}
		);
	}
	
}, BaseController);
//@ sourceURL=transferenciaEstoqueParcial.js