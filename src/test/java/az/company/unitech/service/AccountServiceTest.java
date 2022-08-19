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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getActiveAccounts_Successfully() {
        Long USER_ID = 1L;
        Account account = new Account();
        List<Account> accounts = List.of(account);

        Mockito.when(accountRepository.findByUserIdAndStatus(USER_ID, AccountStatus.ACTIVE))
                .thenReturn(accounts);

        List<Account> activeAccounts = accountService.getActiveAccounts(USER_ID);
        assertThat(activeAccounts).isEqualTo(accounts);
    }


    @Test
    void transfer_whenAccountIsInvalid_throwAccountNotFoundException() {
        String fromAccountNumber = "invalidAccountNumber";
        String toAccountNumber = "2";
        BigDecimal amount = BigDecimal.ONE;
        TransferRequestModel transferRequestModel = new TransferRequestModel(fromAccountNumber, toAccountNumber, amount);

        Mockito.when(accountRepository.findByAccountNumber(fromAccountNumber))
                        .thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(transferRequestModel);
        });
    }

    @Test
    void transfer_whenAccountIsOutOfBalance_throwOutOfBalanceException() {
        String fromAccountNumber = "1";
        String toAccountNumber = "2";
        BigDecimal transferAmount = BigDecimal.TEN;
        TransferRequestModel transferRequestModel = new TransferRequestModel(fromAccountNumber, toAccountNumber, transferAmount);

        Account account = new Account(fromAccountNumber, BigDecimal.ONE, 1L, AccountStatus.ACTIVE);
        Mockito.when(accountRepository.findByAccountNumber(fromAccountNumber))
                .thenReturn(Optional.of(account));
        Mockito.when(accountRepository.findByAccountNumber(toAccountNumber))
                .thenReturn(Optional.of(account));

        Assertions.assertThrows(OutOfBalanceException.class, () -> {
            accountService.transfer(transferRequestModel);
        });
    }

    @Test
    void transfer_whenSameAccount_throwInvalidArgumentException() {
        String fromAccountNumber = "1";
        String toAccountNumber = "1";
        BigDecimal transferAmount = BigDecimal.TEN;
        TransferRequestModel transferRequestModel = new TransferRequestModel(fromAccountNumber, toAccountNumber, transferAmount);

        Account account = new Account(fromAccountNumber, BigDecimal.ONE, 1L, AccountStatus.ACTIVE);
        Mockito.when(accountRepository.findByAccountNumber(fromAccountNumber))
                .thenReturn(Optional.of(account));

        Assertions.assertThrows(InvalidArgumentException.class, () -> {
            accountService.transfer(transferRequestModel);
        });
    }

    @Test
    void transfer_whenAccountIsDeactivated_throwDeactiveAccountException() {
        String fromAccountNumber = "1";
        String toAccountNumber = "2";
        BigDecimal transferAmount = BigDecimal.TEN;
        TransferRequestModel transferRequestModel = new TransferRequestModel(fromAccountNumber, toAccountNumber, transferAmount);

        Account account = new Account(fromAccountNumber, BigDecimal.ONE, 1L, AccountStatus.DEACTIVE);
        Mockito.when(accountRepository.findByAccountNumber(fromAccountNumber))
                .thenReturn(Optional.of(account));

        Mockito.when(accountRepository.findByAccountNumber(toAccountNumber))
                .thenReturn(Optional.of(account));

        Assertions.assertThrows(DeactiveAccountException.class, () -> {
            accountService.transfer(transferRequestModel);
        });
    }

    @Test
    void transfer_Successfully() {
        String fromAccountNumber = "1";
        String toAccountNumber = "2";
        BigDecimal transferAmount = BigDecimal.ONE;
        TransferRequestModel transferRequestModel = new TransferRequestModel(fromAccountNumber, toAccountNumber, transferAmount);

        Account fromAccount = new Account(fromAccountNumber, BigDecimal.TEN, 1L, AccountStatus.ACTIVE);
        Account toAccount = new Account(fromAccountNumber, BigDecimal.TEN, 1L, AccountStatus.ACTIVE);

        Mockito.when(accountRepository.findByAccountNumber(fromAccountNumber))
                .thenReturn(Optional.of(fromAccount));
        Mockito.when(accountRepository.findByAccountNumber(toAccountNumber))
                .thenReturn(Optional.of(toAccount));

        TransferStatus transfer = accountService.transfer(transferRequestModel);

        assertThat(transfer).isEqualTo(TransferStatus.SUCCEEDED);
    }
}