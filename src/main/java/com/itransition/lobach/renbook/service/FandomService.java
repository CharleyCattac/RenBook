package com.itransition.lobach.renbook.service;

import com.itransition.lobach.renbook.entity.Fandom;
import com.itransition.lobach.renbook.enums.FandomType;
import com.itransition.lobach.renbook.repository.FandomRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FandomService {

    @Autowired
    private FandomRepository fandomRepository;

    public Fandom saveFandom(String name, String codeName, String type) {
        if (name != null && type != null) {
            if (!name.isBlank() && !type.isBlank()) {
                if (EnumUtils.isValidEnum(FandomType.class, type.toUpperCase())
                        && !type.equals(FandomType.FANDOM.name())) {
                    return fandomRepository.save(Fandom.builder()
                            .name(name)
                            .codeName(codeName)
                            .type(type.toUpperCase())
                            .build());
                }
            }
        }
        return null;
    }

    public List<Fandom> saveFandomList(List<String> fandomNames, List<String> fandomTypes) {
        List<Fandom> fandoms = new ArrayList<>();
        if (fandomNames.size() == fandomTypes.size()) {
            String name;
            String type;
            Fandom fandom;
            for (int i = 0; i < fandomNames.size(); i++) {
                name = fandomNames.get(i);
                fandom = fandomRepository.findByName(name);
                if (fandom == null) {
                    type = fandomTypes.get(i);
                    fandom = saveFandom(name, name, type);
                }
                if (fandom != null) {
                    fandoms.add(fandom);
                }
            }
        }
        return fandoms;
    }

    public List<Fandom> findAll() {
        return fandomRepository.getAllByOrderByNameAsc();
    }

    public List<Fandom> findAllByType(String type) {
        return fandomRepository.getAllByTypeOrderByNameAsc(type);
    }
}
