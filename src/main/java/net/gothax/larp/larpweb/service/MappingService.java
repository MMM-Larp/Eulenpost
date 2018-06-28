package net.gothax.larp.larpweb.service;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.model.Mapping;
import net.gothax.larp.larpweb.persistence.MappingRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

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

    /**
     * This makes sure, that neither first nor second receiver are equal to sender or to themselves.
     */
    public boolean isShuffleCorrect(List<Entry> senders, List<Entry> receiverOne, List<Entry> receiverTwo) {
        for(int i = 0; i < senders.size(); i++)
            if(senders.get(i) == receiverOne.get(i) ||
               senders.get(i) == receiverTwo.get(i) ||
               receiverOne.get(i) == receiverTwo.get(i))
                return false;
        return true;
    }

    public void saveMapping(List<Entry> senders, List<Entry> receiverOne, List<Entry> receiverTwo) {
        for(int i = 0; i < senders.size(); i++) {
            Mapping m = new Mapping();
            m.setSender(senders.get(i));
            m.setReceiverOne(receiverOne.get(i));
            m.setReceiverTwo(receiverTwo.get(i));

            mappingRepository.save(m);
        }
    }
}
