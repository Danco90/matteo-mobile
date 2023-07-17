package com.example.matteomobile.repos;


import com.example.matteomobile.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
}
