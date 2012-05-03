function Endereco(paramTela) {

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
				data.mensagens.listaMensagens
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

		if ($(".enderecosGrid").css('display') == 'none') {

			$(".enderecosGrid").show();
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
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();
				
				$("#tipoEndereco").focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	};		
	
	this.incluirNovoEndereco = function () {

		var formData = $("#formEnderecos").serializeArray();
		
		var _this = this;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/incluirNovoEndereco',
			formData,
			function(result) {
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				_this.limparFormEndereco();
				
				$("#tipoEndereco").focus();
			},
			function(result) {
				
				_this.processarResultadoConsultaEndereco(result);
			},
			true
		);
	};

	this.editarEndereco = function(idEndereco) {

		$("#linkIncluirNovoEndereco").html("<img src='"+contextPath+"/images/ico_salvar.gif' hspace='5' border='0' /> Salvar");
		$("#btnIncluirNovoEndereco").removeClass();
		$("#btnIncluirNovoEndereco").addClass("bt_novos");
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
				contextPath+'/cadastro/endereco/editarEndereco',
				data,
			function(result) {
				$("#idEndereco").val(result.id);
				$("#tipoEndereco").val(result.tipoEndereco);
				$("#cep").val(result.endereco.cep);
				$("#tipoLogradouro").val(result.endereco.tipoLogradouro);
				$("#logradouro").val(result.endereco.logradouro);
				$("#numero").val(result.endereco.numero);
				$("#complemento").val(result.endereco.complemento);
				$("#bairro").val(result.endereco.bairro);
				$("#cidade").val(result.endereco.cidade);
				$("#uf").val(result.endereco.uf);
				$("#principal").attr("checked", result.enderecoPrincipal);
			},
			null, 
			true
		);
	};

	this.removerEndereco = function (idEndereco) {
		
		var _this = this;
		
		var data = "tela=" + paramTela +"&idEnderecoAssociacao=" + idEndereco;
		
		$.postJSON(
			contextPath+'/cadastro/endereco/removerEndereco',
			data,
			function(result) {
				$(".enderecosGrid").flexAddData({
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

		$("#manutencaoEnderecos").dialog({
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

		$("#linkIncluirNovoEndereco").html("Incluir Novo");
		$("#btnIncluirNovoEndereco").removeClass();
		$("#btnIncluirNovoEndereco").addClass("bt_add");

		$("#idEndereco").val("");
		$("#tipoEndereco").val("");
		$("#cep").val("");
		$("#tipoLogradouro").val("");
		$("#logradouro").val("");
		$("#numero").val("");
		$("#complemento").val("");
		$("#bairro").val("");
		$("#cidade").val("");
		$("#uf").val("");
		$("#principal").attr("checked", false);
	};
	
	$(function() {
		
		var _this = this;
		
		$("#cep").mask("99999-999");
		$("#uf").mask("aa");
		$("#numero").numeric();
		
		$("#linkIncluirNovoEndereco").keypress(function() {
			
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
	
		var cep =  $("#cep").val();
		
		$.postJSON(
				contextPath+'/cadastro/endereco/obterEnderecoPorCep',
			{ "cep": cep },			 
			function(result) {
				$("#idEndereco").val(result.id);
				$("#tipoEndereco").val(result.tipoEndereco);
				$("#cep").val(result.endereco.cep);
				$("#tipoLogradouro").val(result.endereco.tipoLogradouro);
				$("#logradouro").val(result.endereco.logradouro);
				$("#numero").val(result.endereco.numero);
				$("#complemento").val(result.endereco.complemento);
				$("#bairro").val(result.endereco.bairro);
				$("#cidade").val(result.endereco.cidade);
				$("#uf").val(result.endereco.uf);
				$("#principal").attr("checked", result.enderecoPrincipal);
			},
			null, 
			true
		);
	};
	
	this.popularGrid = function(){
		
		$(".enderecosGrid").flexigrid({
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