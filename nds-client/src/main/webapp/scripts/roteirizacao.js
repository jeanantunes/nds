var roteiroSelecionadoAutoComplete = false;	
var transferirRotasComNovoRoteiro = false;
var transferirRoteirizacaoComNovaRota= false;
var pesquisaPorCota = false;

var roteirizacao = {

		abrirTelaRoteiro : function () {
			
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRoteiro',null,
					function(result) {
							roteirizacao.limparTelaRoteiro();
				            $('#ordemInclusaoRoteiro').numeric();
				            $("#ordemInclusaoRoteiro").val(result.int);
		   					$("#dialog-roteiro" ).dialog({
		   						resizable: false,
		   						height:240,
		   						width:420,
		   						modal: true,
		   						buttons: {
		   							"Confirmar": function() {
		   								roteirizacao.confirmarInclusaoRoteiro();
		   							},
		   							"Cancelar": function() {
		   								$( this ).dialog( "close" );
		   							}
		   						}
		   					});
					   },
					   null,
					   true
				);

		},
		
		
	   confirmarInclusaoRoteiro : function() {
		   var tipoRoteiro = 'NORMAL';
		   if ( $('input[name=tipoRoteiro]').is(':checked') ){
			   tipoRoteiro = 'ESPECIAL';
		   } 
		   
			$.postJSON(contextPath + '/cadastro/roteirizacao/incluirRoteiro',
					 {
						'idBox' :  $("#boxInclusaoRoteiro").val(),
						'ordem' :  $("#ordemInclusaoRoteiro").val(),
						'nome' :  $("#nomeInclusaoRoteiro").val(),
						'tipoRoteiro' : tipoRoteiro
						
					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-roteiro').dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							
					   },
					   null,
					   true
			);
		},
		
		//Busca dados para o auto complete do nome da cota
		autoCompletarRoteiroPorNome : function(idRoteiro, callBack) {
			
			var descricao = $(idRoteiro).val();
			
			descricao = $.trim(descricao);
			
			$(idRoteiro).autocomplete({source: ""});
			
			roteiroSelecionadoAutoComplete = false;	
			if (descricao && descricao.length > 1) {
			
				
				$.postJSON(
					contextPath + "/cadastro/roteirizacao/autoCompletarRoteiroPorDescricao",
					 {
						'descricao' : descricao
						
					 },
					function(result) { 
						 roteirizacao.exibirAutoComplete(result, idRoteiro ,callBack); 
					},
					null, 
					true
				);
			}
		},
		//Exibe o auto complete no campo
		exibirAutoComplete : function(result, idCampo ,callBack) {
			$(idCampo).autocomplete({
				source: result,
				focus : function(event, ui) {
					roteiroSelecionadoAutoComplete = true;	
				},
				close : function(event, ui) {
					roteiroSelecionadoAutoComplete = true;	
				},
				select : function(event, ui) {
					roteiroSelecionadoAutoComplete = true;	
					if (callBack){ 
					  callBack(ui.item.chave,true);
					} 
				//	roteirizacao.populaDadosRoteiro(ui.item.chave);
				
				},
				minLength: 2,
				delay : 0,
			});
		},
		//Busca dados para o auto complete do nome da cota
		buscaRoteiroPorNome : function(campo) {
			var descricao = $(campo).val();
			descricao = $.trim(descricao);
			if ( !roteiroSelecionadoAutoComplete  && descricao != "") {
			
					$.postJSON(
						contextPath + "/cadastro/roteirizacao/buscaRoteiroPorDescricao",
						 {
							'descricao' : descricao
							
						 },
						function(result) { 
							 
							    var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								$('#dialog-rota').dialog( "close" );
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
									roteirizacao.reiniciaTelaRoteirizacao();
								} else {							 
									roteirizacao.populaDadosRoteiro(result[0], false);
								}	
						},
						null, 
						true
					);
			}	
			roteiroSelecionadoAutoComplete = false;
		},
		
		//Busca dados para o auto complete do nome da cota
		populaDadosRoteiro : function(roteiro, autoComplete) {
			roteiroSelecionadoAutoComplete = autoComplete;
			if ( roteiro.tipoRoteiro == "ESPECIAL" ){ 
				$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Ordem: </strong>'+roteiro.ordem);
			} else {
				$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Box: </strong>'+roteiro.box.nome+' - <strong>Ordem: </strong>'+roteiro.ordem);
			}
			$('#idRoteiroSelecionado').val(roteiro.id);
			roteirizacao.populaListaCotasRoteiro(roteiro.id);
			roteirizacao.habilitaBotao("botaoNovaRota",function(){roteirizacao.abrirTelaRota();});
			$('#nomeRota').removeAttr('readonly');
		    roteirizacao.habilitaBotao('botaoNovaRotaNome', function(){roteirizacao.abrirTelaRotaComNome();});
		    
		},
		
		//Busca dados para o auto complete do nome da cota
		populaListaCotasRoteiro : function(roteiroId) {
			$(".cotasDisponiveisGrid").clear();
			$(".cotasRotaGrid").clear();
			roteirizacao.iniciaRotasGrid();
			$(".rotasGrid").flexOptions({
				"url" : contextPath + '/cadastro/roteirizacao/pesquisarRotaPorNome',
				params : [{
					name : "roteiroId",
					value : roteiroId
				}, {
					name : "nomeRota",
					value :$('#nomeRota').val()
				}],
				newp:1
			});
			
			$(".rotasGrid").flexReload();

			
			
		},
		
		iniciaRotasGrid : function(){
			
			$(".rotasGrid").flexigrid({
				preProcess:roteirizacao.callBackRotaGrid,
				dataType : 'json',
				colModel : [ {
					display : '',
					name : 'selecione',
					width : 15,
					sortable : false,
					align : 'center'
				},{
					display : 'Ordem',
					name : 'ordem',
					width : 35,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'descricaoRota',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'detalhe',
					width : 15,
					sortable : false,
					align : 'center'
				}],
				sortname : "codigo",
				width : 270,
				height : 280
			});
		},
		
		callBackRotaGrid :  function (data){
		
			$.each(data.rows, function(index, value) {
				var idRota = $.trim(value.cell.id);
				var selecione = '<input type="checkbox" value="'+idRota +'" name="rotaCheckbox" id="rotaCheckbox"  onclick="roteirizacao.habilitaBotoesRota()" />';
				var detalhe ='<a href="javascript:roteirizacao.cotaSelecionada('+idRota+');" ><img src="'+contextPath+'/images/ico_detalhes.png" border="0" alt="Detalhes" /></a>';
				value.cell.selecione = selecione;
				value.cell.detalhe = detalhe;
	        	
			});
			
			$(".grids").show();
			
			return data;
		},
		
		abrirTelaRota : function () {
			var idRoteiro =  $('#idRoteiroSelecionado').val();
			$('#nomeRotaInclusao').val('');
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRota', { 'idRoteiro' : idRoteiro } ,
					function(result) {
				 			$("#ordemRotaInclusao").numeric();	
		   					$("#ordemRotaInclusao").val(result.int);
		   					$( "#dialog-rota" ).dialog({
		   						resizable: false,
		   						height:210,
		   						width:420,
		   						modal: true,
		   						buttons: {
		   							"Confirmar": function() {
		   								roteirizacao.confirmarInclusaoRota();
		   							},
		   							"Cancelar": function() {
		   								$( this ).dialog( "close" );
		   							}
		   						}
		   					});
					   },
					   null,
					   true
				);

		},
		confirmarInclusaoRota : function() {
			 	$.postJSON(contextPath + '/cadastro/roteirizacao/incluirRota',
						 {
							'roteiroId' :  $("#idRoteiroSelecionado").val(),
							'ordem' :  $("#ordemRotaInclusao").val(),
							'nome' :  $("#nomeRotaInclusao").val()
							
						 },
						   function(result) {
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								$('#dialog-rota').dialog( "close" );
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
								}
								$(".rotasGrid").flexReload();
								
						   },
						   null,
						   true
				);
		},
			
		buscaRotasSelecionadas : function(){
			 rotasSelecinadas = new Array();
			$("input[type=checkbox][name='rotaCheckbox']:checked").each(function(){
				rotasSelecinadas.push(parseInt($(this).val()));
			});
			
			return rotasSelecinadas;
		},
		excluiRotas : function() {
		 	$.postJSON(contextPath + '/cadastro/roteirizacao/excluiRotas',
					 {
						'rotasId' : roteirizacao.buscaRotasSelecionadas(),
						'roteiroId' :  $("#idRoteiroSelecionado").val()
						
					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-rota').dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							$(".rotasGrid").flexReload();
							
					   },
					   null,
					   true
			);
	},
	
	popupExcluirRotas : function() {
	$( "#dialog-excluir-rotas" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					roteirizacao.excluiRotas();
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	popupTransferirRota : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$("#roteiroTranferenciaNome").val('');
		$("#dialog-transfere-rota" ).dialog({
			resizable: false,
			height:320,
			width:410,
			modal: true,
			buttons: {
				"Confirmar": function() {
					if ( transferirRotasComNovoRoteiro ){
						roteirizacao.transferirRotasComNovoRoteiro();
					} else {
						roteirizacao.transferirRotas();
					}
					
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	transferirRotas : function() {
		$('#roteiroTranferenciaSelecionadoId').val(roteiro.id);
		
		var roteiroId = null;
		
		if ( $('#roteiroTranferenciaSelecionadoId').val() != null &&   $.trim( $('#roteiroTranferenciaNome').val()) == $('#roteiroTranferenciaSelecionadoNome').val()) {
			roteiroId =  $('#roteiroTranferenciaSelecionadoId').val();
		}
		
	 	$.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotas',
				 {
					'rotasId' : roteirizacao.buscaRotasSelecionadas(),
					'roteiroId' : roteiroId,
					'roteiroNome' : $('#roteiroTranferenciaNome').val()
					
				 },
				   function(result) {
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						$('#dialog-rota').dialog( "close" );
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
						}
						$(".rotasGrid").flexReload();
						
				   },
				   null,
				   true
		);
	 },
	 transferirRotasComNovoRoteiro : function() {
		   var tipoRoteiro = 'NORMAL';
		   if ( $('input[name=tipoRoteiroTranferencia]').is(':checked') ){
			   tipoRoteiro = 'ESPECIAL';
		   } 
		 	$.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotasComNovoRoteiro',
					 {
		 		
		 		        'rotasId' : roteirizacao.buscaRotasSelecionadas(),
		 		        'idBox' :  $("#boxRoteiroTranferencia").val(),
						'ordem' :  $("#ordemRoteiroTranferencia").val(),
						'roteiroNome' :  $("#roteiroTranferenciaNome").val(),
						'tipoRoteiro' : tipoRoteiro 


					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-rota').dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							$(".rotasGrid").flexReload();
							
					   },
					   null,
					   true
			);
		 },
	 
	 
	 
	 selecionaRoteiroTranferencia : function(roteiro) {
		 $('#roteiroTranferenciaSelecionadoId').val(roteiro.id);
		 $('#roteiroTranferenciaSelecionadoNome').val(roteiro.descricaoRoteiro);
	 },
	 exibiRoteiroNovoTranferencia : function (){
			$('.roteiroNovo').show();
			transferirRotasComNovoRoteiro = true;	
	 },
	 escondeRoteiroNovoTranferencia : function (){
			$('.roteiroNovo').hide();
			transferirRotasComNovoRoteiro = false;	
	  },
		//Busca dados para o auto complete do nome da cota
	  filtroGridRotasPorNome : function() {
		  
		  if ( $.trim($('#nomeRota').val()) != ''){
		    $(".rotasGrid").flexOptions({
					'url' : contextPath + '/cadastro/roteirizacao/pesquisarRotaPorNome',
					params : [{
						name : 'roteiroId',
						value : $('#idRoteiroSelecionado').val()
					}, {
						name : 'nomeRota',
						value :$('#nomeRota').val()
					}],
					newp:1
				});
			  
			  $(".rotasGrid").flexReload();
		  }
		},
		
		iniciaCotasRotaGrid : function(){
			$(".cotasRotaGrid").flexigrid({
				preProcess:roteirizacao.callBackCotasRotaGrid,
				dataType : 'json',
				colModel : [ {
					display : '',
					name : 'selecione',
					width : 15,
					sortable : true,
					align : 'center'
				},{
					display : 'Ordem',
					name : 'ordem',
					width : 35,
					sortable : true,
					align : 'left'
				}, {
					display : 'Pto. Venda',
					name : 'pontoVenda',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Orig.',
					name : 'origemEndereco',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Endereço',
					name : 'endereco',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
					display : 'Cota',
					name : 'numeroCota',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 95,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ordenar',
					name : 'ordenar',
					width : 50,
					sortable : true,
					align : 'right'
				}],
				sortname : "ordem",
				width : 590,
				height : 284
			});
		},
	
		callBackCotasRotaGrid :  function (data){
			var tamanhoLista = data.rows.length ;
			
			$.each(data.rows, function(index, value) {
	
				if ( tamanhoLista == 1 ){
					value.cell.ordenar = '<a href="javascript:;" style="cursor:default;"><img src="'+contextPath+'/images/seta_sobe_desab.gif" border="0" alt="Sobe" hspace="3" /></a><a href="javascript:;" style="cursor:default;"><img src="'+contextPath+'/images/seta_desce_desab.gif" border="0" alt="Desce" hspace="5"  /></a>';
				} else if ( index == 0 ){
					value.cell.ordenar = '<a href="javascript:;"  style="cursor:default;"><img src="'+contextPath+'/images/seta_sobe_desab.gif" border="0" alt="Sobe" hspace="3" /></a><a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoDesc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_desce.gif" border="0" alt="Desce" hspace="5" /></a>';
				}  else if ( index == (tamanhoLista-1)  ){
					value.cell.ordenar = '<a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoAsc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_sobe.gif" border="0" alt="Sobe" hspace="3" /></a><a href="javascript:;"   style="cursor:default;"><img src="'+contextPath+'/images/seta_desce_desab.gif" border="0" alt="Desce" hspace="5" /></a>';
				} else {
					value.cell.ordenar = '<a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoAsc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_sobe.gif" border="0" alt="Sobe" hspace="3" /></a><a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoDesc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_desce.gif" border="0" alt="Desce" hspace="5" /></a>';
				}
				
				
				var selecione = '<input type="checkbox" value="'+value.cell.idRoteirizacao +'" name="roteirizacaoCheckbox" onclick="roteirizacao.habilitaBotoesRoteirizacao()"  id="roteirizacaoCheckbox" />';
				value.cell.selecione = selecione;
				value.cell.origemEndereco = value.cell.idRoteirizacao;
			});
			
			$(".grids").show();
			
			return data;
		},
		
		
		populaCotasRotaGrid : function(rotaId) {
			   $('#rotaSelecionada').val(rotaId);
			    $(".cotasRotaGrid").flexOptions({
						'url' : contextPath + '/cadastro/roteirizacao/buscarRoterizacaoPorRota',
						params : [{
							name : 'rotaId',
							value : rotaId
						}],
						newp:1
					});
				  
				  $(".cotasRotaGrid").flexReload();
		},
		
		populaDadosCota : function(rotaId) {
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscarRotaPorId',
					{ 
						'rotaId' :rotaId
					}
			        ,
					function(result) {
			        	
			        	$('#spanDadosRota').html('<strong>Rota Selecionada:</strong>&nbsp;'+result.descricaoRota+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Ordem:'+result.ordem +'</strong>&nbsp;');
					},
					null,
					true
				);
			
		},
		
		cotaSelecionada : function(rotaId) {
			 roteirizacao.populaDadosCota(rotaId);
	         roteirizacao.populaCotasRotaGrid(rotaId);
	         roteirizacao.habilitaBotao('botaoCotaAusentes', function(){roteirizacao.abrirTelaCotas()}) // desabilitaBotao('botaoCotaAusentes');
	       
		},
		
		
	
		abrirTelaCotas : function () {
			$("#cepPesquisa").mask("99999-999");
			$("#numeroCotaPesquisa").val('');
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaCotas',null,
					function(result) {
			            roteirizacao.populaComboUf(result);
			            roteirizacao.iniciaCotasDisponiveisGrid();
						$( "#dialog-cotas-disponiveis" ).dialog({
							resizable: false,
							height:515,
							width:870,
							modal: true,
							buttons: {
								"Confirmar": function() {
									roteirizacao.confirmaRoteirizacao();
									$( this ).dialog( "close" );
									
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
				   },
					   null,
					   true
				);

		},
		
		buscalistaMunicipio : function () {
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaMunicipio',
					{ 
						'uf' :$('#comboUf').val()
					}
			        ,
					function(result) {
			            roteirizacao.populaMunicipio(result);
					},
					null,
					true
				);

		},
		
		buscalistaBairro : function () {
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaBairro',
					{ 
						'uf' :$('#comboUf').val(),
						'municipio' :$('#comboMunicipio').val()
					}
			        ,
					function(result) {
			            roteirizacao.populaBairro(result);
					},
					null,
					true
				);

		},
		populaComboUf : function(result) {
			$('#comboUf > option').remove();
			$('#comboUf').append('<option value=""> Selecione...</option>');
			roteirizacao.resetComboBairro();
			roteirizacao.resetComboMunicipio();
			
			$.each(result, function(index, row){
				$('#comboUf').append('<option>'+row+'</option>');
				}
			);
			
		},
		populaMunicipio : function(result) {
			roteirizacao.resetComboBairro();
			roteirizacao.resetComboMunicipio();
			$.each(result, function(index, row){
				$('#comboMunicipio').append('<option value="'+row.locNu+'">'+row.locNo+'</option>');
				}
			);
			
		},
		populaBairro : function(result) {
			roteirizacao.resetComboBairro();
			$.each(result, function(index, row){
				$('#comboBairro').append('<option>'+row.baiNo+'</option>');
				}
			);
			
		},
		
		resetComboMunicipio : function(){
			$('#comboMunicipio > option').remove();
			$('#comboMunicipio').append('<option value="" >Todos</option>');
		},
		resetComboBairro : function(){
			$('#comboBairro > option').remove();
			$('#comboBairro').append('<option value="" >Todos</option>');
		},
		
		iniciaCotasDisponiveisGrid : function(){
			$(".cotasDisponiveisGrid").clear();
			$(".cotasDisponiveisGrid").flexigrid({
				preProcess:roteirizacao.callCotasDisponiveisGrid,
				dataType : 'json',
				colModel : [ {
					display : 'Pto. Venda',
					name : 'pontoVenda',
					width : 95,
					sortable : true,
					align : 'left'
				}, {
					display : 'Orig. End',
					name : 'origemEndereco',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Endereço',
					name : 'endereco',
					width : 270,
					sortable : true,
					align : 'left'
				}, {
					display : 'Cota',
					name : 'numeroCota',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ordem',
					name : 'ordem',
					width : 55,
					sortable : true,
					align : 'center'
				}, {
					display : '',
					name : 'selecione',
					width : 20,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				width : 800,
				height : 200
			});
		},
		
		callCotasDisponiveisGrid :  function (data){
			
			if  ( data.rows.length == 0   ){
				 exibirMensagemDialog("WARNING", ["Não exitem cota disponíveis."],'dialogRoteirizacaoCotaDisponivel');
				
			}
			
			$.each(data.rows, function(index, value) {
				var idPontoVenda = value.cell.idPontoVenda;
				var selecione = '<input type="checkbox" value="'+idPontoVenda +'" name="pdvCheckbox" id="pdvCheckbox" />';
				var ordem ='<input type="input" value="'+index +'" name="pdvOrdem'+idPontoVenda+'" id="pdvOrdem'+idPontoVenda+'" size="6" length="6" />';
				value.cell.selecione = selecione;
				value.cell.ordem = ordem;
	        	
			});
			
			$(".grids").show();
			
			return data;
		},
		
		pesquisarPvsPorCota : function(){
			$('#cotaDisponivelPesquisa').html('');
			roteirizacao.carregarNomeCotasPesquisa('cotaDisponivelPesquisa',  $('#numeroCotaPesquisa').val(), function(){roteirizacao.buscarPvsPorCota()} );
		
		},
		
		
		buscarPvsPorCota : function() {
			pesquisaPorCota = true;
		    $(".cotasDisponiveisGrid").flexOptions({
					'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorCota',
					params : [{
						name : 'numeroCota',
						value : $('#numeroCotaPesquisa').val()
					},
					{
						name : "rotaId",
						value :	$('#rotaSelecionada').val()
					},
					
					{
						name : "roteiroId",
						value :	$('#idRoteiroSelecionado').val()
					}
					],
					newp:1
				});
			  
			  $(".cotasDisponiveisGrid").flexReload();
	},
	

	confirmaRoteirizacao : function () {
        var params = roteirizacao.populaParamentrosContaSelecionadas();
     	$.postJSON(contextPath + '/cadastro/roteirizacao/confirmaRoteirizacao', params,
				function(result) {
		           // roteirizacao.populaBairro(result);
     		       $(".cotasRotaGrid").flexReload();
     		        var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
					}
				},
				null,
				true
			);

	},
	
	populaParamentrosContaSelecionadas : function(){
		var dados ="";
		var index = 0;
		$("input[type=checkbox][name='pdvCheckbox']:checked").each(function(){
			if (dados != ""){
				dados+=",";
			}
				
			var idPontoVenda =  $(this).val();
			var ordem = $('#pdvOrdem'+idPontoVenda).val();
			dados+='{name:"lista['+index+'].idPontoVenda",value:'+idPontoVenda+'}, {name:"lista['+index+'].ordem",value:'+ordem+'}';
			index++;
		});
		dados+=',{name:"idRota",value:'+$('#rotaSelecionada').val()+'}';
		var params = '['+dados+ ']';
        
		return eval(params);
	},
	limparTelaRoteirizacao:function(){
		$("#lstRoteiros").val('');
		$(".rotasGrid").clear();
		$(".cotasDisponiveisGrid").clear();
		$(".cotasRotaGrid").clear();
		$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
		$('#spanDadosRota').html('<strong>Rota Selecionada:</strong>&nbsp;&nbsp;&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
	
	},
	
	limparTelaRoteiro:function(){
		$('#boxInclusaoRoteiro').val('')
		$('#nomeInclusaoRoteiro').val('');
		$("#tipoRoteiro").attr("checked",false);
	
	},
	popupTransferirCota : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$('#lstRotaTranferencia').val('');
		$("#dialog-transfere-cotas" ).dialog({
			resizable: false,
			height:250,
			width:410,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					if ( transferirRoteirizacaoComNovaRota) {
						roteirizacao.transferirRoteirizacaoComNovaRota();
					} else {
						roteirizacao.transferirRoteirizacao();
					}
					roteirizacao.escondeRotaNovaTranferencia();
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					roteirizacao.escondeRotaNovaTranferencia();
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	
	//Busca dados para o auto complete do nome da cota
	autoCompletarRotaPorNome : function(idRota, callBack) {
		
		var descricao = $(idRota).val();
		
		descricao = $.trim(descricao);
		
		$(idRota).autocomplete({source: ""});
		
		if (descricao && descricao.length > 1) {
			
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/autoCompletarRotaPorDescricao",
				 {
					'roteiroId' : $('#idRoteiroSelecionado').val(),
					'nomeRota' : descricao
					
				 },
				function(result) { 
					 roteirizacao.exibirAutoComplete(result, idRota ,callBack); 
				},
				null, 
				true
			);
		}
	},
	
	//Busca dados para o auto complete do nome da cota
	autoCompletarRotaPorNome : function(idRota, callBack) {
		
		var descricao = $(idRota).val();
		
		descricao = $.trim(descricao);
		
		$(idRota).autocomplete({source: ""});
		
		if (descricao && descricao.length > 1) {
			
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/autoCompletarRotaPorDescricao",
				 {
					'roteiroId' : $('#idRoteiroSelecionado').val(),
					'nomeRota' : descricao
					
				 },
				function(result) { 
					 roteirizacao.exibirAutoComplete(result, idRota ,callBack); 
				},
				null, 
				true
			);
		}
	},
	
	transferirRoteirizacao : function() {
		var descricao = $('#lstRotaTranferencia').val();
		descricao = $.trim(descricao);
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/transferirRoteirizacao",
				 {
					'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
					'roteiroId' : $('#idRoteiroSelecionado').val(),
					'rotaNome' : descricao
					
				 },
				function(result) { 
					  $(".cotasRotaGrid").flexReload();
	     		        var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
						}
				},
				null, 
				true
			);
	},
	
	transferirRoteirizacaoComNovaRota : function() {
		var descricao = $('#lstRotaTranferencia').val();
		descricao = $.trim(descricao);
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/transferirRoteirizacaoComNovaRota",
				 {
					'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
					'roteiroId' : $('#idRoteiroSelecionado').val(),
					'rotaNome' : descricao,
					'ordem' : $('#ordemRotaTranferencia').val()
					
					
				 },
				function(result) { 
					  $(".cotasRotaGrid").flexReload();
	     		        var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
						}
				},
				null, 
				true
			);
	},
	
	excluirRoteirizacao :  function(){
		$.postJSON(
				contextPath + "/cadastro/roteirizacao/excluirRoteirizacao",
				 {
					'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas()
					
				 },
				function(result) { 
					  $(".cotasRotaGrid").flexReload();
	     		        var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
						}
				},
				null, 
				true
			);
		
	},
	
	popupExcluirRoteirizacao : function() {
		
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir-cotas" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					roteirizacao.excluirRoteirizacao();
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	
	buscaRoteirizacaoSelecionadas : function(){
		 roteirizacaoSelecionadas = new Array();
		$("input[type=checkbox][name='roteirizacaoCheckbox']:checked").each(function(){
			roteirizacaoSelecionadas.push(parseInt($(this).val()));
		});
		
		return roteirizacaoSelecionadas;
	},
	
	buscarPvsPorEndereco : function() {
		pesquisaPorCota = false;
		$('#cotaDisponivelPesquisa').html('');
		var municipio =  $('#comboMunicipio').val() ;
		
		if ( municipio != "" ){
			municipio = $('#comboMunicipio option:selected').text();	
		}
		
		
		
		
	    $(".cotasDisponiveisGrid").flexOptions({
				'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorEndereco',
				params : [{
					name : 'CEP',
					value : $('#cepPesquisa').val()
				},
				{
					name : 'uf',
					value : $('#comboUf').val()
				},
				{
					name : 'municipio',
					value :municipio
				},
				{
					name : 'bairro',
					value : $('#comboBairro').val()
				},
				{
					name : "rotaId",
					value :	$('#rotaSelecionada').val()
				},
				
				{
					name : "roteiroId",
					value :	$('#idRoteiroSelecionado').val()
				}
				],
				newp:1
			});
		  
		  $(".cotasDisponiveisGrid").flexReload();
},

 checarTodasCotasGrid : function() {
	
	 if ( $("#selecionaTodos").is(":checked") ) {
			$("input[type=checkbox][name='pdvCheckbox']").each(function(){
				$(this).attr('checked', true);
			});
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Desmarcar todos";
	 } else {
			$("input[type=checkbox][name='pdvCheckbox']").each(function(){
				$(this).attr('checked', false);
			});
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Marcar todos";
	 }
 } ,

carregarComboRoteiro : function () {
	roteirizacao.resetComboRoteiroPesquisa();
	roteirizacao.resetComboRotaPesquisa();
	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiro',
			{ 
				'boxId' :$('#boxPesquisa').val()
			}
	        ,
			function(result) {
	    			$.each(result, function(index, row){
	    				$('#roteiroPesquisa').append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
	    				}
	    			);
	    	},
			null,
			true
		);

},

carregarComboRota : function () {
	roteirizacao.resetComboRotaPesquisa();
	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRota',
			{ 
				'roteiroId' :$('#roteiroPesquisa').val()
			}
	        ,
			function(result) {
	    		$.each(result, function(index, row){
    				$('#rotaPesquisa').append('<option value="'+row.id+'">'+row.descricaoRota+'</option>');
    				}
    			);
    	
			},
			null,
			true
		);

},

carregarComboRoteiroEspecial : function () {
	roteirizacao.resetComboRoteiroPesquisa();
	roteirizacao.resetComboRotaPesquisa();
	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiroEspecial',null ,
			function(result) {
	    			$.each(result, function(index, row){
	    				$('#roteiroPesquisa').append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
	    				}
	    			);
	    	},
			null,
			true
		);

},

resetComboRoteiroPesquisa : function(){
	$('#roteiroPesquisa > option').remove();
	$('#roteiroPesquisa').append('<option value="" >Selecione...</option>');
},
resetComboRotaPesquisa : function(){
	$('#rotaPesquisa > option').remove();
	$('#rotaPesquisa').append('<option value="" >Selecione...</option>');
},

pesquisaComRoteiroEspecial : function() {
	
	 if ($("#tipoRoteiroPesquisa").is(":checked") ) {
		 roteirizacao.resetComboRoteiroPesquisa();
		 roteirizacao.resetComboRotaPesquisa();
		 $('#boxPesquisa').attr("disabled", "disabled");
		 $('#boxPesquisa').val("");
		 roteirizacao.carregarComboRoteiroEspecial();
		  
	 } else {
		 roteirizacao.resetComboRoteiroPesquisa();
		 roteirizacao.resetComboRotaPesquisa();
		 $('#boxPesquisa').val("");
		 $('#roteiroPesquisa').removeAttr("disabled");
	 }
} ,
roteiroEspecial : function() {
	
	 if ($("#tipoRoteiroTranferencia").is(":checked") ) {
		 $('#boxRoteiroTranferencia').attr("disabled", "disabled");
		 $('#boxRoteiroTranferencia').val("");
		  
	 } else {
		 $('#boxRoteiroTranferencia').val("");
		 $('#boxRoteiroTranferencia').removeAttr("disabled");
	 }
} ,

roteiroEspecialNovo : function() {
	
	 if ($("#tipoRoteiro").is(":checked") ) {
		 $('#boxInclusaoRoteiro').attr("disabled", "disabled");
		 $('#boxInclusaoRoteiro').val("");
		  
	 } else {
		 $('#boxInclusaoRoteiro').val("");
		 $('#boxInclusaoRoteiro').removeAttr("disabled");
	 }
} ,


iniciarPesquisaRoteirizacaoGrid : function () {
	$(".rotaRoteirosGrid").clear();
		
		$(".rotaRoteirosGrid").flexigrid({
		preProcess: roteirizacao.callBackPesquisaRoteirizacaoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'nomeBox',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'descricaoRoteiro',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'descricaoRota',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 360,
				sortable : true,
				align : 'left'
			}],
			sortname : "nomeBox",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255,
			singleSelect : true
			
		});
	},

	callBackPesquisaRoteirizacaoGrid: function (data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return data;
		}
		$.each(data.rows, function(index, value) {
			
//			value.cell.box = value.cell.rota.roteiro.box.nome
//        	value.cell.roteiro = value.cell.rota.roteiro.descricaoRoteiro;
//			value.cell.rota = value.cell.rota.descricaoRota;
//			value.cell.cota = value.cell.pdv.cota.numeroCota;
//			value.cell.nome = value.cell.pdv.cota.pessoa.nome;
		});
		
		$(".grids").show();
		
		return data;
	},
	

	pesquisarRoteirizacao: function () {
		$('#nomeCotaPesquisa').html('');
		var tipoRoteiro = "NORMAL";
		 if ($("#tipoRoteiroTranferencia").is(":checked") ) {
			 tipoRoteiro = "ESPECIAL";
		 }
		 pesquisaRoteizicaoPorCota = false;
		
		roteirizacao.iniciarPesquisaRoteirizacaoGrid();
		$(".rotaRoteirosGrid").clear();
			$(".rotaRoteirosGrid").flexOptions({
				"url" : contextPath + '/cadastro/roteirizacao/pesquisarRoteirizacao',
				params : [{
					name : "boxId",
					value : $('#boxPesquisa').val()
				}, {
					name : "roteiroId",
					value : $('#roteiroPesquisa').val()
				}, {
					name : "rotaId",
					value : $('#rotaPesquisa').val()
				},
				{
					name : "tipoRoteiro",
					value : tipoRoteiro
				}],
				
				
				newp:1
			});
			
			$(".rotaRoteirosGrid").flexReload();
	},
	
	
	
	

	buscarRoteirizacaoPorCota: function () {
		var tipoRoteiro = "NORMAL";
		 if ($("#tipoRoteiroTranferencia").is(":checked") ) {
			 tipoRoteiro = "ESPECIAL";
		 }
		 pesquisaRoteizicaoPorCota = true;
		roteirizacao.iniciarPesquisaRoteirizacaoGrid();
		$(".rotaRoteirosGrid").clear();
			$(".rotaRoteirosGrid").flexOptions({
				"url" : contextPath + '/cadastro/roteirizacao/pesquisarRoteirizacaoPorCota',
				params : [{
					name : "numeroCota",
					value : $('#cotaPesquisa').val()
				}, 
				{
					name : "tipoRoteiro",
					value : tipoRoteiro
				}],
				
				
				newp:1
			});
			
			$(".rotaRoteirosGrid").flexReload();
	},
	
	
	
	carregarNomeCotas : function (campoExibicao, numeroCota, callBack) {
		$('#'+campoExibicao).html('');
		var result = false;
		$.postJSON(contextPath + '/cadastro/roteirizacao/buscaCotaPorNumero',
				{
			      'numeroCota': numeroCota
				} ,
				function(result) {
					  var tipoMensagem = result.tipoMensagem;
					  var listaMensagens = result.listaMensagens;
					  if (tipoMensagem && listaMensagens) {
							exibirMensagem(tipoMensagem, listaMensagens);
					   } else {
						   $('#'+campoExibicao).html(result.pessoa.nome);
						   if (callBack){
							   callBack();
						   }
						   
					   }
					  
					
					result = true;	
		    	},
				null,
				true
			);
		 return result;	

	},
	
	pesquisarRoteirizacaoPorCota : function(){
		roteirizacao.carregarNomeCotas('nomeCotaPesquisa',  $('#cotaPesquisa').val(), function(){roteirizacao.buscarRoteirizacaoPorCota()});

	},
	
	popupRoteirizacao: function() {
		roteirizacao.reiniciaTelaRoteirizacao();
	    $( "#dialog-roteirizacao" ).dialog({
				resizable: false,
				height:590,
				width:955,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		},
		
	habilitaBotao: function(idBotao, funcao){
		$('#'+idBotao).css("cursor","");
		$('#'+idBotao).css("opacity","1");
		$('#'+idBotao).css("filter","alpha(opaity=100)");
		$('#'+idBotao).click(funcao);
		
	},
	desabilitaBotao: function(idBotao){
		$('#'+idBotao).css("cursor","default");
		$('#'+idBotao).css("opacity","0.4");
		$('#'+idBotao).css("filter","alpha(opaity=40)");
		$('#'+idBotao).unbind('click');
	},
	
	habilitaBotoesRota : function() {
		
		listaRotas = roteirizacao.buscaRotasSelecionadas();
		
		 if (listaRotas.length == 0 ) {
			 roteirizacao.desabilitaBotao('botaoTransfereciaRota');
			 roteirizacao.desabilitaBotao('botaoExcluirRota');
			 
		 } else {
			 roteirizacao.habilitaBotao('botaoTransfereciaRota', function(){roteirizacao.popupTransferirRota()});
			 roteirizacao.habilitaBotao('botaoExcluirRota',function(){roteirizacao.popupExcluirRotas()});
		 }
	} ,
	
	habilitaBotoesRoteirizacao : function() {
		
		listaRoteirizacao = roteirizacao.buscaRoteirizacaoSelecionadas()
		
		 if (listaRoteirizacao.length == 0 ) {
			 roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro');
				roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao');
			 
		 } else {
			 roteirizacao.habilitaBotao('botaoTransferenciaRoteiro', function(){roteirizacao.popupTransferirCota()});
			 roteirizacao.habilitaBotao('botaoExcluirRoteirizacao',function(){roteirizacao.popupExcluirRoteirizacao()});
		 }
	} ,
	
	reiniciaTelaRoteirizacao :function(){
		$(".rotasGrid").clear();
		$(".cotasDisponiveisGrid").clear();
		$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
		roteirizacao.iniciaRotasGrid();
		roteirizacao.iniciaCotasDisponiveisGrid();
		roteirizacao.iniciaCotasRotaGrid();
		roteirizacao.desabilitaBotao('botaoTransfereciaRota');
		roteirizacao.desabilitaBotao('botaoExcluirRota');
		roteirizacao.desabilitaBotao('botaoNovaRota');
		roteirizacao.desabilitaBotao('botaoCotaAusentes');
		roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro');
		roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao');
		$('botaoNovaRotaNome').val('');
		roteirizacao.desabilitaBotao('botaoNovaRotaNome');
		$('#nomeRota').attr("readonly","true");
		$('#lstRoteiros').val('');
		roteirizacao.escondeRotaNovaTranferencia();
		
		
	},
	
	abrirTelaRotaComNome :function(){
		$('#nomeRotaInclusao').val($('botaoNovaRotaNome').val());
		roteirizacao.abrirTelaRota();
		
		
	},
	 exibiRotaNovaTranferencia : function (){
			$('.rotaNovaTransferencia').show();
			transferirRoteirizacaoComNovaRota = true;	
	 },
	 escondeRotaNovaTranferencia : function (){
			$('.rotaNovaTransferencia').hide();
			transferirRoteirizacaoComNovaRota = false;	
	  },
	  
	  atualizaOrdenacaoAsc : function(roteirizacaoId, ordem, idPontoVenda){
		  $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
					{
			  				'roteirizacaoId' :roteirizacaoId,
			  				'rotaId':$('#rotaSelecionada').val() ,
			  				'roteiroId' :$('#idRoteiroSelecionado').val(),
			  				'ordem':ordem,
			  				'pontoVendaId':idPontoVenda,
			  				'ordenacao' : 'ASC'
					} ,
					function(result) {
						 $(".cotasRotaGrid").flexReload();
			    	},
					null,
					true
				);
	  },
	  
	  atualizaOrdenacaoDesc : function(roteirizacaoId, ordem, idPontoVenda){
		  $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
					{
			  				'roteirizacaoId' :roteirizacaoId,
			  				'rotaId':$('#rotaSelecionada').val() ,
			  				'roteiroId' :$('#idRoteiroSelecionado').val(),
			  				'ordem':ordem,
			  				'pontoVendaId':idPontoVenda,
			  				'ordenacao' : 'DESC'
					} ,
					function(result) {
						 $(".cotasRotaGrid").flexReload();
			    	},
					null,
					true
				);
	  },
	  
		carregarNomeCotasPesquisa : function (campoExibicao, numeroCota, callBack) {
			$('#'+campoExibicao).html('');
			var result = false;
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscaCotaPorNumero',
					{
				      'numeroCota': numeroCota
					} ,
					function(result) {
						  var tipoMensagem = result.tipoMensagem;
						  var listaMensagens = result.listaMensagens;
						  if (tipoMensagem && listaMensagens) {
							  exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacaoCotaDisponivel');
						  } else {
							  $('#'+campoExibicao).html(result.pessoa.nome);
							  if (callBack){
								  callBack();
							  }
						  }
						  
						
						result = true;	
			    	},
					null,
					true
				);
			
			return  result

		},
		 gerarArquivoRoteirizacao : function(fileType) {
			 var tipoRoteiro = "NORMAL";
			 if ($("#tipoRoteiroTranferencia").is(":checked") ) {
				 tipoRoteiro = "ESPECIAL";
			 }
			 
			window.location = 
				contextPath + 
				"/cadastro/roteirizacao/imprimirArquivo?" + 
				"boxId=" +  $('#boxPesquisa').val() +
				"&roteiroId=" + $('#roteiroPesquisa').val() + 
				"&rotaId=" + $('#rotaPesquisa').val() +
				"&tipoRoteiro=" + tipoRoteiro +
				"&numeroCota=" + $('#cotaPesquisa').val() +
				"&pesquisaRoteizicaoPorCota=" +pesquisaRoteizicaoPorCota +
				"&sortname=" + $(".rotaRoteirosGrid").flexGetSortName() +
				"&sortorder=" + $(".rotaRoteirosGrid").getSortOrder() +
				"&rp=" + $(".rotaRoteirosGrid").flexGetRowsPerPage() +
				"&page=" + $(".rotaRoteirosGrid").flexGetPageNumber() +
				"&fileType=" + fileType;


			return false;
		},
		
		imprimirArquivo : function (fileType) {
			 var tipoRoteiro = "NORMAL";
			 if ($("#tipoRoteiroTranferencia").is(":checked") ) {
				 tipoRoteiro = "ESPECIAL";
			 }
			 
			window.location = 
				contextPath + 
				"/cadastro/roteirizacao/imprimirArquivo?" + 
				"boxId=" +  $('#boxPesquisa').val() +
				"&roteiroId=" + $('#roteiroPesquisa').val() + 
				"&rotaId=" + $('#rotaPesquisa').val() +
				"&tipoRoteiro=" + tipoRoteiro +
				"&numeroCota=" + $('#cotaPesquisa').val() +
				"&pesquisaRoteizicaoPorCota=" +pesquisaRoteizicaoPorCota +
				"&sortname=" + $(".rotaRoteirosGrid").flexGetSortName() +
				"&sortorder=" + $(".rotaRoteirosGrid").getSortOrder() +
				"&rp=" + $(".rotaRoteirosGrid").flexGetRowsPerPage() +
				"&page=" + $(".rotaRoteirosGrid").flexGetPageNumber() +
				"&fileType=" + fileType;

			return false;
		}
	
	  
		
};