package com.example.ProyectoFinalTurnos.features.foto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Long> {

    Optional<FotoEntity> findByTipoAndReferenciaId(TipoFoto tipo, Long referenciaId);

    @Transactional
    void deleteByTipoAndReferenciaId(TipoFoto tipo, Long referenciaId);
}
