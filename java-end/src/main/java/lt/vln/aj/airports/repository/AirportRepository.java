package lt.vln.aj.airports.repository;

import lt.vln.aj.airports.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, Integer> {

    List<AirportEntity> findAllByIsoRegion(String regionCode);
}
