<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoNFe.js"></script>
<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialogCotasSuspensas fieldset{width:570px!important;}
  </style>

<script type="text/javascript">
var geracaoNFe;
$(function(){
	geracaoNFe =  new GeracaoNFe();
	
});
</script>

<div id="dialogCotasSuspensas" title="Gera&ccedil;&atilde;o NF-e">
     <fieldset>
     	<legend>Cotas Suspensas</legend>
     	<table id="gridCotasSuspensas"></table>
        <span class="bt_sellAll" style="float:right;">
	        <label for="checkboxCheckAllCotasSuspensas">Selecionar Todos</label>
	        <input type="checkbox" name="Todos" id="checkboxCheckAllCotasSuspensas" style="float:left;"/>
        </span>
     </fieldset>
</div>


<fieldset class="classFieldset">

   	    <legend> Pesquisar NF-e</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		  <tr>
		    <td>Fornecedor:</td>		
		    <td colspan="3">
		     	<select id="selectFornecedores" multiple="multiple" style="width:400px">
					<c:forEach items="${fornecedores}" var="fornecedor">
							<option value="${fornecedor.key }">${fornecedor.value }</option>
					</c:forEach>
				</select>
		    </td>		
		    <td>Data Emiss&atilde;o:</td>		
		    <td><input name="datepickerDe" type="text" id="datepickerEmissao" style="width:80px;"/></td>		
		    </tr>		
		  <tr>		
		    <td width="91">Tipo de Nota:</td>		
		    <td width="303">		
			    <select id="selectTipoNotaFiscal" style="width:250px; font-size:11px!important">		
				   <c:forEach items="${listaTipoNotaFiscal}" var="tipoNotaFiscal">
							<option value="${tipoNotaFiscal.key }">${tipoNotaFiscal.value }</option>
					</c:forEach>		
				</select>		
		    </td>		
		    <td width="101">Intervalo Box:</td>		
		    <td><input type="text" id="inputIntervaloBoxDe" style="width:76px;"/>&nbsp;At&eacute; &nbsp;<input type="text" id="inputIntervaloBoxAte" style="width:76px;"/></td>
		    <td colspan="2">&nbsp;      &nbsp;</td>		
		  </tr>		
		  <tr>		
		    <td>Cota de:</td>		
		    <td><input type="text" id="inputIntervaloCotaDe" style="width:80px;"/>&nbsp;At&eacute;&nbsp;<input type="text" id="inputIntervaloCotaAte" style="width:80px;"/></td>		
		    <td>Data Movimento:</td>		
		    <td width="233"><input type="text" id="datepickerIntervaloMovimentoDe" style="width:76px;"/>		
		      &nbsp;&nbsp;At&eacute;&nbsp;<input type="text" id="datepickerIntervaloMovimentoAte" style="width:76px;"/></td>		
		    <td width="87">&nbsp;</td>		
		    <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisar">Pesquisar</a></span></tr>		
		  </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       <fieldset class="classFieldset">
       	  <legend>Gera&ccedil;&atilde;o NF-e</legend>
          <div class="grids" style="display:none;">
		  <table id="gridNFe"></table>
          <span class="bt_confirmar_novo" title="Confirma  Gera&ccedil;&atilde;o de Nf-e?"><a href="javascript:;" id="btnGerar" ><img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" border="0" hspace="5" />Gerar</a></span>

          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;" id="btnImprimirXLS"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

          <span class="bt_novos" title="Imprimir"><a href="javascript:;" id="btnImprimirPDF"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>	

          <span class="bt_novos" title="Imprimir NE/NECA"><a href="javascript:alert('Nao Implementada');"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir NE/NECA" hspace="5" border="0" />Imprimir NE/NECA</a></span>

          <span class="bt_novos" title="Visualizar NE/NECA"><a href="javascript:alert('Nao Implementada');"><img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Visualizar NE/NECA" hspace="5" border="0" />Visualizar NE/NECA</a></span>

		</div>

      </fieldset>

      <div class="linha_separa_fields">&nbsp;</div>
