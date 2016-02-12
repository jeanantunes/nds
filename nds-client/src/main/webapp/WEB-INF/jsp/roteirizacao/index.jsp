<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/confirmDialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.justInput.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/roteirizacao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<style>
label{ vertical-align:super;}
.linha_separa_fields{width:880px!important;}
#dialog-roteirizacao fieldset{margin-right:0px!important; margin-bottom:2px;}
#dialog-roteiro fieldset, #dialog-rota fieldset, #dialog-excluir fieldset, #dialog-transfere-cotas fieldset, #dialog-transfere-rota fieldset{width:350px!important;}
</style>

<script type="text/javascript">

var pesquisaCotaFiltroConsulta = new PesquisaCota(roteirizacao.workspace);
$("#cotaPesquisa",roteirizacao.workspace).justInput(/[0-9]/);
</script>

</head>

<body>

<jsp:include page="roteirizacao.jsp"/>

<form action="" method="get" id="form1" name="form1">
	
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos"><a href="javascript:;" onclick="roteirizacao.novaRoteirizacao();" rel="tipsy" title="Incluir Nova RoteirizaÃ§Ã£o"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0"/></a></span>
			<div id="botoesExportacao" style="display:none;">
				<span class="bt_arq"><a href="javascript:;" onclick="roteirizacao.exportar('XLS');" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>
	    		<span class="bt_arq"><a href="javascript:;" onclick="roteirizacao.exportar('PDF');" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>
	    		<span class="bt_arq"><a href="javascript:;" onclick="roteirizacao.imprimir();" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" alt="Imprimir PDF" hspace="5" border="0" /></a></span>
	    		<span class="bt_arq"><a href="javascript:;" onclick="roteirizacao.imprimirXLS();" rel="tipsy" title="Imprimir XLS"><img src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" alt="Extração Excel" hspace="5" border="0" /></a></span>
    		</div>
		</div>
	</div>
	
    <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Rota / Roteiro</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="33">Box:</td>
              <td width="120">
	               <select name="boxPesquisa" id="boxPesquisa"   onchange="roteirizacao.carregarComboRoteiro()" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
				  </select>
              </td>
                <td width="54">Roteiro:</td>
                <td width="209">
                	<select name="roteiroPesquisa" id="roteiroPesquisa"  onchange="roteirizacao.carregarComboRota('rotaPesquisa')" style="width: 200px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="roteiro" items="${listaRoteiro}">
							<option value="${roteiro.key}">${roteiro.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
                <td width="54">Rota:</td>
                <td width="207">
					<select name="rotaPesquisa" id="rotaPesquisa" style="width: 190px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="rota" items="${listaRota}">
							<option value="${rota.key}">${rota.value}</option>
						</c:forEach>
				    </select>
                </td>
              
             	<td><span class="bt_novos"><a href="javascript:;" onclick="roteirizacao.pesquisarRoteirizacao();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
             	
            </tr>
            <tr>
              <td>Cota:</td>
              <td>
              	
              	<input name="cotaPesquisa" 
		               id="cotaPesquisa" 
		               type="text"
		               maxlength="11"
		               style="width:70px; 
		               float:left; margin-right:5px;"
		               onchange="pesquisaCotaFiltroConsulta.pesquisarPorNumeroCota('#cotaPesquisa', '#nomeCotaPesquisa',false,null,null);"/>
		              
              <td>Nome:</td>
              <td>
              	
              	<input  name="nomeCotaPesquisa" 
			      		 id="nomeCotaPesquisa" 
			      		 type="text" 
			      		 class="nome_jornaleiro" 
			      		 maxlength="255"
			      		 style="width:130px;"
			      		 onkeyup="pesquisaCotaFiltroConsulta.autoCompletarPorNome('#nomeCotaPesquisa');" 
			      		 onblur="pesquisaCotaFiltroConsulta.pesquisarPorNomeCota('#cotaPesquisa', '#nomeCotaPesquisa',false,null,null);" />
              
              </td>   
            </tr>
          </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:none;">
	  
	      <fieldset class="fieldGrid">
	      	
	      	<legend>Rotas / Roteiros Cadastrados</legend>
	       	  	
	       	<div class="gridWrapper">
	        
	        	<table class="rotaRoteirosGrid"></table>
	        
	        </div>
	        	
	      </fieldset>
      </div>
      
      <div id="dialog-detalhes" title="Cotas" style="width: auto; min-height: 60.40000009536743px; height: auto; " class="ui-dialog-content ui-widget-content" scrolltop="0" scrollleft="0">
		<fieldset>
	    	<legend id="legendDetalhesCota"></legend>
	        
	        <table id="cotasGrid" width="440" border="0" cellspacing="1" cellpadding="1"> </table>
	
	  </fieldset>
	</div>
      
</form>

</body>
