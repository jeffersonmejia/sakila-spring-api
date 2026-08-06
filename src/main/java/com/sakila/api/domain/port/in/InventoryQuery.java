package com.sakila.api.domain.port.in;

import com.sakila.api.domain.model.Availability;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;

public interface InventoryQuery {

    PageResult<Inventory> listInventory(PageQuery query);

    Inventory getInventory(Integer id);

    PageResult<Inventory> inventoryByFilm(Integer filmId, PageQuery query);

    Availability availability(Integer filmId);
}
