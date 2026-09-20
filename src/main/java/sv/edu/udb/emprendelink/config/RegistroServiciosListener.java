package sv.edu.udb.emprendelink.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import sv.edu.udb.emprendelink.dao.CategoriaDAO;
import sv.edu.udb.emprendelink.dao.EmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.PedidoDAO;
import sv.edu.udb.emprendelink.dao.PublicacionDAO;
import sv.edu.udb.emprendelink.dao.RolDAO;
import sv.edu.udb.emprendelink.dao.UsuarioDAO;

import sv.edu.udb.emprendelink.dao.jdbc.JdbcCategoriaDAO;
import sv.edu.udb.emprendelink.dao.jdbc.JdbcEmprendimientoDAO;
import sv.edu.udb.emprendelink.dao.jdbc.JdbcPedidoDAO;
import sv.edu.udb.emprendelink.dao.jdbc.JdbcPublicacionDAO;
import sv.edu.udb.emprendelink.dao.jdbc.JdbcRolDAO;
import sv.edu.udb.emprendelink.dao.jdbc.JdbcUsuarioDAO;

import sv.edu.udb.emprendelink.service.impl.AutenticacionServiceImpl;
import sv.edu.udb.emprendelink.service.impl.CategoriaServiceImpl;
import sv.edu.udb.emprendelink.service.impl.EmprendimientoServiceImpl;
import sv.edu.udb.emprendelink.service.impl.PedidoServiceImpl;
import sv.edu.udb.emprendelink.service.impl.PublicacionServiceImpl;
import sv.edu.udb.emprendelink.service.impl.UsuarioServiceImpl;

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