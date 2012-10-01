<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/vendaEncalheCota.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/contaCorrenteCota.js'></script>

<script language="javascript" type="text/javascript">
	var pesquisaCotaContaCorrentCota = new PesquisaCota();
	
	$(function(){
		contaCorrenteCotaController.init();
	});
	
</script>
<style type="text/css">
  fieldset { width:auto!important; }
  
  #dialog-venda-encalhe, #dialog-consignado, #dialog-email, #bt_email{
  	display: none;
  }
  </style>
</head>

<body>

<form id="form-consignado">
<div id="dialog-consignado" title="Consignados">
     
	<fieldset style="">
    	<strong><span id="datacotanome_consignado"></span></strong>
    	
    	 <div class="gridsConsignado" style="display: none;">
        
        	<table class="consignadoCotaGrid"></table>
        
        	<span class="bt_novos">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarConsignadoCota?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_novos">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarConsignadoCota?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
				     
				<div align="right">
								
					<table>
						<tr>
							<td><strong>Total R$:</strong></td>
							<td> </td>
						</tr>				
						<tr>
							<td></td>													
							<td><span id="listaInfoConsignado"></span></td>
						</tr>						
					</table>			
				</div>       	
	    </div>  
    </fieldset>
</div>
</form>

<form id="form-encalhe">
<div id="dialog-encalhe" title="Encalhe da Cota">
	<fieldset>
    
      	<strong><span id="datacotanome"></span></strong>
        
        <div class="gridsEncalhe" style="display: none;">
        
	        <table class="encalheCotaGrid"></table>
	        
	        <span class="bt_novos">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarEncalhe?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_novos">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarEncalhe?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
			
			<div align="right">
								
				<table>
					<tr>
						<td><strong>Total R$:</strong></td>
						<td> </td>
					</tr>				
					<tr>
						<td></td>													
						<td><span id="listaInfoEncalhe"></span></td>
					</tr>						
				</table>
			</div>       	
	    </div>   
    </fieldset>

</div>
</form>

<form id="form-email">
<div id="dialog-email" title="Enviar por e-mail">
	<fieldset>
    	<legend>Dados da Conta</legend>
        
        <table width="437" border="0" cellspacing="2" cellpadding="2">
	        <tr>
	            <td width="82">Cota:</td>
	            <td width="348"><span name ="assunto" id="numeroCotaEmail"></span>
	            <!-- <span class="classPesquisar"><a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarItemContaCorrenteCotaEmail();">&nbsp;</span></td> -->
	          </tr>
	          <tr>
	            <td>Nome:</td>
	            <td><span id="nomeCotaEmail"></span></td>
	          </tr>
	          <tr>
	            <td>E-mail:</td>
	            <td><input name="destinatarios" id="emailCotaEmail" disabled="disabled" type="text" style="width:300px;" />
	            	<a href="javascript:;" onclick="contaCorrenteCotaController.editarEmail();">
	            		<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0" alt="Editar E-mail" />
	            	</a>
	            </td>
	          </tr>
	          <tr>
	            <td>C&oacute;pia para:</td>
	            <td><input name="destinatarios" id="copiaParaCotaEmail" type="text" style="width:300px;" /></td>
	          </tr>
	          <tr>
	            <td valign="top">Mensagem:</td>
	            <td><textarea name="mensagem" id="mensagemCotaEmail" cols="" rows="10" style="width:300px;"></textarea>
	  			</td>
	          </tr>
        </table>

  </fieldset>
</div>
</form>


<form id="form-venda-encalhe">
<div id="dialog-venda-encalhe" title="Venda de Encalhe">
	<fieldset>
    	<legend><span id="datacotanome-venda-encalhe"></span></legend>
        
        <table class="vendaEncalheGrid"></table>
        <span class="bt_novos">
			<a id="dialog-venda-encalhe-export-xls" href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarVendaEncalhe?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
			</a>
		</span>
			
		<span class="bt_novos">
			<a id="dialog-venda-encalhe-export-pdf" href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarVendaEncalhe?fileType=PDF" rel="tipsy" title="Imprimir">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
			</a>
		</span>
       	<div align="right">								
				<table id="totaisFornecedores-venda-encalhe" width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;" >
					<tr>
						<td><strong>Total R$:</strong></td>
					</tr>			
				</table>
			</div>       	
       	
    </fieldset>
</div>
</form>

    
		<div class="areaBts">
			<div class="area">
				<span class="bt_novos">
					<a href="javascript:;" rel="tipsy" title="Negociar Divida">
						<img src="${pageContext.request.contextPath}/images/ico_negociar.png" hspace="5" border="0" />
					</a>
				</span>
				
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportar?fileType=XLS" rel="tipsy"  title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
				
				<span class="bt_novos" id="bt_email">
					<a href="javascript:;"  onclick="contaCorrenteCotaController.popup_email();" rel="tipsy" title="Enviar por e-mail">
						<img src="${pageContext.request.contextPath}/images/ico_email.png" hspace="5" border="0" />
					</a>
				</span>
				
			</div>
		</div>
	
		<div class="linha_separa_fields">&nbsp;</div>	
      <fieldset class="fieldFiltro">
   	    <legend> Pesquisar Conta-Corrente</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="33">Cota:</td>
              <td width="92"><input type="text" name="filtroViewContaCorrenteCota.numeroCota" id="cota" onchange="pesquisaCotaContaCorrentCota.pesquisarPorNumeroCota('#cota', '#nomeCota');" style="width:80px; float:left; margin-right:5px;"/>
              </td>
              <td width="41">Nome:</td>
              <td width="240"><input type="text" name="nomeCota" id="nomeCota" onkeyup="pesquisaCotaContaCorrentCota.autoCompletarPorNome('#nomeCota');" onblur="pesquisaCotaContaCorrentCota.pesquisarPorNomeCota('#cota', '#nomeCota');" style="width:230px;"/></td>
              <td width="518"><span class="bt_novos"><a href="javascript:;" onclick="contaCorrenteCotaController.pesquisarItemContaCorrenteCota();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display: none;">
	      <fieldset class="fieldGrid">
	       	  <legend>Conta-Corrente Selecionado: <span id="cotanome"></span></legend>
	       	  <table class="itemContaCorrenteCotaGrid"></table>
	      </fieldset>
      </div>

<script>

</script>
</body>
</html>
