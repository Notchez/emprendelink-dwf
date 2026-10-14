package emprendelink.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "emprendimientos")
public class EmprendimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprendimiento")
    private Integer idEmprendimiento;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "id_propietario",
            nullable = false
    )
    private UsuarioEntity propietario;

    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    @JoinColumn(
            name = "id_plan",
            nullable = false
    )
    private PlanEntity plan;

    @NotBlank
    @Size(max = 120)
    @Column(
            name = "nombre",
            nullable = false,
            length = 120
    )
    private String nombre;

    @Column(
            name = "descripcion",
            columnDefinition = "TEXT"
    )
    private String descripcion;

    @Size(max = 120)
    @Column(
            name = "contacto",
            length = 120
    )
    private String contacto;

    @Column(
            name = "activo",
            nullable = false
    )
    private boolean activo;

    @NotNull
    @Column(
            name = "fecha_registro",
            nullable = false
    )
    private LocalDateTime fechaRegistro;

    public EmprendimientoEntity() {
    }

    public Integer getIdEmprendimiento() {
        return idEmprendimiento;
    }

    public void setIdEmprendimiento(
            Integer idEmprendimiento) {

        this.idEmprendimiento =
                idEmprendimiento;
    }

    public UsuarioEntity getPropietario() {
        return propietario;
    }

    public void setPropietario(
            UsuarioEntity propietario) {

        this.propietario =
                propietario;
    }

    public PlanEntity getPlan() {
        return plan;
    }

    public void setPlan(
            PlanEntity plan) {

        this.plan =
                plan;
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

    public String getContacto() {
        return contacto;
    }

    public void setContacto(
            String contacto) {

        this.contacto =
                contacto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(
            boolean activo) {

        this.activo =
                activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(
            LocalDateTime fechaRegistro) {

        this.fechaRegistro =
                fechaRegistro;
    }
}