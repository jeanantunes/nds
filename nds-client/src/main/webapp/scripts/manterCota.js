var TAB_COTA = new TabCota('tabCota');

var MANTER_COTA = {
	
    numeroCota:"",
    idCota:"",
    tipoCotaSelecionada:"",
    tipoCota_CPF:"FISICA",
    tipoCota_CNPJ:"JURIDICA",
    fecharModalCadastroCota:false,

    formDataPesquisa: function(){
		
		var formData = [ {name:"numCota",value:$("#numCota").val()},
		                 {name:"nomeCota",value:$("#descricaoPessoa").val()},
			             {name:"numeroCpfCnpj",value:$("#txtCPF_CNPJ").val()}
			            ];
		return formData;
	},
	
	 carregarDadosCadastrais:function(){
	    
		 TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
	 },
	
    carregarTelefones:function(){
    	
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarTelefone;
    	COTA.carregarTelefones();
    },
    
    carregaFinanceiroCota:function (){
    	
    	TAB_COTA.funcaoSalvar = postarParametroCobranca;
    	carregaFinanceiro(MANTER_COTA.idCota);
    },
    
    carregarEnderecos: function(){
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarEndereco;
    	ENDERECO_COTA.popularGridEnderecos();
    },
    
    carregarGarantias:function(){
    	
    },
    
    carregarDescontos:function(){
    	
    	TAB_COTA.funcaoSalvar = COTA_DESCONTO.salvarDesconto;
    	COTA_DESCONTO.carregarDescontoCota();
    },
    
    carregarDistribuicao:function(){
    	
    	TAB_COTA.funcaoSalvar = DISTRIB_COTA.salvar;
    	DISTRIB_COTA.carregarDadosDistribuicaoCota(MANTER_COTA.idCota);
    },
    
    carregarDadosSocio:function(){
    	
    	TAB_COTA.funcaoSalvar = SOCIO_COTA.salvarSocios;
    	SOCIO_COTA.carregarSociosCota();
    },
    
	carregarPDV : function (){
		
		PDV.idCota = MANTER_COTA.idCota;
		PDV.pesquisarPdvs(MANTER_COTA.idCota);
	},
	
	carregarFornecedores:function(){
		
		TAB_COTA.funcaoSalvar = COTA_FORNECEDOR.salvarFornecedores;
		COTA_FORNECEDOR.carregarFornecedores();
	},
	
	limparFormsTabs: function () {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();
		
		$.postJSON(contextPath + "/cadastro/cota/cancelar",
				null, 
				null
		);
	},
	
	executarPreProcessamento:function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grids").hide();

			return resultado.tableModel;
		}
		
		// Monta as colunas com os inputs do grid
		$.each(resultado.rows, function(index, row) {
			
			var paramCota =  "'" +row.cell.numero +"'," + "'"+ row.cell.idCota + "'" ;
			
			var linkEdicao = '<a href="javascript:;" onclick="MANTER_COTA.editar('+ paramCota +');" style="cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Cota" />' +
				 '</a>';			
			 
			var linkExclusao ='<a href="javascript:;" onclick="MANTER_COTA.exibirDialogExclusao('+ row.cell.idCota +' );" style="cursor:pointer">' +
                 '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Cota" />' +
                  '</a>';		 					 
			
             row.cell.acao = linkEdicao + linkExclusao; 
		});
		
		$("#grids").show();
		
		return resultado;
	},
	
	pesquisar:function(){
		
		$(".pessoasGrid").flexOptions({
			url: contextPath + "/cadastro/cota/pesquisarCotas",
			params: MANTER_COTA.formDataPesquisa(),newp: 1
		});
		
		$(".pessoasGrid").flexReload();
	},
	
	exibirDialogExclusao:function (idCota){
		
		$("#dialog-excluirCota" ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					MANTER_COTA.excluir(idCota);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	editar:function(numeroCota,idCota){
		
		MANTER_COTA.numeroCota = numeroCota;
		MANTER_COTA.idCota = idCota;
		
		MANTER_COTA.fecharModalCadastroCota = false;
		
		$.postJSON(contextPath + "/cadastro/cota/editar",
				"idCota="+idCota, 
				function(result){
				
					if(result){
						
						MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
						
						if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){
							COTA_CPF.editarCPF();
						}
						else {
							COTA_CNPJ.editarCNPJ(result);
						}
					}
			}
		);
		
	},
	
	excluir:function(idCota){
		
		$.postJSON(contextPath + "/cadastro/cota/excluir",
				"idCota="+idCota, 
				function(){
					MANTER_COTA.pesquisar();
				}
		);
	},
	
	montarCombo:function(result,idCombo){
	
		var comboClassificacao =  montarComboBox(result, false);
		
		$(idCombo).html(comboClassificacao);
	},
	
	
	
	salvarDadosCadastrais:function(){
		
		if(MANTER_COTA.tipoCotaSelecionada == MANTER_COTA.tipoCota_CNPJ){
			
			COTA_CNPJ.salvarDadosBasico();
		}
		else {
			
		}
	},
	
	salvarEndereco:function(){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarEnderecos",
				"idCota="+ MANTER_COTA.idCota, 
				null,
				null,
				true
		);
		
	},
	
	salvarTelefone:function(){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarTelefones",
				"idCota="+ MANTER_COTA.idCota, 
				null,
				null,
				true
		);
	},
	
	
	
	validarEmail : function (idInput)	{
		er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
		
		if($(idInput).val().length == 0){
			return;
		}
		
		if(!er.exec($(idInput).val())) {
			$(idInput).focus();
			exibirMensagemDialog("WARNING",["E-mail inv&aacutelido."],"");
		}
	},
	
	popupCota: function() {
		
		//Define a função salvar inicial ao abrir o dialog de cadastro de cota 
		TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
		
		$('input[id^="historico"]').numeric();
		
		$("#numeroCnpj").mask("99.999.999/9999-99");
		
		$('input[id^="periodoCota"]').mask("99/99/9999");
		
		$('input[id^="periodoCota"]').datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$( "#dialog-cota" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					TAB_COTA.funcaoSalvar();
				},
				"Cancelar": function() {
					MANTER_COTA.fecharModalCadastroCota = false;
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function(event, ui) {
				
				clearMessageDialogTimeout();
				clearMessageDialogTimeout();
					
				if (!MANTER_COTA.fecharModalCadastroCota){
					
					MANTER_COTA.cancelarCadastro();
					
					return MANTER_COTA.fecharModalCadastroCota;
				}
				
				MANTER_COTA.limparFormsTabs();
				
				return MANTER_COTA.fecharModalCadastroCota;
				
			}
		});
	},
	
	cancelarCadastro:function(){
		
		$("#dialog-cancelar-cadastro-cota").dialog({
			resizable: false,
			height:150,
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					MANTER_COTA.fecharModalCadastroCota = true;
					
					$("#dialog-close").dialog("close");
					$("#dialog-cancelar-cadastro-cota").dialog("close");
					$("#dialog-cota").dialog("close");
					
				},
				"Cancelar": function() {
					MANTER_COTA.fecharModalCadastroCota = false;
					$(this).dialog("close");
				}
			}
		});
	},
	
	validarCotaHistoricoBase:function(idCampoNumeroCota, idCampoPorcentagem){
		
		if($(idCampoNumeroCota).val().length > 0){
			
			$.postJSON(
					contextPath + "/cadastro/cota/validarNumeroCotaHistoricoBase",
					"&numeroCota="+ $(idCampoNumeroCota).val(), 
					null,
					function(){
						$(idCampoNumeroCota).val("");
						$(idCampoPorcentagem).val("");
						$(idCampoNumeroCota).focus();
					},	
					true
			);
		}
		
	}
};

var COTA_DESCONTO = {
		
		salvarDesconto:function(){
			
			var descontos = "";
			
			 $("#selectDesconto option").each(function (index) {
				 descontos = descontos + "descontos["+index+"]="+ $(this).val() +"&";
			 });
			
			$.postJSON(
					contextPath + "/cadastro/cota/salvarDescontos",
					descontos + 
					"&idCota="+ MANTER_COTA.idCota, 
					null,
					null,
					true
			);
		},
		
		carregarDescontoCota:function(){
			
			$.postJSON(contextPath + "/cadastro/cota/obterDescontos",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
						
						if(result){
							MANTER_COTA.montarCombo(result,"#selectTipoDesconto");
						}
					},null,true
			);
			
			$.postJSON(contextPath + "/cadastro/cota/obterDescontosSelecionados",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
					
						if(result){
							MANTER_COTA.montarCombo(result,"#selectDesconto");
						}
					},null,true
			);
		}
		
};

var COTA_FORNECEDOR = {
	
		salvarFornecedores: function(){
			
			var fornecedores ="";
			
			 $("#selectFornecedorSelecionado_option_cnpj option").each(function (index) {
				 fornecedores = fornecedores + "fornecedores["+index+"]="+ $(this).val() +"&";
			 });
			
			$.postJSON(
					contextPath + "/cadastro/cota/salvarFornecedores",
					fornecedores + 
					"idCota="+ MANTER_COTA.idCota, 
					null,
					null,
					true
			);

		},
		
		carregarFornecedores:function(){
			
			$.postJSON(contextPath + "/cadastro/cota/obterFornecedores",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
						
						if(result){
							MANTER_COTA.montarCombo(result,"#selectFornecedor_option_cnpj");
						}
					},null,true
			);
			
			$.postJSON(contextPath + "/cadastro/cota/obterFornecedoresSelecionados",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
					
						if(result){
							MANTER_COTA.montarCombo(result,"#selectFornecedorSelecionado_option_cnpj");
						}
					},null,true
			);
		}
};

var COTA_CNPJ = {	
		
	novoCNPJ:function(){
		
		$( "#tabCota" ).tabs({ selected: 0 });
		TAB_COTA.possuiDadosObrigatorios = false;
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		COTA_CNPJ.limparCampos();
		
		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCNPJ",
				null, 
				function(result){
					
					var dados = result;
					
					$("#dataInclusao").html(dados.dataInicioAtividade);
					$("#numeroCota").val(dados.numeroSugestaoCota);
					$("#status").val(dados.status);
					
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionada");
					
					MANTER_COTA.popupCota();
				}
		);
	},
	
	editarCNPJ:function(result){
		
		COTA_CNPJ.limparCampos();
		
		$( "#tabCota" ).tabs({ selected:0 });
		TAB_COTA.possuiDadosObrigatorios = true;
		
		$("#numeroCota").val(result.numeroCota);
		$("#email").val(result.email);
		$("#status").val(result.status);
		$("#dataInclusao").html(result.dataInclusao.$);
		$("#razaoSocial").val(result.razaoSocial);
		$("#nomeFantasia").val(result.nomeFantasia);
		$("#numeroCnpj").val(result.numeroCnpj);
		$("#inscricaoEstadual").val(result.inscricaoEstadual);
		$("#inscricaoMunicipal").val(result.inscricaoMunicipal);
		$("#emailNF").val(result.emailNF);
		$("#emiteNFE").attr("checked", (result.emiteNFE == true)?"checked":null);
		$("#classificacaoSelecionada").val(result.classificacaoSelecionada);
		$("#historicoPrimeiraCota").val(result.historicoPrimeiraCota);
		$("#historicoPrimeiraPorcentagem").val( eval( result.historicoPrimeiraPorcentagem));
		$("#historicoSegundaCota").val(result.historicoSegundaCota);
		$("#historicoSegundaPorcentagem").val( eval( result.historicoSegundaPorcentagem));
		$("#historicoTerceiraCota").val(result.historicoTerceiraCota);
		$("#historicoTerceiraPorcentagem").val( eval( result.historicoTerceiraPorcentagem));
		
		if(result.inicioPeriodo){
			$("#periodoCotaDe").val(result.inicioPeriodo.$);
		}
		
		if(result.fimPeriodo){
			$("#periodoCotaAte").val(result.fimPeriodo.$);
		}
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		
		MANTER_COTA.popupCota();
	},
		
	salvarDadosBasico:function (){

		var formData = $("#formDadosBasicoCnpj").serializeArray();
		
		formData.push({name:"cotaDTO.idCota",value: MANTER_COTA.idCota});
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					
					TAB_COTA.possuiDadosObrigatorios = true;
					
					exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"");

				},
				null,
				true
		);
	},
	
	limparCampos:function(){
		
		$("#numeroCota").val("");
		$("#email").val("");
		$("#status").val("");
		$("#dataInclusao").html("");
		$("#razaoSocial").val("");
		$("#nomeFantasia").val("");
		$("#numeroCnpj").val("");
		$("#inscricaoEstadual").val("");
		$("#inscricaoMunicipal").val("");
		$("#emailNF").val("");
		$("#emiteNFE").attr("checked", null);
		$("#classificacaoSelecionada").val("");
		$("#historicoPrimeiraCota").val("");
		$("#historicoPrimeiraPorcentagem").val("" );
		$("#historicoSegundaCota").val("");
		$("#historicoSegundaPorcentagem").val("");
		$("#historicoTerceiraCota").val("");
		$("#historicoTerceiraPorcentagem").val("");
		$("#periodoCotaDe").val("");
		$("#periodoCotaAte").val("");
		
		clearMessageDialogTimeout(null);
	},
	
	carregarDadosCNPJ: function(idCampo){
		
		$.postJSON(contextPath + "/cadastro/cota/obterDadosCNPJ",
				"numeroCnpj="+$(idCampo).val() , 
				function(result){

					if(result.email){$("#email").val(result.email);}
					
					if(result.razaoSocial){$("#razaoSocial").val(result.razaoSocial);}
					
					if(result.nomeFantasia){$("#nomeFantasia").val(result.nomeFantasia);}
					
					if(result.inscricaoEstadual){$("#inscricaoEstadual").val(result.inscricaoEstadual);}
					
					if(result.inscricaoMunicipal){$("#inscricaoMunicipal").val(result.inscricaoMunicipal);}

				},
				null,
				true
		);
	}
};

var COTA_CPF = {
	
	novoCPF:function(){
		
		alert("Operação em desenvolvimento!!");
		/*
		$("#tabCota" ).tabs( "option", "disabled", [9] );
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.popupCota();*/
	},

	editarCPF:function(){
	
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.popupCota();
	}
		
};

var SOCIO_COTA = {
		
		itemEdicao:null,
		rows:[],
		
		socio:function(){
			
			var socio = {
					nome:$("#idNomeSocio").val(),
					cargo:$("#idCargoSocio").val(),
					principal:($("#idSocioPrincipal").attr("checked"))?true:false,
					id:null
			};
			
			return socio;
		},
		
		salvarSocios:function(){
			
			var list =   serializeArrayToPost("sociosCota",SOCIO_COTA.obterListaSocios());			
			var objPost = concatObjects({idCota:MANTER_COTA.idCota},list);
			
			$.postJSON(contextPath + "/cadastro/cota/salvarSocioCota",
					objPost , 
					null,
					null,
					true
			);
			
			return false;
		},
		
		carregarSociosCota: function() {
			
			SOCIO_COTA.rows = [];
			
			$.postJSON(
				contextPath+'/cadastro/cota/carregarSociosCota',
				"idCota=" + MANTER_COTA.idCota,
				function(result) {
					
					$.each(result, function(index, value) {
						
						var socio = {nome:value.nome, cargo:value.cargo,principal:value.principal};
						
						SOCIO_COTA.rows.push({"id": SOCIO_COTA.rows.length,"cell":socio});
					});
					
					$(".sociosPjGrid").flexAddData({rows:SOCIO_COTA.rows,page:1,total:1}  );
					
					SOCIO_COTA.limparFormSocios();
				},
				function(result) {
					
					SOCIO_COTA.processarResultadoConsultaSocios(result);
				},
				true
			);
		},		
		
		processarResultadoConsultaSocios:function(data){
			
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					""
				);
				
				return;
			}
			
			$.each(data.rows, function(index, value) {
				
				var idSocio = value.id;
			
				var acao  = '<a href="javascript:;" onclick="SOCIO_COTA.editarSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_editar.gif" border="0" hspace="5" /></a>';
				    acao += '<a href="javascript:;" onclick="SOCIO_COTA.removerSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

				value.cell.acao = acao;
				
				
				value.cell.principalFlag =(value.cell.principal == true) 
								?'<img src="' + contextPath + '/images/ico_check.gif" border="0" hspace="5" />'
							  	:'&nbsp';
			});
			
			return data;
			
		},
		
		editarSocio:function(idSocio){
			
			var socios  = SOCIO_COTA.rows[idSocio].cell;
			
			SOCIO_COTA.itemEdicao = idSocio;
			
			$("#idNomeSocio").val(socios.nome),
			$("#idCargoSocio").val(socios.cargo),
			$("#idSocioPrincipal").attr("checked",(socios.principal == true)?"checked":null);
			
			$("#btnEditarSocio").show();
			$("#btnAddSocio").hide();
		},
		
		limparFormSocios:function(){
			
			$("#idNomeSocio").val(""),
			$("#idCargoSocio").val(""),
			$("#idSocioPrincipal").attr("checked",null);
			SOCIO_COTA.itemEdicao = null;
			
			$("#btnEditarSocio").hide();
			$("#btnAddSocio").show();
		},
		
		removerSocio:function(idSocio){
			
			$("#dialog-excluir-socio").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						SOCIO_COTA.rows.splice(idSocio, 1);
						
						var lista = new Array;
						
						for (var index in SOCIO_COTA.rows) {	
							lista.push({"id":lista.length, "cell":SOCIO_COTA.rows[index].cell});
						}
						
						SOCIO_COTA.rows = lista;
						
						$(".sociosPjGrid").flexAddData({rows:lista,page:1,total:1}  );
						
						$(this).dialog("close");
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$("#dialog-excluir-socio").show();
		},
		
		obterListaSocios:function(){
			
			var list = new Array();
			
			for (var index in SOCIO_COTA.rows) {
				var socio = SOCIO_COTA.rows[index].cell;
				socio.principalFlag=null;
				socio.acao = null;
				list.push(socio);
			}
			
			return list;
		},
		
		incluirSocio:function(){
			
			var data  = serializeObjectToPost("socioCota",  SOCIO_COTA.socio());
			var list =   serializeArrayToPost("sociosCota",SOCIO_COTA.obterListaSocios());			
			var objPost = concatObjects(data,list);					
			
			$.postJSON(contextPath + "/cadastro/cota/incluirSocioCota", 
						objPost , 
						function(result){

							if (result){
								
								var novoSocio = result;
								var rows = SOCIO_COTA.rows;
								
								if (SOCIO_COTA.itemEdicao == null || SOCIO_COTA.itemEdicao < 0) {
									rows.push({"id": rows.length,"cell":novoSocio});
								} else {
									rows.slice(SOCIO_COTA.itemEdicao, 1);
									rows[SOCIO_COTA.itemEdicao] = {"id":SOCIO_COTA.itemEdicao,"cell":novoSocio};
								}
								
								$(".sociosPjGrid").flexAddData({rows:rows,page:1,total:1} );	
							}
							
							SOCIO_COTA.limparFormSocios();
					},
					null,
					true
				);
		}
};
