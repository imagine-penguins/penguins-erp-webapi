package com.knackitsolutions.crm.imaginepenguins.dbservice.repository;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
