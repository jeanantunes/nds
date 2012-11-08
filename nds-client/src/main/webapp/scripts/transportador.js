var transportadorController = $.extend(true, {
	fecharModalCadastroTransp : false,

	init : function () {
			$("#tabs", transportadorController.workspace).tabs();
			
			$(".transportadoraGrid", transportadorController.workspace).flexigrid({
				dataType : 'json',
				preProcess: function(data){
					if(typeof data.mensagens == "object") {
						
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						
						$(".divTransportadoraGrid", transportadorController.workspace).hide();
						
					}else{
						
						$.each(data.rows, function(index, value) {						
							
							var idTransportadora = value.cell.id;								
							var acao = '<a href="javascript:;" onclick="transportadorController.editarTransportadora('+idTransportadora+');"><img src="'+contextPath+'/images/ico_editar.gif" border="0" hspace="5" />';
							acao +='</a> <a href="javascript:;" onclick="transportadorController.excluirTransportadora('+idTransportadora+');""><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" /></a>';
							
							value.cell.acao = acao;
							
							var cnpj = value.cell.pessoaJuridica.cnpj;
							
							cnpj = cnpj.replace("-", "").replace(".", "").replace(".", "").replace("/", "");
							
							value.cell.cnpj = 
								cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
							
							value.cell.razaoSocial = value.cell.pessoaJuridica.razaoSocial;
							value.cell.email = value.cell.pessoaJuridica.email;
							
							if (value.cell.pessoaJuridica.telefones[0]){
								
								value.cell.telefone = value.cell.pessoaJuridica.telefones[0].ddd + " - " + 
									value.cell.pessoaJuridica.telefones[0].numero;
							} else {
								
								value.cell.telefone = "";
							}
						});
						
						$(".divTransportadoraGrid", transportadorController.workspace).show();
						
						return data;	
					}
				},
				colModel : [ {
					display : 'Código',
					name : 'id',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Razão Social',
					name : 'razaoSocial',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ',
					name : 'cnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Responsável',
					name : 'responsavel',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				} ],
				sortname : "id",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto',
				disableSelect: true
			});
		
			$(".associacaoGrid").flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {
						
						var idAssoc = value.id;
						
						var acao = '<a href="javascript:;" onclick="transportadorController.excluirAssociacao('+ idAssoc +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
						value.cell.acao = acao;
						
						var tipoVeiculo = value.cell.veiculo.tipoVeiculo;
						var placa = value.cell.veiculo.placa;
						var nome = value.cell.motorista.nome;
						var cnh = value.cell.motorista.cnh;
						var rota = value.cell.rota.descricaoRota;
						var roteiro = value.cell.rota.descricaoRoteiro;
						
						value.cell.veiculo = tipoVeiculo;
						value.cell.placa = placa;
						value.cell.motorista = nome;
						value.cell.cnh = cnh;
						value.cell.rota = rota;
						value.cell.roteiro = roteiro;
					});
					
					return data;
				},
				colModel : [ {
					display : 'Veículo',
					name : 'veiculo',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Placa',
					name : 'placa',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Motorista',
					name : 'motorista',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNH',
					name : 'cnh',
					width : 95,
					sortable : true,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'rota',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Roteiro',
					name : 'roteiro',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
					sortable : false,
					align : 'center'
				} ],
				width : 820,
				height : 100,
				disableSelect: true,
				sortname : "veiculo",
				sortorder : "asc"
			});
			$(".associacaoGrid", transportadorController.workspace).flexOptions({url: contextPath + "/cadastro/transportador/carregarAssociacoes"});
		
			$(".veiculosGrid", transportadorController.workspace).flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {						
						
						var idVeiculo = value.id;
						
						value.cell.sel = '<input type="radio" name="radioVeiculo" value="'+ idVeiculo +'"/>';
						
						var acao = '<a href="javascript:;" onclick="transportadorController.editarVeiculo('+ idVeiculo +');"><img src="'+ contextPath +'/images/ico_editar.gif" border="0" hspace="2" style="margin-right:10px" />';
						acao +='</a> <a href="javascript:;" onclick="transportadorController.excluirVeiculo('+ idVeiculo +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
						value.cell.acao = acao;
					});
					
					return data;
				},
				colModel : [ {
					display : ' ',
					name : 'sel',
					width : 20,
					sortable : false,
					align : 'left'
				}, {
					display : 'Tipo',
					name : 'tipoVeiculo',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Placa',
					name : 'placa',
					width : 64,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 37,
					sortable : false,
					align : 'center'
				} ],
				width : 260,
				height : 150,
				disableSelect: true,
				sortname : "tipoVeiculo",
				sortorder : "asc"
			});
			$(".veiculosGrid", transportadorController.workspace).flexOptions({url: contextPath + "/cadastro/transportador/carregarVeiculos"});
		
			$(".motoristasGrid", transportadorController.workspace).flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {						
						
						var idMotorista = value.id;
						
						value.cell.sel = '<input type="radio" name="radioMotorista" value="'+ idMotorista +'"/>';
						
						var acao = '<a href="javascript:;" onclick="transportadorController.editarMotorista('+ idMotorista +');"><img src="'+ contextPath +'/images/ico_editar.gif" border="0" hspace="2" />';
						acao +='</a> <a href="javascript:;" onclick="transportadorController.excluirMotorista('+ idMotorista +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
						value.cell.acao = acao;
					});
					
					return data;
				},
				colModel : [ {
					display : ' ',
					name : 'sel',
					width : 20,
					sortable : false,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNH',
					name : 'cnh',
					width : 55,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 37,
					sortable : false,
					align : 'center'
				} ],
				width : 260,
				height : 150,
				disableSelect: true,
				sortname : "nome",
				sortorder : "asc"
			});
			$(".motoristasGrid", transportadorController.workspace).flexOptions({url: contextPath + "/cadastro/transportador/carregarMotoristas"});
		
			$(".boxRotaGrid", transportadorController.workspace).flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {						
						
						var disabled = 'disabled="disabled"';
						
						if (value.cell.disponivel){
							
							disabled = "";
						}
						
						value.cell.checks = '<input type="checkbox" '+ disabled +' name="checkRota" value="'+ value.cell.idRota +'"/>';
					});
					
					return data;
				},
				colModel : [ {
					display : ' ',
					name : 'checks',
					width : 20,
					sortable : false,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'descricaoRota',
					width : 91,
					sortable : true,
					align : 'left'
				}, {
					display : 'Roteiro',
					name : 'descricaoRoteiro',
					width : 91,
					sortable : true,
					align : 'left'
				} ],
				width : 260,
				height : 150,
				disableSelect: true,
				sortname : "descricaoRota",
				sortorder : "asc"
			});
			$(".boxRotaGrid", transportadorController.workspace).flexOptions({url: contextPath + "/cadastro/transportador/carregarRotas"});
			$("#gridCotasAtendidas", transportadorController.workspace).flexigrid({
				dataType : 'json',
				preProcess: function(data){
					
					$.each(data.rows, function(index, value) {
						
						if (!value.cell.valor){
							
							value.cell.valor = "";
						}
					});
					
					return data;
				},
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 222,
					sortable : true,
					align : 'left'
				}, {
					display : 'Box',
					name : 'box',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Roteiro',
					name : 'roteiro',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'rota',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor R$ / %',
					name : 'valor',
					width : 80,
					sortable : true,
					align : 'left'
				}],
				sortname : "numeroCota",
				sortorder : "asc",
				showTableToggleBtn : true,
				width : 830,
				height : 200,
				disableSelect: true
			});
			$("#gridCotasAtendidas", transportadorController.workspace).flexOptions({url: contextPath + "/cadastro/transportador/carregarCotasAtendidas"});
			
			$("#cnpj", transportadorController.workspace).mask("99.999.999/9999-99");
			
			$("#valorTaxaFixa", transportadorController.workspace).maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			$("#valorPercentualFaturamento", transportadorController.workspace).maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});			
			$("#inputQuinzenalDiaInicio", transportadorController.workspace).numeric();
			$("#inputCobrancaMensal", transportadorController.workspace).numeric();
			
			$.mask.definitions['#']='[\-\.0-9]';
			
			$("#inscEstadual", transportadorController.workspace).mask("?##################",{placeholder:" "});
			
	},
	
	popup_novo_transportador : function() {
		
		fecharModalCadastroTransp = false;
		
		$('#tabs', transportadorController.workspace).tabs('select', 0);
		
		$("#dialog-novo", transportadorController.workspace).dialog({
			resizable : false,
			height : 590,
			width : 960,
			modal : true,
			buttons : [
				{
					id: "btnConfirmarTransportador",
					text: "Confirmar",
					click: function() {
						
						var diaCobranca = '';
						
						if ($("[name=radioPeriodicidade]:checked", transportadorController.workspace).val() == "QUINZENAL"){
							
							diaCobranca = $("#inputQuinzenalDiaInicio", transportadorController.workspace).val();
						} else if ($("[name=radioPeriodicidade]:checked", transportadorController.workspace).val() == "MENSAL"){
							
							diaCobranca = $("#inputCobrancaMensal", transportadorController.workspace).val();
						}
						
						var valorCobranca = '';
						
						if ($("#modalidadeCobranca", transportadorController.workspace).val() == "TAXA_FIXA"){
							
							valorCobranca = transportadorController.preparaValor($("#valorTaxaFixa", transportadorController.workspace).val());
						} else {
							
							valorCobranca = transportadorController.preparaValor($("#valorPercentualFaturamento", transportadorController.workspace).val());
						}
						
						var data = [{name:"transportador.pessoaJuridica.razaoSocial", value:$("#razaoSocial", transportadorController.workspace).val()},
									{name:"transportador.pessoaJuridica.nomeFantasia", value:$("#nomeFantasia", transportadorController.workspace).val()},
									{name:"transportador.pessoaJuridica.email", value:$("#email", transportadorController.workspace).val()},
									{name:"transportador.pessoaJuridica.cnpj", value:$("#cnpj", transportadorController.workspace).val()},
									{name:"transportador.pessoaJuridica.inscricaoEstadual", value:$("#inscEstadual", transportadorController.workspace).val()},
									{name:"transportador.responsavel", value:$("#responsavel", transportadorController.workspace).val()},
									
									{name:"transportador.parametroCobrancaTransportador.periodicidadeCobranca", 
										value:$("[name=radioPeriodicidade]:checked", transportadorController.workspace).val()},
									{name:"transportador.parametroCobrancaTransportador.diaCobranca", value: diaCobranca},
									{name:"transportador.parametroCobrancaTransportador.modelidadeCobranca", 
										value: $("#modelidadeCobranca", transportadorController.workspace).val()},
									{name: "transportador.parametroCobrancaTransportador.valor", value: valorCobranca},
									{name: "transportador.parametroCobrancaTransportador.porEntrega", 
										value: $("#checkPorEntrega").is(':checked')}
						];
						
						$.each($("[name=diaSemanaCob]:checked", transportadorController.workspace), 
								function(index, value) {
							
							data.push({name: "transportador.parametroCobrancaTransportador.diasSemanaCobranca["+ index +"]", value: value.value});
						});
						
						$.postJSON(contextPath + "/cadastro/transportador/cadastrarTransportador", data, 
							function(result){
								
								fecharModalCadastroTransp = true;
								
								$("#dialog-cancelar-cadastro-transportador", transportadorController.workspace).dialog("close");
								$("#dialog-novo", transportadorController.workspace).dialog("close");
								
								exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
								$(".transportadoraGrid", transportadorController.workspace).flexReload();
							},null, true, "idModalCadastroTransportador"
						);
	
					}
				},
				{
					id: "btnCancTransportador",
					text: "Cancelar",
					click: function() {
						$(this).dialog("close");
					}
				}
			],
			beforeClose: function(event, ui) {
				
				if (!fecharModalCadastroTransp){
					
					transportadorController.cancelarCadastro();
					
					return fecharModalCadastroTransp;
				}
				
				return fecharModalCadastroTransp;
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});

	},
	
	limparCamposCadastroTransportador : function(){
		$("#razaoSocial", transportadorController.workspace).val("");
		$("#nomeFantasia", transportadorController.workspace).val("");
		$("#email", transportadorController.workspace).val("");
		$("#cnpj", transportadorController.workspace).val("");
		$("#inscEstadual", transportadorController.workspace).val("");
		$("#responsavel", transportadorController.workspace).val("");
		
		$("#checkPorEntrega", transportadorController.workspace).uncheck();
		$("#radioPeridioDiario", transportadorController.workspace).check();
		$(".checksDiasSemana", transportadorController.workspace).uncheck();
		$("#inputQuinzenalDiaInicio", transportadorController.workspace).val("");
		$("#inputQuinzenalDiaFim", transportadorController.workspace).val("");
		$("#inputCobrancaMensal", transportadorController.workspace).val("");
	},
	
	cancelarCadastro : function(){
		$("#dialog-cancelar-cadastro-transportador", transportadorController.workspace).dialog({
			resizable: false,
			height:150,
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					$.postJSON(contextPath + "/cadastro/transportador/cancelarCadastro", null, 
						function(result){
							
							fecharModalCadastroTransp = true;
							
							$("#dialog-cancelar-cadastro-transportador", transportadorController.workspace).dialog("close");
							$("#dialog-novo", transportadorController.workspace).dialog("close");
						}
					);
				},
				"Cancelar": function() {
					
					$(this).dialog("close");
					
					fecharModalCadastroTransp = false;
				}
			},
			form: $("#dialog-cancelar-cadastro-transportador", this.workspace).parents("form")
		});
	},

	popup_excluir :function() {

		$("#dialog-excluir", transportadorController.workspace).dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
	},

	popup_incluir_veiculo :function() {
		
		$("#btnAddVeiculo", transportadorController.workspace).show();
		
		$("#tipoVeiculo", transportadorController.workspace).val("");
		$("#placa", transportadorController.workspace).val("");
		
		$("#dialog-incluir-veiculo", transportadorController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 420,
			modal : true,
			buttons : [
				{
					id: "btnConfNovoVeiculo",
					text: "Confirmar",
					click: function() {
						/*
						$.postJSON("<c:url value='/cadastro/transportador/cadastrarVeiculos'/>", null, 
							function(result){
								
								$(".veiculosGrid").flexAddData({
									page: result.page, total: result.total, rows: result.rows
								});
								
								$("#dialog-incluir-veiculo").dialog("close");
							}
						);
						*/
						$("#dialog-incluir-veiculo", transportadorController.workspace).dialog("close");
					}
				},
				{
					id: "btnCancelarNovoVeiculo",
					text: "Cancelar",
					click: function() {
						/*
						$.postJSON("<c:url value='/cadastro/transportador/cancelarCadastroVeiculos'/>", null, 
							function(result){
								
								$(".veiculosGrid").flexAddData({
									page: result.page, total: result.total, rows: result.rows
								});
								
								$("#dialog-incluir-veiculo").dialog("close");
							}
						);*/
						$("#dialog-incluir-veiculo", transportadorController.workspace).dialog("close");
					}
				}
			],
			form: $("#dialog-incluir-veiculo", this.workspace).parents("form")
		});
	},
	
	adicionarVeiculo : function(){
		
		data = [{name:"veiculo.tipoVeiculo", value: $("#tipoVeiculo", transportadorController.workspace).val()},
				{name:"veiculo.placa", value: $("#placa", transportadorController.workspace).val()}];
		
		$.postJSON(contextPath + "/cadastro/transportador/adicionarVeiculo", data, 
			function(){
				
				$(".veiculosGrid", transportadorController.workspace).flexReload();
			
				$("#tipoVeiculo", transportadorController.workspace).val("");
				$("#placa", transportadorController.workspace).val("");
				
				$("#tipoVeiculo", transportadorController.workspace).focus();
			}, null, true, "idModalCadastroVeiculo"
		);
	},
	
	excluirVeiculo : function(idVeiculo){
		
		$("#dialog-excluir-veiculo", transportadorController.workspace).dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					data = [{name:"referencia", value: idVeiculo}];
					
					$.postJSON(contextPath + "/cadastro/transportador/excluirVeiculo", data, 
						function(result){
							
							exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalCadastroTransportador");
							
							$(".veiculosGrid", transportadorController.workspace).flexReload();
							
							$("#dialog-excluir-veiculo", transportadorController.workspace).dialog("close");
						}
					);
				},
				"Cancelar" : function() {
					
					$("#dialog-excluir-veiculo", transportadorController.workspace).dialog("close");
				}
			},
			form: $("#dialog-excluir-veiculo", this.workspace).parents("form")
		});
	},
	
	editarVeiculo : function(idVeiculo){
		
		$.postJSON(contextPath + "/cadastro/transportador/editarVeiculo", {referencia:idVeiculo}, 
			function(result){
				
				var idVeiculo = result.id;
				
				$("#tipoVeiculo", transportadorController.workspace).val(result.tipoVeiculo);
				$("#placa", transportadorController.workspace).val(result.placa);
				
				$("#btnAddVeiculo", transportadorController.workspace).hide();
			
				$("#dialog-incluir-veiculo", transportadorController.workspace).dialog({
					resizable : false,
					height : 'auto',
					width : 420,
					modal : true,
					buttons : [
						{
							id: "btnConfEditVeiculo",
							text: "Confirmar",
							click: function() {
								
								var data = [{name: "veiculo.id", value: idVeiculo},
								            {name: "veiculo.tipoVeiculo", value: $("#tipoVeiculo", transportadorController.workspace).val()},
								            {name: "veiculo.placa", value: $("#placa", transportadorController.workspace).val()}];
								
								$.postJSON(contextPath + "/cadastro/transportador/adicionarVeiculo", data, 
									function(){
										
										$(".veiculosGrid", transportadorController.workspace).flexReload();
										$(".associacaoGrid", transportadorController.workspace).flexReload();
										
										$("#dialog-incluir-veiculo", transportadorController.workspace).dialog("close");
									}, null, true, "idModalCadastroVeiculo"
								);
							}
						},
						{
							id: "btnCancEditVeiculo",
							text: "Cancelar",
							click: function() {
								$("#dialog-incluir-veiculo", transportadorController.workspace).dialog("close");
								$("#btnAddVeiculo", transportadorController.workspace).show();
							}
						}
					],
					form: $("#dialog-incluir-veiculo", this.workspace).parents("form")
				});
			}
		);
	},

	popup_incluir_motorista : function() {

		$("#btnAddMotorista", transportadorController.workspace).show();
		
		$("#nomeMotorista", transportadorController.workspace).val("");
		$("#cnhMotorista", transportadorController.workspace).val("");
		
		$("#dialog-incluir-motorista", transportadorController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 420,
			modal : true,
			buttons : [
				{
					id: "btnConfNovoMotorista",
					text: "Confirmar",
					click: function() {
						/*
						$.postJSON("<c:url value='/cadastro/transportador/cadastrarMotoristas'/>", null, 
							function(result){
								
								$(".motoristasGrid").flexAddData({
									page: result.page, total: result.total, rows: result.rows
								});
								
								
							}
						);
						*/
						$("#dialog-incluir-motorista", transportadorController.workspace).dialog("close");
					}
				},
				{
					id: "btnCancNovoMotorista",
					text: "Cancelar",
					click: function() {
						/*
						$.postJSON("<c:url value='/cadastro/transportador/cancelarCadastroMotoristas'/>", null, 
							function(result){
								
								$(".motoristasGrid").flexAddData({
									page: result.page, total: result.total, rows: result.rows
								});
								
								$("#dialog-incluir-motorista").dialog("close");
							}
						);*/
						$("#dialog-incluir-motorista", transportadorController.workspace).dialog("close");
					}
				}
			],
			form: $("#dialog-incluir-motorista", this.workspace).parents("form")
		});
	},
	
	adicionarMotorista : function(){
		
		data = [{name:"motorista.nome", value: $("#nomeMotorista", transportadorController.workspace).val()},
				{name:"motorista.cnh", value: $("#cnhMotorista", transportadorController.workspace).val()}];
		
		$.postJSON(contextPath + "/cadastro/transportador/adicionarMotorista", data, 
			function(){
				
				$(".motoristasGrid", transportadorController.workspace).flexReload();
			
				$("#nomeMotorista", transportadorController.workspace).val("");
				$("#cnhMotorista", transportadorController.workspace).val("");
				
				$("#nomeMotorista", transportadorController.workspace).focus();
			}, null, true, "idModalCadastroMotorista"
		);
	},
	
	excluirMotorista : function(idMotorista){
			
		$("#dialog-excluir-motorista", transportadorController.workspace).dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					data = [{name:"referencia", value: idMotorista}];
					
					$.postJSON(contextPath + "/cadastro/transportador/excluirMotorista", data, 
						function(result){
							
							exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalCadastroTransportador");
							
							$(".motoristasGrid", transportadorController.workspace).flexReload();
							
							$("#dialog-excluir-motorista", transportadorController.workspace).dialog("close");
						}
					);
				},
				"Cancelar" : function() {
					
					$("#dialog-excluir-motorista", transportadorController.workspace).dialog("close");
				}
			},
			form: $("#dialog-excluir-motorista", this.workspace).parents("form")
		});
	},
	
	editarMotorista : function(idMotorista){
		
		$.postJSON(contextPath + "/cadastro/transportador/editarMotorista", {referencia:idMotorista}, 
			function(result){
				
				var idMotorista = result.id;
				
				$("#nomeMotorista", transportadorController.workspace).val(result.nome);
				$("#cnhMotorista", transportadorController.workspace).val(result.cnh);
				
				$("#btnAddMotorista", transportadorController.workspace).hide();
			
				$("#dialog-incluir-motorista", transportadorController.workspace).dialog({
					resizable : false,
					height : 'auto',
					width : 420,
					modal : true,
					buttons : [
						{
							id: "btnConfEditMotorista",
							text: "Confirmar",
							click: function() {
								
								var data = [{name: "motorista.id", value: idMotorista},
								            {name: "motorista.nome", value: $("#nomeMotorista", transportadorController.workspace).val()},
								            {name: "motorista.cnh", value: $("#cnhMotorista", transportadorController.workspace).val()}];
								
								$.postJSON(contextPath + "/cadastro/transportador/adicionarMotorista", data, 
									function(result){
										
										$(".motoristasGrid", transportadorController.workspace).flexReload();
										$(".associacaoGrid", transportadorController.workspace).flexReload();
										
										$("#dialog-incluir-motorista", transportadorController.workspace).dialog("close");
									}, null, true, "idModalCadastroMotorista"
								);
							}
						},
						{
							id: "btnCancEditMotorista",
							text: "Cancelar",
							click: function() {
								$("#dialog-incluir-motorista", transportadorController.workspace).dialog("close");
								$("#btnAddMotorista", transportadorController.workspace).show();
							}
						}
				],
				form: $("#dialog-incluir-motorista", this.workspace).parents("form")
				});
			}
		);
	},
	
	pesquisarTransportadores : function(){
		
		$(".transportadoraGrid", transportadorController.workspace).flexOptions(
				{"url": contextPath + "/cadastro/transportador/pesquisarTransportadores", 
				 params:[
					{name:"filtro.razaoSocial", value:$("#razaoSocialPesquisa", transportadorController.workspace).val()},
					{name:"filtro.nomeFantasia", value:$("#nomeFantasiaPesquisa", transportadorController.workspace).val()},
					{name:"filtro.cnpj", value:$("#cnpjPesquisa", transportadorController.workspace).val()}]
				}
		);
		
		$(".transportadoraGrid", transportadorController.workspace).flexReload();
	},
	
	editarTransportadora : function(idTransp){
		$.postJSON(contextPath + "/cadastro/transportador/editarTransportador", {referencia:idTransp}, 
			function(result) {
				
				if (result){
					
					$("#razaoSocial", transportadorController.workspace).val(result[0]);
					$("#nomeFantasia", transportadorController.workspace).val(result[1]);
					$("#email", transportadorController.workspace).val(result[2]);
					$("#responsavel", transportadorController.workspace).val(result[3]);
					$("#cnpj", transportadorController.workspace).val(result[4]);
					$("#inscEstadual", transportadorController.workspace).val(result[5]);
					
					$("#modelidadeCobranca", transportadorController.workspace).val(result[6]);
					$("#valorTaxaFixa", transportadorController.workspace).val(transportadorController.preparaValor(result[7]));
					 
					if (result[8] == "true"){
						
						$("#checkPorEntrega", transportadorController.workspace).check();
					} else {
						
						$("#checkPorEntrega", transportadorController.workspace).uncheck();
					}
					
					transportadorController.alterarPeriodicidadeCobranca(result[9]);
					
					if (result[9] == "QUINZENAL"){
						
						$("#inputQuinzenalDiaInicio").val(result[10]);
						transportadorController.calcularDiaFimCobQuinzenal();
					} else if (result[9] == "MENSAL"){
						
						$("#inputCobrancaMensal").val(result[10]);
					} else if (result[9] == "SEMANAL"){
						
						for (var i = 11 ; i < 18 ; i++){
							
							if (result[i]){
								switch (result[i]){
									case "DOMINGO":
										$("#checkDomingo", transportadorController.workspace).check();
									break;
								
									case "SEGUNDA_FEIRA":
										$("#checkSegunda", transportadorController.workspace).check();
									break;
									
									case "TERCA_FEIRA":
										$("#checkTerca", transportadorController.workspace).check();
									break;
									
									case "QUARTA_FEIRA":
										$("#checkQuarta", transportadorController.workspace).check();
									break;
									
									case "QUINTA_FEIRA":
										$("#checkQuinta", transportadorController.workspace).check();
									break;
									
									case "SEXTA_FEIRA":
										$("#checkSexta", transportadorController.workspace).check();
									break;
									
									case "SABADO":
										$("#checkSabado", transportadorController.workspace).check();
									break;
								}
							}
						}
					}
				}
			
				transportadorController.popup_novo_transportador();
			}
		);
	},
	
	excluirTransportadora : function(idTransp){
		
		$("#dialog-excluir", transportadorController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$.postJSON(contextPath + "/cadastro/transportador/excluirTransportador", 
						[{name:"referencia", value: idTransp}], 
						function(result) {
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							
							$("#dialog-excluir", transportadorController.workspace).dialog("close");
							
							$(".transportadoraGrid", transportadorController.workspace).flexReload();
						},
						null,
						true
					);
				},
				"Cancelar" : function() {
					
					$("#dialog-excluir", transportadorController.workspace).dialog("close");
				}
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
		
		$("#dialog-excluir", transportadorController.workspace).show();
	},
	
	carregarGrids : function(){
		
		$.postJSON(contextPath + "/cadastro/transportador/carregarTelaAssociacao", null, 
			function(result) {
				
				if (result[0] != ""){
					
					$(".veiculosGrid", transportadorController.workspace).flexAddData({
						page: result[0].page, total: result[0].total, rows: result[0].rows
					});
				}
				
				if (result[1] != ""){
					
					$(".motoristasGrid", transportadorController.workspace).flexAddData({
						page: result[1].page, total: result[1].total, rows: result[1].rows
					});
				}
				
				if (result[2] != ""){
					
					$(".boxRotaGrid", transportadorController.workspace).flexAddData({
						page: result[2].page, total: result[2].total, rows: result[2].rows
					});
				}

				$(".boxRotaGrid", transportadorController.workspace).flexReload();
				
				if (result[3] != ""){
					
					$(".associacaoGrid", transportadorController.workspace).flexAddData({
						page: result[3].page, total: result[3].total, rows: result[3].rows
					});
				}
			}
		);
		
	},
	
	carregarCotasAtendidas : function(){
		
		$.postJSON(contextPath + "/cadastro/transportador/carregarCotasAtendidas", null, 
			function(result) {
				
				if (result[0] != ""){
					
					$("#gridCotasAtendidas", transportadorController.workspace).flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
				}
			}
		);
	},
	
	buscarPessoaCNPJ : function(cnpj){
		
		if (cnpj != "__.___.___/____-__" && cnpj != ""){
			
			$.postJSON(contextPath + "/cadastro/transportador/buscarPessoaCNPJ", {cnpj:cnpj}, 
				function(result) {
					
					if (result[0]){
						
						$("#razaoSocial", transportadorController.workspace).val(result[0]);
						$("#nomeFantasia", transportadorController.workspace).val(result[1]);
						$("#email", transportadorController.workspace).val(result[2]);
						$("#inscEstadual", transportadorController.workspace).val(result[3]);
					}
				},
				null,
				true
			);
		}
	},
	
	confirmarAssociacao : function(){
		
		var idVeiculo = $("input:radio[name='radioVeiculo']:checked", transportadorController.workspace).val();
		var idMotorista = $("input:radio[name='radioMotorista']:checked", transportadorController.workspace).val();
		var idsRotas = $("input:checkbox[name='checkRota']:checked", transportadorController.workspace).val();
		
		var msgs = [];
		
		if (!idVeiculo){
			
			msgs.push("Escolha um veículo.");
		}
		
		if (!idMotorista){
			
			msgs.push("Escolha um motorista.");
		}
		
		if (!idsRotas){
			
			msgs.push("Escolha pelo menos uma rota.");
		}
		
		if (msgs.length > 0){
			
			exibirMensagemDialog("WARNING", msgs, "idModalCadastroTransportador");
			return;
		}
		
		var tipoVeiculo = $("input:radio[name='radioVeiculo']:checked", transportadorController.workspace).parent().parent().parent().children()[1].children[0].innerHTML;
		var placaVeiculo = $("input:radio[name='radioVeiculo']:checked", transportadorController.workspace).parent().parent().parent().children()[2].children[0].innerHTML;
		
		var nomeMotorista = $("input:radio[name='radioMotorista']:checked", transportadorController.workspace).parent().parent().parent().children()[1].children[0].innerHTML;
		var cnhMotorista = $("input:radio[name='radioMotorista']:checked", transportadorController.workspace).parent().parent().parent().children()[2].children[0].innerHTML;
		
		var data = [];
		
		data.push({name: "veiculo.id", value: idVeiculo});
		data.push({name: "veiculo.tipoVeiculo", value: tipoVeiculo});
		data.push({name: "veiculo.placa", value: placaVeiculo});
		data.push({name: "motorista.id", value: idMotorista});
		data.push({name: "motorista.nome", value: nomeMotorista});
		data.push({name: "motorista.cnh", value: cnhMotorista});
		
		$.each($("input:checkbox[name='checkRota']:checked", transportadorController.workspace), function(index, value){
			
			var id = $(value).val();
			var rota = $(value).parent().parent().parent().children()[1].children[0].innerHTML;
			var roteiro = $(value).parent().parent().parent().children()[2].children[0].innerHTML;
			
			data.push({name:'rotas['+ index +'].idRota', value: id});
			data.push({name:'rotas['+ index +'].descricaoRota', value: rota});
			data.push({name:'rotas['+ index +'].descricaoRoteiro', value: roteiro});
		});
		
		$.postJSON(contextPath + "/cadastro/transportador/cadastrarAssociacao", data, 
			function(result) {
				
				$(".associacaoGrid", transportadorController.workspace).flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});
				
				$(".boxRotaGrid", transportadorController.workspace).flexReload();
			},
			null,
			true
		);
	},
	
	excluirAssociacao : function(referencia){
		
		$("#dialog-excluir-associacao", transportadorController.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$.postJSON(contextPath + "/cadastro/transportador/excluirAssociacao", 
						[{name:"referencia", value: referencia}], 
						function(result) {
							
							$(".associacaoGrid", transportadorController.workspace).flexReload();
							
							$(".boxRotaGrid", transportadorController.workspace).flexReload();
							
							$("#dialog-excluir-associacao", transportadorController.workspace).dialog("close");
						},
						null,
						true
					);
				},
				"Cancelar" : function() {
					
					$("#dialog-excluir-associacao", transportadorController.workspace).dialog("close");
				}
			},
			form: $("#dialog-excluir-associacao", this.workspace).parents("form")
		});
	},
	
	mostrarOpcaoTaxaFixa : function(){
		
		$(".transpTaxaFixa", this.workspace).show();
		$(".transpPercentual", this.workspace).hide();
	}, 
	
	mostrarOpcaoPercentual : function(){
		
		$(".transpTaxaFixa", this.workspace).hide();
		$(".transpPercentual", this.workspace).show();
	},
	
	exportarArquivo : function(fileType){
		
		var sortorder = $("#gridCotasAtendidas", transportadorController.workspace).getSortOrder();
		var sortname = $("#gridCotasAtendidas", transportadorController.workspace).flexGetSortName();
		
		window.location = 
			contextPath + "/cadastro/transportador/exportarCotasAtendidas?fileType=" + fileType + "&sortorder=" + sortorder + "&sortname=" + sortname;
	},
	
	alterarPeriodicidadeCobranca : function(tipoCobranca){
		
		$(".perCobrancaSemanal", transportadorController.workspace).hide();
		$(".perCobrancaQuinzenal", transportadorController.workspace).hide();
		$(".perCobrancaMensal", transportadorController.workspace).hide();
		
		if (tipoCobranca == 'SEMANAL'){
			
			$(".perCobrancaSemanal", transportadorController.workspace).show();
		} else if (tipoCobranca == 'QUINZENAL'){
			
			$(".perCobrancaQuinzenal", transportadorController.workspace).show();
		} else if (tipoCobranca == 'MENSAL'){
			
			$(".perCobrancaMensal", transportadorController.workspace).show();
		}
		
		$("input:radio[value="+ tipoCobranca +"]", transportadorController.workspace).check();
	},
	
	calcularDiaFimCobQuinzenal : function(){
		
		var valorInput = parseInt($("#inputQuinzenalDiaInicio").val());
		
		if (!valorInput || valorInput <= 0){
			
			$("#inputQuinzenalDiaFim", transportadorController.workspace).val("");
		} else {
			
			var diaFim = valorInput + 14 < 31 ? valorInput + 14 : 31;
			
			$("#inputQuinzenalDiaFim", transportadorController.workspace).val(diaFim);
		}
	},
	
	preparaValor : function(vr){
		
		if(vr.substr(vr.length-3,1)==","){
			vr = this.replaceAll(vr,".","");
			vr = this.replaceAll(vr,",",".");
		}
		if(vr.substr(vr.length-3,1)=="."){
			vr = this.replaceAll(vr,",","");
		}
		return vr;
	},
	
	replaceAll : function(string, token, newtoken) {
		while (string.indexOf(token) != -1) {
	 		string = string.replace(token, newtoken);
		}
		return string;
	}

}, BaseController);


//@ sourceURL=transportador.js
