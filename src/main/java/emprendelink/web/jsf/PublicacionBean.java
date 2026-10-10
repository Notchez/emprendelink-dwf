package emprendelink.web.jsf;

import emprendelink.model.Categoria;
import emprendelink.model.Emprendimiento;
import emprendelink.model.Publicacion;
import emprendelink.model.Usuario;
import emprendelink.model.enums.TipoPublicacion;
import emprendelink.service.CategoriaService;
import emprendelink.service.EmprendimientoService;
import emprendelink.service.PublicacionService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Named("publicacionBean")
@ViewScoped
public class PublicacionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PublicacionService publicacionService;

    @Inject
    private EmprendimientoService emprendimientoService;

    @Inject
    private CategoriaService categoriaService;

    private Integer idPublicacion;
    private Integer idEmprendimiento;
    private Integer idCategoria;
    private TipoPublicacion tipo = TipoPublicacion.PRODUCTO;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock = 0;

    private String filtroTexto;
    private TipoPublicacion filtroTipo;
    private Integer filtroCategoriaId;

    private Integer idDetalle;
    private Publicacion detalle;

    public void guardar() {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        Usuario usuario = obtenerUsuarioSesion();

        if (usuario == null) {
            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );
            return;
        }

        try {

            if (idPublicacion == null) {

                publicacionService.crear(
                        usuario.getIdUsuario(),
                        idEmprendimiento,
                        idCategoria,
                        tipo,
                        nombre,
                        descripcion,
                        precio,
                        stock
                );

                contexto.addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Publicación creada",
                                "La publicación fue registrada correctamente."
                        )
                );

            } else {

                publicacionService.actualizar(
                        usuario.getIdUsuario(),
                        idPublicacion,
                        idEmprendimiento,
                        idCategoria,
                        tipo,
                        nombre,
                        descripcion,
                        precio,
                        stock
                );

                contexto.addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Publicación actualizada",
                                "Los cambios fueron guardados."
                        )
                );
            }

            limpiarFormulario();

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo guardar",
                    e.getMessage()
            );
        }
    }

    public void editar(Integer id) {

        Usuario usuario = obtenerUsuarioSesion();

        if (usuario == null) {
            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );
            return;
        }

        Publicacion publicacion =
                publicacionService.buscarPropiaPorId(
                        id,
                        usuario.getIdUsuario()
                );

        if (publicacion == null) {
            mensajeError(
                    "No disponible",
                    "La publicación no existe o no te pertenece."
            );
            return;
        }

        idPublicacion = publicacion.getIdPublicacion();
        idEmprendimiento = publicacion.getEmprendimiento()
                .getIdEmprendimiento();
        idCategoria = publicacion.getCategoria()
                .getIdCategoria();
        tipo = publicacion.getTipo();
        nombre = publicacion.getNombre();
        descripcion = publicacion.getDescripcion();
        precio = publicacion.getPrecio();
        stock = publicacion.getStock();
    }

    public void cancelarEdicion() {
        limpiarFormulario();
    }

    public void alternarEstado(
            Integer id,
            boolean estadoActual) {

        Usuario usuario = obtenerUsuarioSesion();

        if (usuario == null) {
            mensajeError(
                    "Sesión inválida",
                    "Debes iniciar sesión nuevamente."
            );
            return;
        }

        try {

            publicacionService.cambiarEstado(
                    usuario.getIdUsuario(),
                    id,
                    !estadoActual
            );

            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Estado actualizado",
                                    "El estado de la publicación fue actualizado."
                            )
                    );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo actualizar",
                    e.getMessage()
            );
        }
    }

    public List<Publicacion> getPublicacionesPropias() {

        Usuario usuario = obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        return publicacionService.listarPorPropietario(
                usuario.getIdUsuario()
        );
    }

    public List<Publicacion> getPublicacionesAdmin() {
        return publicacionService.listarTodas();
    }

    public List<Publicacion> getCatalogoFiltrado() {

        String texto = filtroTexto == null
                ? ""
                : filtroTexto.trim()
                .toLowerCase(Locale.ROOT);

        return publicacionService.listarActivas()
                .stream()
                .filter(p -> texto.isBlank()
                        || p.getNombre()
                        .toLowerCase(Locale.ROOT)
                        .contains(texto)
                        || (p.getDescripcion() != null
                        && p.getDescripcion()
                        .toLowerCase(Locale.ROOT)
                        .contains(texto)))
                .filter(p -> filtroTipo == null
                        || p.getTipo() == filtroTipo)
                .filter(p -> filtroCategoriaId == null
                        || p.getCategoria() != null
                        && filtroCategoriaId.equals(
                        p.getCategoria().getIdCategoria()
                ))
                .toList();
    }

    public List<Emprendimiento> getEmprendimientosPropios() {

        Usuario usuario = obtenerUsuarioSesion();

        if (usuario == null) {
            return List.of();
        }

        return emprendimientoService.listarPorPropietario(
                        usuario.getIdUsuario()
                )
                .stream()
                .filter(Emprendimiento::isActivo)
                .toList();
    }

    public List<Categoria> getCategoriasActivas() {
        return categoriaService.listarActivas();
    }

    public TipoPublicacion[] getTipos() {
        return TipoPublicacion.values();
    }

    public boolean isServicio() {
        return tipo == TipoPublicacion.SERVICIO;
    }

    public boolean isEditando() {
        return idPublicacion != null;
    }

    public void cargarDetalle() {

        if (idDetalle == null) {
            enviar404();
            return;
        }

        detalle = publicacionService.buscarActivaPorId(
                idDetalle
        );

        if (detalle == null) {
            enviar404();
        }
    }

    private void enviar404() {

        FacesContext contexto =
                FacesContext.getCurrentInstance();

        ExternalContext externo =
                contexto.getExternalContext();

        try {
            externo.responseSendError(
                    404,
                    "Publicación no encontrada"
            );
            contexto.responseComplete();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo responder con 404.",
                    e
            );
        }
    }

    private Usuario obtenerUsuarioSesion() {

        Object valor = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("usuarioAutenticado");

        return valor instanceof Usuario
                ? (Usuario) valor
                : null;
    }

    private void mensajeError(
            String resumen,
            String detalleMensaje) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                resumen,
                                detalleMensaje
                        )
                );
    }

    private void limpiarFormulario() {

        idPublicacion = null;
        idEmprendimiento = null;
        idCategoria = null;
        tipo = TipoPublicacion.PRODUCTO;
        nombre = null;
        descripcion = null;
        precio = null;
        stock = 0;
    }

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public void setIdEmprendimiento(Integer idEmprendimiento) {
        this.idEmprendimiento = idEmprendimiento;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public TipoPublicacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoPublicacion tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getFiltroTexto() {
        return filtroTexto;
    }

    public void setFiltroTexto(String filtroTexto) {
        this.filtroTexto = filtroTexto;
    }

    public TipoPublicacion getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(TipoPublicacion filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    public Integer getFiltroCategoriaId() {
        return filtroCategoriaId;
    }

    public void setFiltroCategoriaId(Integer filtroCategoriaId) {
        this.filtroCategoriaId = filtroCategoriaId;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Publicacion getDetalle() {
        return detalle;
    }
}
