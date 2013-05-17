var desenglobacaoController = $.extend(true, {

	pesquisaCota:null,    
	errorCallBack : function errorCallBack(){
		$('#filtroDesenglobaNomePessoa,#statusCota,#filtroPrincipalNumeroCota,#filtroPrincipalNomePessoa,#filtroDesenglobaNumeroCota').val('');        

    },

    sucessCallBack : function errorCallBack(result){
        if (result) {
            $('#statusCota').val(result.status);
        }
    },

    init : function() {

        // ###### INICIO FILTRO DA TELA PRINCIPAL ######
		
		if(desenglobacaoController.pesquisaCota){
			desenglobacaoController.pesquisaCota.validateResult=function(result){
				var msgArray =[];
				if(result.tipoDistribuicaoCota!='Alternativo' && result.tipoDistribuicaoCota!='Convencional'){
					msgArray.push("Cota pesquisada dever ser do tipo Alternativo ou Convencional.");
				}else if(result.tipoDistribuicaoCota=='Alternativo'){
					msgArray.push("Cota do tipo Alternativo, atualizar Mix.");
				}
				return msgArray;
			};
		}
        // FILTRO PRINCIPAL - POR COTA
        $('#filtroPrincipalNumeroCota').change(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNumeroCota('#filtroPrincipalNumeroCota','#filtroPrincipalNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
        });

        $('#filtroPrincipalNomePessoa').keyup(function (){
            desenglobacaoController.pesquisaCota.autoCompletarPorNome('#filtroPrincipalNomePessoa');
        });

        //pesquisarPorNomeCota
        $('#filtroPrincipalNomePessoa').blur(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNomeCota('#filtroPrincipalNumeroCota','#filtroPrincipalNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
        });

        //###### FIM FILTRO DA TELA PRINCIPAL ######




        //###### INICIO FILTRO DA POPUP INSERT ######

        //FILTRO DA TELA DE DESENGLOBACAO
        // FILTRO POR COTA
        $('#filtroDesenglobaNumeroCota').change(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNumeroCota('#filtroDesenglobaNumeroCota','#filtroDesenglobaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack,true);
        });

        $('#filtroDesenglobaNomePessoa').keyup(function (){
            desenglobacaoController.pesquisaCota.autoCompletarPorNome('#filtroDesenglobaNomePessoa');
        });

        //FILTRO POR NOME
        $('#filtroDesenglobaNomePessoa').blur(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNomeCota('#filtroDesenglobaNumeroCota','#filtroDesenglobaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack,true);
        });



        //FILTRO POPUP INSERIR COTA
        // FILTRO POR COTA
        $('#inserirEnglobadaNumeroCota').change(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNumeroCota('#inserirEnglobadaNumeroCota','#inserirEnglobadaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack,true);
        });

        $('#inserirEnglobadaNomePessoa').keyup(function (){
            desenglobacaoController.pesquisaCota.autoCompletarPorNome('#inserirEnglobadaNomePessoa');
        });

        //FILTRO POR NOME
        $('#inserirEnglobadaNomePessoa').blur(function (){
            desenglobacaoController.pesquisaCota.pesquisarPorNomeCota('#inserirEnglobadaNumeroCota','#inserirEnglobadaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack,true);
        });

        //###### FIM FILTRO DA POPUP INSERT ######



        //###### AÇÕES DOS BOTÕES DA TELA ######

        //EXPORTAR
        $('#gerarArquivoDesengloba').click(function(){
            desenglobacaoController.exportar("XLS");
        });

        $('#imprimirDesengloba').click(function(){
            desenglobacaoController.exportar("PDF");
        });

        //PESQUISAR DA TELA PRINCIPAL
        $('#pesquisaPorCota').click(function (){
            desenglobacaoController.porCota();
        });

        $('#btnInserirCotaEnglobada').click(function(e){
            e.preventDefault();

            var indexInput = $('#tableCotasEnglobadas tr').length;


            desenglobacaoController.adicionarCotaEnglobada(indexInput,$('#inserirEnglobadaNumeroCota').val(),$('#inserirEnglobadaNomePessoa').val(),$('#inserirEnglobadaPorcentagemCota').val(),
                    $('#filtroDesenglobaNumeroCota').val(),$('#filtroDesenglobaNomePessoa').val());

            clearFormEnglobada();
            indexInput++;
        });

        function clearFormEnglobada() {
            $('#inserirEnglobadaNumeroCota,#inserirEnglobadaNomePessoa,#inserirEnglobadaPorcentagemCota').val('');
        }

        desenglobacaoController.Url = {},

        desenglobacaoController.Grids = {
                Util : {
                    reload : function reload(options) {

                        // Inicializa o objeto caso não
                        // exista
                        options = options || {};

                        // obtendo a url padrão caso o
                        // usuário não tenha informado
                        options.url = this.Url.urlDefault || options.url;

                        options.preProcess = options.preProcess || this.PreProcess._default || function(result) {
                            if (result.mensagens) {
                                exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                                $(".grids").hide();
                                return result;
                            }

                            $(".grids").show();
                            return result;
                        };

                        // GUARDA O ULTIMO PARÂMETRO
                        // UTILIZADO
                        this.lastParams = options.params;

                        if (options !== undefined) {
                            if (options.workspace === undefined) {
                                $("." + this.gridName).flexOptions(options);
                                $("." + this.gridName).flexReload();
                            } else {
                                $("." + this.gridName, options.workspace).flexOptions(options);
                                $("." + this.gridName, options.workspace).flexReload();
                            }
                        }
                    }
                },
                EnglobadosGrid : {
                    reload : {},
                    lastParams : []
                }
        };

        desenglobacaoController.Util = {
                getFiltroByForm : function(idForm) {
                    var filtro;

                    if (idForm === undefined) {
                        return null;
                    } else {
                        filtro = $('#' + idForm).serializeArray();

                        for ( var index in filtro) {
                            if (filtro[index].value === "on") {
                                filtro[index].value = true;
                            } else if (filtro[index].value === "off") {
                                filtro[index].value = false;
                            }
                        }

                        return filtro;
                    }
                },
        },

        desenglobacaoController.Grids = {
                EnglobadosGrid : {
                    gridName : "englobadosGrid",
                    Url : {
                        urlDefault : contextPath + "/distribuicao/desenglobacao/pesquisaPrincipal",
                    },
                    comments : "GRID PRINCIPAL DA TELA DESENGLOBAÇÃO",
                    reload : desenglobacaoController.Grids.Util.reload,
                    PreProcess : {

                        _default : function(result) {

                            if (result.mensagens) {
                                exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                                $(".grids").hide();
                                return result;
                            }
                            desenglobacaoController.listaDesenglobacaoLength = result.rows.length;
                            $.each(result.rows,	function(index,	row) {
                                // onclick="desenglobacaoController.excluirExcecaoProduto('+row.cell.idExcecaoProdutoCota+');"
                                var link = '<a href="javascript:;" style="cursor:pointer" onclick="desenglobacaoController.acaoEditar('+row.cell.numeroCotaDesenglobada+');">'
                                + '<img title="detalhe" src="'	+ contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />'
                                + '</a>';
                                link+="<input type='image' src='"+contextPath+"/images/ico_excluir.gif' onclick='desenglobacaoController.acaoExcluir("+row.cell.idDesenglobacao+");' alt='Excluir'/>";		

                                row.cell.acao = link;
                            });

                            $(".grids").show();
                            return result;
                        }
                    },
                    init : $(".englobadosGrid").flexigrid({
                        colModel : [ {
                            display : 'Cota',
                            name : 'numeroCotaEnglobada',
                            width : 40,
                            sortable : true,
                            align : 'left'
                        }, {
                            display : 'Nome',
                            name : 'nomeCotaEnglobada',
                            width : 220,
                            sortable : true,
                            align : 'left'
                        }, {
                            display : 'Tipo de PDV',
                            name : 'nomePDV',
                            width : 220,
                            sortable : true,
                            align : 'left'
                        }, {
                            display : '% da Cota',
                            name : 'porcentagemCota',
                            width : 63,
                            sortable : true,
                            align : 'left'
                        }, {
                            display : 'Usuário',
                            name : 'nomeUsuario',
                            width : 110,
                            sortable : true,
                            align : 'left'
                        }, {
                            display : 'Data Alteração',
                            name : 'dataAlteracao',
                            width : 100,
                            sortable : true,
                            align : 'center'
                        }, {
                            display : 'Hora',
                            name : 'hora',
                            width : 50,
                            sortable : true,
                            align : 'center'
                        },
                        {
                            display : 'Ação',
                            name : 'acao',
                            width : 50,
                            sortable : true,
                            align : 'center'
                        }],
                        sortname : "numeroCota",
                        sortorder : "asc",
                        usepager : true,
                        useRp : true,
                        rp : 15,
                        showTableToggleBtn : true,
                        width : 960,
                        height : 255
                    })
                }
        };

    },

    porCota : function() {
        var filtroPrincipalCota = [], util = desenglobacaoController.Util, grids = desenglobacaoController.Grids;

        filtroPrincipalCota = util.getFiltroByForm("filtroPrincipal");

        grids.EnglobadosGrid.reload({
            dataType : 'json',
            params : filtroPrincipalCota,
        });
    },

    novaEnglobacao : function(listaEnglobadas){

        $("#formInserirEnglobada input[name='alterando'][type='hidden']").val(desenglobacaoController.res);
        var formData = $("#formInserirEnglobada").serialize();	

        $.post(contextPath + "/distribuicao/desenglobacao/inserirEnglobacao", formData, function(result) {

            if (result.mensagens) {
                exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                return result;
            }

            var filtroPrincipalCota = [], util = desenglobacaoController.Util, grids = desenglobacaoController.Grids;

            filtroPrincipalCota = util.getFiltroByForm("filtroPrincipal");

            grids.EnglobadosGrid.reload({
                dataType : 'json',
                params : filtroPrincipalCota,
            });
        });

    },
    popup: function popup(alterando) {

        desenglobacaoController.res=alterando;

        $('#tableCotasEnglobadas').empty();
        $('#filtroDesenglobaNumeroCota,#filtroDesenglobaNomePessoa,#inserirEnglobadaNumeroCota,#inserirEnglobadaNomePessoa').val('');

        $("#dialog-novo-desenglobacao").dialog({
            resizable: false,
            height:500,
            width:650,
            modal: true,
            open:function(){
                if(alterando){
                    $.post(contextPath + "/distribuicao/desenglobacao/editarDesenglobacao", [{name:'cotaNumeroDesenglobada', value: alterando}],
                            function(res) {
                        if(res){
//                          desenglobacaoController.res=res;
                            if(res){
                                result=res.result;
                                var cotaDesenglobada = result[0];
                                var cotasEnglobadasArray = result[1];
                                $("#filtroDesenglobaNumeroCota").val(cotaDesenglobada.numeroCota);
                                $("#filtroDesenglobaNomePessoa").val(cotaDesenglobada.nomePessoa);

                                for ( var int = 0; int < cotasEnglobadasArray.length; int++) {
                                    cota = cotasEnglobadasArray[int];
                                    desenglobacaoController.adicionarCotaEnglobada(int,cota.numeroCotaEnglobada,cota.nomeCotaEnglobada,cota.porcentagemCota,
                                            cotaDesenglobada.numeroCota,cotaDesenglobada.nomePessoa);
                                }

                            }
                        }
                    });
                }
            },
            buttons: {
                "Confirmar": function() {
                    $("#effect").show("highlight", {}, 1000, callback);
                    $(".grids").show();
                    desenglobacaoController.novaEnglobacao();
                    $(this, desenglobacaoController.workspace).dialog("destroy");
                },
                "Cancelar": function() {
                    $(this, desenglobacaoController.workspace).dialog("destroy");
                }
            }
        });
    },

    acaoEditar:function(idx){
        this.popup(idx);
    },
    acaoExcluir:function(idDesenglobacao){
        $("#dialog-excluir-desenglobacao").dialog({
            resizable: false,
            draggable: false,
            height:170,
            width:380,
            modal: true,
            buttons: {
                "Confirmar": function() {

                    $.postJSON(contextPath + "/distribuicao/desenglobacao/excluirDesenglobacao", 
                            {id:idDesenglobacao},
                            function(result) {
                                console.log(result);
                                desenglobacaoController.ex=result;
                                if(result.mensagens){
                                    exibirMensagem(result.mensagens.tipoMensagem,result.mensagens.listaMensagens);
                                    return;
                                }

                                desenglobacaoController.Grids.EnglobadosGrid.reload({
                                    dataType : 'json',
                                    params : desenglobacaoController.Util.getFiltroByForm("filtroPrincipal"),
                                });
                                $("#dialog-excluir-desenglobacao").dialog("close");
                                return;
                            },
                            null,
                            true
                    );
                },
                "Cancelar": function() {
                    $( this ).dialog( "close" );
                }
            }
        });

    },

    adicionarCotaEnglobada:function(indexInput,inserirEnglobadaNumeroCota,inserirEnglobadaNomePessoa,inserirEnglobadaPorcentagemCota,
            filtroDesenglobaNumeroCota,filtroDesenglobaNomePessoa){

        var html = '<tr>';
        html+= "<td><input type='text' name='desenglobaDTO["+indexInput+"].numeroCotaEnglobada' value='"+ inserirEnglobadaNumeroCota +"' style='width: 30px;' class='filtroDefaultCota'></td>";
        html+= "<td style='width: 450px;'><input type='text' name='desenglobaDTO["+indexInput+"].nomeCotaEnglobada' value='"+ inserirEnglobadaNomePessoa +"' style='width: 400px;' class='filtroDefaultPessoa'></td>";
        html+= "<td><input type='text' name='desenglobaDTO["+indexInput+"].porcentagemCota' value='"+ inserirEnglobadaPorcentagemCota +"' style='width: 30px;'>";

        if (indexInput > -1) {
        	html+= "<input type='hidden' name='desenglobaDTO["+indexInput+"].numeroCotaDesenglobada' value='"+filtroDesenglobaNumeroCota+"'>";
        	html+= "<input type='hidden' name='desenglobaDTO["+indexInput+"].nomeCotaDesenglobada' value='"+filtroDesenglobaNomePessoa+"'>";
        }
        
        html+="</td>";
        html+= '</tr>';

        $('#tableCotasEnglobadas').append(html);

    },
    //guarda resultado da pesquisa de desenglobacao
    listaDesenglobacaoLength: 0,

    exportar: function exportar(fileType) {
        if(desenglobacaoController.listaDesenglobacaoLength==0){
            exibirMensagemDialog("WARNING",["Sem resultados para exportar."]);
            return;
        }
        $.get(contextPath + "/distribuicao/desenglobacao/exportar?fileType=" + fileType, function(result) {
            if (result.mensagens) {
                exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                return result;
            } else {
                window.location = contextPath + "/distribuicao/desenglobacao/exportar?fileType=" + fileType;
            }
        });
    },

}, BaseController);
//@ sourceURL=desenglobacaoController.js
