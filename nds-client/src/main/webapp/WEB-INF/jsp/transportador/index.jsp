<head>
	<script language="javascript" type="text/javascript">
		
		var fecharModalCadastroTransp = false;
	
		function popup_novo_transportador() {
			
			fecharModalCadastroTransp = false;
			
			$('#tabs').tabs('select', 0);
			
			$("#dialog-novo").dialog({
				resizable : false,
				height : 590,
				width : 900,
				modal : true,
				buttons : [
					{
						id: "btnConfirmarTransportador",
						text: "Confirmar",
						click: function() {
							
							var data = [{name:"transportador.pessoaJuridica.razaoSocial", value:$("#razaoSocial").val()},
										{name:"transportador.pessoaJuridica.nomeFantasia", value:$("#nomeFantasia").val()},
										{name:"transportador.pessoaJuridica.email", value:$("#email").val()},
										{name:"transportador.pessoaJuridica.cnpj", value:$("#cnpj").val()},
										{name:"transportador.pessoaJuridica.inscricaoEstadual", value:$("#inscEstadual").val()},
										{name:"transportador.responsavel", value:$("#responsavel").val()}];
							
							$.postJSON("<c:url value='/cadastro/transportador/cadastrarTransportador'/>", data, 
								function(result){
									
									fecharModalCadastroTransp = true;
									
									$("#dialog-cancelar-cadastro-transportador").dialog("close");
									$("#dialog-novo").dialog("close");
									
									exibirMensagem(result.tipoMensagem, result.listaMensagens);
									
									$(".transportadoraGrid").flexReload();
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
						
						cancelarCadastro();
						
						return fecharModalCadastroTransp;
					}
					
					return fecharModalCadastroTransp;
				}
			});
	
		}
		
		function limparCamposCadastroTransportador(){
			$("#razaoSocial").val("");
			$("#nomeFantasia").val("");
			$("#email").val("");
			$("#cnpj").val("");
			$("#inscEstadual").val("");
			$("#responsavel").val("");
		}
		
		function cancelarCadastro(){
			$("#dialog-cancelar-cadastro-transportador").dialog({
				resizable: false,
				height:150,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON("<c:url value='/cadastro/transportador/cancelarCadastro'/>", null, 
							function(result){
								
								fecharModalCadastroTransp = true;
								
								$("#dialog-cancelar-cadastro-transportador").dialog("close");
								$("#dialog-novo").dialog("close");
							}
						);
					},
					"Cancelar": function() {
						
						$(this).dialog("close");
						
						fecharModalCadastroTransp = false;
					}
				}
			});
		}
	
		function popup_excluir() {
	
			$("#dialog-excluir").dialog({
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
				}
			});
		}
	
		function popup_incluir_veiculo() {
			
			$("#btnAddVeiculo").show();
			
			$("#tipoVeiculo").val("");
			$("#placa").val("");
			
			$("#dialog-incluir-veiculo").dialog({
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
							$("#dialog-incluir-veiculo").dialog("close");
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
							$("#dialog-incluir-veiculo").dialog("close");
						}
					}
				]
			});
		}
		
		function adicionarVeiculo(){
			
			data = [{name:"veiculo.tipoVeiculo", value: $("#tipoVeiculo").val()},
					{name:"veiculo.placa", value: $("#placa").val()}];
			
			$.postJSON("<c:url value='/cadastro/transportador/adicionarVeiculo'/>", data, 
				function(){
					
					$(".veiculosGrid").flexReload();
				
					$("#tipoVeiculo").val("");
					$("#placa").val("");
					
					$("#tipoVeiculo").focus();
				}, null, true, "idModalCadastroVeiculo"
			);
		}
		
		function excluirVeiculo(idVeiculo){
			
			$("#dialog-excluir-veiculo").dialog({
				resizable : false,
				height : 170,
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						data = [{name:"referencia", value: idVeiculo}];
						
						$.postJSON("<c:url value='/cadastro/transportador/excluirVeiculo'/>", data, 
							function(result){
								
								exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalCadastroTransportador");
								
								$(".veiculosGrid").flexReload();
								
								$("#dialog-excluir-veiculo").dialog("close");
							}
						);
					},
					"Cancelar" : function() {
						
						$("#dialog-excluir-veiculo").dialog("close");
					}
				}
			});
		}
		
		function editarVeiculo(idVeiculo){
			
			$.postJSON("<c:url value='/cadastro/transportador/editarVeiculo'/>", "referencia=" + idVeiculo, 
				function(result){
					
					var idVeiculo = result.id;
					
					$("#tipoVeiculo").val(result.tipoVeiculo);
					$("#placa").val(result.placa);
					
					$("#btnAddVeiculo").hide();
				
					$("#dialog-incluir-veiculo").dialog({
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
									            {name: "veiculo.tipoVeiculo", value: $("#tipoVeiculo").val()},
									            {name: "veiculo.placa", value: $("#placa").val()}];
									
									$.postJSON("<c:url value='/cadastro/transportador/adicionarVeiculo'/>", data, 
										function(){
											
											$(".veiculosGrid").flexReload();
											$(".associacaoGrid").flexReload();
											
											$("#dialog-incluir-veiculo").dialog("close");
										}, null, true, "idModalCadastroVeiculo"
									);
								}
							},
							{
								id: "btnCancEditVeiculo",
								text: "Cancelar",
								click: function() {
									$("#dialog-incluir-veiculo").dialog("close");
									$("#btnAddVeiculo").show();
								}
							}
						]
					});
				}
			);
		}
	
		function popup_incluir_motorista() {
	
			$("#btnAddMotorista").show();
			
			$("#nomeMotorista").val("");
			$("#cnhMotorista").val("");
			
			$("#dialog-incluir-motorista").dialog({
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
							$("#dialog-incluir-motorista").dialog("close");
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
							$("#dialog-incluir-motorista").dialog("close");
						}
					}
				]
			});
		}
		
		function adicionarMotorista(){
			
			data = [{name:"motorista.nome", value: $("#nomeMotorista").val()},
					{name:"motorista.cnh", value: $("#cnhMotorista").val()}];
			
			$.postJSON("<c:url value='/cadastro/transportador/adicionarMotorista'/>", data, 
				function(){
					
					$(".motoristasGrid").flexReload();
				
					$("#nomeMotorista").val("");
					$("#cnhMotorista").val("");
					
					$("#nomeMotorista").focus();
				}, null, true, "idModalCadastroMotorista"
			);
		}
		
		function excluirMotorista(idMotorista){
				
			$("#dialog-excluir-motorista").dialog({
				resizable : false,
				height : 170,
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						data = [{name:"referencia", value: idMotorista}];
						
						$.postJSON("<c:url value='/cadastro/transportador/excluirMotorista'/>", data, 
							function(result){
								
								exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalCadastroTransportador");
								
								$(".motoristasGrid").flexReload();
								
								$("#dialog-excluir-motorista").dialog("close");
							}
						);
					},
					"Cancelar" : function() {
						
						$("#dialog-excluir-motorista").dialog("close");
					}
				}
			});
		}
		
		function editarMotorista(idMotorista){
			
			$.postJSON("<c:url value='/cadastro/transportador/editarMotorista'/>", "referencia=" + idMotorista, 
				function(result){
					
					var idMotorista = result.id;
					
					$("#nomeMotorista").val(result.nome);
					$("#cnhMotorista").val(result.cnh);
					
					$("#btnAddMotorista").hide();
				
					$("#dialog-incluir-motorista").dialog({
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
									            {name: "motorista.nome", value: $("#nomeMotorista").val()},
									            {name: "motorista.cnh", value: $("#cnhMotorista").val()}];
									
									$.postJSON("<c:url value='/cadastro/transportador/adicionarMotorista'/>", data, 
										function(result){
											
											$(".motoristasGrid").flexReload();
											$(".associacaoGrid").flexReload();
											
											$("#dialog-incluir-motorista").dialog("close");
										}, null, true, "idModalCadastroMotorista"
									);
								}
							},
							{
								id: "btnCancEditMotorista",
								text: "Cancelar",
								click: function() {
									$("#dialog-incluir-motorista").dialog("close");
									$("#btnAddMotorista").show();
								}
							}
					]
					});
				}
			);
		}
	
		$(function() {
			$("#tabs").tabs();
			
			$(".transportadoraGrid").flexigrid({
				dataType : 'json',
				preProcess: function(data){
					if(typeof data.mensagens == "object") {
						
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						
						$(".divTransportadoraGrid").hide();
						
					}else{
						
						$.each(data.rows, function(index, value) {						
							
							var idTransportadora = value.cell.id;								
							var acao = '<a href="javascript:;" onclick="editarTransportadora('+idTransportadora+');"><img src="'+contextPath+'/images/ico_editar.gif" border="0" hspace="5" />';
							acao +='</a> <a href="javascript:;" onclick="excluirTransportadora('+idTransportadora+');""><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" /></a>';
							
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
						
						$(".divTransportadoraGrid").show();
						
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
				height : 255,
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
						
						var acao = '<a href="javascript:;" onclick="excluirAssociacao('+ idAssoc +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
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
			$(".associacaoGrid").flexOptions({url: "<c:url value='/cadastro/transportador/carregarAssociacoes'/>"});
		
			$(".veiculosGrid").flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {						
						
						var idVeiculo = value.id;
						
						value.cell.sel = '<input type="radio" name="radioVeiculo" value="'+ idVeiculo +'"/>';
						
						var acao = '<a href="javascript:;" onclick="editarVeiculo('+ idVeiculo +');"><img src="'+ contextPath +'/images/ico_editar.gif" border="0" hspace="2" />';
						acao +='</a> <a href="javascript:;" onclick="excluirVeiculo('+ idVeiculo +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
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
			$(".veiculosGrid").flexOptions({url: "<c:url value='/cadastro/transportador/carregarVeiculos'/>"});
		
			$(".motoristasGrid").flexigrid({
				dataType : 'json',
				preProcess: function (data){
					
					if (data.result){
						
						data = data.result;
					}
					
					$.each(data.rows, function(index, value) {						
						
						var idMotorista = value.id;
						
						value.cell.sel = '<input type="radio" name="radioMotorista" value="'+ idMotorista +'"/>';
						
						var acao = '<a href="javascript:;" onclick="editarMotorista('+ idMotorista +');"><img src="'+ contextPath +'/images/ico_editar.gif" border="0" hspace="2" />';
						acao +='</a> <a href="javascript:;" onclick="excluirMotorista('+ idMotorista +');""><img src="'+ contextPath +'/images/ico_excluir.gif" hspace="2" border="0" /></a>';
						
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
			$(".motoristasGrid").flexOptions({url: "<c:url value='/cadastro/transportador/carregarMotoristas'/>"});
		
			$(".boxRotaGrid").flexigrid({
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
			$(".boxRotaGrid").flexOptions({url: "<c:url value='/cadastro/transportador/carregarRotas'/>"});
			
			$("#cnpj").mask("99.999.999/9999-99");
		});
		
		function pesquisarTransportadores(){
			
			$(".transportadoraGrid").flexOptions(
					{"url": contextPath + "/cadastro/transportador/pesquisarTransportadores", 
					 params:[
						{name:"filtro.razaoSocial", value:$("#razaoSocialPesquisa").val()},
						{name:"filtro.nomeFantasia", value:$("#nomeFantasiaPesquisa").val()},
						{name:"filtro.cnpj", value:$("#cnpjPesquisa").val()}]
					}
			);
			
			$(".transportadoraGrid").flexReload();
		}
		
		function editarTransportadora(idTransp){
			$.postJSON("<c:url value='/cadastro/transportador/editarTransportador' />", "referencia=" + idTransp, 
				function(result) {
					
					if (result){
						
						$("#razaoSocial").val(result[0]);
						$("#nomeFantasia").val(result[1]);
						$("#email").val(result[2]);
						$("#responsavel").val(result[3]);
						$("#cnpj").val(result[4]);
						$("#inscEstadual").val(result[5]);
					}
				
					popup_novo_transportador();
				}
			);
		}
		
		function excluirTransportadora(idTransp){
			
			$("#dialog-excluir").dialog({
				resizable : false,
				height : 'auto',
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$.postJSON("<c:url value='/cadastro/transportador/excluirTransportador' />", 
							[{name:"referencia", value: idTransp}], 
							function(result) {
								
								exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
								$("#dialog-excluir").dialog("close");
								
								$(".transportadoraGrid").flexReload();
							},
							null,
							true
						);
					},
					"Cancelar" : function() {
						
						$("#dialog-excluir").dialog("close");
					}
				}
			});
			
			$("#dialog-excluir").show();
		}
		
		function carregarGrids(){
			
			$.postJSON("<c:url value='/cadastro/transportador/carregarTelaAssociacao' />", null, 
				function(result) {
					
					if (result[0] != ""){
						
						$(".veiculosGrid").flexAddData({
							page: result[0].page, total: result[0].total, rows: result[0].rows
						});
					}
					
					if (result[1] != ""){
						
						$(".motoristasGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
					}
					
					if (result[2] != ""){
						
						$(".boxRotaGrid").flexAddData({
							page: result[2].page, total: result[2].total, rows: result[2].rows
						});
					}
					
					if (result[3] != ""){
						
						$(".associacaoGrid").flexAddData({
							page: result[3].page, total: result[3].total, rows: result[3].rows
						});
					}
				}
			);
		}
		
		function buscarPessoaCNPJ(cnpj){
			
			if (cnpj != "__.___.___/____-__" && cnpj != ""){
				
				$.postJSON("<c:url value='/cadastro/transportador/buscarPessoaCNPJ' />", "cnpj=" + cnpj, 
					function(result) {
						
						if (result[0]){
							
							$("#razaoSocial").val(result[0]);
							$("#nomeFantasia").val(result[1]);
							$("#email").val(result[2]);
							$("#inscEstadual").val(result[3]);
						}
					},
					null,
					true
				);
			}
		}
		
		function confirmarAssociacao(){
			
			var idVeiculo = $("input:radio[name='radioVeiculo']:checked").val();
			var idMotorista = $("input:radio[name='radioMotorista']:checked").val();
			var idsRotas = $("input:checkbox[name='checkRota']:checked").val();
			
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
			
			var tipoVeiculo = $("input:radio[name='radioVeiculo']:checked").parent().parent().parent().children()[1].children[0].innerHTML;
			var placaVeiculo = $("input:radio[name='radioVeiculo']:checked").parent().parent().parent().children()[2].children[0].innerHTML;
			
			var nomeMotorista = $("input:radio[name='radioMotorista']:checked").parent().parent().parent().children()[1].children[0].innerHTML;
			var cnhMotorista = $("input:radio[name='radioMotorista']:checked").parent().parent().parent().children()[2].children[0].innerHTML;
			
			var data = [];
			
			data.push({name: "veiculo.id", value: idVeiculo});
			data.push({name: "veiculo.tipoVeiculo", value: tipoVeiculo});
			data.push({name: "veiculo.placa", value: placaVeiculo});
			data.push({name: "motorista.id", value: idMotorista});
			data.push({name: "motorista.nome", value: nomeMotorista});
			data.push({name: "motorista.cnh", value: cnhMotorista});
			
			$.each($("input:checkbox[name='checkRota']:checked"), function(index, value){
				
				var id = $(value).val();
				var rota = $(value).parent().parent().parent().children()[1].children[0].innerHTML;
				var roteiro = $(value).parent().parent().parent().children()[2].children[0].innerHTML;
				
				data.push({name:'rotas['+ index +'].idRota', value: id});
				data.push({name:'rotas['+ index +'].descricaoRota', value: rota});
				data.push({name:'rotas['+ index +'].descricaoRoteiro', value: roteiro});
			});
			
			$.postJSON("<c:url value='/cadastro/transportador/cadastrarAssociacao' />", data, 
				function(result) {
					
					$(".associacaoGrid").flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
					
					$(".boxRotaGrid").flexReload();
				},
				null,
				true
			);
		}
		
		function excluirAssociacao(referencia){
			
			$("#dialog-excluir-associacao").dialog({
				resizable : false,
				height : 'auto',
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$.postJSON("<c:url value='/cadastro/transportador/excluirAssociacao' />", 
							[{name:"referencia", value: referencia}], 
							function(result) {
								
								$(".associacaoGrid").flexReload();
								
								$(".boxRotaGrid").flexReload();
								
								$("#dialog-excluir-associacao").dialog("close");
							},
							null,
							true
						);
					},
					"Cancelar" : function() {
						
						$("#dialog-excluir-associacao").dialog("close");
					}
				}
			});
		}
	</script>
	<style>
		.diasFunc label,.finceiro label {
			vertical-align: super;
		}
		
		#tabs-4 .especialidades fieldset {
			width: 220px !important;
			margin-left: -16px;
			width: 258px !important;
		}
		
		#tabs-4 .bt_novos,#tabs-4 .bt_confirmar_novo {
			margin-left: -14px !important;
		}
		
		#dialog-incluir-veiculo fieldset,#dialog-incluir-motorista fieldset {
			width: 370px !important;
		}
		
		#dialog-incluir-veiculo,#dialog-incluir-motorista,#dialog-excluir-associacao{
			display: none;
		}
		
		.associacao {
			width: 818px !important;
			margin-left: -11px !important;
		}
	</style>
</head>

<body>

	<div id="dialog-excluir-associacao" title="Veículos / Motoristas / Rota / Roteiro">
		<p>Confirma a exclusão desta associação de Veículos / Motoristas / Rota / Roteiro?</p>
	</div>

	<div id="dialog-excluir" title="Excluir Transportador">
		<p>Confirma a exclusão deste Transportador</p>
	</div>
	
	<div id="dialog-excluir-veiculo" title="Excluir Veículo" style="display: none;">
		<p>Confirma a exclusão deste Veículo</p>
	</div>
	
	<div id="dialog-excluir-motorista" title="Excluir Motorista" style="display: none;">
		<p>Confirma a exclusão deste Motorista</p>
	</div>
	
	<div id="dialog-cancelar-cadastro-transportador" title="Transportadores" style="display: none;">
		<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>

	<div id="dialog-incluir-motorista" title="Motoristas">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroMotorista" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Motorista</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="54">Nome:</td>
					<td width="280"><input type="text" style="width: 230px" id="nomeMotorista" maxlength="255" /></td>
				</tr>
				<tr>
					<td>CNH:</td>
					<td><input type="text" style="width: 110px" id="cnhMotorista" maxlength="255" /></td>
				</tr>
				<tr id="btnAddMotorista">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:adicionarMotorista();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="dialog-incluir-veiculo" title="Veículos">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroVeiculo" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Veículos</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="95">Tipo de Veículo:</td>
					<td width="239"><input type="text" style="width: 230px" id="tipoVeiculo" maxlength="255" /></td>
				</tr>
				<tr>
					<td>Placa:</td>
					<td><input type="text" style="width: 110px" id="placa" maxlength="255" /></td>
				</tr>
				<tr id="btnAddVeiculo">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:adicionarVeiculo();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>


	<div id="dialog-novo" title="Novo Transportador">
	
		<jsp:include page="../messagesDialog.jsp">
		
			<jsp:param value="idModalCadastroTransportador" name="messageDialog"/>

		</jsp:include>
		
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Dados Cadastrais</a></li>
				<li><a href="#tabs-2" onclick="ENDERECO_TRANSPORTADOR.popularGridEnderecos();">Endereços</a></li>
				<li><a href="#tabs-3" onclick="TRANSPORTADOR.carregarTelefones();">Telefones</a></li>
				<li><a href="#tabs-4" onclick="carregarGrids();">Veículos / Motoristas</a></li>
			</ul>
			<div id="tabs-1">
				<table width="730" cellpadding="2" cellspacing="2" style="text-align: left;">
					<tr>
						<td width="98">Razão Social:</td>
						<td width="248"><input type="text" style="width: 230px" id="razaoSocial" maxlength="255" /></td>
						<td width="113">Nome Fantasia:</td>
						<td width="243"><input type="text" style="width: 230px" id="nomeFantasia" maxlength="255" /></td>
					</tr>
					<tr>
						<td>E-mail:</td>
						<td><input type="text" style="width: 230px" id="email" maxlength="255" /></td>
						<td>Responsável:</td>
						<td><input type="text" style="width: 230px" id="responsavel" maxlength="255" /></td>
					</tr>
					<tr>
						<td>CNPJ:</td>
						<td><input type="text" style="width: 150px" id="cnpj" onblur="buscarPessoaCNPJ(this.value);" /></td>
						<td>Insc. Estadual:</td>
						<td><input type="text" style="width: 150px" id="inscEstadual" maxlength="255" /></td>
					</tr>
				</table>
			</div>
			<div id="tabs-2">
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_TRANSPORTADOR" name="telaEndereco"/>
				</jsp:include>
			</div>
			<div id="tabs-3">
				<jsp:include page="../telefone/index.jsp">
					<jsp:param value="TRANSPORTADOR" name="tela"/>
				</jsp:include>
			</div>

			<div id="tabs-4">
				<table border="0" cellpadding="2" cellspacing="2">
					<tr class="especialidades">
						<td valign="top">
							<fieldset>
								<legend>Veículos</legend>
								<table class="veiculosGrid"></table>
							</fieldset>
						</td>
						<td valign="top">
							<fieldset>
								<legend>Motoristas</legend>
								<table class="motoristasGrid"></table>
							</fieldset>
						</td>
						<td valign="top">
							<fieldset>
								<legend>Rota / Roteiro</legend>
								<table class="boxRotaGrid"></table>
							</fieldset>
						</td>
					</tr>
					<tr class="especialidades">
						<td valign="top">
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="popup_incluir_veiculo();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Veículo
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="popup_incluir_motorista();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Motorista
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_confirmar_novo"	title="Confirmar">
								<a href="javascript:;" onclick="confirmarAssociacao();">
									<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar Associação
								</a>
							</span>
						</td>
					</tr>
				</table>
				<br clear="all" />

				<fieldset class="associacao">
					<legend>Veículos / Motoristas / Rota / Roteiro</legend>
					<table class="associacaoGrid"></table>
				</fieldset>
				<br clear="all" />
			</div>
		</div>
	</div>
	
	<div class="corpo">
		<div class="container">

			<fieldset class="classFieldset">
				<legend> Pesquisar Transportador </legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
					<tr>
						<td width="85">Razão Social:</td>
						<td colspan="3"><input type="text" id="razaoSocialPesquisa" style="width: 190px;" maxlength="255" /></td>
						<td width="104">Nome Fantasia:</td>
						<td width="192"><input type="text" id="nomeFantasiaPesquisa" style="width: 190px;" maxlength="255" /></td>
						<td width="42">CNPJ:</td>
						<td width="179"><input type="text" id="cnpjPesquisa" style="width: 110px;" maxlength="255" /></td>
						<td width="109"><span class="bt_pesquisar">
							<a href="javascript:;" onclick="pesquisarTransportadores();">Pesquisar</a></span>
						</td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset class="classFieldset">
				<legend>Transportadores Cadastrados</legend>
				<div class="divTransportadoraGrid" style="display: none;">
					<table class="transportadoraGrid"></table>
				</div>

				<span class="bt_novos" title="Novo">
					<a href="javascript:;" onclick="limparCamposCadastroTransportador();popup_novo_transportador();">
						<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"	hspace="5" border="0" />Novo
					</a>
				</span>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
</body>