package emprendelink.web.jsf.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("contrasenaValidator")
public class ContrasenaValidator
        implements Validator<String> {

    @Override
    public void validate(
            FacesContext contexto,
            UIComponent componente,
            String valor)
            throws ValidatorException {

        if (valor == null
                || valor.length() < 8) {

            throw new ValidatorException(
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Contraseña inválida",
                            "La contraseña debe tener al menos 8 caracteres."
                    )
            );
        }
    }
}