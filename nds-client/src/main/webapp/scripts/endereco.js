function Endereco(paramTela,paramMessage) {

	this.confirmarExclusaoEndereco = function (idEndereco) {
		
		var _this = this;
		
		$( "#dialog-excluir-end" ).dialog({
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
	};

	var _this = this;
	
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
			
			console.log(data);
		}

		if ($("."+paramTela+"enderecosGrid").css('display') == 'none') {

			$("."+paramTela+"enderecosGrid").show();
		}

		return data;
	};

	this.getAction = function (idEndereco) {

		return '<a href="javascript:;" onclick="'+paramTela+'.editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="'+contextPath+'/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="'+paramTela+'.confirmarExclusaoEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="'+contextPath+'/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
	};

	this.popularGridEnderecos = function() {
		
		this.popularGrid(); 
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/pesquisarEnderecos',
			"tela=" + paramTela,
			function(result) {
				$("."+paramTela+"enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();

				_this.preencherComboUF();
				
				$("#"+paramTela+"tipoEndereco").focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	};		
	
	this.incluirNovoEndereco = function () {

		var formData = $("#"+paramTela+"formEnderecos").serializeArray();
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/incluirNovoEndereco',
			formData,
			function(result) {
				$("."+paramTela+"enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();
				
				$("#"+paramTela+"tipoEndereco").focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true,
			paramMessage
		);
	};

	this.editarEndereco = function(idEndereco) {
		
		$("#"+paramTela+"linkIncluirNovoEndereco").html("");
		$("#"+paramTela+"linkIncluirNovoEndereco").html("<img src='"+contextPath+"/images/ico_salvar.gif' hspace='5' border='0' /> Salvar");
		$("#"+paramTela+"btnIncluirNovoEndereco").removeClass("bt_add");
		$("#"+paramTela+"btnIncluirNovoEndereco").addClass("bt_novos");
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
				contextPath+'/cadastro/endereco/editarEndereco',
				data,
			function(result) {
				$("#"+paramTela+"idEndereco").val(result.id);
				$("#"+paramTela+"enderecoid").val(result.endereco.id);
				$("#"+paramTela+"tipoEndereco").val(result.tipoEndereco);
				$("#"+paramTela+"cep").val(adicionarMascaraCEP(result.endereco.cep));
				$("#"+paramTela+"tipoLogradouro").val(result.endereco.tipoLogradouro);
				$("#"+paramTela+"logradouro").val(result.endereco.logradouro);
				$("#"+paramTela+"numero").val(result.endereco.numero);
				$("#"+paramTela+"complemento").val(result.endereco.complemento);
				$("#"+paramTela+"bairro").val(result.endereco.bairro);
				$("#"+paramTela+"cidade").val(result.endereco.cidade);
				$("#"+paramTela+"uf").val(result.endereco.uf);
				$("#"+paramTela+"principal").attr("checked", result.enderecoPrincipal);
				$("#"+paramTela+"codigoBairro").val(result.endereco.codigoBairro);
				$("#"+paramTela+"codigoCidadeIBGE").val(result.endereco.codigoCidadeIBGE);
			},
			null, 
			true,
			paramMessage
		);
	};

	this.removerEndereco = function (idEndereco) {
		
		var _this = this;
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/removerEndereco',
			data,
			function(result) {
				$("."+paramTela+"enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});		
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	};

	this.popup = function () {

		$("#"+paramTela+"manutencaoEnderecos").dialog({
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
		
		this.popularGrid();
	};
	
	this.limparFormEndereco = function () {

		$("#"+paramTela+"linkIncluirNovoEndereco").html("Incluir Novo");
		$("#"+paramTela+"btnIncluirNovoEndereco").removeClass();
		$("#"+paramTela+"btnIncluirNovoEndereco").addClass("bt_add");

		$("#"+paramTela+"idEndereco").val("");
		$("#"+paramTela+"tipoEndereco").val("");
		$("#"+paramTela+"cep").val("");
		$("#"+paramTela+"tipoLogradouro").val("");
		$("#"+paramTela+"logradouro").val("");
		$("#"+paramTela+"numero").val("");
		$("#"+paramTela+"complemento").val("");
		$("#"+paramTela+"bairro").val("");
		$("#"+paramTela+"cidade").val("");
		$("#"+paramTela+"uf").val("");
		$("#"+paramTela+"principal").attr("checked", false);
	};
	
	$(function() {
		
		var _this = this;
		
		$("#"+paramTela+"cep").mask("99999-999");
		$("#"+paramTela+"uf").mask("aa");
		
		$("#"+paramTela+"linkIncluirNovoEndereco").keypress(function() {
			
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
				$("#"+paramTela+"idEndereco").val(result.id);
				$("#"+paramTela+"tipoLogradouro").val(result.tipoLogradouro);
				$("#"+paramTela+"logradouro").val(result.logradouro);
				$("#"+paramTela+"codigoBairro").val(result.codigoBairro);
				$("#"+paramTela+"bairro").val(result.bairro);
				$("#"+paramTela+"uf").val(result.uf);
				$("#"+paramTela+"codigoCidadeIBGE").val(result.codigoCidadeIBGE);
				$("#"+paramTela+"cidade").val(result.localidade);
			},
			null, 
			isFromModal,
			paramMessage
		);
	};
	
	this.preencherComboUF = function () {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
		
		var idComboUF = "#" + paramTela + "uf";

		$.postJSON(
			contextPath + '/cadastro/endereco/obterDadosComboUF',
			null,
			function(result) {

				$(idComboUF).html("");
				
				$(idComboUF).append('<option selected="selected"></option>');

				$.each(result, function(index, value) {

					var option = "<option value='" + value + "'>" + value + "</option>";

					$(idComboUF).append(option);	
				});
			},
			null,
			isFromModal
		);
	}

	this.autoCompletarLocalidades = function(isOnBlur) {
		
		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}

		var idComboUF = "#" + paramTela + "uf";

		var uf = $(idComboUF).val();

		var idCampoCidade = "#" + paramTela + "cidade";
		
		var nomeLocalidade = $(idCampoCidade).val();
		
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

						$(idCampoCidade).val(valor);

						$("#"+paramTela+"codigoCidadeIBGE").val(codigoIBGE);

					} else {

						$(idCampoCidade).autocomplete({
							source: result
						});	
					}
				},
				null,
				isFromModal
			);
		}
	}

	this.autoCompletarBairros = function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}
		
		var nomeBairro = $("#"+paramTela+"bairro").val();
		
		var codigoIBGE = $("#"+paramTela+"codigoCidadeIBGE").val();
		
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

						$("#"+paramTela+"bairro").val(nome);

						$("#"+paramTela+"codigoBairro").val(codigoBairro);

					} else {

						$("#"+paramTela+"bairro").autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	}
	
	this.autoCompletarLogradouros = function(isOnBlur) {

		var isFromModal = true;
		
		if (paramMessage == "") {
			
			isFromModal = false;
		}

		var codigoBairro = $("#"+paramTela+"codigoBairro").val();
		
		var nomeLogradouros = $("#"+paramTela+"logradouro").val();

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

						$("#"+paramTela+"logradouro").val(nome);

					} else {

						$("#"+paramTela+"logradouro").autocomplete({
							source: result
						});	
					}					
				},
				null,
				isFromModal
			);
		}
	}
	
	this.popularGrid = function(){
		
		var nomeGrid = paramTela +"enderecosGrid";
		
		$("."+nomeGrid).flexigrid({
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
			singleSelect: true
		});
	};
	
}