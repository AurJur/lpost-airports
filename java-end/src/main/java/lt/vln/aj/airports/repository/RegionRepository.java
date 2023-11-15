package lt.vln.aj.airports.repository;

import lt.vln.aj.airports.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {

    List<RegionEntity> findAllByIsoCountry(String isoCountry);
}
