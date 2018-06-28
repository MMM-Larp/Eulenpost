package net.gothax.larp.larpweb.service;

import edu.emory.mathcs.backport.java.util.Collections;
import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.persistence.EntryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EntryService {
    @Resource
    private EntryRepository entryRepository;

    public List<Entry> getShuffledEntries() {
        List<Entry> entries = entryRepository.findAll();
        Collections.shuffle(entries);

        return entries;
    }

    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    public void saveEntry(Entry entry) {
        entryRepository.save(entry);
    }

    public void clearEntries() {
        entryRepository.deleteAll();
    }
 }
