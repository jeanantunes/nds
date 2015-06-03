<div>
<script type="text/javascript" src="scripts/flexGridService.js"></script>
<script type="text/javascript" src="scripts/analiseHistoricoVenda.js"></script>

<script type="text/javascript">

$(function() {
	analiseHistoricoVendaController.init();
	//if(typeof(montarDados) == "function")montarDados(anaLiseHistogramaController.workspace);
});


	
	// adicionando a função voltar para baseAnalise
	$('#voltarBaseAnalise').click(function(){
		$("#baseAnalise").show();
  	 	$('#analiseHistoricoContent').hide();
  	 	$("#analiseHistorico").show();
	})
	
function mostraDados_historicoVenda(){
	$('.detalhesDados-historicoVenda').show();
	}
function escondeDados_historicoVenda(){
	$('.detalhesDados-historicoVenda').hide();
	}
</script>
   


<style>
.gridScroll tr:hover{background:#FFC}
.analiseRel tbody{height:100px; overflow:auto;}
.analiseRel tr:hover{background:#FFC;}
.class_tpdv{width:55px;}
.class_novaCota{width:32px;}
.class_cota{width:40px;}
.class_nome{width:90px;}
.class_npdv{width:30px;}
.class_media{width:35px; color:#F00; font-weight:bold;}
.class_vlrs{width:35px;}
.class_vda{width:35px; color:#F00; font-weight:bold;}
.detalhesDados-historicoVenda{display:none; background:#fff; margin-left: 164px; border:1px solid #ccc; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); z-index:1; width:950px; }
</style>

<div id="dialog-cotas-detalhes" title="Pontos de Vendas" style="display:none;">  
<fieldset style="width:690px!important; margin-top:5px;">
  <legend>Cota</legend>
        
    <table width="686" border="0" cellpadding="2" cellspacing="1">
        <tr>
          <td width="50"><strong>Cota:</strong></td>
          <td width="43" id="popUpNumeroCota"></td>
          <td width="78"><strong>Nome:</strong></td>
          <td width="289" id="popUpNomePessoa"></td>
          <td width="58"><strong>Tipo:</strong></td>
          <td width="137" id="popUpTipoCota"></td>
        </tr>
        <tr>
          <td><strong>Ranking:</strong></td>
          <td id="popUpRanking"></td>
          <td><strong>Faturamento:</strong></td>
          <td id="popUpFaturamentoCota"></td>
          <td><strong>Mês/Ano:</strong></td>
          <td id="popUpData"></td>
        </tr>
    </table>

</fieldset>
  	<fieldset style="width:690px!important; margin-top:5px;">
    	<legend>PDV´s Cadastrados</legend>
    	<table class="cotasDetalhesGrid"></table>
    </fieldset>
    
  


</div>
<div class="corpo">
  <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Base de Estudo < evento > com < status >.</b></p>
	</div>
    
    	<div class="detalhesDados-historicoVenda" style="margin-left: 0px;">
        	<a href="javascript:;" onclick="escondeDados_historicoVenda();" style="float:right; margin-right:5px; margin-top:5px;"><img src="images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" /></a>
	    	<small></small><table border="0" cellpadding="2" cellspacing="2" class="dadosTab"><small>
	    		</small><tr id="analiseHistoricoPopUpNomeProduto" />
	    	    <tr id="analiseHistoricoPopUpNumeroEdicao" />
	    	    <tr id="analiseHistoricoPopUpDatalancamento" />
	    	    <tr id="analiseHistoricoPopUpReparte" />
	    	    <tr id="analiseHistoricoPopUpVenda" />
	  	    </table>
    	</div>
      <fieldset class="classFieldset">
       	  <legend>Histórico de Vendas</legend>
        <div class="grids" style="display:block;">
        
        <table width="950" border="0" cellspacing="2" cellpadding="2">
              <tr>
                <td width="257" align="right"><strong>Edições:</strong></td>
                <td width="72" align="center" class="header_table">Média</td>
                <c:forEach var="prodEdicao" items="${listProdutoEdicao}" varStatus="status">
                	<c:if test="${!empty prodEdicao.numeroEdicao}">
                		<td width="85" align="center" class="header_table">${prodEdicao.numeroEdicao}</td>
                	</c:if>
                	<c:if test="${status.last}">
                		<c:forEach var="i" begin="1" end="${6 - status.count}">
	                		<td width="85" align="center" class="header_table"></td>
                		</c:forEach>
                	</c:if>
                </c:forEach>
                
                <td width="22" align="center"><a href="javascript:;" onclick="mostraDados_historicoVenda();"><img src="images/ico_boletos.gif" title="Exibir Detalhes" width="19" height="15" border="0" /></a></td>
            </tr>
          </table>
        <table class="baseHistoricoGrid"></table>
        

        <table width="950" border="0" cellspacing="2" cellpadding="2">
          <tr class="class_linha_1" id="rodapeAnaliseHistorico">
            <!-- adicionado via ajax -->
          </tr>
          </table>
        <span class="bt_novos"><a href="javascript:;" id="voltarBaseAnalise"><img src="images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span></div>
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"  id="analiseHistoricoVendaXLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
    <span class="bt_novos" title="Imprimir"><a href="javascript:;" id="analiseHistoricoVendaPDF" ><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
      </fieldset>
       </div>
     </div>
</div>