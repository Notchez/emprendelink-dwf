package emprendelink.web.jsf.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("nombreCategoriaValidator")
public class NombreCategoriaValidator implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre es obligatorio."));
        }
        if (value.length() < 3 || value.length() > 50) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre debe tener entre 3 y 50 caracteres."));
        }
    }
}