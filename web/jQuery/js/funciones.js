/* 
\u00e1 -> á 
\u00e9 -> é 
\u00ed -> í 
\u00f3 -> ó
\u00fa -> ú 

\u00c1 -> Á 
\u00c9 -> É 
\u00cd -> Í 
\u00d3 -> Ó 
\u00da -> Ú 

\u00f1 -> ñ 
\u00d1 -> Ñ
 * and open the template in the editor.
 */
//Libreria de funciones comunes y validaciones
function getkey(e)
{
    if (window.event)
        return window.event.keyCode;
    else if (e)
        return e.which;
    else
        return null;
}

function openpop(sPagina, nWidth,nLenght) {
    window.open(sPagina,'saraPop',"location=0,status=0,scrollbars=yes,width=" + nWidth + ",height=" + nLenght);
}


function check_date(field){
    var checkstr = "0123456789";
    var DateField = field;
    var Datevalue = "";
    var DateTemp = "";
    var seperator = "/";
    var day;
    var month;
    var year;
    var leap = 0;
    var err = 0;
    var i;
    err = 0;
    DateValue = DateField.value;
    /* Delete all chars except 0..9 */
    for (i = 0; i < DateValue.length; i++) {
        if (checkstr.indexOf(DateValue.substr(i,1)) >= 0) {
            DateTemp = DateTemp + DateValue.substr(i,1);
        }
    }
    DateValue = DateTemp;
    /* Always change date to 8 digits - string*/
    /* if year is entered as 2-digit / always assume 20xx */
    if (DateValue.length == 6) {
        DateValue = DateValue.substr(0,4) + '20' + DateValue.substr(4,2);
    }
    if (DateValue.length != 8) {
        err = 19;
    }
    /* year is wrong if year = 0000 */
    year = DateValue.substr(4,4);
    if (year == 0) {
        err = 20;
    }
    /* Validation of month*/
    month = DateValue.substr(2,2);
    if ((month < 1) || (month > 12)) {
        err = 21;
    }
    /* Validation of day*/
    day = DateValue.substr(0,2);
    if (day < 1) {
        err = 22;
    }
    /* Validation leap-year / february / day */
    if ((year % 4 == 0) || (year % 100 == 0) || (year % 400 == 0)) {
        leap = 1;
    }
    if ((month == 2) && (leap == 1) && (day > 29)) {
        err = 23;
    }
    if ((month == 2) && (leap != 1) && (day > 28)) {
        err = 24;
    }
    /* Validation of other months */
    if ((day > 31) && ((month == "01") || (month == "03") || (month == "05") || (month == "07") || (month == "08") || (month == "10") || (month == "12"))) {
        err = 25;
    }
    if ((day > 30) && ((month == "04") || (month == "06") || (month == "09") || (month == "11"))) {
        err = 26;
    }
    /* if 00 ist entered, no error, deleting the entry */
    if ((day == 0) && (month == 0) && (year == 00)) {
        err = 0;
        day = "";
        month = "";
        year = "";
        seperator = "";
    }
    /* if no error, write the completed date to Input-Field (e.g. 13.12.2001) */
    if (err == 0) {
        DateField.value = day + seperator + month + seperator + year;
        return true;
    }
    /* Error-message if err != 0 */
    else {
        alert("Fecha no v\u00e1lida, verifique");
        DateField.value="";
        DateField.select();
        DateField.focus();
        return false;
    }
}

/*-------*/

function check_number(field,nRangoMenor,nRangoMayor) {
    var numberField = field.value;// parseInt(field.value);

    if (isNaN(numberField)&&field.value!=='') {
        alert('Valor inv\u00e1lido, se debe indicar un n\u00famero, verifique.');
        field.value='';	
        field.select();
        field.focus();
        return false;
    }
    else
    {
        if (nRangoMayor==0&&nRangoMenor==0)
            return true;
        else 
        {
            if (numberField>nRangoMayor) {
                alert('El n\u00famero indicado es mayor a ' + nRangoMayor +', verifique');
                field.select();
                field.focus();	
                return false;
            }
            else {
                if (numberField<nRangoMenor) {
                    alert('El n\u00famero indicado es menor a ' + nRangoMenor +', verifique');
                    field.select();
                    field.focus();
                    return false
                }
                else return true;
            }	
        }
    }	
    return true;	
}

function setCookie(name, value, expires, path, domain, secure) {
    var curCookie = name + "=" + escape(value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") +
    ((secure) ? "; secure" : "");
    document.cookie = curCookie;
}


/*
  name - name of the desired cookie
  return string containing value of specified cookie or null
  if cookie does not exist
*/

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    } else
        begin += 2;
    var end = document.cookie.indexOf(";", begin);
    if (end == -1)
        end = dc.length;
    return unescape(dc.substring(begin + prefix.length, end));
}

function right(e) {
    if (navigator.appName == 'Netscape' && (e.which == 3 || e.which == 2))
        return false;
    else if (navigator.appName == 'Microsoft Internet Explorer' && (event.button == 2 || event.button == 3)) {
        alert("FIDE (c)");
        return false;
    }
    return true;
}

function getRS(s) {
    //Recupera un archivo XML a partir de una consulta a la base de datos
    if (window.ActiveXObject) // for IE 
    { 
        var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
        var objDOM=new ActiveXObject("Microsoft.XMLDOM");
        objDOM.async=false;
    } 
    else if (window.XMLHttpRequest) // for other browsers 
    { 
        var httpRequest = new XMLHttpRequest(); 
        var objDOM=document.implementation.createDocument("","",null) 
    } 	

    httpRequest.open('GET','http://investigacion.ilce.edu.mx/panel_control/getXML.asp?q=' + s, false); 
    httpRequest.send(null); 
    return httpRequest.responseText;
	
}

function setXMLInSelect2(sSelect, sTabla, sCampo,sWhere) {
    if (window.ActiveXObject) // for IE 
    { 
        var objDOM=new ActiveXObject("Microsoft.XMLDOM");
    } 
    else if (window.XMLHttpRequest) // for other browsers 
    { 
        var objDOM=document.implementation.createDocument("","",null) 
    } 	

    objDOM.async=false;
    var s='http://investigacion.ilce.edu.mx/panel_control/getXML2.asp?t=' + sTabla + '&c=' + sCampo + '&w=' + sWhere
    objDOM.load(s);
    var x = objDOM.getElementsByTagName('row');

    //Llena una lista a partir de un archivo XML
    //Obtiene el nombre del select y lo transforma en objeto
    var aSelect=sSelect.split(",");
    for (a=0;a<aSelect.length;a++) {
		
        var oSelect=document.getElementById(aSelect[a]);
        //var oSelect=eval('window.document.form1.' + aSelect[a]); 
		
        if (oSelect!=null) {

            //Elimina los elementos de la lista
            oSelect.options.length=0
		
            /*for (m=oSelect.options.length-1;m>0;m--) {
			oSelect.options[m]=null
		}*/
		
            //Carga en un objeto el XML
            //Siempre deja la primera opci?n vac?a
            var addOption = new Option('',''); 
            oSelect.options[oSelect.length] = addOption;
            for (i=0;i<x.length;i++) {
                addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue); 
                oSelect.options[oSelect.length] = addOption;
            }
        } 
    }
}

function setXMLInSelect3(sSelect,cf,ta,pk,w) {
    var httpRequest;
    if (window.ActiveXObject) // for IE 
        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest) // for other browsers 
        httpRequest = new XMLHttpRequest();

    var s="";
    if (w==undefined)
        w="";

    if (pk!=null || pk!="")
        s="control?$cmd=plain&$cf="+cf+"&$ta="+ta+"&$pk="+pk+"&$w="+w
    else
        s="control?$cmd=plain&$cf="+cf+"&$ta="+ta+"&$w="+w

    httpRequest.open('GET',s, false);
    httpRequest.send(null);

    var x = httpRequest.responseXML.getElementsByTagName('registro');
    var error=httpRequest.responseXML.getElementsByTagName('error');

    if (error.length>0) {
        alert("Ha perdido la conexi\u00f3n, intente otra vez");
        return
    }

    //Llena una lista a partir de un archivo XML
    //Obtiene el nombre del select y lo transforma en objeto
    //Analiza el nombre del objeto
    if (sSelect.indexOf(" ")>-1) {
        oSelect = document.forms[sSelect.split("#")[0].trim()].elements[sSelect.split("#")[1]];
    }
    else 
        oSelect=document.getElementById(sSelect);
    
    //var oSelect=eval('window.document.form1.' + aSelect[a]);

    if (oSelect!=null) {

        //Elimina los elementos de la lista
        oSelect.options.length=0

        //Carga en un objeto el XML
        //Siempre deja la primera opcion vacia
        var addOption = new Option('','');
        oSelect.options[oSelect.length] = addOption;
        for (i=0;i<x.length;i++) {
            /*if (pk!=null)
            addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[1].childNodes[0].nodeValue);
        else*/
            var display=x[i].childNodes[3].childNodes[0].nodeValue.replace(/&aacute;/g,"\u00e1").replace(/&eacute;/g,"\u00e9").replace(/&iacute;/g,"�").replace(/&oacute;/g,"�").replace(/&uacute;/g,"\u00fa").replace(/&ntilde;/g,"�");
            addOption = new Option(display,x[i].childNodes[1].childNodes[0].nodeValue);
            oSelect.options[oSelect.length] = addOption;
        }
    }
}

function setXMLInSelect4(sSelect,cf,ta,pk,w) {
    var httpRequest;
    if (window.ActiveXObject) // for IE 
        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest) // for other browsers 
        httpRequest = new XMLHttpRequest();

    var s="";
    if (w==undefined)
        w="";

    if (pk!=null || pk!="")
        s="control?$cmd=plain&$cf="+cf+"&$ta="+ta+"&$pk="+pk+"&$w="+w
    else
        s="control?$cmd=plain&$cf="+cf+"&$ta="+ta+"&$w="+w

    httpRequest.open('GET',s, false);
    httpRequest.send(null);

    var x = httpRequest.responseXML.getElementsByTagName('registro');
    var error=httpRequest.responseXML.getElementsByTagName('error');

    if (error.length>0) {
        alert(error);
        return
    }

    //Llena una lista a partir de un archivo XML
    //Obtiene el nombre del select y lo transforma en objeto
		
    var oSelect=$(sSelect);
    //var oSelect=eval('window.document.form1.' + aSelect[a]);

    if (oSelect.length>0) {

        //Elimina los elementos de la lista
        oSelect.find('option').remove().end().append('<option value=""></option>');
        
        //Carga en un objeto el XML
        //Siempre deja la primera opcion vacia
        
        for (i=0;i<x.length;i++) {
            /*if (pk!=null)
            addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[1].childNodes[0].nodeValue);
        else*/
           var display=x[i].childNodes[3].childNodes[0].nodeValue.replace(/&aacute;/g,"\u00e1").replace(/&eacute;/g,"\u00e9").replace(/&iacute;/g,"\u00ed").replace(/&oacute;/g,"\u00f3").replace(/&uacute;/g,"\u00fa").replace(/&ntilde;/g,"\u00f1");
           oSelect.append('<option value="' + x[i].childNodes[1].childNodes[0].nodeValue + '" >' + display + '</option>');
        }
    } else{
        alert("No se encontr\u00f3 el control indicado");
    }      
}

function emailCheck (emailStr) {
    /* Verificar si el email tiene el formato user@dominio. */
    var emailPat=/^(.+)@(.+)$/ 

    /* Verificar la existencia de caracteres. ( ) < > @ , ; : \ " . [ ] */
    var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]" 

    /* Verifica los caracteres que son v�lidos en una direcci�n de email */
    var validChars="\[^\\s" + specialChars + "\]" 

    var quotedUser="(\"[^\"]*\")" 

    /* Verifica si la direcci�n de email est� representada con una direcci�n IP V�lida */ 
    var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/


    /* Verificar caracteres inv?lidos */ 
    var atom=validChars + '+'
    var word="(" + atom + "|" + quotedUser + ")"
    var userPat=new RegExp("^" + word + "(\\." + word + ")*$")
    /*domain, as opposed to ipDomainPat, shown above. */
    var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$")


    var matchArray=emailStr.match(emailPat)
    if (matchArray==null) {
        alert("El correo parece ser incorrecto (Verifique el @ y los .)")
        return false
    }
    var user=matchArray[1]
    var domain=matchArray[2]

    // Si el user "user" es valido 
    if (user.match(userPat)==null) {
        // Si no
        alert("El nombre de usuario no es v\u00e1lido.")
        return false
    }

    /* Si la direcci?n IP es v?lida */
    var IPArray=domain.match(ipDomainPat)
    if (IPArray!=null) {
        for (var i=1;i<=4;i++) {
            if (IPArray[i]>255) {
                alert("IP de destino inv\u00e1lida")
                return false
            }
        }
        return true
    }

    var domainArray=domain.match(domainPat)
    if (domainArray==null) {
        alert("El dominio parece no ser v\u00e1lido.")
        return false
    }

    var atomPat=new RegExp(atom,"g")
    var domArr=domain.match(atomPat)
    var len=domArr.length
    if (domArr[domArr.length-1].length<2 || domArr[domArr.length-1].length>3) { 
        alert("La direcci\u00f3n debe tener 3 letras si es .'com' o 2 si en de alg\u00fan pa\u00eds.");
        return false
    }

    if (len<2) {
        var errStr="La direcci\u00f3n es erronea"
        alert(errStr)
        return false
    }

    // La direcci�n de email ingresada es V�lida
    return true;
}
// End -->

function sDateToString(dDate) {
    var nDay=dDate.getDate();
    var nMonth=dDate.getMonth()+1;
    var nYear=dDate.getFullYear();
    var sDate="";


    if (nDay<10) {
        sDate=sDate + '0' + nDay;
    }
    else {
        sDate=sDate + nDay;
    }
        
    sDate+="/";
        
    if (nMonth<10) {
        sDate= sDate + '0' + nMonth;
    }
    else {
        sDate= sDate + nMonth;
    }

    sDate+="/"+nYear;
        
    return sDate;
}

function sDate(dDate) {
    var nDay=dDate.getDate();
    var nMonth=dDate.getMonth()+1;
    var nYear=dDate.getFullYear();
    var sDate="";

    sDate= nYear;
    if (nMonth<10) {
        sDate= sDate + '0' + nMonth;
    }
    else {
        sDate= sDate + nMonth;
    }

    if (nDay<10) {
        sDate=sDate + '0' + nDay;
    }
    else {
        sDate=sDate + nDay;
    }

    return sDate
}

function sDateTime(dDate) {
    var nSeconds=dDate.getSeconds();
    var nMinutes=dDate.getMinutes();
    var nHours=dDate.getHours();
    var nDay=dDate.getDate();
    var nMonth=dDate.getMonth()+1;
    var nYear=dDate.getFullYear();
    var sDate="";

    sDate= nYear;
    if (nMonth<10) {
        sDate= sDate + '0' + nMonth;
    }
    else {
        sDate= sDate + nMonth;
    }

    if (nDay<10) {
        sDate=sDate + '0' + nDay;
    }
    else {
        sDate=sDate + nDay;
    }

    var timeValue = "" + ((nHours >12) ? nHours -12 :nHours)
    if (timeValue == "0") timeValue = 12;
    timeValue += ((nMinutes < 10) ? "0" : "") + nMinutes
    timeValue += ((nSeconds < 10) ? "0" : "") + nSeconds

    return sDate + timeValue;
}

function sDateTimeToString(dDate) {
    //var nSeconds=dDate.getSeconds();
    var nMinutes=dDate.getMinutes();
    var nHours=dDate.getHours();
    var nDay=dDate.getDate();
    var nMonth=dDate.getMonth()+1;
    var nYear=dDate.getFullYear();
    var sDate="";

    if (nDay<10) {
        sDate+='0' + nDay;
    }
    else {
        sDate+=nDay;
    }
    
        if (nMonth<10) {
        sDate+=  '/0' + nMonth;
    }
    else {
        sDate+= '/' + nMonth;
    }
    
    sDate+= "/" + nYear ;

    var timeValue = " "+ nHours; // + ((nHours >12) ? nHours -12 :nHours)
    //if (timeValue == "0") timeValue = 12;
    timeValue += ((nMinutes < 10) ? ":0" : ":") + nMinutes;
    //timeValue += ((nSeconds < 10) ? ":0" : ":"); + nSeconds

    return sDate + timeValue;
}

function removeHTMLTags(s){
    var strInputCode = s;
    /* 
    This line is optional, it replaces escaped brackets with real ones, 
    i.e. < is replaced with < and > is replaced with >
 		*/	
    strInputCode = strInputCode.replace(/&(lt|gt);/g, function (strMatch, p1){
        return (p1 == "lt")? "<" : ">";
    });
    var strTagStrippedText = strInputCode.replace(/<\/?[^>]+(>|$)/g, "");
    return strTagStrippedText;
// Use the alert below if you want to show the input and the output text
//		alert("Input code:\n" + strInputCode + "\n\nOutput text:\n" + strTagStrippedText);	

}

function setXMLInSelect(sSelect, q) {
    if (window.ActiveXObject) // for IE
    {
        var objDOM=new ActiveXObject("Microsoft.XMLDOM");
    }
    else if (window.XMLHttpRequest) // for other browsers
    {
        var objDOM=document.implementation.createDocument("","",null)
    }

    objDOM.async=false;
    var s='panel_control/getXML.asp?q=' + q
    objDOM.load(s);
    var x = objDOM.getElementsByTagName('row');

    //Llena una lista a partir de un archivo XML
    //Obtiene el nombre del select y lo transforma en objeto
    var aSelect=sSelect.split(",");
    for (a=0;a<aSelect.length;a++) {

        var oSelect=document.getElementById(aSelect[a]);
        //var oSelect=eval('window.document.form1.' + aSelect[a]);

        if (oSelect!=null) {

            //Elimina los elementos de la lista
            oSelect.options.length=0

            /*for (m=oSelect.options.length-1;m>0;m--) {
			oSelect.options[m]=null
		}*/

            //Carga en un objeto el XML
            //Siempre deja la primera opci�n vac�a
            var addOption = new Option('','');
            oSelect.options[oSelect.length] = addOption;
            for (i=0;i<x.length;i++) {
                addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue);
                oSelect.options[oSelect.length] = addOption;
            }
        }
    }
}

function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
        cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        num = num.substring(0,num.length-(4*i+3))+','+
        num.substring(num.length-(4*i+3));
    return (((sign)?'':'-') + '$' + num + '.' + cents);
}

function formatInt(num) {
    num = num.toString().replace(/\$|\,/g,'');
    if(isNaN(num))
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num*100+0.50000000001);
    cents = num%100;
    num = Math.floor(num/100).toString();
    if(cents<10)
        cents = "0" + cents;
    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
        num = num.substring(0,num.length-(4*i+3))+','+
        num.substring(num.length-(4*i+3));
    return (((sign)?'':'-')  + num + '.' + cents);
}

function validateCode(code) {
    try {
        eval(code); 
        alert ('C\u00f3digo v\u00e1lido');
    } catch (e) {
        if (e instanceof SyntaxError) {
            alert(e.message);
        }
    }
}

function validateSQL(code) {
    var httpRequest;
    if (window.ActiveXObject) // for IE 
        httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
    else if (window.XMLHttpRequest) // for other browsers 
        httpRequest = new XMLHttpRequest();

    s="control?$cmd=validate&q="+encodeURIComponent(code);

    httpRequest.open('POST',s, false);
    httpRequest.send(null);

    var error=httpRequest.responseXML.getElementsByTagName('error');

    if (error.length>0) {
        alert("Consulta no v\u00e1lida: "+ $(error).text());
        return
    }
    
    var resultado=httpRequest.responseXML.getElementsByTagName('resultado');
    alert ('Consulta resultante: ' + $(resultado).text());
}

function replaceAll( text, busca, reemplaza ){
  while (text.toString().indexOf(busca) != -1)
      text = text.toString().replace(busca,reemplaza);
  return text;
}
