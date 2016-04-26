package mx.org.fide.modelo;
import java.util.ArrayList;


/**
 * Clase encargada de contener el diccionario de datos de una consulta
 */
public class Campo {
    /* Información proveniente de los metadatos */
    String nombre;
    int clave;
    String tipoDato;
    boolean autoIncrement = false;
    String valor;
    String valorOriginal;
    /* Información proveniente del diccionario de datos*/
    String alias;
    byte obligatorio;
    String tipoControl;
    String evento;
    int claveFormaForanea;
    String filtroForaneo;
    byte editaFormaForanea;
    byte noPermitirValorForaneoNulo;
    String ayuda;
    byte activo;
    int tamano;
    byte visible;
    String valorPredeterminado;
    byte justificarCambio;
    byte usadoParaAgrupar;
    byte cargaDatoForaneosRetrasada;
    Consulta FormaForanea;
    Boolean permitirRangosEnBusqueda;
    String formato;
    String tablaBusqueda;
    /**
     * Recupera clave primaria del campo
     * @return Clave primaria del campo
     */
    public int getClave() {
        return clave;
    }

    /**
     * Establece clave primaria del campo
     * @param clave 
     */
    public void setClave(int clave) {
        this.clave = clave;
    }

    
    /**
     * Recupera clave de forma fornea relacionada con el campo
     * @return Clave de forma fornea relacionada con el campo
     */
    public Consulta getFormaForanea() {
        return FormaForanea;
    }

    /**
     * Establece clave de forma foranea relacionada con el campo
     * @param FormaForanea 
     */
    public void setFormaForanea(Consulta FormaForanea) {
        this.FormaForanea =FormaForanea ;
    }
    
    /**
     * Establece forma foránea, permitiendo pasar parámetros para su construcción
     * @param claveForma 
     * @param reglasDeReemplazo <code>ArrayList</code> con las reglas de reemplazo que resolverán las consultas.
     * @param pk
     * @param w 
     * @param oDb               Objeto <code>com.administrax.modelo.Conexion</code> con datos de la conexión a la base de datos
     * @throws Fallo            si ocurre un problema relacionado a la base de datos
     */
    public void setFormaForanea(int claveForma, String pk, String w, Usuario usuario) throws Fallo {
        this.FormaForanea = new Consulta(claveForma, "foreign", pk, w, null,usuario); 
    }
     
    /**
     * Establece marca de activo del campo, en caso de que esté inactivo el campo será solo lectura
     * @return Verdadero si el campo está activo, falso de otro modo
     */
    public byte getActivo() {
        return activo;
    }

    /**
     * Establece marca de activo del campo, en caso de que esté inactivo el campo será solo lectura
     * @param activo Verdadero si el campo está activo, falso de otro modo
     */
    public void setActivo(byte activo) {
        this.activo = activo;
    }

    /**
     * Recupera la etiqueta que verán los usuario como nombre del campo
     * @return Alias del campo
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Establece la etiqueta que verán los usuario como nombre del campo
     * @param alias Alias del campo
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Recupera la marca de campo autoincrementable de acuerdo a los metadatos de la base
     * @return  Verdadero si el campo es de tipo autoincrement (mysql), identity (ms sql server) 
     *          o cuenta con un trigger de secuencia que lleva el control de la llave primaria (oracle)
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * Recupera la marca de campo autoincrementable de acuerdo a los metadatos de la base
     * @param autoIncrement Verdadero si el campo es de tipo autoincrement (mysql), identity (ms sql server) 
     *          o cuenta con un trigger de secuencia que lleva el control de la llave primaria (oracle)
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * Recupera ayuda relacionada al campo
     * @return  Ayuda relacionada al campo, notas relacionadas, se puede incluir código HTML 
     */
    public String getAyuda() {
        return ayuda;
    }

    /**
     * Establece la ayuda relacionada al campo
     * @param ayuda Descripción del campo, notas relacionadas, se puede incluir código HTML
     */
    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    /**
     * Recupera la clave de la forma foranea relacionada con el campo
     * @return  Clave de forma foranea relacioanda con el campo
     */
    public int getClaveFormaForanea() {
        return claveFormaForanea;
    }

    /**
     * Establece la clave de la forma foranea relacionada con el campo
     * @param claveFormaForanea Clave de forma foranea relacioanda con el campo
     */
    public void setClaveFormaForanea(int claveFormaForanea) {
        this.claveFormaForanea =claveFormaForanea;
    }

    /**
     * Recupera el tipo de dato del campo de acuerdo a los metadatos de la base
     * @return Tipo de datos definido en la  base de datos, los valores dependen del tipo de base de datos
     */
    public String getTipoDato() {
        return tipoDato;
    }

    /**
     * Establece el tipo de dato del campo de acuerdo a los metadatos de la base
     * @param tipoDato Tipo de datos definido en la  base de datos, los valores dependen del tipo de base de datos
     */
    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }


    /**
     * Recupera la marca que indica si el usuario podrá agregar y editar datos de los catálogos foraneos desde los registros
     * del catálogo maestro
     * @return Verdadero si es posible insertar y editar el catálogo foraneo, falso de otro modo
     */
    public byte getEditaFormaForanea() {
        return editaFormaForanea;
    }

    /**
     * Establece la marca que indica si el usuario podrá agregar y editar datos de los catálogos foraneos desde los registros
     * del catálogo maestro
     * @param editaFormaForanea Verdadero si es posible insertar y editar el catálogo foraneo, falso de otro modo
     */
    public void setEditaFormaForanea(byte editaFormaForanea) {
        this.editaFormaForanea = editaFormaForanea;
    }

    /**
     * Recupera el código javascript que disparará eventos asociados al campo, es posible incluir código de jQuery,
     * se debe emplear para operaciones de validación del campo en la capa vista
     * @return Código javascript + jQuery
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Establece el código javascript que disparará eventos asociados al campo, es posible incluir código de jQuery,
     * se debe emplear para operaciones de validación del campo en la capa vista
     * @param evento Código javascript + jQuery
     */
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * Recupera el parámetro que se buscará en el querystring que se empleará para filtrar la consulta foranea
     * @return  Parámetro del querystring
     */
    public String getFiltroForaneo() {
        return filtroForaneo;
    }

    /**
     * Establece el parámetro que se buscará en el querystring que se empleará para filtrar la consulta foranea
     * @param filtroForaneo Parámetro del querystring
     */
    public void setFiltroForaneo(String filtroForaneo) {
        this.filtroForaneo = filtroForaneo;
    }

    /**
     * Recupera marca que indica que el cambio de valor del campo disparará el mecanismo que solicita la justificación del cambio
     * @return Verdadero si se deben justificar los cambios, falso de otro modo
     */
    public byte getJustificarCambio() {
        return justificarCambio;
    }

    /**
     * Establece marca que indica que el cambio de valor del campo disparará el mecanismo que solicita la justificación del cambio
     * @param justificarCambio Verdadero si se deben justificar los cambios, falso de otro modo
     */
    public void setJustificarCambio(byte justificarCambio) {
        this.justificarCambio = justificarCambio;
    }

    /**
     * Recupera el nombre del campo tal como se ve en la base de datos, en el caso de oracle se convierten en minúsculas
     * @return  Nombre del campo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del campo tal como se ve en la base de datos, en el caso de oracle se convierten en minúsculas
     * @param nombre    Nombre del campo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Recupera marca que indica si la lista desplegable del catálogo foraneo asociado con el campo contiene una 
     * opción vacia al principio
     * @return  Verdadero si no se permite un valor nulo en el campo foraneo, falso de otro modo
     */
    public byte getNoPermitirValorForaneoNulo() {
        return noPermitirValorForaneoNulo;
    }

    /**
     * Establece marca que indica si la lista desplegable del catálogo foraneo asociado con el campo contiene una 
     * opción vacia al principio
     * @param noPermitirValorForaneoNulo Verdadero si no se permite un valor nulo en el campo foraneo, falso de otro modo
     */
    public void setNoPermitirValorForaneoNulo(byte noPermitirValorForaneoNulo) {
        this.noPermitirValorForaneoNulo = noPermitirValorForaneoNulo;
    }

    /**
     * Recupera la marca de obligatoriedad de asignarle un valor al campo, la validación se hace automáticameente en la capa vista
     * @return Verdadero si el llenado del campo es obligatorio, falso de otro modo
     */
    public byte getObligatorio() {
        return obligatorio;
    }

    /**
     * Establce la marca de obligatoriedad de asignarle un valor al campo, la validación se hace automáticameente en la capa vista
     * @param obligatorio Verdadero si el llenado del campo es obligatorio, falso de otro modo
     */
    public void setObligatorio(byte obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * Recupera el tamaño en pixeles que ocupa el campo en el grid
     * @return  Número de pixeles de la columna en el grid
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Establece el tamaño en pixeles que ocupa el campo en el grid
     * @param tamano Número de pixeles de la columna en el grid
     */
    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    /**
     * Recupera el tipo de control que se desplegará: hay dos opciones posibles "password" y "file"
     * En caso de que el tipo de control sea password se establecerá el atributo <code>PASSWORD</code> a la etiqueta 
     * <code>INPUT</code> relacionada al campo 
     * @return  Tipo de control <code>HTML</code>
     */
    public String getTipoControl() {
        return tipoControl;
    }

    /**
     * Establece el tipo de control que se desplegará: hay dos opciones posibles "password" y "file"
     * @param tipoControl  Tipo de control <code>HTML</code>
     */
    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    /**
     * Recupera la marca que indica que se debe usar el campo para agrupar consulta; no implementado todavía en la capa vista
     * @return Verdadero si el campo está marcado para ser utilizado en GROUPs BY automatizados
     */
    public byte getUsadoParaAgrupar() {
        return usadoParaAgrupar;
    }

    /**
     * Establece la marca que indica que se debe usar el campo para agrupar consulta; no implementado todavíaa en la capa vista
     * @param usadoParaAgrupar  Verdadero si el campo está marcado para ser utilizado en GROUPs BY automatizados
     */
    public void setUsadoParaAgrupar(byte usadoParaAgrupar) {
        this.usadoParaAgrupar = usadoParaAgrupar;
    }

    /**
     * Recupera el valor predeterminado del campo
     * @return  Valor predeterminado del campo
     */
    public String getValorPredeterminado() {
        return valorPredeterminado;
    }

    /**
     * Establece el valor predeterminado del campo
     * @param valorPredeterminado   Valor predeterminado del campo
     */
    public void setValorPredeterminado(String valorPredeterminado) {
        this.valorPredeterminado = valorPredeterminado;
    }

    /**
     * Recupera la marca de visibilidad del campo
     * @return  Verdadero si el campo es visible, falso de otro modo
     */
    public byte getVisible() {
        return visible;
    }

    /**
     * Establece la marca de visibilidad del campo
     * @param visible   Verdadero si el campo es visible, falso de otro modo
     */
    public void setVisible(byte visible) {
        this.visible = visible;
    }

    /**
     * Recupera la marca de carga retrasada del catálogo foráneo, esta marca indicará al sistema que a pesar de que el campo 
     * está asociado a un catálogo foraneo no se deberán cargar los registros, puesto que antes es necesario validar algún 
     * otro dato.
     * @return  Verdadero si está marcado como carga retrasada, falso de otro modo
     */
    public byte getCargaDatoForaneosRetrasada() {
        return cargaDatoForaneosRetrasada;
    }

    /**
     * Establece la marca de carga retrasada del catálogo foráneo, esta marca indicará al sistema que a pesar de que el campo 
     * está asociado a un catálogo foraneo no se deberán cargar los registros, puesto que antes es necesario validar algún 
     * otro dato.
     * @param cargaDatoForaneosRetrasada     Verdadero si está marcado como carga retrasada, falso de otro modo
     */
    public void setCargaDatoForaneosRetrasada(byte cargaDatoForaneosRetrasada) {
        this.cargaDatoForaneosRetrasada = cargaDatoForaneosRetrasada;
    }

    /**
     * Recupera el valor del campo
     * @return  Valor del campo
     */
    public String getValor() {
        return valor;
    }

    
    /**
     * Establece el valor del campo
     * @param valor Valor del campo
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * Recupera el valor original del campo antes de su edición
     * @return Valor original del campo
     */
    public String getValorOriginal() {
        return valorOriginal;
    }

    /**
     * Establece el valor original del campo antes de su edición
     * @param valorOriginal Valor original del campo
     */
    public void setValorOriginal(String valorOriginal) {
        this.valorOriginal = valorOriginal;
    }

    public Boolean getPermitirRangosEnBusqueda() {
        return permitirRangosEnBusqueda;
    }

    public void setPermitirRangosEnBusqueda(Boolean permitirRangosEnBusqueda) {
        this.permitirRangosEnBusqueda = permitirRangosEnBusqueda;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getTablaBusqueda() {
        return tablaBusqueda;
    }

    public void setTablaBusqueda(String tablaBusqueda) {
        this.tablaBusqueda = tablaBusqueda;
    }

    /**
     * Constructor del objeto
     * @param nombre        Nombre del campo tal como viene de la base de datos pasado a minúsculas
     * @param tipoDato      Tipo de dato tal como viene de los metadaots de la base; en el caso de oracle se hace conversiones equivalentes con los tipos de ms sql y mysql
     * @param autoIncrement Marca que indica si el campo está marcado como AUTOINCREMENT (MySQL), IDENTITY (MS SQL), o cuenta con un trigger que maneja la secuencia de la llave primaria
     */
    public Campo(String nombre, String tipoDato, boolean autoIncrement) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.autoIncrement = autoIncrement;
    }


    @Override
    public String toString() {
        return "FieldDef{" + "nombre=" + nombre + ", tipoDato=" + tipoDato + ", autoIncrement=" + autoIncrement + ", alias=" + alias + ", obligatorio=" + obligatorio + ", tipoControl=" + tipoControl + ", evento=" + evento + ",claveFormaForanea=" + claveFormaForanea + ", filtroForaneo=" + filtroForaneo + ", editaFormaForanea=" + editaFormaForanea + ", noPermitirValorForaneoNulo=" + noPermitirValorForaneoNulo + ", ayuda=" + ayuda + ", activo=" + activo + ", tamano=" + tamano + ", visible=" + visible + ", valorPredeterminado=" + valorPredeterminado + ", justificarCambio=" + justificarCambio + ", usadoParaAgrupar=" + usadoParaAgrupar + '}';
    }

    
}
