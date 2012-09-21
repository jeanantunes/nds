function Telefone(paramTela,message) {

	this.workspace = "";
    //Flag indicando se tela irá operar em modo cadastro/consulta
    this.readonly = false;
	
	this.init = function(workspace) {
		this.workspace = workspace;
		
		$("#"+paramTela+"wrapperBtnAdicionarTelefoneHabilitado").show();
		
		$("#"+paramTela+"ddd", Telefone.workspace).numeric();
		$("#"+paramTela+"numeroTelefone", Telefone.workspace).numeric();
		$("#"+paramTela+"numeroTelefone", Telefone.workspace).mask("9999-9999");
		$("#"+paramTela+"ramal", Telefone.workspace).numeric();

	};

    //Define a tela como operação de cadastro/consulta
    this.definirReadonly = function(readonly) {
        this.readonly = readonly;
        var idBotaoIncluir = '#'+ paramTela + 'wrapperBtnAdicionarTelefoneHabilitado';
        if (this.readonly) {
            $(idBotaoIncluir).hide();
        } else {
            $(idBotaoIncluir).show();
        }
    };
	
	this.popupTelefone = function() {

		$("#"+paramTela+"manutencaoTelefones", Telefone.workspace).dialog({
			resizable : false,
			height : 500,
			width : 840,
			modal : true
		});

		this.popularGrid();
	};

	this.popularGrid = function() {

		$("#"+paramTela+"telefonesGrid", Telefone.workspace).flexigrid({
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

		$("#"+paramTela+"telefonesGrid", Telefone.workspace).flexOptions({
			url : contextPath + "/cadastro/telefone/pesquisarTelefones",
			params:[{name:'tela', value:paramTela}]
		});
	};
	
	var _this = this;

	this.processarResultado = function(data) {
		
		if (data.mensagens) {
			
			exibirMensagemDialog(data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					message);
			
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

		if ($('#telefonesGrid', Telefone.workspace).css('display') == 'none') {

			$('#telefonesGrid', Telefone.workspace).show();
		}

		if (data.result) {
			
			return data.result;
		}
		
		return data;
	};

	this.getActions = function(idTelefone) {

        var title = this.readonly ? 'Visualizar Telefone' : 'Editar Telefone';

        var retorno = '<a href="javascript:;" onclick="' + paramTela + '.editarTelefone('
				+ idTelefone
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="'+ title +'">'
				+ '<img src="/nds-client/images/ico_editar.gif" border="0px"/>'
				+ '</a>';

        if (!this.readonly) {
            retorno+='<a href="javascript:;" onclick="' + paramTela + '.removerTelefone('
                + idTelefone
                + ')" '
                + ' style="cursor:pointer;border:0px;margin:5px" title="Excluir telefone">'
                + '<img src="/nds-client/images/ico_excluir.gif" border="0px"/>'
                + '</a>';
        }
        return retorno;
	};

	this.adicionarTelefone = function() {

		var data = "tela=" + paramTela + "&referencia="
				+ $("#"+paramTela+"referenciaHidden", Telefone.workspace).val() + "&tipoTelefone="
				+ $("#"+paramTela+"tipoTelefone", Telefone.workspace).val() + "&ddd=" + $("#"+paramTela+"ddd", Telefone.workspace).val()
				+ "&numero=" + $("#"+paramTela+"numeroTelefone", Telefone.workspace).val() + "&ramal="
				+ $("#"+paramTela+"ramal", Telefone.workspace).val() + "&principal="
				+ ("" + $("#"+paramTela+"telefonePrincipal", Telefone.workspace).attr("checked") == 'checked');

		var _this = this;
		
		$.postJSON(contextPath + "/cadastro/telefone/adicionarTelefone",
					data, 
					function(result) {
						$("#"+paramTela+"telefonesGrid", Telefone.workspace).flexAddData({
							page : 1,
							total : 1,
							rows : result.rows
						});

						_this.limparCamposTelefone();

						$("#"+paramTela+"referenciaHidden", Telefone.workspace).val("");

						$("#"+paramTela+"botaoAddEditar", Telefone.workspace).text("Incluir Novo");
					}, 
					null, 
					true,
					message
		);
	};

	this.removerTelefone = function(referenciaTelefone) {
		var data = "tela=" + paramTela + "&referencia=" + referenciaTelefone;

		var _this = this;
		
		$("#"+paramTela+"dialog-excluir", Telefone.workspace).dialog({
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
				  					$("#"+paramTela+"telefonesGrid", Telefone.workspace).flexAddData({
				  						page : 1,
										total : 1,
										rows : result.rows
									});
				  				
				  					_this.limparCamposTelefone();

				  					$("#"+paramTela+"referenciaHidden", Telefone.workspace).val("");

				  					$("#"+paramTela+"botaoAddEditar", Telefone.workspace).text("Incluir Novo");
								}, 
								null, 
								true,
								message
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

		$("#"+paramTela+"dialog-excluir", Telefone.workspace).show();
	};

	this.editarTelefone = function(referenciaTelefone) {
		
		this.limparCamposTelefone();

		var data = "tela=" + paramTela +"&referencia=" + referenciaTelefone;
		
		var _this = this;
		
		$.postJSON(contextPath + "/cadastro/telefone/editarTelefone", data,
				function(result) {
					if (result != '') {
						$("#"+paramTela+"tipoTelefone", Telefone.workspace).val(result.tipoTelefone);
						$("#"+paramTela+"ddd", Telefone.workspace).val(result.telefone.ddd);
						$("#"+paramTela+"numeroTelefone", Telefone.workspace).val(result.telefone.numero);
						$("#"+paramTela+"ramal", Telefone.workspace).val(result.telefone.ramal);
						$("#"+paramTela+"telefonePrincipal", Telefone.workspace).attr("checked",
								result.principal);

						$("#"+paramTela+"referenciaHidden", Telefone.workspace).val(referenciaTelefone);

						$("#"+paramTela+"botaoAddEditar", Telefone.workspace).text("Editar");

						_this.opcaoTel(result.tipoTelefone, 'trRamalId', 'lblRamalId', 'ramal');
					}
				}, null, true, message);
	};

	this.limparCamposTelefone = function() {
		$("#"+paramTela+"tipoTelefone").val("");
		$("#"+paramTela+"ddd").val("");
		$("#"+paramTela+"numeroTelefone").val("");
		$("#"+paramTela+"ramal").val("");
		$("#"+paramTela+"telefonePrincipal").attr("checked", false);
	};

	this.bloquearCamposFormTelefone = function(indBloqueiaCampo) {
		
		if(indBloqueiaCampo) {
			$("#"+paramTela+"wrapperBtnAdicionarTelefoneHabilitado").hide();
			$("#"+paramTela+"wrapperBtnAdicionarTelefoneDesabilitado").show();
		} else {
			$("#"+paramTela+"wrapperBtnAdicionarTelefoneHabilitado").show();
			$("#"+paramTela+"wrapperBtnAdicionarTelefoneDesabilitado").hide();
		}		
		
		$("#"+paramTela+"tipoTelefone").prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"ddd").prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"numeroTelefone").prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"ramal").prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"telefonePrincipal").prop('disabled', indBloqueiaCampo);
		
	};
	
	this.opcaoTel = function(opcao, idDiv, idLbl, idCampo) {
		var div1 = $("#"+paramTela+ idDiv);
		var lbl = $("#"+paramTela + idLbl);
		var campo = $("#"+paramTela + idCampo);

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
						$("#"+paramTela+"telefonesGrid").flexAddData({
							page : result.page,
							total : result.total,
							rows : result.rows
						});

						_this.limparCamposTelefone();

						$("#"+paramTela+"referenciaHidden").val("");

						$("#"+paramTela+"botaoAddEditar").text("Incluir Novo");
					}, 
					null, 
					true,
					message
		);
	};

}