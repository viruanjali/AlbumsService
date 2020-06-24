package com.amsidh.mvc.service;

import com.amsidh.mvc.data.AlbumEntity;

import java.util.List;

public interface AlbumsService {
    List<AlbumEntity> getAlbums(String userId);
}
