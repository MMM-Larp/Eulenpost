package net.gothax.larp.larpweb.persistence;

import net.gothax.larp.larpweb.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {}
