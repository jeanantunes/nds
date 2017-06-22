<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Treelog</title>

<link rel="shortcut icon" type="image/ico" href="images/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="css/NDS.css" />

<script language="javascript" type="text/javascript" src="scripts/jquery-1.7.1.min.js"></script>
<script language="javascript" type="text/javascript">

	$(document).keydown(function (e) {
		if(e.which == 13) {
			$('#btEntrar')[0].click();
		}
	});

	$(document).ready(function() {
		$('input:visible:enabled:first').focus();
	});
	
</script>

</head>

<body>
<br />
<br />
<br />
<br />
<br />
<br />
<br />

<form name="form" action="<c:url value='j_spring_security_check'/>" method="POST" onload="$('#username').focus()" onsubmit="form.submit();">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <input:hidden id="logout_true" name="logout_true" />
  <tr>
    <td align="center" ><img src="images/Logo_Total_Express.png" border="0" alt="Treelog"  /></td>
  </tr>
  <tr>
    <td style="padding-bottom:5px;">&nbsp;
      <div class="bg_login">
        <table width="549" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="28%"  height="140px" align="right">
              <img src="images/logo_nds1_new.png" width="270" height="144" alt="NDS -Sistem TREELOG"  />
            </td>
            <td width="65%" style="border:1px solid #d6d6d6; background:#FFF;"><table width="70%" border="0" align="center" cellpadding="2" cellspacing="2" id="tabLogin">
              
              <tr align="center" >
                <td width="22%">Endereços:</td>
                <td width="78%">
                  <select id="distribuidores" style="width:200px;">
                    <option>Selecione</option>
                    <option value="rjar.nds.treelog.com.br">AR RJ</option>
                    <option value="spar.nds.treelog.com.br">AR SP</option>
                    <option value="spts.nds.treelog.com.br">TELESENA SP</option>
                    <option value="rjtr.nds.treelog.com.br">Agência RJ</option>
                    <option value="sptr.nds.treelog.com.br">Agência SP</option>
                    <option value="rjts.nds.treelog.com.br">Telesena RJ</option>
                    <option value="rjbau.nds.treelog.com.br">Rj BAU</option>
                    <option value="cmp.nds.treelog.com.br">Campinas</option>
                    <option value="sjc.nds.treelog.com.br">São José dos Campos</option>
                    <option value="soc.nds.treelog.com.br">Sorocaba</option>
                    <option value="mcz.nds.treelog.com.br">Maceió</option>
                    <option value="joi.nds.treelog.com.br">Joinville</option>
                    <option value="lag.nds.treelog.com.br">Lages</option>
                    <option value="bsb.nds.treelog.com.br">Brasília</option>
                    <option value="bau.nds.treelog.com.br">BAU</option>
                    <option value="cmpts.nds.treelog.com.br">CMPTS</option>
                    <option value="socts.nds.treelog.com.br">SOCTS</option>
                    <option value="spbau.nds.treelog.com.br">SPBAU</option>
                    <option value="bnu.nds.treelog.com.br">Blumenau</option>
                    <option value="nat.nds.treelog.com.br">Natal</option>
                    <option value="gyn.nds.treelog.com.br">Goiânia</option>
                    <option value="pfp.nds.treelog.com.br">Passo Fundo</option>
                    <option value="aju.nds.treelog.com.br">Aracajú</option>
                    <option value="imp.nds.treelog.com.br">Imperatriz</option>
                    <option value="the.nds.treelog.com.br">Teresina</option>
                    <option value="ccp.nds.treelog.com.br">Caçapava</option>
                  </select>

                </td>
              </tr>

            </table></td>
            <td width="7%">&nbsp;</td>
          </tr>
        </table>
    </div></td>
  </tr>
</table>
</form>
<script language="javascript" type="text/javascript">
$(function() {  


  $('#distribuidores').change(function(){
    var _redirectPage =  $(this).val();

    if(_redirectPage != 'undefined' & _redirectPage != 'null'){
      var redirectWindow = window.open('http://' + _redirectPage, '_blank');
        redirectWindow.location;
    }

  })
});
</script>
</body>
</html>