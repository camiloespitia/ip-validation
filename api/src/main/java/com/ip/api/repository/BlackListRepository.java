package com.ip.api.repository;

import com.ip.api.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cristian.espitia
 */
@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    BlackList getFirstByIpValue(String ip);

}
