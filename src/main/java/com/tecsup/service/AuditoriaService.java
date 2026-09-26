package com.tecsup.service;

import com.tecsup.model.AuditoriaLog;
import com.tecsup.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository repo;

    @Transactional
    public void registrar(String accion, String metodo, String detalle) {
        AuditoriaLog log = new AuditoriaLog(accion, metodo, detalle);
        repo.save(log);
    }
}
