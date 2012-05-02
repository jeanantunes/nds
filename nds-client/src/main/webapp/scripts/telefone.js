function Telefone(paramTela) {

	this.popupTelefone = function() {

		$("#manutencaoTelefones").dialog({
			resizable : false,
			height : 500,
			width : 840,
			modal : true
		});

		this.popularGrid();
	};

	this.popularGrid = function() {

		$("#telefonesGrid").flexigrid({
			preProcess : this.processarResultado,
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

		$("#telefonesGrid").flexOptions({
			url : contextPath + "/cadastro/telefone/pesquisarTelefones",
			params:[{name:'tela', value:paramTela}]
		});
	};
	
	var _this = this;

	this.processarResultado = function(data) {
		
		if (data.mensagens) {
			
			exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
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

			data.rows[i].cell[lastIndex] = _this.getActions(data.rows[i].id);
		}

		if ($('#telefonesGrid').css('display') == 'none') {

			$('#telefonesGrid').show();
		}

		if (data.result) {
			
			return data.result;
		}
		
		return data;
	};

	this.getActions = function(idTelefone) {

		return '<a href="javascript:;" onclick="' + paramTela + '.editarTelefone('
				+ idTelefone
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar telefone">'
				+ '<img src="/nds-client/images/ico_editar.gif" border="0px"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="' + paramTela + '.removerTelefone('
				+ idTelefone
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir telefone">'
				+ '<img src="/nds-client/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	};

	this.adicionarTelefone = function() {

		var data = "tela=" + paramTela + "&referencia="
				+ $("#referenciaHidden").val() + "&tipoTelefone="
				+ $("#tipoTelefone").val() + "&ddd=" + $("#ddd").val()
				+ "&numero=" + $("#numeroTelefone").val() + "&ramal="
				+ $("#ramal").val() + "&principal="
				+ ("" + $("#telefonePrincipal").attr("checked") == 'checked');

		var _this = this;
		
		$.postJSON(contextPath + "/cadastro/telefone/adicionarTelefone",
					data, 
					function(result) {
						$("#telefonesGrid").flexAddData({
							page : 1,
							total : 1,
							rows : result.rows
						});

						_this.limparCamposTelefone();

						$("#referenciaHidden").val("");

						$("#botaoAddEditar").text("Incluir Novo");
					}, 
					null, 
					true
		);
	};

	this.removerTelefone = function(referenciaTelefone) {
		var data = "tela=" + paramTela + "&referencia=" + referenciaTelefone;

		var _this = this;
		
		$("#dialog-excluir").dialog({
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
				  					$("#telefonesGrid").flexAddData({
				  						page : 1,
										total : 1,
										rows : result.rows
									});
				  				
				  					_this.limparCamposTelefone();

				  					$("#referenciaHidden").val("");

				  					$("#botaoAddEditar").text("Incluir Novo");
								}, 
								null, 
								true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

		$("#dialog-excluir").show();
	};

	this.editarTelefone = function(referenciaTelefone) {
		
		this.limparCamposTelefone();

		var data = "tela=" + paramTela +"&referencia=" + referenciaTelefone;
		
		var _this = this;
		
		$.postJSON(contextPath + "/cadastro/telefone/editarTelefone", data,
				function(result) {
					if (result != '') {
						$("#tipoTelefone").val(result.tipoTelefone);
						$("#ddd").val(result.telefone.ddd);
						$("#numeroTelefone").val(result.telefone.numero);
						$("#ramal").val(result.telefone.ramal);
						$("#telefonePrincipal").attr("checked",
								result.principal);

						$("#referenciaHidden").val(referenciaTelefone);

						$("#botaoAddEditar").text("Editar");

						_this.opcaoTel(result.tipoTelefone, 'trRamalId', 'lblRamalId', 'ramal');
					}
				}, null, true);
	};

	this.limparCamposTelefone = function() {
		$("#tipoTelefone").val("");
		$("#ddd").val("");
		$("#numeroTelefone").val("");
		$("#ramal").val("");
		$("#telefonePrincipal").attr("checked", false);
	};

	this.opcaoTel = function(opcao, idDiv, idLbl, idCampo) {
		var div1 = $("#" + idDiv);
		var lbl = $("#" + idLbl);
		var campo = $("#" + idCampo);

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
	};

	this.carregarTelefones = function() {
		this.popularGrid();
		
		var _this = this;

		$.postJSON(contextPath + "/cadastro/telefone/pesquisarTelefones",
					"tela=" + paramTela, 
					function(result) {
						$("#telefonesGrid").flexAddData({
							page : result.page,
							total : result.total,
							rows : result.rows
						});

						_this.limparCamposTelefone();

						$("#referenciaHidden").val("");

						$("#botaoAddEditar").text("Incluir Novo");
					}, 
					null, 
					true
		);
	};

	$(function() {
		$("#ddd").numeric();
		$("#numeroTelefone").numeric();
		$("#ramal").numeric();
	});
}