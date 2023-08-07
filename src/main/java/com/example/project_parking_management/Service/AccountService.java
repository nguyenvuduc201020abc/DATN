package com.example.project_parking_management.Service;

import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public Account saveAccount(Account account) {

        return accountRepository.save(account);
    }

    public void deleteAccount(Account account) {

        accountRepository.delete(account);
    }

    public boolean checkLoginUser(String username, String password) {
        Account user = accountRepository.findByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword()) && user.getRole().equals("user")) {
            return true;
        }
        return false;
    }

    //    public boolean checkLoginEmployee(String username, String password) {
//        Account user = accountRepository.findByUsername(username);
//        if (user != null && PasswordUtil.checkPassword(password, user.getPassword()) && user.getRole().equals("employee")) {
//            return true;
//        }
//        return false;
//    }
//    public boolean checkLoginManage(String username, String password) {
//        Account user = accountRepository.findByUsername(username);
//        if (user != null && PasswordUtil.checkPassword(password, user.getPassword()) && user.getRole().equals("manager")) {
//            return true;
//        }
//        return false;
//    }
    public Account findByUserName(String user_name) {
        return accountRepository.findByUsername(user_name);
    }
    public List<Account> getAll(){
        return accountRepository.findAll();
    }
    @Transactional
    public void deleteAccount(String username) {
        accountRepository.deleteById(username);
    }
}
