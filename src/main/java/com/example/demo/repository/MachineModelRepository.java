package com.example.demo.repository;

import com.example.demo.model.MachineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineModelRepository extends JpaRepository<MachineModel, Long> {
    MachineModel findByMachineModel(String machineModel);
}