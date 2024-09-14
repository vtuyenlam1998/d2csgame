package com.d2csgame.server.image;

import com.d2csgame.entity.Image;
import com.d2csgame.entity.enumration.EActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.actionId = :id AND i.actionType = :eActionType ")
    List<Image> findByActionId(@Param("id") Long id, @Param("eActionType") EActionType eActionType);

    @Query("SELECT i FROM Image i WHERE i.actionId = :id AND i.actionType = :eActionType AND i.isPrimary = true")
    Optional<Image> findByActionIdAndIsPrimary(Long id, EActionType eActionType);

    @Transactional
    @Modifying
    @Query("UPDATE Image i SET i.isPrimary=:isPrimary WHERE i.id=:imageId")
    void updatePrimary(Long imageId, boolean isPrimary);
}
