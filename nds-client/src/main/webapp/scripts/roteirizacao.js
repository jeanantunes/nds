var roteiroSelecionadoAutoComplete = false;	
var transferirRotasComNovoRoteiro = false;
var transferirRoteirizacaoComNovaRota= false;
var pesquisaPorCota = false;

var roteirizacao = $.extend(true, {
	
		abrirTelaRoteiro : function () {
			
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRoteiro',null,
					function(result) {
							roteirizacao.limparTelaRoteiro();
				            $('#ordemInclusaoRoteiro', roteirizacao.workspace).numeric();
				            $("#ordemInclusaoRoteiro", roteirizacao.workspace).val(result.int);
		   					$("#dialog-roteiro", roteirizacao.workspace ).dialog({
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
		   						},
		   						form: $("#dialog-roteiro", this.workspace).parents("form")
		   					});
					   },
					   null,
					   true
				);
			
			this.init();

		},
		
		
	   confirmarInclusaoRoteiro : function() {
		   var tipoRoteiro = 'NORMAL';
		   if ( $('input[name=tipoRoteiro]').is(':checked') ){
			   tipoRoteiro = 'ESPECIAL';
		   } 
		   
			$.postJSON(contextPath + '/cadastro/roteirizacao/incluirRoteiro',
					 {
						'idBox' :  $("#boxInclusaoRoteiro", roteirizacao.workspace).val(),
						'ordem' :  $("#ordemInclusaoRoteiro", roteirizacao.workspace).val(),
						'nome' :  $("#nomeInclusaoRoteiro", roteirizacao.workspace).val(),
						'tipoRoteiro' : tipoRoteiro
						
					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-roteiro', roteirizacao.workspace).dialog( "close" );
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
			
			var descricao = $(idRoteiro, roteirizacao.workspace).val();
			
			descricao = $.trim(descricao);
			
			$(idRoteiro, roteirizacao.workspace).autocomplete({source: ""});
			
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
			$(idCampo, roteirizacao.workspace).autocomplete({
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
								$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
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
				$('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Ordem: </strong>'+roteiro.ordem);
			} else {
				$('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Box: </strong>'+roteiro.box.nome+' - <strong>Ordem: </strong>'+roteiro.ordem);
			}
			$('#idRoteiroSelecionado', roteirizacao.workspace).val(roteiro.id);
			roteirizacao.populaListaCotasRoteiro(roteiro.id);
			roteirizacao.habilitaBotao("botaoNovaRota",function(){roteirizacao.abrirTelaRota();});
			$('#nomeRota', roteirizacao.workspace).removeAttr('readonly');
		    roteirizacao.habilitaBotao('botaoNovaRotaNome', function(){roteirizacao.abrirTelaRotaComNome();});
		    
		},
		
		//Busca dados para o auto complete do nome da cota
		populaListaCotasRoteiro : function(roteiroId) {
			$(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
			$(".cotasRotaGrid", roteirizacao.workspace).clear();
			roteirizacao.iniciaRotasGrid();
			$(".rotasGrid", roteirizacao.workspace).flexOptions({
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
			
			$(".rotasGrid", roteirizacao.workspace).flexReload();

			
			
		},
		
		iniciaRotasGrid : function(){
			
			$(".rotasGrid", roteirizacao.workspace).flexigrid({
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
				height : 220
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
			
			$(".grids", roteirizacao.workspace).show();
			
			return data;
		},
		
		abrirTelaRota : function () {
			var idRoteiro =  $('#idRoteiroSelecionado', roteirizacao.workspace).val();
			$('#nomeRotaInclusao', roteirizacao.workspace).val('');
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaRota', { 'idRoteiro' : idRoteiro } ,
					function(result) {
				 			$("#ordemRotaInclusao", roteirizacao.workspace).numeric();	
		   					$("#ordemRotaInclusao", roteirizacao.workspace).val(result.int);
		   					$( "#dialog-rota", roteirizacao.workspace ).dialog({
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
		   						},
		   						form: $("#dialog-rota", this.workspace).parents("form")
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
								$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
								}
								$(".rotasGrid", roteirizacao.workspace).flexReload();
								
						   },
						   null,
						   true
				);
		},
			
		buscaRotasSelecionadas : function(){
			 rotasSelecinadas = new Array();
			$("input[type=checkbox][name='rotaCheckbox']:checked", roteirizacao.workspace).each(function(){
				rotasSelecinadas.push(parseInt($(this).val()));
			});
			
			return rotasSelecinadas;
		},
		excluiRotas : function() {
		 	$.postJSON(contextPath + '/cadastro/roteirizacao/excluiRotas',
					 {
						'rotasId' : roteirizacao.buscaRotasSelecionadas(),
						'roteiroId' :  $("#idRoteiroSelecionado", roteirizacao.workspace).val()
						
					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							$(".rotasGrid", roteirizacao.workspace).flexReload();
							
					   },
					   null,
					   true
			);
	},
	
	popupExcluirRotas : function() {
	$( "#dialog-excluir-rotas", roteirizacao.workspace ).dialog({
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
			},
			form: $("#dialog-excluir-rotas", this.workspace).parents("form")
		});	
		      
	},
	
	popupDetalhesCota : function(title, box, roteiro, rota) {
		
		$('#legendDetalhesCota').html(title);
		
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:420,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-detalhes", this.workspace).parents("form")
		});
	},
	
	
	popupTransferirRota : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$("#roteiroTranferenciaNome", roteirizacao.workspace).val('');
		$("#dialog-transfere-rota", roteirizacao.workspace ).dialog({
			resizable: false,
			height:'auto',
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
			},
			form: $("#dialog-transfere-rota", this.workspace).parents("form")
		});	
		      
	},
	transferirRotas : function() {
		$('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val(roteiro.id);
		
		var roteiroId = null;
		
		if ( $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val() != null &&   $.trim( $('#roteiroTranferenciaNome', roteirizacao.workspace).val()) == $('#roteiroTranferenciaSelecionadoNome', roteirizacao.workspace).val()) {
			roteiroId =  $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val();
		}
		
	 	$.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotas',
				 {
					'rotasId' : roteirizacao.buscaRotasSelecionadas(),
					'roteiroId' : roteiroId,
					'roteiroNome' : $('#roteiroTranferenciaNome', roteirizacao.workspace).val()
					
				 },
				   function(result) {
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
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
		 		        'idBox' :  $("#boxRoteiroTranferencia", roteirizacao.workspace).val(),
						'ordem' :  $("#ordemRoteiroTranferencia", roteirizacao.workspace).val(),
						'roteiroNome' :  $("#roteiroTranferenciaNome", roteirizacao.workspace).val(),
						'tipoRoteiro' : tipoRoteiro 


					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							$(".rotasGrid", roteirizacao.workspace).flexReload();
							
					   },
					   null,
					   true
			);
		 },
	 
	 
	 
	 selecionaRoteiroTranferencia : function(roteiro) {
		 $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val(roteiro.id);
		 $('#roteiroTranferenciaSelecionadoNome', roteirizacao.workspace).val(roteiro.descricaoRoteiro);
	 },
	 exibiRoteiroNovoTranferencia : function (){
			$('.roteiroNovo', roteirizacao.workspace).show();
			transferirRotasComNovoRoteiro = true;	
	 },
	 escondeRoteiroNovoTranferencia : function (){
			$('.roteiroNovo', roteirizacao.workspace).hide();
			transferirRotasComNovoRoteiro = false;	
	  },
		//Busca dados para o auto complete do nome da cota
	  filtroGridRotasPorNome : function() {
		  
		  if ( $.trim($('#nomeRota', roteirizacao.workspace).val()) != ''){
		    $(".rotasGrid", roteirizacao.workspace).flexOptions({
					'url' : contextPath + '/cadastro/roteirizacao/pesquisarRotaPorNome',
					params : [{
						name : 'roteiroId',
						value : $('#idRoteiroSelecionado', roteirizacao.workspace).val()
					}, {
						name : 'nomeRota',
						value :$('#nomeRota', roteirizacao.workspace).val()
					}],
					newp:1
				});
			  
			  $(".rotasGrid", roteirizacao.workspace).flexReload();
		  }
		},
		
		iniciaCotasRotaGrid : function(){
			$(".cotasRotaGrid").flexigrid({
				preProcess:roteirizacao.callBackCotasRotaGrid,
				dataType : 'json',
				colModel : [ {
					display : '',
					name : 'selecione',
					width : 20,
					sortable : true,
					align : 'left'
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
				height : 220
			});
		},
	
		callBackCotasRotaGrid :  function (data){
			var tamanhoLista = data.rows.length ;
			
			$.each(data.rows, function(index, value) {
	
				if ( tamanhoLista == 1 ){
					value.cell.ordenar = '<a href="javascript:;" style="cursor:default; margin-right:5px;"><img src="'+contextPath+'/images/seta_sobe_desab.gif" border="0" alt="Sobe" /></a><a href="javascript:;" style="cursor:default;margin-right:5px;"><img src="'+contextPath+'/images/seta_desce_desab.gif" border="0" alt="Desce"  /></a>';
				} else if ( index == 0 ){
					value.cell.ordenar = '<a href="javascript:;"  style="cursor:default; margin-right:5px;"><img src="'+contextPath+'/images/seta_sobe_desab.gif" border="0" alt="Sobe"  /></a><a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoDesc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_desce.gif" border="0" alt="Desce"  /></a>';
				}  else if ( index == (tamanhoLista-1)  ){
					value.cell.ordenar = '<a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoAsc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )" style=" margin-right:5px;"><img src="'+contextPath+'/images/seta_sobe.gif" border="0" alt="Sobe" style=" margin-right:10px;" /></a><a href="javascript:;"   style="cursor:default;"><img src="'+contextPath+'/images/seta_desce_desab.gif" border="0" alt="Desce" /></a>';
				} else {
					value.cell.ordenar = '<a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoAsc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )" style=" margin-right:5px;"><img src="'+contextPath+'/images/seta_sobe.gif" border="0" alt="Sobe" /></a><a href="javascript:;" onclick="roteirizacao.atualizaOrdenacaoDesc('+value.cell.idRoteirizacao+','+value.cell.ordem+','+value.cell.idPontoVenda+' )"><img src="'+contextPath+'/images/seta_desce.gif" border="0" alt="Desce"  /></a>';
				}
				
				
				var selecione = '<input type="checkbox" value="'+value.cell.idRoteirizacao +'" name="roteirizacaoCheckbox" onclick="roteirizacao.habilitaBotoesRoteirizacao()"  id="roteirizacaoCheckbox" />';
				value.cell.selecione = selecione;
				value.cell.origemEndereco = value.cell.idRoteirizacao;
			});
			
			$(".grids", roteirizacao.workspace).show();
			
			return data;
		},
		
		
		populaCotasRotaGrid : function(rotaId) {
			   $('#rotaSelecionada', roteirizacao.workspace).val(rotaId);
			    $(".cotasRotaGrid", roteirizacao.workspace).flexOptions({
						'url' : contextPath + '/cadastro/roteirizacao/buscarRoterizacaoPorRota',
						params : [{
							name : 'rotaId',
							value : rotaId
						}],
						newp:1
					});
				  
				  $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
		},
		
		populaDadosCota : function(rotaId) {
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscarRotaPorId',
					{ 
						'rotaId' :rotaId
					}
			        ,
					function(result) {
			        	
			        	$('#spanDadosRota', roteirizacao.workspace).html('<strong>Rota Selecionada:</strong>&nbsp;'+result.descricaoRota+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Ordem:'+result.ordem +'</strong>&nbsp;');
					},
					null,
					true
				);
			
		},
		
		cotaSelecionada : function(rotaId) {
			 roteirizacao.populaDadosCota(rotaId);
	         roteirizacao.populaCotasRotaGrid(rotaId);
	         roteirizacao.habilitaBotao('botaoCotaAusentes', function(){roteirizacao.abrirTelaCotas();}); // desabilitaBotao('botaoCotaAusentes');
	       
		},
		
		
	
		abrirTelaCotas : function () {
			$("#cepPesquisa", roteirizacao.workspace).mask("99999-999");
			$("#numeroCotaPesquisa", roteirizacao.workspace).val('');
			$.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaCotas',null,
					function(result) {
			            roteirizacao.populaComboUf(result);
			            roteirizacao.iniciaCotasDisponiveisGrid();
						$( "#dialog-cotas-disponiveis", roteirizacao.workspace ).dialog({
							resizable: false,
							height:470,
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
							},
							form: $("#dialog-cotas-disponiveis", this.workspace).parents("form")
						});
				   },
					   null,
					   true
				);

		},
		
		buscalistaMunicipio : function () {
			$.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaMunicipio',
					{ 
						'uf' :$('#comboUf', roteirizacao.workspace).val()
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
						'uf' :$('#comboUf', roteirizacao.workspace).val(),
						'municipio' :$('#comboMunicipio', roteirizacao.workspace).val()
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
			$('#comboUf > option', roteirizacao.workspace).remove();
			$('#comboUf', roteirizacao.workspace).append('<option value=""> Selecione...</option>');
			roteirizacao.resetComboBairro();
			roteirizacao.resetComboMunicipio();
			
			$.each(result, function(index, row){
				$('#comboUf', roteirizacao.workspace).append('<option>'+row+'</option>');
				}
			);
			
		},
		populaMunicipio : function(result) {
			roteirizacao.resetComboBairro();
			roteirizacao.resetComboMunicipio();
			$.each(result, function(index, row){
				$('#comboMunicipio', roteirizacao.workspace).append('<option value="'+row.locNu+'">'+row.locNo+'</option>');
				}
			);
			
		},
		populaBairro : function(result) {
			roteirizacao.resetComboBairro();
			$.each(result, function(index, row){
				$('#comboBairro', roteirizacao.workspace).append('<option>'+row.baiNo+'</option>');
				}
			);
			
		},
		
		resetComboMunicipio : function(){
			$('#comboMunicipio > option', roteirizacao.workspace).remove();
			$('#comboMunicipio', roteirizacao.workspace).append('<option value="" >Todos</option>');
		},
		resetComboBairro : function(){
			$('#comboBairro > option', roteirizacao.workspace).remove();
			$('#comboBairro', roteirizacao.workspace).append('<option value="" >Todos</option>');
		},
		
		iniciaCotasDisponiveisGrid : function(){
			$(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
			$(".cotasDisponiveisGrid", roteirizacao.workspace).flexigrid({
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
			
			$(".grids", roteirizacao.workspace).show();
			
			return data;
		},
		
		pesquisarPvsPorCota : function(){
			$('#cotaDisponivelPesquisa', roteirizacao.workspace).html('');
			roteirizacao.carregarNomeCotasPesquisa('cotaDisponivelPesquisa',  $('#numeroCotaPesquisa', roteirizacao.workspace).val(), function(){roteirizacao.buscarPvsPorCota();} );
		
		},
		
		
		buscarPvsPorCota : function() {
			pesquisaPorCota = true;
		    $(".cotasDisponiveisGrid", roteirizacao.workspace).flexOptions({
					'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorCota',
					params : [{
						name : 'numeroCota',
						value : $('#numeroCotaPesquisa', roteirizacao.workspace).val()
					},
					{
						name : "rotaId",
						value :	$('#rotaSelecionada', roteirizacao.workspace).val()
					},
					
					{
						name : "roteiroId",
						value :	$('#idRoteiroSelecionado', roteirizacao.workspace).val()
					}
					],
					newp:1
				});
			  
			  $(".cotasDisponiveisGrid", roteirizacao.workspace).flexReload();
	},
	

	confirmaRoteirizacao : function () {
        var params = roteirizacao.populaParamentrosContaSelecionadas();
     	$.postJSON(contextPath + '/cadastro/roteirizacao/confirmaRoteirizacao', params,
				function(result) {
		           // roteirizacao.populaBairro(result);
     		       $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
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
		$("input[type=checkbox][name='pdvCheckbox']:checked", roteirizacao.workspace).each(function(){
			if (dados != ""){
				dados+=",";
			}
				
			var idPontoVenda =  $(this).val();
			var ordem = $('#pdvOrdem'+idPontoVenda).val();
			dados+='{name:"lista['+index+'].idPontoVenda",value:'+idPontoVenda+'}, {name:"lista['+index+'].ordem",value:'+ordem+'}';
			index++;
		});
		dados+=',{name:"idRota",value:'+$('#rotaSelecionada', roteirizacao.workspace).val()+'}';
		var params = '['+dados+ ']';
        
		return eval(params);
	},
	limparTelaRoteirizacao:function(){
		$("#lstRoteiros", roteirizacao.workspace).val('');
		$(".rotasGrid", roteirizacao.workspace).clear();
		$(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
		$(".cotasRotaGrid", roteirizacao.workspace).clear();
		$('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
		$('#spanDadosRota', roteirizacao.workspace).html('<strong>Rota Selecionada:</strong>&nbsp;&nbsp;&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
	
	},
	
	limparTelaRoteiro:function(){
		$('#boxInclusaoRoteiro', roteirizacao.workspace).val('')
		$('#nomeInclusaoRoteiro', roteirizacao.workspace).val('');
		$("#tipoRoteiro", roteirizacao.workspace).attr("checked",false);
	
	},
	popupTransferirCota : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$('#lstRotaTranferencia', roteirizacao.workspace).val('');
		$("#dialog-transfere-cotas", roteirizacao.workspace ).dialog({
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
					$("#effect", roteirizacao.workspace).hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					roteirizacao.escondeRotaNovaTranferencia();
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-transfere-cotas", this.workspace).parents("form")
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
					'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
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
		var descricao = $('#lstRotaTranferencia', roteirizacao.workspace).val();
		descricao = $.trim(descricao);
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/transferirRoteirizacao",
				 {
					'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
					'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
					'rotaNome' : descricao
					
				 },
				function(result) { 
					  $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
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
		var descricao = $('#lstRotaTranferencia', roteirizacao.workspace).val();
		descricao = $.trim(descricao);
			$.postJSON(
				contextPath + "/cadastro/roteirizacao/transferirRoteirizacaoComNovaRota",
				 {
					'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
					'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
					'rotaNome' : descricao,
					'ordem' : $('#ordemRotaTranferencia', roteirizacao.workspace).val()
					
					
				 },
				function(result) { 
					  $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
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
					  $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
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
	
		$( "#dialog-excluir-cotas", roteirizacao.workspace ).dialog({
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
			},
			form: $("#dialog-excluir-cotas", this.workspace).parents("form")
		});	
		      
	},
	
	buscaRoteirizacaoSelecionadas : function(){
		 roteirizacaoSelecionadas = new Array();
		$("input[type=checkbox][name='roteirizacaoCheckbox']:checked", roteirizacao.workspace).each(function(){
			roteirizacaoSelecionadas.push(parseInt($(this).val()));
		});
		
		return roteirizacaoSelecionadas;
	},
	
	buscarPvsPorEndereco : function() {
		pesquisaPorCota = false;
		$('#cotaDisponivelPesquisa', roteirizacao.workspace).html('');
		var municipio =  $('#comboMunicipio', roteirizacao.workspace).val() ;
		
		if ( municipio != "" ){
			municipio = $('#comboMunicipio option:selected', roteirizacao.workspace).text();	
		}
		
		
		
		
	    $(".cotasDisponiveisGrid", roteirizacao.workspace).flexOptions({
				'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorEndereco',
				params : [{
					name : 'CEP',
					value : $('#cepPesquisa', roteirizacao.workspace).val()
				},
				{
					name : 'uf',
					value : $('#comboUf', roteirizacao.workspace).val()
				},
				{
					name : 'municipio',
					value :municipio
				},
				{
					name : 'bairro',
					value : $('#comboBairro', roteirizacao.workspace).val()
				},
				{
					name : "rotaId",
					value :	$('#rotaSelecionada', roteirizacao.workspace).val()
				},
				
				{
					name : "roteiroId",
					value :	$('#idRoteiroSelecionado', roteirizacao.workspace).val()
				}
				],
				newp:1
			});
		  
		  $(".cotasDisponiveisGrid", roteirizacao.workspace).flexReload();
},

 checarTodasCotasGrid : function() {
	
	 if ( $("#selecionaTodos", roteirizacao.workspace).is(":checked") ) {
			$("input[type=checkbox][name='pdvCheckbox']", roteirizacao.workspace).each(function(){
				$(this).attr('checked', true);
			});
			var elem = $("#textoCheckAllCotas", roteirizacao.workspace);
			elem.innerHTML = "Desmarcar todos";
	 } else {
			$("input[type=checkbox][name='pdvCheckbox']", roteirizacao.workspace).each(function(){
				$(this).attr('checked', false);
			});
			var elem = document.getElementById("textoCheckAllCotas", roteirizacao.workspace);
			elem.innerHTML = "Marcar todos";
	 }
 } ,

carregarComboRoteiro : function () {
	roteirizacao.resetComboRoteiroPesquisa();
	roteirizacao.resetComboRotaPesquisa();
	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiro',
			{ 
				'boxId' :$('#boxPesquisa', roteirizacao.workspace).val()
			}
	        ,
			function(result) {
	    			$.each(result, function(index, row){
	    				$('#roteiroPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
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
				'roteiroId' :$('#roteiroPesquisa', roteirizacao.workspace).val()
			}
	        ,
			function(result) {
	    		$.each(result, function(index, row){
    				$('#rotaPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRota+'</option>');
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
	    				$('#roteiroPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
	    				}
	    			);
	    	},
			null,
			true
		);

},

resetComboRoteiroPesquisa : function(){
	$('#roteiroPesquisa > option', roteirizacao.workspace).remove();
	$('#roteiroPesquisa', roteirizacao.workspace).append('<option value="" >Selecione...</option>');
},
resetComboRotaPesquisa : function(){
	$('#rotaPesquisa > option', roteirizacao.workspace).remove();
	$('#rotaPesquisa', roteirizacao.workspace).append('<option value="" >Selecione...</option>');
},

pesquisaComRoteiroEspecial : function() {
	
	 if ($("#tipoRoteiroPesquisa", roteirizacao.workspace).is(":checked") ) {
		 roteirizacao.resetComboRoteiroPesquisa();
		 roteirizacao.resetComboRotaPesquisa();
		 $('#boxPesquisa', roteirizacao.workspace).attr("disabled", "disabled");
		 $('#boxPesquisa', roteirizacao.workspace).val("");
		 roteirizacao.carregarComboRoteiroEspecial();
		  
	 } else {
		 roteirizacao.resetComboRoteiroPesquisa();
		 roteirizacao.resetComboRotaPesquisa();
		 $('#boxPesquisa', roteirizacao.workspace).val("");
		 $('#roteiroPesquisa', roteirizacao.workspace).removeAttr("disabled");
	 }
} ,
roteiroEspecial : function() {
	
	 if ($("#tipoRoteiroTranferencia", roteirizacao.workspace).is(":checked") ) {
		 $('#boxRoteiroTranferencia', roteirizacao.workspace).attr("disabled", "disabled");
		 $('#boxRoteiroTranferencia', roteirizacao.workspace).val("");
		  
	 } else {
		 $('#boxRoteiroTranferencia', roteirizacao.workspace).val("");
		 $('#boxRoteiroTranferencia', roteirizacao.workspace).removeAttr("disabled");
	 }
} ,

roteiroEspecialNovo : function() {
	
	 if ($("#tipoRoteiro", roteirizacao.workspace).is(":checked") ) {
		 $('#boxInclusaoRoteiro', roteirizacao.workspace).attr("disabled", "disabled");
		 $('#boxInclusaoRoteiro', roteirizacao.workspace).val("");
		  
	 } else {
		 $('#boxInclusaoRoteiro', roteirizacao.workspace).val("");
		 $('#boxInclusaoRoteiro', roteirizacao.workspace).removeAttr("disabled");
	 }
} ,


iniciarPesquisaRoteirizacaoGrid : function () {
	
	$(".gridWrapper", this.workspace).empty();
	
	$(".gridWrapper", this.workspace).append($("<table>").attr("class", "rotaRoteirosGrid"));
	
	var rotaPesquisa 	= $('#rotaPesquisa', roteirizacao.workspace).val();
	
	var cotaPesquisa 	= $('#cotaPesquisa', roteirizacao.workspace).val();
	
	var indGridPorRotaOuCota = (rotaPesquisa != "" || cotaPesquisa != "");
	
	if(indGridPorRotaOuCota) {
		
		$(".rotaRoteirosGrid", roteirizacao.workspace).flexigrid({
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
				width : 78,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 78,
				sortable : true,
				align : 'center'
			}],
			sortname : "nomeBox",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
			
		});
		
	} else {
		
		$(".rotaRoteirosGrid", roteirizacao.workspace).flexigrid({
			preProcess: roteirizacao.callBackPesquisaRoteirizacaoGridCotasSumarizadas,
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
				display : 'Qtd. Cotas',
				name : 'qntCotas',
				width : 78,
				sortable : true,
				align : 'left',
			}],
			sortname : "nomeBox",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
			
		});
		
	}
		
},

	callBackPesquisaRoteirizacaoGrid: function (data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids", roteirizacao.workspace).hide();

			roteirizacao.esconderBotoesExportacao();
			
			return data;
		}
		
		var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 

		
		$.each(data.rows, function(index, value) {
			
			var idBox 		= value.cell.idBox;
			var idCota 		= value.cell.idCota;
			var idRota 		= value.cell.idRota;
			var idRoteiro 	= value.cell.idRoteiro;
			
			var parametros = idBox + ',' + idCota + ',' + idRota + ',' + idRoteiro;
			
			value.cell.acao =  '<a href="javascript:;" onclick="roteirizacao.detalharRotaRoteiro(' + parametros + ');">' + imgEdicao + '</a>';
			
			
		});
		
		$(".grids", roteirizacao.workspace).show();
		
		roteirizacao.mostrarBotoesExportacao();
				
		return data;
	},

	callBackPesquisaRoteirizacaoGridCotasSumarizadas: function (data) {
				
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids", roteirizacao.workspace).hide();

			roteirizacao.esconderBotoesExportacao();
			
			return data;
		}

		
		$.each(data.rows, function(index, value) {
			
			var qntCotas = value.cell.qntCotas;

			var idBox 		= value.cell.idBox;
			var idRota 		= value.cell.idRota;
			var idRoteiro 	= value.cell.idRoteiro;

			var title = value.cell.descricaoRota + ' - ' + value.cell.descricaoRoteiro;			
			
			value.cell.qntCotas =  '<a href="javascript:;" ' + 
				'onclick="roteirizacao.detalharRotaRoteiroCotasSumarizadas(\''+title+'\','+idBox+','+idRota+','+idRoteiro+');">' + qntCotas + '</a>';
		});
		
		$(".grids", roteirizacao.workspace).show();
		
		roteirizacao.mostrarBotoesExportacao();
		
		return data;
	},

	
	detalharRotaRoteiro : function(idBox, idCota, idRota, idRoteiro) {
		
		//TODO: implementar js
		
		alert('Detalhando rota roteiro');
		
	},
	
	detalharRotaRoteiroCotasSumarizadas : function(title, idBox, idRota, idRoteiro) {
		
		var data = [];
		
		data.push({name:'idBox',		value: idBox });
		data.push({name:'idRota',		value: idRota });
		data.push({name:'idRoteiro',	value: idRoteiro });
		
		$("#cotasGrid", this.workspace).flexOptions({ params:data });		
		$("#cotasGrid", this.workspace).flexReload();
		
		roteirizacao.popupDetalhesCota(title, idBox, idRoteiro, idRota);		
	},
	
	pesquisarRoteirizacao: function () {
		
		roteirizacao.iniciarPesquisaRoteirizacaoGrid();
					
		$(".rotaRoteirosGrid", roteirizacao.workspace).flexOptions({
				url : contextPath + '/cadastro/roteirizacao/pesquisarRoteirizacao',
				params : [{
					name : "boxId",
					value : $('#boxPesquisa', roteirizacao.workspace).val()
				}, {
					name : "roteiroId",
					value : $('#roteiroPesquisa', roteirizacao.workspace).val()
				}, {
					name : "rotaId",
					value : $('#rotaPesquisa', roteirizacao.workspace).val()
				},
				{
					name : "numeroCota",
					value : $('#cotaPesquisa', roteirizacao.workspace).val()
				}],
				
				newp:1
		});
			
		$(".rotaRoteirosGrid", roteirizacao.workspace).flexReload();
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
	
	popupRoteirizacao: function() {
		roteirizacao.reiniciaTelaRoteirizacao();
	    $( "#dialog-roteirizacao", roteirizacao.workspace ).dialog({
				resizable: false,
				height:510,
				width:940,
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
				},
				form: $("#dialog-roteirizacao", this.workspace).parents("form")
			});
		},
		
	habilitaBotao: function(idBotao, funcao){
		$('#'+idBotao, roteirizacao.workspace).css("cursor","");
		$('#'+idBotao, roteirizacao.workspace).css("opacity","1");
		$('#'+idBotao, roteirizacao.workspace).css("filter","alpha(opaity=100)");
		$('#'+idBotao, roteirizacao.workspace).click(funcao);
		
	},
	desabilitaBotao: function(idBotao){
		$('#'+idBotao, roteirizacao.workspace).css("cursor","default");
		$('#'+idBotao, roteirizacao.workspace).css("opacity","0.4");
		$('#'+idBotao, roteirizacao.workspace).css("filter","alpha(opaity=40)");
		$('#'+idBotao, roteirizacao.workspace).unbind('click');
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
			 roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro', roteirizacao.workspace);
				roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao', roteirizacao.workspace);
			 
		 } else {
			 roteirizacao.habilitaBotao('botaoTransferenciaRoteiro', function(){roteirizacao.popupTransferirCota()});
			 roteirizacao.habilitaBotao('botaoExcluirRoteirizacao',function(){roteirizacao.popupExcluirRoteirizacao()});
		 }
	} ,
	
	reiniciaTelaRoteirizacao :function(){
		$(".rotasGrid", roteirizacao.workspace).clear();
		$(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
		$('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
		roteirizacao.iniciaRotasGrid();
		roteirizacao.iniciaCotasDisponiveisGrid();
		roteirizacao.iniciaCotasRotaGrid();
		roteirizacao.desabilitaBotao('botaoTransfereciaRota', roteirizacao.workspace);
		roteirizacao.desabilitaBotao('botaoExcluirRota', roteirizacao.workspace);
		roteirizacao.desabilitaBotao('botaoNovaRota', roteirizacao.workspace);
		roteirizacao.desabilitaBotao('botaoCotaAusentes', roteirizacao.workspace);
		roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro', roteirizacao.workspace);
		roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao', roteirizacao.workspace);
		$('botaoNovaRotaNome', roteirizacao.workspace).val('');
		roteirizacao.desabilitaBotao('botaoNovaRotaNome', roteirizacao.workspace);
		$('#nomeRota', roteirizacao.workspace).attr("readonly","true");
		$('#lstRoteiros', roteirizacao.workspace).val('');
		roteirizacao.escondeRotaNovaTranferencia();
		
		
	},
	
	abrirTelaRotaComNome :function(){
		$('#nomeRotaInclusao', roteirizacao.workspace).val($('botaoNovaRotaNome', roteirizacao.workspace).val());
		roteirizacao.abrirTelaRota();
		
		
	},
	 exibiRotaNovaTranferencia : function (){
			$('.rotaNovaTransferencia', roteirizacao.workspace).show();
			transferirRoteirizacaoComNovaRota = true;	
	 },
	 escondeRotaNovaTranferencia : function (){
			$('.rotaNovaTransferencia', roteirizacao.workspace).hide();
			transferirRoteirizacaoComNovaRota = false;	
	  },
	  
	  atualizaOrdenacaoAsc : function(roteirizacaoId, ordem, idPontoVenda){
		  $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
					{
			  				'roteirizacaoId' :roteirizacaoId,
			  				'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
			  				'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
			  				'ordem':ordem,
			  				'pontoVendaId':idPontoVenda,
			  				'ordenacao' : 'ASC'
					} ,
					function(result) {
						 $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
			    	},
					null,
					true
				);
	  },
	  
	  atualizaOrdenacaoDesc : function(roteirizacaoId, ordem, idPontoVenda){
		  $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
					{
			  				'roteirizacaoId' :roteirizacaoId,
			  				'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
			  				'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
			  				'ordem':ordem,
			  				'pontoVendaId':idPontoVenda,
			  				'ordenacao' : 'DESC'
					} ,
					function(result) {
						 $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
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
							  $('#'+campoExibicao, roteirizacao.workspace).html(result.pessoa.nome);
							  if (callBack){
								  callBack();
							  }
						  }
						  
						
						result = true;	
			    	},
					null,
					true
				);
			
			return  result;

		},
		
		exportar : function(fileType) {
			 
			 //TODO:
			 var tipoRoteiro = "NORMAL";
			 
			 if ($("#tipoRoteiroTranferencia", roteirizacao.workspace).is(":checked") ) {
				 tipoRoteiro = "ESPECIAL";
			 }
			 
			window.location = 
				contextPath + 
				"/cadastro/roteirizacao/exportar?fileType=" + fileType;

			return false;
		},
		
		mostrarBotoesExportacao : function() {
			
			$("#botoesExportacao").show();
		},
		
		esconderBotoesExportacao : function() {
			
			$("#botoesExportacao", roteirizacao.workspace).hide();
		},
				
		init : function() {
			
			$("#cotasGrid",roteirizacao.workspace).flexigrid({
				autoload : false,
				url : contextPath + '/cadastro/roteirizacao/obterCotasSumarizadas',
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 250,
					sortable : true,
					align : 'left'
				}],
				sortname : "numeroCota",
				width : 380,
				height : 140
			});
		}
	  
		
}, BaseController);

$(function() {
	roteirizacao.init();
});

//@ sourceURL=meuScriptRoteirizacao.js
