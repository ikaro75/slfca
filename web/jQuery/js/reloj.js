 $(document).ready(function() {
    // Crear dos variables con los nombres de los meses y los nombres de los dias de la semana
    var nombreMes = [ "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" ]; 
    var nombreDia= ["Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"]

    // Creamos el objeto fechaActual deDate 
    var anio = parseInt($('#fechaActual').val().split(",")[0]);
    var mes = parseInt($('#fechaActual').val().split(",")[1])-1;
    var dia = parseInt($('#fechaActual').val().split(",")[2]);
    var hr = parseInt($('#fechaActual').val().split(",")[3]);
    var min = parseInt($('#fechaActual').val().split(",")[4]);
    var seg = parseInt($('#fechaActual').val().split(",")[5]);
    
    var fechaActual = new Date(anio,mes,dia,hr,min,seg);
    
    // Mostramos la fecha en la jsp   
    $('#Date').html(nombreDia[fechaActual.getDay()] + ", " + fechaActual.getDate() + ' de ' + nombreMes[fechaActual.getMonth()] + ' del ' + fechaActual.getFullYear());
    
    var horas = fechaActual.getHours();
    var minutos = fechaActual.getMinutes();
    var segundos = fechaActual.getSeconds();
    var dn = "a.m.";
    
    setInterval( function() {
        segundos++;
        
        if (segundos == 60) {
            segundos = 0;
            minutos++;
            if (minutos == 60) {
                minutos = 0;
                horas++;
                if (horas == 24) {
                    horas = 0;
                }
            }
        }
        $("#hrs").html(( horas < 10 ? "0" : "" ) + horas);
        $("#min").html(( minutos < 10 ? "0" : "" ) + minutos);
        $("#seg").html(( segundos < 10 ? "0" : "" ) + segundos);
    },1000);
}); 