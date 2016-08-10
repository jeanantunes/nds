function Endereco(paramTela, paramMessage) {

	this.workspace = "";
	
	//Flag indicando se tela irá operar em modo readonly
	this.readonly = false;

	var _this = this;
    
	$(function() {
		
		var _this = this;
		
		$("#"+paramTela+"cep", Endereco.workspace).mask("99999-999");
		$("#"+paramTela+"uf", Endereco.workspace).mask("aa");
		
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).keypress(function() {
			
			var keynum = 0;
	          
	        if(window.event) {

	            keynum = event.keyCode;
	        
	        } else if(event.which) {   

	        	keynum = event.which;
	        }

			if (keynum == 13) {
				_this.incluirNovoEndereco();
			}
		});
	});

	this.init = function(workspace) {
		this.workspace = workspace;
	},
	
	//Define a tela como operação de edição/readonly
	this.definirReadonly = function(readonly) {
		this.readonly = readonly;
        var idBotaoIncluir = '#'+ paramTela +'btnIncluirNovoEndereco';
        if (this.readonly) {
			$(idBotaoIncluir).hide();
		} else {
			$(idBotaoIncluir).show();
		}
	},
	
	
	//Define a tela como operação de edição/readonly
	this.desabilitarCodigoUfIBGE = function() {
		$(".cuf", Endereco.workspace).hide();
	},
	
	this.habitarCodigoUfIBGE = function() {
		$(".cuf", Endereco.workspace).show();
	},
	this.confirmarExclusaoEndereco = function (idEndereco) {
		
		var _this = this;
		
		$( "#dialog-excluir-end", Endereco.workspace).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					_this.removerEndereco(idEndereco);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}			
		});
	},
	
	this.processarResultadoConsultaEndereco = function (data) {
                                                                                                                                                                                                                                                                                                                                                               
		if (data.mensagens) {

			exibirMensagemDialog(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens,
				paramMessage
			);

			return;
		}
		
		var i;
		
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			var principal = data.rows[i].cell[lastIndex - 1];
			
			data.rows[i].cell[lastIndex - 1] = data.rows[i].cell[lastIndex - 1] == "true" 
						? '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>'
						: '&nbsp;';

			data.rows[i].cell[lastIndex] = _this.getAction(data.rows[i].id, principal);
			
		}

		if ($("."+paramTela+"enderecosGrid", Endereco.workspace).css('display') == 'none') {

			$("."+paramTela+"enderecosGrid", Endereco.workspace).show();
		}

		return data;
	},

	this.getAction = function (idEndereco, principal) {

        var title = this.readonly ? 'Visualizar Endereco' : 'Editar Endereço';
        
		var retorno = '<a href="javascript:;" isEdicao="true" onclick="'+paramTela+'.editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="'+ title +'">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>';

		if (!this.readonly) {
			retorno += '<a href="javascript:;" isEdicao="true" class="acaoExclusao" onclick="'+paramTela+'.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
			' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
			'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
			'</a>';
		}
		
		/*retorno += '<span style="width: 80px; border: 0px; background-color: inherit;" readonly="readonly"> ' +
				'<a href="javascript:;" isEdicao="false" id="linkBloquearExclusao" ' +
					' style="cursor:pointer;border:0px;margin:5px;cursor:default;opacity:0.4" title="Excluir endereço">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a></span>';
			
		this.bloquearLink("linkBloquearExclusao");*/
		
		/*
		if(principal == "true") {
			retorno += '<span style="width: 80px; border: 0px; background-color: inherit;" readonly="readonly"> ' +
				'<a href="javascript:;" isEdicao="false" id="linkBloquearExclusao" ' +
					' style="cursor:pointer;border:0px;margin:5px;cursor:default;opacity:0.4" title="Excluir endereço">' +
					'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
					'</a></span>';
			
			this.bloquearLink("linkBloquearExclusao");
		} else {
			
			if (!this.readonly) {
				retorno += '<a href="javascript:;" isEdicao="true" class="acaoExclusao" onclick="'+paramTela+'.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
			}
		}
		
		 
		 */
		
		return retorno;
	},

	this.bloquearLink = function(idLink) {

		var link = $("#" + idLink);
		link.addClass("linkDisabled");
		link.unbind("click");
		link.css("text-decoration", "none");
	},

	
	this.popularGridEnderecos = function() {
		
		this.popularGrid(); 
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/pesquisarEnderecos',
			{tela:paramTela},
			function(result) {
				$("."+paramTela+"enderecosGrid", Endereco.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();

				_this.preencherComboUF();
				
				$("#"+paramTela+"tipoEndereco", Endereco.workspace).focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	},		
	
	this.incluirNovoEndereco = function () {

		$('.tipsy').hide();
		var formData = $("#"+paramTela+"formEnderecos :input", Endereco.workspace).serializeArray();
		$.each(formData, function(index, row) {
			row.value = row.value.toString().toUpperCase();
		});
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/incluirNovoEndereco',
			formData,
			function(result) {
				
				$("."+paramTela+"enderecosGrid", Endereco.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();
				
				$("#"+paramTela+"tipoEndereco", Endereco.workspace).focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	},

	this.editarEndereco = function(idEndereco) {
		
		if ( paramTela == 'ENDERECO_COTA')
			this.habitarCodigoUfIBGE();
		else
			this.desabilitarCodigoUfIBGE();
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).html("");
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).attr('title', 'Editar Endereço');
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).html("<img src='"+contextPath+"/images/ico_salvar.gif' hspace='5' border='0' /> ");
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).removeClass("bt_add");
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).addClass("bt_novos");
		
		var data = {tela:paramTela,idEnderecoAssociacao:idEndereco};
		
		$.postJSON(
				contextPath+'/cadastro/endereco/editarEndereco',
				data,
			function(result) {
				$("#"+paramTela+"idEndereco", Endereco.workspace).val(result.id);
				$("#"+paramTela+"enderecoid", Endereco.workspace).val(result.endereco.id);
				$("#"+paramTela+"tipoEndereco", Endereco.workspace).val(result.tipoEndereco);
				$("#"+paramTela+"cep", Endereco.workspace).val(adicionarMascaraCEP(result.endereco.cep));
				$("#"+paramTela+"tipoLogradouro option").each(function() { this.selected = (this.text.toUpperCase() == (result.endereco.tipoLogradouro ? result.endereco.tipoLogradouro.toUpperCase() : "")); });
				$("#"+paramTela+"logradouro", Endereco.workspace).val(result.endereco.logradouro);
				$("#"+paramTela+"numero", Endereco.workspace).val(result.endereco.numero);
				$("#"+paramTela+"complemento", Endereco.workspace).val(result.endereco.complemento);
				$("#"+paramTela+"bairro", Endereco.workspace).val(result.endereco.bairro);
				$("#"+paramTela+"cidade", Endereco.workspace).val(result.endereco.cidade);
				$("#"+paramTela+"uf", Endereco.workspace).val(result.endereco.uf);
				$("#"+paramTela+"codigoUf", Endereco.workspace).val(result.endereco.codigoUf);
				$("#"+paramTela+"principal", Endereco.workspace).attr("checked", result.enderecoPrincipal);
				$("#"+paramTela+"codigoBairro", Endereco.workspace).val(result.endereco.codigoBairro);
				$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val(result.endereco.codigoCidadeIBGE);
				$("#"+paramTela+"enderecoPessoa", Endereco.workspace).val(result.enderecoPessoa);
				if ( result.endereco.cep.length == 8 && result.endereco.codigoUf != undefined && result.endereco.codigoCidadeIBGE != undefined 
						&& result.endereco.codigoUf.toString() == result.endereco.codigoCidadeIBGE.toString().substring(0,2)) {
				$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', true);
				$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', true);
				} else {
					$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', false);
					$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', false);
				}

			},
			function() {
				$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', false);
				$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', false);

			},
			true,
			paramMessage
		);
	},

	this.removerEndereco = function (idEndereco) {
		
		var _this = this;
		
		var data = {tela:paramTela,idEnderecoAssociacao:idEndereco};
		
		$.postJSON(
			contextPath+'/cadastro/endereco/removerEndereco',
			data,
			function(result) {
				$("."+paramTela+"enderecosGrid", Endereco.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});		
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	},

	this.popup = function () {

		$("#"+paramTela+"manutencaoEnderecos", Endereco.workspace).dialog({
			resizable: false,
			height:640,
			width:840,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				}
			}
		});
		
		this.popularGrid();
	},
	
	this.limparFormEndereco = function () {

		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).html("Incluir Novo");
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).attr('title', 'Incluir Novo Endereço');
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).removeClass();
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).addClass("bt_add");

		$("#"+paramTela+"idEndereco", Endereco.workspace).val("");
		$("#"+paramTela+"tipoEndereco", Endereco.workspace).val("");
		$("#"+paramTela+"cep", Endereco.workspace).val("");
		$("#"+paramTela+"tipoLogradouro", Endereco.workspace).val("");
		$("#"+paramTela+"logradouro", Endereco.workspace).val("");
		$("#"+paramTela+"numero", Endereco.workspace).val("");
		$("#"+paramTela+"complemento", Endereco.workspace).val("");
		$("#"+paramTela+"bairro", Endereco.workspace).val("");
		$("#"+paramTela+"cidade", Endereco.workspace).val("");
		$("#"+paramTela+"uf", Endereco.workspace).val("");
		$("#"+paramTela+"codigoUf", Endereco.workspace).val("");
		$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val("");
		$("#"+paramTela+"principal", Endereco.workspace).attr("checked", false);
		$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', false);
		$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', false);

	},
	
	this.bloquearCamposFormEndereco = function(indBloqueiaCampo) {
		
		if(indBloqueiaCampo) {
			
			$("#"+paramTela+"wrapperBtnIncluirNovoEnderecoHabilitado", Endereco.workspace).hide();
			$("#"+paramTela+"wrapperBtnIncluirNovoEnderecoDesabilitado", Endereco.workspace).show();
			
			$("#"+paramTela+"wrapperBtnPesquisaCepHabilitado", Endereco.workspace).hide();
			$("#"+paramTela+"wrapperBtnPesquisaCepDesabilitado", Endereco.workspace).show();
			
			
		} else {

			$("#"+paramTela+"wrapperBtnIncluirNovoEnderecoHabilitado", Endereco.workspace).show();
			$("#"+paramTela+"wrapperBtnIncluirNovoEnderecoDesabilitado", Endereco.workspace).hide();
			
			$("#"+paramTela+"wrapperBtnPesquisaCepHabilitado", Endereco.workspace).show();
			$("#"+paramTela+"wrapperBtnPesquisaCepDesabilitado", Endereco.workspace).hide();
			
		}
		
		$("#"+paramTela+"idEndereco", 				Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"tipoEndereco", 			Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"cep", 						Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"tipoLogradouro", 			Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"logradouro", 				Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"numero", 					Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"complemento", 				Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"bairro", 					Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"cidade", 					Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"uf", 						Endereco.workspace).prop('disabled', indBloqueiaCampo);
		$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', true);
		$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', true);
		$("#"+paramTela+"principal", 				Endereco.workspace).prop('disabled', true);
		
	},
	
	this.pesquisarEnderecoPorCep = function () {
		
		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
	
		var cep = $("#"+paramTela+"cep").val();

	//	$("#"+paramTela+"numero", Endereco.workspace).val("");
		
	//	$("#"+paramTela+"complemento", Endereco.workspace).val("");

		
		$.postJSON(
			contextPath+'/cadastro/endereco/obterEnderecoPorCep',
			{ "cep": cep },			 
			function(result) {
				
				if (result){
				
					$("#"+paramTela+"idEndereco", Endereco.workspace).val(result.id);
					$("#"+paramTela+"tipoLogradouro", Endereco.workspace).val(result.tipoLogradouro ? result.tipoLogradouro.toUpperCase() : "");
					$("#"+paramTela+"logradouro", Endereco.workspace).val(result.logradouro);
					$("#"+paramTela+"codigoBairro", Endereco.workspace).val(result.codigoBairro);
					$("#"+paramTela+"bairro", Endereco.workspace).val(result.bairro);
					$("#"+paramTela+"uf", Endereco.workspace).val(result.uf);
					$("#"+paramTela+"codigoUf", Endereco.workspace).val(result.codigoUf);
					$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val(result.codigoCidadeIBGE);
					$("#"+paramTela+"cidade", Endereco.workspace).val(result.localidade);
					$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', true);
					$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', true);
				
				}
			},
			function() {
				
				$("#"+paramTela+"idEndereco", Endereco.workspace).val("");
				$("#"+paramTela+"tipoLogradouro", Endereco.workspace).val("");
				$("#"+paramTela+"logradouro", Endereco.workspace).val("");
				$("#"+paramTela+"codigoBairro", Endereco.workspace).val("");
				$("#"+paramTela+"bairro", Endereco.workspace).val("");
				$("#"+paramTela+"uf", Endereco.workspace).val("");
				$("#"+paramTela+"codigoUf", Endereco.workspace).val("");
				$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val("");
				$("#"+paramTela+"cidade", Endereco.workspace).val("");
				$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', false);
				$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', false);
			
			}, 
			isFromModal,
			paramMessage
		);
	},
	
	this.preencherComboUF = function (ufSelecionado) {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
		
		var idComboUF = "#" + paramTela + "uf";

		$.postJSON(
			contextPath + '/cadastro/endereco/obterDadosComboUF',
			null,
			function(result) {

				$(idComboUF, Endereco.workspace).html("");
				
				$(idComboUF, Endereco.workspace).append('<option selected="selected"></option>');

				$.each(result, function(index, value) {

					var option = "<option value='" + value + "'>" + value + "</option>";

					$(idComboUF, Endereco.workspace).append(option);	
				});
				
				if (ufSelecionado) {
					
					$(idComboUF, Endereco.workspace).val(ufSelecionado);
				}
			},
			null,
			isFromModal
		);
	},

	this.autoCompletarCep = function() {
		var cep = $("#"+paramTela+"cep", Endereco.workspace).val().replace("_","");
		
		if (cep.length == 9) {
			this.pesquisarEnderecoPorCep();
		} else {
			$("#"+paramTela+"codigoUf", 				Endereco.workspace).prop('disabled', false);
			$("#"+paramTela+"codigoCidadeIBGE", 		Endereco.workspace).prop('disabled', false);
		}
	},

	
	this.autoCompletarLocalidades = function(isOnBlur) {
		
		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}

		var idComboUF = "#" + paramTela + "uf";

		var uf = $(idComboUF, Endereco.workspace).val();

		var idCampoCidade = "#" + paramTela + "cidade";
		
		var nomeLocalidade = $(idCampoCidade, Endereco.workspace).val();
		
		if (nomeLocalidade && nomeLocalidade.length > 2 && uf) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarLocalidadePorNome',
				{ 
					'siglaUF' : uf,
					'nomeLocalidade' : nomeLocalidade
				},
				function(result) {

					if (isOnBlur) {
						
						if(result[0]) {
						
							var valor = result[0].value;

							var codigoIBGE = result[0].chave.$;

							$(idCampoCidade, Endereco.workspace).val(valor);

							$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val(codigoIBGE);
						}
						
					} else {

						$(idCampoCidade, Endereco.workspace).autocomplete({
							source: result
						});	
					}
				},
				null,
				isFromModal
			);
		}
	},

	this.autoCompletarBairros = function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
		
		var nomeBairro = $("#"+paramTela+"bairro", Endereco.workspace).val();
		
		var codigoIBGE = $("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val();
		
		if (nomeBairro && nomeBairro.length > 2 && codigoIBGE) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarBairroPorNome',
				{ 
					'codigoIBGE' : codigoIBGE,
					'nomeBairro' : nomeBairro
				},
				function(result) {

					if (isOnBlur) {
						
						if (result[0]) {
						
							var nome = result[0].value;

							var codigoBairro = result[0].chave.$;
						
							$("#"+paramTela+"bairro", Endereco.workspace).val(nome);

							$("#"+paramTela+"codigoBairro", Endereco.workspace).val(codigoBairro);
						}
						
					} else {

						$("#"+paramTela+"bairro", Endereco.workspace).autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	},
	
	this.autoCompletarLogradouros = function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}

		var codigoBairro = $("#"+paramTela+"codigoBairro", Endereco.workspace).val();
		
		var nomeLogradouros = $("#"+paramTela+"logradouro", Endereco.workspace).val();

		if (nomeLogradouros && nomeLogradouros.length > 2 && codigoBairro) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarLogradourosPorNome',
				{ 
					'codigoBairro' : codigoBairro,
					'nomeLogradouro' : nomeLogradouros
				},
				function(result) {

					if (isOnBlur) {
						
						if (result[0]) {
						
							var nome = result[0].value;

							$("#"+paramTela+"logradouro", Endereco.workspace).val(nome);
						}
					} else {

						$("#"+paramTela+"logradouro", Endereco.workspace).autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	},
	
	this.popularGrid = function(){
		
		if ( paramTela == 'ENDERECO_COTA')
			this.habitarCodigoUfIBGE();
		else
			this.desabilitarCodigoUfIBGE();
		
		var nomeGrid = paramTela +"enderecosGrid";
		
		$("."+nomeGrid, Endereco.workspace).flexigrid({
			preProcess: this.processarResultadoConsultaEndereco,
			dataType : 'json',
			colModel : [  {
				display : 'Tipo Endereço',
				name : 'tipoEndereco',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Logradouro',
				name : 'endereco.logradouro',
				width : 205,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'endereco.bairro',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cep',
				name : 'endereco.cep',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'endereco.cidade',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'enderecoPrincipal',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			width : 770,
			height : 150,
			sortorder: "asc",
			sortname: "endereco.logradouro",
			singleSelect: true, 
			onSuccess: this.gridPopuladoCallback
		});
	};
	
}

//@ sourceURL=endereco.js
