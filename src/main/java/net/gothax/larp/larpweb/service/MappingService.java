package net.gothax.larp.larpweb.service;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.model.Mapping;
import net.gothax.larp.larpweb.persistence.MappingRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class MappingService {
    @Resource
    private MappingRepository mappingRepository;

    public void clearMappings() {
        mappingRepository.deleteAll();
    }

    public List<Mapping> getAllMappings() {
        return mappingRepository.findAll();
    }

    public void saveMapping(List<Entry> senders) {
        List<Entry> dayOne = new ArrayList<>(senders);
        List<Entry> dayTwo = new ArrayList<>(senders);

        while(isShuffleNotCorrect(senders, dayOne, dayTwo)) {
            Collections.shuffle(dayOne);
            Collections.shuffle(dayTwo);
        }

        for(int i = 0; i < senders.size(); i++) {
            Mapping m = new Mapping();
            m.setSender(senders.get(i));
            m.setReceivers(Arrays.asList(
                dayOne.get(i),
                dayTwo.get(i)
            ));
            mappingRepository.save(m);
        }
    }

    private boolean isShuffleNotCorrect(List<Entry> senders, List<Entry> dayOne, List<Entry> dayTwo) {
        for(int i = 0; i < senders.size(); i++) {
            if(senders.get(i) == dayOne.get(i) ||
               senders.get(i) == dayTwo.get(i) ||
               dayOne.get(i) == dayTwo.get(i))
                return true;
        }
        return false;
    }
}
