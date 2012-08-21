function getEnderecoController(tela,mensagem) {
	enderecoController.init(tela, mensagem);
	return enderecoController;
}

var enderecoController = $.extend(true, {

	paramTelaEndereco : "",
	paramMessageEndereco : "",

	init : function(tela,mensagem) {
		
		paramTelaEndereco = tela;
		paramMessageEndereco = mensagem;
		
		$("#"+paramTelaEndereco+"cep", enderecoController.workspace).mask("99999-999");
		$("#"+paramTelaEndereco+"uf", enderecoController.workspace).mask("aa");
		
		$("#"+paramTelaEndereco+"linkIncluirNovoEndereco", enderecoController.workspace).keypress(function() {
			
			var keynum = 0;
	          
	        if(window.event) {

	            keynum = event.keyCode;
	        
	        } else if(event.which) {   

	        	keynum = event.which;
	        }

			if (keynum == 13) {
				enderecoController.incluirNovoEndereco();
			}
		});

	},
	
	confirmarExclusaoEndereco : function (idEndereco) {
		
		$( "#dialog-excluir-end", enderecoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					enderecoController.removerEndereco(idEndereco);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}		
		});
	},

	processarResultadoConsultaEndereco : function (data) {
                                                                                                                                                                                                                                                                                                                                                                  
		if (data.mensagens) {

			exibirMensagemDialog(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens,
				paramMessageEndereco
			);

			return;
		}
		
		var i;
		
		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex - 1] =					
				data.rows[i].cell[lastIndex - 1] == "true" 
						? '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>'
						: '&nbsp;';

			data.rows[i].cell[lastIndex] = enderecoController.getAction(data.rows[i].id);
		}

		if ($("."+paramTelaEndereco+"enderecosGrid", enderecoController.workspace).css('display') == 'none') {

			$("."+paramTelaEndereco+"enderecosGrid", enderecoController.workspace).show();
		}

		return data;
	},

	getAction : function (idEndereco) {

		return '<a href="javascript:;" onclick="'+paramTelaEndereco+'.editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="'+paramTelaEndereco+'.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
	},

	popularGridEnderecos : function() {
		
		enderecoController.popularGrid(); 
		
		$.postJSON(
			contextPath+'/cadastro/endereco/pesquisarEnderecos',
			"tela=" + paramTelaEndereco,
			function(result) {
				$("."+paramTelaEndereco+"enderecosGrid", enderecoController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				enderecoController.limparFormEndereco();

				enderecoController.preencherComboUF();
				
				$("#"+paramTelaEndereco+"tipoEndereco", enderecoController.workspace).focus();
			},
			function(result) {
				
				enderecoController.processarResultadoConsultaEndereco(result);
			},
			true
		);
	},		
	
	incluirNovoEndereco : function () {

		var formData = $("#"+paramTelaEndereco+"formEnderecos :input", enderecoController.workspace).serializeArray();

		$.postJSON(
			contextPath+'/cadastro/endereco/incluirNovoEndereco',
			formData,
			function(result) {
				$("."+paramTelaEndereco+"enderecosGrid", enderecoController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				enderecoController.limparFormEndereco();
				
				$("#"+paramTelaEndereco+"tipoEndereco", enderecoController.workspace).focus();
			},
			function(result) {
				
				enderecoController.processarResultadoConsultaEndereco(result);
			},
			true,
			paramMessageEndereco
		);
	},

	editarEndereco : function(idEndereco) {
		
		$("#"+paramTelaEndereco+"linkIncluirNovoEndereco", enderecoController.workspace).html("");
		$("#"+paramTelaEndereco+"linkIncluirNovoEndereco", enderecoController.workspace).html("<img src='"+contextPath+"/images/ico_salvar.gif' hspace='5' border='0' /> Salvar");
		$("#"+paramTelaEndereco+"btnIncluirNovoEndereco", enderecoController.workspace).removeClass("bt_add");
		$("#"+paramTelaEndereco+"btnIncluirNovoEndereco", enderecoController.workspace).addClass("bt_novos");
		
		var data = "tela=" + paramTelaEndereco +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
				contextPath+'/cadastro/endereco/editarEndereco',
				data,
			function(result) {
				$("#"+paramTelaEndereco+"idEndereco", enderecoController.workspace).val(result.id);
				$("#"+paramTelaEndereco+"enderecoid", enderecoController.workspace).val(result.endereco.id);
				$("#"+paramTelaEndereco+"tipoEndereco", enderecoController.workspace).val(result.tipoEndereco);
				$("#"+paramTelaEndereco+"cep", enderecoController.workspace).val(adicionarMascaraCEP(result.endereco.cep));
				$("#"+paramTelaEndereco+"tipoLogradouro", enderecoController.workspace).val(result.endereco.tipoLogradouro);
				$("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).val(result.endereco.logradouro);
				$("#"+paramTelaEndereco+"numero", enderecoController.workspace).val(result.endereco.numero);
				$("#"+paramTelaEndereco+"complemento", enderecoController.workspace).val(result.endereco.complemento);
				$("#"+paramTelaEndereco+"bairro", enderecoController.workspace).val(result.endereco.bairro);
				$("#"+paramTelaEndereco+"cidade", enderecoController.workspace).val(result.endereco.cidade);
				$("#"+paramTelaEndereco+"uf", enderecoController.workspace).val(result.endereco.uf);
				$("#"+paramTelaEndereco+"principal", enderecoController.workspace).attr("checked", result.enderecoPrincipal);
				$("#"+paramTelaEndereco+"codigoBairro", enderecoController.workspace).val(result.endereco.codigoBairro);
				$("#"+paramTelaEndereco+"codigoCidadeIBGE", enderecoController.workspace).val(result.endereco.codigoCidadeIBGE);
			},
			null, 
			true,
			paramMessageEndereco
		);
	},

	removerEndereco : function (idEndereco) {
		
		var data = "tela=" + paramTelaEndereco +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/removerEndereco',
			data,
			function(result) {
				$("."+paramTelaEndereco+"enderecosGrid", enderecoController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});		
			},
			function(result) {
				
				enderecoController.processarResultadoConsultaEndereco(result);
			},
			true
		);
	},

	popup : function () {

		$("#"+paramTelaEndereco+"manutencaoEnderecos", enderecoController.workspace).dialog({
			resizable: false,
			height:640,
			width:840,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				},
			}
		});
		
		enderecoController.popularGrid();
	},
	
	limparFormEndereco : function () {

		$("#"+paramTelaEndereco+"linkIncluirNovoEndereco", enderecoController.workspace).html("Incluir Novo");
		$("#"+paramTelaEndereco+"btnIncluirNovoEndereco", enderecoController.workspace).removeClass();
		$("#"+paramTelaEndereco+"btnIncluirNovoEndereco", enderecoController.workspace).addClass("bt_add");

		$("#"+paramTelaEndereco+"idEndereco", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"tipoEndereco", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"cep", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"tipoLogradouro", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"numero", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"complemento", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"bairro", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"cidade", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"uf", enderecoController.workspace).val("");
		$("#"+paramTelaEndereco+"principal", enderecoController.workspace).attr("checked", false);
	},
	
	pesquisarEnderecoPorCep : function () {
		
		var isFromModal = true;
		
		if (paramMessageEndereco == "") {
			
			isFromModal = false;
		}
	
		var cep = $("#"+paramTelaEndereco+"cep", enderecoController.workspace).val();

		$.postJSON(
			contextPath+'/cadastro/endereco/obterEnderecoPorCep',
			{ "cep": cep },			 
			function(result) {
				$("#"+paramTelaEndereco+"idEndereco", enderecoController.workspace).val(result.id);
				$("#"+paramTelaEndereco+"tipoLogradouro", enderecoController.workspace).val(result.tipoLogradouro);
				$("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).val(result.logradouro);
				$("#"+paramTelaEndereco+"codigoBairro", enderecoController.workspace).val(result.codigoBairro);
				$("#"+paramTelaEndereco+"bairro", enderecoController.workspace).val(result.bairro);
				$("#"+paramTelaEndereco+"uf", enderecoController.workspace).val(result.uf);
				$("#"+paramTelaEndereco+"codigoCidadeIBGE", enderecoController.workspace).val(result.codigoCidadeIBGE);
				$("#"+paramTelaEndereco+"cidade", enderecoController.workspace).val(result.localidade);
			},
			null, 
			isFromModal,
			paramMessageEndereco
		);
	},
	
	preencherComboUF : function (ufSelecionado) {

		var isFromModal = true;
		
		if (paramMessageEndereco == "") {
			
			isFromModal = false;
		}
		
		var idComboUF = "#" + paramTelaEndereco + "uf";

		$.postJSON(
			contextPath + '/cadastro/endereco/obterDadosComboUF',
			null,
			function(result) {

				$(idComboUF, enderecoController.workspace).html("");
				
				$(idComboUF, enderecoController.workspace).append('<option selected="selected"></option>');

				$.each(result, function(index, value) {

					var option = "<option value='" + value + "'>" + value + "</option>";

					$(idComboUF, enderecoController.workspace).append(option);	
				});
				
				if (ufSelecionado) {
					
					$(idComboUF).val(ufSelecionado);
				}
			},
			null,
			isFromModal
		);
	},

	autoCompletarCep : function() {
		var cep = $("#"+paramTelaEndereco+"cep").val().replace("_","");
		
		if (cep.length == 9) {
			this.pesquisarEnderecoPorCep();
		}
	},
	
	autoCompletarLocalidades : function(isOnBlur) {
		
		var isFromModal = true;
		
		if (paramMessageEndereco == "") {
			
			isFromModal = false;
		}

		var idComboUF = "#" + paramTelaEndereco + "uf";

		var uf = $(idComboUF, enderecoController.workspace).val();

		var idCampoCidade = "#" + paramTelaEndereco + "cidade";
		
		var nomeLocalidade = $(idCampoCidade, enderecoController.workspace).val();
		
		if (nomeLocalidade && nomeLocalidade.length > 2 && uf) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarLocalidadePorNome',
				{ 
					'siglaUF' : uf,
					'nomeLocalidade' : nomeLocalidade
				},
				function(result) {

					if (isOnBlur) {

						var valor = result[0] ? result[0].value : "";

						var codigoIBGE = result[0] ? result[0].chave.$ : "";

						$(idCampoCidade, enderecoController.workspace).val(valor);

						$("#"+paramTelaEndereco+"codigoCidadeIBGE", enderecoController.workspace).val(codigoIBGE);

					} else {

						$(idCampoCidade, enderecoController.workspace).autocomplete({
							source: result
						});	
					}
				},
				null,
				isFromModal
			);
		}
	},

	autoCompletarBairros : function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessageEndereco == "") {
			
			isFromModal = false;
		}
		
		var nomeBairro = $("#"+paramTelaEndereco+"bairro", enderecoController.workspace).val();
		
		var codigoIBGE = $("#"+paramTelaEndereco+"codigoCidadeIBGE", enderecoController.workspace).val();
		
		if (nomeBairro && nomeBairro.length > 2 && codigoIBGE) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarBairroPorNome',
				{ 
					'codigoIBGE' : codigoIBGE,
					'nomeBairro' : nomeBairro
				},
				function(result) {

					if (isOnBlur) {

						var nome = result[0] ? result[0].value : "";

						var codigoBairro = result[0] ? result[0].chave.$ : "";

						$("#"+paramTelaEndereco+"bairro", enderecoController.workspace).val(nome);

						$("#"+paramTelaEndereco+"codigoBairro", enderecoController.workspace).val(codigoBairro);

					} else {

						$("#"+paramTelaEndereco+"bairro", enderecoController.workspace).autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	},
	
	autoCompletarLogradouros : function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessageEndereco == "") {
			
			isFromModal = false;
		}

		var codigoBairro = $("#"+paramTelaEndereco+"codigoBairro", enderecoController.workspace).val();
		
		var nomeLogradouros = $("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).val();

		if (nomeLogradouros && nomeLogradouros.length > 2 && codigoBairro) {

			$.postJSON(
				contextPath + '/cadastro/endereco/autoCompletarLogradourosPorNome',
				{ 
					'codigoBairro' : codigoBairro,
					'nomeLogradouro' : nomeLogradouros
				},
				function(result) {

					if (isOnBlur) {

						var nome = result[0] ? result[0].value : nomeLogradouros;

						$("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).val(nome);

					} else {

						$("#"+paramTelaEndereco+"logradouro", enderecoController.workspace).autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	},
	
	popularGrid : function(){
		
		var nomeGrid = paramTelaEndereco +"enderecosGrid";
		
		$("."+nomeGrid, enderecoController.workspace).flexigrid({
			preProcess: enderecoController.processarResultadoConsultaEndereco,
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
			singleSelect: true
		});
	}
	
}, BaseController);