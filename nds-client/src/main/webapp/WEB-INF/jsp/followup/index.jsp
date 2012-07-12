<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<title>NDS - Novo Distrib</title>


<script language="javascript" type="text/javascript">
$(function() {
		$( "#tab-followup" ).tabs();

		
		
		$(".chamadaoGrid").flexigrid($.extend({},{
			url : '<c:url value="/followup/pesquisaDadosChamadao"/>',
			dataType : 'json',
			preProcess: exPreProcFollowupChamadao ,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado R$',
				name : 'valorTotalConsignadoFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Suspenso (dias)',
				name : 'qtdDiasSuspensao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Programado',
				name : 'dataProgramadoChamadao',
				width : 85,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 120,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
        }));

		$(".pendenciasGrid").flexigrid($.extend({},{
			url : '<c:url value="/followup/pesquisaDadosPendenciaNFEEncalhe"/>',
			dataType : 'json',
			preProcess: exPreProcFollowupPendenciasnfe,
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 302,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pend&ecirc;ncia',
				name : 'tipoPendencia',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Dt. Entrada',
				name : 'dataEntrada',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vlr. Diferença R$',
				name : 'valorDiferencaFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Telefone',
				name : 'numeroTelefone',
				width : 125,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));

		$(".alteracaoStatusGrid").flexigrid($.extend({},{
			url : '<c:url value="/followup/pesquisaDadosStatusCota"/>',
	        preProcess:  exPreProcFollowupStatusCota, 
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Per&iacuteodo',
				name : 'periodoStatus',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'statusAtual',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Novo Status',
				name : 'statusNovo',
				width : 83,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 155,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 880,
			height : 255
		}));
		
	});
	
</script>

</head>

<body>
    <br clear="all"/>
    <br />
    	<div id="divGeral" >
      <fieldset class="classFieldset">
   	    <legend> Follow Up do Sistema </legend>
        <div id="tab-followup">
        
            <ul>
                <li><a href="#tabNegocia">Negociação</a></li>
                <li><a href="#tabChamadao">Chamadão</a></li>
                <li><a href="#tabAlteracao">Alteração de Status Cota</a></li>
                <li><a href="#tabAtualizacao">Atualização Cadastral</a></li>
                <li><a href="#tabPendencia">Pend&ecirc;ncias NF-e Encalhe</a></li>
            </ul>
            <div id="tabNegocia">
               <fieldset style="width:880px!important;">
               	  <legend>Negociação</legend>
                    <table class="negociacaoGrid"></table>
                    <span class="bt_novos" title="Gerar Arquivo">
                        <a href="javascript:;">
                             <img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo
                        </a>
                    </span>
                    <span class="bt_novos" title="Imprimir">
                        <a href="javascript:;">
                             <img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir
                        </a>
                    </span>
               </fieldset>
            </div>
            <div id="tabChamadao">
                <fieldset style="width:880px!important;">
               	<legend>Chamadão</legend>
                <table class="chamadaoGrid"></table>
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=chamadao">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=chamadao">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>                
               </fieldset>
            </div>
            <div id="tabAlteracao">
                <fieldset style="width:880px!important;">
               	<legend>Alteração de Status Cota</legend>
                <table class="alteracaoStatusGrid"></table>
                  <span class="bt_novos" title="Gerar Arquivo">
                      <a href="javascript:;">
                           <img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo
                      </a>
                  </span>
                  <span class="bt_novos" title="Imprimir">
                      <a href="javascript:;">
                           <img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir
                      </a>
                  </span>
               </fieldset>
            </div>
            <div id="tabAtualizacao">
               <fieldset style="width:880px!important;">
               	<legend>Atualização Cadastral</legend>
                <table class="atualizacaoCadastralGrid"></table>
                  <span class="bt_novos" title="Gerar Arquivo">
                      <a href="javascript:;">
                           <img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo
                      </a>
                  </span>
                  <span class="bt_novos" title="Imprimir">
                      <a href="javascript:;">
                           <img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir
                      </a>
                  </span>
               </fieldset>
            </div>            
            <div id="tabPendencia">
               <fieldset style="width:880px!important;">
               	<legend>Pend&ecirc;ncias NF-e Encalhe</legend>
                <table class="pendenciasGrid"></table>
                  <span class="bt_novos" title="Gerar Arquivo">                      
						<a href="${pageContext.request.contextPath}/followup/exportar?fileType=XLS&tipoExportacao=pendenciaNFE">
         					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
         				Arquivo
						</a>
                  </span>
                  <span class="bt_novos" title="Imprimir">                      
                      <a href="${pageContext.request.contextPath}/followup/exportar?fileType=PDF&tipoExportacao=pendenciaNFE">
         					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
         				Imprimir
						</a>
                  </span>    
               </fieldset>
            </div>
            
            <br clear="all" />
            <br />

		</div>
        <br clear="all" />	
      </fieldset>
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

<script>



$(".atualizacaoCadastralGrid").flexigrid({
        preProcess:  exPreProcFollowupChamadao, 
		dataType : 'xml',
		colModel : [ {
			display : 'Cota',
			name : 'cota',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nome',
			width : 290,
			sortable : true,
			align : 'left'
		}, {
			display : 'Responsável',
			name : 'responsavel',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Documento',
			name : 'docto',
			width : 125,
			sortable : true,
			align : 'left'
		}, {
			display : 'Valor R$',
			name : 'vlr',
			width : 125,
			sortable : true,
			align : 'right'
		}, {
			display : 'Dt. Vencto.',
			name : 'dtVencto',
			width : 85,
			sortable : true,
			align : 'center'
		}],
		sortname : "cota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 880,
		height : 255
	});








$(".negociacaoGrid").flexigrid({
        preProcess:  exPreProcFollowupNegociacao, 
		dataType : 'json',
		colModel : [ {
			display : 'Cota',
			name : 'cota',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nome',
			width : 240,
			sortable : true,
			align : 'left'
		}, {
			display : 'Negociação',
			name : 'negociacao',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Parcela',
			name : 'parcela',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Forma de Pagamento',
			name : 'formaPagto',
			width : 140,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data Vencto',
			name : 'dtVencto',
			width : 85,
			sortable : true,
			align : 'center'
		}],
		sortname : "cota",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 880,
		height : 255
	});
	
	function exPreProcFollowupNegociacao(resultado) {
		alert("exefollowPreProcessamentoNegociacao");
		return resultado;
	};
	function exPreProcFollowupChamadao(resultado) {
			$.each(resultado.rows, function(index, row) {						
			
			var linkExcluir = '<a href="javascript:;" onclick="irParaChamadao(' + row.cell.dataProgramadoChamadao + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir Desconto" src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0px" />Programar' +
							   '</a>';
			
			row.cell.acao = linkExcluir;
		});
		
		$(".grids").show();
		return resultado;
	};

	function irParaChamadao(idCota){
			alert("Entrou na function de ir para o chamadao" + idCota);
		}
	function exPreProcFollowupStatusCota(resultado) {		
		return resultado;
	};
	function exPreProcFollowupCadastro(resultado) {
		alert("exefollowPreProcessamentoCadastro");
		return resultado;
	};	
	function exPreProcFollowupPendenciasnfe(resultado) {        		
   		return resultado;
	};
	
</script>
</body>
</html>