var bancoController = $.extend(true, {
	init : function() {

			$(".bancosGrid", this.workspace).flexigrid({
				preProcess: this.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Banco',
					name : 'banco',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 148,
					sortable : true,
					align : 'left'
				}, {
					display : 'Agência / Dígito',
					name : 'agencia',
					width : 90,
					sortable : true,
					align : 'left',
				}, {
					display : 'Conta-Corrente / Dígito',
					name : 'contaCorrente',
					width : 130,
					sortable : true,
					align : 'left'
				}, {
					display : 'Cedente',
					name : 'cedente',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Apelido',
					name : 'apelido',
					width : 80,
					sortable : true,
					align : 'left'
				},{
					display : 'Carteira',
					name : 'carteira',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Status',
					name : 'status',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : false,
					align : 'center'
				}],
				sortname : "Nome",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto'
			});
		
			$("#numero", this.workspace).numeric();	
			$("#cedente", this.workspace).numeric();	
			
			$("#newNumero", this.workspace).numeric();	
			$("#newCodigoCedente", this.workspace).numeric();	
			$("#newAgencia", this.workspace).numeric();	
			$("#newConta", this.workspace).numeric();	
			$("#newDigito", this.workspace).numeric();
			$("#newCarteira", this.workspace).numeric();
			$("#newJuros", this.workspace).numeric();	
			$("#newMulta", this.workspace).numeric();
			$("#newVrMulta", this.workspace).numeric();
			
			$("#alterNumero", this.workspace).numeric();	
			$("#alterCodigoCedente", this.workspace).numeric();	
			$("#alterAgencia", this.workspace).numeric();	
			$("#alterConta", this.workspace).numeric();	
			$("#alterDigito", this.workspace).numeric();	
			$("#alterJuros", this.workspace).numeric();	
			$("#alterMulta", this.workspace).numeric();
			$("#alterVrMulta", this.workspace).numeric();

		},
		
	    popup : function() {
		
	    	bancoController.limparTelaCadastroBanco();
			
			$( "#dialog-novo", this.workspace).dialog({
				resizable: false,
				height:310,
				width:655,
				modal: true,
				buttons: {
					"Confirmar": function() {
						bancoController.novoBanco();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				},
				form: $("#dialog-novo", this.workspace).parents("form")
			});
		},
		
		popup_alterar : function() {
		
			$( "#dialog-alterar", this.workspace).dialog({
				resizable: false,
				height:310,
				width:655,
				modal: true,
				buttons: {
					"Confirmar": function() {
						bancoController.alterarBanco();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
				form: $("#dialog-alterar", this.workspace).parents("form")
			});	
			      
		},
		
		popup_excluir : function(idBanco) {
		
			$( "#dialog-excluir", this.workspace ).dialog({
				resizable: false,
				height:170,
				width:380,
				modal: true,

				buttons:[ 
				          {
					           id:"bt_confirmar",
					           text:"Confirmar", 
					           click: function() {
					        	   bancoController.desativarBanco(idBanco);
					           }
				           },
				           {
					           id:"bt_cancelar",
					           text:"Cancelar", 
					           click: function() {
					        	   $( this ).dialog( "close" );
					           }
				           }
		        ],
		        
				beforeClose: function() {
					clearMessageDialogTimeout();
			    },
				form: $("#dialog-excluir", this.workspace).parents("form")
				
			});
		},
		
	    mostrarGridConsulta : function() {
	    	
	    	$("#ativo", this.workspace).val(0);
	    	if (document.formularioFiltro.ativo.checked){
	    		$("#ativo", this.workspace).val(1);
			}
	    	
			/*PASSAGEM DE PARAMETROS*/
			$(".bancosGrid", this.workspace).flexOptions({
				/*METODO QUE RECEBERA OS PARAMETROS*/
				url: contextPath + "/banco/consultaBancos",
				params: [
				         {name:'nome', value:$("#nome", this.workspace).val()},
				         {name:'numero', value:$("#numero", this.workspace).val()},
				         {name:'cedente', value:$("#cedente", this.workspace).val()},
				         {name:'ativo', value:$("#ativo", this.workspace).val() }
				        ] ,
				        newp: 1
			});
			
			/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
			$(".bancosGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
		},
		
		getDataFromResult : function(resultado) {
			
			//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", this.workspace).hide();
				return resultado.tableModel;
			}
			
			var dadosPesquisa;
			$.each(resultado, function(index, value) {
				  if(value[0] == "TblModelBancos") {
					  dadosPesquisa = value[1];
				  }
		    });

			$.each(dadosPesquisa.rows, 
					function(index, row) {

						 var linkEditar = '<a href="javascript:;" onclick="bancoController.editarBanco(' + row.cell[0] + ');" style="margin-right:10px; ">' +
	                                      '<img src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" title="Altera banco" />' +
		                                  '</a>';			
					
				         var linkExcluir =    '<a href="javascript:;" onclick="bancoController.popup_excluir(' + row.cell[0] + ');" style="cursor:pointer">' +
				                              '<img src="'+ contextPath + '/images/ico_excluir.gif" border="0px" title="Exclui banco" />' +
							                  '</a>';		 					 
										
					     row.cell[9] = linkEditar + linkExcluir;

			         }
			);
			
			return dadosPesquisa;
		},
		
		fecharDialogs : function() {
			$( "#dialog-novo", this.workspace ).dialog( "close" );
		    $( "#dialog-alterar", this.workspace ).dialog( "close" );
		    $( "#dialog-excluir", this.workspace ).dialog( "close" );
		},
		
	    novoBanco : function() {
			var param ={
					 numero     : $("#newNumero", this.workspace).val(),
					 nome       : $("#newNome", this.workspace).val(),
					 codigoCedente    : $("#newCodigoCedente", this.workspace).val(),
					 agencia    : $("#newAgencia", this.workspace).val(),
					 conta      : $("#newConta", this.workspace).val(),
					 digito     : $("#newDigito", this.workspace).val(),
					 apelido    : $("#newApelido", this.workspace).val(),
					 carteira   : $("#newCarteira", this.workspace).val(),
					 juros      : $("#newJuros", this.workspace).val(),
					 ativo      : $("#newAtivo", this.workspace).is(':checked'),
					 multa      : $("#newMulta", this.workspace).val(),
					 vrMulta    : $("#newVrMulta", this.workspace).val(),
					 instrucoes : $("#newInstrucoes", this.workspace).val()
			
			};

			$.postJSON(contextPath + "/banco/novoBanco",param,
					   function(result) {
						   bancoController.fecharDialogs();
						   var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   bancoController.mostrarGridConsulta();
		               },
					   null,
					   true);
		},
		
		alterarBanco : function() {
			
			var param = {idBanco    : $("#idBanco", this.workspace).val(),
			    	 numero     : $("#alterNumero", this.workspace).val(),
					 nome       : $("#alterNome", this.workspace).val(),
					 codigoCedente    : $("#alterCodigoCedente", this.workspace).val(),
					 agencia    : $("#alterAgencia", this.workspace).val(),
					 conta      : $("#alterConta", this.workspace).val(),
					 digito     : $("#alterDigito", this.workspace).val(),
					 apelido    : $("#alterApelido", this.workspace).val(),
					 carteira   : $("#alterCarteira", this.workspace).val(),
					 juros      : $("#alterJuros", this.workspace).val(),					
					 ativo      : $("#alterAtivo", this.workspace).is(':checked'),					
					 multa      : $("#alterMulta", this.workspace).val(),
					 vrMulta    : $("#alterVrMulta", this.workspace).val(),
					 instrucoes : $("#alterInstrucoes", this.workspace).val()};

			$.postJSON(contextPath + "/banco/alteraBanco", param,
					   function(result) {
						   bancoController.fecharDialogs();
					       var tipoMensagem = result.tipoMensagem;
						   var listaMensagens = result.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   bancoController.mostrarGridConsulta();
				       },
					   null,
					   true);
		},
		
		editarBanco : function(idBanco){
			var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath + "/banco/buscaBanco",
					   data,
					   bancoController.sucessCallbackCadastroBanco, 
					   bancoController.fecharDialogs);
		},
		
		sucessCallbackCadastroBanco : function(resultado) {
			
			$("#idBanco", this.workspace).val(resultado.idBanco);
			$("#alterNumero", this.workspace).val(resultado.numero);
			$("#alterNome", this.workspace).val(resultado.nome);
			$("#alterCodigoCedente", this.workspace).val(resultado.codigoCedente);
			$("#alterAgencia", this.workspace).val(resultado.agencia);
			$("#alterConta", this.workspace).val(resultado.conta);
			$("#alterDigito", this.workspace).val(resultado.digito);
			$("#alterApelido", this.workspace).val(resultado.apelido);
			$("#alterCarteira", this.workspace).val(resultado.carteira);
			$("#alterJuros", this.workspace).val(resultado.juros);
			
			$("#alterAtivo", this.workspace).val(resultado.ativo);
			document.formularioAlteraBanco.alterAtivo.checked = resultado.ativo;
			
			$("#alterMulta", this.workspace).val(resultado.multa);
			$("#alterVrMulta", this.workspace).val(resultado.vrMulta);
			$("#alterInstrucoes", this.workspace).val(resultado.instrucoes);
			
			bancoController.popup_alterar();
		},
	    desativarBanco : function(idBanco) {
	    	var data = [{name: 'idBanco', value: idBanco}];
			$.postJSON(contextPath + "/banco/desativaBanco",
					   data,
					   function(result) {
						   bancoController.fecharDialogs();
						   bancoController.mostrarGridConsulta();
				       },
				       function(result) {
				    	   bancoController.fecharDialogs();
						   bancoController.mostrarGridConsulta();
				       });
		},
		
	    limparTelaCadastroBanco : function() {
			$("#newNumero", this.workspace).val("");
			$("#newNome", this.workspace).val("");
			$("#newCodigoCedente", this.workspace).val("");
			$("#newAgencia", this.workspace).val("");
			$("#newConta", this.workspace).val("");
			$("#newDigito", this.workspace).val("");
			$("#newApelido", this.workspace).val("");
			$("#newCarteira", this.workspace).val("");
			$("#newJuros", this.workspace).val("");
			$("#newAtivo", this.workspace).val("");
			$("#newMulta", this.workspace).val("");
			$("#newVrMulta", this.workspace).val("");
			$("#newInstrucoes", this.workspace).val("");
		}, 
	    
	    limparMulta : function(){
	    	$("#newMulta", this.workspace).val("");
	    	$("#alterMulta", this.workspace).val("");
	    },
	    
	    limparVrMulta : function(){
	    	$("#newVrMulta", this.workspace).val("");
	    	$("#alterVrMulta", this.workspace).val("");
	    }
	    
}, BaseController);

//@ sourceURL=scriptBancos.js
