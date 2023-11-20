package de.webtech.backend.repository;

import de.webtech.backend.model.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

    List<Skin> findByUsername(String username);
}

