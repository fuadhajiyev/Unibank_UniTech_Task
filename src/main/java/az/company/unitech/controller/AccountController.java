package az.company.unitech.controller;

import az.company.unitech.entity.Account;
import az.company.unitech.model.TransferRequestModel;
import az.company.unitech.model.TransferStatus;
import az.company.unitech.service.AccountService;
import az.company.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Account>> getMyActiveAccounts() {
        List<Account> accounts = accountService.getActiveAccounts(userService.getAuthUser().getId());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferStatus> transfer(@Valid @RequestBody TransferRequestModel request) {
        return new ResponseEntity<>(accountService.transfer(request), HttpStatus.OK);
    }
}
