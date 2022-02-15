package org.comppress.customnewsapi.service.criteria;

import org.comppress.customnewsapi.dto.CriteriaDto;
import org.comppress.customnewsapi.entity.Criteria;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.CriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriteriaServiceImpl implements CriteriaService {

    private final CriteriaRepository criteriaRepository;
    private final MapstructMapper mapstructMapper;

    @Autowired
    public CriteriaServiceImpl(CriteriaRepository criteriaRepository, MapstructMapper mapstructMapper) {
        this.criteriaRepository = criteriaRepository;
        this.mapstructMapper = mapstructMapper;
    }

    @Override
    public List<CriteriaDto> getCriteria() {
        List<CriteriaDto> criteriaDtoList = new ArrayList<>();
        for(Criteria criteria:criteriaRepository.findAll()){
            criteriaDtoList.add(mapstructMapper.criteriaToCriteriaDto(criteria));
        }
        return criteriaDtoList;
    }
}
