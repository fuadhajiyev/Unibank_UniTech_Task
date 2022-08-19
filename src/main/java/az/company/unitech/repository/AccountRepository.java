package az.company.unitech.repository;

import az.company.unitech.entity.Account;
import az.company.unitech.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByUserIdAndStatus(Long userId, AccountStatus status);
    Optional<Account> findByAccountNumber(String accountNumber);
}
