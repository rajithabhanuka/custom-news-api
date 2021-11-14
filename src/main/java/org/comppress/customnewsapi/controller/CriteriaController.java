package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.CriteriaDto;
import org.comppress.customnewsapi.service.criteria.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/criteria")
public class CriteriaController {

    private final CriteriaService criteriaService;

    @Autowired
    public CriteriaController(CriteriaService criteriaService) {
        this.criteriaService = criteriaService;
    }

    @GetMapping
    public ResponseEntity<List<CriteriaDto>> getCriteria(){
        return ResponseEntity.ok().body(criteriaService.getCriteria());
    }


}
