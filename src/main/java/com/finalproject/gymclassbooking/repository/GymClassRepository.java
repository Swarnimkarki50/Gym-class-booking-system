package com.finalproject.gymclassbooking.repository;

import com.finalproject.gymclassbooking.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {

    List<GymClass> findByActiveTrueOrderByStartsAtAscNameAsc();

    @Query("""
            select c from GymClass c
            where c.active = true
              and (
                lower(c.name) like lower(concat('%', :keyword, '%')) or
                lower(c.instructor) like lower(concat('%', :keyword, '%')) or
                lower(c.category) like lower(concat('%', :keyword, '%')) or
                lower(c.description) like lower(concat('%', :keyword, '%'))
              )
            order by c.startsAt asc, c.name asc
            """)
    List<GymClass> searchActive(@Param("keyword") String keyword);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
            update GymClass c
            set c.price = c.price * :conversionRate
            where c.price is not null and c.price < :wonThreshold
            """)
    int convertLegacyPricesToWon(
            @Param("wonThreshold") BigDecimal wonThreshold,
            @Param("conversionRate") BigDecimal conversionRate
    );
}
