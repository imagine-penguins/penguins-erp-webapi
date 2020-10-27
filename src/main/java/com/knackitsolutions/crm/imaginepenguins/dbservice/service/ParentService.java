package com.knackitsolutions.crm.imaginepenguins.dbservice.service;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Institute;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Parent;
import com.knackitsolutions.crm.imaginepenguins.dbservice.exception.ParentNotFoundException;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
    @Autowired
    ParentRepository parentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Parent findById(Long id){
        return parentRepository.findById(id)
                .orElseThrow(()->new ParentNotFoundException(id));
    }

    public List<Parent> parents(){
        return parentRepository.findAll();
    }

    public Parent newParent(Parent parent) {
        parent.setPassword(passwordEncoder.encode(parent.getPassword()));
        return parentRepository.save(parent);
    }

    public List<Parent> listParentWith(Integer instituteId, Optional<Boolean> active, Pageable pageable) {
        Specification<Parent> specification = parentsByInstituteId(instituteId);
        active
                .map(ParentService::parentsByActive)
                .ifPresent(specification::and);
        return parentRepository.findAll(specification, pageable).toList();
    }

    public List<Parent> listParentWith(Integer instituteId, Optional<Boolean> active) {
        Specification<Parent> specification = parentsByInstituteId(instituteId);
        active
                .map(ParentService::parentsByActive)
                .ifPresent(specification::and);
        return parentRepository.findAll(specification);
    }

    public static Specification<Parent> parentsByInstituteId(Integer instituteId) {
        return (root, query, criteriaBuilder) -> {
            Join<Parent, Institute> parentInstituteJoin = root
                    .join("students", JoinType.LEFT)
                    .join("instituteClassSection", JoinType.LEFT)
                    .join("instituteClass", JoinType.LEFT)
                    .join("institute", JoinType.LEFT);
            Predicate equalPredicate = criteriaBuilder.equal(parentInstituteJoin.get("id"), instituteId);
            return equalPredicate;
        };
    }

    public static Specification<Parent> parentsByActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("students", JoinType.LEFT).get("active"), active);
        };
    }
}
