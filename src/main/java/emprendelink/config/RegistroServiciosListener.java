package emprendelink.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import emprendelink.dao.CategoriaDAO;
import emprendelink.dao.EmprendimientoDAO;
import emprendelink.dao.PedidoDAO;
import emprendelink.dao.PublicacionDAO;
import emprendelink.dao.RolDAO;
import emprendelink.dao.UsuarioDAO;

import emprendelink.dao.jdbc.JdbcCategoriaDAO;
import emprendelink.dao.jdbc.JdbcEmprendimientoDAO;
import emprendelink.dao.jdbc.JdbcPedidoDAO;
import emprendelink.dao.jdbc.JdbcPublicacionDAO;
import emprendelink.dao.jdbc.JdbcRolDAO;
import emprendelink.dao.jdbc.JdbcUsuarioDAO;

import emprendelink.service.impl.AutenticacionServiceImpl;
import emprendelink.service.impl.CategoriaServiceImpl;
import emprendelink.service.impl.EmprendimientoServiceImpl;
import emprendelink.service.impl.PedidoServiceImpl;
import emprendelink.service.impl.PublicacionServiceImpl;
import emprendelink.service.impl.UsuarioServiceImpl;

@WebListener
public class RegistroServiciosListener
        implements ServletContextListener {

    @Override
    public void contextInitialized(
            ServletContextEvent evento) {

        ServletContext contexto =
                evento.getServletContext();

        UsuarioDAO usuarioDAO = new JdbcUsuarioDAO();
        RolDAO rolDAO = new JdbcRolDAO();

        EmprendimientoDAO emprendimientoDAO =
                new JdbcEmprendimientoDAO();

        CategoriaDAO categoriaDAO =
                new JdbcCategoriaDAO();

        PublicacionDAO publicacionDAO =
                new JdbcPublicacionDAO();

        PedidoDAO pedidoDAO = new JdbcPedidoDAO();

        contexto.setAttribute(
                "autenticacionService",
                new AutenticacionServiceImpl(usuarioDAO)
        );

        contexto.setAttribute(
                "usuarioService",
                new UsuarioServiceImpl(
                        usuarioDAO,
                        rolDAO
                )
        );

        contexto.setAttribute(
                "emprendimientoService",
                new EmprendimientoServiceImpl(
                        emprendimientoDAO,
                        usuarioDAO
                )
        );

        contexto.setAttribute(
                "categoriaService",
                new CategoriaServiceImpl(categoriaDAO)
        );

        contexto.setAttribute(
                "publicacionService",
                new PublicacionServiceImpl(
                        publicacionDAO,
                        emprendimientoDAO,
                        categoriaDAO,
                        usuarioDAO
                )
        );

        contexto.setAttribute(
                "pedidoService",
                new PedidoServiceImpl(
                        pedidoDAO,
                        usuarioDAO,
                        emprendimientoDAO,
                        publicacionDAO
                )
        );
    }
}