package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Pet;
import com.example.demo.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetControllerTest {
    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPetById() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Doggie");

        when(petService.getPetById(1L)).thenReturn(Optional.of(pet));

        ResponseEntity<Pet> response = petController.getPetById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doggie", response.getBody().getName());
        verify(petService, times(1)).getPetById(1L);
    }

    @Test
    void testGetPetByIdNotFound() {
        when(petService.getPetById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> petController.getPetById(1L));
        verify(petService, times(1)).getPetById(1L);
    }

    @Test
    void testAddPet() {
        Pet pet = new Pet();
        pet.setName("Doggie");

        when(petService.addPet(pet)).thenReturn(pet);

        ResponseEntity<Pet> response = petController.addPet(pet);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Doggie", response.getBody().getName());
        verify(petService, times(1)).addPet(pet);
    }

    @Test
    void testUpdatePet() {
        Pet pet = new Pet();
        pet.setName("Updated Doggie");

        when(petService.updatePet(pet)).thenReturn(pet);

        ResponseEntity<Pet> response = petController.updatePet( pet);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Doggie", response.getBody().getName());
        verify(petService, times(1)).updatePet(pet);
    }

    @Test
    void testUpdatePetNotFound() {
        Pet pet = new Pet();
        pet.setName("Updated Doggie");

        when(petService.updatePet(pet)).thenThrow(new ResourceNotFoundException("Pet not found with id: 1"));

        assertThrows(ResourceNotFoundException.class, () -> petController.updatePet(pet));
        verify(petService, times(1)).updatePet(pet);
    }

    @Test
    void testDeletePet() {
        doNothing().when(petService).deletePet(1L);

        ResponseEntity<Void> response = petController.deletePet(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(petService, times(1)).deletePet(1L);
    }

    @Test
    void testDeletePetNotFound() {
        doThrow(new ResourceNotFoundException("Pet not found with id: 1")).when(petService).deletePet(1L);

        assertThrows(ResourceNotFoundException.class, () -> petController.deletePet(1L));
        verify(petService, times(1)).deletePet(1L);
    }
}