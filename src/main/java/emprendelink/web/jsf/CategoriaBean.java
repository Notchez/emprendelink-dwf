package emprendelink.web.jsf;

import emprendelink.model.Categoria;
import emprendelink.service.CategoriaService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("categoriaBean")
@ViewScoped
public class CategoriaBean
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CategoriaService categoriaService;

    private Integer idCategoria;
    private String nombre;
    private String descripcion;

    private boolean manejaStock =
            true;

    public void guardar() {

        try {

            if (idCategoria == null) {

                categoriaService.crear(
                        nombre,
                        descripcion,
                        manejaStock
                );

                mensajeInfo(
                        "Categoría creada",
                        "La categoría fue registrada correctamente."
                );

            } else {

                categoriaService.actualizar(
                        idCategoria,
                        nombre,
                        descripcion,
                        manejaStock
                );

                mensajeInfo(
                        "Categoría actualizada",
                        "Los cambios fueron guardados correctamente."
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

    public void editar(
            Categoria categoria) {

        if (categoria == null) {
            return;
        }

        idCategoria =
                categoria.getIdCategoria();

        nombre =
                categoria.getNombre();

        descripcion =
                categoria.getDescripcion();

        manejaStock =
                categoria.isManejaStock();
    }

    public void cancelarEdicion() {

        limpiarFormulario();
    }

    public void alternarEstado(
            Integer idCategoria,
            boolean estadoActual) {

        try {

            categoriaService.cambiarEstado(
                    idCategoria,
                    !estadoActual
            );

            mensajeInfo(
                    "Estado actualizado",
                    "El estado de la categoría fue actualizado."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    "No se pudo actualizar",
                    e.getMessage()
            );
        }
    }

    public List<Categoria> getCategorias() {

        return categoriaService
                .listarTodas();
    }

    public boolean isEditando() {
        return idCategoria != null;
    }

    private void limpiarFormulario() {

        idCategoria = null;
        nombre = null;
        descripcion = null;
        manejaStock = true;
    }

    private void mensajeInfo(
            String resumen,
            String detalle) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                resumen,
                                detalle
                        )
                );
    }

    private void mensajeError(
            String resumen,
            String detalle) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                resumen,
                                detalle
                        )
                );
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(
            Integer idCategoria) {

        this.idCategoria =
                idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(
            String nombre) {

        this.nombre =
                nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(
            String descripcion) {

        this.descripcion =
                descripcion;
    }

    public boolean isManejaStock() {
        return manejaStock;
    }

    public void setManejaStock(
            boolean manejaStock) {

        this.manejaStock =
                manejaStock;
    }
}