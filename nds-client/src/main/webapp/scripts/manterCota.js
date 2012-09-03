var ModoTela = {
  CADASTRO_COTA : {value: 'CADASTRO_COTA'},
  HISTORICO_TITULARIDADE : {value: 'HISTORICO_TITULARIDADE'}
};

var TAB_COTA = new TabCota('tabCota');

var MANTER_COTA = $.extend(true, {

    numeroCota:"",
    idCota:"",
    tipoCotaSelecionada:"",
    tipoCota_CPF:"FISICA",
    tipoCota_CNPJ:"JURIDICA",
    fecharModalCadastroCota:false,
    isAlteracaoTitularidade: false,
    _workspace: this.workspace,
	 modoTela: null,
    idHistorico:"",
    
    init: function() {
    	this.definirModoTelaCadastroCota();

    	$( "#tabpdv", this.workspace ).tabs();

		$("#descricaoPessoa", this.workspace).autocomplete({source: ""});
		
		$("#numCota", this.workspace).numeric();
		
		COTA_FORNECEDOR.initTabFornecedorCota();	

		SOCIO_COTA.initGridSocioCota();

		this.initCotaGridPrincipal();
    },
    
    initCotaGridPrincipal: function() {

    	$(".pessoasGrid", this.workspace).flexigrid({
			preProcess: MANTER_COTA.executarPreProcessamento,
			dataType : 'json',
			colModel : [  {
				display : 'Código',
				name : 'numero',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome / Razação Social',
				name : 'nome',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'CPF/CNPJ',
				name : 'numeroCpfCnpj',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display: 'Box',
				name: 'descricaoBox',
				width: 90,
				sortable: true,
				align: 'left'
			}, {
				display : 'Contato',
				name : 'contato',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Telefone',
				name : 'telefone',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'E-Mail',
				name : 'email',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "numero",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});
    },

    formDataPesquisa: function(){

		var formData = [ {name:"numCota",value:$("#numCota", this.workspace).val()},
		                 {name:"nomeCota",value:$("#descricaoPessoa", this.workspace).val()},
			             {name:"numeroCpfCnpj",value:$("#txtCPF_CNPJ", this.workspace).val()},
			             {name:"logradouro",value:$("#logradouroPesquisa", this.workspace).val()},
			             {name:"bairro",value:$("#bairroPesquisa", this.workspace).val()},
			             {name:"municipio",value:$("#municipioPesquisa", this.workspace).val()}
			            ];
		return formData;
	},
	
	 carregarDadosCadastrais:function(){

         if (MANTER_COTA.isModoTelaCadastroCota()) {
             TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
         }

	 },
	
    carregarTelefones:function(){
        COTA.definirReadonly(!MANTER_COTA.isModoTelaCadastroCota());

        if (MANTER_COTA.isModoTelaCadastroCota()) {
    	    TAB_COTA.funcaoSalvar = MANTER_COTA.salvarTelefone;
        }
        var data = [{name:"idCota", value: MANTER_COTA.idCota },
		            {name:"idHistorico", value:MANTER_COTA.idHistorico},
		            {name:"modoTela", value:MANTER_COTA.modoTela.value}];
		$.postJSON(contextPath + "/cadastro/cota/recarregarTelefone", data, function(){
			COTA.carregarTelefones();
		},null,true,null);
    },
    
    carregaFinanceiroCota:function (){
        parametroCobrancaCotaController.definirReadonly(!MANTER_COTA.isModoTelaCadastroCota());
        if (MANTER_COTA.isModoTelaCadastroCota()) {
    	    TAB_COTA.funcaoSalvar = parametroCobrancaCotaController.postarParametroCobranca;
        }
    	parametroCobrancaCotaController.carregaFinanceiro(MANTER_COTA.idCota);
    },
    
    carregarEnderecos: function(){
        ENDERECO_COTA.definirReadonly(!MANTER_COTA.isModoTelaCadastroCota());
        if (MANTER_COTA.isModoTelaCadastroCota()) {
               TAB_COTA.funcaoSalvar = MANTER_COTA.salvarEndereco;
        }
        	
        var data = [{name:"idCota", value: MANTER_COTA.idCota },
		            {name:"idHistorico", value:MANTER_COTA.idHistorico},
		            {name:"modoTela", value:MANTER_COTA.modoTela.value}];
    	$.postJSON(contextPath + "/cadastro/cota/recarregarEndereco", data, function(){
    		ENDERECO_COTA.popularGridEnderecos();
    	},null,true,null);
    },
    
    carregarGarantias:function(){
    	
    },
    
    carregarDescontos:function(){

    	COTA_DESCONTO.initDescontos(MANTER_COTA.numeroCota);
    },
    
    carregarDistribuicao:function(){
        if (MANTER_COTA.isModoTelaCadastroCota()) {
    	    TAB_COTA.funcaoSalvar = DISTRIB_COTA.salvar;
        }
    	DISTRIB_COTA.carregarDadosDistribuicaoCota(MANTER_COTA.idCota);
    },
    
    carregarDadosSocio:function(){
        if (MANTER_COTA.isModoTelaCadastroCota()) {
    	    TAB_COTA.funcaoSalvar = SOCIO_COTA.salvarSocios;
        }
    	SOCIO_COTA.carregarSociosCota();
    },
    
	carregarPDV : function (){
        PDV.idCota = MANTER_COTA.idCota;
        PDV.idHistorico = MANTER_COTA.idHistorico;
        PDV.definirModoTela(MANTER_COTA.modoTela);
		PDV.pesquisarPdvs();
	},
	
	carregarFornecedores:function(){
        if (MANTER_COTA.isModoTelaCadastroCota()) {
		    TAB_COTA.funcaoSalvar = COTA_FORNECEDOR.salvarFornecedores;
        }
		COTA_FORNECEDOR.carregarFornecedores();
	},
	
	limparFormsTabs: function () {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();
		
		$.postJSON(contextPath + "/cadastro/cota/cancelar",null, null);
	},
	
	executarPreProcessamento:function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grids", this.workspace).hide();

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
		
		$("#grids", this.workspace).show();
		
		return resultado;
	},
	
	pesquisar:function(){
		
		$(".pessoasGrid", this.workspace).flexOptions({
			url: contextPath + "/cadastro/cota/pesquisarCotas",
			params: MANTER_COTA.formDataPesquisa(),newp: 1
		});
		
		$(".pessoasGrid", this.workspace).flexReload();
	},
	
	exibirDialogExclusao:function (idCota){
		
		$("#dialog-excluirCota", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					MANTER_COTA.excluir(idCota);
					$( this, this.workspace ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this, this.workspace ).dialog( "close" );
				}
			},
			form: $("#workspaceCota", this.workspace)			
		});
	},
	
	editar:function(numeroCota,idCota){
		
		MANTER_COTA.numeroCota = numeroCota;
		MANTER_COTA.idCota = idCota;

        MANTER_COTA.definirModoTelaCadastroCota();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " +  numeroCota);
		
		MANTER_COTA.fecharModalCadastroCota = false;
		
		$.postJSON(contextPath + "/cadastro/cota/editar",
				"idCota="+idCota, 
				function(result){
				
					if(result){
						
						if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){	
							MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionadaCPF");
							COTA_CPF.editarCPF(result);
						}
						else {
							MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
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
		
		$(idCombo, MANTER_COTA._workspace).html(comboClassificacao);
	},

	salvarDadosCadastrais:function(){
		
		if(MANTER_COTA.tipoCotaSelecionada == MANTER_COTA.tipoCota_CNPJ){
			
			COTA_CNPJ.salvarDadosBasico();
		}
		else {
			
			COTA_CPF.salvarDadosBasico();
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
				MANTER_COTA.carregarTelefones,
				null,
				true
		);
	},
	
	validarEmail : function (idInput)	{
		er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
		
		if($(idInput, this.workspace).val().length == 0){
			return;
		}
		
		if(!er.exec($(idInput, this.workspace).val())) {
			$(idInput, this.workspace).focus();
			exibirMensagemDialog("WARNING",["E-mail inv&aacutelido."],"");
		}
	},
	
	novoPopupCotaCPF: function () {
		
		MANTER_COTA.isAlteracaoTitularidade = false;
		
		COTA_CPF.novoCPF();
		
	},
	
	novoPopupCotaCNPJ: function () {
		
		MANTER_COTA.isAlteracaoTitularidade = false;
		
		COTA_CNPJ.novoCNPJ();
	},
	
	popupCota: function() {
		
		//Define a função salvar inicial ao abrir o dialog de cadastro de cota 
		TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
		
		$('input[id^="historico"]', this.workspace).numeric();
		
		$("#numeroCnpj", this.workspace).mask("99.999.999/9999-99");
		
		$("#numeroCPF", this.workspace).mask("999.999.999-99");
				
		$('input[id^="periodoCota"]', this.workspace).mask("99/99/9999");
		
		$('input[id^="dataNascimento"]', this.workspace).mask("99/99/9999");
		
		$('input[id^="periodoCota"]', this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="dataNascimento"]', this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		if (MANTER_COTA.isModoTelaCadastroCota()) {
			$( "#dialog-cota", this.workspace ).dialog({
				resizable: false,
				height:590,
				width:950,
				modal: true,
				buttons: [
			         {id:"btn_confirmar_cota",text:"Confirmar",
		        	  click: function() {
								if(TAB_COTA.funcaoSalvar)
									TAB_COTA.funcaoSalvar();
		        	  		}
			         },
		        	{id:"btn_cancelar_cota",text:"Cancelar",
			         click:function(){
		        				MANTER_COTA.fecharModalCadastroCota = false;
		        				$( this, this.workspace ).dialog( "close" );
		        		}	  
		        	}  
				],
				beforeClose: function(event, ui) {
				
					clearMessageDialogTimeout();
				
					if (!MANTER_COTA.fecharModalCadastroCota){
					
						MANTER_COTA.cancelarCadastro();
					
						return MANTER_COTA.fecharModalCadastroCota;
					}
				
					MANTER_COTA.limparFormsTabs();
				
					return MANTER_COTA.fecharModalCadastroCota;
				
				},
				form: $("#workspaceCota", this.workspace)		
			});
		} else {
			$( "#dialog-cota", this.workspace ).dialog({
				resizable: false,
				height:590,
				width:950,
				modal: true,
				buttons: [
			         {id:"btn_fechar_historico_titularidade_cota", text:"Fechar",
		        	  click: function() {
                          $(this).dialog("close");
                          MANTER_COTA.definirModoTelaCadastroCota();
                          MANTER_COTA.editar(MANTER_COTA.numeroCota, MANTER_COTA.idCota);
		        	  	}
			         }],
				form: $("#workspaceCota", this.workspace)
			});
		}
	},
	
	cancelarCadastro:function(){
		
		$("#dialog-cancelar-cadastro-cota", this.workspace).dialog({
			resizable: false,
			height:150,
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					MANTER_COTA.fecharModalCadastroCota = true;
					
					$("#dialog-close", this.workspace).dialog("close");
					$("#dialog-cancelar-cadastro-cota", this.workspace).dialog("close");
					$("#dialog-cota", this.workspace).dialog("close");
					
				},
				"Cancelar": function() {
					MANTER_COTA.fecharModalCadastroCota = false;
					$(this, this.workspace).dialog("close");
				}
			},
			form: $("#workspaceCota", this.workspace)			
		});
	},
	
	validarCotaHistoricoBase:function(idCampoNumeroCota, idCampoPorcentagem){
		
		if($(idCampoNumeroCota, this.workspace).val().length > 0){
			
			$.postJSON(
					contextPath + "/cadastro/cota/validarNumeroCotaHistoricoBase",
					"&numeroCota="+ $(idCampoNumeroCota, this.workspace).val(), 
					null,
					function(){
						$(idCampoNumeroCota, this.workspace).focus();
						$(idCampoNumeroCota, this.workspace).val("");
						$(idCampoPorcentagem, this.workspace).val("");
					},	
					true
			);
		}
		else {
			$(idCampoPorcentagem, this.workspace).val("");
		}	
	},
	
	mudarNomeModalCadastro:function(value){
		
		$("#ui-dialog-title-dialog-cota", this.workspace).html(value);
	},
	
	pesquisarLogradouros: function(idCampoPesquisa) {
		
		var nomeLogra = $(idCampoPesquisa, this.workspace).val();
		
		nomeLogra = $.trim(nomeLogra);
		
		$(idCampoPesquisa, this.workspace).autocomplete({source: ""});
		
		if (nomeLogra && nomeLogra.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/endereco/pesquisarLogradouros", "nomeLogradouro=" + nomeLogra,
				function(result) { 
					MANTER_COTA.exibirAutoComplete(result, idCampoPesquisa); 
				}
			);
		}
	},
	
	pesquisarBairros: function(idCampoPesquisa) {
		
		var nomeBairro = $(idCampoPesquisa, this.workspace).val();
		
		nomeBairro = $.trim(nomeBairro);
		
		$(idCampoPesquisa, this.workspace).autocomplete({source: ""});
		
		if (nomeBairro && nomeBairro.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/endereco/pesquisarBairros", "nomeBairro=" + nomeBairro,
				function(result) { 
					MANTER_COTA.exibirAutoComplete(result, idCampoPesquisa); 
				}
			);
		}
	},
	
	pesquisarMunicipios: function(idCampoPesquisa) {
		
		var nomeMunicipio = $(idCampoPesquisa, this.workspace).val();
		
		nomeMunicipio = $.trim(nomeMunicipio);
		
		$(idCampoPesquisa, this.workspace).autocomplete({source: ""});
		
		if (nomeMunicipio && nomeMunicipio.length > 2) {
			
			$.postJSON(
				contextPath + "/cadastro/endereco/pesquisarLocalidades", "nomeLocalidade=" + nomeMunicipio,
				function(result) { 
					MANTER_COTA.exibirAutoComplete(result, idCampoPesquisa); 
				}
			);
		}
	},
	
	exibirAutoComplete: function(result, idCampo) {
		
		$(idCampo, this.workspace).autocomplete({
			source: result,
			minLength: 4,
			delay : 0
        });
	}, 
	
	popupAlterarTitular : function() {
		$( "#dialog-titular", this.workspace ).dialog({
			resizable: false,
			height:150,
			width:230,
			modal: true,
			form: $("#workspaceCota", MANTER_COTA._workspace)
		});
	},
	
	alterarTitular: function(isPessoaFisica) {

		MANTER_COTA.isAlteracaoTitularidade = true;

		var idCota = MANTER_COTA.idCota;
		var campoNumeroCota;
		
		var numeroCota = $("#numeroCotaCPF", this.workspace).val() ?
								$("#numeroCotaCPF", this.workspace).val() :
								$("#numeroCota", this.workspace).val();

		if (isPessoaFisica) {

			campoNumeroCota = $("#numeroCotaCPF", this.workspace);

			COTA_CPF.novoCPF();

		} else {

			campoNumeroCota = $("#numeroCota", this.workspace);

			COTA_CNPJ.novoCNPJ();
		}
		
		MANTER_COTA.idCota = idCota;

		campoNumeroCota.attr("disabled", "disabled");
		campoNumeroCota.val(numeroCota);
		
		MANTER_COTA.numeroCota = numeroCota;

		$( "#dialog-titular", this.workspace ).dialog("close");
	},
	
	visualizarHistoricoTitularidade : function(idHistorico) {
		MANTER_COTA.idHistorico = idHistorico;

		this.fecharDialogExibicaoHistoricoTitularidade();
		this.definirModoTelaHistoricoTitularidade();
		
		var data = [{name:"idCota", value: MANTER_COTA.idCota },
		            {name:"idHistorico", value:MANTER_COTA.idHistorico}];
		$.postJSON(contextPath + "/cadastro/cota/historicoTitularidade", data, 
				function(result){
					if(result){
						if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){	
							COTA_CPF.editarCPF(result);
						}
						else {
							COTA_CNPJ.editarCNPJ(result);
						}
					}
			}
		);
	}, 
	
	definirModoTelaCadastroCota : function() {
		this.definirModoTela(ModoTela.CADASTRO_COTA);
	},
	
	definirModoTelaHistoricoTitularidade : function() {
		this.definirModoTela(ModoTela.HISTORICO_TITULARIDADE);
	},
	
	definirModoTela : function(modoTela) {
		this.modoTela = modoTela;
		this.atualizarEstadoTela();
	},
	
	isModoTelaCadastroCota : function() {
		return this.modoTela == ModoTela.CADASTRO_COTA;
	},
	
	fecharDialogExibicaoHistoricoTitularidade : function() {
		this.fecharModalCadastroCota = true;
		$("#dialog-cota", this._workspace).dialog("close");
	},
	
	atualizarEstadoTela : function() {
		if (this.isModoTelaCadastroCota()) {
			this.mudarNomeModalCadastro("Cota - " +  MANTER_COTA.idCota);
			$("#dialog-cota", this._workspace).find(':input(:disabled)').prop('disabled', false);
            $("#dialog-cota", this._workspace).find('input[name="cotaDTO.status"]').prop('disabled', true);
			$("#dialog-cota", this._workspace).find('.antigosProp').show();
			$("#dialog-cota", this._workspace).find('.ui-datepicker-trigger').show();	
			$("#dialog-cota", this._workspace).find('span[id^="btnAlterarTitularidade"]').show();
            $("#dialog-cota", this._workspace).find('.classPesquisar').show();
		} else {
			this.mudarNomeModalCadastro("Histórico Titularidade Cota - " +  MANTER_COTA.idCota);
			$("#dialog-cota", this._workspace).find(':input:not(:disabled)').prop('disabled', true);
			$("#dialog-cota", this._workspace).find('.antigosProp').hide();	
			$("#dialog-cota", this._workspace).find('.ui-datepicker-trigger').hide();	
			$("#dialog-cota", this._workspace).find('span[id^="btnAlterarTitularidade"]').hide();
            $("#dialog-cota", this._workspace).find('.classPesquisar').hide();
		}
	}

}, BaseController);

var COTA_DESCONTO = $.extend(true,
    {
	    initDescontos : function(numCota){
	    	COTA_DESCONTO.initDescontoCota();
	    	COTA_DESCONTO.initDescontoProduto();
	    	COTA_DESCONTO.obterDescontoCota(numCota); 
	    	COTA_DESCONTO.obterDescontoProduto(numCota);
	    	
	    },
	
	    initDescontoProduto : function(){
		    $(".descProdutosGrid", this.workspace).flexigrid({
		    	preProcess: COTA_DESCONTO.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 350,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'nomeProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : '% Desconto',
					name : 'desconto',
					width : 115,
					sortable : true,
					align : 'right'
				}, {
					display : 'Data da Alteração',
					name : 'dataAlteracao',
					width : 120,
					sortable : true,
					align : 'center'
				}],
				sortname : "dataAlteracao",
				sortorder : "asc",
				width : 810,
				height : 150
			});
	    },	
		
	    initDescontoCota : function(){
			$(".descCotaGrid", this.workspace).flexigrid({
				preProcess: COTA_DESCONTO.getDataFromResult,
				dataType : 'json',
				colModel : [ {
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 440,
					sortable : true,
					align : 'left'
				}, {
					display : '% Desconto',
					name : 'desconto',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Tipo',
					name : 'descTipoDesconto',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Última Atualização',
					name : 'dataAlteracao',
					width : 100,
					sortable : true,
					align : 'center'
				}],
				sortname : "dataAlteracao",
				sortorder : "asc",
				width : 810,
				height : 150
			});
	    },
	    
        obterDescontoProduto : function(numCota){
        	
        	$(".descProdutosGrid", this.workspace).flexOptions({
				url: contextPath+'/cadastro/cota/obterTiposDescontoProduto',
				params: [
				         {name:'numCota', value:numCota}
				        ] ,
				        newp: 1
			});
			
			$(".descProdutosGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
	    },
	    
	    obterDescontoCota : function(numCota){
	    	
			$(".descCotaGrid", this.workspace).flexOptions({
				url: contextPath+'/cadastro/cota/obterTiposDescontoCota',
				params: [
				         {name:'numCota', value:numCota}
				        ] ,
				        newp: 1
			});
			
			$(".descCotaGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
	    },

	    
        getDataFromResult : function(resultado){
			
			if (resultado.mensagens) {
				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids", this.workspace).hide();
				return resultado;
			}	
				
			$(".grids", this.workspace).show();
			
			return resultado;
		}
 
    }
	,
	BaseController
);

var COTA_FORNECEDOR = $.extend(true, {
	
		salvarFornecedores: function(){
			
			var fornecedores ="";
			
			 $("#selectFornecedorSelecionado_option_cnpj option", this.workspace).each(function (index) {
				 fornecedores = fornecedores + "fornecedores["+index+"]="+ $(this, this.workspace).val() +"&";
			 });
			
			$.postJSON(
					contextPath + "/cadastro/cota/salvarFornecedores",
					fornecedores + 
					"idCota="+ MANTER_COTA.idCota, 
					null,
					function(mensagens){
						
						COTA_FORNECEDOR.carregarFornecedores();
					},
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
		},

		initTabFornecedorCota: function() {
			
			var idHidden = $("#telaCotaidFornecedorHidden", this.workspace).val();
			
			$("select[name='selectFornecedorSelecionado_"+ idHidden +"']", this.workspace).multiSelect(
				"select[name='selectFornecedor_"+ idHidden +"']", 
				{trigger: "#linkFornecedorVoltarTodos_"+ idHidden}
			);
			
			$("select[name='selectFornecedor_"+ idHidden +"']", this.workspace).multiSelect(
				"select[name='selectFornecedorSelecionado_"+ idHidden +"']", 
				{trigger: "#linkFornecedorEnviarTodos_"+ idHidden}
			);
		}
}, BaseController);

var COTA_CNPJ = $.extend(true, {	
	
	gridAntigosProprietarios : new GridAntigosProprietarios(".antigosProprietariosGridCNPJ", this.workspace),
	
	tratarExibicaoDadosCadastrais:function(){
		
		$("#dadosCNPJ", this.workspace).show();
		$("#dadosCPF", this.workspace).hide();
		$("#idTabSocio", this.workspace).parent().show();
		$( "#tabCota", this.workspace ).tabs({ selected: 0 });
	},	
		
	novoCNPJ:function(){
		
		COTA_CNPJ.tratarExibicaoDadosCadastrais();
		
		TAB_COTA.possuiDadosObrigatorios = false;
		MANTER_COTA.fecharModalCadastroCota = false;
		MANTER_COTA.mudarNomeModalCadastro("Nova Cota");
		
		COTA_CNPJ.gridAntigosProprietarios.init();
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		COTA_CNPJ.limparCampos();
		
		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCNPJ",
				null, 
				function(result){
					
					var dados = result;
					
					$("#dataInclusao", this.workspace).html(dados.dataInicioAtividade);
					$("#status", this.workspace).val(dados.status);
					
					if (!MANTER_COTA.isAlteracaoTitularidade) {
					
						$("#numeroCota", this.workspace).val(dados.numeroSugestaoCota);
					}
					
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionada");
					
					MANTER_COTA.popupCota();
				}
		);
	},
	
	editarCNPJ:function(result){
		
		COTA_CNPJ.tratarExibicaoDadosCadastrais();
		
		COTA_CNPJ.carregarDadosCadastraisCnpj(result);
		
		COTA_CNPJ.gridAntigosProprietarios.init(result);	
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
			
		MANTER_COTA.popupCota();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
	},
	
	carregarDadosCadastraisCnpj:function(result){
		
		COTA_CNPJ.limparCampos();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
		
		$( "#tabCota", this.workspace ).tabs({ selected:0 });
		TAB_COTA.possuiDadosObrigatorios = true;
		
		$("#numeroCota", this.workspace).val(result.numeroCota);
		$("#email", this.workspace).val(result.email);
		$("#status", this.workspace).val(result.status);
		$("#dataInclusao", this.workspace).html(result.dataInclusao.$);
		$("#razaoSocial", this.workspace).val(result.razaoSocial);
		$("#nomeFantasia", this.workspace).val(result.nomeFantasia);
		$("#numeroCnpj", this.workspace).val(result.numeroCnpj);
		$("#inscricaoEstadual", this.workspace).val(result.inscricaoEstadual);
		$("#inscricaoMunicipal", this.workspace).val(result.inscricaoMunicipal);
		$("#emailNF", this.workspace).val(result.emailNF);
		$("#emiteNFE", this.workspace).attr("checked", (result.emiteNFE == true)?"checked":null);
		$("#classificacaoSelecionada", this.workspace).val(result.classificacaoSelecionada);
		$("#historicoPrimeiraCota", this.workspace).val(result.historicoPrimeiraCota);
		$("#historicoPrimeiraPorcentagem", this.workspace).val( eval( result.historicoPrimeiraPorcentagem));
		$("#historicoSegundaCota", this.workspace).val(result.historicoSegundaCota);
		$("#historicoSegundaPorcentagem", this.workspace).val( eval( result.historicoSegundaPorcentagem));
		$("#historicoTerceiraCota", this.workspace).val(result.historicoTerceiraCota);
		$("#historicoTerceiraPorcentagem", this.workspace).val( eval( result.historicoTerceiraPorcentagem));
		
		if(result.inicioPeriodo){
			$("#periodoCotaDe", this.workspace).val(result.inicioPeriodo.$);
		}
		
		if(result.fimPeriodo){
			$("#periodoCotaAte", this.workspace).val(result.fimPeriodo.$);
		}
		
		if (result.status == "ATIVO") {

			var linkTitularidade = $("#btnAlterarTitularidadeCNPJ", this.workspace).find("a");
			
			linkTitularidade.css("opacity", 1); 
			
			linkTitularidade.click(function() {
				
				MANTER_COTA.popupAlterarTitular();
			});
		}
	},
		
	salvarDadosBasico:function (){

		var formData = $("#formDadosBasicoCnpj", this.workspace).serializeArray();
		
		formData.push({name:"cotaDTO.idCota", value: MANTER_COTA.idCota});
		formData.push({name:"cotaDTO.alteracaoTitularidade", value: MANTER_COTA.isAlteracaoTitularidade});
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					
					TAB_COTA.possuiDadosObrigatorios = true;
					
					COTA_CNPJ.carregarDadosCadastraisCnpj(result);
					
					exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"");

				},
				null,
				true
		);
	},
	
	limparCampos:function(){
		
		$("#numeroCota", this.workspace).val("");
		$("#email", this.workspace).val("");
		$("#status", this.workspace).val("");
		$("#dataInclusao", this.workspace).html("");
		$("#razaoSocial", this.workspace).val("");
		$("#nomeFantasia", this.workspace).val("");
		$("#numeroCnpj", this.workspace).val("");
		$("#inscricaoEstadual", this.workspace).val("");
		$("#inscricaoMunicipal", this.workspace).val("");
		$("#emailNF", this.workspace).val("");
		$("#emiteNFE", this.workspace).attr("checked", null);
		$("#classificacaoSelecionada", this.workspace).val("");
		$("#historicoPrimeiraCota", this.workspace).val("");
		$("#historicoPrimeiraPorcentagem", this.workspace).val("" );
		$("#historicoSegundaCota", this.workspace).val("");
		$("#historicoSegundaPorcentagem", this.workspace).val("");
		$("#historicoTerceiraCota", this.workspace).val("");
		$("#historicoTerceiraPorcentagem", this.workspace).val("");
		$("#periodoCotaDe", this.workspace).val("");
		$("#periodoCotaAte", this.workspace).val("");
		
		clearMessageDialogTimeout(null);
	},
	
	carregarDadosCNPJ: function(idCampo){
		
		$.postJSON(contextPath + "/cadastro/cota/obterDadosCNPJ",
				"numeroCnpj="+$(idCampo, this.workspace).val() , 
				function(result){

					if (result.email){$("#email", this.workspace).val(result.email);}
					if (result.razaoSocial){$("#razaoSocial", this.workspace).val(result.razaoSocial);}
					if (result.nomeFantasia){$("#nomeFantasia", this.workspace).val(result.nomeFantasia);}
					if (result.inscricaoEstadual){$("#inscricaoEstadual", this.workspace).val(result.inscricaoEstadual);}
					if (result.inscricaoMunicipal){$("#inscricaoMunicipal", this.workspace).val(result.inscricaoMunicipal);}
				},
				null,
				true
		);
	}
}, BaseController);

var COTA_CPF = $.extend(true, {
	
	gridAntigosProprietarios : new GridAntigosProprietarios(".antigosProprietariosGridCPF", this.workspace),
	
	tratarExibicaoDadosCadastrais:function(){
		
		$("#dadosCPF", this.workspace).show();
		$("#dadosCNPJ", this.workspace).hide();
		$("#idTabSocio", this.workspace).parent().hide();
		$( "#tabCota", this.workspace ).tabs({ selected: 0 });
	},		
		
	novoCPF:function(){
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		COTA_CPF.tratarExibicaoDadosCadastrais();
		COTA_CPF.gridAntigosProprietarios.init();
		COTA_CPF.limparCampos();
		
		TAB_COTA.possuiDadosObrigatorios = false;
		MANTER_COTA.fecharModalCadastroCota = false;
		
		MANTER_COTA.mudarNomeModalCadastro("Nova Cota");
		
		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCPF",
				null, 
				function(result){
					
					var dados = result;

					$("#dataInclusaoCPF", this.workspace).html(dados.dataInicioAtividade);
					$("#statusCPF", this.workspace).val(dados.status);

					if (!MANTER_COTA.isAlteracaoTitularidade) {
					
						$("#numeroCotaCPF", this.workspace).val(dados.numeroSugestaoCota);
					}
										
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionadaCPF");
					
					MANTER_COTA.popupCota();
				}
		);
	},

	editarCPF:function(result){
		
		
		COTA_CPF.tratarExibicaoDadosCadastrais();
		
		COTA_CPF.carregarDadosCpf(result);
		
		COTA_CPF.gridAntigosProprietarios.init(result);		
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		
		MANTER_COTA.popupCota();

        if (MANTER_COTA.isModoTelaCadastroCota()) {
            MANTER_COTA.mudarNomeModalCadastro("Cota - " +  MANTER_COTA.idCota);
        } else {
            MANTER_COTA.mudarNomeModalCadastro("Histórico Titularidade Cota - " +  MANTER_COTA.idCota);
        }
		
	},
	
	carregarDadosCpf:function(result){
		
		COTA_CPF.limparCampos();

		$( "#tabCota", this.workspace ).tabs({ selected:0 });
		TAB_COTA.possuiDadosObrigatorios = true;
		
		$("#numeroCotaCPF", this.workspace).val(result.numeroCota);
		$("#emailCPF", this.workspace).val(result.email);
		$("#statusCPF", this.workspace).val(result.status);
		$("#dataInclusaoCPF", this.workspace).html(result.dataInclusao.$);
		$("#nomePessoaCPF", this.workspace).val(result.nomePessoa);
		$("#numeroCPF", this.workspace).val(result.numeroCPF);
		$("#numeroRG", this.workspace).val(result.numeroRG);
		$("#orgaoEmissor", this.workspace).val(result.orgaoEmissor);
		$("#estadoSelecionado", this.workspace).val(result.estadoSelecionado);
		$("#estadoCivilSelecionado", this.workspace).val(result.estadoCivilSelecionado);
		$("#sexoSelecionado", this.workspace).val(result.sexoSelecionado);
		$("#nacionalidade", this.workspace).val(result.nacionalidade);
		$("#natural", this.workspace).val(result.natural);
		$("#emailNFCPF", this.workspace).val(result.emailNF);
		$("#emiteNFECPF", this.workspace).attr("checked", (result.emiteNFE == true)?"checked":null);
		$("#classificacaoSelecionadaCPF", this.workspace).val(result.classificacaoSelecionada);
		$("#historicoPrimeiraCotaCPF", this.workspace).val(result.historicoPrimeiraCota);
		$("#historicoPrimeiraPorcentagemCPF", this.workspace).val( eval( result.historicoPrimeiraPorcentagem));
		$("#historicoSegundaCotaCPF", this.workspace).val(result.historicoSegundaCota);
		$("#historicoSegundaPorcentagemCPF", this.workspace).val( eval( result.historicoSegundaPorcentagem));
		$("#historicoTerceiraCotaCPF", this.workspace).val(result.historicoTerceiraCota);
		$("#historicoTerceiraPorcentagemCPF", this.workspace).val( eval( result.historicoTerceiraPorcentagem));
		
		if(result.dataNascimento){
			$("#dataNascimento", this.workspace).val(result.dataNascimento.$);
		}
		
		if(result.inicioPeriodo){
			$("#periodoCotaDeCPF", this.workspace).val(result.inicioPeriodo.$);
		}
		
		if(result.fimPeriodo){
			$("#periodoCotaAteCPF", this.workspace).val(result.fimPeriodo.$);
		}
		
		if (result.status == "ATIVO") {
			
			var linkTitularidade = $("#btnAlterarTitularidadeCPF", this.workspace).find("a");
			
			linkTitularidade.css("opacity", 1); 
			
			linkTitularidade.click(function() {
				
				MANTER_COTA.popupAlterarTitular();
			});
		}
	},
	
	salvarDadosBasico:function (){

		var formData = $("#formDadosBasicoCpf", this.workspace).serializeArray();
		
		formData.push({name:"cotaDTO.idCota",value: MANTER_COTA.idCota});
		formData.push({name:"cotaDTO.alteracaoTitularidade", value: MANTER_COTA.isAlteracaoTitularidade});

		if (MANTER_COTA.isAlteracaoTitularidade) {
			
			formData.push({name:"cotaDTO.numeroCota", value: MANTER_COTA.numeroCota});
		}
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCPF",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					
					TAB_COTA.possuiDadosObrigatorios = true;
					
					COTA_CPF.carregarDadosCpf(result);
					
					exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"");

				},
				null,
				true
		);
		
	},
	carregarDadosCPF: function(idCampo){
		
		$.postJSON(contextPath + "/cadastro/cota/obterDadosCPF",
				"numeroCPF="+$(idCampo, this.workspace).val() , 
				function(result){
					
					if(result.email)$("#emailCPF", this.workspace).val(result.email);
					if(result.nomePessoa)$("#nomePessoaCPF", this.workspace).val(result.nomePessoa);
					if(result.numeroRG)$("#numeroRG", this.workspace).val(result.numeroRG);
					if(result.dataNascimento)$("#dataNascimento", this.workspace).val(result.dataNascimento.$);
					if(result.orgaoEmissor)$("#orgaoEmissor", this.workspace).val(result.orgaoEmissor);
					if(result.estadoSelecionado)$("#estadoSelecionado", this.workspace).val(result.estadoSelecionado);
					if(result.estadoCivilSelecionado)$("#estadoCivilSelecionado", this.workspace).val(result.estadoCivilSelecionado);
					if(result.sexoSelecionado)$("#sexoSelecionado", this.workspace).val(result.sexoSelecionado);
					if(result.nacionalidade)$("#nacionalidade", this.workspace).val(result.nacionalidade);
					if(result.natural)$("#natural", this.workspace).val(result.natural);
				},
				null,
				true
		);
	},
	
	limparCampos:function(){
		
		$("#numeroCotaCPF", this.workspace).val("");
		$("#emailCPF", this.workspace).val("");
		$("#statusCPF", this.workspace).val("");
		$("#dataInclusaoCPF", this.workspace).html("");
		$("#nomePessoaCPF", this.workspace).val("");
		$("#numeroCPF", this.workspace).val("");
		$("#numeroRG", this.workspace).val("");
		$("#dataNascimento", this.workspace).val("");
		$("#orgaoEmissor", this.workspace).val("");
		$("#estadoSelecionado", this.workspace).val("");
		$("#estadoCivilSelecionado", this.workspace).val("");
		$("#sexoSelecionado", this.workspace).val("");
		$("#nacionalidade", this.workspace).val("");
		$("#natural", this.workspace).val("");
		$("#emailNFCPF", this.workspace).val("");
		$("#emiteNFECPF", this.workspace).attr("checked", null);
		$("#classificacaoSelecionadaCPF", this.workspace).val("");
		$("#historicoPrimeiraCotaCPF", this.workspace).val("");
		$("#historicoPrimeiraPorcentagemCPF", this.workspace).val("");
		$("#historicoSegundaCotaCPF", this.workspace).val("");
		$("#historicoSegundaPorcentagemCPF", this.workspace).val("");
		$("#historicoTerceiraCotaCPF", this.workspace).val("");
		$("#historicoTerceiraPorcentagemCPF", this.workspace).val("");
		$("#periodoCotaDeCPF", this.workspace).val("");
		$("#periodoCotaAteCPF", this.workspace).val("");
		
		clearMessageDialogTimeout(null);
	}
	
}, BaseController);

var SOCIO_COTA = $.extend(true, {

		itemEdicao:null,
		rows:[],
		_workspace: this.workspace,
		enderecoSocio: new Endereco("", "dialog-socio"),
		
		socio:function(){
			
			var socio = {
					nome:$("#idNomeSocio", this.workspace).val(),
					cargo:$("#idCargoSocio", this.workspace).val(),
					principal:($("#idSocioPrincipal", this.workspace).attr("checked"))?true:false,
					id:($("#idSocio", this.workspace).val())
			};
			
			return socio;
		},
		
		inicializarPopupSocio: function() {
			
			SOCIO_COTA.popup_novo_socio();
			
			SOCIO_COTA.bindButtonActions();
			
			SOCIO_COTA.enderecoSocio.preencherComboUF();

			SOCIO_COTA.limparCamposSocio();
			
			$("#cep", this.workspace).mask("99999-999");
		},
		
		popup_novo_socio: function() {
			
			$( "#dialog-socio", SOCIO_COTA._workspace ).dialog({
				resizable: false,
				height:340,
				width:760,
				modal: true,
				buttons: {
					"Confirmar": function() {

						SOCIO_COTA.incluirSocio();
					},
					"Cancelar": function() {
						$( this, SOCIO_COTA._workspace ).dialog( "close" );
					}
				},
				form: $("#workspaceCota", this.workspace)
			});
		},
		
		bindButtonActions: function() {

			$("#btnPesquisarEndereco", SOCIO_COTA._workspace).click(function() {
				
				SOCIO_COTA.enderecoSocio.pesquisarEnderecoPorCep();
			});
			
			$("#cidade", SOCIO_COTA._workspace).keyup(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarLocalidades();
			});
			
			$("#cidade", SOCIO_COTA._workspace).blur(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarLocalidades(true);
			});
			
			$("#bairro", SOCIO_COTA._workspace).keyup(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarBairros();
			});
			
			$("#bairro", SOCIO_COTA._workspace).blur(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarBairros(true);
			});

			$("#logradouro", SOCIO_COTA._workspace).keyup(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarLogradouros();
			});
			
			$("#logradouro", SOCIO_COTA._workspace).blur(function() {
				
				SOCIO_COTA.enderecoSocio.autoCompletarLogradouros(true);
			});
		},
		
		initGridSocioCota: function() {
		
			$(".sociosPjGrid", this.workspace).flexigrid({
				dataType : 'json',
				preProcess:SOCIO_COTA.processarResultadoConsultaSocios,
				colModel : [{
					display : 'Nome',
					name : 'nome',
					width : 120,
					sortable : false,
					align : 'left'
				},{
					display : 'Cargo',
					name : 'cargo',
					width : 100,
					sortable : false,
					align : 'left'
				}, {
					display : 'Endereco',
					name : 'endereco',
					width : 340,
					sortable : false,
					align : 'left'
				},{
					display : 'Telefone',
					name : 'telefone',
					width : 115,
					sortable : false,
					align : 'left'
				},{
					display : 'Principal',
					name : 'principalFlag',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
					sortable : false,
					align : 'center'
				}],
				width : 880,
				height : 180
			});
		},
		
		limparCamposSocio: function() {

			$("#dialog-socio", SOCIO_COTA._workspace).find(":input, select").each(function () {
		        switch (this.type) {
		            case "text":
		                $(this, SOCIO_COTA._workspace).val("");
		                break;
		            case "hidden":
		                $(this, SOCIO_COTA._workspace).val("");
		                break;
		            case "checkbox":
		            	$(this, SOCIO_COTA._workspace).removeAttr("checked");
		            	break;
		            case "select":
		            	$(this, SOCIO_COTA._workspace).val("");
		            	break;
		        }
		    });
		},
		
		salvarSocios:function(){
			
			$.postJSON(contextPath + "/cadastro/cota/confirmarSocioCota",
					"idCota=" + MANTER_COTA.idCota, 
					function(mensagens) {

						if (mensagens) {
							
							exibirMensagemDialog(
						
								mensagens.tipoMensagem, 
								mensagens.listaMensagens,
								"dialog-cota"
							);
						}
					},
					function(result) {
						
						if (result.mensagens) {
						
							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens,
								"dialog-cota"
							);
						}
					},
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
						
						var socio = {
								id:value.id,
								nome:value.nome, 
								cargo:value.cargo,
								principal:value.principal,
								endereco:value.endereco.tipoLogradouro + 
										 " " + 
										 value.endereco.logradouro +
										 ", " + 
										 value.endereco.numero + 
										 " - " + 
										 value.endereco.cidade +
										 "/" + 
										 value.endereco.uf +
										 " " +
										 value.endereco.cep,			
								telefone:value.telefone.ddd + 
										 " " +
										 value.telefone.numero};
						
						SOCIO_COTA.rows.push({"id": value.id,"cell":socio});
					});
					
					$(".sociosPjGrid", this.workspace).flexAddData({rows:SOCIO_COTA.rows,page:1,total:1}  );
					
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
			
			SOCIO_COTA.itemEdicao = idSocio;
			
			$.postJSON(
				contextPath+'/cadastro/cota/carregarSocioPorId',
				"idSocioCota=" + idSocio,
				function(result) {

					SOCIO_COTA.enderecoSocio.preencherComboUF(result.endereco.uf);
					
					$("#nomeSocio", this.workspace).val(result.nome),
					$("#cargoSocio", this.workspace).val(result.cargo),
					$("#checkboxSocioPrincipal", this.workspace).attr("checked",(result.principal == true)?"checked":null);
					$("#idSocioCota", this.workspace).val(idSocio);	

					$("#idTelefone", this.workspace).val(result.telefone.id);
					$("#ddd", this.workspace).val(result.telefone.ddd);
					$("#numeroTelefone", this.workspace).val(result.telefone.numero);
					
					$("#idEndereco", this.workspace).val(result.endereco.id);
					$("#uf", this.workspace).val(result.endereco.uf);
					$("#cep", this.workspace).val(result.endereco.cep);
					$("#cidade", this.workspace).val(result.endereco.cidade);
					$("#bairro", this.workspace).val(result.endereco.bairro);
					$("#complemento", this.workspace).val(result.endereco.complemento);
					$("#tipoLogradouro", this.workspace).val(result.endereco.tipoLogradouro);
					$("#logradouro", this.workspace).val(result.endereco.logradouro);
					$("#numero", this.workspace).val(result.endereco.numero);
					
					SOCIO_COTA.popup_novo_socio();
				},
				null,
				true
			);
		},
		
		limparFormSocios:function(){
			
			$("#idNomeSocio", this.workspace).val(""),
			$("#idCargoSocio", this.workspace).val(""),
			$("#idSocioPrincipal", this.workspace).attr("checked",null);
			$("#idSocio", this.workspace).val("");
			SOCIO_COTA.itemEdicao = null;
			
			$("#btnEditarSocio", this.workspace).hide();
			$("#btnAddSocio", this.workspace).show();
		},
		
		removerSocio:function(idSocio) {
			
			$("#dialog-excluir-socio", SOCIO_COTA._workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$.postJSON(
							contextPath + "/cadastro/cota/removerSocioCota",
							[{name:'idSocioCota', value:idSocio}],
							function(mensagens) {

								$("#dialog-excluir-socio", SOCIO_COTA._workspace).dialog("close");

								SOCIO_COTA.carregarSociosCota();
								
								if (mensagens) {
									
									exibirMensagemDialog(
								
										mensagens.tipoMensagem, 
										mensagens.listaMensagens,
										"dialog-cota"
									);
								}
							},
							function(result) {
								
								if (result.mensagens) {
								
									$("#dialog-excluir-socio", SOCIO_COTA._workspace).dialog("close");
								
									exibirMensagemDialog(
										result.mensagens.tipoMensagem, 
										result.mensagens.listaMensagens,
										"dialog-cota"
									);
								}
							},
							true
						);
					},
					"Cancelar": function() {
						$(this, SOCIO_COTA._workspace).dialog("close");
					}
				},
				form: $("#workspaceCota", this.workspace)				
			});
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

			var data = $("#dialog-socio", SOCIO_COTA._workspace).find("select, input").serializeArray();

			data.push({name:'idCota', value:MANTER_COTA.idCota});

			$.postJSON(
					contextPath + "/cadastro/cota/incluirSocioCota",
					data,
					function(mensagens) {

						SOCIO_COTA.carregarSociosCota();

						if (mensagens) {

							exibirMensagemDialog(
								mensagens.tipoMensagem, 
								mensagens.listaMensagens,
								"dialog-cota"
							);
						}
						
						$( "#dialog-socio", SOCIO_COTA._workspace ).dialog( "close" );
					},
					function(result) {
						
						if (result.mensagens) {

							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens,
								"dialog-socio"
							);
						}
					},
					true
				);
		}
}, BaseController);

function GridAntigosProprietarios(element, workspace) {
	
	var _element = element;
	var _workspace = workspace;
	
	this.init =  function(data) { 
			$(_element, _workspace).flexigrid({
			dataType : 'json',
			preProcess: function(data) {
				if (data.rows) {
					$.each(data.rows, function(index, row) {
						row.cell.periodo = row.cell.inicio.$ + ' a ' + row.cell.fim.$;
						var acao = '<a href="javascript:;" onclick="MANTER_COTA.visualizarHistoricoTitularidade('+ row.cell.id +');" style="cursor:pointer">';
						acao += '<img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>';
						row.cell.acao = acao;
					});
					return data;
				}
			},
			colModel : [{
				display : 'Período',
				name : 'periodo',
				width : 120,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'CPF',
				name : 'documento',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 400,
			height : 110
		});
		if (data) {
			$(_element, _workspace).flexAddData({
				rows:  toFlexiGridObject(data.proprietarios), 
				page:1, 
				total:data.proprietarios.length
			});
		} else {
			$(_element, _workspace).flexAddData({
				rows:  toFlexiGridObject([]), 
				page:1, 
				total:0
			});
		}
	};
}

//@ sourceURL=scriptManterCota.js
