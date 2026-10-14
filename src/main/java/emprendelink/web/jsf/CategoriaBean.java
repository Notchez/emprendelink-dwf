package emprendelink.web.jsf;

import emprendelink.model.Categoria;
import emprendelink.service.CategoriaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CategoriaBean implements Serializable {

    @Inject
    private CategoriaService categoriaService;

    private Categoria categoriaActual;
    private List<Categoria> listaCategorias;

    @PostConstruct
    public void init() {
        categoriaActual = new Categoria();
        cargarCategorias();
    }

    public void cargarCategorias() {
        listaCategorias = categoriaService.listarTodas();
    }

    public void guardarCategoria() {
        try {
            // Evaluamos si es nueva (no tiene ID) o si se está editando
            if (categoriaActual.getIdCategoria() == null) {
                categoriaService.crear(categoriaActual.getNombre(), categoriaActual.getDescripcion());
            } else {
                categoriaService.actualizar(categoriaActual.getIdCategoria(), categoriaActual.getNombre(), categoriaActual.getDescripcion());
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Categoría guardada correctamente."));
            categoriaActual = new Categoria();
            cargarCategorias();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar: " + e.getMessage()));
        }
    }

    public void prepararEdicion(Categoria cat) {
        this.categoriaActual = cat;
    }

    public void cambiarEstado(Categoria cat) {
        categoriaService.cambiarEstado(cat.getIdCategoria(), !cat.isActivo());
        cargarCategorias();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado actualizado."));
    }

    public Categoria getCategoriaActual() { return categoriaActual; }
    public void setCategoriaActual(Categoria categoriaActual) { this.categoriaActual = categoriaActual; }
    public List<Categoria> getListaCategorias() { return listaCategorias; }
}