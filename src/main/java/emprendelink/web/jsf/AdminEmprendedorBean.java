package emprendelink.web.jsf;

import emprendelink.model.Categoria;
import emprendelink.model.Plan;
import emprendelink.model.Usuario;
import emprendelink.service.AdminEmprendedorService;
import emprendelink.service.CategoriaService;
import emprendelink.service.PlanService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("adminEmprendedorBean")
@ViewScoped
public class AdminEmprendedorBean
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private AdminEmprendedorService adminEmprendedorService;

    @Inject
    private PlanService planService;

    @Inject
    private CategoriaService categoriaService;

    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;

    private String nombreNegocio;
    private String descripcionNegocio;
    private String contactoNegocio;
    private String logoUrl;

    private Integer idPlan;
    private Integer idCategoria;

    private List<Plan> planes =
            Collections.emptyList();

    private List<Categoria> categorias =
            Collections.emptyList();

    @PostConstruct
    public void inicializar() {

        planes =
                planService.listarActivos();

        categorias =
                categoriaService.listarActivas();

        if (!planes.isEmpty()) {

            idPlan =
                    planes.get(0)
                            .getIdPlan();
        }

        if (!categorias.isEmpty()) {

            idCategoria =
                    categorias.get(0)
                            .getIdCategoria();
        }
    }

    public void crear() {

        Usuario administrador =
                obtenerUsuarioSesion();

        if (administrador == null) {

            mensajeError(
                    "Sesión inválida."
            );

            return;
        }

        try {

            adminEmprendedorService
                    .crearEmprendedorConNegocio(
                            administrador
                                    .getIdUsuario(),
                            nombre,
                            apellido,
                            correo,
                            contrasena,
                            telefono,
                            nombreNegocio,
                            descripcionNegocio,
                            contactoNegocio,
                            logoUrl,
                            idPlan,
                            idCategoria
                    );

            limpiar();

            mensajeExito(
                    "La cuenta y el negocio fueron creados correctamente."
            );

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            mensajeError(
                    e.getMessage()
            );
        }
    }

    private Usuario obtenerUsuarioSesion() {

        Object valor =
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .get(
                                "usuarioAutenticado"
                        );

        return valor instanceof Usuario
                ? (Usuario) valor
                : null;
    }

    private void limpiar() {

        nombre = null;
        apellido = null;
        correo = null;
        contrasena = null;
        telefono = null;

        nombreNegocio = null;
        descripcionNegocio = null;
        contactoNegocio = null;
        logoUrl = null;

        idPlan =
                planes.isEmpty()
                        ? null
                        : planes.get(0)
                        .getIdPlan();

        idCategoria =
                categorias.isEmpty()
                        ? null
                        : categorias.get(0)
                        .getIdCategoria();
    }

    private void mensajeExito(
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Emprendedor creado",
                                mensaje
                        )
                );
    }

    private void mensajeError(
            String mensaje) {

        FacesContext
                .getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "No se pudo crear el emprendedor",
                                mensaje
                        )
                );
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(
            String nombreNegocio) {

        this.nombreNegocio =
                nombreNegocio;
    }

    public String getDescripcionNegocio() {
        return descripcionNegocio;
    }

    public void setDescripcionNegocio(
            String descripcionNegocio) {

        this.descripcionNegocio =
                descripcionNegocio;
    }

    public String getContactoNegocio() {
        return contactoNegocio;
    }

    public void setContactoNegocio(
            String contactoNegocio) {

        this.contactoNegocio =
                contactoNegocio;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(
            String logoUrl) {

        this.logoUrl =
                logoUrl;
    }

    public Integer getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(
            Integer idPlan) {

        this.idPlan =
                idPlan;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(
            Integer idCategoria) {

        this.idCategoria =
                idCategoria;
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }
}