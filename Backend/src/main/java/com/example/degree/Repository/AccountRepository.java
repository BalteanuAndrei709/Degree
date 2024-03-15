package com.example.degree.Repository;


import com.example.degree.Entity.Account;
import com.example.degree.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);

    Boolean existsByUsernameAndActivatedIsNull(String username);

    Account getAccountById(Integer accountId);

    Long countAccountsByRole(Role role);


}
