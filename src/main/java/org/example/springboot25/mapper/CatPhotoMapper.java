package org.example.springboot25.mapper;

import org.example.springboot25.dto.CatPhotoInputDTO;
import org.example.springboot25.dto.CatPhotoOutputDTO;
import org.example.springboot25.dto.CatPhotoUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.CatPhoto;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class CatPhotoMapper {

    public CatPhotoOutputDTO toDTO(CatPhoto catPhoto) {
        CatPhotoOutputDTO dto = new CatPhotoOutputDTO();
        dto.setId(catPhoto.getCatPhotoId());
        dto.setCatPhotoUrl(catPhoto.getCatPhotoUrl());
        dto.setCatPhotoCaption(catPhoto.getCatPhotoCaption());

        if (catPhoto.getCatPhotoCreatedAt() != null) {
            dto.setCatPhotoCreatedAt(catPhoto.getCatPhotoCreatedAt().atOffset(ZoneOffset.UTC));
        }

        dto.setCatPhotoCatId(catPhoto.getCatPhotoCat().getCatId());
        return dto;
    }

    public CatPhoto toEntityInput(CatPhotoInputDTO input, Cat cat) {
        CatPhoto catPhoto = new CatPhoto();
        catPhoto.setCatPhotoUrl(input.getCatPhotoUrl());
        catPhoto.setCatPhotoCaption(input.getCatPhotoCaption());
        catPhoto.setCatPhotoCat(cat);
        return catPhoto;
    }

    public CatPhoto toEntityUpdate(CatPhotoUpdateDTO input, Cat cat) {
        CatPhoto catPhoto = new CatPhoto();
        if (input.getCatPhotoUrl() != null) {
            catPhoto.setCatPhotoUrl(input.getCatPhotoUrl());
        }
        if (input.getCatPhotoCaption() != null) {
            catPhoto.setCatPhotoCaption(input.getCatPhotoCaption());
        }
        if (input.getCatPhotoCatId() != null) {
            catPhoto.setCatPhotoCat(cat);
        }
        return catPhoto;
    }
}
