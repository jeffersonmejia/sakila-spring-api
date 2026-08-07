package com.sakila.api.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sakila.api.adapter.out.persistence.entity.PaymentEntity;
import com.sakila.api.domain.model.ActiveRentalsByStore;
import com.sakila.api.domain.model.MostRentedFilm;
import com.sakila.api.domain.model.RentalsByMonth;
import com.sakila.api.domain.model.RevenueByCategory;
import com.sakila.api.domain.model.TopCustomer;

public interface JpaReportRepository extends JpaRepository<PaymentEntity, Integer> {

    @Query("""
            select new com.sakila.api.domain.model.MostRentedFilm(r.inventory.film.id, r.inventory.film.title, count(r))
            from RentalEntity r
            group by r.inventory.film.id, r.inventory.film.title
            order by count(r) desc
            """)
    List<MostRentedFilm> mostRentedFilms(Pageable pageable);

    @Query("""
            select new com.sakila.api.domain.model.TopCustomer(
                r.customer.id, concat(r.customer.firstName, ' ', r.customer.lastName), count(r))
            from RentalEntity r
            group by r.customer.id, r.customer.firstName, r.customer.lastName
            order by count(r) desc
            """)
    List<TopCustomer> topCustomers(Pageable pageable);

    @Query("""
            select new com.sakila.api.domain.model.RevenueByCategory(c.name, sum(p.amount))
            from PaymentEntity p
            join RentalEntity r on p.rentalId = r.id
            join r.inventory.film f
            join f.categories c
            group by c.name
            order by sum(p.amount) desc
            """)
    List<RevenueByCategory> revenueByCategory();

    @Query("""
            select new com.sakila.api.domain.model.RentalsByMonth(
                cast(function('to_char', r.rentalDate, 'YYYY-MM') as string), count(r))
            from RentalEntity r
            group by function('to_char', r.rentalDate, 'YYYY-MM')
            order by function('to_char', r.rentalDate, 'YYYY-MM')
            """)
    List<RentalsByMonth> rentalsByMonth();

    @Query("""
            select new com.sakila.api.domain.model.ActiveRentalsByStore(r.inventory.store.id, count(r))
            from RentalEntity r
            where r.returnDate is null
            group by r.inventory.store.id
            order by count(r) desc
            """)
    List<ActiveRentalsByStore> activeRentalsByStore();
}
