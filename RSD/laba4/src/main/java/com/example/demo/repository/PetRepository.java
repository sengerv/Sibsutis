package com.example.demo.repository;

import com.example.demo.model.Pet;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {
    private final List<Pet> pets = new ArrayList<>();
    private long idCounter = 1;

    public List<Pet> findAll() {
        return pets;
    }

    public Optional<Pet> findById(Long id) {
        return pets.stream().filter(pet -> pet.getId().equals(id)).findFirst();
    }

    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            pet.setId(idCounter++);
        } else {
            pets.removeIf(p -> p.getId().equals(pet.getId()));
        }
        pets.add(pet);
        return pet;
    }

    public void deleteById(Long id) {
        pets.removeIf(pet -> pet.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return pets.stream()
                .anyMatch(pet -> pet.getId().equals(id));
    }
}