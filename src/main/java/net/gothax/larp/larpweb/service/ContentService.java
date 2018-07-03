package net.gothax.larp.larpweb.service;

import net.gothax.larp.larpweb.model.Content;
import net.gothax.larp.larpweb.persistence.ContentRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContentService {
    @Resource
    private ContentRepository contentRepository;

    public Content getContent() {
        List<Content> c = contentRepository.findAll();
        if(c.size() > 0)
            return c.get(0);
        else
            return null;
    }

    public void saveContent(Content content) {
        contentRepository.save(content);
    }
}
