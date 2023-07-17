package com.example.matteomobile.repos;

import com.example.matteomobile.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{
}
