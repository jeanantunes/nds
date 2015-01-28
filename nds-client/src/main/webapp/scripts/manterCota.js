var ModoTela = {
    CADASTRO_COTA : {value: 'CADASTRO_COTA'},
    HISTORICO_TITULARIDADE : {value: 'HISTORICO_TITULARIDADE'}
};


var TAB_COTA = new TabCota('tabCota');

var autoCompl;

var MANTER_COTA = $.extend(true, {

    numeroCota:"",
    idCota:"",
    tipoCota:"",
    tipoCotaSelecionada:"",
    tipoCota_CPF:"FISICA",
    tipoCota_CNPJ:"JURIDICA",
	confirmado:false,
    fecharModalCadastroCota:false,
    isAlteracaoTitularidade: false,
    _workspace: this.workspace,
    modoTela: null,
    idHistorico:"",
    resultCotaLength:0,
    
    init: function() {
    	
    	autoCompl = new AutoCompleteCampos(MANTER_COTA.workspace);
    	
        this.definirModoTelaCadastroCota();

        $( "#tabpdv", this.workspace ).tabs();

        $("#descricaoPessoa", this.workspace).autocomplete({source: ""});

        $("#numCota", this.workspace).numeric();
        
        $('#logradouroPesquisa').keyup(function (){
        	autoCompl.autoCompletarPorNomeSimples("/cadastro/endereco/pesquisarLogradouros", '#logradouroPesquisa', "nomeLogradouro", 1);
    	});
        
        $('#bairroPesquisa').keyup(function (){
        	autoCompl.autoCompletarPorNomeSimples("/cadastro/endereco/pesquisarBairros", '#bairroPesquisa', "nomeBairro", 1);
    	});
        
        $('#municipioPesquisa').keyup(function (){
        	autoCompl.autoCompletarPorNomeSimples("/cadastro/endereco/pesquisarLocalidades", '#municipioPesquisa', "nomeLocalidade", 1);
    	});

        COTA_FORNECEDOR.initTabFornecedorCota();

        SOCIO_COTA.initGridSocioCota();

        this.initCotaGridPrincipal();
        
        
        $(document).ready(function(){
        	
        	focusSelectRefField($("#numCota"));
        	
        	$(document.body).keydown(function(e) {
        		
        		if(keyEventEnterAux(e)){
        			MANTER_COTA.pesquisar();
        		}
        		
        		return true;
        	});
        });
    },
    
    verificaTipoCota : function(tipoCotaCPF) {
    	
    	if (MANTER_COTA.idCota == "") {
    		return;
    	}
    	
    	$("<div>")
    	.html("O Mix (para o tipo alternativo) e a Fixação (para o tipo convencional) desta Cota serão apagados, confirma?")
    	.dialog({
    		title: "Confirmação",
            resizable: false,
            height:'auto',
            width:350,
            modal: true,
            buttons: {
                "Confirmar": function() {
                    $( this, this.workspace ).dialog( "close" );
					$.postJSON(contextPath + "/cadastro/cota/apagarTipoCota", {idCota:MANTER_COTA.idCota, tipoCota:tipoCotaCPF.value});
                },
                "Cancelar": function() {
                    $( this, this.workspace ).dialog( "close" );
                }
            },
        });
    },
    
    initCotaGridPrincipal: function() {

        $(".pessoasGrid", MANTER_COTA.workspace).flexigrid({
            preProcess: MANTER_COTA.executarPreProcessamento,
            dataType : 'json',
            colModel : [  {
                display : 'Código',
                name : 'numero',
                width : 60,
                sortable : true,
                align : 'left'
            },{
                display : 'Nome / Razão Social',
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
            
            {name:"status",value:$("#selectStatus", this.workspace).val()},
            
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
    	
    	 MANTER_COTA._indCadastroCotaAlterado = false;
    	
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
        parametroCobrancaCotaController.definirModoTela(MANTER_COTA.modoTela, MANTER_COTA.idHistorico);
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

        DISTRIB_COTA.definirModoTela(MANTER_COTA.modoTela, MANTER_COTA.idHistorico);
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

            $("#gridsCota", this.workspace).hide();

            return resultado.tableModel;
        }

        // Monta as colunas com os inputs do grid
        $.each(resultado.rows, function(index, row) {

            var paramCota =  "'" +row.cell.numero +"'," + "'"+ row.cell.idCota + "'" ;

            var linkEdicao = '<a href="javascript:;" onclick="MANTER_COTA.editar('+ paramCota +');" style="cursor:pointer; margin-right:10px;">' +
                '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Cota" />' +
                '</a>';

            var linkExclusao ='<a href="javascript:;" onclick="MANTER_COTA.exibirDialogExclusao('+ row.cell.idCota +' );" style="cursor:pointer">' +
                '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Cota" />' +
                '</a>';

            row.cell.acao = linkEdicao + linkExclusao; 
        });

        MANTER_COTA.resultCotaLength=resultado.rows.length;
        
        $("#gridsCota", this.workspace).show();

        return resultado;
    },

    pesquisar:function(){

        $(".pessoasGrid", MANTER_COTA.workspace).flexOptions({
            url: contextPath + "/cadastro/cota/pesquisarCotas",
            params: MANTER_COTA.formDataPesquisa(),newp: 1
        });

        $(".pessoasGrid", MANTER_COTA.workspace).flexReload();
    },

    exibirDialogExclusao:function (idCota){
    	
    	if(!verificarPermissaoAcesso(this.workspace)){
			return;
		}
    	
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

        MANTER_COTA.confirmado = false;
        
        MANTER_COTA.numeroCota = numeroCota;
        MANTER_COTA.idCota = idCota;

        MANTER_COTA.definirModoTelaCadastroCota();

        MANTER_COTA.fecharModalCadastroCota = false;

        $.postJSON(contextPath + "/cadastro/cota/editar",
            {idCota:idCota},
            function(result){

                if(result){

                    if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){
                    	
                    	$("#numeroCotaCPF", this.workspace).attr("disabled", "disabled");
                    	
                    	$("#tabCota", this.workspace).tabs( "option", "disabled", [4] );
                    	
                        MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionadaCPF");
                        
                        COTA_CPF.editarCPF(result);
                    }
                    else {
                    	
                    	$("#numeroCotaCNPJ", this.workspace).attr("disabled", "disabled");
                    	
                    	$("#tabCota", this.workspace).tabs( "option", "disabled", [4] );
                    	
                        MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
                        
                        COTA_CNPJ.editarCNPJ(result);
                    }
                }
            }
        );

    },

    excluir:function(idCota){

        $.postJSON(contextPath + "/cadastro/cota/excluir",
        		{idCota:idCota},
            function(){
                MANTER_COTA.pesquisar();
            }
        );
    },

    montarCombo:function(result,idCombo){

        var comboClassificacao =  montarComboBox(result, false);
        
        if(idCombo == "#classificacaoSelecionadaCPF" ){
        	var inicio = '<select name="cotaDTO.classificacaoSelecionada" id="classificacaoSelecionadaCPF" style="width:300px;" disabled="disabled">';
        	var fim = '</select>';
        	$("#selectClassificacaoCPF", MANTER_COTA._workspace).html(inicio + comboClassificacao + fim);
        }else if(idCombo == "#classificacaoSelecionada"){
        	var inicio = '<select name="cotaDTO.classificacaoSelecionada" id="classificacaoSelecionada" style="width:300px;" disabled="disabled">';
        	var fim = '</select>';
        	$("#manter-selectClassificacao", MANTER_COTA._workspace).html(inicio + comboClassificacao + fim);

        }else{
        	$(idCombo, MANTER_COTA._workspace).html(comboClassificacao);        	
        	
        }

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
            {idCota:MANTER_COTA.idCota},
            null,
            null,
            true
        );

    },

    salvarTelefone:function(){

        $.postJSON(
            contextPath + "/cadastro/cota/salvarTelefones",
            {idCota:MANTER_COTA.idCota},
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
            exibirMensagemDialog("WARNING",["E-mail inválido."],"");
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

    popupCota: function(novo) {

        //Define a funcao salvar inicial ao abrir o dialog de cadastro de cota
        TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
        
        MANTER_COTA.confirmado = false;

        $('input[id^="historico"]', this.workspace).numeric();

        $("#numeroCnpj", this.workspace).mask("99.999.999/9999-99");

        $("#numeroCPF", this.workspace).mask("999.999.999-99");

        $('input[id^="periodoCota"]', this.workspace).mask("99/99/9999");

        $('input[id^="dataNascimento"]', this.workspace).mask("99/99/9999");

//        $('input[id^="periodoCota"]', this.workspace).datepicker({
//            showOn: "button",
//            buttonImage: contextPath+"/images/calendar.gif",
//            buttonImageOnly: true,
//            dateFormat: "dd/mm/yy"
//        });

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
                title: novo ? "Nova Cota" : "Cota - " + MANTER_COTA.numeroCota,
                modal: true,
                buttons: [
                    {id:"btn_confirmar_cota",text:"Confirmar",
                        click: function() {
                            if(TAB_COTA.funcaoSalvar)
                                TAB_COTA.funcaoSalvar();
                            	MANTER_COTA.confirmado = true;
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

                    var confirmado = MANTER_COTA.confirmado; 

                    MANTER_COTA.confirmado = false;

                    if (MANTER_COTA.isModoTelaCadastroCota()) {
                        if (!MANTER_COTA.fecharModalCadastroCota && !confirmado) {
                            MANTER_COTA.cancelarCadastro();
                            $('#emailNFCPF').removeAttr('onblur');
                            $('#emailNF').removeAttr('onblur');
                            $('#email').removeAttr('onblur');
                            $('#emailCPF').removeAttr('onblur');
                        }

                        return MANTER_COTA.fecharModalCadastroCota || confirmado;
                    } else {
                        MANTER_COTA.recarregarCadastroCota();
                        return true;
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
                title: novo ? "Nova Cota" : "Histórico de Titularidade Cota - " + MANTER_COTA.numeroCota,
                buttons: [
                    {id:"btn_fechar_historico_titularidade_cota", text:"Fechar",
                        click: function() {
                            $(this).dialog("close");
                            MANTER_COTA.recarregarCadastroCota();
                        }
                    }],
                form: $("#workspaceCota", this.workspace)
            });
        }
        
        MANTER_COTA.verificarAlteracoesCadastroCota();
        
    },

    recarregarCadastroCota : function() {
        MANTER_COTA.definirModoTelaCadastroCota();
        MANTER_COTA.editar(MANTER_COTA.numeroCota, MANTER_COTA.idCota);
    },

    cancelarCadastro:function(acaoPosCancelar){
    	
    	if(!MANTER_COTA._indCadastroCotaAlterado){
    		
            MANTER_COTA.fecharModalCadastroCota = true;

            $("#dialog-close", this.workspace).dialog("close");
            
            $("#dialog-cancelar-cadastro-cota", this.workspace).dialog("close");
            
            $("#dialog-cota", this.workspace).dialog("close");
           
            if (acaoPosCancelar) {
                acaoPosCancelar();
            }

            return;
    	}
    	
        $("#dialog-cancelar-cadastro-cota", this.workspace).dialog({
            resizable: false,
            height:150,
            width:600,
            modal: true,
            buttons: {
                "Confirmar": function() {

                    MANTER_COTA.fecharModalCadastroCota = true;
                    
                    $('#email').attr('onblur','MANTER_COTA.validarEmail(\'#email\')');
                    $('#emailCPF').attr('onblur','MANTER_COTA.validarEmail(\'#emailCPF\')');
                    $('#emailNFCPF').attr('onblur','MANTER_COTA.validarEmail(\'#emailNFCPF\')');
                    $('#emailNF').attr('onblur','MANTER_COTA.validarEmail(\'#emailNF\')');
                    $("#dialog-close", this.workspace).dialog("close");
                    $("#dialog-cancelar-cadastro-cota", this.workspace).dialog("close");
                    $("#dialog-cota", this.workspace).dialog("close");
                    if (acaoPosCancelar) {
                        acaoPosCancelar();
                    }
                },
                "Cancelar": function() {
                	
                	$('#email').attr('onblur','MANTER_COTA.validarEmail(\'#email\')');
                	$('#emailCPF').attr('onblur','MANTER_COTA.validarEmail(\'#emailCPF\')');
                	$('#emailNFCPF').attr('onblur','MANTER_COTA.validarEmail(\'#emailNFCPF\')');
                	$('#emailNF').attr('onblur','MANTER_COTA.validarEmail(\'#emailNF\')');
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
                {numeroCota:$(idCampoNumeroCota, this.workspace).val()},
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

        $(".ui-dialog-title-dialog-cota", this.workspace).html(value);
    },

    /**
     * Flag que indica alteraÃ§Ãµes no cadastro da cota.
     */
    _indCadastroCotaAlterado : false,
    
    /**
     * Detecta que se houveram quaisquer alteraÃ§Ãµes no cadastro de cota.
     */
    verificarAlteracoesCadastroCota : function() {
    	
    	MANTER_COTA._indCadastroCotaAlterado = false;
    	
    	var inputs_aba_dados_cadastrais = 'div[id=tabCota-1] :input';
    	var inputs_aba_enderecos 		= 'div[id=tabCota-2] :input';
    	var inputs_aba_telefones 		= 'div[id=tabCota-3] :input';
    	var inputs_aba_pdv 				= 'div[id=tabCota-4] :input';
    	var inputs_aba_garantia 		= 'div[id=tabCota-5] :input';
    	var inputs_aba_fornecedores 	= 'div[id=tabCota-6] :input';
    	var inputs_aba_desconto 		= 'div[id=tabCota-7] :input';
    	var inputs_aba_financeiro 		= 'div[id=tabCota-8] :input';
    	var inputs_aba_distribuicao 	= 'div[id=tabCota-9] :input';
    	var inputs_aba_socios 			= 'div[id=tabCota-10] :input';
    	
    	var inputAbas = new Array();
    	
    	inputAbas.push(inputs_aba_dados_cadastrais);
    	inputAbas.push(inputs_aba_enderecos);
    	inputAbas.push(inputs_aba_telefones);
    	inputAbas.push(inputs_aba_pdv);
    	inputAbas.push(inputs_aba_garantia);
    	inputAbas.push(inputs_aba_fornecedores);
    	inputAbas.push(inputs_aba_desconto);
    	inputAbas.push(inputs_aba_financeiro);
    	inputAbas.push(inputs_aba_distribuicao);
    	inputAbas.push(inputs_aba_socios);
    	
    	$.each(inputAbas, function(index, value){
    		
        	$(value).change(function() {
        		
        		MANTER_COTA._indCadastroCotaAlterado = true;

        	});
    		
    		
    	});
    	
    },
    
    exibirAutoComplete: function(result, idCampo) {

        $(idCampo, this.workspace).autocomplete({
            source: result,
            minLength: 4,
            delay : 0
        });
    },

    popupAlterarTitular : function() {
        
    	if(!verificarPermissaoAcesso(MANTER_COTA._workspace)){
    		return;
    	}
    	
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
        var numeroCota = "";

        if (MANTER_COTA.tipoCotaSelecionada == MANTER_COTA.tipoCota_CNPJ){

            numeroCota = $("#numeroCotaCNPJ", this.workspace).val();

        } else {

            numeroCota = $("#numeroCotaCPF", this.workspace).val();
        }

        if (isPessoaFisica) {

            campoNumeroCota = $("#numeroCotaCPF", this.workspace);

            COTA_CPF.novoCPF();

        } else {

            campoNumeroCota = $("#numeroCotaCNPJ", this.workspace);

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
        MANTER_COTA.fecharDialogExibicaoHistoricoTitularidade();
    },

    carregarHistoricoTitularidade : function() {
        MANTER_COTA.definirModoTelaHistoricoTitularidade();
        var data = [{name:"idCota", value: MANTER_COTA.idCota },
            {name:"idHistorico", value:MANTER_COTA.idHistorico}];
        $.postJSON(contextPath + "/cadastro/cota/historicoTitularidade", data,
            function(result){
                if(result){
                    if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){
                    	
                    	if(result.listaClassificacao) {
                    		 MANTER_COTA.montarCombo(result.listaClassificacao, "#classificacaoSelecionadaCPF");
                    	}
                    	
                        COTA_CPF.editarCPF(result);
                    } else {
                        MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
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
        MANTER_COTA.cancelarCadastro(MANTER_COTA.carregarHistoricoTitularidade);
    },

    atualizarEstadoTela : function() {
        if (this.isModoTelaCadastroCota()) {
            $("#dialog-cota", this._workspace).find(':input(:disabled)').prop('disabled', false);
            $("#dialog-cota", this._workspace).find('input[name="cotaDTO.status"]').prop('disabled', true);
            $("#dialog-cota", this._workspace).find('.antigosProp').show();
            $("#dialog-cota", this._workspace).find('.ui-datepicker-trigger').show();
            $("#dialog-cota", this._workspace).find('span[id^="btnAlterarTitularidade"]').show();
            $("#dialog-cota", this._workspace).find('.classPesquisar').show();
            $("#btnAddSocio", this._workspace).show();
        } else {
            $("#dialog-cota", this._workspace).find(':input:not(:disabled)').prop('disabled', true);
            $("#dialog-cota", this._workspace).find('.antigosProp').hide();
            $("#dialog-cota", this._workspace).find('.ui-datepicker-trigger').hide();
            $("#dialog-cota", this._workspace).find('span[id^="btnAlterarTitularidade"]').hide();
            $("#dialog-cota", this._workspace).find('.classPesquisar').hide();
            $("#btnAddSocio", this._workspace).hide();
        }
    },

    verificarEntregador : function(){

        if (MANTER_COTA.idCota && MANTER_COTA.idCota != ""){

        	var param = {idCota:MANTER_COTA.idCota};
        	
            $.postJSON(contextPath + "/cota/parametroCobrancaCota/verificarEntregador",
            	param,
                function (result){
                    if (result && result.boolean){

                        $("#cotaTemEntregador").show();
                    } else {

                        $("#cotaTemEntregador").hide();
                    }
                    
                    DISTRIB_COTA.verificarTipoConvencional(MANTER_COTA.idCota);
                },
                null,
                true,
                "dialog-cota"
            );
            this.carregarDistribuicao();
            MANTER_COTA.confirmado = false;
        }
    },
    
    imprimir:function(type){
    	
    	if(MANTER_COTA.resultCotaLength==0){
    		exibirMensagemDialog("WARNING",["Sem resultados para exportar."]);
    		return;
    	}
    	if(type){
    		
    		window.location = contextPath + "/cadastro/cota/exportar?fileType="+type;
    		//$.getJSON(contextPath + "/cadastro/cota/exportar?fileType="+type,null, null);
    		
    	}
    },
    
    uniqArray: function(dirtyArr, keyParam) {
    	var cleanArrObj = {},
    		returnArr = [];
    	
    	for ( var i=0; i < dirtyArr.length; i++ ) {
    		if(dirtyArr[i].value !== '') {
    			cleanArrObj[dirtyArr[i][keyParam]] = dirtyArr[i];
    		}
    	}
    	
    	for ( key in cleanArrObj ) {
    		returnArr.push(cleanArrObj[key]);
    	}
    	
    	return returnArr;
    }

}, BaseController);

var COTA_DESCONTO = $.extend(true,
    {
        initDescontos : function(numCota){
            COTA_DESCONTO.initDescontoCota();
            COTA_DESCONTO.initDescontoProduto();
            COTA_DESCONTO.obterDescontoCota();
            COTA_DESCONTO.obterDescontoProduto();

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
                    width : 395,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Edição',
                    name : 'numeroEdicao',
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
                width : 860,
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
                    width : 165,
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
                width : 860,
                height : 150
            });
        },

        obterDescontoProduto : function(){

            $(".descProdutosGrid", this.workspace).flexOptions({
                url: contextPath+'/cadastro/cota/obterTiposDescontoProduto',
                params: [{name:'idCota', value:MANTER_COTA.idCota},
                    {name:'modoTela', value:MANTER_COTA.modoTela.value},
                    {name:'idHistorico', value:MANTER_COTA.idHistorico}] ,
                newp: 1,
                preProcess:function(result) {
                	 $.each(result.rows, function(index, value) {
                		 
                		 if(value.cell.qtdeProxLcmt != null) value.cell.numeroEdicao = 'por ' + value.cell.qtdeProxLcmt + ' edição(ões)';
                		 
                		 if(!value.cell.numeroEdicao)
                			 value.cell.numeroEdicao='*';
                	 });
                	 
                	return result;
                }
            });

            $(".descProdutosGrid", this.workspace).flexReload();

            $(".grids", MANTER_COTA.workspace).show();
        },

        obterDescontoCota : function(numCota){

            $(".descCotaGrid", this.workspace).flexOptions({
                url: contextPath+'/cadastro/cota/obterTiposDescontoCota',
                params: [{name:'idCota', value: MANTER_COTA.idCota},
                    {name:'modoTela', value: MANTER_COTA.modoTela.value},
                    {name:'idHistorico', value: MANTER_COTA.idHistorico}] ,
                newp: 1
            });

            $(".descCotaGrid", this.workspace).flexReload();

            $(".grids", MANTER_COTA.workspace).show();
        },


        getDataFromResult : function(resultado){

            if (resultado.mensagens) {
                exibirMensagemDialog(
                    resultado.mensagens.tipoMensagem,
                    resultado.mensagens.listaMensagens
                );
                $(".grids", MANTER_COTA.workspace).hide();
                return resultado;
            }

            $(".grids", MANTER_COTA.workspace).show();

            return resultado;
        }

    }
    ,
    BaseController
);

var COTA_FORNECEDOR = $.extend(true, {

    salvarFornecedores: function(){

        var fornecedores = new Array();

        $("#selectFornecedorSelecionado_option_cnpj option", this.workspace).each(function (index) {
            fornecedores.push($(this, this.workspace).val());
        });
        
        var param = {idCota:MANTER_COTA.idCota};
        
        param = serializeArrayToPost('fornecedores', fornecedores, param);

        $.postJSON(
            contextPath + "/cadastro/cota/salvarFornecedores",
            param,
            null,
            function(mensagens){

                COTA_FORNECEDOR.carregarFornecedores();
            },
            true
        );

    },

    carregarFornecedores:function(){
        paramFornecedores = [{name:"idCota", value: MANTER_COTA.idCota},
            {name:"modoTela", value: MANTER_COTA.modoTela.value}];
        $.postJSON(contextPath + "/cadastro/cota/obterFornecedores",
            paramFornecedores,
            function(result){

                if(result){
                    MANTER_COTA.montarCombo(result,"#selectFornecedor_option_cnpj");
                }
            },null,true
        );
        paramSelecionados = [{name:"idCota", value: MANTER_COTA.idCota},
            {name:"modoTela", value: MANTER_COTA.modoTela.value},
            {name:"idHistorico", value: MANTER_COTA.idHistorico}];
        $.postJSON(contextPath + "/cadastro/cota/obterFornecedoresSelecionados",
            paramSelecionados,
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

        MANTER_COTA.definirModoTelaCadastroCota();
        COTA_CNPJ.tratarExibicaoDadosCadastrais();

        TAB_COTA.possuiDadosObrigatorios = false;
        MANTER_COTA.fecharModalCadastroCota = false;

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

                    $("#numeroCotaCNPJ", this.workspace).val(dados.numeroSugestaoCota);
                }

                MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionada");
                
                $("#tabCota", this.workspace).tabs( "option", "disabled", [4] );

                MANTER_COTA.popupCota(true);
            }
        );
        MANTER_COTA.numeroCota = $("#numeroCotaCNPJ", this.workspace).val();
    },

    editarCNPJ:function(result){

        COTA_CNPJ.tratarExibicaoDadosCadastrais();

        COTA_CNPJ.carregarDadosCadastraisCnpj(result);

        MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;

        MANTER_COTA.popupCota(false);
    },

    carregarDadosCadastraisCnpj:function(result){

        COTA_CNPJ.limparCampos();

        $( "#tabCota", this.workspace ).tabs({ selected:0 });
        TAB_COTA.possuiDadosObrigatorios = true;

        $("#numeroCotaCNPJ", this.workspace).val(result.numeroCota);
        $("#email", this.workspace).val(result.email);
        $("#status", this.workspace).val(result.status);
        $("#dataInclusao", this.workspace).html(result.dataInclusao.$);
        $("#razaoSocial", this.workspace).val(result.razaoSocial);
        $("#nomeFantasia", this.workspace).val(result.nomeFantasia);
        $("#numeroCnpj", this.workspace).val(result.numeroCnpj);
        $("#inscricaoEstadual", this.workspace).val(result.inscricaoEstadual);
        $("#inscricaoMunicipal", this.workspace).val(result.inscricaoMunicipal);
        $("#emailNF", this.workspace).val(result.emailNF);
        $("#exigeNFE", this.workspace).attr("checked", (result.exigeNFE == true)?"checked":null);
        $("#contribuinteICMS", this.workspace).attr("checked", (result.contribuinteICMS == true)?"checked":null);
        $("#classificacaoSelecionada", this.workspace).val(result.classificacaoSelecionada);
        $('[name="cotaDTO.tipoDistribuicaoCota"]', this.workspace).val(result.tipoDistribuicaoCota);
        
        $("#percentualCotaBase", this.workspace).html(result.percentualCotaBase+"%");
        
        $("#historicoPrimeiraCota", this.workspace).val((result.cotasBases[0] != undefined) ? result.cotasBases[0].numeroCota : "").disable();
        $("#historicoSegundaCota", this.workspace).val((result.cotasBases[1] != undefined) ? result.cotasBases[1].numeroCota : "").disable();
        $("#historicoTerceiraCota", this.workspace).val((result.cotasBases[2] != undefined) ? result.cotasBases[2].numeroCota : "").disable();
        $("#historicoPrimeiraPorcentagem", this.workspace).val((result.cotasBases[0] != undefined) ? result.cotasBases[0].nomeCota : "").disable();
        $("#historicoSegundaPorcentagem", this.workspace).val((result.cotasBases[1] != undefined) ? result.cotasBases[1].nomeCota : "").disable();
        $("#historicoTerceiraPorcentagem", this.workspace).val((result.cotasBases[2] != undefined) ? result.cotasBases[2].nomeCota : "").disable();

        if(result.inicioPeriodo){
            $("#periodoCotaDe", this.workspace).val(result.inicioPeriodo.$).disable();
        }

        if(result.fimPeriodo){
            $("#periodoCotaAte", this.workspace).val(result.fimPeriodo.$).disable();
        }
        
        $("#tipoCotaFinanceiro", this.workspace).val(result.tipoCotaFinanceiro);
        
        if (result.tipoCotaFinanceiro){
			
			$("#tipoCotaFinanceiro", this.workspace).attr("disabled", "disabled");
		}
        
        COTA_CNPJ.gridAntigosProprietarios.init(result);

        if (result.status == "ATIVO" || result.status == "SUSPENSO") {

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
        formData.push({name:"cotaDTO.tipoDistribuicaoCota", value: $('[name="cotaDTO.tipoDistribuicaoCota"]:visible', this.workspace).val()});

        var existeCota = false;
        for (var i = 0; i < formData.length; i++) {
        	if (formData[i].value == 'cotaDTO.numeroCota') {
        		existeCota = true;
        		break;
        	}
        }
        if (!existeCota) {
	        if (MANTER_COTA.numeroCota) {
	        	formData.push({name:"cotaDTO.numeroCota", value: MANTER_COTA.numeroCota});
	        } else {
	        	formData.push({name:"cotaDTO.numeroCota", value: $('[name="cotaDTO.numeroCota"]').val()});
	        }
        }
        
        var uniqFormData = MANTER_COTA.uniqArray(formData, 'name');

        $.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
    		uniqFormData,
            function(result){
                MANTER_COTA.idCota = result.idCota;
                MANTER_COTA.numeroCota = result.numeroCota;

                TAB_COTA.possuiDadosObrigatorios = true;

                COTA_CNPJ.carregarDadosCadastraisCnpj(result);

                exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"dialog-cota");

                if (MANTER_COTA.isAlteracaoTitularidade) {
                    MANTER_COTA.isAlteracaoTitularidade = false;
                }
                
                MANTER_COTA._indCadastroCotaAlterado = false;

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
        $("#exigeNFE", this.workspace).attr("checked", null);
        $("#contribuinteICMS", this.workspace).attr("checked", null);
        $("#classificacaoSelecionada", this.workspace).val("");
        $("#historicoPrimeiraCota", this.workspace).val("").disable();
        $("#historicoPrimeiraPorcentagem", this.workspace).val("" ).disable();
        $("#historicoSegundaCota", this.workspace).val("").disable();
        $("#historicoSegundaPorcentagem", this.workspace).val("").disable();
        $("#historicoTerceiraCota", this.workspace).val("").disable();
        $("#historicoTerceiraPorcentagem", this.workspace).val("").disable();
        $("#periodoCotaDe", this.workspace).val("").disable();
        $("#periodoCotaAte", this.workspace).val("").disable();

        clearMessageDialogTimeout(null);
    },

    carregarDadosCNPJ: function(idCampo){

    	var cnpj = $(idCampo, this.workspace).val();
    	
    	var params = {"numeroCnpj":cnpj};
    	
        $.postJSON(contextPath + "/cadastro/cota/obterDadosCNPJ", params ,
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
        MANTER_COTA.definirModoTelaCadastroCota();


        COTA_CPF.tratarExibicaoDadosCadastrais();
        COTA_CPF.gridAntigosProprietarios.init();
        COTA_CPF.limparCampos();

        TAB_COTA.possuiDadosObrigatorios = false;
        MANTER_COTA.fecharModalCadastroCota = false;



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
                
                $("#tabCota", this.workspace).tabs( "option", "disabled", [4] );

                MANTER_COTA.popupCota(true);
            }
        );

        MANTER_COTA.numeroCota = $("#numeroCotaCPF", this.workspace).val();

    },

    editarCPF:function(result){


        COTA_CPF.tratarExibicaoDadosCadastrais();

        COTA_CPF.carregarDadosCpf(result);

        MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;

        MANTER_COTA.popupCota(false);

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
        $("#dados-basico-orgaoEmissor", this.workspace).val(result.orgaoEmissor);
        $("#estadoSelecionado", this.workspace).val(result.estadoSelecionado);
        $("#estadoCivilSelecionado", this.workspace).val(result.estadoCivilSelecionado);
        $("#sexoSelecionado", this.workspace).val(result.sexoSelecionado);
        $("#nacionalidade", this.workspace).val(result.nacionalidade);
        $("#natural", this.workspace).val(result.natural);
        $("#emailNFCPF", this.workspace).val(result.emailNF);
        $("#exigeNFE", this.workspace).attr("checked", (result.exigeNFE == true)?"checked":null);
        $("#contribuinteICMS", this.workspace).attr("checked", (result.contribuinteICMS == true)?"checked":null);
        $("#classificacaoSelecionadaCPF", this.workspace).val(result.classificacaoSelecionada);
        $('[name="cotaDTO.tipoDistribuicaoCota"]', this.workspace).val(result.tipoDistribuicaoCota);
        
//        //Ajuste 0153
//        $("#percentualCotaBase", this.workspace).html(result.percentualCotaBase+"%");
        
        $("#historicoPrimeiraCotaCPF", this.workspace).val((result.cotasBases[0] != undefined) ? result.cotasBases[0].numeroCota : "").disable();
        $("#historicoSegundaCotaCPF", this.workspace).val((result.cotasBases[1] != undefined) ? result.cotasBases[1].numeroCota : "").disable();
        $("#historicoTerceiraCotaCPF", this.workspace).val((result.cotasBases[2] != undefined) ? result.cotasBases[2].numeroCota : "").disable();
        $("#historicoPrimeiraPorcentagemCPF", this.workspace).val((result.cotasBases[0] != undefined) ? result.cotasBases[0].nomeCota : "").disable();
        $("#historicoSegundaPorcentagemCPF", this.workspace).val((result.cotasBases[1] != undefined) ? result.cotasBases[1].nomeCota : "").disable();
        $("#historicoTerceiraPorcentagemCPF", this.workspace).val((result.cotasBases[2] != undefined) ? result.cotasBases[2].nomeCota : "").disable();

        if(result.dataNascimento){
            //$("#dataNascimento", this.workspace).val(result.dataNascimento.$);
            $("#dataNascimento", this.workspace).val((result.dataNascimento && result.dataNascimento.$) ? result.dataNascimento.$ : result.dataNascimento);
        }

        if(result.inicioPeriodo){
            $("#periodoCotaDeCPF", this.workspace).val(result.inicioPeriodo.$).disable();
        }

        if(result.fimPeriodo){
            $("#periodoCotaAteCPF", this.workspace).val(result.fimPeriodo.$).disable();
        }
        
        $("#tipoCotaFinanceiro", this.workspace).val(result.tipoCotaFinanceiro);

        if (result.tipoCotaFinanceiro){
			
			$("#tipoCotaFinanceiro", this.workspace).attr("disabled", "disabled");
		}

        COTA_CPF.gridAntigosProprietarios.init(result);

        if (result.status == "ATIVO" || result.status == "SUSPENSO") {

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
        formData.push({name:"cotaDTO.tipoCotaFinanceiro", value: $("#tipoCotaFinanceiro", this.workspace).val()});
        formData.push({name:"cotaDTO.tipoDistribuicaoCota", value: $('[name="cotaDTO.tipoDistribuicaoCota"]:visible', this.workspace).val()});

        if (MANTER_COTA.numeroCota) {
        	formData.push({name:"cotaDTO.numeroCota", value: MANTER_COTA.numeroCota});
        } else {
        	formData.push({name:"cotaDTO.numeroCota", value: $('[name="cotaDTO.numeroCota"]').val()});
        }
        
        var uniqFormData = MANTER_COTA.uniqArray(formData, 'name');

        $.postJSON(contextPath + "/cadastro/cota/salvarCotaCPF",
    		uniqFormData,
            function(result){

                MANTER_COTA.idCota = result.idCota;
                MANTER_COTA.numeroCota = result.numeroCota;

                TAB_COTA.possuiDadosObrigatorios = true;

                COTA_CPF.carregarDadosCpf(result);

                exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"dialog-cota");

                if (MANTER_COTA.isAlteracaoTitularidade) {
                    MANTER_COTA.isAlteracaoTitularidade = false;
                }
                
                if(result.dataNascimento) {
                	$("#dataNascimento", this.workspace).val(result.dataNascimento.$);
                }
                
                MANTER_COTA._indCadastroCotaAlterado = false;

            },
            null,
            true
        );

    },
    carregarDadosCPF: function(idCampo){

        $.postJSON(contextPath + "/cadastro/cota/obterDadosCPF",
            {numeroCPF:$(idCampo, this.workspace).val()} ,
            function(result){

                if(result.email)$("#emailCPF", this.workspace).val(result.email);
                if(result.nomePessoa)$("#nomePessoaCPF", this.workspace).val(result.nomePessoa);
                if(result.numeroRG)$("#numeroRG", this.workspace).val(result.numeroRG);
                if(result.dataNascimento)$("#dataNascimento", this.workspace).val(result.dataNascimento.$);
                if(result.orgaoEmissor)$("#dados-basico-orgaoEmissor", this.workspace).val(result.orgaoEmissor);
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
        $("#dados-basico-orgaoEmissor", this.workspace).val("");
        $("#estadoSelecionado", this.workspace).val("");
        $("#estadoCivilSelecionado", this.workspace).val("");
        $("#sexoSelecionado", this.workspace).val("");
        $("#nacionalidade", this.workspace).val("");
        $("#natural", this.workspace).val("");
        $("#emailNFCPF", this.workspace).val("");
        $("#exigeNFECPF", this.workspace).attr("checked", null);
        $("#classificacaoSelecionadaCPF", this.workspace).val("");
        $("#historicoPrimeiraCotaCPF", this.workspace).val("").disable();
        $("#historicoPrimeiraPorcentagemCPF", this.workspace).val("").disable();
        $("#historicoSegundaCotaCPF", this.workspace).val("").disable();
        $("#historicoSegundaPorcentagemCPF", this.workspace).val("").disable();
        $("#historicoTerceiraCotaCPF", this.workspace).val("").disable();
        $("#historicoTerceiraPorcentagemCPF", this.workspace).val("").disable();
        $("#periodoCotaDeCPF", this.workspace).val("").disable();
        $("#periodoCotaAteCPF", this.workspace).val("").disable();

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
        $("input[name='socioCota.telefone.ddd']", this.workspace).numeric();
		$("input[name='socioCota.telefone.numero']", this.workspace).numeric();
		$("input[name='socioCota.telefone.numero']", this.workspace).mask("9999-9999");
    	
        $("#cep", this.workspace).mask("99999-999");
    },

    popup_novo_socio: function() {
    	
        if (MANTER_COTA.isModoTelaCadastroCota()) {
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
        } else {
            $( "#dialog-socio", SOCIO_COTA._workspace ).dialog({
                resizable: false,
                height:340,
                width:760,
                modal: true,
                buttons: {
                    "Fechar": function() {
                        $( this, SOCIO_COTA._workspace ).dialog( "close" );
                    }
                },
                form: $("#workspaceCota", this.workspace)
            });
        }
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

        $("#cep", SOCIO_COTA._workspace).keyup(function() {

            SOCIO_COTA.enderecoSocio.autoCompletarCep();
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
            {idCota:MANTER_COTA.idCota},
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

        var param = [{name: 'idCota', value: MANTER_COTA.idCota},
            {name: 'modoTela', value: MANTER_COTA.modoTela.value},
            {name: 'idHistorico', value: MANTER_COTA.idHistorico}];

        SOCIO_COTA.rows = [];
        $.postJSON(
            contextPath+'/cadastro/cota/carregarSociosCota',
            param,
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
                "dialog-cota"
            );

            return;
        }

        $.each(data.rows, function(index, value) {

            var idSocio = value.id;

            var acao  = '<a href="javascript:;" onclick="SOCIO_COTA.editarSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_editar.gif" border="0" hspace="5" /></a>';

            if (MANTER_COTA.isModoTelaCadastroCota()) {
                acao += '<a href="javascript:;" isEdicao="true" onclick="SOCIO_COTA.removerSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" /></a>';
            }

            value.cell.acao = acao;

            value.cell.principalFlag =(value.cell.principal == true)
                ?'<img src="' + contextPath + '/images/ico_check.gif" border="0" hspace="5" />'
                :'&nbsp';
        });

        return data;

    },

    editarSocio:function(idSocio){

        SOCIO_COTA.itemEdicao = idSocio;
        var param = [{name: 'idSocioCota', value: idSocio},
            {name: 'modoTela', value: MANTER_COTA.modoTela.value}];
        $.postJSON(
            contextPath+'/cadastro/cota/carregarSocioPorId',
            param,
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
        if (MANTER_COTA.isModoTelaCadastroCota()) {
            $("#btnAddSocio", this.workspace).show();
        }
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
                        row.cell.periodo = row.cell.inicio + ' a ' + row.cell.fim;
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
                sortable : false,
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
        
        $(document).live('keypress', function(e) { 

        	MANTER_COTA.confirmado = false; 
        });
    };
}
//@ sourceURL=manterCota.js