<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">

body{font-size:11px!important;}
td{padding-left:3px;padding-right:3px;}
.titulo{ color:#333; font-size:10px;!important;}
.dadosNota{ color:#000;}

.quebraPaginaEmissao{
			page-break-after: always;
			table-layout: fixed;
		}
.capaImgBox {
	display: none;
	width: 115px;
	float: left;
	border: solid 1px #000; 
	margin: 1px;
}
.capaImg{
	width:110px;
	height:143px;
}
</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao" ).hide();
	window.print();
}

$(function(){$('img.capaImg').load(function() {
	$(this).parent().parent().show();
});});



</script>
</head>

<body>


<c:forEach items="${cotasEmissao}" var="cotaEmissao">

<div class="quebraPaginaEmissao">

<table width="850" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td widlth="781" valign="top">
    <table width="760" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
  <tr>
    <td width="121" rowspan="6" valign="top"><span class="logo"><img src="../images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></td>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="titulo">Razão Social<br />
    </span></td>
    <td width="237" align="center" style="border-left:1px solid #000;"><span class="titulo" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
    </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
    <td rowspan="2" align="center" style="border-left:1px solid #000;">
      
      <H3>Documento Número: ${cotaEmissao.idChamEncCota}</H3>
      
</td>
    </tr>
  <tr>
    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
    </span></td>
    <th width="25">&nbsp;</th>
    <th width="75">&nbsp;</th>
    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
    </span></td>
    <td style="border-bottom:1px solid #000;"><span class="dadosNota">    </span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
    <td style="border-left:1px solid #000;">&nbsp;</td>
    </tr>
  <tr>
    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td align="center"><span class="titulo">UF</span></td>
    <td align="center"><span class="titulo">CEP</span></td>
    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
    <td style="border-left:1px solid #000;">&nbsp;</td>
    </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
    <td align="center" style="border-left:1px solid #000;">&nbsp;</td>
    </tr>
  </table>
<table width="760" border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</span></td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço  </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table>
<table width="760" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
  <tr>
    <td width="179" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="161" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="420" style="border-right:1px solid #000;"><span class="titulo">Box / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} - Rota: ${cotaEmissao.codigoRota} - ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table>
<table width="760" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;">
            <tr class="class_linha_3">
              <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Seq</strong></td>
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Código</strong></td>
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Produto</strong></td>
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
              <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Desconto</strong></td>
              <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>TR</strong></td>
              <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Data Lancto</strong></td>
              <td align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Preço Desc R$</strong></td>
              <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
              <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Qtde. Dev</strong></td>
  </tr>
            <tr class="class_linha_3">
              <td colspan="10" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Código de Barras</strong></td>
            </tr>
           
           
			<c:forEach items="${cotaEmissao.produtos}" var="produto" varStatus="status">
           
            <tr class="class_linha_${status.index%2==0?1:2}">
              <td width="44" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.sequencia}</td>
              <td width="78" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.codigoProduto}</td>
              <td width="175" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.nomeProduto}</td>
              <td width="57" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.edicao}</td>
              <td width="62" align="right" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.desconto}</td>
              <td width="45" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.tipoRecolhimento}</td>
              <td width="80" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.dataLancamento}</td>
              <td width="82" align="right" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.precoComDesconto}</td>
              <td width="67" align="center" style="border-bottom:1px solid #000;padding-left:5px; ">${produto.reparte}</td>
              <td width="70" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.quantidadeDevolvida}</td>
            </tr>
            <tr class="class_linha_${status.index%2==0?1:2}">
              <td colspan="2" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.codigoBarras}</td>
              <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;"></td>
              <td colspan="7" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;"></td>
            </tr>
            
            </c:forEach>
            
</table>


<table width="760" border="0" cellpadding="1" cellspacing="1">
  <tr>
    <td>&nbsp;</td>
    <td align="right">&nbsp;</td>
    <td align="right">&nbsp;</td>
  </tr>
  <tr>
    <td width="430">&nbsp;</td>
    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong> Desconto %</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Vale Reparte Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Encalhe</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Total Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
  </tr>
</table>



    </td>
    <td style="width:10px; border-left:1px dotted #000;">&nbsp;</td>
    <td width="30" valign="top"><table width="410" border="0" cellpadding="0" cellspacing="0" style=" margin-top:10px;">
      <tr>
        <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">Razão Social<br />
        </span></td>
        </tr>
      <tr>
        <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
        </tr>
      <tr>
        <td width="199" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
        </span></td>
        <th width="23">&nbsp;</th>
        <th width="67">&nbsp;</th>
        <td width="121" align="center" style="border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
      </tr>
      <tr>
        <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
        </span></td>
        <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
        <td align="center"><span class="titulo">UF</span></td>
        <td align="center"><span class="titulo">CEP</span></td>
        <td align="center" style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
        </tr>
    </table>
      <table width="410" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
        <tr>
          <td width="207" height="24" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
          <td width="203" align="center" style="border-left:1px solid #000;border-right:1px solid #000;">Data Emissão</td>
        </tr>
        <tr>
          <td height="22" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
          <td align="center" style="border-bottom:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
        </tr>
        <tr>
          <td height="18" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Cliente</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</span></td>
        </tr>
        <tr>
          <td height="17" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Box/Rota</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} - Rota: ${cotaEmissao.codigoRota} - ${cotaEmissao.nomeRota}</span></td>
        </tr>
    </table>
      <table width="410" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;">
        <tr class="class_linha_3">
          <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Seq</strong></td>
          <td colspan="3" align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Produto</strong></td>
          <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
          <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Qtde. Dev</strong></td>
        </tr>
        <tr class="class_linha_3">
          <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000; "><strong>Código</strong></td>
          <td width="67" align="left" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
          <td width="67" align="right" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000;"><strong>Desc</strong></td>
          <td width="126" align="right" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000; "><strong>Preço Desc R$</strong></td>
          <td class="relatorios" style="padding-left:5px; border-bottom:1px solid #000; ">&nbsp;</td>
          <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;">&nbsp;</td>
        </tr>
        
        
        <c:forEach items="${cotaEmissao.produtos}" var="produto" varStatus="status">
        
	        <tr class="class_linha_1">
	          <td width="89" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.sequencia}</td>
	          <td colspan="3" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.nomeProduto}</td>
	          <td width="61" align="center" style="border-bottom:1px solid #000;padding-left:5px; ">${produto.reparte}</td>
	          <td width="67" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.quantidadeDevolvida}</td>
	        </tr>
	        <tr class="class_linha_1">
	          <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.codigoProduto}</td>
	          <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.edicao}</td>
	          <td align="right" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.desconto}</td>
	          <td align="right" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">${produto.precoComDesconto}</td>
	          <td align="center" style="padding-left:5px; border-bottom:1px solid #000;">&nbsp;</td>
	          <td align="center" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
	        </tr>
               
        </c:forEach>
        
    </table>
      <table width="410" border="0" cellpadding="1" cellspacing="1">
  <tr>
    <td>&nbsp;</td>
    <td align="right">&nbsp;</td>
    <td align="right">&nbsp;</td>
  </tr>
  <tr>
    <td width="430">&nbsp;</td>
    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong> Desconto %</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Vale Reparte Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Encalhe</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Total Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
  </tr>
</table>
       
       </td>
  </tr>
</table>
<br/><br/><br/><br/><br/><br/>
</div>
<c:if test="${withCapa}">

<div class="quebraPaginaEmissao" id=paginaCapas>	
	<table width="850" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td widlth="781" valign="top">
		    <table width="760" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
			  <tr>
			    <td width="121" rowspan="6" valign="top"><span class="logo"><img src="../images/logo_sistema.png" width="110" height="70" alt="Novo Distrib"  /></span></td>
			    <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="titulo">Razão Social<br />
			    </span></td>
			    <td width="237" align="center" style="border-left:1px solid #000;"><span class="titulo" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
			    </tr>
			  <tr>
			    <td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
			    <td rowspan="2" align="center" style="border-left:1px solid #000;">
			     <H3>Documento Número: ${cotaEmissao.idChamEncCota}</H3>
			      
			    </td>
			  </tr>
			  <tr>
			    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
			    </span></td>
			    <th width="25">&nbsp;</th>
			    <th width="75">&nbsp;</th>
			    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
			  </tr>
			  <tr>
			    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
			    </span></td>
			    <td style="border-bottom:1px solid #000;"><span class="dadosNota">    </span></td>
			    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
			    <td style="border-left:1px solid #000;">&nbsp;</td>
			    </tr>
			  <tr>
			    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
			    <td align="center"><span class="titulo">UF</span></td>
			    <td align="center"><span class="titulo">CEP</span></td>
			    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
			    <td style="border-left:1px solid #000;">&nbsp;</td>
			    </tr>
			  <tr>
			    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
			    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
			    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
			    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
			    <td align="center" style="border-left:1px solid #000;">&nbsp;</td>
			    </tr>
		  </table>
	<table width="760" border="0" cellpadding="0" cellspacing="0" >
	  <tr>
	    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
	    </span></td>
	    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
	  </tr>
	  <tr>
	    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</span></td>
	    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
	  </tr>
	  <tr>
	    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço  </span></td>
	    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
	    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
	    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
	    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
	  </tr>
	  <tr>
	    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
	  </tr>
	</table>
	<table width="760" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
	  <tr>
	    <td width="179" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
	    <td width="161" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
	    <td width="420" style="border-right:1px solid #000;"><span class="titulo">Box / Rota</span></td>
	  </tr>
	  <tr>
	    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} - Rota: ${cotaEmissao.codigoRota} - ${cotaEmissao.nomeRota}</span></td>
	  </tr>
	</table>
	</td></tr></table>

	<div id="painelCapas">	
		<div style="width: inherit; text-align: center;">
				<span class="titulo" style="font-size:11px!important;">Capas</span>			
		</div>	
	
	<c:forEach items="${cotaEmissao.produtos}" var="produto" varStatus="status">	
		<div class="capaImgBox">			
			<div style="width: inherit; text-align: center;">
				<strong>${status.index+1}</strong>				
			</div>			
			<div style="width: inherit; text-align: center;">
				<img class="capaImg" src="<c:url value='/capa/${produto.idProdutoEdicao}'></c:url>"/>
			</div>
		</div>	
		
    </c:forEach>
    </div>
   <br clear="all"/>
</div>
</c:if>
</c:forEach>
</body>
</html>
