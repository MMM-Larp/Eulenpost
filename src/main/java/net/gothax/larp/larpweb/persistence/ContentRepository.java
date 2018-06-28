package net.gothax.larp.larpweb.persistence;

import net.gothax.larp.larpweb.model.Content;
import net.gothax.larp.larpweb.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {}
