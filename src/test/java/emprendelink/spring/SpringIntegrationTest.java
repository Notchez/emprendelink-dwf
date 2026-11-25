package emprendelink.spring;

import emprendelink.model.Rol;
import emprendelink.model.Usuario;
import emprendelink.spring.config.SpringConfig;
import emprendelink.spring.security.AccessPolicy;
import emprendelink.spring.security.RouteRules.WebDecision;
import emprendelink.spring.rest.ApiErrorPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static emprendelink.model.enums.TipoRol.*;
import static org.junit.jupiter.api.Assertions.*;

class SpringIntegrationTest {
    @Test void contextAndRolePolicyAreFunctional() {
        try (var context = new AnnotationConfigApplicationContext(SpringConfig.class)) {
            var policy = context.getBean(AccessPolicy.class);
            assertSame(policy, context.getBean(AccessPolicy.class));
            var client = new Usuario(); client.setRol(new Rol(ROLE_CLIENTE));
            var seller = new Usuario(); seller.setRol(new Rol(ROLE_EMPRENDEDOR));
            var admin = new Usuario(); admin.setRol(new Rol(ROLE_ADMIN));
            assertTrue(policy.publicApi("GET", "publicaciones"));
            assertFalse(policy.publicApi("POST", "health"));
            assertTrue(policy.allowedApi("POST", "pedidos/checkout", client));
            assertFalse(policy.allowedApi("POST", "pedidos/checkout", seller));
            assertTrue(policy.allowedApi("PUT", "pedidos/1/estado", seller));
            assertFalse(policy.allowedApi("GET", "pedidos", admin));
            assertFalse(policy.allowedApi("GET", "unknown", admin));
            assertTrue(policy.needsCsrf("POST"));
            assertFalse(policy.needsCsrf("GET"));
            assertEquals(WebDecision.LOGIN, policy.web("/admin/inicio.xhtml", null));
            assertEquals(WebDecision.FORBIDDEN, policy.web("/admin/inicio.xhtml", client));
            assertEquals(WebDecision.ALLOW, policy.web("/admin/inicio.xhtml", admin));
            assertEquals(WebDecision.ENTREPRENEUR_HOME, policy.web("/catalogo/index.xhtml", seller));
            assertEquals(WebDecision.ALLOW, policy.web("/catalogo/index.xhtml", null));
            client.setActivo(false);
            assertFalse(policy.allowedApi("POST", "pedidos/checkout", client));
            var error = context.getBean(ApiErrorPolicy.class).describe(404);
            assertEquals(404, error.getStatus());
            assertEquals("Recurso no encontrado.", error.getMessage());
        }
    }
}
