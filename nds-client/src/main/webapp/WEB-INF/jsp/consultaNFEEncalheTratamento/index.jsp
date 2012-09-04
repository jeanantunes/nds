<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/consultaNFEEncalheTratamento.js"></script>
<script language="javascript" type="text/javascript">
$(function(){
	consultaNFEEncalheTratamentoController.init();
});
</script>
<style type="text/css">
#dialog-nfe{display:none;}
  .dados, .dadosFiltro, .nfes{display:none;}
  #dialog-novo, #dialog-alterar, #dialog-excluir, #dialog-rejeitar, #dialog-confirm{display:none; font-size:12px;}
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialog-dadosNotaFiscal fieldset{width:810px!important;}
  </style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">
<div id="dialog-dadosNotaFiscal" title="Dados da Nota Fiscal" style="display:none;">
	<fieldset>
        <legend>Nota Fiscal</legend>
        <table width="670" border="0" cellspacing="1" cellpadding="1" style="color:#666;">
          <tr>
            <td width="133">Núm. Nota Fiscal:</td>
            <td width="307" id="numeroNotaFiscalPopUp"></td>
            <td width="106">Série:</td>
            <td width="111" id="serieNotaFiscalPopUp"></td>
          </tr>
          <tr>
            <td>Data:</td>
            <td id="dataNotaFiscalPopUp"></td>
            <td>Valor Total R$:</td>
            <td id="valorNotaFiscalPopUp"></td>
          </tr>
          <tr>
            <td>Chave de Acesso:</td>
            <td colspan="3" id="chaveAcessoNotaFiscalPopUp"></td>
          </tr>
        </table>
     </fieldset>
	<br clear="all" />
    <br />

	<fieldset>
        <legend>Produtos Nota Fiscal</legend>
        <table class="pesqProdutosNotaGrid"></table>
    


    <table width="800" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td width="352" align="right"><strong>Total:</strong>&nbsp;&nbsp;</td>
        <td width="83">09</td>
        <td width="82">06</td>
        <td width="182">&nbsp;</td>
        <td width="85">43,00</td>
      </tr>
    </table>
</fieldset>

</div>
<div id="dialog-nfe" title="NF-e">
	<fieldset style="width:310px!important;">
    <legend>Incluir NF-e</legend>
    <table width="280" border="0" cellspacing="1" cellpadding="0">
  <tr>
    <td width="84">Cota:</td>
    <td width="193"><input type="text" id="cotaCadastroNota" name="cotaCadastroNota" style="width:80px; float:left; margin-right:5px;"/>
      <span class="classPesquisar"><a href="javascript:;" onclick="consultaNFEEncalheTratamentoController.pesqEncalhe();">&nbsp;</a></span></td>
  </tr>
  <tr>
    <td>Nome:</td>
    <td><input type="text" name="nomeCotaCadastroNota" id="nomeCotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>NF-e:</td>
    <td><input type="text" name="numeroNotaCadastroNota" id="numeroNotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>Série:</td>
    <td><input type="text" name="serieNotaCadastroNota" id="serieNotaCadastroNota" /></td>
  </tr>
  <tr>
    <td>Chave-Acesso:</td>
    <td><input type="text" name="chaveAcessoCadastroNota" id="chaveAcessoCadastroNota" /></td>
  </tr>
</table>
    </fieldset>
 

</div>



<div id="dialog-confirmar-cancelamento" title="Cancelamento de NF-e" style="display:none;">
	<p>Confirma o cancelamento da NF-e?</p>
 

</div>

<div id="dialog-confirm" title="Aprovar Solicitação">
	<p>Você esta Aprovando uma Solicitação, tem certeza?</p>
 

</div>

<div id="dialog-rejeitar" title="Rejeitar Solicitação">
	<p>Tem certeza que deseja Rejeitar esta Solicitação?</p>
 

</div>



<div id="dialog-novo" title="Geração arquivos Nf-e">
     <p>Confirma a Geração arquivos Nf-e?</p>
</div>


<div class="corpo">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisa NF-e Encalhe para Tratamento</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="31">Cota:</td>
    <td width="120"><input type="text" id="codigoCota" name="codigoCota" style="width:80px; float:left; margin-right:5px;" onblur="consultaNFEEncalheTratamentoController.pesquisarCota();"/></td>
    <td width="35">Nome:</td>    
    <td width="259"><span name="nomeCota" id="nomeCota"></span></td>
    <td width="35">Data:</td>
    <td width="105"><input name="data" type="text" id="data" style="width:80px;"/></td>
    <td width="42">Status:</td>
    <td width="173">    
		<select name="situacaoNfe" id="situacaoNfe" style="width:290px;" onchange="consultaNFEEncalheTratamentoController.mostra_status(this.value);">
		    <option value=""  selected="selected"></option>
		    <c:forEach items="${comboStatusNota}" var="comboStatusNota">
		      		<option value="${comboStatusNota.key}">${comboStatusNota.value}</option>	
		    </c:forEach>
	    </select>
    </td><td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="consultaNFEEncalheTratamentoController.pesqEncalhe();">Pesquisar</a></span></td></tr>
  </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
       <fieldset class="classFieldset">
       	  <legend>NF-e Encalhe para Tratamento</legend>
        <div class="grids" style="display:none;">
		  
          
          <div id="notaRecebida" style="display:none;">
          	<table class="notaRecebidaGrid"></table>
          </div>
          
          <div id="pendenteRecEmissao" style="display:none;">
          	<table class="encalheNfeGrid"></table>
          </div>
          
          
          
			<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/nfe/consultaNFEEncalheTratamento/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/nfe/consultaNFEEncalheTratamento/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
					Imprimir
				</a>
			</span>
             
            <span class="bt_confirmar_novo" title="Confirmar Cancelamento"><a href="javascript:;" onclick="consultaNFEEncalheTratamentoController.popup_nfe('0','0');">
            	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Registrar NF-e</a>
            </span>
            
            <span class="bt_confirmar_novo" title="Confirmar Cancelamento">
            	<a href="javascript:;" onclick="consultaNFEEncalheTratamentoController.popup_confirmar();">
            	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Gerar</a>
            </span>
            
             
             <span class="bt_sellAll" style="float:right;"><label for="sel">Selecionar Todos</label><input type="checkbox" id="sel" name="Todos" onclick="consultaNFEEncalheTratamentoController.checkAll();" style="float:left; margin-right:25px;"/></span>
             
		</div>
              
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

    </div>
</div> 
</form>
</body>
</html>
