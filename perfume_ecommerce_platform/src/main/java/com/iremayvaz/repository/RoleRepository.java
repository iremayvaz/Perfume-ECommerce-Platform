package com.iremayvaz.repository;

import com.iremayvaz.model.entity.Role;
import com.iremayvaz.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    // JpaRepository içindeki metotlar yetmediğinde kendi özel metotlarımızı buraya yazacağız!!!

    /*
     * HQL : sınıfın ismi ve değişken isimleri kullanılarak sorgular yazılır. (nativeQuery = false)
     * SQL : db'deki tablo(sema_adi.tablo_adi) ve sütun isimleri kullanılır. (nativeQuery = true)
     * */

    @Query(value = "FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(RoleName roleName);
}
