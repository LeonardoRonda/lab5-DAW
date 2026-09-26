package com.tecsup.aspect;

import com.tecsup.service.AuditoriaService;
import com.tecsup.model.Producto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired
    private AuditoriaService auditoriaService;

    @AfterReturning("execution(* com.tecsup.service.ProductoService.guardar(..))")
    public void auditarGuardar(JoinPoint joinPoint) {
        Producto producto = (Producto) joinPoint.getArgs()[0];

        auditoriaService.registrar(
                "CREAR",
                joinPoint.getSignature().getName(),
                "Se creó producto con ID: " + producto.getId()
        );
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.actualizar(..))")
    public void auditarActualizar(JoinPoint joinPoint) {
        Producto producto = (Producto) joinPoint.getArgs()[0];

        auditoriaService.registrar(
                "ACTUALIZAR",
                joinPoint.getSignature().getName(),
                "Se actualizó producto con ID: " + producto.getId()
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
                "Se listaron " + productos.size() + " productos"
        );
    }

    @AfterReturning("execution(* com.tecsup.service.ProductoService.eliminar(..))")
    public void auditarEliminar(JoinPoint joinPoint) {

        auditoriaService.registrar(
                "ELIMINAR",
                joinPoint.getSignature().getName(),
                "Se eliminó producto con ID: " + joinPoint.getArgs()[0]
        );
    }
}
