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

			data.rows[i].cell[lastIndex - 1] =					
				data.rows[i].cell[lastIndex - 1] == "true" 
						? '<img src="'+contextPath+'/images/ico_check.gif" border="0px"/>'
						: '&nbsp;';

			data.rows[i].cell[lastIndex] = _this.getAction(data.rows[i].id);
			
		}

		if ($("."+paramTela+"enderecosGrid", Endereco.workspace).css('display') == 'none') {

			$("."+paramTela+"enderecosGrid", Endereco.workspace).show();
		}

		return data;
	},

	this.getAction = function (idEndereco) {

        var title = this.readonly ? 'Visualizar Endereco' : 'Editar Endereço';

		var retorno = '<a href="javascript:;" onclick="'+paramTela+'.editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="'+ title +'">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>';

		if (!this.readonly) {
			retorno += '<a href="javascript:;" class="acaoExclusao" onclick="'+paramTela+'.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
			' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
			'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
			'</a>';
		}
		return retorno;
	},

	this.popularGridEnderecos = function() {
		
		this.popularGrid(); 
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/pesquisarEnderecos',
			"tela=" + paramTela,
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

		var formData = $("#"+paramTela+"formEnderecos :input", Endereco.workspace).serializeArray();
		
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
		
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).html("");
		$("#"+paramTela+"linkIncluirNovoEndereco", Endereco.workspace).html("<img src='"+contextPath+"/images/ico_salvar.gif' hspace='5' border='0' /> Salvar");
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).removeClass("bt_add");
		$("#"+paramTela+"btnIncluirNovoEndereco", Endereco.workspace).addClass("bt_novos");
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
				contextPath+'/cadastro/endereco/editarEndereco',
				data,
			function(result) {
				$("#"+paramTela+"idEndereco", Endereco.workspace).val(result.id);
				$("#"+paramTela+"enderecoid", Endereco.workspace).val(result.endereco.id);
				$("#"+paramTela+"tipoEndereco", Endereco.workspace).val(result.tipoEndereco);
				$("#"+paramTela+"cep", Endereco.workspace).val(adicionarMascaraCEP(result.endereco.cep));
				$("#"+paramTela+"tipoLogradouro", Endereco.workspace).val(result.endereco.tipoLogradouro);
				$("#"+paramTela+"logradouro", Endereco.workspace).val(result.endereco.logradouro);
				$("#"+paramTela+"numero", Endereco.workspace).val(result.endereco.numero);
				$("#"+paramTela+"complemento", Endereco.workspace).val(result.endereco.complemento);
				$("#"+paramTela+"bairro", Endereco.workspace).val(result.endereco.bairro);
				$("#"+paramTela+"cidade", Endereco.workspace).val(result.endereco.cidade);
				$("#"+paramTela+"uf", Endereco.workspace).val(result.endereco.uf);
				$("#"+paramTela+"principal", Endereco.workspace).attr("checked", result.enderecoPrincipal);
				$("#"+paramTela+"codigoBairro", Endereco.workspace).val(result.endereco.codigoBairro);
				$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val(result.endereco.codigoCidadeIBGE);
			},
			null, 
			true,
			paramMessage
		);
	},

	this.removerEndereco = function (idEndereco) {
		
		var _this = this;
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
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
		$("#"+paramTela+"principal", Endereco.workspace).attr("checked", false);
	},
	
	this.pesquisarEnderecoPorCep = function () {
		
		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
	
		var cep = $("#"+paramTela+"cep").val();

		$.postJSON(
			contextPath+'/cadastro/endereco/obterEnderecoPorCep',
			{ "cep": cep },			 
			function(result) {
				
					$("#"+paramTela+"idEndereco", Endereco.workspace).val(result.id);
					$("#"+paramTela+"tipoLogradouro", Endereco.workspace).val(result.tipoLogradouro);
					$("#"+paramTela+"logradouro", Endereco.workspace).val(result.logradouro);
					$("#"+paramTela+"codigoBairro", Endereco.workspace).val(result.codigoBairro);
					$("#"+paramTela+"bairro", Endereco.workspace).val(result.bairro);
					$("#"+paramTela+"uf", Endereco.workspace).val(result.uf);
					$("#"+paramTela+"codigoCidadeIBGE", Endereco.workspace).val(result.codigoCidadeIBGE);
					$("#"+paramTela+"cidade", Endereco.workspace).val(result.localidade);
				
			},
			null, 
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
