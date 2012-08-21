function getTelefoneController(tela,mensagem) {
	telefoneController.init(tela, mensagem);
	return telefoneController;
}

var telefoneController = $.extend(true, {

	paramTelaTelefone : "",
	messageTelefone : "",

	init : function(tela,mensagem) {
		
		paramTelaTelefone = tela;
		messageTelefone = mensagem;

		$("#"+paramTelaTelefone+"ddd", telefoneController.workspace).numeric();
		$("#"+paramTelaTelefone+"numeroTelefone", telefoneController.workspace).numeric();
		$("#"+paramTelaTelefone+"numeroTelefone", telefoneController.workspace).mask("9999-9999");
		$("#"+paramTelaTelefone+"ramal", telefoneController.workspace).numeric();
		
	},
	
	popupTelefone : function() {

		$("#"+paramTelaTelefone+"manutencaoTelefones", telefoneController.workspace).dialog({
			resizable : false,
			height : 500,
			width : 840,
			modal : true
		});

		telefoneController.popularGrid();
	},

	popularGrid : function() {

		$("#"+paramTelaTelefone+"telefonesGrid", telefoneController.workspace).flexigrid({
			preProcess : telefoneController.processarResultado,
			dataType : 'json',
			colModel : [ {
				display : 'Tipo Telefone',
				name : 'tipoTelefone',
				width : 165,
				sortable : true,
				align : 'left'
			}, {
				display : 'DDD',
				name : 'telefone.ddd',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Número',
				name : 'telefone.numero',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ramal / ID',
				name : 'telefone.ramal',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Principal',
				name : 'principal',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			} ],
			width : 770,
			height : 150,
			disableSelect : true,
			sortorder : "asc",
			sortname : "tipoTelefone"
		});

		$("#"+paramTelaTelefone+"telefonesGrid", telefoneController.workspace).flexOptions({
			url : contextPath + "/cadastro/telefone/pesquisarTelefones",
			params:[{name:'tela', value:paramTelaTelefone}]
		});
	},

	processarResultado : function(data) {
		
		if (data.mensagens) {
			
			exibirMensagemDialog(data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					messageTelefone);
			
			return;
		}

		if (data.result) {
			
			data.rows = data.result.rows;
		}

		var i;

		for (i = 0; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex - 1] = data.rows[i].cell[lastIndex - 1] == "true" ? '<img src="/nds-client/images/ico_check.gif" border="0px"/>'
					: '&nbsp;';

			data.rows[i].cell[lastIndex] = telefoneController.getActions(data.rows[i].id);
		}

		if ($('#telefonesGrid', telefoneController.workspace).css('display') == 'none') {

			$('#telefonesGrid', telefoneController.workspace).show();
		}

		if (data.result) {
			
			return data.result;
		}
		
		return data;
	},

	getActions : function(idTelefone) {

		return '<a href="javascript:;" onclick="' + paramTelaTelefone + '.editarTelefone('
				+ idTelefone
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar telefone">'
				+ '<img src="/nds-client/images/ico_editar.gif" border="0px"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="' + paramTelaTelefone + '.removerTelefone('
				+ idTelefone
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir telefone">'
				+ '<img src="/nds-client/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	},

	adicionarTelefone : function() {

		var data = "tela=" + paramTelaTelefone + "&referencia="
				+ $("#"+paramTelaTelefone+"referenciaHidden", telefoneController.workspace).val() + "&tipoTelefone="
				+ $("#"+paramTelaTelefone+"tipoTelefone", telefoneController.workspace).val() + "&ddd=" + $("#"+paramTelaTelefone+"ddd", telefoneController.workspace).val()
				+ "&numero=" + $("#"+paramTelaTelefone+"numeroTelefone", telefoneController.workspace).val() + "&ramal="
				+ $("#"+paramTelaTelefone+"ramal", telefoneController.workspace).val() + "&principal="
				+ ("" + $("#"+paramTelaTelefone+"telefonePrincipal", telefoneController.workspace).attr("checked") == 'checked');

		$.postJSON(contextPath + "/cadastro/telefone/adicionarTelefone",
					data, 
					function(result) {
						$("#"+paramTelaTelefone+"telefonesGrid", telefoneController.workspace).flexAddData({
							page : 1,
							total : 1,
							rows : result.rows
						});

						telefoneController.limparCamposTelefone();

						$("#"+paramTelaTelefone+"referenciaHidden", telefoneController.workspace).val("");

						$("#"+paramTelaTelefone+"botaoAddEditar", telefoneController.workspace).text("Incluir Novo");
					}, 
					null, 
					true,
					messageTelefone
		);
	},

	removerTelefone : function(referenciaTelefone) {
		var data = "tela=" + paramTelaTelefone + "&referencia=" + referenciaTelefone;

		$("#"+paramTelaTelefone+"dialog-excluir", telefoneController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 300,
			modal : true,
			buttons : {
				"Confirmar" : 
					function() {
					$(this).dialog("close");
					
					$.postJSON(contextPath + "/cadastro/telefone/removerTelefone",
				  				data,
				  				function(result) {
				  					$("#"+paramTelaTelefone+"telefonesGrid", telefoneController.workspace).flexAddData({
				  						page : 1,
										total : 1,
										rows : result.rows
									});
				  				
				  					telefoneController.limparCamposTelefone();

				  					$("#"+paramTelaTelefone+"referenciaHidden", telefoneController.workspace).val("");

				  					$("#"+paramTelaTelefone+"botaoAddEditar", telefoneController.workspace).text("Incluir Novo");
								}, 
								null, 
								true,
								messageTelefone
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

		$("#"+paramTelaTelefone+"dialog-excluir", telefoneController.workspace).show();
	},

	editarTelefone : function(referenciaTelefone) {
		
		telefoneController.limparCamposTelefone();

		var data = "tela=" + paramTelaTelefone +"&referencia=" + referenciaTelefone;
		
		$.postJSON(contextPath + "/cadastro/telefone/editarTelefone", data,
				function(result) {
					if (result != '') {
						$("#"+paramTelaTelefone+"tipoTelefone", telefoneController.workspace).val(result.tipoTelefone);
						$("#"+paramTelaTelefone+"ddd", telefoneController.workspace).val(result.telefone.ddd);
						$("#"+paramTelaTelefone+"numeroTelefone", telefoneController.workspace).val(result.telefone.numero);
						$("#"+paramTelaTelefone+"ramal", telefoneController.workspace).val(result.telefone.ramal);
						$("#"+paramTelaTelefone+"telefonePrincipal", telefoneController.workspace).attr("checked",
								result.principal);

						$("#"+paramTelaTelefone+"referenciaHidden", telefoneController.workspace).val(referenciaTelefone);

						$("#"+paramTelaTelefone+"botaoAddEditar", telefoneController.workspace).text("Editar");

						telefoneController.opcaoTel(result.tipoTelefone, 'trRamalId', 'lblRamalId', 'ramal');
					}
				}, null, true, messageTelefone);
	},

	limparCamposTelefone : function() {
		$("#"+paramTelaTelefone+"tipoTelefone", telefoneController.workspace).val("");
		$("#"+paramTelaTelefone+"ddd", telefoneController.workspace).val("");
		$("#"+paramTelaTelefone+"numeroTelefone", telefoneController.workspace).val("");
		$("#"+paramTelaTelefone+"ramal", telefoneController.workspace).val("");
		$("#"+paramTelaTelefone+"telefonePrincipal", telefoneController.workspace).attr("checked", false);
	},

	opcaoTel : function(opcao, idDiv, idLbl, idCampo) {
		var div1 = $("#"+paramTelaTelefone+ idDiv, telefoneController.workspace);
		var lbl = $("#"+paramTelaTelefone + idLbl, telefoneController.workspace);
		var campo = $("#"+paramTelaTelefone + idCampo, telefoneController.workspace);

		switch (opcao) {
		case 'COMERCIAL':
		case 'FAX':
			div1.show();
			lbl.text('Ramal:');
			campo.css('width', 40);
			break;
		case 'RADIO':
			div1.show();
			lbl.text('ID:');
			campo.css('width', 167);
			break;
		default:
			div1.hide();
			campo.val("");
			break;
		}
	},

	carregarTelefones : function() {
		telefoneController.popularGrid();
		
		$.postJSON(contextPath + "/cadastro/telefone/pesquisarTelefones",
					"tela=" + paramTelaTelefone, 
					function(result) {
						$("#"+paramTelaTelefone+"telefonesGrid", telefoneController.workspace).flexAddData({
							page : result.page,
							total : result.total,
							rows : result.rows
						});

						telefoneController.limparCamposTelefone();

						$("#"+paramTelaTelefone+"referenciaHidden", telefoneController.workspace).val("");

						$("#"+paramTelaTelefone+"botaoAddEditar", telefoneController.workspace).text("Incluir Novo");
					}, 
					null, 
					true,
					messageTelefone
		);
	}
});