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

    public int findAllSavginsAccountFetchJoin() {
        return customerRepository.findAllFetchJoin().size();
    }

    public int findAllBatchSize() {
        List<Customer> customers = customerRepository.findAll();
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(i);
            customers.get(i).getSavingsAccount2().size();
        }
        return customers.size();
    }

    public int findAllSubSelect() {
        List<Customer> customers = customerRepository.findAll();
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(i);
            customers.get(i).getSavingsAccount3().size();
        }
        return customers.size();
    }

    public void insertData() {
        System.out.println("insert data");
        List<SavingsAccount> accounts = new ArrayList<SavingsAccount>();
        for (int i = 1; i <= 10; i++) {
            Customer customer = Customer.builder().name(i + "번 사람").build();
            customerRepository.save(customer);
            for (int j = 1; j <= 10; j++) {
                accounts.add(SavingsAccount.builder().name(j + "번 계좌").customer(customer).build());
            }
        }
        savingsAccountRepository.saveAll(accounts);
    }
}
