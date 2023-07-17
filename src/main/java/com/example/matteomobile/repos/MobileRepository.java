package com.example.matteomobile.repos;

import com.example.matteomobile.models.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileRepository extends JpaRepository<Mobile, Long>
{
}
