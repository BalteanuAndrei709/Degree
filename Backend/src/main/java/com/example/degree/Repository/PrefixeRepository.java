package com.example.degree.Repository;

import com.example.degree.Entity.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixeRepository extends JpaRepository<Prefix,Integer> {

    Prefix getPrefixByName(String name);
}
