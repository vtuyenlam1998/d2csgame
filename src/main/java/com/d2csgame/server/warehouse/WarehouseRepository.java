package com.d2csgame.server.warehouse;

import com.d2csgame.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepository extends CrudRepository<Account, Long> {
}
