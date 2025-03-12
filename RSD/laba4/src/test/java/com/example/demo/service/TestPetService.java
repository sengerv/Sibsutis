package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Pet;
import com.example.demo.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPets() {
        // Подготовка данных
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Doggie");

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Cattie");

        List<Pet> pets = Arrays.asList(pet1, pet2);

        // Мокируем вызов репозитория
        when(petRepository.findAll()).thenReturn(pets);

        // Вызов метода
        List<Pet> result = petService.getAllPets();

        // Проверки
        assertEquals(2, result.size());
        assertEquals("Doggie", result.get(0).getName());
        assertEquals("Cattie", result.get(1).getName());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    void testGetPetById() {
        // Подготовка данных
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Doggie");

        // Мокируем вызов репозитория
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        // Вызов метода
        Optional<Pet> result = petService.getPetById(1L);

        // Проверки
        assertTrue(result.isPresent());
        assertEquals("Doggie", result.get().getName());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPetByIdNotFound() {
        // Мокируем вызов репозитория
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызов метода
        Optional<Pet> result = petService.getPetById(1L);

        // Проверки
        assertFalse(result.isPresent());
        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    void testAddPet() {
        // Подготовка данных
        Pet pet = new Pet();
        pet.setName("Doggie");

        // Мокируем вызов репозитория
        when(petRepository.save(pet)).thenReturn(pet);

        // Вызов метода
        Pet createdPet = petService.addPet(pet);

        // Проверки
        assertEquals("Doggie", createdPet.getName());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testUpdatePet() {
        // Подготовка данных
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Doggie");

        // Мокируем вызов репозитория
        when(petRepository.existsById(1L)).thenReturn(true);
        when(petRepository.save(pet)).thenReturn(pet);

        // Вызов метода
        Pet updatedPet = petService.updatePet(pet);

        // Проверки
        assertEquals("Doggie", updatedPet.getName());
        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void testUpdatePetNotFound() {
        // Подготовка данных
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Doggie");

        // Мокируем вызов репозитория
        when(petRepository.existsById(1L)).thenReturn(false);

        // Проверка исключения
        assertThrows(ResourceNotFoundException.class, () -> petService.updatePet(pet));
        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, never()).save(pet);
    }

    @Test
    void testDeletePet() {
        // Мокируем вызов репозитория
        when(petRepository.existsById(1L)).thenReturn(true);
        doNothing().when(petRepository).deleteById(1L);

        // Вызов метода
        petService.deletePet(1L);

        // Проверки
        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePetNotFound() {
        // Мокируем вызов репозитория
        when(petRepository.existsById(1L)).thenReturn(false);

        // Проверка исключения
        assertThrows(ResourceNotFoundException.class, () -> petService.deletePet(1L));
        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, never()).deleteById(1L);
    }
}