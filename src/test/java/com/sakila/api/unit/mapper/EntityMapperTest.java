package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.out.persistence.entity.ActorEntity;
import com.sakila.api.adapter.out.persistence.entity.AddressEntity;
import com.sakila.api.adapter.out.persistence.entity.CategoryEntity;
import com.sakila.api.adapter.out.persistence.entity.CityEntity;
import com.sakila.api.adapter.out.persistence.entity.CountryEntity;
import com.sakila.api.adapter.out.persistence.entity.CustomerEntity;
import com.sakila.api.adapter.out.persistence.entity.FilmEntity;
import com.sakila.api.adapter.out.persistence.entity.InventoryEntity;
import com.sakila.api.adapter.out.persistence.entity.LanguageEntity;
import com.sakila.api.adapter.out.persistence.entity.RentalEntity;
import com.sakila.api.adapter.out.persistence.entity.StaffEntity;
import com.sakila.api.adapter.out.persistence.entity.StoreEntity;
import com.sakila.api.adapter.out.persistence.entity.UserEntity;
import com.sakila.api.adapter.out.persistence.mapper.EntityMapper;
import com.sakila.api.domain.model.Actor;
import com.sakila.api.domain.model.Address;
import com.sakila.api.domain.model.Category;
import com.sakila.api.domain.model.City;
import com.sakila.api.domain.model.Country;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.Film;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.Language;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.model.Staff;
import com.sakila.api.domain.model.Store;
import com.sakila.api.domain.model.User;

class EntityMapperTest {

    private final LanguageEntity language = new LanguageEntity(1, "English");
    private final CountryEntity country = new CountryEntity(1, "United States");
    private final CityEntity city = new CityEntity(2, "Austin", country);
    private final AddressEntity address = new AddressEntity(3, "47 MySakila Drive", "Alberta", "1400", "555", city);
    private final StoreEntity store = new StoreEntity(1, 1, address);
    private final StaffEntity staff = new StaffEntity(1, "MIKE", "HILL", "mike@sakila.com", "mike", true);
    private final FilmEntity film = new FilmEntity(5, "ACADEMY", "desc", 2006, language, (short) 6, BigDecimal.ONE,
            (short) 86, BigDecimal.TEN, "PG");
    private final CustomerEntity customer = new CustomerEntity(4, "MARY", "SMITH", "mary@sakila.com", true, store,
            address, LocalDate.of(2026, 1, 10));

    @Test
    void toActorMapsFields() {
        Actor actor = EntityMapper.toActor(new ActorEntity(7, "PENELOPE", "GUINESS"));

        assertEquals(7, actor.id());
        assertEquals("PENELOPE", actor.firstName());
        assertEquals("GUINESS", actor.lastName());
    }

    @Test
    void toCategoryMapsFields() {
        Category category = EntityMapper.toCategory(new CategoryEntity(8, "Action"));

        assertEquals(8, category.id());
        assertEquals("Action", category.name());
    }

    @Test
    void toLanguageMapsFields() {
        Language result = EntityMapper.toLanguage(language);

        assertEquals(1, result.id());
        assertEquals("English", result.name());
    }

    @Test
    void toCountryMapsFields() {
        Country result = EntityMapper.toCountry(country);

        assertEquals(1, result.id());
        assertEquals("United States", result.country());
    }

    @Test
    void toCityMapsCountryNested() {
        City result = EntityMapper.toCity(city);

        assertEquals(2, result.id());
        assertEquals("Austin", result.city());
        assertEquals(1, result.country().id());
    }

    @Test
    void toAddressMapsCityNested() {
        Address result = EntityMapper.toAddress(address);

        assertEquals(3, result.id());
        assertEquals("47 MySakila Drive", result.address());
        assertEquals("Alberta", result.district());
        assertEquals("1400", result.postalCode());
        assertEquals("555", result.phone());
        assertEquals(2, result.city().id());
    }

    @Test
    void toStoreMapsManagerAndAddress() {
        Store result = EntityMapper.toStore(store);

        assertEquals(1, result.id());
        assertEquals(1, result.managerStaffId());
        assertEquals(3, result.address().id());
    }

    @Test
    void toStaffMapsFields() {
        Staff result = EntityMapper.toStaff(staff);

        assertEquals(1, result.id());
        assertEquals("MIKE", result.firstName());
        assertEquals("HILL", result.lastName());
        assertEquals("mike@sakila.com", result.email());
        assertEquals("mike", result.username());
        assertEquals(true, result.active());
    }

    @Test
    void toFilmMapsLanguageNested() {
        Film result = EntityMapper.toFilm(film);

        assertEquals(5, result.id());
        assertEquals("ACADEMY", result.title());
        assertEquals("desc", result.description());
        assertEquals(2006, result.releaseYear());
        assertEquals(1, result.language().id());
        assertEquals("English", result.language().name());
        assertEquals((short) 6, result.rentalDuration());
        assertEquals(BigDecimal.ONE, result.rentalRate());
        assertEquals((short) 86, result.length());
        assertEquals(BigDecimal.TEN, result.replacementCost());
        assertEquals("PG", result.rating());
    }

    @Test
    void toCustomerMapsStoreAndAddress() {
        Customer result = EntityMapper.toCustomer(customer);

        assertEquals(4, result.id());
        assertEquals("MARY", result.firstName());
        assertEquals("SMITH", result.lastName());
        assertEquals("mary@sakila.com", result.email());
        assertEquals(true, result.active());
        assertEquals(1, result.storeId());
        assertEquals(3, result.address().id());
        assertEquals(LocalDate.of(2026, 1, 10), result.createDate());
    }

    @Test
    void toCustomerWithNullStoreMapsNullStoreId() {
        CustomerEntity noStore = new CustomerEntity(4, "MARY", "SMITH", "mary@sakila.com", true, null, address,
                LocalDate.of(2026, 1, 10));

        assertNull(EntityMapper.toCustomer(noStore).storeId());
    }

    @Test
    void toInventoryMapsFilmAndStore() {
        InventoryEntity inventory = new InventoryEntity(9, film, store);

        Inventory result = EntityMapper.toInventory(inventory, true);

        assertEquals(9, result.id());
        assertEquals(5, result.filmId());
        assertEquals("ACADEMY", result.filmTitle());
        assertEquals(1, result.storeId());
        assertEquals(true, result.available());
    }

    @Test
    void toRentalMapsRelationsAndFilmTitle() {
        InventoryEntity inventory = new InventoryEntity(9, film, store);
        RentalEntity rental = new RentalEntity(10, Instant.parse("2026-08-01T10:00:00Z"), inventory, customer, staff);

        Rental result = EntityMapper.toRental(rental);

        assertEquals(10, result.id());
        assertEquals(Instant.parse("2026-08-01T10:00:00Z"), result.rentalDate());
        assertNull(result.returnDate());
        assertEquals(4, result.customerId());
        assertEquals(9, result.inventoryId());
        assertEquals("ACADEMY", result.filmTitle());
        assertEquals(1, result.staffId());
    }

    @Test
    void toRentalWithNullFilmTitleMapsNull() {
        InventoryEntity noFilm = new InventoryEntity(9, null, store);
        RentalEntity rental = new RentalEntity(10, Instant.parse("2026-08-01T10:00:00Z"), noFilm, customer, staff);

        assertNull(EntityMapper.toRental(rental).filmTitle());
    }

    @Test
    void toUserMapsFields() {
        User result = EntityMapper.toUser(new UserEntity(1L, "admin", "hash", "ADMIN", true));

        assertEquals(1L, result.id());
        assertEquals("admin", result.username());
        assertEquals("hash", result.password());
        assertEquals("ADMIN", result.role());
        assertEquals(true, result.enabled());
    }
}
