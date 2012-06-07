var roteiroSelecionadoAutoComplete = false;	
var transferirRotasComNovoRoteiro = false;


function CotaDisponivelRoteirizacaoDTO  (idPontoVenda, pontoVenda, origemEndereco, endereco, numeroCota, nome, ordem  ){
	this.idPontoVenda = idPontoVenda;
	this.pontoVenda = pontoVenda;
	this.origemEndereco = origemEndereco;
	this.endereco = endereco;
	this.numeroCota = numeroCota;
	this.nome = nome;
	this.ordem = ordem; 
	
} 
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
								exibirMensagem(tipoMensagem, listaMensagens);
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
				},
				close : function(event, ui) {
				},
				select : function(event, ui) {
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
			$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Box: </strong>'+roteiro.box.nome+' - <strong>Ordem: </strong>'+roteiro.ordem);
			$('#idRoteiroSelecionado').val(roteiro.id);
			roteirizacao.populaListaCotasRoteiro(roteiro.id);
			
		},
		
		//Busca dados para o auto complete do nome da cota
		populaListaCotasRoteiro : function(roteiroId) {
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
				var selecione = '<input type="checkbox" value="'+idRota +'" name="rotaCheckbox" id="rotaCheckbox" />';
				var detalhe ='<a href="javascript:roteirizacao.cotaSelecionada('+idRota+');"><img src="'+contextPath+'/images/ico_detalhes.png" border="0" alt="Detalhes" /></a>';
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
		dialog-excluir-cotas
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
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
			height:300,
			width:400,
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
			
			$.each(data.rows, function(index, value) {
				value.cell.ordenar = '<a href="javascript:;"><img src="'+contextPath+'/images/seta_sobe.gif" border="0" alt="Sobe" hspace="3" /></a><a href="javascript:;"><img src="'+contextPath+'/images/seta_desce_desab.gif" border="0" alt="Desce" hspace="5" /></a>';
				var selecione = '<input type="checkbox" value="'+value.cell.idRoteirizacao +'" name="roteirizacaoCheckbox" id="roteirizacaoCheckbox" />';
				value.cell.selecione = selecione;
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
		},
		
		
	
		abrirTelaCotas : function () {
			$("#cepPesquisa").mask("99999-999");
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaCotas',null,
					function(result) {
			            roteirizacao.populaComboUf(result);
			            roteirizacao.iniciaCotasDisponiveisGrid();
						$( "#dialog-cotas-disponiveis" ).dialog({
							resizable: false,
							height:490,
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
				$('#comboMunicipio').append('<option>'+row+'</option>');
				}
			);
			
		},
		populaBairro : function(result) {
			roteirizacao.resetComboBairro();
			$.each(result, function(index, row){
				$('#comboBairro').append('<option>'+row+'</option>');
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
		
		buscarPvsPorCota : function() {
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
			height:220,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					roteirizacao.transferirRoteirizacao();
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
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
	
	//Busca dados para o auto complete do nome da cota
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
					value : $('#comboMunicipio').val()
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

};