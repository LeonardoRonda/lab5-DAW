package com.tecsup.aspect;

import com.tecsup.exception.ForbiddenException;
import com.tecsup.exception.UnauthorizedException;
import com.tecsup.model.Producto;
import com.tecsup.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Aspect
@Component
public class AuditoriaAspect {

    private static final Map<String, String> USUARIOS = Map.of(
            "Ricardo", "ADMIN",
            "Ana", "USER",
            "Luis", "USER"
    );

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private HttpServletRequest request;

    private void validarUsuario() {
        String usuario = request.getHeader("Usuario");
        String rolHeader = request.getHeader("Rol");

        if (usuario == null || usuario.isBlank() || rolHeader == null || rolHeader.isBlank()) {
            throw new UnauthorizedException("Debe enviar Usuario y Rol");
        }

        String rolReal = USUARIOS.get(usuario);
        if (rolReal == null || !rolReal.equals(rolHeader)) {
            throw new ForbiddenException("No tiene permisos");
        }
    }

    private void validarRol(String... rolesPermitidos) {
        validarUsuario();
        String rol = request.getHeader("Rol");

        for (String permitido : rolesPermitidos) {
            if (permitido.equals(rol)) {
                return;
            }
        }

        throw new ForbiddenException("Acceso denegado");
    }

    private String obtenerUsuario() {
        String usuario = request.getHeader("Usuario");
        String rol = USUARIOS.get(usuario);
        return rol == null ? "DESCONOCIDO" : usuario + " (" + rol + ")";
    }

    @Before("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void validarCrear() {
        validarRol("ADMIN", "USER");
    }

    @Before("execution(* com.tecsup.service.ProductoService.listar(..))")
    public void validarListar() {
        validarRol("ADMIN", "USER");
    }

    @Before("execution(* com.tecsup.service.ProductoService.actualizar(..))")
    public void validarActualizar() {
        validarRol("ADMIN");
    }

    @Before("execution(* com.tecsup.service.ProductoService.eliminar(..))")
    public void validarEliminar() {
        validarRol("ADMIN");
    }

    @Before("execution(* com.tecsup.service.ProductoService.obtener(..)) || execution(* com.tecsup.service.ProductoService.buscarPorNombre(..))")
    public void validarConsultaAdmin() {
        validarRol("ADMIN");
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void auditarGuardar(JoinPoint joinPoint) {
        Producto producto = (Producto) joinPoint.getArgs()[0];
        auditoriaService.registrar(
                "CREAR",
                joinPoint.getSignature().getName(),
                "Se creó producto con ID: " + producto.getId(),
                obtenerUsuario()
        );
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.actualizar(..))")
    public void auditarActualizar(JoinPoint joinPoint) {
        Producto producto = (Producto) joinPoint.getArgs()[0];
        auditoriaService.registrar(
                "ACTUALIZAR",
                joinPoint.getSignature().getName(),
                "Se actualizó producto con ID: " + producto.getId(),
                obtenerUsuario()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.tecsup.service.ProductoService.listar(..))",
            returning = "productos"
    )
    public void auditarListar(JoinPoint joinPoint, List<Producto> productos) {
        auditoriaService.registrar(
                "LISTAR",
                joinPoint.getSignature().getName(),
                "Cantidad de productos: " + productos.size(),
                obtenerUsuario()
        );
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.eliminar(..))")
    public void auditarEliminar(JoinPoint joinPoint) {
        auditoriaService.registrar(
                "ELIMINAR",
                joinPoint.getSignature().getName(),
                "Se eliminó producto ID: " + joinPoint.getArgs()[0],
                obtenerUsuario()
        );
    }
}
