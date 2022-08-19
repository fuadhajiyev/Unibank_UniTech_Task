package az.company.unitech.service;

import az.company.unitech.entity.Account;
import az.company.unitech.entity.AccountStatus;
import az.company.unitech.exception.AccountNotFoundException;
import az.company.unitech.exception.DeactiveAccountException;
import az.company.unitech.exception.InvalidArgumentException;
import az.company.unitech.exception.OutOfBalanceException;
import az.company.unitech.model.TransferRequestModel;
import az.company.unitech.model.TransferStatus;
import az.company.unitech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public List<Account> getActiveAccounts(Long userId) {
        return accountRepository.findByUserIdAndStatus(userId, AccountStatus.ACTIVE);
    }

    /**
     * I assume each account can belong to only one user.
     * If an account belonged to multiple users,
     * I would need to consider parallel transfer transaction to make sure consistency.
     *
     * In that case, we would need to use locking ***
     */
    @Transactional
    public TransferStatus transfer(TransferRequestModel transferModel) {
        Account fromAccount = getOrThrowExceptionIfAccountNotFound(transferModel.getFromAccountNumber());
        Account toAccount = getOrThrowExceptionIfAccountNotFound(transferModel.getToAccountNumber());
        throwExceptionIfSameAccount(transferModel.getFromAccountNumber(), transferModel.getToAccountNumber());
        throwExceptionIfAccountIsDeactivated(transferModel.getFromAccountNumber());
        throwExceptionIfAccountIsDeactivated(transferModel.getToAccountNumber());
        throwExceptionIfOutOfBalance(transferModel.getFromAccountNumber(), transferModel.getAmount());

        return processTransfer(fromAccount, toAccount, transferModel.getAmount());
    }

    private TransferStatus processTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        fromAccount.setAmount(fromAccount.getAmount().subtract(amount));
        accountRepository.save(fromAccount);
        toAccount.setAmount(toAccount.getAmount().add(amount));
        accountRepository.save(toAccount);

        return TransferStatus.SUCCEEDED;
    }

    private Account getOrThrowExceptionIfAccountNotFound(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);

        if (account.isEmpty()) {
            throw new AccountNotFoundException("No valid account");
        }

        return account.get();
    }

    private void throwExceptionIfAccountIsDeactivated(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);

        if (account.isEmpty()) {
            throw new AccountNotFoundException("No valid account");
        }

        if (account.get().getStatus() == AccountStatus.DEACTIVE) {
            throw new DeactiveAccountException("The given account is not active");
        }
    }

    private void throwExceptionIfSameAccount(String fromAccountNumber, String toAccountNumber) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new InvalidArgumentException("The transfer cannot be applied to the same account");
        }
    }

    private void throwExceptionIfOutOfBalance(String fromAccountNumber, BigDecimal amount) {
        Optional<Account> account = accountRepository.findByAccountNumber(fromAccountNumber);

        if (account.isEmpty()) {
            throw new AccountNotFoundException("No valid account");
        }

        if (account.get().getAmount().compareTo(amount) < 0) {
            throw new OutOfBalanceException("There's no available funds in balance");
        }
    }

}
