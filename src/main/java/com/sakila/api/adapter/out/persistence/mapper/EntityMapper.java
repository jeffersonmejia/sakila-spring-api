package com.sakila.api.adapter.out.persistence.mapper;

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

public final class EntityMapper {

    private EntityMapper() {
    }

    public static Actor toActor(ActorEntity e) {
        return new Actor(e.getId(), e.getFirstName(), e.getLastName());
    }

    public static Category toCategory(CategoryEntity e) {
        return new Category(e.getId(), e.getName());
    }

    public static Language toLanguage(LanguageEntity e) {
        return new Language(e.getId(), e.getName());
    }

    public static Country toCountry(CountryEntity e) {
        return new Country(e.getId(), e.getCountry());
    }

    public static City toCity(CityEntity e) {
        return new City(e.getId(), e.getCity(), toCountry(e.getCountry()));
    }

    public static Address toAddress(AddressEntity e) {
        return new Address(e.getId(), e.getAddress(), e.getDistrict(), e.getPostalCode(), e.getPhone(),
                toCity(e.getCity()));
    }

    public static Store toStore(StoreEntity e) {
        return new Store(e.getId(), e.getManagerStaffId(), toAddress(e.getAddress()));
    }

    public static Staff toStaff(StaffEntity e) {
        return new Staff(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getUsername(), e.getActive());
    }

    public static Film toFilm(FilmEntity e) {
        return new Film(e.getId(), e.getTitle(), e.getDescription(), e.getReleaseYear(),
                toLanguage(e.getLanguage()), e.getRentalDuration(), e.getRentalRate(), e.getLength(),
                e.getReplacementCost(), e.getRating());
    }

    public static Customer toCustomer(CustomerEntity e) {
        return new Customer(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(), e.getActive(),
                e.getStore() != null ? e.getStore().getId() : null, toAddress(e.getAddress()), e.getCreateDate());
    }

    public static Inventory toInventory(InventoryEntity e, boolean available) {
        return new Inventory(e.getId(), e.getFilm().getId(), e.getFilm().getTitle(), e.getStore().getId(), available);
    }

    public static Rental toRental(RentalEntity e) {
        return new Rental(e.getId(), e.getRentalDate(), e.getReturnDate(), e.getCustomer().getId(),
                e.getInventory().getId(),
                e.getInventory().getFilm() != null ? e.getInventory().getFilm().getTitle() : null,
                e.getStaff().getId());
    }

    public static User toUser(UserEntity e) {
        return new User(e.getId(), e.getUsername(), e.getPassword(), e.getRole(), e.getEnabled());
    }
}
