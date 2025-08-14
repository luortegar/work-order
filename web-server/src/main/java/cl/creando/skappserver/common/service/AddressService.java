package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.entity.address.Commune;
import cl.creando.skappserver.common.repository.address.CommuneRepository;
import cl.creando.skappserver.common.repository.address.CountryRepository;
import cl.creando.skappserver.common.repository.address.RegionRepository;
import cl.creando.skappserver.common.response.CommuneResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final CommuneRepository communeRepository;

    public List<CommuneResponse> autoCompleteCommune(String searchTerm) {
        List<Commune> communeList = communeRepository.findTop10ByNameContainingIgnoreCase(searchTerm);
        return communeList.stream().map(CommuneResponse::new).toList();
    }
}
