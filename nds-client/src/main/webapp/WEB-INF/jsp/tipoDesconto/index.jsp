<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/tipoDesconto.js"></script>

<script language="javascript" type="text/javascript">

	var pesquisaCotaTipoDesconto = new PesquisaCota();

	$(function(){
		tipoDescontoController.init();
	});


</script>
<style type="text/css">
#dialog-box, .produto, .especifico{display:none;}
#dialog-box fieldset{width:570px!important;}
</style>
</head>

<body>

	<div id="dialog-geral" title="Novo Tipo de Desconto Geral" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Desconto %:</td>
	              <td width="239"><input type="text" name="descontoGeral" id="descontoGeral" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="dataAlteracaoGeral" id="dataAlteracaoGeral" style="width:100px;" disabled="disabled" value="${dataAtual}" /></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="textfield24" id="textfield24" style="width:230px;" disabled="disabled" value="Junior Fonseca" /></td>
	            </tr>
	  </table>         
	
	</div>


	<div id="dialog-especifico" title="Novo Tipo de Desconto Especifico" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Cota:</td>
	              <td width="239"><input type="text" name="cotaEspecifica" id="cotaEspecifica" onchange="pesquisaCotaTipoDesconto.pesquisarPorNumeroCota('#cotaEspecifica', '#nomeEspecifico');"  style="width:100px; float:left; margin-right:5px;" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	            </tr>
	            <tr>
	              <td>Nome:</td>
	              <td><input type="text" name="nomeEspecifico" id="nomeEspecifico" style="width:230px;" /></td>
	            </tr>
	            <tr>
	              <td>Desconto %:</td>
	              <td><input type="text" name="descontoEspecifico" id="descontoEspecifico" style="width:100px;" /></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="dataAlteracaoEspecifico" id="dataAlteracaoEspecifico" style="width:100px;" disabled="disabled" value="${dataAtual}"/></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="usuarioEspecifico" id="usuarioEspecifico" style="width:230px;" disabled="disabled" value="Junior Fonseca"/></td>
	            </tr>
	          </table>       
	
	</div>


	<div id="dialog-produto" title="Novo Tipo de Desconto Produto" style="display:none;">    
	    <table width="350" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="100">Código:</td>
	              <td width="239"><input type="text" name="textfield22" id="codigo"  style="width:100px; float:left; margin-right:5px;" onblur="tipoDescontoController.buscarNomeProduto();" /><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	            </tr>
	            <tr>
	              <td>Produto:</td>
	              <td><input type="text" name="textfield4" id="produto" style="width:230px;" onkeyup="tipoDescontoController.pesquisarPorNomeProduto();" /></td>
	            </tr>	            
	            <tr>
	              <td>Edição:</td>
	              <td><input type="text" name="textfield5" id="textfield5" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Desconto %:</td>
	              <td><input type="text" name="textfield2" id="descontoProduto" style="width:100px;"/></td>
	            </tr>
	            <tr>
	              <td>Data Alteração:</td>
	              <td><input type="text" name="textfield3" id="textfield3" style="width:100px;" disabled="disabled" value="${dataAtual}"/></td>
	            </tr>
	            <tr>
	              <td>Usuário:</td>
	              <td><input type="text" name="textfield" id="textfield" style="width:230px;" disabled="disabled" value="Junior Fonseca"/></td>
	            </tr>
	          </table>       
	
	</div>

<div class="corpo">   
    <br clear="all"/>
    <br />
   
    <div class="container">
    
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Tipo de Desconto Cota</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="20"><input type="radio" name="radio" id="radioGeral" value="radio" onclick="tipoDescontoController.mostra_geral();" /></td>
                <td width="47">Geral</td>
                <td width="20"><input type="radio" name="radio" id="radioEspecifico" value="radio" onclick="tipoDescontoController.mostra_especifico();"  /></td>
                <td width="65">Específico</td>
                <td width="20"><input type="radio" name="radio" id="radioProduto" value="radio" onclick="tipoDescontoController.mostra_produto();"  /></td>
                <td width="48">Produto</td>
                <td width="585">
                <div class="especifico">
                <label style="width:auto!important;">Cota:</label>
                <input name="" type="text" style="width:80px; float:left;" />
                <label style="width:auto!important;">Nome:</label>
                <input name="" type="text" style="width:160px; float:left;" />
                </div>
                
                <div class="produto">
                <label style="width:auto!important;">Código:</label>
                <input name="" type="text" style="width:80px; float:left;" />
                <label style="width:auto!important;">Produto:</label>
                <input name="" type="text" style="width:160px; float:left;" />
                </div>
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="tipoDescontoController.pesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset" id="tpoGeral" style="display:none;">
       	  <legend>Tipos de Desconto Geral</legend>
        
        	<table class="tiposDescGeralGrid"></table>
            
       		<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
             <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="tipoDescontoController.popup_geral();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoEspecifico" style="display:none;">
       	  <legend>Tipos de Desconto Específico</legend>
        
        	<table class="tiposDescEspecificoGrid"></table>
       
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
             <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="tipoDescontoController.popup_especifico();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
      
      
      <fieldset class="classFieldset" id="tpoProduto" style="display:none;">
       	  <legend>Tipos de Desconto Produto</legend>
        
       	<table class="tiposDescProdutoGrid"></table>
       
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
             <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="tipoDescontoController.popup_produto();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
    </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

</body>