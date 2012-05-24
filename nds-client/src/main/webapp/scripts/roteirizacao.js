var roteiroSelecionadoAutoComplete = false;	
var roteirizacao = {

		abrirTelaRoteiro : function () {
			
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRoteiro',null,
					function(result) {
				 			$("#ordemInclusaoRoteiro").numeric();	
		   					$("#ordemInclusaoRoteiro").val(result.int);
		   					$( "#dialog-roteiro" ).dialog({
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
		autoCompletarRoteiroPorNome : function(idRoteiro, isFromModal) {
			
			var descricao = $(idRoteiro).val();
			
			descricao = $.trim(descricao);
			
			$(idRoteiro).autocomplete({source: ""});
			
			if (descricao && descricao.length > 2) {
				
				$.postJSON(
					contextPath + "/cadastro/roteirizacao/autoCompletarRoteiroPorDescricao",
					 {
						'descricao' : descricao
						
					 },
					function(result) { 
						 roteirizacao.exibirAutoComplete(result, idRoteiro); 
					},
					null, 
					isFromModal
				);
			}
		},
		//Exibe o auto complete no campo
		exibirAutoComplete : function(result, idCampo) {
			$(idCampo).autocomplete({
				source: result,
				focus : function(event, ui) {
				},
				close : function(event, ui) {
				},
				select : function(event, ui) {
					roteirizacao.populaDadosRoteiro(ui.item.chave);
					roteiroSelecionadoAutoComplete = true;
				},
				minLength: 3,
				delay : 0,
			});
		},
		//Busca dados para o auto complete do nome da cota
		buscaRoteiroPorNome : function(idRoteiro, isFromModal) {
			if ( !roteiroSelecionadoAutoComplete ) {
				var descricao = $(idRoteiro).val();
				descricao = $.trim(descricao);
					$.postJSON(
						contextPath + "/cadastro/roteirizacao/buscaRoteiroPorDescricao",
						 {
							'descricao' : descricao
							
						 },
						function(result) { 
							 roteirizacao.populaDadosRoteiro(result[0]);
						},
						null, 
						isFromModal
					);
			}	
		},
		
		//Busca dados para o auto complete do nome da cota
		populaDadosRoteiro : function(roteiro) {
			$('#spanDadosRoteiro').html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Box: </strong>'+roteiro.box.nome+' - <strong>Ordem: </strong>'+roteiro.ordem);
			$('#idRoteiroSelecionado').val(roteiro.id);
			roteirizacao.populaListaCotasRoteiro(roteiro.id);
			
		},
		
		//Busca dados para o auto complete do nome da cota
		populaListaCotasRoteiro : function(roteiroId) {
			roteirizacao.iniciaRotasGrid();
			$(".rotasGrid").flexOptions({
				"url" : contextPath + '/cadastro/roteirizacao/buscaRotasPorRoteiro',
				params : [{
					name : "roteiroId",
					value : roteiroId
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
				var idRota = value.cell.id;
				var selecione = '<input type="checkbox" value="'+idRota +'" name="rotaCheckbox" id="rotaCheckbox" />';
				var detalhe ='<a href="javascript:;"><img src="'+contextPath+'/images/ico_detalhes.png" border="0" alt="Detalhes" /></a>';
				value.cell.selecione = selecione;
				value.cell.detalhe = detalhe;
	        	
			});
			
			$(".grids").show();
			
			return data;
		},
		
		abrirTelaRota : function () {
			
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRota',null,
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
	
		$( "#dialog-transfere-rota" ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					roteirizacao.transferirRotas();
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	transferirRotas : function() {
	 	$.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotas',
				 {
					'rotasId' : roteirizacao.buscaRotasSelecionadas(),
					'roteiroId' : $('#roteiroTranferenciaNome').val()
					
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
	
		
		
};