package net.gothax.larp.larpweb.persistence;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.model.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingRepository extends JpaRepository<Mapping, Long> {}
