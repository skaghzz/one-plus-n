package com.example.oneplusn.service;

import java.util.ArrayList;
import java.util.List;

import com.example.oneplusn.domain.Customer;
import com.example.oneplusn.domain.SavingsAccount;
import com.example.oneplusn.repository.CustomerRepository;
import com.example.oneplusn.repository.SavingsAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {
    private final CustomerRepository customerRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    @Autowired
    public AccountService(CustomerRepository customerRepository, SavingsAccountRepository savingsAccountRepository) {
        this.customerRepository = customerRepository;
        this.savingsAccountRepository = savingsAccountRepository;
    }

    public void findAllSavginsAccountFetchJoin() {
        customerRepository.findAllFetchJoin();
    }

    public void insertData() {
        System.out.println("insert data");
        List<SavingsAccount> accounts = new ArrayList<SavingsAccount>();
        for (int i = 1; i <= 100; i++) {
            Customer customer = Customer.builder().name(i + "번 사람").build();
            customerRepository.save(customer);
            for (int j = 1; j <= 10; j++) {
                accounts.add(SavingsAccount.builder().name(j + "번 계좌").customer(customer).build());
            }
        }
        savingsAccountRepository.saveAll(accounts);
    }
}
