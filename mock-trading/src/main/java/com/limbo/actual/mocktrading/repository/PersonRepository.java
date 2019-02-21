package com.limbo.actual.mocktrading.repository;

import com.limbo.actual.mocktrading.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends JpaRepository<Person,Long> {

    /**
     * 执行用户的扣款
     * @param id 扣款者id
     * @param price 消费价格
     * @return 修改的数据条数 0：扣款失败， 1：成功
     */
    @Modifying
    @Query("update Person set deposit = deposit - ?2 where id = ?1 and deposit >= ?2")
    int pay(Long id, Long price);

    /**
     * 执行用户的收款
     * @param id 受款者id
     * @param price 所得收入
     * @return 修改的数据条数 0：失败， 1：成功
     */
    @Modifying
    @Query("update Person set deposit = deposit + ?2 where id = ?1")
    int earn(Long id, Long price);
}
