package com.iremayvaz.repository;

import com.iremayvaz.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser_Id(Long userId);
    Optional<Address> findByIdAndUser_Id(Long addressId, Long userId);

}
