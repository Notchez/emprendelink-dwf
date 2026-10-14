package emprendelink.web.jsf.converter;

import emprendelink.model.Categoria;
import emprendelink.dao.CategoriaDAO;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "categoriaConverter", managed = true)
public class CategoriaConverter implements Converter<Categoria> {

    @Override
    public Categoria getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        CategoriaDAO dao = CDI.current().select(CategoriaDAO.class).get();
        // Usamos .orElse(null) para sacar la Categoria del Optional
        return dao.buscarPorId(Integer.valueOf(value)).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Categoria value) {
        if (value == null || value.getIdCategoria() == null) {
            return "";
        }
        return String.valueOf(value.getIdCategoria());
    }
}