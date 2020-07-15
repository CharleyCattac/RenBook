package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Fandom;
import com.itransition.lobach.renbook.enums.FandomType;
import com.itransition.lobach.renbook.repository.FandomRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FandomService {

    @Autowired
    private FandomRepository fandomRepository;

    public Fandom saveFandom(String name, String codeName, String type) {
        if (name != null && codeName != null
                && EnumUtils.isValidEnum(FandomType.class, type.toUpperCase())) {
            return fandomRepository.save(Fandom.builder().
                    name(name).codeName(codeName).type(type)
                    .build());
        }
        return null;
    }

    public List<Fandom> findAllByType(String type) {
        return fandomRepository.getAllByTypeOrderByNameAsc(type);
    }
}
