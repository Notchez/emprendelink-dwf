package emprendelink.web.jsf.converter;

import emprendelink.model.enums.TipoRol;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("tipoRolConverter")
public class TipoRolConverter
        implements Converter<TipoRol> {

    @Override
    public TipoRol getAsObject(
            FacesContext contexto,
            UIComponent componente,
            String valor) {

        if (valor == null
                || valor.isBlank()) {

            return null;
        }

        try {

            return TipoRol.valueOf(valor);

        } catch (IllegalArgumentException e) {

            throw new ConverterException(
                    "El rol seleccionado no es válido.",
                    e
            );
        }
    }

    @Override
    public String getAsString(
            FacesContext contexto,
            UIComponent componente,
            TipoRol valor) {

        if (valor == null) {
            return "";
        }

        return valor.name();
    }
}