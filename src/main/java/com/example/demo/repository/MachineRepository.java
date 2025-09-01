package com.example.demo.repository;

import com.example.demo.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    Optional<Machine> findByCcn(String ccn);

    @Query("SELECT m.liveStatus, COUNT(m) FROM Machine m GROUP BY m.liveStatus")
    List<Object[]> countByLiveStatus();
}
